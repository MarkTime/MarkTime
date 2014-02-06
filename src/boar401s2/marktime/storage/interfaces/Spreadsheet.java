package boar401s2.marktime.storage.interfaces;

import java.util.List;

import com.google.gdata.data.spreadsheet.WorksheetEntry;

public interface Spreadsheet {
	
	/**
	 * Gets the name of the spreadsheet
	 * @return
	 */
	public String getName();
	
	/**
	 * Gets the parent folder
	 */
	public String getParentFolder();
	
	/**
	 * Gets a worksheet in the spreadsheet
	 * @param name
	 * @return
	 */
	public Worksheet getWorksheet(String name);
	
	/**
	 * Get list of the worksheet names
	 * @return
	 */
	public List<String> getWorksheetNames();
	
	/**
	 * Gets a list of the current worksheets
	 * @return
	 */
	public List<Worksheet> getWorksheets();
	
	/**
	 * Gets the number of worksheets in the spreadsheet
	 * @return
	 */
	public int getNumberOfWorksheets();
	
	/**
	 * Returns of the specified worksheet exists
	 * @param name Name of spreadsheet to find out if exists
	 * @return
	 */
	public boolean worksheetExists(String name);
	
	/**
	 * Creates a worksheet
	 * @param name Name of the worksheet to create
	 * @return 
	 */
	public void createWorksheet(String name);
	
	/**
	 * Refreshes the spreadsheet from the source
	 */
	public void refresh();
	
	/**
	 * Saves spreadsheet to source
	 */
	public void save(String address);
	
	/**
	 * Loads spreadsheet from source
	 * @param address
	 */
	public void load(String source);
	
	/**
	 * Used to duplicate a certain sheet
	 * @param sheet Sheet to duplicate
	 * @param name Name of the new sheet
	 */
	public void duplicateSheet(String sheet, String name);

}
