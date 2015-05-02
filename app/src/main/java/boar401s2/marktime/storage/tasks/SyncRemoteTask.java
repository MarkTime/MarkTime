package boar401s2.marktime.storage.tasks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.Link;
import com.google.gdata.util.ServiceException;

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
import boar401s2.marktime.util.Position;


public class SyncRemoteTask {
	
	AsyncTaskParent parent;
	GDrive drive;
	OnlineWorksheet worksheet;
	SyncRemote task;
	
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
		
		public URL getBatchCellFeedURL(OnlineSpreadsheet onlineSpreadsheet){
			URL cellFeedUrl = null;
			FeedURLFactory urlFactory = FeedURLFactory.getDefault();
			try{
				cellFeedUrl = urlFactory.getCellFeedUrl(onlineSpreadsheet.spreadsheet.getKey(), "od6", "private", "full");
			} catch (MalformedURLException ex){
				ex.printStackTrace();
			}
			return cellFeedUrl;
		}
		
		/**
		 * Gets the feed used in a batch request
		 * @param onlineSpreadsheet
		 * @return
		 */
		public CellFeed getBatchCellFeed(OnlineSpreadsheet onlineSpreadsheet){
			CellFeed cellFeed = null;
			URL cellFeedUrl = null;
			try {
				cellFeedUrl = getBatchCellFeedURL(onlineSpreadsheet);
				cellFeed = drive.getAuthenticatedSpreadsheetService().getSpreadsheetService().getFeed(cellFeedUrl, CellFeed.class);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			return cellFeed;
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
		 * Copies the offlineWorksheet data to the onlineWorksheet
		 * This method is replacing updateSheet, which is using a much slower method.
		 * @param onlineWorksheet
		 * @param offlineWorksheet
		 * @throws ServiceException 
		 * @throws IOException 
		 */
		@SuppressWarnings("rawtypes")
		public void updateSheet_(OnlineSpreadsheet onlineSpreadsheet, OnlineWorksheet onlineWorksheet, OfflineWorksheet offlineWorksheet) throws IOException, ServiceException{
			long startTime = System.currentTimeMillis();
			List<Position> cellAddrs = new ArrayList<Position>();
			SpreadsheetService service = drive.getAuthenticatedSpreadsheetService().getSpreadsheetService();
			System.out.println("Generating a list of the cells that need updating!");
			Iterator it = offlineWorksheet.getData().entrySet().iterator();
		    while (it.hasNext()) {	   
				Map.Entry pairs = (Map.Entry)it.next();
				cellAddrs.add(new Position((String) pairs.getKey()));				
		        it.remove();
		    }		
		    
		    Map<String, CellEntry> cellEntries = getCellEntryMap(service, onlineSpreadsheet, cellAddrs);
		    
		    System.out.println("Generating batch request!");
		    CellFeed batchRequest = new CellFeed();
		    for (Position cellAddr : cellAddrs) {
		      URL entryUrl = new URL(getBatchCellFeedURL(onlineSpreadsheet).toString() + "/" + cellAddr.getCell());
		      CellEntry batchEntry = new CellEntry(cellEntries.get(cellAddr.getCell()));
		      batchEntry.changeInputValueLocal(cellAddr.getCell());
		      BatchUtils.setBatchId(batchEntry, cellAddr.getCell());
		      BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
		      batchRequest.getEntries().add(batchEntry);
		    }
		    
		    System.out.println("Submitting the request");
		    
		    // Submit the update
		    Link batchLink = getBatchCellFeed(onlineSpreadsheet).getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
		    CellFeed batchResponse = service.batch(new URL(batchLink.getHref()), batchRequest);

		    System.out.println("Getting the results of our arduous efforts!");
		    
		    // Check the results
		    boolean isSuccess = true;
		    for (CellEntry entry : batchResponse.getEntries()) {
		      String batchId = BatchUtils.getBatchId(entry);
		      if (!BatchUtils.isSuccess(entry)) {
		        isSuccess = false;
		        BatchStatus status = BatchUtils.getBatchStatus(entry);
		        System.out.printf("%s failed (%s) %s", batchId, status.getReason(), status.getContent());
		      }
		    }

		    System.out.println(isSuccess ? "\nBatch operations successful." : "\nBatch operations failed");
		    System.out.printf("\n%s ms elapsed\n", System.currentTimeMillis() - startTime);
		    
		    offlineWorksheet.setModified(false);
		}
		
		/**
		   * Connects to the specified {@link SpreadsheetService} and uses a batch
		   * request to retrieve a {@link CellEntry} for each cell enumerated in {@code
		   * cellAddrs}. Each cell entry is placed into a map keyed by its RnCn
		   * identifier.
		   *
		   * @param ssSvc the spreadsheet service to use.
		   * @param cellFeedUrl url of the cell feed.
		   * @param cellAddrs list of cell addresses to be retrieved.
		   * @return a map consisting of one {@link CellEntry} for each address in {@code
		   *         cellAddrs}
		   */
		  public Map<String, CellEntry> getCellEntryMap(
		      SpreadsheetService ssSvc, OnlineSpreadsheet onlineSpreadsheet, List<Position> cellAddrs)
		      throws IOException, ServiceException {
			System.out.println("Generating cellEntryMap!");
		    CellFeed batchRequest = new CellFeed();
		    for (Position cellId : cellAddrs) {
		      CellEntry batchEntry = new CellEntry(cellId.getY(), cellId.getX(), cellId.getCell());
		      batchEntry.setId(String.format("%s/%s", getBatchCellFeedURL(onlineSpreadsheet).toString(), cellId.getCell()));
		      BatchUtils.setBatchId(batchEntry, cellId.getCell());
		      BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.QUERY);
		      batchRequest.getEntries().add(batchEntry);
		    }
		    
		    System.out.println("Generated map...");

		    CellFeed cellFeed = getBatchCellFeed(onlineSpreadsheet);
		    System.out.println("Got the cell feed");
		    CellFeed queryBatchResponse =
		      ssSvc.batch(new URL(cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM).getHref()),
		                  batchRequest);

		    System.out.println("Printing map!");
		    Map<String, CellEntry> cellEntryMap = new HashMap<String, CellEntry>(cellAddrs.size());
		    for (CellEntry entry : queryBatchResponse.getEntries()) {
		      cellEntryMap.put(BatchUtils.getBatchId(entry), entry);
		      System.out.printf("batch %s {CellEntry: id=%s editLink=%s inputValue=%s\n",
		          BatchUtils.getBatchId(entry), entry.getId(), entry.getEditLink().getHref(),
		          entry.getCell().getInputValue());
		    }

		    return cellEntryMap;
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
					try {
						System.out.println("EXPERIMENTAL! Using batch requests to update the spreadsheet");
						updateSheet_(onlineSpreadsheet, onlineWorksheet, offlineWorksheet);
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Failed");
					} catch (ServiceException e) {
						e.printStackTrace();
						System.out.println("Failed");
					}
				} else {
					updateSheet(onlineWorksheet, offlineWorksheet);
				}
			}
			
		}
	}
}
