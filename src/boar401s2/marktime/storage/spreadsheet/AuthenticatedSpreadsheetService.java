package boar401s2.marktime.storage.spreadsheet;

import java.net.URL;

import android.os.AsyncTask;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.security.Credentials;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;

//TODO: Update this class to use OAuth2

/**
 * Used to autenticated services. At the moment it only authenticates
 * spreadsheet services.
 */
public class AuthenticatedSpreadsheetService{
	
	private String email;
	private String password;
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
	
	public void authenticate(Credentials credentials){
		this.email = credentials.getEmail();
		this.password = credentials.getPassword();
		ConnectTask task = new ConnectTask();
		task.execute();
	}
	
	private class ConnectTask extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute(){
			parent.onPreExecute();
			parent.onStatusChange("Connecting to GDrive...");
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				spreadsheetService = new SpreadsheetService("SpreadsheetService");
				publishProgress("Aucthenticating...");
				spreadsheetService.setUserCredentials(email, password);
				publishProgress("Getting spreadsheet feed...");
				SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
				feed = spreadsheetService.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
				return "Connected!";
			} catch (Exception ex){
				ex.printStackTrace();
				return "An Error Occured!";
			}
		}
		
		@Override
		protected void onPostExecute(String status){
			parent.onPostExecute();
		}
		
		@Override
		public void onProgressUpdate(String... text){
			parent.onStatusChange(text[0]);
		}
	}
}