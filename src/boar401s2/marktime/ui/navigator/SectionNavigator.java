package boar401s2.marktime.ui.navigator;

import android.app.Activity;
import boar401s2.marktime.handlers.Company;
import boar401s2.marktime.ui.ListTemplateHandler;
import boar401s2.marktime.ui.ListViewEntryTypes;

public class SectionNavigator {
	
	Company company;
	ListTemplateHandler list;
	Activity parent;

	public SectionNavigator(Activity parent, Company company){
		this.parent = parent;
		this.company = company;
		list = new ListTemplateHandler(parent, "Section Navigator");
		list.addHeader("Sections");
		for(String section: company.getSectionNames()){
			list.addItem("Sections", section, ListViewEntryTypes.BUTTON);
		}
		list.addHeader("Settings");
		list.addItem("Settings", "Add new section...", ListViewEntryTypes.BUTTON);
	}
	
	public void show(){
		list.show(LevelIDList.SECTION);
	}

}
