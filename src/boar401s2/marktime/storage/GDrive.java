package boar401s2.marktime.storage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import boar401s2.marktime.interfaces.SynchroniseEvents;
import boar401s2.marktime.storage.spreadsheet.Spreadsheet;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ServiceException;

public class GDrive {
	
	public SpreadsheetService spreadsheetService;
	SpreadsheetFeed feed;
	
	List<SpreadsheetEntry> spreadsheets;
	
	URL SPREADSHEET_FEED_URL;
	Authentication auth;
	Boolean isConnected = false;
	
	Activity activityParent;
	SynchroniseEvents eventsParent;
	
	public GDrive(SynchroniseEvents eventsParent, Activity activityParent, String email, String password){
		this.activityParent = activityParent;
		this.eventsParent = eventsParent;
		ConnectTask task = new ConnectTask();
		task.execute(email, password);
	}
	
	//==========[Spreadsheeting Section]==========//
	
	public List<SpreadsheetEntry> getSpreadsheets(){
		return feed.getEntries();
	}
	
	public List<String> getSpreadsheetNames(){
		List<SpreadsheetEntry> entries = getSpreadsheets();
		List<String> names = new ArrayList<String>();
		for(SpreadsheetEntry entry: entries){
			names.add(entry.getTitle().getPlainText());
		}
		return names;
	}
	
	public boolean driveContainsSpreadsheet(String name){
		return getSpreadsheetNames().contains(name);
	}
	
	public int getSpreadsheetID(String name){
		return getSpreadsheetNames().indexOf(name);
	}
	
	public Spreadsheet getSpreadsheet(String name){
		try {
			return new Spreadsheet(getSpreadsheets().get(getSpreadsheetID(name)), this);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private class ConnectTask extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute(){
			eventsParent.onStatusChange("Connecting to GDrive...");
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				publishProgress("Creating SpreadsheetService...");
				spreadsheetService = new SpreadsheetService("SpreadsheetService");
				publishProgress("Authenticating...");
				auth = new Authentication(params[0], params[1]);
				auth.authenticateSpreadsheetService(spreadsheetService);
				publishProgress("Getting spreadsheet feed...");
				SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
				feed = spreadsheetService.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
				publishProgress("Connected!");
				return "Connected!";
			} catch (Exception ex){
				ex.printStackTrace();
				return "An Error Occured!";
			}
		}
		
		@Override
		protected void onPostExecute(String status){
			//publishProgress(status);
			eventsParent.onConnected();
		}
		
		@Override
		public void onProgressUpdate(String... text){
			eventsParent.onStatusChange(text[0]);
		}
	}

}
