package boar401s2.marktime.storage.spreadsheet.worksheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.util.Position;

/**
 * Class that acts as a wrapper for the saved worksheet file
 * @author boar401s2
 */
public class OfflineWorksheet implements Worksheet{

	private Spreadsheet parent;
	private HashMap<String, String> data = new HashMap<String, String>();
	
	public OfflineWorksheet(String name, Spreadsheet parent){
		data.put("name", name);
		this.parent = parent;
	}
	
	/**
	 * Sets the raw data of the spreadsheet from a hashmap
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public void setData(HashMap<String, String> map){
		this.data = (HashMap<String, String>) map.clone();
	}
	
	/**
	 * Gets the spreadsheet raw data as a HashMap
	 * @return
	 */
	public HashMap<String, String> getData(){
		return data;
	}
	
	@Override
	public String getName() {
		if (data.containsKey("name")){
			return data.get("name");
		} else { return null; }
	}
	
	@Override
	public void setName(String name){
		data.put("name", name);
	}

	@Override
	public Spreadsheet getParent() {
		return parent;
	}
	
	@Override
	public String getCell(String cell) {
		if(!data.containsKey(cell)){
			return null;
		} else {
			return data.get(cell);
		}
	}
	
	@Deprecated
	@Override
	//TODO Need to fix, not working at last check
	public String getCell(Position pos) {
		String data = getCell(pos.getCell());
		return data;
	}

	@Override
	public void setCell(String cell, String value) {
		data.put(cell, value);
		setModified(true);
	}

	@Override
	public void setCell(Position pos, String value) {
		setCell(pos.getCell(), value);
	}

	@Override
	public void setSize(int width, int height){
		data.put("width", String.valueOf(width));
		data.put("height", String.valueOf(height));
	}
	
	
	
	@Override
	public int getHeight() {
		if (data.containsKey("height")){
			return Integer.valueOf(data.get("width"));
		} else { return -1; }
	}

	@Override
	public int getWidth() {
		if (data.containsKey("width")){
			return Integer.valueOf(data.get("height"));
		} else { return -1; }
	}

	@Override
	public boolean cellHasInformation(Position pos) {
		if (getCell(pos.getCell()).equalsIgnoreCase("") || !data.containsKey(pos.getCell())){
			return false;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks whether a cell exists in the HashMap
	 */
	public boolean cellExists(Position pos){
		return data.containsKey(pos.getCell());
	}
	
	/**
	 * Checks whether the worksheet has been modified since last upload
	 * @return
	 */
	public boolean hasBeenModified(){
		return Boolean.parseBoolean(data.get("modified"));
	}
	
	/**
	 * Sets whether the worksheet has been modified
	 * @param modified
	 */
	public void setModified(boolean modified){
		data.put("modified", String.valueOf(modified));
	}

	@Override
	public Date getModificationDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			System.out.println("Moddate: "+data.get("moddate"));
			return sdf.parse(data.get("moddate"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setModificationDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		data.put("moddate", sdf.format(date));
	}
	
	@Override
	public void deleteCell(String name){
		data.remove(name);
	}
}
