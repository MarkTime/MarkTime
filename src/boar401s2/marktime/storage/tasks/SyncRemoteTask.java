package boar401s2.marktime.storage.tasks;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.ServiceException;

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
import boar401s2.marktime.util.Position;


public class SyncRemoteTask {
	
	AsyncTaskParent parent;
	GDrive drive;
	OnlineWorksheet worksheet;
	SyncRemote task;
	
	CellFeed cellFeed;
	URL cellFeedUrl;
	SpreadsheetService service;
	
	boolean USING_BATCH_REQUESTS = true;
	
	public SyncRemoteTask(AsyncTaskParent parent, GDrive drive){
		this.parent = parent;
		this.drive = drive;
	}
	
	public void run(){
		task = new SyncRemote();
		task.execute();
	}

	public void stop(){
		task.cancel(true);
	}
	
	/**
	 * Task that syncs the local spreadsheets with the remote ones
	 */
	class SyncRemote extends AsyncTask<Void, String, Integer> {	
		
		@Override
		protected void onPreExecute(){
			//Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MarkTime.activity.getApplicationContext()));
			parent.onPreExecute();
			parent.openProgressDialog("Syncing cloud...");
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			//Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MarkTime.activity.getApplicationContext()));
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
		
		/**
		 * Gets the remote equivalent sheet of a local sheet
		 * Creates sheet if it doesn't exist.
		 * @param onlineSpreadsheet
		 * @param offlineWorksheet
		 * @return
		 */
		public OnlineWorksheet getRemoteSheetToBeUpdated(OnlineSpreadsheet onlineSpreadsheet, OfflineWorksheet offlineWorksheet){
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
					return null;
				}
			}
			return onlineWorksheet;
		}
		
