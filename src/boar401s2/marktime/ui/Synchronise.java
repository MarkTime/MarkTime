package boar401s2.marktime.ui;

import boar401s2.marktime.MarkTime;
import boar401s2.marktime.R;
import boar401s2.marktime.constants.ResultIDList;
import boar401s2.marktime.constants.TaskIDList;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.tasks.SyncLocalTask;
import boar401s2.marktime.storage.tasks.SyncRemoteTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class Synchronise extends Activity implements AsyncTaskParent{
	
	GDrive gdrive;
	
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
		if(MarkTime.settings.getString("spreadsheet", "").equalsIgnoreCase("")){
			Toast.makeText(MarkTime.activity.getApplicationContext(), "Please enter spreadsheet name in 'Settings'", Toast.LENGTH_LONG).show();
			finish();
		}
		if (MarkTime.settings.getString("account", "").length()==0){
			Toast.makeText(MarkTime.activity.getApplicationContext(), "Please enter account details in 'Settings'", Toast.LENGTH_LONG).show();
			finish();
		} else {
			openProgressDialog("Connecting to GDrive...");
			gdrive = new GDrive(this, this, MarkTime.settings.getString("account", ""));
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

	//==========[Callbacks]==========//
	
	/**
	 * 'Callback' to update the status message
	 */
	@Override
	public void onStatusChange(String status) {
		if(!(progressDialog==null)){
			progressDialog.setMessage(status);
		} else {
			openProgressDialog(status);
		}
	}
	
	/**
	 * Opens a progress dialog.
	 * @param message Message to display in it
	 */
	public void openProgressDialog(String message){
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(message);
		progressDialog.show();
	}
	
	/**
	 * Closes the currently open progress dialog.
	 */
	public void closeProgressDialog(){
		progressDialog.dismiss();
	}

	/**
	 * Called when starting to authenticate spreadsheet service
	 */
	@Override
	public void onPreExecute() {
		
	}
	
	/**
	 * Called when spreadsheet service has been authenticated
	 */
	@Override
	public void onPostExecute(Integer taskID, Integer status) {
		closeProgressDialog();
		if (taskID==TaskIDList.TASK_GDRIVE_CONNECTED){
			Toast.makeText(MarkTime.activity.getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
			closeProgressDialog();
			setContentView(R.layout.activity_synchronise);
		} else if(taskID==TaskIDList.TASK_SYNC_LOCAL){
			closeProgressDialog();
			if (status==ResultIDList.RESULT_OK){
				Toast.makeText(MarkTime.activity.getApplicationContext(), "Synced local device!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MarkTime.activity.getApplicationContext(), "Internal Error!", Toast.LENGTH_SHORT).show();
			}
		} else if(taskID == TaskIDList.TASK_SYNC_REMOTE){
			closeProgressDialog();
			if (status==ResultIDList.RESULT_OK){
				Toast.makeText(MarkTime.activity.getApplicationContext(), "Synced remote spreadsheet!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MarkTime.activity.getApplicationContext(), "Internal Error!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * Callback for when an activity returns
	 */
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
	}
	
	/**
	 * Callback for "Sync Local" button
	 * @param view
	 */
	public void onSyncLocalClick(View view){
		syncLocalWithRemote();
	}
	
	/**
	 * Callback for "Sync Remote" button
	 * @param view
	 */
	public void onSyncRemoteClick(View view){
		syncRemoteWithLocal();
	}
	
	/**
	 * Callback for "Sync" button
	 * @param view
	 */
	public void onSyncClick(View view){
		onSyncLocalClick(view);
		onSyncRemoteClick(view);
	}
	
	//==========[Sync Stuff]==========//
	
	public void syncLocalWithRemote(){
		new SyncLocalTask(this, gdrive).run();
	}
	
	public void syncRemoteWithLocal(){
		new SyncRemoteTask(this, gdrive).run();
	}
	
}
