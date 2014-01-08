package boar401s2.marktime.handlers;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.MarkTime;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.OfflineSpreadsheet;

public class Company {
	
	AsyncTaskParent parent;
	Spreadsheet attendance;
	Spreadsheet register;
	
	public Company(AsyncTaskParent parent){
		this.parent = parent;
		
		//Initalize spreadsheets
		attendance = new OfflineSpreadsheet(MarkTime.settings.getString("spreadsheet", ""));
		register = new OfflineSpreadsheet(MarkTime.settings.getString("register", ""));
		
		//Load spreadsheets from file
		attendance.load(MarkTime.activity.getFilesDir()+"/"+attendance.getName()+".db");
		register.load(MarkTime.activity.getFilesDir()+"/"+register.getName()+".db");
	}
	
	/**
	 * Gets a list of the sections
	 */
	public List<String> getSections(){
		List<String> sections = new ArrayList<String>();
		for (Worksheet sheet: attendance.getWorksheets()){
			if(sheet.getName().startsWith("Section-")){
				sections.add(sheet.getName());
			}
		}
		return sections;
	}
	
	//==========[Spreadsheet Stuff]==========//
	
	public Spreadsheet getAttendanceSpreadsheet(){
		return attendance;
	}
	
	public Spreadsheet getRegisterSpreadsheet(){
		return register;
	}
}
