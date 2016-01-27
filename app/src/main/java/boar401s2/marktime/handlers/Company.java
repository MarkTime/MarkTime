package boar401s2.marktime.handlers;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.MarkTime;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.database.Database;
import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;

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
	Database database;
	
	public Company(AsyncTaskParent parent){
		this.parent = parent;

		database = new Database(MarkTime.app.getBaseContext());
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
		attendance.save(MarkTime.activity.getFilesDir() + "/" + attendance.getName() + ".db");
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

	public int getSectionID(String name){
		Cursor c = database.db.rawQuery("SELECT * FROM sections WHERE sectionName=?", new String[]{name});
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("_id"));
	}

	public List<String> getSectionNames(){
		Cursor c = database.db.rawQuery("SELECT sectionName FROM sections;", null);
		c.moveToFirst();
		List<String> names = new ArrayList<String>();
		while(!c.isAfterLast()){
			names.add(c.getString(0));
			c.moveToNext();
		}
		return names;
	}
	
	/**
	 * Returns a list of the section names
     * Returns from spreadsheet. Depricated.
	 * @return
	 */
    @Deprecated
	public List<String> getSectionNames_(){
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
    @Deprecated
	public void addSection_(String id){
		String name = id;
		attendance.createWorksheet(name);
		attendance.getWorksheet(name).setSize(1, 6);
		attendance.getWorksheet(name).setCell("A1", "Squads in Section");
		saveAttendance();
	}

	public void addSection(String sectionName){
		database.db.execSQL("INSERT INTO sections (sectionName) VALUES ('" + sectionName + "');");
	}
	
	/**
	 * Deletes a section from the company
	 * @param id
	 */
	@Deprecated
	public void deleteSection_(String id){
		attendance.deleteSheet(id);
		saveAttendance();
	}

	public void deleteSection(String sectionName){
		database.db.execSQL("DELETE FROM sections WHERE sectionName=?", new String[]{sectionName});
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

	public Database getDatabase(){
		return database;
	}
}
