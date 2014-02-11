package boar401s2.marktime.storage.spreadsheet.parsers;

import java.util.HashMap;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.util.Position;

public class KeyValueParser {
	
	Worksheet worksheet;
	Position startingPos;
	HashMap<String, String> keys = new HashMap<String, String>();
	
	
	public KeyValueParser(Worksheet worksheet, String startingCell){
		this.worksheet = worksheet;
		this.startingPos = new Position(startingCell);
		this.startingPos.convertToCartesian();
	}
	
	public void mapKeys(){
		Position newPos = new Position(startingPos);
		newPos.setY(newPos.getY()+1);
		while(true){
			newPos.convertToSpreadsheetNotation();
			if(!worksheet.getCell(newPos.getCell()).equalsIgnoreCase("")){
				keys.put(worksheet.getCell(newPos.getCell()), newPos.getCell());
				System.out.println(worksheet.getCell(newPos.getCell()));
			} else {
				break;
			}
			newPos = new Position(newPos.getCell());
			newPos.convertToCartesian();
			newPos.setX(newPos.getX()+1);
		}
	}
	
	public String getValue(String key){
		Position pos = new Position(keys.get(key));
		pos.convertToCartesian();
		pos.setY(pos.getY()+1);
		pos.convertToSpreadsheetNotation();
		return worksheet.getCell(pos.getCell());
	}
	
	public void setValue(String key, String value){
		Position pos = new Position(keys.get(key));
		pos.convertToCartesian();
		pos.setY(pos.getY()+1);
		pos.convertToSpreadsheetNotation();
		worksheet.setCell(pos.getCell(), value);
	}

}
