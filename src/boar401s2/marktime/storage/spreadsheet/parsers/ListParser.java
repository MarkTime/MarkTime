package boar401s2.marktime.storage.spreadsheet.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.util.Position;

/**
 * List Parser for parsing a list from a sheet
 * @author student
 *
 */
public class ListParser {
	
	Worksheet worksheet;
	List<String> values;
	
	public ListParser(Worksheet worksheet){
		this.worksheet = worksheet;
	}
	
	/**
	 * Get the parent worksheet
	 * @return
	 */
	public Worksheet getWorksheet(){
		return worksheet;
	}
	
	/**
	 * Parses the list from the worksheet
	 */
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
	
	/**
	 * Sorts the information in the worksheet list
	 * WARNING: UNTESTED
	 */
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
	
	/**
	 * Returns the postion of the first unused slot/cell
	 * @return
	 */
	public int findFirstAvaliableSlot(){
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
					if(value.equalsIgnoreCase("")){
						return yCounter+1;
					}
				}
			} catch (Exception ex){
				break;
			}
		}
		return values.size()+2;
	}
	
	/**
	 * Gets the values in the list
	 * @return
	 */
	public List<String> getValues(){
		return values;
	}
	
	/**
	 * Adds a value to the list in the first avaliable slot
	 * @param value
	 */
	public void addValue(String value){
		System.out.println("A"+String.valueOf(findFirstAvaliableSlot()));
		worksheet.setCell("A"+String.valueOf(findFirstAvaliableSlot()), value);
		parse();
	}
	
	/**
	 * Sets the value of a cell/slot
	 * @param id
	 * @param value
	 */
	public void setValue(Integer id, String value){
		worksheet.setCell("A"+String.valueOf(id+1), value);
		parse();
	}
	
	/**
	 * Removes a value from the list
	 * @param value
	 */
	public void removeValue(String value){
		worksheet.setCell("A"+String.valueOf(values.indexOf(value)+2), "");
		parse();
	}

}
