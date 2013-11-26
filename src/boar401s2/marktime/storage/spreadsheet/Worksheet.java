package boar401s2.marktime.storage.spreadsheet;

import java.io.IOException;
import java.net.URL;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class Worksheet {
	
	WorksheetEntry worksheet;
	CellFeed feed;
	URL cellFeedURL;
	Spreadsheet parent;
	
	Worksheet(WorksheetEntry worksheet, Spreadsheet parent){
		this.worksheet = worksheet;
		this.parent = parent;
		cellFeedURL = worksheet.getCellFeedUrl();
		try {
			feed = parent.parent.spreadsheetService.getFeed(cellFeedURL, CellFeed.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public boolean cellHasInformation(Position pos){
		return getCellValue(pos).length()>0;
	}
	
	public String getCellValue(Position pos){
		for(CellEntry entry: feed.getEntries()){
			if (entry.getTitle().getPlainText().equalsIgnoreCase(toSpreadsheetCoords(pos))){
				return entry.getCell().getValue();
			}
		}
		return "";
	}
	
	public void setCellValue(Position pos, String value){
		try {
			CellEntry entry = new CellEntry(pos.x, pos.y, value);
			feed.insert(entry);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printCells(){
		setCellValue(new Position(1, 1), "Hello World!");
	}
	
	//==========[Misc Functions]==========//
	
	public Position toCartesian(String spreadsheetCoords){//Untested for "BA1" Should be good up to 676 columns...
		char[] coords = spreadsheetCoords.toCharArray();
		int x = 0;
		int y = 0;
		int index = 0;
		for (Character j: coords){
			if ((int)j>64 && (int)j<91){ //If it's a letter
				x = x + (index*(26*getIntFromChar(String.valueOf(j))) + getIntFromChar(String.valueOf(j)))+1;
			} 
			if((int)j>48 && (int)j<58){ //If it's a number
				y = Integer.valueOf(spreadsheetCoords.substring(index));
			}
			index++;
		}
		return new Position(x, y);
	}
	
	public String toSpreadsheetCoords(Position pos){
		String a = "";
		if (pos.y > 26){
			a = getCharFromInt(pos.y/26);
		}
		a = a + getCharFromInt(pos.y-((pos.y/26)*26));
		String b = String.valueOf(pos.x);
		return a+b;
	}
	
	private int getIntFromChar(String s){
		int ascii = (int) s.charAt(0);
		int start = 64;
		if ((ascii >= 97) && (ascii <= 122)){
			start = 96;
		}
		return ascii-start;
	}
	
	private String getCharFromInt(Integer i){
		return Character.toString(Character.toChars(i+64)[0]);
	}
}

class Position{
	int x = 0;
	int y = 0;
	Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public String getString(){
		return String.valueOf(x)+", "+String.valueOf(y);
	}
}