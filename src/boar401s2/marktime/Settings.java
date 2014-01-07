package boar401s2.marktime;

import java.util.Arrays;

import com.google.android.gms.common.AccountPicker;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.DriveScopes;

import android.os.Bundle;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
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
	
	GoogleAccountCredential credential;
	
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
	
	/**
	 * Used to fill in the text boxes with the saved settings
	 */
	public void fillTextBoxes(){
		EditText spreadsheet = (EditText) findViewById(R.id.settings_spreadsheet_name);
		EditText template = (EditText) findViewById(R.id.settings_spreadsheet_template);
		EditText account = (EditText) findViewById(R.id.settings_account);
		EditText company = (EditText) findViewById(R.id.settings_company);
		spreadsheet.setText(settings.getString("spreadsheet", ""));
		template.setText(settings.getString("template", ""));
		account.setText(settings.getString("account", ""));
		company.setText(settings.getString("company", ""));
		
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
	
	/**
	 * Callback for the button "Submit". Writes data to parameters.
	 * @param view
	 */
	public void onSubmit(View view){
		EditText spreadsheet = (EditText) findViewById(R.id.settings_spreadsheet_name);
		EditText template = (EditText) findViewById(R.id.settings_spreadsheet_template);
		EditText account = (EditText) findViewById(R.id.settings_account);
		EditText company = (EditText) findViewById(R.id.settings_company);
		editor.putString("spreadsheet", spreadsheet.getText().toString());
		editor.putString("template", template.getText().toString());
		editor.putString("account", account.getText().toString());
		editor.putString("company", company.getText().toString());
		editor.apply();
		finish();
	}
	
	/**
	 * Callback for the textbox "Account". This opens 
	 * an account selector dialog.
	 * @param view
	 */
	public void onAccountEditorClicked(View view){
		credential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(DriveScopes.DRIVE_FILE));
		Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
		         false, null, null, null, null);
		 startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if(requestCode==1 && resultCode==RESULT_OK){
			String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			EditText txt = (EditText) findViewById(R.id.settings_account);
			txt.setText(accountName);
		}
	}
}
