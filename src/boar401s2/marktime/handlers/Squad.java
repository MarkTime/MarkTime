package boar401s2.marktime.handlers;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.parsers.ListParser;

/**
 * A class that acts as a wrapper for the squad as
 * defined in the Attendance file
 * @author student
 *
 */
public class Squad {
	
	String name;
	Section section;
	ListParser listParser;
	Worksheet worksheet;
	
	public Squad(String name, Section section){
		this.name = name;
		this.section = section;
		worksheet = getCompany().getAttendanceSpreadsheet().getWorksheet(getName());
		listParser = new ListParser(worksheet);
		listParser.parse();
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
		List<String> names = new ArrayList<String>();
		for (Boy boy: getBoys()){
			names.add(boy.getName());
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
	public List<Boy> getBoys(){
		List<String> boyNames = listParser.getValues();
		List<Boy> boys = new ArrayList<Boy>();
		for (String boy: boyNames){
			boys.add(new Boy(boy, this));
		}
		return boys;
	}
	
	/**
	 * Adds a boy to the squad
	 * @param name
	 */
	public void addBoy(String name){
		listParser.addValue(name);
		section.company.saveAttendance();
	}
	
	/**
	 * Removes a boy from the squad
	 * @param name
	 */
	public void removeBoy(String name){
		listParser.removeValue(name);
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
