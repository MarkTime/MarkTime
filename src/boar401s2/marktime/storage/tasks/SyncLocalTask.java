package boar401s2.marktime.storage.tasks;

import java.util.List;

import com.google.gdata.data.spreadsheet.SpreadsheetEntry;

import android.os.AsyncTask;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.spreadsheet.AuthenticatedSpreadsheetService;


public class SyncLocalTask {
	
	AsyncTaskParent parent;
	AuthenticatedSpreadsheetService service;
	
	public SyncLocalTask(AsyncTaskParent parent, AuthenticatedSpreadsheetService spreadsheetService){
		this.parent = parent;
		service = spreadsheetService;
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
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			List<SpreadsheetEntry> se = service.getSpreadsheetFeed().getEntries();
			for (SpreadsheetEntry e: se){
				System.out.println(e.getTitle());
			}
			return ResultIDList.RESULT_OK;
		}
		
		@Override
		protected void onPostExecute(Integer result){
			if (result!=ResultIDList.RESULT_NO_RETURN){
				parent.onPostExecute(TaskIDList.TASK_SYNC_LOCAL, result);
			}
		}
		
		@Override
		public void onProgressUpdate(String... text){
			parent.onStatusChange(text[0]);
		}
	}

}
