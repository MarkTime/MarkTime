package boar401s2.marktime.ui;

import java.util.HashMap;

import boar401s2.marktime.R;

public class ListViewEntry {
	
	int typeID;
	int viewID;
	boolean enabled = true;
	boolean isHeader;
	
	HashMap<String, Object> extras = new HashMap<String, Object>();
	
	String title;
	
	public ListViewEntry(int typeID, String title){
		this.typeID = typeID;
		setViewID();
		this.title = title;
		isHeader = false;
	}
	
	public void setExtra(String key, Object value){
		extras.put(key, value);
	}
	
	public Object getExtra(String key){
		return extras.get(key);
	}
	
	public void setViewID(){
		if(typeID == ListViewEntryTypes.BUTTON){
			viewID = R.layout.lv_layout;
		} else if(typeID == ListViewEntryTypes.CHECKBOX){
			viewID = R.layout.lv_layout_checkbox;
		} else if(typeID == ListViewEntryTypes.HEADER){
			viewID = R.layout.lv_header_layout;
		} else if(typeID == ListViewEntryTypes.BUTTON_WITH_SUB){
			viewID = R.layout.lv_layout_sub;
		} else if(typeID == ListViewEntryTypes.SUBMIT){
			viewID = R.layout.lv_submit_layout;
		}
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public void setType(int typeID){
		this.typeID = typeID;
		setViewID();
	}
	
	public int getViewID(){
		return viewID;
	}
	
	public int getTypeID(){
		return typeID;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public boolean isHeader(){
		return isHeader;
	}
	
	public String getTitle(){
		return title;
	}

}
