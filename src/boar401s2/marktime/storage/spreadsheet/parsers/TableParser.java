package boar401s2.marktime.storage.spreadsheet.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.util.Position;

/**
 * 2D spreadsheet array parser
 * @author student
 */
public class TableParser {

	Worksheet worksheet;
	HashMap<String, String> columnLookup = new HashMap<String, String>();
	HashMap<String, String> rowLookup = new HashMap<String, String>();
	
	HashMap<String, String> namedColumn;
	
	public TableParser(Worksheet worksheet){
		this.worksheet = worksheet;
		mapCells();
	}
	
	/**
	 * Maps all the values
	 */
	public void mapCells(){
		int xCounter = 0;
		for(int i=0; i<worksheet.getWidth(); i++){ //Scan through columns adding their titles to the hashmap
			Position pos = new Position(xCounter, 0);
			pos.convertToSpreadsheetNotation();
			if(worksheet.getCell(pos.getCell())==null){
				break;
			} else {
				columnLookup.put(worksheet.getCell(pos.getCell()), Integer.toString(xCounter));
			}
			xCounter++;
		}
		
		int yCounter = 0;
		for(int i=0; i<30; i++){ //Scan down the rows adding them to the hashmap
			yCounter++;
			Position pos = new Position(0, yCounter);
			pos.convertToSpreadsheetNotation();
			if(worksheet.getCell(pos.getCell())==null){
				break;
			} else {
				rowLookup.put(worksheet.getCell(pos.getCell()), Integer.toString(yCounter));
			}
		}
	}
	
	/**
	 * Gets the first avalaiable row
	 * @return
	 */
	public int nextAvaliableRow(){
		int yCounter = 0;
		for(int i=worksheet.getHeight(); i<30; i++){ //Scan down the rows adding them to the hashmap
			yCounter++;
			Position pos = new Position(0, yCounter);
			pos.convertToSpreadsheetNotation();
			if(worksheet.getCell(pos.getCell())==null){
				return yCounter;
			}
		}
		return 1;
	}
	
	/**
	 * Gets the next unused row
	 * @return
	 */
	//TODO What's the difference betwene this and nextAvaliableRow??
	@SuppressWarnings("rawtypes")
	public int getNextUnusedRow(){
		List<Integer> rows = new ArrayList<Integer>();
		rows.add(0);
	    Iterator it = rowLookup.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        rows.add(Integer.valueOf((String) pairs.getValue()));
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    return Collections.max(rows) + 1;
	}
	
	/**
	 * Gets a cell position from a (named) row and column
	 * @param row
	 * @param column
	 * @return
	 */
	public Position getCellPosition(String row, String column){
		if (rowLookup.containsKey(row)){
			return new Position(Integer.parseInt((String) columnLookup.get(column)), Integer.parseInt((String) rowLookup.get(row)));
		} else {
			int r = getNextUnusedRow();
			rowLookup.put(row, String.valueOf(r));
			return new Position(Integer.parseInt((String) columnLookup.get(column)), r);
		}
	}
	
	/**
	 * Gets a value from the (named) row and column
	 * @param row
	 * @param column
	 * @return
	 */
	public String getValue(String row, String column){
		Position pos = getCellPosition(row, column);
		pos.convertToSpreadsheetNotation();
		return worksheet.getCell(pos.getCell());
	}
	
	/**
	 * Sets the value at a row and column
	 * @param row
	 * @param column
	 * @param value
	 */
	public void setValue(String row, String column, String value){
		Position pos = getCellPosition(row, column);
		pos.convertToSpreadsheetNotation();
		worksheet.setCell(pos.getCell(), value);
	}
	
	/**
	 * Sets the name of a row, at it's raw ID
	 * @param name
	 * @param row
	 */
	public void setRow(String name, int row){
		rowLookup.put(name, String.valueOf(row));
		Position pos = new Position(0, row);
		pos.convertToSpreadsheetNotation();
		worksheet.setCell(pos.getCell(), name);
	}
}
