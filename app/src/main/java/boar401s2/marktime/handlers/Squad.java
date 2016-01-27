package boar401s2.marktime.handlers;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;

/**
 * A class that acts as a wrapper for the squad as
 * defined in the Attendance file
 * @author student
 *
 */
public class Squad {
	
	String name;
	Section section;
	
	public Squad(String name, Section section){
		this.name = name;
		this.section = section;
	}
	
	/**
	 * @return Name of the squad
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets a list of the boys names
	 * @return List of boys names
	 */
	public List<String> getBoysNames(){
		Cursor c = section.company.database.db.rawQuery("SELECT boyName FROM boys WHERE boySquad=?;", new String[]{name});
		System.out.println(c.getCount());
		c.moveToFirst();
		List<String> names = new ArrayList<String>();
		while(!c.isAfterLast()){
			System.out.println(c.getString(0));
			names.add(c.getString(0));
			c.moveToNext();
		}
		return names;
	}
	
	/**
	 * Gets a boy in the squad
	 * @param name
	 * @return Boy
	 */
	public Boy getBoy(String name){
		return new Boy(name, this);
	}
	
	/**
	 * Gets all the boys in the squad
	 * @return List of boys
	 */
	
	/**
	 * Adds a boy to the squad
	 * @param boyName
	 */
	public void addBoy(String boyName){
		section.company.database.db.execSQL("INSERT INTO boys (boyName, boySquad) VALUES (?, ?)", new String[]{boyName, name});
	}
	
	/**
	 * Removes a boy from the squad
	 * @param boyName
	 */
    public void removeBoy(String boyName){
        section.company.database.db.execSQL("DELETE FROM boys WHERE boyName=?", new String[]{boyName});
    }


	
	//==========[Parent stuff==========//
	
	/**
	 * Gets the parent company
	 * @return Parent company
	 */
	public Company getCompany(){
		return section.getCompany();
	}
	
	/**
	 * Gets the parent section
	 * @return Parent section
	 */
	public Section getSection(){
		return section;
	}

}
