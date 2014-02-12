package boar401s2.marktime.handlers;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.parsers.ListParser;

public class Section {

	String name;
	Company company;
	List<Squad> squads = new ArrayList<Squad>();
	ListParser listParser;
	Worksheet worksheet;
	
	public Section(String name, Company company){
		this.company = company;
		this.name = name;
		worksheet = getCompany().getAttendanceSpreadsheet().getWorksheet(getName());
		listParser = new ListParser(worksheet);
		listParser.parse();
	}
	
	public String getName(){
		return name;
	}
	
	public Squad getSquad(String name){
		if(getSquadNames().contains(name)){
			return new Squad(name, this);
		} else {
			return null;
		}
	}
	
	public List<String> getSquadNames(){
		List<String> squadNames = new ArrayList<String>();
		for (Squad squad: getSquads()){
			squadNames.add(squad.getName());
		}
		return squadNames;
		
	}
	
	public List<Squad> getSquads(){
		List<String> squadNames = listParser.getValues();
		List<Squad> squads = new ArrayList<Squad>();
		for (String squadName: squadNames){
			squads.add(new Squad(squadName, this));
		}
		return squads;
	}
	
	public void addSquad(String name){
		System.out.println("'Squad-"+name+"'");
		listParser.addValue("Squad-"+name);
		worksheet.getParent().createWorksheet("Squad-"+name);
		worksheet.getParent().getWorksheet("Squad-"+name).setSize(1, 21);
		worksheet.getParent().getWorksheet("Squad-"+name).setCell("A1", "Name");
		company.saveAttendance();
	}
	
	//==========[Parent stuff==========//
	
	public Company getCompany(){
		return company;
	}
	
}
