package boar401s2.marktime.handlers;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.parsers.ListParser;
import boar401s2.marktime.util.Position;

public class Squad {
	
	String name;
	Section section;
	
	public Squad(String name, Section section){
		this.name = name;
		this.section = section;
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
	
	public List<Boy> getBoys(){
		Worksheet worksheet = getCompany().getAttendanceSpreadsheet().getWorksheet(getName());
		ListParser listParser = new ListParser(worksheet);
		List<String> boyNames = listParser.parse(new Position(0, 1));
		List<Boy> boys = new ArrayList<Boy>();
		for (String boy: boyNames){
			boys.add(new Boy(boy, this));
		}
		return boys;
	}
	
	//==========[Parent stuff==========//
	
	public Company getCompany(){
		return section.getCompany();
	}
	
	public Section getSection(){
		return section;
	}

}
