package boar401s2.marktime.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import boar401s2.marktime.ui.ListViewEntryTypes;

public class ListTemplateHandler{

	List<String> entries = new ArrayList<String>();
	Activity parent;
	String title = "Template";
	
	public ListTemplateHandler(Activity parent, String title){
		this.parent = parent;
		this.title = title;
	}
	
	public ListTemplateHandler(Activity parent){
		this.parent = parent;
	}
	
	public void addHeader(String header){
		entries.add(header+","+ListViewEntryTypes.HEADER);
	}
	
	public void addItem(String header, String title, Integer type){
		entries.add(header+":"+title+","+String.valueOf(type));
	}
	
	public void addItemWithSubtitle(String header, String title, String subtitle){
		entries.add(header+":"+title+","+String.valueOf(ListViewEntryTypes.BUTTON_WITH_SUB)+","+subtitle);
	}
	
	public void show(int level){
		Intent i = new Intent(parent, ListTemplate.class);
		i.putExtra("entries", entries.toArray(new String[0]));
		i.putExtra("title", title);
		i.putExtra("requestCode", level);
		parent.startActivityForResult(i, level);
	}

}
