package boar401s2.marktime.ui;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.R;
import boar401s2.marktime.constants.LevelIDList;
import boar401s2.marktime.constants.ResultIDList;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.handlers.Boy;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.util.MarkingData;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class BoyUI extends Activity implements AsyncTaskParent {

	List<ListViewEntry> entries_ = new ArrayList<ListViewEntry>();
	ListView listView;
    ListAdapter adapter;
    Integer currentLevel = -1;
    Boy boy;
    String date = MarkingData.getDate();
    
    boolean dataChanged = false;
    MarkingData data = new MarkingData();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boy);
		setupActionBar();
		String section = getIntent().getExtras().getString("boy").split(":")[0];
		String squad = getIntent().getExtras().getString("boy").split(":")[1];
		String name = getIntent().getExtras().getString("boy").split(":")[2];
		boy = new Company(this).getSection(section).getSquad(squad).getBoy(name);
		start();
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.boy, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			navigateUp();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		navigateUp();
	}
	
	public void navigateUp(){
		if(currentLevel==LevelIDList.BOY_MAIN){
			Intent i = new Intent();
			i.putExtra("location", boy.getSection().getName()+"."+boy.getSquad().getName());
			setResult(ResultIDList.RESULT_UP, i);
			finish();
		} else if(currentLevel==LevelIDList.BOY_MARK){
			displayLevel(LevelIDList.BOY_MAIN);
		}
	}
	
	public void displayLevel(Integer level){
		if(level == LevelIDList.BOY_MAIN){
			currentLevel = level;
			setTitle(boy.getName());
			clearItems();
			
			addHeader("Attendance");
			addItem("Mark", ListViewEntryTypes.BUTTON);
			addItem("Browse Attendance", ListViewEntryTypes.BUTTON);
			
			addHeader("Register");
			addItem("General", ListViewEntryTypes.BUTTON);
			addItem("Activity Badges", ListViewEntryTypes.BUTTON);
			addItem("Special Badges & Awards", ListViewEntryTypes.BUTTON);
			addItem("Uniform", ListViewEntryTypes.BUTTON);
			addItem("Contact Information", ListViewEntryTypes.BUTTON);
			
			addHeader("Settings");
			addItem("Remove from "+boy.getSquad().getName(), ListViewEntryTypes.BUTTON);
			
			updateListView();
		} else if(level == LevelIDList.BOY_MARK){
			currentLevel = level;
			setTitle("Marking: "+boy.getName());
			clearItems();
			
			addHeader("Uniform");
			addItem("Hat", ListViewEntryTypes.CHECKBOX);
			addItem("Tie", ListViewEntryTypes.CHECKBOX);
			addItem("Havasac", ListViewEntryTypes.CHECKBOX);
			addItem("Badges", ListViewEntryTypes.CHECKBOX);
			addItem("Belt", ListViewEntryTypes.CHECKBOX);
			addItem("Pants", ListViewEntryTypes.CHECKBOX);
			addItem("Socks", ListViewEntryTypes.CHECKBOX);
			addItem("Shoes", ListViewEntryTypes.CHECKBOX);
			
			addHeader("Other");
			addItem("Church", ListViewEntryTypes.CHECKBOX);
			addItem("Attendance", ListViewEntryTypes.BUTTON);
			addItem("Date to Mark", MarkingData.getDate());
			addItem("Submit", ListViewEntryTypes.SUBMIT);
			
			updateListView();
		} else if(level == LevelIDList.BOY){

		}
	}
    
    public void start(){
		adapter = new ListAdapter(this);
		listView = (ListView)findViewById(R.id.boy_list_view);
		listView.setAdapter(adapter);
		
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		     public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
		    	 onItemClicked(entries_.get(position));
		     }
		});
        
        displayLevel(LevelIDList.BOY_MAIN);
	}
    
    public void showAttendanceDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final NumberPicker picker = new NumberPicker(this);
		picker.setMinValue(0);
		picker.setMaxValue(3);
		picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		builder.setView(picker);
		builder.setCancelable(false);
		builder.setTitle("Attendance Score");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	dataChanged = true;
		    	data.attendance = picker.getValue();
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
    
    public void stop(){
		finish();
	}
    
    public void addHeader(String text){
    	entries_.add(new ListViewEntry(ListViewEntryTypes.HEADER, text));
	}
	
	public void addItem(String title, Integer type){
		entries_.add(new ListViewEntry(type, title));
	}
	
	public void addItem(String title, String sub){
		ListViewEntry entry = new ListViewEntry(ListViewEntryTypes.BUTTON_WITH_SUB, title);
		entry.setExtra("subtitle", sub);
		entries_.add(entry);
	}
	
	public void clearItems(){
		entries_.clear();
	}
	
	public void updateListView(){
		adapter.notifyDataSetChanged();
	}
	
	public void onItemClicked(final ListViewEntry entry){
		if(currentLevel == LevelIDList.BOY_MARK && entry.getTypeID()==ListViewEntryTypes.CHECKBOX){
			dataChanged = true;
		}
		
		if(currentLevel == LevelIDList.BOY_MAIN){
			if(entry.getTitle().startsWith("Mark")){
		       displayLevel(LevelIDList.BOY_MARK);
			} else if(entry.getTitle().startsWith("Remove")){
				
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	boy.getSquad().removeBoy(boy.getName());
				        	boy.getCompany().saveAttendance();
				        	navigateUp();
				            break;
				        case DialogInterface.BUTTON_NEGATIVE:
				            break;
				        }
				    }
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Are you sure you want to remove "+boy.getName()+
						" from "+boy.getSquad().getName()+"?").setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("No", dialogClickListener).show();
				
			}else {
				Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT).show();
			}
		} else if(currentLevel == LevelIDList.BOY_MARK){
			if(entry.getTitle().startsWith("Attendance")){
				showAttendanceDialog();
			} else if(entry.getTitle().startsWith("Date")){	
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				final EditText input = new EditText(this);
				input.setText((String) entry.getExtra("subtitle"));
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				builder.setView(input);
				builder.setCancelable(false);
				builder.setTitle("Set Date to Mark (dd/mm)");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        data.date = input.getText().toString().trim();
				        entry.setExtra("subtitle", data.date);
				        updateListView();
				    }
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        dialog.cancel();
				    }
				});
				builder.show();
				
			} else if(entry.getTitle().startsWith("Hat")){
				data.hat = (Boolean) entry.getExtra("checkbox");
			} else if(entry.getTitle().startsWith("Tie")){
				data.tie = (Boolean) entry.getExtra("checkbox");
			} else if(entry.getTitle().startsWith("Havasac")){
				data.havasac = (Boolean) entry.getExtra("checkbox");
			} else if(entry.getTitle().startsWith("Badges")){
				data.badges = (Boolean) entry.getExtra("checkbox");
			} else if(entry.getTitle().startsWith("Belt")){
				data.belt = (Boolean) entry.getExtra("checkbox");
			} else if(entry.getTitle().startsWith("Pants")){
				data.pants = (Boolean) entry.getExtra("checkbox");
			} else if(entry.getTitle().startsWith("Socks")){
				data.socks = (Boolean) entry.getExtra("checkbox");
			} else if(entry.getTitle().startsWith("Shoes")){
				data.shoes = (Boolean) entry.getExtra("checkbox");
			} else if(entry.getTitle().startsWith("Church")){
				data.church = (Boolean) entry.getExtra("checkbox");
				
				
			} else if(entry.getTitle().startsWith("Submit")){
				if(dataChanged){
					data.date = date;
					boy.setNightData(data);
				}
				displayLevel(LevelIDList.BOY_MAIN);
			}
		} else if(currentLevel == LevelIDList.BOY){
		}
	}
	
	private class ListAdapter extends BaseAdapter {
        private final Context mContext;
        
        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return entries_.size();
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
        	final ListViewEntry entry = entries_.get(position); 	

        	View item = convertView;
        	item = LayoutInflater.from(mContext).inflate(entry.getViewID(), parent, false);
        	TextView headerTextView = (TextView) item.findViewById(R.id.lv_title);
        	headerTextView.setText(entry.getTitle());
        	
        	if(entry.getTypeID()==ListViewEntryTypes.BUTTON_WITH_SUB){
        		TextView subTextView = (TextView) item.findViewById(R.id.lv_subtext);
        		subTextView.setText((String) entry.getExtra("subtitle"));
        	}
        	
        	if(entry.getTypeID() == ListViewEntryTypes.CHECKBOX){
        		final CheckBox check = (CheckBox) item.findViewById(R.id.lv_checkbox);
        		check.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						entry.setExtra("checkbox", check.isChecked());
						onItemClicked(entry);
					}
        		});
        		
        		if(entry.getTitle().startsWith("Hat")){
        			check.setChecked(data.hat);
        		} else if(entry.getTitle().startsWith("Tie")){
        			check.setChecked(data.tie);
        		} else if(entry.getTitle().startsWith("Havasac")){
        			check.setChecked(data.havasac);
        		} else if(entry.getTitle().startsWith("Badges")){
        			check.setChecked(data.badges);
        		} else if(entry.getTitle().startsWith("Belt")){
        			check.setChecked(data.belt);
        		} else if(entry.getTitle().startsWith("Pants")){
        			check.setChecked(data.pants);
        		} else if(entry.getTitle().startsWith("Socks")){
        			check.setChecked(data.socks);
        		} else if(entry.getTitle().startsWith("Shoes")){
        			check.setChecked(data.shoes);
        		} else if(entry.getTitle().startsWith("Church")){
        			check.setChecked(data.church);
        		}
        		
        		
        	}
        	
        	return item;
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
	public void onPreExecute() {
		
	}
	
}
