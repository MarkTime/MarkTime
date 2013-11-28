package boar401s2.marktime.storage.spreadsheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.GDrive;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

/**
 * A handler for handling spreadsheets
 */
public class Spreadsheet {
	
	SpreadsheetEntry spreadsheet;
	List<WorksheetEntry> worksheets;
	SpreadsheetService service;
	GDrive parent;
	
	public Spreadsheet(SpreadsheetEntry spreadsheet, GDrive parent) throws IOException, ServiceException{
		this.spreadsheet = spreadsheet;
		this.worksheets = spreadsheet.getWorksheets();
		this.parent = parent;
	}
	
	/**
	 * Gets a list of the worksheets in the 
	 * current spreadsheet
	 * @return List of worksheets
	 */
	public List<String> getWorksheetNames(){
		List<String> names = new ArrayList<String>();
		for (WorksheetEntry entry: worksheets){
			names.add(entry.getTitle().getPlainText());
		}
		return names;
	}
	
	/**
	 * Gets the ID of the worksheet in the 
	 * list of worksheets
	 * @param worksheet
	 * @return
	 */
	public int getWorksheetID(String worksheet){
		return getWorksheetNames().indexOf(worksheet);
	}
	
	/**
	 * Gets a worksheet by it's name
	 * @param worksheet It's name
	 * @return A worksheet instance
	 */
	public Worksheet getWorksheet(String worksheet){
		return new Worksheet(worksheets.get(getWorksheetID(worksheet)), this);
	}

}
