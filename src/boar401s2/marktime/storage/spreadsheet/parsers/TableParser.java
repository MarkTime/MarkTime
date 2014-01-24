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
 * 2D array parser
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
	
	public void mapCells(){
		int xCounter = 0;
		for(int i=0; i<worksheet.getWidth(); i++){ //Scan through columns adding their titles to the hashmap
			xCounter++;
			Position pos = new Position(xCounter, 0);
			pos.convertToSpreadsheetNotation();
			if(worksheet.getCell(pos.getCell())==null){
				break;
			} else {
				columnLookup.put(worksheet.getCell(pos.getCell()), Integer.toString(xCounter));
			}
		}
		
		int yCounter = 0;
		for(int i=worksheet.getHeight(); i<30; i++){ //Scan down the rows adding them to the hashmap
			yCounter++;
			Position pos = new Position(0, yCounter);
			pos.convertToSpreadsheetNotation();
			if(worksheet.getCell(pos.getCell())==null){
				break;
			} else {
				System.out.println(worksheet.getCell(pos.getCell()));
				rowLookup.put(worksheet.getCell(pos.getCell()), Integer.toString(yCounter));
			}
		}
	}
	
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
	
	@SuppressWarnings("rawtypes")
	public int getNextUnusedRow(){
		List<Integer> rows = new ArrayList<Integer>();
		rows.add(0);
	    Iterator it = rowLookup.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        rows.add((Integer) pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    return Collections.max(rows) + 1;
	}
	
	public Position getCellPosition(String row, String column){
		if (rowLookup.containsKey(row)){
			return new Position(Integer.getInteger(columnLookup.get(column)), Integer.getInteger(rowLookup.get(row)));
		} else {
			return new Position(Integer.valueOf(columnLookup.get(column)), getNextUnusedRow());
		}
	}
	
	public String getValue(String row, String column){
		Position pos = getCellPosition(row, column);
		pos.convertToSpreadsheetNotation();
		return worksheet.getCell(pos.getCell());
	}
	
	public void setValue(String row, String column, String value){
		Position pos = getCellPosition(row, column);
		pos.convertToSpreadsheetNotation();
		worksheet.setCell(pos.getCell(), value);
	}
	
	public void insertRow(String name){
		int y = nextAvaliableRow();
		rowLookup.put(name, Integer.toString(y));
		Position pos = new Position(0, y);
		pos.convertToSpreadsheetNotation();
		worksheet.setCell(pos.getCell(), name);
	}
}
