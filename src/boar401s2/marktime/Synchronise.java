package boar401s2.marktime;

import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.security.Credentials;
import boar401s2.marktime.storage.GDrive;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
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
		Credentials creds = new Credentials(MarkTime.settings.getString("username", ""), MarkTime.settings.getString("password", ""));
		if (creds.isEmpty()){
			Toast.makeText(MarkTime.activity.getApplicationContext(), "Please enter login details in 'Settings'", Toast.LENGTH_LONG).show();
			finish();
		} else {
			openProgressDialog("Connecting to GDrive...");
			gdrive = new GDrive(this, this, creds);
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
	public void onPostExecute() {
		closeProgressDialog();
		Toast.makeText(MarkTime.activity.getApplicationContext(), "Connected to Google Drive!", Toast.LENGTH_SHORT).show();
		setContentView(R.layout.activity_synchronise);
	}
}
