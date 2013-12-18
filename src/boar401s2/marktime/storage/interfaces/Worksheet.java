package boar401s2.marktime.storage.interfaces;

public interface Worksheet {

	public String getName();
	public Spreadsheet getParent();
	
	public String getCell(String cell);
	public void setCell(String cell, String data);
	
	public void setSize(int width, int height);
	public int getHeight();
	public int getWidth();

}
