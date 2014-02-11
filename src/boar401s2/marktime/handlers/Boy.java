package boar401s2.marktime.handlers;

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
	
	public MarkingData getMarkingData(String date){
		return null;
	}
	
	public void setMarkingData(String date){
		
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
