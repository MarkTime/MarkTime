package boar401s2.marktime.ui.navigator;

import android.app.Activity;
import boar401s2.marktime.handlers.Squad;
import boar401s2.marktime.ui.ListTemplateHandler;
import boar401s2.marktime.ui.ListViewEntryTypes;

public class BoyNavigator {
	
	Squad squad;
	ListTemplateHandler list;
	Activity parent;

	public BoyNavigator(Activity parent, Squad squad){
		this.parent = parent;
		this.squad = squad;
		list = new ListTemplateHandler(parent, "Boy Navigator");
		list.addHeader("Boys");
		for(String boy: squad.getBoysNames()){
			list.addItem("Boys", boy, ListViewEntryTypes.BUTTON);
		}
		list.addHeader("Settings");
		list.addItem("Settings", "New Boy", ListViewEntryTypes.BUTTON);
		list.addItem("Settings", "Rename Squad", ListViewEntryTypes.BUTTON);
		list.addItem("Settings", "Delete Squad", ListViewEntryTypes.BUTTON);
	}
	
	public void show(){
		list.show(LevelIDList.BOY);
	}

}