		/**
		 * Copies the offlineWorksheet data to the onlineWorksheet
		 * 
		 * This function updates cell-by-cell and is very slow. In the process
		 * of developing an alternative method using batchrequests. This
		 * function is likely to be depricated soon.
		 * @param onlineWorksheet
		 * @param offlineWorksheet
		 */
		public void updateSheet(OnlineWorksheet onlineWorksheet, OfflineWorksheet offlineWorksheet){
			@SuppressWarnings("rawtypes")
			Iterator it = offlineWorksheet.getData().entrySet().iterator();
		    while (it.hasNext()) {
		        @SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry)it.next();
		        onlineWorksheet.setCell((String) pairs.getKey(), (String) pairs.getValue());
		        publishProgress("Updating"+worksheet.getName()+"': "+pairs.getValue());
		        it.remove();
		    }
		    offlineWorksheet.setModified(false);
		}
		
		/**
		   * Writes (to stdout) a list of the entries in the batch request in a human
		   * readable format.
		   * 
		   * @param batchRequest the CellFeed containing entries to display.
		   */
		  private void printBatchRequest(CellFeed batchRequest) {
		    System.out.println("Current operations in batch");
		    for (CellEntry entry : batchRequest.getEntries()) {
		      String msg = "\tID: " + BatchUtils.getBatchId(entry) + " - "
		          + BatchUtils.getBatchOperationType(entry) + " row: "
		          + entry.getCell().getRow() + " col: " + entry.getCell().getCol()
		          + " value: " + entry.getCell().getInputValue();
		      System.out.println(msg);
		    }
		  }
		  
		  /**
		   * Returns a CellEntry with batch id and operation type that will tell the
		   * server to update the specified cell with the given value. The entry is
		   * fetched from the server in order to get the current edit link (for
		   * optimistic concurrency).
		   * 
		   * @param row the row number of the cell to operate on
		   * @param col the column number of the cell to operate on
		   * @param value the value to set in case of an update the cell to operate on
		   * 
		   * @throws ServiceException when the request causes an error in the Google
		   *         Spreadsheets service.
		   * @throws IOException when an error occurs in communication with the Google
		   *         Spreadsheets service.
		   */
		  private CellEntry createUpdateOperation(int row, int col, String value)
		      throws ServiceException, IOException {
			String batchId = "R" + row + "C" + col;
			URL entryUrl = new URL(cellFeedUrl.toString() + "/" + batchId);
			CellEntry entry = service.getEntry(entryUrl, CellEntry.class);
			entry.changeInputValueLocal(value);
			BatchUtils.setBatchId(entry, batchId);
			BatchUtils.setBatchOperationType(entry, BatchOperationType.UPDATE);
			
			return entry;
		  }
		
		/**
		 * Copies the offlineWorksheet data to the onlineWorksheet
		 * This method is replacing updateSheet, which is using a much slower method.
		 * @param onlineWorksheet
		 * @param offlineWorksheet
		 * @throws ServiceException 
		 * @throws IOException 
		 */
		public void updateSheet_(OnlineSpreadsheet onlineSpreadsheet, OnlineWorksheet onlineWorksheet, OfflineWorksheet offlineWorksheet) throws IOException, ServiceException{
			long startTime = System.currentTimeMillis();
		    
			service = drive.getAuthenticatedSpreadsheetService().getSpreadsheetService();
			service.setProtocolVersion(SpreadsheetService.Versions.V2);
			service.setHeader("If-Match", "*");
			
			cellFeedUrl = onlineWorksheet.getWorksheetEntry().getCellFeedUrl();
			System.out.println(cellFeedUrl.toString());
			cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
			
			CellFeed batchRequest = new CellFeed();
			
			@SuppressWarnings("rawtypes")
			Iterator it = offlineWorksheet.getData().entrySet().iterator();
		    while (it.hasNext()) {
		        @SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry)it.next();
		        Position pos = new Position((String) pairs.getKey());
		        if(pairs.getValue()!=null){
		        	CellEntry batchOperation = createUpdateOperation(pos.getY()+2, pos.getX()+2, (String) pairs.getValue());
		        	batchRequest.getEntries().add(batchOperation);
		        }
		        it.remove();
		    }
		    
		    System.out.println("Compiled the batch request");

		    CellFeed feed = service.getFeed(cellFeedUrl, CellFeed.class);
		    Link batchLink = feed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
		    URL batchUrl = new URL(batchLink.getHref());
		    CellFeed batchResponse = service.batch(batchUrl, batchRequest);
			
		    System.out.println("Submitted the batch request.");
		    
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
		    
		    offlineWorksheet.setModified(false);
		}
		
		/**
		 * Uploads the attendance to the spreadsheet
		 */
		public void uploadAttendance(){
			publishProgress("Opening remote Attendance spreadsheet...");
			
			//Initalize spreadsheets
			OnlineSpreadsheet onlineSpreadsheet = drive.getSpreadsheet(MarkTime.settings.getString("spreadsheet", ""));
			
			publishProgress("Loading local Attendance spreadsheet...");
			OfflineSpreadsheet offlineSpreadsheet = new OfflineSpreadsheet(onlineSpreadsheet.getName());
			offlineSpreadsheet.load(MarkTime.activity.getFilesDir()+"/"+onlineSpreadsheet.getName()+".db");
		    
			//Iterate thruogh the sheets and update them all (if they've been modified since last sync)
			for(Worksheet worksheet: offlineSpreadsheet.getWorksheets()){
				publishProgress("Updating '"+offlineSpreadsheet.getName()+":"+worksheet.getName()+"'!");
				OfflineWorksheet offlineWorksheet = (OfflineWorksheet) worksheet;
				OnlineWorksheet onlineWorksheet = getRemoteSheetToBeUpdated(onlineSpreadsheet, offlineWorksheet);
				if (onlineWorksheet == null){
					continue;
				}
				
				if (USING_BATCH_REQUESTS){
					System.out.println("EXPERIMENTAL! Using batch requests to update the spreadsheet");
					try {
						updateSheet_(onlineSpreadsheet, onlineWorksheet, offlineWorksheet);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				} else {
					updateSheet(onlineWorksheet, offlineWorksheet);
				}
			}
			
		}
	}
}
