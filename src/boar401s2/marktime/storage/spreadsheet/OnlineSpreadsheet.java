package boar401s2.marktime.storage.spreadsheet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.worksheet.OnlineWorksheet;

public class OnlineSpreadsheet implements Spreadsheet{
	
	SpreadsheetService service;
    GDrive parent;
    public AuthenticatedSpreadsheetService spreadsheetService;
    public SpreadsheetEntry spreadsheet;
	
	public HashMap<String, OnlineWorksheet> mapOfWorksheets = new HashMap<String, OnlineWorksheet>();
    List<Worksheet> listOfWorksheets = new ArrayList<Worksheet>();
	
	public OnlineSpreadsheet(GDrive parent, SpreadsheetEntry spreadsheet){
		this.parent = parent;
		this.spreadsheetService = parent.getAuthenticatedSpreadsheetService();
		this.spreadsheet = spreadsheet;
		
		try {
			compileMapOfWorksheets();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return spreadsheet.getTitle().getPlainText();
	}
	
	public List<String> getAuthors(){
		List<String> authors = new ArrayList<String>();
		for (Person p: spreadsheet.getAuthors()){
			authors.add(p.getName());
		}
		return authors;
	}

	@Override
	public Worksheet getWorksheet(String name) {
		return mapOfWorksheets.get(name);
	}

	@Override
	public List<Worksheet> getWorksheets() {
		return listOfWorksheets;
	}
	
	@Override
	public List<String> getWorksheetNames(){
		List<String> names = new ArrayList<String>();
		for(Worksheet worksheet: getWorksheets()){
			names.add(worksheet.getName());
		}
		return names;
	}

	@Override
	public int getNumberOfWorksheets() {
		return mapOfWorksheets.size();
	}

	@Override
	public void refresh() {
		try {
			compileMapOfWorksheets();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(String address) {
	}

	@Override
	public void load(String source) {
	}

	/**
	 * Not implemented yet!
	 */
	@Override
	public String getParentFolder() {
		return null;
	}
	
	/**
	 * Fills out mapOfWorksheets with Title,Worksheet pairs
	 * @throws IOException
	 * @throws ServiceException
	 */
	public void compileMapOfWorksheets() throws IOException, ServiceException{
		for(WorksheetEntry worksheet: spreadsheet.getWorksheets()){
			mapOfWorksheets.put(worksheet.getTitle().getPlainText(), new OnlineWorksheet(worksheet, this));
		}
		compileListOfWorksheets();
	}
	
	/**
	 * Populates listOfWorksheets with worksheets
	 * Pre-req: mapOfWorksheets to have been populated
	 * @throws IOException
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	public void compileListOfWorksheets() throws IOException, ServiceException{
		listOfWorksheets.clear();
		@SuppressWarnings("unchecked")
		HashMap<String, OnlineWorksheet> cloned = (HashMap<String, OnlineWorksheet>) mapOfWorksheets.clone();
		Iterator it = cloned.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        listOfWorksheets.add((OnlineWorksheet) pairs.getValue());
	        it.remove();
	    }
	}
	
	public AuthenticatedSpreadsheetService getService(){
		return spreadsheetService;
	}

	@Override
	public void duplicateSheet(String sheet, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean worksheetExists(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createWorksheet(String name) {
		WorksheetEntry worksheet = new WorksheetEntry();
	    worksheet.setTitle(new PlainTextConstruct(name));
	    worksheet.setColCount(20);
	    worksheet.setRowCount(20);
	    URL worksheetFeedUrl = spreadsheet.getWorksheetFeedUrl();

	    try {
			parent.getAuthenticatedSpreadsheetService().getSpreadsheetService().insert(worksheetFeedUrl, worksheet);
			parent.getAuthenticatedSpreadsheetService().getSpreadsheetFeed();
	    } catch (IOException e1) {
			e1.printStackTrace();
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
	    try {
			compileMapOfWorksheets();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
