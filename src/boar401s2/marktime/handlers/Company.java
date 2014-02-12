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
	
	public Section getSection(String name){
		if (getSectionNames().contains(name)){
			return new Section(name, this);
		} else {
			return null;
		}
	}
	
	public void saveAttendance(){
		attendance.save(MarkTime.activity.getFilesDir()+"/"+attendance.getName()+".db");
	}
	
	public void saveRegister(){
		register.save(MarkTime.activity.getFilesDir()+"/"+register.getName()+".db");
	}
	
	/**
	 * Gets a list of the sections
	 */
	public List<Section> getSections(){
		List<Section> sections = new ArrayList<Section>();
		for (Worksheet sheet: attendance.getWorksheets()){
			if(sheet.getName().startsWith("Section-")){
				sections.add(new Section(sheet.getName(), this));
			}
		}
		return sections;
	}
	
	public List<String> getSectionNames(){
		List<String> result = new ArrayList<String>();
		for (Section s: getSections()){
			result.add(s.getName());
		}
		return result;
	}
	
	public void addSection(String id){
		String name = "Section-"+id;
		attendance.createWorksheet(name);
		attendance.getWorksheet(name).setSize(1, 6);
		attendance.getWorksheet(name).setCell("A1", "Squads in Section");
	}
	
	//==========[Spreadsheet Stuff]==========//
	
	public Spreadsheet getAttendanceSpreadsheet(){
		return attendance;
	}
	
	public Spreadsheet getRegisterSpreadsheet(){
		return register;
	}
}
