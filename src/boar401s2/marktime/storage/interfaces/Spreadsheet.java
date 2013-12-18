package boar401s2.marktime.storage.interfaces;

public interface Spreadsheet {
	
	public String getName();
	public Worksheet getWorksheet(String name);
	
	public int getWorksheets();
	public int getNumberOfWorksheets();
	
	public void refresh();
	public void save();
	public void load(String address);

}
