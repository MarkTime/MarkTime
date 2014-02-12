package boar401s2.marktime.ui.navigator;

import android.app.Activity;
import boar401s2.marktime.handlers.Section;
import boar401s2.marktime.ui.ListTemplateHandler;
import boar401s2.marktime.ui.ListViewEntryTypes;

public class SquadNavigator {

	Section section;
	ListTemplateHandler list;
	Activity parent;

	public SquadNavigator(Activity parent, Section section){
		this.parent = parent;
		this.section = section;
		list = new ListTemplateHandler(parent, "Squad Navigator");
		list.addHeader("Squads");
		for(String squad: section.getSquadNames()){
			list.addItem("Squads", squad, ListViewEntryTypes.BUTTON);
		}
		list.addHeader("Settings");
		list.addItem("Settings", "New Squad", ListViewEntryTypes.BUTTON);
		list.addItem("Settings", "Rename Section", ListViewEntryTypes.BUTTON);
		list.addItem("Settings", "Delete Section", ListViewEntryTypes.BUTTON);
	}
	
	public void show(){
		list.show(LevelIDList.SQUAD);
	}
	
}
