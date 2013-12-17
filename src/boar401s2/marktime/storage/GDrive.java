package boar401s2.marktime.storage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import boar401s2.marktime.interfaces.events.SynchroniseInterface;
import boar401s2.marktime.storage.spreadsheet.Spreadsheet;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ServiceException;

/**
 * Handler for communicating to Google Drive and 
 * opening things like spreadsheets.
 */
public class GDrive {
	
	public SpreadsheetService spreadsheetService;
	SpreadsheetFeed feed;
	
	List<SpreadsheetEntry> spreadsheets;
	
	URL SPREADSHEET_FEED_URL;
	Authentication auth;
	Boolean isConnected = false;
	
	Activity activityParent;
	SynchroniseInterface eventsParent;
	
	HashMap<String, Spreadsheet> openedSpreadsheets = new HashMap<String, Spreadsheet>();
	
	public GDrive(SynchroniseInterface eventsParent, Activity activityParent, String email, String password){
		this.activityParent = activityParent;
		this.eventsParent = eventsParent;
		ConnectTask task = new ConnectTask();
		task.execute(email, password);
	}
	
	//==========[Spreadsheeting Section]==========//
	
	/**
	 * Gets a list of the spreadsheets in the current account
	 * @return List of spreadsheets in current authenticated account
	 */
	public List<SpreadsheetEntry> getSpreadsheets(){
		return feed.getEntries();
	}
	
	/**
	 * Gets a list of the spreadsheet names in the current account
	 * @return List of spreadsheet names
	 */
	public List<String> getSpreadsheetNames(){
		List<SpreadsheetEntry> entries = getSpreadsheets();
		List<String> names = new ArrayList<String>();
		for(SpreadsheetEntry entry: entries){
			names.add(entry.getTitle().getPlainText());
		}
		return names;
	}
	
	/**
	 * Checks to see if a drive contains "name"
	 */
	public boolean driveContainsSpreadsheet(String name){
		return getSpreadsheetNames().contains(name);
	}
	
	/**
	 * Gets the List ID of a spreadsheet
	 * @param name The name of the spreadsheet
	 * @return ID of the spreadsheet in the list
	 */
	public int getSpreadsheetID(String name){
		return getSpreadsheetNames().indexOf(name);
	}
	
	/**
	 * Gets a spreadsheet by name
	 * @param name Name of the spreadsheet to get
	 * @return A new instance of the Spreadsheet object
	 */
	public Spreadsheet getSpreadsheet(String name){
		try {
			if(openedSpreadsheets.containsKey(name)){
				return openedSpreadsheets.get(name);
			} else {
				openedSpreadsheets.put(name, new Spreadsheet(getSpreadsheets().get(getSpreadsheetID(name)), this));
				return openedSpreadsheets.get(name);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Task to connect to, authenticate with, and get
	 * the spreadsheet from Google Drive
	 */
	private class ConnectTask extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute(){
			eventsParent.onStatusChange("Connecting to GDrive...");
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				spreadsheetService = new SpreadsheetService("SpreadsheetService");
				publishProgress("Authenticating...");
				auth = new Authentication(params[0], params[1]);
				auth.authenticateSpreadsheetService(spreadsheetService);
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
			eventsParent.onConnected();
		}
		
		@Override
		public void onProgressUpdate(String... text){
			eventsParent.onStatusChange(text[0]);
		}
	}

}
