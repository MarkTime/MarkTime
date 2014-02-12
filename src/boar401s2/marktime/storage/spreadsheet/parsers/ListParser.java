package boar401s2.marktime.storage.spreadsheet.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.util.Position;

public class ListParser {
	
	Worksheet worksheet;
	List<String> values;
	
	public ListParser(Worksheet worksheet){
		this.worksheet = worksheet;
	}
	
	public Worksheet getWorksheet(){
		return worksheet;
	}
	
	public void parse(){// TODO When getCell(pos) is updated, use it here
		List<String> result = new ArrayList<String>();
		int yCounter = 0;
		while(true){
			yCounter++;
			Position newPos = new Position(0, yCounter);
			newPos.convertToSpreadsheetNotation();
			try{
				if(worksheet.getCell(newPos.getCell())==null){
					break;
				} else {
					String value = worksheet.getCell(newPos.getCell());
					if(!value.equalsIgnoreCase("")){
						result.add(value);
					}
				}
			} catch (Exception ex){
				break;
			}
		}
		values = result;
	}
	
	public void sort(){
		parse();
		List<String> vals = getValues();
		Collections.sort(vals);
		
		int yCounter = 1;
		while(true){
			Position newPos = new Position(0, yCounter);
			newPos.convertToSpreadsheetNotation();
			try{
				if(yCounter <= vals.size()){
					worksheet.setCell(newPos.getCell(), vals.get(yCounter-1));
				} else {
					worksheet.setCell(newPos.getCell(), "");
				}
			} catch (Exception ex){
				break;
			}
			yCounter++;
		}
		values = vals;
	}
	
	public List<String> getValues(){
		return values;
	}
	
	public void addValue(String value){
		worksheet.setCell("A"+String.valueOf(values.size()+2), value);
		parse();
	}
	
	public void setValue(Integer id, String value){
		worksheet.setCell("A"+String.valueOf(id+1), value);
		parse();
	}

}
