package boar401s2.marktime.storage.spreadsheet.worksheet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.OnlineSpreadsheet;
import boar401s2.marktime.util.Position;

/**
 * Class that acts as a wrapper for the Google Drive worksheets
 * @author boar401s2
 */
public class OnlineWorksheet implements Worksheet{
	
    WorksheetEntry worksheet;
    CellFeed feed;
    OnlineSpreadsheet parent;
	
	public OnlineWorksheet(WorksheetEntry worksheet, OnlineSpreadsheet parent){
        this.worksheet = worksheet;
        this.parent = parent;
        try {
        	feed = getCellFeed();
        } catch (IOException e) {
                e.printStackTrace();
        } catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the google drive worksheet entry
	 * @return
	 */
	public WorksheetEntry getWorksheetEntry(){
		return worksheet;
	}

	@Override
	public String getName() {
		return worksheet.getTitle().getPlainText();
	}

	@Override
	public Spreadsheet getParent() {
		return parent;
	}

	@Override
	public String getCell(String cell) {
		for(CellEntry entry: feed.getEntries()){
	        if (entry.getTitle().getPlainText().equalsIgnoreCase(cell)){
	        	return entry.getCell().getValue();
	        }
	    }
	    return "";
	}
	
	@Override
	public String getCell(Position pos) {
		return getCell(pos.getCell());
	}

	@Override
	public void setCell(String cell, String data) {
		Position pos = new Position(cell);
		setCell(pos, data);
	}

	@Override
	public void setCell(Position pos, String data) {
        try {
	        CellEntry entry = new CellEntry(pos.getY(), pos.getX()+1, data);
	        parent.spreadsheetService.getSpreadsheetService().insert(worksheet.getCellFeedUrl(), entry);
	    } catch (ServiceException e) {
            e.printStackTrace();
	    } catch (IOException e) {
            e.printStackTrace();
	    }	
	}
	
	/**
	 * Creates a cell on the OnlineSpreadsheet
	 * @param cell
	 * @param data
	 * @return
	 */
	public CellEntry createCell(String cell, String data){
		Position pos = new Position(cell);
		CellEntry entry = new CellEntry(pos.getY(), pos.getX()+1, data);
		return entry;
	}

	@Override
	public void setSize(int width, int height) {
		worksheet.setColCount(width);
		worksheet.setRowCount(height);
	}

	@Override
	public int getHeight() {
		return worksheet.getRowCount();
	}

	@Override
	public int getWidth() {
		return worksheet.getColCount();
	}
	
	public boolean cellHasInformation(String cell){
        return getCell(cell).length()>0;
	}

	@Override
	public boolean cellHasInformation(Position pos) {
		return false;
	}

	@Override
	public Date getModificationDate() { //yyyy-MM-dd HH:mm:ss
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {
			SimpleDateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String oldDate = worksheet.getUpdated().toString();
			Date date = formatter.parse(oldDate.substring(0, 24));
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the cell feed
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 */
	public CellFeed getCellFeed() throws IOException, ServiceException{
		return parent.getService().getSpreadsheetService().getFeed(worksheet.getCellFeedUrl(), CellFeed.class);
	}

	@Override
	public void setModificationDate(Date date) {
		DateTime dt = new DateTime(date);
		worksheet.setEdited(dt);
	}

	@Override
	public void setName(String name) {
		
	}

	@Override
	public boolean cellExists(Position pos) {
		return false;
	}
	
	@Override
	public void deleteCell(String cell){
		
	}
}
