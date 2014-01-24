package boar401s2.marktime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.handlers.Section;
import boar401s2.marktime.handlers.Squad;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.OfflineSpreadsheet;
import boar401s2.marktime.storage.spreadsheet.parsers.TableParser;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

//TODO LIST
//Write code to duplicate worksheet 10min - Done
//Write Table Parser (2D array) 30min
//Write code to save marking data to worksheet 15min
//Write code to shift that worksheet online 30min

public class Roll extends FragmentActivity implements
		ActionBar.OnNavigationListener, AsyncTaskParent{
	
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	public String selectedSection;
	
	Company company;
	Section section;
	Squad squad;
	List<String> squads;
	CharSequence[] sectionChoices;
	Activity activity = this;
	
	OfflineSpreadsheet spreadsheet;
	Worksheet worksheet;
	TableParser parser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		showSectionSelector();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roll);
		
		
		spreadsheet = new OfflineSpreadsheet(MarkTime.settings.getString("spreadsheet", ""));
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.roll, menu);
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

	@Override
	public boolean onNavigationItemSelected(int position, long id) { //Show fragment when item selected
		Fragment fragment = new BoysInSquadFragment();
		
		Bundle args = new Bundle();
		args.putString("selection", squads.get(position));
		fragment.setArguments(args);
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}

	class BoysInSquadFragment extends Fragment {

		List<Map<String, String>> boysNames = new ArrayList<Map<String, String>>();
		
		public BoysInSquadFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_roll_dummy,
					container, false);
			
			ListView boysListView = (ListView) rootView.findViewById(R.id.roll_list);
			//Add in code to populate boysNames
			squad = section.getSquad(getArguments().getString("selection"));
			for(String boy: squad.getBoysNames()){
				boysNames.add(createBoyEntry(boy));
			}
			boysNames.add(createBoyEntry("Create new boy..."));
			
			//Populate List
			SimpleAdapter simpleAdpt = new SimpleAdapter(activity, boysNames, android.R.layout.simple_list_item_1, new String[] {"boy"}, new int[] {android.R.id.text1});
			boysListView.setAdapter(simpleAdpt);
			
			//Add item listener
			boysListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			     public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
			                             long id) {
			         onItemSelected(position);
			     }
			});
			return rootView;
		}
		
		public void onItemSelected(int position){
			String name = squad.getBoys().get(position).getName();
			Intent i = new Intent(activity, MarkBoy.class);
			i.putExtra("name", name);
			startActivityForResult(i, 1);
			//Start intent boy activity
		}
		
		public HashMap<String, String> createBoyEntry(String name){
			HashMap<String, String> entry = new HashMap<String, String>();
			entry.put("boy", name);
			return entry;
		}
	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		spreadsheet.load(MarkTime.activity.getFilesDir()+"/"+spreadsheet.getName()+".db");
		System.out.println("Updating spreadsheet...");
		String worksheetName = squad.getName()+" - "+getDate();
		if (!spreadsheet.worksheetExists(worksheetName)){
			System.out.println("Duplicating!");
			spreadsheet.duplicateSheet("Night Template", worksheetName);
		}
		worksheet = spreadsheet.getWorksheet(worksheetName);
		parser = new TableParser(worksheet);
		
		Bundle extras = data.getExtras();
		String name = extras.getString("name");
		parser.setValue(name, "Church", extras.getString("church"));
		parser.setValue(name, "Hat", extras.getString("hat"));
		parser.setValue(name, "Tie", extras.getString("tie"));
		parser.setValue(name, "Havasac", extras.getString("havasac"));
		parser.setValue(name, "Badges", extras.getString("badges"));
		parser.setValue(name, "Belt", extras.getString("belt"));
		parser.setValue(name, "Pants", extras.getString("pants"));
		parser.setValue(name, "Socks", extras.getString("socks"));
		parser.setValue(name, "Shoes", extras.getString("shoes"));
		parser.setValue(name, "Attendance", Integer.toString(extras.getInt("attendance")));
		System.out.println(parser.getValue(name, "Shoes"));
		spreadsheet.save(MarkTime.activity.getFilesDir()+"/"+spreadsheet.getName()+".db");
		System.out.println("Saved!");
	}
	
	public void showSectionSelector(){
		company = new Company(this); //Put up a list asking between sections
		List<String> sections = company.getSectionNames();
		sectionChoices = sections.toArray(new CharSequence[sections.size()]);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Section to Mark");
        builder.setItems(sectionChoices, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	populateSquadList(item);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	public void populateSquadList(int item){
		String choice = (String) sectionChoices[item];
		section = company.getSection(choice);
		squads = section.getSquadNames();
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setListNavigationCallbacks(
				new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,
						android.R.id.text1, squads), this);
	}

	/**
	 * Returns current date
	 */
	public String getDate(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
		return sdf.format(date);
		
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
