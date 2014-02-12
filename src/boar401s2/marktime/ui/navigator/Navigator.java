package boar401s2.marktime.ui.navigator;

import boar401s2.marktime.R;
import boar401s2.marktime.dialog.InputDialog;
import boar401s2.marktime.dialog.RequestIDList;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.events.InputDialogParent;
import boar401s2.marktime.handlers.Boy;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.handlers.Section;
import boar401s2.marktime.handlers.Squad;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class Navigator extends Activity implements AsyncTaskParent, InputDialogParent {
	
	Company company;
	Section section;
	Squad squad;
	Boy boy;
	
	SectionNavigator sectionNavigator;
	SquadNavigator squadNavigator;
	BoyNavigator boyNavigator;
	
	Integer currentLevel = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigator);
		setupActionBar();
		company = new Company(this);
		currentLevel = LevelIDList.SECTION;
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
		if(!(data==null)){ //If the activity returned naturally
			if(requestCode==LevelIDList.SECTION){
				String id = data.getExtras().getString("id").split(",")[0].split(":")[1];
				currentLevel = LevelIDList.SQUAD;
				section = company.getSection(id);
				squadNavigator = new SquadNavigator(this, section);
				squadNavigator.show();
			} else if(requestCode==LevelIDList.SQUAD){
				String id = data.getExtras().getString("id").split(",")[0].split(":")[1];
				squad = section.getSquad(id);
				currentLevel = LevelIDList.BOY;
				boyNavigator = new BoyNavigator(this, squad);
				boyNavigator.show();
			} else if(requestCode==LevelIDList.BOY){
				String id = data.getExtras().getString("id").split(",")[0].split(":")[1];
				if(id.startsWith("New")){
					InputDialog dialog = new InputDialog(this, this, "Create new boy", "Type in the name of the new boy", RequestIDList.CREATE);
					dialog.show();
				} else {
					System.out.println("Selected boy: "+id);
					finish();
				}
			}
		} else { //Else if the activity either crashed, or the up key was pressed
			if(requestCode==LevelIDList.SECTION){
				finish();
			} else if(requestCode==LevelIDList.SQUAD){
				sectionNavigator = new SectionNavigator(this, company);
				sectionNavigator.show();
				currentLevel = LevelIDList.SECTION;
			} else if(requestCode==LevelIDList.BOY){
				squadNavigator = new SquadNavigator(this, section);
				squadNavigator.show();
				currentLevel = LevelIDList.SQUAD;
			}
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

	@Override
	public void onDialogReturn(String result, Integer requestID) {
		if(currentLevel == LevelIDList.SECTION){
			
		} else if(currentLevel == LevelIDList.SQUAD){
			
		} else if(currentLevel == LevelIDList.BOY){
			if(requestID==RequestIDList.CREATE){
				System.out.println("Creating new boy!");
				squad.addBoy("result");
				company.saveAttendance();
				boyNavigator = new BoyNavigator(this, squad);
				boyNavigator.show();
			}
		}
	}

	@Override
	public void onDialogCancelled() {}

}
