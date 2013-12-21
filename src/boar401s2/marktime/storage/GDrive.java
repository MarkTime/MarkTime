package boar401s2.marktime.storage;

import android.app.Activity;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.security.Credentials;
import boar401s2.marktime.storage.spreadsheet.AuthenticatedSpreadsheetService;

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
	
	//==========[Spreadsheet Stuff]==========//
	
	
}
