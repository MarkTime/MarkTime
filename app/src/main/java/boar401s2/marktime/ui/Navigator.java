package boar401s2.marktime.ui;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.R;
import boar401s2.marktime.constants.LevelIDList;
import boar401s2.marktime.constants.ListViewEntryTypes;
import boar401s2.marktime.constants.ResultIDList;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.handlers.Boy;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.handlers.Section;
import boar401s2.marktime.handlers.Squad;
import boar401s2.marktime.storage.database.Database;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.InputType;

public class Navigator extends Activity implements AsyncTaskParent{
	
	private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    
    Company company;
    Section section;
    Squad squad;
    
    List<String> entries = new ArrayList<String>();
    ListView listView;
    ListAdapter adapter;
    
    Integer currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigator);
		setupActionBar();
		start();
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//==========[Starting/Stopping stuff]==========//
	
	public void displayLevel(Integer level){
		if(level == LevelIDList.SECTION){
			currentLevel = level;
			setTitle("Company");
			clearItems();
			
			addHeader("Section");
			for(String section: company.getSectionNames()){
				addItem("Section", section, ListViewEntryTypes.BUTTON);
			}
			
			addHeader("Settings");
			addItem("Settings", "Create new Section...", ListViewEntryTypes.BUTTON);
			
			updateListView();
		} else if(level == LevelIDList.SQUAD){
			currentLevel = level;
			setTitle(section.getName());
			clearItems();
			
			addHeader("Squad");
			for(String squad: section.getSquadNames()){
				addItem("Squad", squad, ListViewEntryTypes.BUTTON);
			}
			
			addHeader("Settings");
			addItem("Settings", "Create new Squad...", ListViewEntryTypes.BUTTON);
			addItem("Settings", "Delete Section...", ListViewEntryTypes.BUTTON);
			
			updateListView();
		} else if(level == LevelIDList.BOY){
			currentLevel = level;
			setTitle(squad.getName());
			clearItems();
			
			addHeader("Boy");
			for(String boy: squad.getBoysNames()){
				addItem("Boy", boy, ListViewEntryTypes.BUTTON);
			}
			
			addHeader("Settings");
			addItem("Settings", "Create new Boy...", ListViewEntryTypes.BUTTON);
			addItem("Settings", "Delete Squad...", ListViewEntryTypes.BUTTON);
			
			updateListView();
		}
	}
			
	public void start(){
		company = new Company(this);

		adapter = new ListAdapter(this);
		listView = (ListView)findViewById(R.id.navigator_list_view);
		listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
				onItemClicked(entries.get(position).split(",")[0]);
			}
		});
        
        Intent i = getIntent();
        if(i.hasExtra("location")){
        	String location = i.getExtras().getString("location");
        	String locationCopy = location;
        	int count = locationCopy.length() - locationCopy.replace(".", "").length();
        	if(count==0){
        		section = company.getSection(location);
        		displayLevel(LevelIDList.SQUAD);
        	} else if(count==1){
        		section = company.getSection(location.split("\\.")[0]);
        		squad = company.getSection(location.split("\\.")[0]).getSquad(location.split("\\.")[1]);
        		displayLevel(LevelIDList.BOY);
        	}
        } else {
        	displayLevel(LevelIDList.SECTION);
        }
	}
	
	public void stop(){
		finish();
	}
	
	//==========[List Settings]==========//
	
	public void addHeader(String header){
		entries.add(header+","+ListViewEntryTypes.HEADER);
	}
	
	public void addItem(String header, String title, Integer type){
		entries.add(header+":"+title+","+String.valueOf(type));
	}
	
	public void clearItems(){
		entries.clear();
	}
	
	public void updateListView(){
		adapter.notifyDataSetChanged();
	}
	
	//==========[Events]==========//

	public void onItemLongClicked(String id){
		System.out.println("Long clicked "+id);
	}
	
	public void onItemClicked(String id){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setCancelable(false);
		
		if(currentLevel == LevelIDList.SECTION){
			if(id.startsWith("Section")){
				section = company.getSection(id.split(":")[1]);
				displayLevel(LevelIDList.SQUAD);
			} else {
				if(id.split(":")[1].startsWith("Create")){
					
					builder.setTitle("Create Section");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							company.addSection(input.getText().toString().trim());
							//company.addSection_(input.getText().toString().trim());
							displayLevel(currentLevel);
					    }
					});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					builder.show();
					
				}
			}
		} else if(currentLevel == LevelIDList.SQUAD){
			if(id.startsWith("Squad")){
				squad = section.getSquad(id.split(":")[1]);
				displayLevel(LevelIDList.BOY);
			} else {
				if(id.split(":")[1].startsWith("Create")){
					
					builder.setTitle("Create Squad");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        section.addSquad(input.getText().toString().trim());
                            //section.addSquad_(input.getText().toString().trim());
                            displayLevel(currentLevel);
					    }
					});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        dialog.cancel();
					    }
					});
					builder.show();
					
				} else if(id.split(":")[1].startsWith("Delete")){
					
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        switch (which){
					        case DialogInterface.BUTTON_POSITIVE:
					        	company.deleteSection(section.getName());
					        	displayLevel(LevelIDList.SECTION);
					            break;
					        case DialogInterface.BUTTON_NEGATIVE:
					            break;
					        }
					    }
					};

					AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(this);
					confirmDeleteDialog.setMessage("Are you sure you want to remove "+section.getName()+" permanantly?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();
					
				} else {
					Toast.makeText(this, "Not implemented yet.", Toast.LENGTH_SHORT).show();
				}
			}
		} else if(currentLevel == LevelIDList.BOY){
			if(id.startsWith("Boy")){
				Intent i = new Intent();
				Boy boy = squad.getBoy(id.split(":")[1]);
				i.putExtra("boy", boy.getSection().getName()+":"+boy.getSquad().getName()+":"+boy.getName());
				setResult(ResultIDList.RESULT_OK, i);
				stop();
			} else {
				if(id.split(":")[1].startsWith("Create")){
					
					builder.setTitle("Create Boy");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        squad.addBoy(input.getText().toString().trim());
					        displayLevel(currentLevel);
					    }
					});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        dialog.cancel();
					    }
					});
					builder.show();
					
				} else if (id.split(":")[1].startsWith("Delete")){
					
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        switch (which){
					        case DialogInterface.BUTTON_POSITIVE:
					        	section.deleteSquad(squad.getName());
					        	section.getCompany().saveAttendance();
					        	displayLevel(LevelIDList.SQUAD);
					            break;
					        case DialogInterface.BUTTON_NEGATIVE:
					            break;
					        }
					    }
					};

					AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(this);
					confirmDeleteDialog.setMessage("Are you sure you want to remove "+squad.getName()+" permanantly?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();
					
				} else {
					Toast.makeText(this, "Not implemented yet.", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		if(currentLevel==LevelIDList.SECTION){
			super.onBackPressed();
		} else if(currentLevel==LevelIDList.SQUAD){
			displayLevel(LevelIDList.SECTION);
		} else if(currentLevel==LevelIDList.BOY){
			displayLevel(LevelIDList.SQUAD);
		}
	}
	
	//==========[Adapter]==========//
	
	private class ListAdapter extends BaseAdapter {
        private final Context mContext;
        
        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return entries.size();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	String headerName = null;
        	String entryName = null;
        	String subtitleText = "";
        	if(entries.get(position).split(",")[0].contains(":")){
        		headerName = entries.get(position).split(",")[0].split(":")[0];
        		entryName = entries.get(position).split(",")[0].split(":")[1];
        	} else {
        		headerName = entries.get(position).split(",")[0];
        	}
        	if(entries.get(position).split(",").length==3){
        		subtitleText = entries.get(position).split(",")[2];
        	}
        	Integer type = Integer.parseInt(entries.get(position).split(",")[1]);
        	
        	
            if(type == ListViewEntryTypes.HEADER) { //Am I a header?
                View item = convertView;
                if(convertView == null || convertView.getTag() == LIST_ITEM) {

                    item = LayoutInflater.from(mContext).inflate(
                            R.layout.lv_header_layout, parent, false);
                    item.setTag(LIST_HEADER);

                }

                TextView headerTextView = (TextView)item.findViewById(R.id.lv_title);
                headerTextView.setText(headerName);
                return item;
            } else if(type == ListViewEntryTypes.BUTTON){ //Am I a button?
	            View item = convertView;
	            if(convertView == null || convertView.getTag() == LIST_HEADER) {
	                item = LayoutInflater.from(mContext).inflate(
	                        R.layout.lv_layout, parent, false);
	                item.setTag(LIST_ITEM);
	            }
            	
            	TextView header = (TextView)item.findViewById(R.id.lv_title);
	            header.setText(entryName);
	            return item;
            } else if(type == ListViewEntryTypes.BUTTON_WITH_SUB){
            	View item = convertView;
	            if(convertView == null || convertView.getTag() == LIST_HEADER) {
	                item = LayoutInflater.from(mContext).inflate(
	                        R.layout.lv_layout, parent, false);
	                item.setTag(LIST_ITEM);
	            }
            	
            	TextView header = (TextView)item.findViewById(R.id.lv_title);
	            header.setText(entryName);
	            
	            TextView subtext = (TextView)item.findViewById(R.id.lv_subtext);
	            subtext.setText(subtitleText);
	            return item;
            }
            return null;
        }
	}

	@Override
	public void onStatusChange(String status) {}

	@Override
	public void openProgressDialog(String message) {}

	@Override
	public void closeProgressDialog() {}

	@Override
	public void onPostExecute(Integer taskid, Integer result) {}

	@Override
	public void onPreExecute() {}
}
