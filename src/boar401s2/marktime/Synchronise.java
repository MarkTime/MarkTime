package boar401s2.marktime;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.exceptions.NonexistantSquadException;
import boar401s2.marktime.exceptions.SquadNotFetchedException;
import boar401s2.marktime.interfaces.SynchroniseInterface;
import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.handlers.Squad;
import boar401s2.marktime.storage.handlers.Squads;
import boar401s2.marktime.storage.spreadsheet.Spreadsheet;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class Synchronise extends Activity implements SynchroniseInterface{
	
	GDrive gdrive;
	Spreadsheet spreadsheet;
	Squads squads;
	Squad squad;
	List<String> squadsToSync;
	
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (MarkTime.settings.getString("username", "").equalsIgnoreCase("") || MarkTime.settings.getString("password", "").equalsIgnoreCase("")){
			Toast.makeText(MarkTime.activity.getApplicationContext(), "Please enter login details in 'Settings'", Toast.LENGTH_LONG).show();
			finish();
		} else {
			openProgressDialog("Connecting to GDrive...");
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
	
	/**
	 * Called when GDrive has connected.
	 */
	@Override
	public void onConnected(){
		onStatusChange("Connecting to spreadsheet...");
		Toast.makeText(MarkTime.activity.getApplicationContext(), "Connected to Google Drive!", Toast.LENGTH_LONG).show();
		spreadsheet = gdrive.getSpreadsheet(MarkTime.settings.getString("spreadsheet", ""));
		closeProgressDialog();
		squads = new Squads(this, gdrive, spreadsheet);
		
		setContentView(R.layout.activity_synchronise);
		setupActionBar();
	}

	/**
	 * Callback for "Sync Squads" button. Syncs the remote squads with the local ones.
	 * @param view
	 */
	public void onSyncSquads(View view){
		squadsToSync = getRemoteSquads();
		if (squadsToSync.size()==0){
			Toast.makeText(MarkTime.activity.getApplicationContext(), "There are no squads in the current spreadsheet.", Toast.LENGTH_SHORT).show();
		} else {	
			syncSquad(squadsToSync.get(0));
		}
		
	}
	
	/**
	 * Used to sync a specific squad
	 * @param squadName The name of the squad to sync
	 */
	public void syncSquad(String squadName){
		//System.out.println("Syncing squad "+squadName+" on thread "+android.os.Process.getThreadPriority(android.os.Process.myTid()));
		try {
			squad = squads.getSquad(squadName);
			squad.pullSquadFromSpreadsheet();
		} catch (NonexistantSquadException e) {
			Toast.makeText(MarkTime.activity.getApplicationContext(), "The squad "+squad+" doesn't exist!", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Callback for the squad syncing task. If another squad
	 * needs to be synced, it syncs it.
	 */
	@Override
	public void onSquadFetched(){
		onStatusChange("Saving"+squad.squadName+"...");
		try {
			squad.pushSquadToFile();
		} catch (SquadNotFetchedException e) {}
		Toast.makeText(MarkTime.activity.getApplicationContext(), "Synced "+squad.squadName+"!", Toast.LENGTH_SHORT).show();
		squadsToSync.remove(squadsToSync.indexOf(squad.squadName));
		if(squadsToSync.size()>0){
			syncSquad(squadsToSync.get(0));
		} else {
			closeProgressDialog();
		}
	}
	
	/**
	 * Callback for "Sync Roll" button. Starts syncing the roll
	 * @param view
	 * @
	 */
	public void onSyncRoll(View view){
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
	
	/**
	 * The roll syncing task callback
	 */
	public void onRollSynced(){
		Toast.makeText(MarkTime.activity.getApplicationContext(), "Not implemented yet!", Toast.LENGTH_SHORT).show();
	}
	
	//==========[Util]==========//
	
	/**
	 * Gets a list of the locally defined squads
	 * @return List of locally defined squads
	 */
	public List<String> getLocalSquads(){
		List<String> localSquads = new ArrayList<String>();
		for(String s: MarkTime.activity.getApplicationContext().getFilesDir().list()){
			if(s.startsWith("Squad-")){
				localSquads.add(s);
			}
		}
		return localSquads;
	}
	
	/**
	 * Gets a list of the remote squads
	 * Should be called off the main thread.
	 * @return List of remote squads.
	 */
	public List<String> getRemoteSquads(){
		List<String> remoteSquads = new ArrayList<String>();
		for (String name: spreadsheet.getWorksheetNames()){
			if(name.startsWith("Squad-")){
				remoteSquads.add(name);
			}
		}
		return remoteSquads;
	}

	//==========[Status Stuff]==========//
	
	/**
	 * 'Callback' to update the status message
	 */
	@Override
	public void onStatusChange(String status) {
		if(!(progressDialog==null)){
			progressDialog.dismiss();
			openProgressDialog(status);
		}
	}
	
	/**
	 * Opens a progress dialog.
	 * @param message Message to display in it
	 */
	public void openProgressDialog(String message){
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		progressDialog.show();
	}
	
	/**
	 * Closes the currently open progress dialog.
	 */
	public void closeProgressDialog(){
		progressDialog.dismiss();
	}
}
