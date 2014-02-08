package boar401s2.marktime;

import boar401s2.marktime.storage.tasks.ResultIDList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MarkBoy extends Activity {

	String name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark_boy);
		setupActionBar();
		name = getIntent().getExtras().getString("name");
		TextView nameBox = (TextView) findViewById(R.id.boy_name);
		nameBox.setText(name);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.boy, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public int getAttendanceRating(){
		EditText t = (EditText) findViewById(R.id.boy_attendance);
		return Integer.parseInt(t.getText().toString());
		
	}
	
	public void onSubmitClick(View view){
		CheckBox church = (CheckBox) findViewById(R.id.boy_church);
		CheckBox hat = (CheckBox) findViewById(R.id.boy_hat);
		CheckBox tie = (CheckBox) findViewById(R.id.boy_tie);
		CheckBox havasac = (CheckBox) findViewById(R.id.boy_havasac);
		CheckBox badges = (CheckBox) findViewById(R.id.boy_badges);
		CheckBox belt = (CheckBox) findViewById(R.id.boy_belt);
		CheckBox pants = (CheckBox) findViewById(R.id.boy_pants);
		CheckBox socks = (CheckBox) findViewById(R.id.boy_socks);
		CheckBox shoes = (CheckBox) findViewById(R.id.boy_shoes);
		Intent resultIntent = new Intent();
		resultIntent.putExtra("name", name);
		
		resultIntent.putExtra("church", String.valueOf(church.isChecked()));
		resultIntent.putExtra("hat", String.valueOf(hat.isChecked()));
		resultIntent.putExtra("tie", String.valueOf(tie.isChecked()));
		resultIntent.putExtra("havasac", String.valueOf(havasac.isChecked()));
		resultIntent.putExtra("badges", String.valueOf(badges.isChecked()));
		resultIntent.putExtra("belt", String.valueOf(belt.isChecked()));
		resultIntent.putExtra("pants", String.valueOf(pants.isChecked()));
		resultIntent.putExtra("socks", String.valueOf(socks.isChecked()));
		resultIntent.putExtra("shoes", String.valueOf(shoes.isChecked()));
		resultIntent.putExtra("attendance", getAttendanceRating());
		System.out.println(getAttendanceRating());
		setResult(ResultIDList.RESULT_OK, resultIntent);
		finish();
	}

}
