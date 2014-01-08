package boar401s2.marktime.handlers;

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
	
	//==========[Parent stuff==========//
	
	public Company getCompany(){
		return section.getCompany();
	}
	
	public Section getSection(){
		return section;
	}

}
