package boar401s2.marktime.ui;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import boar401s2.marktime.MarkTime;
import boar401s2.marktime.R;
import boar401s2.marktime.storage.database.Database;

public class Synchronise extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronise2);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onSyncLocalClick(View view){
        Database.downloadFile("http://marktime.johnrobboard.com/MarkTimeMaster.db");
        File from = new File("/data/data/boar401s2.marktime/databases/MarkTimeMaster.db");
        File to = new File("/data/data/boar401s2.marktime/databases/MarkTime.db");
        from.renameTo(to);
        Toast.makeText(MarkTime.activity.getApplicationContext(), "Syncing local.", Toast.LENGTH_SHORT).show();
        finishActivity(0);
    }

    public void onSyncRemoteClick(View view){
        Database.uploadFile("/data/data/boar401s2.marktime/databases/MarkTime.db");
        Toast.makeText(MarkTime.activity.getApplicationContext(), "Syncing remote.", Toast.LENGTH_SHORT).show();
        finishActivity(0);
    }

    public void onSyncClick(View view){

    }

}
