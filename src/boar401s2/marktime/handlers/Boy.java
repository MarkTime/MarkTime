package boar401s2.marktime.handlers;

public class Boy {
	
	Squad squad;
	
	public Boy(Squad squad){
		this.squad = squad;
	}

	//==========[Parent stuff==========//
	
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
