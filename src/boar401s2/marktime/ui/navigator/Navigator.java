package boar401s2.marktime.ui.navigator;

import boar401s2.marktime.R;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.handlers.Company;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class Navigator extends Activity implements AsyncTaskParent {
	
	Company company;
	SectionNavigator sectionNavigator;
	SquadNavigator squadNavigator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigator);
		setupActionBar();
		company = new Company(this);
		sectionNavigator = new SectionNavigator(this, company);
		sectionNavigator.show();
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
	
	public void show(){
		sectionNavigator.show();
	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		System.out.println("Activity resulted!");
		if(requestCode==LevelIDList.SECTION){
			String id = data.getExtras().getString("id").split(",")[0].split(":")[1];
			System.out.println(id);
			squadNavigator = new SquadNavigator(this, company.getSection(id));
			squadNavigator.show();
		} else if(requestCode==LevelIDList.SQUAD){
			finish();
		} else if(requestCode==LevelIDList.BOY){
			
		}
	}

	//==========[Async Task Parent stuff]=========//
	
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
