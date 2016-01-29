package boar401s2.marktime.handlers;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for accessing the section as defined
 * in the attendance file.
 * @author boar401s2
 */
public class Section {

	String name;
	Company company;
	List<Squad> squads = new ArrayList<Squad>();

	public Section(String name, Company company) {
		this.company = company;
		this.name = name;

	}
	
	/**
	 * Gets the name of the Section
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets a squad that the section contains
	 * @param name
	 * @return Squad
	 */
	public Squad getSquad(String name){
		if(getSquadNames().contains(name)){
			return new Squad(name, this);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets a list of the name of the squads that this
	 * section contains
	 * @return List of squad names
	 */

	public List<String> getSquadNames(){
		Cursor c = company.database.db.rawQuery("SELECT squadName FROM squads WHERE squadSection=?;", new String[]{name});
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
	 * Gets a list of the squads
	 * @return List of the squads
	 */
	
	/**
	 * Adds a squad to the Section
	 * @param squadName
	 */
	public void addSquad(String squadName){
		company.database.db.execSQL("INSERT INTO squads (squadName, squadSection) VALUES (?, ?)", new String[]{squadName, name});
	}

	public int getSquadID(String squadName){
		Cursor c = company.database.db.rawQuery("SELECT * FROM squads WHERE squadName=?", new String[]{squadName});
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("_id"));
	}

	/**
	 * Deletes a squad from the Section
	 * @param squadName
	 */
	public void deleteSquad(String squadName){
		int squadID = getSquadID(squadName);
		company.database.db.execSQL("DELETE FROM squads WHERE squadName=?", new String[]{squadName});
		company.database.db.execSQL("INSERT INTO changelog (tableName, rowID, action) VALUES ('squads',?,'DEL');", new String[]{String.valueOf(squadID)});
	}
	
	//==========[Parent stuff==========//
	
	/**
	 * Gets the parent company
	 * @return parent company
	 */
	public Company getCompany(){
		return company;
	}
	
}
