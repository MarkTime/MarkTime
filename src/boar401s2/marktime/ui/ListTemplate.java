package boar401s2.marktime.ui;

import boar401s2.marktime.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListTemplate extends Activity {

	private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    int requestCode;
    
    String[] entries;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_template);
		ListView lv = (ListView)findViewById(R.id.template_list);
		
		Intent i = getIntent();
		setTitle(i.getExtras().getString("title"));
		entries = i.getExtras().getStringArray("entries");
		requestCode = i.getExtras().getInt("requestCode");
        lv.setAdapter(new ListAdapter(this));
        
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		     public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
		    	 System.out.println("Something was clicked!");
		    	 Intent i = new Intent();
		    	 i.putExtra("id", entries[position]);
		         setResult(requestCode, i);
		         finish();
		     }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_template, menu);
		return true;
	}

	 private class ListAdapter extends BaseAdapter {
        private final Context mContext;
        
        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return entries.length;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	String headerName = null;
        	String entryName = null;
        	String subtitleText = "";
        	if(entries[position].split(",")[0].contains(":")){
        		headerName = entries[position].split(",")[0].split(":")[0];
        		entryName = entries[position].split(",")[0].split(":")[1];
        	} else {
        		headerName = entries[position].split(",")[0];
        	}
        	if(entries[position].split(",").length==3){
        		subtitleText = entries[position].split(",")[2];
        	}
        	Integer type = Integer.parseInt(entries[position].split(",")[1]);
        	
        	
            if(type == ListViewEntryTypes.HEADER) { //Am I a header?
                View item = convertView;
                if(convertView == null || convertView.getTag() == LIST_ITEM) {

                    item = LayoutInflater.from(mContext).inflate(
                            R.layout.lv_header_layout, parent, false);
                    item.setTag(LIST_HEADER);

                }

                TextView headerTextView = (TextView)item.findViewById(R.id.lv_list_hdr);
                headerTextView.setText(headerName);
                return item;
            } else if(type == ListViewEntryTypes.BUTTON){ //Am I a button?
	            View item = convertView;
	            if(convertView == null || convertView.getTag() == LIST_HEADER) {
	                item = LayoutInflater.from(mContext).inflate(
	                        R.layout.lv_layout, parent, false);
	                item.setTag(LIST_ITEM);
	            }
            	
            	TextView header = (TextView)item.findViewById(R.id.lv_item_header);
	            header.setText(entryName);
	            return item;
            } else if(type == ListViewEntryTypes.BUTTON_WITH_SUB){
            	View item = convertView;
	            if(convertView == null || convertView.getTag() == LIST_HEADER) {
	                item = LayoutInflater.from(mContext).inflate(
	                        R.layout.lv_layout, parent, false);
	                item.setTag(LIST_ITEM);
	            }
            	
            	TextView header = (TextView)item.findViewById(R.id.lv_item_header);
	            header.setText(entryName);
	            
	            TextView subtext = (TextView)item.findViewById(R.id.lv_item_subtext);
	            subtext.setText(subtitleText);
	            return item;
            }

            /*
            View divider = item.findViewById(R.id.item_separator);
            if(position == HDR_POS2 -1) {
                divider.setVisibility(View.INVISIBLE);
            }*/
            return null;
        }
    }
}
