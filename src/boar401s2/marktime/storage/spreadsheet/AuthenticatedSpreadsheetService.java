package boar401s2.marktime.storage.spreadsheet;

import java.net.URL;

import android.os.AsyncTask;
import boar401s2.marktime.MarkTime;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.security.Credentials;
import boar401s2.marktime.storage.tasks.ResultIDList;
import boar401s2.marktime.storage.tasks.TaskIDList;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;

/**
 * Used to autenticated services. At the moment it only authenticates
 * spreadsheet services.
 */
@SuppressWarnings("deprecation")
public class AuthenticatedSpreadsheetService{
	
	private String email;
	private String password;
	private String token;
	public static SpreadsheetService spreadsheetService;
	public static URL SPREADSHEET_FEED_URL;
	public static SpreadsheetFeed feed;
	AsyncTaskParent parent;
	
	public AuthenticatedSpreadsheetService(AsyncTaskParent parent){
		this.parent = parent;
	}
	
	public SpreadsheetService getSpreadsheetService(){
		return spreadsheetService;
	}
	
	public URL getFeedURL(){
		return SPREADSHEET_FEED_URL;
	}
	
	public SpreadsheetFeed getSpreadsheetFeed(){
		return feed;
	}
	
	/**
	 * Authenticate using the self-defined credentials class. (Username/Password)
	 * @param credentials
	 */
	public void authenticate(Credentials credentials){
		this.email = credentials.getEmail();
		this.password = credentials.getPassword();
		ConnectTask task = new ConnectTask();
		task.execute();
	}
	
	/**
	 * Authenticate using the google Credential class. (OAuth2 token)
	 * @param credential
	 */
	public void authenticate(String token){
		this.token = token;
		ConnectTask task = new ConnectTask();
		task.execute();
	}
	
	/**
	 * Task that authenticates a spreadsheet service
	 */
	private class ConnectTask extends AsyncTask<String, String, Integer> {
		
		@Override
		protected void onPreExecute(){
			parent.onPreExecute();
			parent.onStatusChange("Connecting to GDrive...");
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				spreadsheetService = new SpreadsheetService("SpreadsheetService");
				if (token!=null){
					publishProgress("Authenticating using OAuth2 token...");
					System.out.println(token);
					spreadsheetService.setAuthSubToken(token);
				} else {
					publishProgress("Authenticating using credentials...");
					spreadsheetService.setUserCredentials(email, password);
				}
				publishProgress("Getting spreadsheet feed...");
				SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
				feed = spreadsheetService.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
				return ResultIDList.RESULT_OK;
			} catch (Exception ex){
				ex.printStackTrace();
				GoogleAuthUtil.invalidateToken(MarkTime.activity.getApplicationContext(), token);
				return ResultIDList.RESULT_ERROR;
			}
		}
		
		@Override
		protected void onPostExecute(Integer result){
			parent.onPostExecute(TaskIDList.TASK_AUTH_SPREADSHEET_SERVICE, result);
		}
		
		@Override
		public void onProgressUpdate(String... text){
			parent.onStatusChange(text[0]);
		}
	}
}