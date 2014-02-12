package boar401s2.marktime.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.R;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.handlers.Section;
import boar401s2.marktime.handlers.Squad;
import boar401s2.marktime.ui.ListViewEntryTypes;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.navigator, menu);
		return true;
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
			
			addHeader("Section");
			for(String section: company.getSectionNames()){
				addItem("Section", section, ListViewEntryTypes.BUTTON);
			}
			
			addHeader("Settings");
			addItem("Settings", "Create new Section...", ListViewEntryTypes.BUTTON);
			
		} else if(level == LevelIDList.SQUAD){
			currentLevel = level;
			setTitle(section.getName());
			
		} else if(level == LevelIDList.BOY){
			currentLevel = level;
			setTitle(squad.getName());
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
        
        displayLevel(LevelIDList.SECTION);
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
		//adapter.notifyDataSetChanged();
	}
	
	//==========[Events]==========//
	
	public void onItemClicked(String id){
		if(currentLevel == LevelIDList.SECTION){
			
		} else if(currentLevel == LevelIDList.SQUAD){

		} else if(currentLevel == LevelIDList.BOY){

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

                TextView headerTextView = (TextView)item.findViewById(R.id.lv_list_hdr);
                headerTextView.setText(headerName);
                return item;
            } else if(type == ListViewEntryTypes.BUTTON){ //Am I a button?
	            View item = convertView;
	            if(convertView == null || convertView.getTag() == LIST_HEADER) {
	                item = LayoutInflater.from(mContext).inflate(
	                        R.layout.lv_layout, parent, false);
	                item.setTag(LIST_ITEM);
	            }
            	
            	TextView header = (TextView)item.findViewById(R.id.lv_item_header);
	            header.setText(entryName);
	            return item;
            } else if(type == ListViewEntryTypes.BUTTON_WITH_SUB){
            	View item = convertView;
	            if(convertView == null || convertView.getTag() == LIST_HEADER) {
	                item = LayoutInflater.from(mContext).inflate(
	                        R.layout.lv_layout, parent, false);
	                item.setTag(LIST_ITEM);
	            }
            	
            	TextView header = (TextView)item.findViewById(R.id.lv_item_header);
	            header.setText(entryName);
	            
	            TextView subtext = (TextView)item.findViewById(R.id.lv_item_subtext);
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
