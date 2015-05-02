package boar401s2.marktime;

import java.util.HashMap;

import boar401s2.marktime.constants.ResultIDList;
import boar401s2.marktime.exceptions.UnCaughtException;
import boar401s2.marktime.ui.BoyUI;
import boar401s2.marktime.ui.Navigator;
import boar401s2.marktime.ui.Settings;
import boar401s2.marktime.ui.Synchronise;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
	public static MarkTime app;
	
	public static String selectedSection = "";
	
	public static HashMap<String, Object> globalObjectStorage = new HashMap<String, Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		app = this;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		activity = this;
		settings = getSharedPreferences(PREFERENCES, 0);
		editor = settings.edit();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark_time);
		isNetworkAvailable = isNetworkAvailable();
		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MarkTime.this));
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
		//Boy b = null; //#Induceacrash
		//b.getCompany();
		Intent i = new Intent(this, Navigator.class);
		startActivityForResult(i, 1);
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
	
	public static void print(String string){
		Log.i("General", string);
	}
	
	/**
	 * Called when an opened activity, such as BoyUI has returned.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1){
			if(resultCode == ResultIDList.RESULT_OK){
				Intent i = new Intent(this, BoyUI.class);
				i.putExtra("boy", data.getExtras().getString("boy"));
				startActivityForResult(i, 2);
			} else if(resultCode == ResultIDList.RESULT_NO_RETURN){
			} else {
				//Toast.makeText(this, "Unable to complete this task.", Toast.LENGTH_SHORT).show();
			}
		} else if(requestCode == 2){
			if(resultCode == ResultIDList.RESULT_OK){ //This never actually gets called!
				System.out.println("Marked boy!");
			} else if(resultCode == ResultIDList.RESULT_UP){
				Intent i = new Intent(this, Navigator.class);
				i.putExtra("location", data.getExtras().getString("location"));
				startActivityForResult(i, 1);
			} else {
				System.out.println("Error!");
			}
		}
	}
}