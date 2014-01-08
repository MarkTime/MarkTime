package boar401s2.marktime;
 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/**
 * Main activity for this app. Serves as the main menu
 * for opening other activities, such as settings, and
 * synchronise
 * @author John "boar401s2" Board
 * @date 15/11/2013
 * @copywrite (c) 2013 John Board
 */
public class MarkTime extends Activity {
	
	public static final String PREFERENCES = "Preferences";
	public static boolean isNetworkAvailable = false;
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
		isNetworkAvailable = isNetworkAvailable();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mark_time, menu);
		return true;
	}
	
	/**
	 * Checks to see if the device is connected to a network.
	 * @return
	 */
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	/**
	 * Callback for "Mark the Roll" button. Used to open the roll activity.
	 * @param view
	 */
	public void openMark(View view){
		Intent i = new Intent(this, Roll.class);
		startActivity(i);
	}
	
	/**
	 * Callback for "Synchronise" button. Used to open the synchronise activity.
	 * @param view
	 */
	public void openSynchronise(View view){
		if (isNetworkAvailable()){
			Intent i = new Intent(this, Synchronise.class);
			startActivity(i);
		} else {
			Toast.makeText(this, "Internet connection unavaliable. Unable to synchronise.", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Callback for the "Settings" button. Used to open Settings activity.
	 * @param view
	 */
	public void openSettings(View view){
		Intent i = new Intent(this, Settings.class);
		startActivity(i);
	}
	
	/**
	 * Callback for the "Exit" button. Used to exit the app.
	 * @param view
	 */
	public void onExit(View view){
		finish();
	}
	
	public static void print(String string){
		Log.i("General", string);
	}
}