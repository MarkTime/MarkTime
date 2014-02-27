package boar401s2.marktime.handlers;

import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.parsers.TableParser;
import boar401s2.marktime.util.MarkingData;


/**
 * This class is the handler for a Boy. It opens up the appropriate files
 * to get the data about the boy, and then wraps it up in a nice wrapper.
 * @author boar401s2
 */
public class Boy {
	
	String name;
	Squad squad;
	
	public Boy(String name, Squad squad){
		this.name = name;
		this.squad = squad;
	}
	
	/**
	 * @return The boy's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Saves MarkingData to the appropriate attendance file.
	 * Use this method to set the attendance data for this boy.
	 */
	public void setNightData(MarkingData data){
		Spreadsheet spreadsheet = getCompany().attendance;
		String worksheetName = squad.getName()+" - "+MarkingData.getDate();
		if (!spreadsheet.worksheetExists(worksheetName)){
			spreadsheet.duplicateSheet("Night Template", worksheetName);
		}
		Worksheet worksheet = spreadsheet.getWorksheet(worksheetName);
		TableParser parser = new TableParser(worksheet);
		
		parser.setRow(getName(), squad.getBoysNames().indexOf(getName())+1);
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
	
	public MarkingData getNightData(String date){
		Spreadsheet spreadsheet = getCompany().getAttendanceSpreadsheet();
		String worksheetName = squad.getName()+" - "+MarkingData.getDate();
		MarkingData data = new MarkingData();
		if(spreadsheet.worksheetExists(worksheetName)){
			Worksheet worksheet = spreadsheet.getWorksheet(worksheetName);
			TableParser parser = new TableParser(worksheet);
			if(parser.hasRow(getName())){
				System.out.println("Compiling NightData");
				data.hat = Boolean.valueOf(parser.getValue(getName(), "Hat"));
				data.tie = Boolean.valueOf(parser.getValue(getName(), "Tie"));
				data.havasac = Boolean.valueOf(parser.getValue(getName(), "Havasac"));
				data.badges = Boolean.valueOf(parser.getValue(getName(), "Badges"));
				data.belt = Boolean.valueOf(parser.getValue(getName(), "Belt"));
				data.pants = Boolean.valueOf(parser.getValue(getName(), "Pants"));
				data.socks = Boolean.valueOf(parser.getValue(getName(), "Socks"));
				data.shoes = Boolean.valueOf(parser.getValue(getName(), "Shoes"));
				data.church = Boolean.valueOf(parser.getValue(getName(), "Church"));
				data.attendance = Integer.parseInt(parser.getValue(getName(), "Attendance"));
				return data;
			}
		}
		return null;
	}

	//==========[Parent stuff]==========//
	
	/**
	 * @return The boy's company
	 */
	public Company getCompany(){
		return squad.getSection().getCompany();
	}
	
	/**
	 * @return The boy's section
	 */
	public Section getSection(){
		return squad.getSection();
	}
	
	
	/**
	 * @return The boy's squad
	 */
	public Squad getSquad(){
		return squad;
	}
	
}
