package boar401s2.marktime.storage.tasks;

import java.io.IOException;

import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.util.ServiceException;

import android.os.AsyncTask;
import boar401s2.marktime.MarkTime;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.OfflineSpreadsheet;
import boar401s2.marktime.storage.spreadsheet.OnlineSpreadsheet;
import boar401s2.marktime.storage.spreadsheet.worksheet.OfflineWorksheet;
import boar401s2.marktime.storage.spreadsheet.worksheet.OnlineWorksheet;


public class SyncLocalTask {
	
	AsyncTaskParent parent;
	GDrive drive;
	
	public SyncLocalTask(AsyncTaskParent parent, GDrive drive){
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
			parent.openProgressDialog("Syncing local device...");
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			publishProgress("Opening spreadsheet...");
			
			//Initalize spreadsheets
			OnlineSpreadsheet onlineSpreadsheet = drive.getSpreadsheet(MarkTime.settings.getString("register", ""));
			OfflineSpreadsheet offlineSpreadsheet = new OfflineSpreadsheet("Register");
			
			//Loop through all the online spreadsheet's worksheets
			for(Worksheet worksheet: onlineSpreadsheet.getWorksheets()){
				publishProgress("Updating '"+"Register:"+worksheet.getName()+"'!");
				
				//Initalize worksheets
				OnlineWorksheet onlineWorksheet = (OnlineWorksheet) worksheet;
				OfflineWorksheet offlineWorksheet = new OfflineWorksheet(onlineWorksheet.getName(), offlineSpreadsheet);
				
				try {
					for(CellEntry cell: onlineWorksheet.getCellFeed().getEntries()){
						offlineWorksheet.setCell(cell.getTitle().getPlainText(), cell.getPlainTextContent());
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				offlineWorksheet.setSize(onlineWorksheet.getWidth(), onlineWorksheet.getHeight());
				offlineWorksheet.setModificationDate(onlineWorksheet.getModificationDate());
				offlineSpreadsheet.insertWorksheet(offlineWorksheet);
			}
			publishProgress("Saving 'Register'...");
			offlineSpreadsheet.save(MarkTime.activity.getFilesDir().getAbsolutePath()+"Register.db");
			parent.closeProgressDialog();
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
