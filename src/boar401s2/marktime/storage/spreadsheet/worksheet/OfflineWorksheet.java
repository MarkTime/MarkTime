package boar401s2.marktime.storage.spreadsheet.worksheet;

import java.util.HashMap;

import com.google.gdata.data.DateTime;

import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.util.Position;

public class OfflineWorksheet implements Worksheet{

	private Spreadsheet parent;
	private HashMap<String, String> data = new HashMap<String, String>();
	
	public OfflineWorksheet(String name, Spreadsheet parent){
		data.put("name", name);
		this.parent = parent;
	}
	
	@SuppressWarnings("unchecked")
	public void setData(HashMap<String, String> map){
		this.data = (HashMap<String, String>) map.clone();
	}
	
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
	public Spreadsheet getParent() {
		return parent;
	}
	
	@Override
	public String getCell(String cell) {
		return data.get(cell);
	}
	
	@Override
	public String getCell(Position pos) {
		return null;
	}

	@Override
	public void setCell(String cell, String value) {
		data.put(cell, value);
	}

	@Override
	public void setCell(Position pos, String value) {
		pos.convertToSpreadsheetNotation();
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
		return false;
	}

	@Override
	public DateTime getModificationDate() {
		return DateTime.parseDate(data.get("moddate"));
	}

	@Override
	public void setModificationDate(DateTime date) {
		data.put("moddate", date.toString());
	}
}
