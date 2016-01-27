package boar401s2.marktime.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MarkingData {
	
	public int date = 010170;
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
	
	public static String getDate(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
		return sdf.format(date);
	}

	public void printData(){
		System.out.println("Marking Data "+String.valueOf(date));
		System.out.println("    Hat: "+String.valueOf(hat));
		System.out.println("    Tie: "+String.valueOf(tie));
		System.out.println("    Havasac: "+String.valueOf(havasac));
		System.out.println("    Badges: "+String.valueOf(badges));
		System.out.println("    Belt: "+String.valueOf(belt));
		System.out.println("    Pants: "+String.valueOf(pants));
		System.out.println("    Socks: "+String.valueOf(socks));
		System.out.println("    Shoes: "+String.valueOf(shoes));
		System.out.println("    Church: "+String.valueOf(church));
		System.out.println("    Attendance: "+String.valueOf(attendance));
	}

}
