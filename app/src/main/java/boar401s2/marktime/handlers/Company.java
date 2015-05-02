package boar401s2.marktime.handlers;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.MarkTime;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.OfflineSpreadsheet;

/**
 * This class is a wrapper for accessing the
 * company as defined in the Attendance file
 * @author boar401s2
 *
 */
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
	 * @param name
	 * @return Returns a section which this company contains
	 */
	public Section getSection(String name){
		if (getSectionNames().contains(name)){
			return new Section(name, this);
		} else {
			return null;
		}
	}
	
	/**
	 * Saves the Attendance file
	 */
	public void saveAttendance(){
		attendance.save(MarkTime.activity.getFilesDir()+"/"+attendance.getName()+".db");
	}
	
	/**
	 * Saves the Register file
	 */
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
	
	/**
	 * Returns a list of the section names
	 * @return
	 */
	public List<String> getSectionNames(){
		List<String> result = new ArrayList<String>();
		for (Section s: getSections()){
			result.add(s.getName());
		}
		return result;
	}
	
	/**
	 * Adds a section to the company
	 * @param id
	 */
	public void addSection(String id){
		String name = "Section-"+id;
		attendance.createWorksheet(name);
		attendance.getWorksheet(name).setSize(1, 6);
		attendance.getWorksheet(name).setCell("A1", "Squads in Section");
		saveAttendance();
	}
	
	/**
	 * Deletes a section from the company
	 * @param id
	 */
	public void deleteSection(String id){
		attendance.deleteSheet(id);
		saveAttendance();
	}
	
	//==========[Spreadsheet Stuff]==========//
	
	/**
	 * @return Attendance Spreadsheet
	 */
	public Spreadsheet getAttendanceSpreadsheet(){
		return attendance;
	}
	
	/**
	 * @return Register spreadsheet
	 */
	public Spreadsheet getRegisterSpreadsheet(){
		return register;
	}
}
