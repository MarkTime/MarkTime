package boar401s2.marktime.handlers;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.parsers.ListParser;

/**
 * Class for accessing the section as defined
 * in the attendance file.
 * @author boar401s2
 */
public class Section {

	String name;
	Company company;
	List<Squad> squads = new ArrayList<Squad>();
	ListParser listParser;
	Worksheet worksheet;

	public Section(String name, Company company) {
		this.company = company;
		this.name = name;
		worksheet = getCompany().getAttendanceSpreadsheet().getWorksheet(getName());
		listParser = new ListParser(worksheet);
		listParser.parse();

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
		List<String> squadNames = new ArrayList<String>();
		for (Squad squad: getSquads()){
			squadNames.add(squad.getName());
		}
		return squadNames;
	}

	public List<String> getSquadNames_(){
		Cursor c = company.database.db.rawQuery("SELECT squadName FROM squads where squadParent=?;", new String[]{name});
		c.moveToFirst();
		List<String> names = new ArrayList<String>();
		while(!c.isAfterLast()){
			names.add(c.getString(0));
			c.moveToNext();
		}
		return names;
	}
	
	/**
	 * Gets a list of the squads
	 * @return List of the squads
	 */
	public List<Squad> getSquads(){
		List<String> squadNames = listParser.getValues();
		List<Squad> squads = new ArrayList<Squad>();
		for (String squadName: squadNames){
			squads.add(new Squad(squadName, this));
		}
		return squads;
	}
	
	/**
	 * Adds a squad to the Section
	 * @param name
	 */
	public void addSquad(String name){
		listParser.addValue("Squad-"+name);
		worksheet.getParent().createWorksheet("Squad-"+name);
		worksheet.getParent().getWorksheet("Squad-"+name).setSize(1, 21);
		worksheet.getParent().getWorksheet("Squad-"+name).setCell("A1", "Name");
		company.saveAttendance();
	}

	public void addSquad_(String squadName){
		company.database.db.execSQL("INSERT INTO squads (squadName, squadParent) VALUES (?, ?)", new String[]{squadName, name});
	}
	
	/**
	 * Deletes a squad from the Section
	 * @param name
	 */
	public void deleteSquad(String name){
		listParser.removeValue(name);
		worksheet.getParent().deleteSheet(name);
		company.saveAttendance();
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
