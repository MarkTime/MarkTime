package boar401s2.marktime;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.constants.LevelIDList;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.handlers.Boy;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.ui.ListViewEntry;
import boar401s2.marktime.ui.ListViewEntryTypes;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class BoyUI extends Activity implements AsyncTaskParent {
	
	List<String> entries = new ArrayList<String>();
	List<ListViewEntry> entries_ = new ArrayList<ListViewEntry>();
	ListView listView;
    ListAdapter adapter;
    Integer currentLevel = -1;
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
		    	System.out.println(picker.getValue());
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
	
	public void clearItems(){
		entries_.clear();
	}
	
	public void updateListView(){
		adapter.notifyDataSetChanged();
	}
	
	public void onItemClicked(ListViewEntry entry){
		if(currentLevel == LevelIDList.BOY_MAIN){
			if(entry.getTitle().startsWith("Mark")){
		       displayLevel(LevelIDList.BOY_MARK);
			}
		} else if(currentLevel == LevelIDList.BOY_MARK){
			if(entry.getTitle().startsWith("Attendance")){
				showAttendanceDialog();
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
        public View getView(int position, View convertView, ViewGroup parent) { //TODO: Clean this up a bit
        	ListViewEntry entry = entries_.get(position); 	
        	
        	View item = convertView;
        	item = LayoutInflater.from(mContext).inflate(entry.getViewID(), parent, false);
        	TextView headerTextView = (TextView) item.findViewById(R.id.lv_title);
        	headerTextView.setText(entry.getTitle());
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
	public void onPreExecute() {}

}
