package boar401s2.marktime.storage.tasks;

import java.util.Arrays;

import android.os.AsyncTask;
import boar401s2.marktime.MarkTime;
import boar401s2.marktime.events.AsyncTaskParent;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class GetDriveService {
	
	String account;
	
	AsyncTaskParent parent;
	String token;
	GoogleAccountCredential credential;
	Drive driveService;
	
	public GetDriveService(AsyncTaskParent parent, String account){
		this.account = account;
		this.parent = parent;
	}
	
	public void run(){
		new GetDriveServiceTask().execute();
	}
	
	public GoogleAccountCredential getCredential(){
		return credential;
	}
	
	public Drive getDriveService(){
		return driveService;
	}
	
	/**
	 * Task that gets the oauth2 token for a user
	 */
	class GetDriveServiceTask extends AsyncTask<Void, String, Integer> {	
		@Override
		protected void onPreExecute(){
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
			parent.onPostExecute(TaskIDList.TASK_GET_DRIVE_SERVICE, result);
		}
		
		@Override
		public void onProgressUpdate(String... text){
			parent.onStatusChange(text[0]);
		}
		
		private Drive getDriveService(GoogleAccountCredential credential) {
		    return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
		        .build();
		}
	}
	
}
