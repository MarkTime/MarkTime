package boar401s2.marktime.handlers;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.sax.StartElementListener;
import boar401s2.marktime.MarkTime;
import boar401s2.marktime.ui.ListTemplate;

public class ListTemplateHandler {
	
	MarkTime parent;
	List<String> uncompiledItems = new ArrayList<String>();
	List<String> uncompiledItemSubtitles = new ArrayList<String>();
	List<Integer> headerPositions = new ArrayList<Integer>();
	
	public ListTemplateHandler(MarkTime parent){
		this.parent = parent;
	}
	
	public void addNewItem(String item, String description){
		uncompiledItems.add(item);
		uncompiledItemSubtitles.add(description);
	}
	
	public void addNewHeading(String item){
		uncompiledItems.add(item);
		uncompiledItemSubtitles.add(null);
		headerPositions.add(uncompiledItems.size());
	}
	
	public void show(){
		final String[] LIST = uncompiledItems.toArray(new String[0]);
		final String[] TEXTS = uncompiledItemSubtitles.toArray(new String[0]);
		final Integer[] HDRPOS = headerPositions.toArray(new Integer[0]);
		Intent i = new Intent(parent, ListTemplate.class);
		i.putExtra("LIST", LIST);
		i.putExtra("SUBTEXTS", TEXTS);
		i.putExtra("HDRPOS", HDRPOS);
		parent.startActivity(i);
	}

}
