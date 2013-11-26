package boar401s2.marktime;
 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

//Man Hours:
//25 Hours 22nd

//TODO:
//Remove dependancies on plaintext auth stuff, have it work off the stored preferences
//Push to git after that
//
//TODO:
//Fix authentication crashing when incorrect password is entered.

public class MarkTime extends Activity {
	
	public static final String PREFERENCES = "Preferences";
	public static SharedPreferences settings;
	public static SharedPreferences.Editor editor;
	public static Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		activity = this;
		settings = getSharedPreferences(PREFERENCES, 0);
		editor = settings.edit();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark_time);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mark_time, menu);
		return true;
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public void openMark(View view){
		Intent i = new Intent(this, Roll.class);
		startActivity(i);
	}
	
	public void openSynchronise(View view){
		if (isNetworkAvailable()){
			Intent i = new Intent(this, Synchronise.class);
			startActivity(i);
		} else {
			Toast.makeText(this, "Internet connection unavaliable. Unable to synchronise.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void openSettings(View view){
		Intent i = new Intent(this, Settings.class);
		startActivity(i);
	}
	
	public void onExit(View view){
		finish();
	}
}