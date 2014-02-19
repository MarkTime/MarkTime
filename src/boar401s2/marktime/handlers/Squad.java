package boar401s2.marktime.handlers;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.parsers.ListParser;

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
	
	public String getName(){
		return name;
	}
	
	public List<String> getBoysNames(){
		List<String> names = new ArrayList<String>();
		for (Boy boy: getBoys()){
			names.add(boy.getName());
		}
		return names;
	}
	
	public Boy getBoy(String name){
		return new Boy(name, this);
	}
	
	public List<Boy> getBoys(){
		List<String> boyNames = listParser.getValues();
		List<Boy> boys = new ArrayList<Boy>();
		for (String boy: boyNames){
			boys.add(new Boy(boy, this));
		}
		return boys;
	}
	
	public void addBoy(String name){
		listParser.addValue(name);
		section.company.saveAttendance();
	}
	
	public void removeBoy(String name){
		listParser.removeValue(name);
	}
	
	//==========[Parent stuff==========//
	
	public Company getCompany(){
		return section.getCompany();
	}
	
	public Section getSection(){
		return section;
	}

}
