package boar401s2.marktime.storage;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import android.app.Activity;
import android.widget.Toast;
import boar401s2.marktime.MarkTime;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.spreadsheet.AuthenticatedDocsService;
import boar401s2.marktime.storage.spreadsheet.AuthenticatedSpreadsheetService;
import boar401s2.marktime.storage.spreadsheet.OnlineSpreadsheet;
import boar401s2.marktime.storage.tasks.GetAuthToken;
import boar401s2.marktime.storage.tasks.GetDriveService;
import boar401s2.marktime.storage.tasks.ResultIDList;
import boar401s2.marktime.storage.tasks.TaskIDList;

/**
 * Handler for communicating to Google Drive and 
 * opening things like spreadsheets.
 */
public class GDrive implements AsyncTaskParent{
	
	AsyncTaskParent eventsParent;
	Activity activityParent;
	String token;
	
	Drive driveService;
	AuthenticatedSpreadsheetService authedSpreadsheetService;
	AuthenticatedDocsService authedDocsService;
	
	String account;
	
	GetDriveService driveTask;
	GetAuthToken authTask;
	
	public GDrive(AsyncTaskParent eventsParent, Activity activityParent, String account){
		this.eventsParent = eventsParent;
		this.activityParent = activityParent;
		this.account = account;
		driveTask = new GetDriveService(activityParent, this, account);
		driveTask.run();
	}
	
	/**
	 * @return authenticated spreadhseet service
	 */
	public AuthenticatedSpreadsheetService getAuthenticatedSpreadsheetService(){
		return authedSpreadsheetService;
	}
	
	/**
	 * @return drive service
	 */
	public Drive getDriveService(){
		return driveService;
	}
	
	//==========[Document List stuff]==========//
	public List<String> getFiles(){
		List<String> files = new ArrayList<String>();
		for (DocumentListEntry entry : authedDocsService.getDocsFeed().getEntries()) {
			files.add(entry.getFilename());
		}
		return files;
	}
	
	//==========[Spreadsheet Stuff]==========//
	
	/**
	 * Gets a list of the spreadsheet names
	 * @return List<String> spreadsheet names
	 */
	public List<String> getSpreadsheetList(){
		List<String> spreadsheets = new ArrayList<String>();
		for (SpreadsheetEntry e: authedSpreadsheetService.getSpreadsheetFeed().getEntries()){
			spreadsheets.add(e.getTitle().getPlainText());
		}
		return spreadsheets;
	}
	
	/**
	 * Gets the instance of an online spreadsheet
	 * @param name Name of the spreadsheet
	 * @return OnlineSpreadsheet
	 */
	public OnlineSpreadsheet getSpreadsheet(String name){
		return new OnlineSpreadsheet(this, authedSpreadsheetService);
	}
	
	//==========[Events]==========//

	@Override
	public void onStatusChange(String status) {
		eventsParent.onStatusChange(status);
	}
	
	@Override
	public void onPostExecute(Integer taskid, Integer result) {
		if(taskid==TaskIDList.TASK_GET_DRIVE_SERVICE){ //If the finished task was getting the google drive service...
			if (result==ResultIDList.RESULT_OK){ //If it got the drive service, then move onto getting the auth token.
				authTask = new GetAuthToken(this, activityParent, account);
				authTask.run();
			} else { //If it failed, then show error message, and close activity.
				Toast.makeText(MarkTime.activity.getApplicationContext(), "Internal error!", Toast.LENGTH_SHORT).show();
				activityParent.finish();
			}
		} else if (taskid==TaskIDList.TASK_GET_AUTH_TOKEN){//When finished getting the docs service...
			if (result==ResultIDList.RESULT_OK){ //If it got the docs successfully...
				authedSpreadsheetService = new AuthenticatedSpreadsheetService(eventsParent);
				authedSpreadsheetService.authenticate(authTask.getToken());
			} else { //If it didn't then..
				Toast.makeText(MarkTime.activity.getApplicationContext(), "Internal error!", Toast.LENGTH_SHORT).show();
				activityParent.finish();
			}
		}
	}

	@Override
	public void openProgressDialog(String message) {}

	@Override
	public void closeProgressDialog() {}

	@Override
	public void onPreExecute() {}
}
