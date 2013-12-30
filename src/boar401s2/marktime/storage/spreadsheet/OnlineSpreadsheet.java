package boar401s2.marktime.storage.spreadsheet;

import java.util.HashMap;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;

import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.worksheet.OnlineWorksheet;

public class OnlineSpreadsheet implements Spreadsheet{
	
	HashMap<String, OnlineWorksheet> map = new HashMap<String, OnlineWorksheet>();
	SpreadsheetService service;
    GDrive parent;
    public AuthenticatedSpreadsheetService spreadsheetService;
	
	public OnlineSpreadsheet(GDrive parent, AuthenticatedSpreadsheetService spreadsheetService){
		this.parent = parent;
		this.spreadsheetService = spreadsheetService;
	}

	@Override
	public String getName() {
		
		return null;
	}

	@Override
	public Worksheet getWorksheet(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Worksheet> getWorksheets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfWorksheets() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(String source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getParentFolder() {
		return null;
	}

}
