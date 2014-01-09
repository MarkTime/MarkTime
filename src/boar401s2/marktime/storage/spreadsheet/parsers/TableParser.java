package boar401s2.marktime.storage.spreadsheet.parsers;

import java.util.HashMap;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.util.Position;

/**
 * 2D array parser
 * @author student
 */
public class TableParser {

	Worksheet worksheet;
	HashMap<String, HashMap<String, String>> table = new HashMap<String, HashMap<String, String>>();
	
	public TableParser(Worksheet worksheet){
		this.worksheet = worksheet;
	}
	
	public void parse(){
		System.out.println(worksheet);
		System.out.println(worksheet.getName());
		System.out.println(worksheet.getHeight());
		System.out.println(worksheet.getWidth());
		HashMap<String, String> namedColumns = new HashMap<String, String>();
		int xCounter = 0;
		while(true){ //Scan through columns adding their titles to the hashmap
			xCounter++;
			Position pos = new Position(xCounter, 0);
			if(worksheet.getCell(pos).equalsIgnoreCase("") || worksheet.cellHasInformation(pos)==false){
				break;
			} else {
				System.out.println(pos.getString());
				namedColumns.put(worksheet.getCell(pos), "");
				System.out.println(worksheet.getCell(pos));
			}
		}
		
		System.out.println("Scanned through columns!");
		
		int yCounter = 0;
		while(true){ //Scan down the rows adding them to the hashmap
			yCounter++;
			Position pos = new Position(0, yCounter);
			if(worksheet.getCell(pos).equalsIgnoreCase("") || worksheet.cellHasInformation(pos)==false){
				break;
			} else {
				System.out.println(pos.getString());
				table.put(worksheet.getCell(pos), (HashMap<String, String>) namedColumns.clone());
				System.out.println(worksheet.getCell(pos));
			}
		}
		
		System.out.println("Scanned through rows!");
		
		System.out.println("Finished!");
	}
	
	public String getValue(String namedRow, String namedColumn){
		return "";
	}
	
}
