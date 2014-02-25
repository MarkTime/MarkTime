package boar401s2.marktime.storage.interfaces;

import java.util.Date;

import boar401s2.marktime.util.Position;

public interface Worksheet {

	/**
	 * Gets the name of the worksheet
	 * @return
	 */
	public String getName();
	
	/**
	 * Sets the name of the worksheet
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * Returns boolean for if cell exists
	 * @param pos
	 * @return
	 */
	public boolean cellExists(Position pos);
	
	/**
	 * Gets the worksheet's parent
	 * @return
	 */
	public Spreadsheet getParent();
	
	/**
	 * Gets the specified cell's value using it's string value
	 */
	public String getCell(String cell);
	
	/**
	 * Gets the specified cell's value using it's XY value
	 */
	public String getCell(Position pos);
	
	/**
	 * Sets the specified cell's value
	 * @param cell
	 * @param data
	 */
	public void setCell(Position pos, String value);
	
	/**
	 * Sets the cell's data
	 * @param cell
	 * @param data
	 */
	public void setCell(String cell, String value);
	
	/**
	 * Returns bool if cell contains information
	 */
	public boolean cellHasInformation(Position pos);
	
	/**
	 * Sets the size of the current worksheet
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height);
	
	/**
	 * Gets the current height of the worksheet
	 * @return
	 */
	public int getHeight();
	
	/**
	 * Gets the width fo the current worksheet
	 * @return
	 */
	public int getWidth();
	
	/**
	 * Gets the date of last modification
	 */
	public Date getModificationDate();
	
	/**
	 * Sets the date of last modification
	 */
	public void setModificationDate(Date date);

	/**
	 * Deletes a cell
	 * @param cell
	 */
	public void deleteCell(String cell);
}
