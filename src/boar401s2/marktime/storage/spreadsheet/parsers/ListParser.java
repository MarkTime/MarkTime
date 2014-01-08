package boar401s2.marktime.storage.spreadsheet.parsers;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.util.Position;

public class ListParser {
	
	Worksheet worksheet;
	
	public ListParser(Worksheet worksheet){
		this.worksheet = worksheet;
	}
	
	public Worksheet getWorksheet(){
		return worksheet;
	}
	
	public List<String> parse(Position pos){// TODO When getCell(pos) is updated, use it here
		System.out.println("Parsing...");
		List<String> result = new ArrayList<String>();
		int yCounter = 0;
		while(true){
			yCounter++;
			Position newPos = new Position(0, yCounter);
			newPos.convertToSpreadsheetNotation();
			if(worksheet.getCell(newPos.getCell()).equalsIgnoreCase("")){
				break;
			} else {
				String value = worksheet.getCell(newPos.getCell());
				result.add(value);
			}
		}
		return result;
	}

}
