package boar401s2.marktime.handlers;

import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.parsers.TableParser;
import boar401s2.marktime.util.MarkingData;

public class Boy {
	
	String name;
	Squad squad;
	
	public Boy(String name, Squad squad){
		this.name = name;
		this.squad = squad;
	}
	
	public String getName(){
		return name;
	}
	
	public void setNightData(MarkingData data){
		Spreadsheet spreadsheet = getCompany().attendance;
		String worksheetName = squad.getName()+" - "+data.getDate();
		if (!spreadsheet.worksheetExists(worksheetName)){
			spreadsheet.duplicateSheet("Night Template", worksheetName);
		}
		Worksheet worksheet = spreadsheet.getWorksheet(worksheetName);
		TableParser parser = new TableParser(worksheet);
		
		System.out.println(data.attendance);
		parser.setValue(getName(), "Name", getName());
		parser.setValue(getName(), "Hat", String.valueOf(data.hat));
		parser.setValue(getName(), "Tie", String.valueOf(data.tie));
		parser.setValue(getName(), "Havasac", String.valueOf(data.havasac));
		parser.setValue(getName(), "Badges", String.valueOf(data.badges));
		parser.setValue(getName(), "Belt", String.valueOf(data.belt));
		parser.setValue(getName(), "Pants", String.valueOf(data.pants));
		parser.setValue(getName(), "Socks", String.valueOf(data.socks));
		parser.setValue(getName(), "Shoes", String.valueOf(data.shoes));
		parser.setValue(getName(), "Church", String.valueOf(data.church));
		parser.setValue(getName(), "Attendance", String.valueOf(data.attendance));
		
		getCompany().saveAttendance();
	}

	//==========[Parent stuff]==========//
	
	public Company getCompany(){
		return squad.getSection().getCompany();
	}
	
	public Section getSection(){
		return squad.getSection();
	}
	
	public Squad getSquad(){
		return squad;
	}
	
}
