package boar401s2.marktime.storage.tasks;

import java.util.Arrays;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import boar401s2.marktime.MarkTime;
import boar401s2.marktime.constants.ResultIDList;
import boar401s2.marktime.constants.TaskIDList;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.exceptions.UnCaughtException;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

/**
 * Task that gets the google drive service
 * @author boar401s2
 */
public class GetDriveService {
	
	String account;
	
	AsyncTaskParent parent;
	Activity activityParent;
	String token;
	GoogleAccountCredential credential;
	Drive driveService;
	
	public GetDriveService(Activity activityParent, AsyncTaskParent parent, String account){
		this.account = account;
		this.parent = parent;
		this.activityParent = activityParent;
	}
	
	/**
	 * Runs the task
	 */
	public void run(){
		new GetDriveServiceTask().execute();
	}
	
	/**
	 * Gets the credentials
	 * @return
	 */
	public GoogleAccountCredential getCredential(){
		return credential;
	}
	
	/**
	 * Gets the drive service
	 * @return
	 */
	public Drive getDriveService(){
		return driveService;
	}
	
	/**
	 * Task that gets the oauth2 token for a user
	 */
	class GetDriveServiceTask extends AsyncTask<Void, String, Integer> {	
		@Override
		protected void onPreExecute(){
			Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MarkTime.activity.getApplicationContext()));
			parent.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... arg0) {
			try {
				publishProgress("Getting credentials...");
				credential = GoogleAccountCredential.usingOAuth2(MarkTime.activity.getApplicationContext(), Arrays.asList(DriveScopes.DRIVE_FILE));
				credential.setSelectedAccountName(account);
				publishProgress("Getting drive service...");
				driveService = getDriveService(credential);
				
				return ResultIDList.RESULT_OK;
			} catch (Exception ex){
				ex.printStackTrace();
				return ResultIDList.RESULT_ERROR;
			}
		}
		
		@Override
		protected void onPostExecute(Integer result){
			if (result!=ResultIDList.RESULT_NO_RETURN){
				parent.onPostExecute(TaskIDList.TASK_GET_DRIVE_SERVICE, result);
			}
		}
		
		@Override
		public void onProgressUpdate(String... text){
			parent.onStatusChange(text[0]);
		}
		
		/**
		 * Actually builds and runs a request to get the drive service
		 * @param credential
		 * @return
		 */
		private Drive getDriveService(GoogleAccountCredential credential) {
		    return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
		        .build();
		}
	}
	
	/**
	 * Called when an activity returns, which in this case
	 * is the "Google OAuth2.0 permission request" activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if(requestCode==TaskIDList.TASK_GET_AUTH_PERMISSION && resultCode==Activity.RESULT_OK){
			run();
		}
	}
	
}
