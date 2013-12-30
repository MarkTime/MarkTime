package boar401s2.marktime.storage;

import java.util.ArrayList;
import java.util.List;

import com.google.gdata.data.spreadsheet.SpreadsheetEntry;

import android.app.Activity;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.security.Credentials;
import boar401s2.marktime.storage.spreadsheet.AuthenticatedSpreadsheetService;
import boar401s2.marktime.storage.spreadsheet.OnlineSpreadsheet;

/**
 * Handler for communicating to Google Drive and 
 * opening things like spreadsheets.
 */
public class GDrive {
	
	AsyncTaskParent eventsParent;
	Activity activityParent;
	Credentials credentials;
	
	AuthenticatedSpreadsheetService authedSpreadsheetService;
	
	public GDrive(AsyncTaskParent eventsParent, Activity activityParent, Credentials credentials){
		this.eventsParent = eventsParent;
		this.activityParent = activityParent;
		this.credentials = credentials;
		authedSpreadsheetService = new AuthenticatedSpreadsheetService(eventsParent);
		authedSpreadsheetService.authenticate(credentials);
	}
	
	public AuthenticatedSpreadsheetService getAuthenticatedSpreadsheetService(){
		return authedSpreadsheetService;
	}
	
	public List<String> getSpreadsheetList(){
		List<String> spreadsheets = new ArrayList<String>();
		for (SpreadsheetEntry e: authedSpreadsheetService.getSpreadsheetFeed().getEntries()){
			spreadsheets.add(e.getTitle().getPlainText());
		}
		return spreadsheets;
	}
	
	public OnlineSpreadsheet getSpreadsheet(String name){
		return new OnlineSpreadsheet(this, authedSpreadsheetService);
	}
	
	//==========[Spreadsheet Stuff]==========//
	
	
}
