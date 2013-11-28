package boar401s2.marktime;

import boar401s2.marktime.exceptions.NonexistantSquadException;
import boar401s2.marktime.exceptions.SquadNotFetchedException;
import boar401s2.marktime.interfaces.SynchroniseEvents;
import boar401s2.marktime.storage.Authentication;
import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.handlers.Squad;
import boar401s2.marktime.storage.spreadsheet.Spreadsheet;
import boar401s2.marktime.storage.spreadsheet.Worksheet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class Synchronise extends Activity implements SynchroniseEvents{
	
	Authentication auth;
	TextView statusBox;
	
	GDrive gdrive;
	Spreadsheet spreadsheet;
	Worksheet squadSheet;
	Squad squad;

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
		
		if(MarkTime.settings.getString("spreadsheet", "").equalsIgnoreCase("")){
			Toast.makeText(MarkTime.activity.getApplicationContext(), "Please enter spreadsheet name in 'Settings'", Toast.LENGTH_LONG).show();
			finish();
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
	public void onConnected(){
		syncSquads();
	}
	
	public void onSquadFetched(){}
	
	public void syncSquads(){
		//TODO
		//Write code to download squad data stuff
		//(Make sure it's an AsyncTask)
		//
		//Scan for sheets beginning with "squad"
		//Pick one
		//Scan through first and last names and store in hashmap
		//Serialize hashmap and store in file on external storage
		try {
			squad = new Squad(this, gdrive, "1");
			squad.pullSquadFromSpreadsheet();
			onStatusChange("Saving squad data to file...");
			try {
				squad.pushSquadToFile();
			} catch (SquadNotFetchedException e) {}
			onStatusChange("Saved squad data to file!");
		} catch (NonexistantSquadException e) {
			Toast.makeText(MarkTime.activity.getApplicationContext(), "That squad doesn't exist!", Toast.LENGTH_SHORT).show();
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
}
