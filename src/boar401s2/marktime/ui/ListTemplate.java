package boar401s2.marktime.ui;

import java.util.Arrays;
import java.util.HashMap;

import boar401s2.marktime.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListTemplate extends Activity {

	private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;
    
   
    public HashMap<String, String> items = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_template);
		ListView lv = (ListView)findViewById(R.id.template_list);
		//Intent i = getIntent();
        lv.setAdapter(new MyListAdapter(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_template, menu);
		return true;
	}

	 private class MyListAdapter extends BaseAdapter {
	        public MyListAdapter(Context context) {
	            mContext = context;
	        }

	        @Override
	        public int getCount() {
	            return LIST.length;
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

	            String headerText = getHeader(position);
	            if(headerText != null) {

	                View item = convertView;
	                if(convertView == null || convertView.getTag() == LIST_ITEM) {

	                    item = LayoutInflater.from(mContext).inflate(
	                            R.layout.lv_header_layout, parent, false);
	                    item.setTag(LIST_HEADER);

	                }

	                TextView headerTextView = (TextView)item.findViewById(R.id.lv_list_hdr);
	                headerTextView.setText(headerText);
	                return item;
	            }

	            View item = convertView;
	            if(convertView == null || convertView.getTag() == LIST_HEADER) {
	                item = LayoutInflater.from(mContext).inflate(
	                        R.layout.lv_layout, parent, false);
	                item.setTag(LIST_ITEM);
	            }

	            TextView header = (TextView)item.findViewById(R.id.lv_item_header);
	            header.setText(LIST[position % LIST.length]);

	            TextView subtext = (TextView)item.findViewById(R.id.lv_item_subtext);
	            subtext.setText(SUBTEXTS[position % SUBTEXTS.length]);

	            //Set last divider in a sublist invisible
	            View divider = item.findViewById(R.id.item_separator);
	            if(position == HDR_POS2 -1) {
	                divider.setVisibility(View.INVISIBLE);
	            }


	            return item;
	        }

	        private String getHeader(int position) {

	        	System.out.println(position);
	        	System.out.println(Arrays.asList(HDR_POS).contains(position));
	        	System.out.println(contains(HDR_POS, position));
	        	if(contains(HDR_POS, position)){
	            //if(position == HDR_POS1  || position == HDR_POS2) {
	                return LIST[position];
	            }

	            return null;
	        }

	        private final Context mContext;
	    }

	 public boolean contains(final int[] array, final Integer key) {
		 return Arrays.asList(array).contains(key);
	 }
}
