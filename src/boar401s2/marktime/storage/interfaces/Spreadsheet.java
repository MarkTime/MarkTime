package boar401s2.marktime.storage.interfaces;

import java.util.List;

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

}
