package boar401s2.marktime.storage.tasks;

import java.util.Iterator;
import java.util.Map;

import android.os.AsyncTask;
import boar401s2.marktime.MarkTime;
import boar401s2.marktime.constants.ResultIDList;
import boar401s2.marktime.constants.TaskIDList;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.OfflineSpreadsheet;
import boar401s2.marktime.storage.spreadsheet.OnlineSpreadsheet;
import boar401s2.marktime.storage.spreadsheet.worksheet.OfflineWorksheet;
import boar401s2.marktime.storage.spreadsheet.worksheet.OnlineWorksheet;


public class SyncRemoteTask {
	
	AsyncTaskParent parent;
	GDrive drive;
	
	public SyncRemoteTask(AsyncTaskParent parent, GDrive drive){
		this.parent = parent;
		this.drive = drive;
	}
	
	public void run(){
		new SyncLocal().execute();
	}
	
	/**
	 * Task that syncs the local spreadsheets with the remote ones
	 */
	class SyncLocal extends AsyncTask<Void, String, Integer> {	
		
		@Override
		protected void onPreExecute(){
			parent.onPreExecute();
			parent.openProgressDialog("Syncing cloud...");
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			uploadAttendance();
			parent.closeProgressDialog();
			return ResultIDList.RESULT_OK;
		}
		
		@Override
		protected void onPostExecute(Integer result){
			if (result!=ResultIDList.RESULT_NO_RETURN){
				parent.onPostExecute(TaskIDList.TASK_SYNC_REMOTE, result);
			}
		}
		
		@Override
		public void onProgressUpdate(String... text){
			parent.onStatusChange(text[0]);
		}
		
		public void uploadAttendance(){
			publishProgress("Opening remote Attendance spreadsheet...");
			
			//Initalize spreadsheets
			OnlineSpreadsheet onlineSpreadsheet = drive.getSpreadsheet(MarkTime.settings.getString("spreadsheet", ""));
			
			publishProgress("Loading local Attendance spreadsheet...");
			OfflineSpreadsheet offlineSpreadsheet = new OfflineSpreadsheet(onlineSpreadsheet.getName());
			offlineSpreadsheet.load(MarkTime.activity.getFilesDir()+"/"+onlineSpreadsheet.getName()+".db");
			
			for(Worksheet worksheet: offlineSpreadsheet.getWorksheets()){
				publishProgress("Updating '"+offlineSpreadsheet.getName()+":"+worksheet.getName()+"'!");
				
				OfflineWorksheet offlineWorksheet = (OfflineWorksheet) worksheet;
				OnlineWorksheet onlineWorksheet;
				
				if (!onlineSpreadsheet.getWorksheetNames().contains(offlineWorksheet.getName())){
					System.out.println("Creating "+offlineWorksheet.getName());
					String name = offlineWorksheet.getName();
					onlineSpreadsheet.createWorksheet(name);
					onlineWorksheet = (OnlineWorksheet) onlineSpreadsheet.getWorksheet(name);
					onlineWorksheet.setSize(offlineWorksheet.getWidth(), offlineWorksheet.getHeight());
				} else {
					if(offlineWorksheet.hasBeenModified()){
						onlineWorksheet = (OnlineWorksheet) onlineSpreadsheet.getWorksheet(offlineWorksheet.getName());
					} else {
						continue;
					}
				}
				
				@SuppressWarnings("rawtypes")
				Iterator it = offlineWorksheet.getData().entrySet().iterator();
			    while (it.hasNext()) {
			        @SuppressWarnings("rawtypes")
					Map.Entry pairs = (Map.Entry)it.next();
			        onlineWorksheet.setCell((String) pairs.getKey(), (String) pairs.getValue());
			        publishProgress("Updating '"+offlineSpreadsheet.getName()+":"+worksheet.getName()+"': "+pairs.getValue());
			        it.remove();
			    }
			    offlineWorksheet.setModified(false);
			}
		}
	}
}
