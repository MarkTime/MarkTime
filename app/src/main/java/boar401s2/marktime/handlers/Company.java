package boar401s2.marktime.handlers;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.MarkTime;
import boar401s2.marktime.events.AsyncTaskParent;
import boar401s2.marktime.storage.database.Database;

/**
 * This class is a wrapper for accessing the
 * company as defined in the Attendance file
 * @author boar401s2
 *
 */
public class Company {
	
	AsyncTaskParent parent;
	Database database;
	
	public Company(AsyncTaskParent parent){
		this.parent = parent;

		database = new Database(MarkTime.app.getBaseContext());
	}
	
	/**
	 * @param name
	 * @return Returns a section which this company contains
	 */
	public Section getSection(String name) {
		if (getSectionNames().contains(name)) {
			return new Section(name, this);
		} else {
			return null;
		}
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
	
	/**
	 * Adds a section to the company
	 * @param id
	 */

	public void addSection(String sectionName){
		database.db.execSQL("INSERT INTO sections (sectionName) VALUES ('" + sectionName + "');");
	}
	
	/**
	 * Deletes a section from the company
	 * @param id
	 */

	public void deleteSection(String sectionName){
		Cursor c = database.db.rawQuery("SELECT _id FROM sections WHERE sectionName=?", new String[]{sectionName});
		c.moveToFirst();
		int rowID = c.getInt(0);
		database.db.execSQL("DELETE FROM sections WHERE sectionName=?;", new String[]{sectionName});
		database.db.execSQL("INSERT INTO changelog (tableName, rowID, action) VALUES ('sections',?,'DEL');", new String[]{String.valueOf(rowID)});
	}
	
	//==========[Spreadsheet Stuff]==========//

	public Database getDatabase(){
		return database;
	}
}
