package boar401s2.marktime.util;

import java.util.ArrayList;

public class Position{
	public int x = 0;
	public int y = 0;
	public String cell;
	
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Position(String cell){
		this.cell = cell;
	}
	
	public String getString(){
		return String.valueOf(x)+", "+String.valueOf(y);
	}
	
	public String getCell(){
		return cell;
	}
	
	public void convertToCartesian(){
		int x = 0;
		int y = 0;
		ArrayList<Character> xlist = new ArrayList<Character>();
		String ychars = "";
		for (int i = 0; i < cell.length()-1; i++){
		    char c = cell.charAt(i); 
		    if (isChar(c)){
		    	xlist.add(c);
		    	System.out.println(c);
		    } else if (isNumber(c)){
		    	ychars = ychars + c;
		    }
		}
		
		int cnt = 0;
		for (int i=xlist.size()-1; i>-1; i--){
			x = (int) (x + Math.pow(26, cnt)*((int)xlist.get(i)-64));
			cnt += 1;
		}
		this.x = x-1;
		this.y = y;
		this.cell = "";
	}
	
	public void convertToSpreadsheetNotation(){
		int col_num = x;
		int row_num = y;
		row_num++;
		col_num++;
		String col_str = "";
		while(col_num>0){
			int remainder = col_num % 26;
			if (remainder == 0){
				remainder = 26;
			}
			char col_letter = (char) (65+remainder-1);
			col_str = col_letter + col_str;
			col_num = (Integer) (col_num-1)/26;
		}
		this.cell = col_str + String.valueOf(row_num);
		this.x = 0;
		this.y = 0;
	}
	
	public String getPositionNotation(){
		if(this.cell == ""){
			return "CARTESIAN";
		} else {
			return "SPREADSHEET";
		}
	}
	
	private boolean isChar(Character chr){
		if ((int)chr >= 65 && (int)chr <= 90){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isNumber(Character chr){
		if ((int)chr >= 48 && (int)chr <= 57){
			return true;
		} else {
			return false;
		}
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}