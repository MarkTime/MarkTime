package boar401s2.marktime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.handlers.Section;
import boar401s2.marktime.handlers.Squad;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		showSectionSelector();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roll);
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
		System.out.println("positions!");
		System.out.println(position);
		System.out.println(squads.get(position));
		args.putString("selection", squads.get(position));
		fragment.setArguments(args);
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}

	class BoysInSquadFragment extends Fragment {

		List<Map<String, String>> boysNames = new ArrayList<Map<String, String>>();
		Squad squad;
		
		public BoysInSquadFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_roll_dummy,
					container, false);
			
			ListView boysListView = (ListView) rootView.findViewById(R.id.roll_list);
			//Add in code to populate boysNames
			squad = section.getSquad(getArguments().getString("selection"));
			System.out.println(squad);
			for(String boy: squad.getBoysNames()){
				boysNames.add(createBoyEntry(boy));
			}
			boysNames.add(createBoyEntry("Create new boy..."));
			SimpleAdapter simpleAdpt = new SimpleAdapter(activity, boysNames, android.R.layout.simple_list_item_1, new String[] {"boy"}, new int[] {android.R.id.text1});
			boysListView.setAdapter(simpleAdpt);
			
			return rootView;
		}
		
		public HashMap<String, String> createBoyEntry(String name){
			HashMap<String, String> entry = new HashMap<String, String>();
			entry.put("boy", name);
			return entry;
		}
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
