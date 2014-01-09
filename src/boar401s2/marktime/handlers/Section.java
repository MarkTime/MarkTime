package boar401s2.marktime.handlers;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.parsers.ListParser;
import boar401s2.marktime.util.Position;

public class Section {

	String name;
	Company company;
	List<Squad> squads = new ArrayList<Squad>();
	
	public Section(String name, Company company){
		this.company = company;
		this.name = name;
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
		Worksheet worksheet = getCompany().getAttendanceSpreadsheet().getWorksheet(getName());
		ListParser listParser = new ListParser(worksheet);
		List<String> squadNames = listParser.parse(new Position(0, 1));
		List<Squad> squads = new ArrayList<Squad>();
		for (String squadName: squadNames){
			squads.add(new Squad(squadName, this));
		}
		return squads;
	}
	
	//==========[Parent stuff==========//
	
	public Company getCompany(){
		return company;
	}
	
}
