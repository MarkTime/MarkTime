package boar401s2.marktime;

import boar401s2.marktime.storage.Authentication;
import boar401s2.marktime.storage.Events;
import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.spreadsheet.Spreadsheet;
import boar401s2.marktime.storage.spreadsheet.Worksheet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class Synchronise extends Activity implements Events{
	
	Authentication auth;
	TextView statusBox;
	
	GDrive gdrive;
	Spreadsheet spreadsheet;
	Worksheet squadSheet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronise);
		setupActionBar();
		statusBox = (TextView) findViewById(R.id.synchronise_status);
		
		if (MarkTime.settings.getString("username", "").equalsIgnoreCase("") || MarkTime.settings.getString("password", "").equalsIgnoreCase("")){
			Toast.makeText(MarkTime.activity.getApplicationContext(), "Please enter login details in 'Settings'", Toast.LENGTH_LONG).show();
			finish();
		} else {
			gdrive = new GDrive(this, this, MarkTime.settings.getString("username", ""), MarkTime.settings.getString("password", ""));
		}
	}
	
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.synchronise, menu);
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
	public void onStatusChange(String status) {
		statusBox.setText(status);
	}
	
	@Override
	public void onConnect(){
		onSyncSquads();
	}
	
	public void onSyncSquads(){
		//TODO
		//Write code to download squad data stuff
		//(Make sure it's an AsyncTask)
		//
		//Scan for sheets beginning with "squad"
		//Pick one
		//Scan through first and last names and store in hashmap
		//Serialize hashmap and store in file on external storage
		if (gdrive.driveContainsSpreadsheet("Attendance: 2013")){
			spreadsheet = gdrive.getSpreadsheet("Attendance: 2013");
			for (String s: spreadsheet.getWorksheetNames()){
				System.out.println(s);
			}
			squadSheet = spreadsheet.getWorksheet("Sheet1");
			squadSheet.printCells();
		} else {
			onStatusChange("SS does not exist!");
		}	
	}
	
	public void onSyncRoll(){
		//TODO
		//Write code to sync the local night database to online
		//
		//Find night files
		//Pick one
		//Unserialize hashmap
		//Check for online night sheet
		//If one doesn't exist, create it
		//Iterate through hasWhmap
		//Send data to the online spreadsheet
		//Delete night file
	}

	/*8class AuthenticateTask implements Runnable{
		
		Synchronise parent;
		
		public AuthenticateTask(Synchronise parent){
			this.parent = parent;
		}
		
		@Override
		public void run(){
			gdrive = new GDrive(parent, parent, username, password);
			onSyncSquads();
		}
	};*/
}
