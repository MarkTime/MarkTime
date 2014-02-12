package boar401s2.marktime;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.constants.LevelIDList;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.handlers.Boy;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.ui.ListViewEntryTypes;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.text.InputType;

public class BoyUI extends Activity implements AsyncTaskParent {

	private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
	
	List<String> entries = new ArrayList<String>();
	ListView listView;
    ListAdapter adapter;
    Integer currentLevel;
    Boy boy;
	
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void displayLevel(Integer level){
		if(level == LevelIDList.BOY_MAIN){
			setTitle(boy.getName());
			clearItems();
			
			addHeader("Attendance");
			addItem("Attendance", "Mark", ListViewEntryTypes.BUTTON);
			addItem("Attendance", "Browse Attendance", ListViewEntryTypes.BUTTON);
			
			addHeader("Register");
			addItem("Register", "General", ListViewEntryTypes.BUTTON);
			addItem("Register", "Activity Badges", ListViewEntryTypes.BUTTON);
			addItem("Register", "Special Badges & Awards", ListViewEntryTypes.BUTTON);
			addItem("Register", "Uniform", ListViewEntryTypes.BUTTON);
			addItem("Register", "Contact Information", ListViewEntryTypes.BUTTON);
			
			updateListView();
		} else if(level == LevelIDList.BOY){

		} else if(level == LevelIDList.BOY){

		}
	}
    
    public void start(){
		adapter = new ListAdapter(this);
		listView = (ListView)findViewById(R.id.boy_list_view);
		listView.setAdapter(adapter);
		
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		     public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
		    	 onItemClicked(entries.get(position).split(",")[0]);
		     }
		});
        
        displayLevel(LevelIDList.BOY_MAIN);
	}
    
    public void stop(){
		finish();
	}
    
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
	
	public void onItemClicked(String id){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setCancelable(false);
		
		if(currentLevel == LevelIDList.SECTION){
		} else if(currentLevel == LevelIDList.SQUAD){
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
