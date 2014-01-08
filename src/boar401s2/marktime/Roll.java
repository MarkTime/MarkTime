package boar401s2.marktime;

import java.util.List;

import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.handlers.Section;
import boar401s2.marktime.handlers.Squad;
import android.app.ActionBar;
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
import android.widget.TextView;

public class Roll extends FragmentActivity implements
		ActionBar.OnNavigationListener, AsyncTaskParent{
	
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	public String selectedSection;
	
	Company company;
	Section section;
	Squad squad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Company company = new Company(this);
		
		List<String> sections = company.getSections();
		final CharSequence[] items = sections.toArray(new CharSequence[sections.size()]);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Section to Mark");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	section = new Section();
            	selectedSection = (String) items[item];
            	
            	/*squads = new Squads(null, null, null);
        		List<String> sqds = squads.getLocalSquads();
        		
        		actionBar.setListNavigationCallbacks(
        				new ArrayAdapter<String>(this,
        						android.R.layout.simple_list_item_1,
        						android.R.id.text1, sqds), this);*/
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roll);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
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
	public boolean onNavigationItemSelected(int position, long id) {
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		args.putString("Title", "");
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}

	public static class DummySectionFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_roll_dummy,
					container, false);
			
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			
			//dummyTextView.setText(squads.getLocalSquads().get(getArguments().getInt(ARG_SECTION_NUMBER)-1));
			
			return rootView;
		}
	}

	@Override
	public void onStatusChange(String status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openProgressDialog(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeProgressDialog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostExecute(Integer taskid, Integer result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPreExecute() {
		// TODO Auto-generated method stub
		
	}

}
