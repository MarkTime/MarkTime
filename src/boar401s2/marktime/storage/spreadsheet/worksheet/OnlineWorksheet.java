package boar401s2.marktime.storage.spreadsheet.worksheet;

import java.io.IOException;

import com.google.gdata.data.Content;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.model.atom.TextContent;
import com.google.gdata.util.ServiceException;

import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.OnlineSpreadsheet;
import boar401s2.marktime.util.Position;

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
		pos.convertToSpreadsheetNotation();
		return getCell(pos.getCell());
	}

	@Override
	public void setCell(String cell, String data) {
		Position pos = new Position(cell);
		pos.convertToCartesian();
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
	public DateTime getModificationDate() {
		return worksheet.getEdited();
	}
	
	public CellFeed getCellFeed() throws IOException, ServiceException{
		return parent.getService().getSpreadsheetService().getFeed(worksheet.getCellFeedUrl(), CellFeed.class);
	}

	@Override
	public void setModificationDate(DateTime date) {
		worksheet.setEdited(date);
	}

	@Override
	public void setName(String name) {
		
	}

	@Override
	public boolean cellExists(Position pos) {
		return false;
	}
}
