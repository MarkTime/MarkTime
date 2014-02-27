package boar401s2.marktime.storage.tasks;

import java.util.Iterator;
import java.util.Map;

import android.os.AsyncTask;
import boar401s2.marktime.MarkTime;
import boar401s2.marktime.constants.ResultIDList;
import boar401s2.marktime.constants.TaskIDList;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.exceptions.UnCaughtException;
import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.OfflineSpreadsheet;
import boar401s2.marktime.storage.spreadsheet.OnlineSpreadsheet;
import boar401s2.marktime.storage.spreadsheet.worksheet.OfflineWorksheet;
import boar401s2.marktime.storage.spreadsheet.worksheet.OnlineWorksheet;


public class SyncRemoteTask {
	
	AsyncTaskParent parent;
	GDrive drive;
	OnlineWorksheet worksheet;
	
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
			Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MarkTime.activity.getApplicationContext()));
			parent.onPreExecute();
			parent.openProgressDialog("Syncing cloud...");
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MarkTime.activity.getApplicationContext()));
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
		
		/*private CellEntry createUpdateOperation(int row, int col, String value, OnlineWorksheet onlineWorksheet)
				throws ServiceException, IOException {
			SpreadsheetService service = drive.getAuthenticatedSpreadsheetService().getSpreadsheetService();
			String batchId = "R" + row + "C" + col;
			URL entryUrl = new URL(onlineWorksheet.getWorksheetEntry().getCellFeedUrl().toString() + "/" + batchId);
			CellEntry entry = service.getEntry(entryUrl, CellEntry.class);
			entry.changeInputValueLocal(value);
			BatchUtils.setBatchId(entry, batchId);
			BatchUtils.setBatchOperationType(entry, BatchOperationType.UPDATE);
			return entry;
		}*/
		
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
				
				//Iterate over cells and set the value from the local spreadsheet
				
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
				
				//Batch operations code
				
				/*CellFeed batchRequest = new CellFeed();
				System.out.println("Creating batch request.");
				@SuppressWarnings("rawtypes")
				Iterator it = offlineWorksheet.getData().entrySet().iterator();
			    while (it.hasNext()) {
			        @SuppressWarnings("rawtypes")
					Map.Entry pairs = (Map.Entry)it.next();
			        
			        Position pos = new Position((String) pairs.getKey());
			        pos.convertToCartesian();
					try {
						CellEntry batchOperation = createUpdateOperation(pos.getY()+1, pos.getX()+1, (String) pairs.getValue(), onlineWorksheet);
						System.out.println((String) pairs.getKey() + (String) pairs.getValue());
						batchRequest.getEntries().add(batchOperation);
					} catch (ServiceException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Added value.");
			        it.remove();
			    }
			    offlineWorksheet.setModified(false);
			    
			    //printBatchRequest(batchRequest);
			    
			    System.out.println("Pushing stuff up");
			    
			    SpreadsheetService service = drive.getAuthenticatedSpreadsheetService().getSpreadsheetService();
			    CellFeed feed;
			    CellFeed batchResponse = null;
				try {
					feed = service.getFeed(onlineWorksheet.getWorksheetEntry().getCellFeedUrl(), CellFeed.class);
					Link batchLink = feed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
				    URL batchUrl = new URL(batchLink.getHref());
				    batchResponse = service.batch(batchUrl, batchRequest);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			    
				boolean isSuccess = true;
			    for (CellEntry entry : batchResponse.getEntries()) {
			      String batchId = BatchUtils.getBatchId(entry);
			      if (!BatchUtils.isSuccess(entry)) {
			        isSuccess = false;
			        BatchStatus status = BatchUtils.getBatchStatus(entry);
			        System.out.println("\n" + batchId + " failed (" + status.getReason()
			            + ") " + status.getContent());
			      }
			    }
			    if (isSuccess) {
			      System.out.println("Batch operations successful.");
			    }
				
			    
			    //Don't forget HASBEENMODIFIED*/
			}
		}
	}
}
