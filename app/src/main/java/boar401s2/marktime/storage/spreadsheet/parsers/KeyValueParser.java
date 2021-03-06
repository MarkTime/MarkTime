package boar401s2.marktime.storage.spreadsheet.parsers;

import java.util.HashMap;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.util.Position;

/**
 * Parser for parsing a HashMap from a Worksheet
 * @author boar401s2
 */
public class KeyValueParser {
	
	Worksheet worksheet;
	Position startingPos;
	HashMap<String, String> keys = new HashMap<String, String>();
	
	
	public KeyValueParser(Worksheet worksheet, String startingCell){
		this.worksheet = worksheet;
		this.startingPos = new Position(startingCell);
	}
	
	/**
	 * Map all the keys
	 */
	public void mapKeys(){
		Position newPos = new Position(startingPos);
		newPos.setY(newPos.getY()+1);
		while(true){
			if(!worksheet.getCell(newPos.getCell()).equalsIgnoreCase("")){
				keys.put(worksheet.getCell(newPos.getCell()), newPos.getCell());
				System.out.println(worksheet.getCell(newPos.getCell()));
			} else {
				break;
			}
			newPos = new Position(newPos.getCell());
			newPos.setX(newPos.getX()+1);
		}
	}
	
	/**
	 * Gets a value from a key
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		Position pos = new Position(keys.get(key));
		pos.setY(pos.getY()+1);
		return worksheet.getCell(pos.getCell());
	}
	
	/**
	 * Sets a value at a key
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value){
		Position pos = new Position(keys.get(key));
		pos.setY(pos.getY()+1);
		worksheet.setCell(pos.getCell(), value);
	}

}
