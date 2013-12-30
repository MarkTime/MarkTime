package boar401s2.marktime.storage.spreadsheet.worksheet;

import java.io.IOException;
import java.net.URL;

import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.OnlineSpreadsheet;
import boar401s2.marktime.util.Position;

public class OnlineWorksheet implements Worksheet{
	
    WorksheetEntry worksheet;
    CellFeed feed;
    URL cellFeedURL;
    Spreadsheet parent;
	
	public OnlineWorksheet(WorksheetEntry worksheet, OnlineSpreadsheet parent){
        this.worksheet = worksheet;
        this.parent = parent;
        cellFeedURL = worksheet.getCellFeedUrl();
        try {
                feed = parent.spreadsheetService.getSpreadsheetService().getFeed(cellFeedURL, CellFeed.class);
        } catch (IOException e) {
                e.printStackTrace();
        } catch (ServiceException e) {
                e.printStackTrace();
        }
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
	public void setCell(Position pos, String data) {
        try {
	        CellEntry entry = new CellEntry(pos.getX(), pos.getY(), data);
	        feed.insert(entry);
	    } catch (ServiceException e) {
            e.printStackTrace();
	    } catch (IOException e) {
            e.printStackTrace();
	    }	
	}

	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean cellHasInformation(String cell){
        return getCell(cell).length()>0;
	}

	@Override
	public boolean cellHasInformation(Position pos) {
		// TODO Auto-generated method stub
		return false;
	}

}
