package boar401s2.marktime;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class Settings extends Activity {
	
	MarkTime parent;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	
	public Settings(){
		settings = MarkTime.settings;
		editor = MarkTime.editor;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setupActionBar();
		fillTextBoxes();
	}
	
	public void fillTextBoxes(){
		EditText spreadsheet = (EditText) findViewById(R.id.settings_spreadsheet_name);
		EditText template = (EditText) findViewById(R.id.settings_spreadsheet_template);
		EditText username = (EditText) findViewById(R.id.settings_username);
		EditText password = (EditText) findViewById(R.id.settings_password);
		spreadsheet.setText(settings.getString("spreadsheet", ""));
		template.setText(settings.getString("template", ""));
		username.setText(settings.getString("username", ""));
		password.setText(settings.getString("password", ""));
	}
	
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onSubmit(View view){
		EditText spreadsheet = (EditText) findViewById(R.id.settings_spreadsheet_name);
		EditText template = (EditText) findViewById(R.id.settings_spreadsheet_template);
		EditText username = (EditText) findViewById(R.id.settings_username);
		EditText password = (EditText) findViewById(R.id.settings_password);
		editor.putString("spreadsheet", spreadsheet.getText().toString());
		editor.putString("template", template.getText().toString());
		editor.putString("username", username.getText().toString());
		editor.putString("password", password.getText().toString());
		editor.apply();
		finish();
	}
	
	/*
	 * 	private void getAccountNames() {
		Intent intent = AccountPicker.newChooseAccountIntent(null, null,
				new String[] { "com.google" }, false, null, null, null, null);
		parentActivity.startActivityForResult(intent, ACCOUNT_PICKER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACCOUNT_PICKER && resultCode == RESULT_OK) {
			String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			txtAccount = accountName;
		} else if (requestCode == USER_RECOVERABLE_AUTH && resultCode == RESULT_OK) {
			new GetAuthToken(parentActivity, txtAccount).execute();
		} else if (requestCode == USER_RECOVERABLE_AUTH && resultCode == RESULT_CANCELED) {
			Toast.makeText(parentActivity, "User rejected authorization.",
					Toast.LENGTH_SHORT).show();
		}
	}*/

}
