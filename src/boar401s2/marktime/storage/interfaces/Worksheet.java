package boar401s2.marktime.storage.interfaces;

public interface Worksheet {

	/**
	 * Gets the name of the worksheet
	 * @return
	 */
	public String getName();
	
	/**
	 * Gets the worksheet's parent
	 * @return
	 */
	public Spreadsheet getParent();
	
	/**
	 * Gets the specified cell's value
	 */
	public String getCell(String cell);
	
	/**
	 * Sets the specified cell's value
	 * @param cell
	 * @param data
	 */
	public void setCell(String cell, String data);
	
	/**
	 * Returns bool if cell contains information
	 */
	public boolean cellHasInformation(String cell);
	
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

}
