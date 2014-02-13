package boar401s2.marktime.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MarkingData {
	
	public String date = "01/01";
	public boolean hat = false;
	public boolean tie = false;
	public boolean havasac = false;
	public boolean badges = false;
	public boolean belt = false;
	public boolean pants = false;
	public boolean socks = false;
	public boolean shoes = false;
	public int attendance = 0;
	public boolean church = false;
	
	public String getDate(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
		return sdf.format(date);
	}

}
