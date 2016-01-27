package boar401s2.marktime.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by John Board on 26/01/2016.
 */
public class Database extends SQLiteOpenHelper{

    public static String DATABASE_NAME = "MarkTime.db";
    public static int DATABASE_VERSION = 1;
    public SQLiteDatabase db;

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
        try {
            upload(new URL("http://johnrobboard.com/MarkTime/upload.php"), new File("/data/data/boar401s2.marktime/databases/MarkTime.db"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Tables created in onOpen (CREATE IF NOT EXISTS...)
        System.out.println("On create called.");
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        db.execSQL(DBContract.getCreateAttendanceTableQuery());
        db.execSQL(DBContract.getCreateBoyTableQuery());
        db.execSQL(DBContract.getCreateSectionTableQuery());
        db.execSQL(DBContract.getCreateSquadTableQuery());
        System.out.println("Created tables.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        System.out.println("On upgrade called");
        db.execSQL("DROP TABLE boys;");
        db.execSQL("DROP TABLE " + DBContract.getAttendanceTableName() + ";");
    }

    public static void upload(URL url, File file) throws IOException, URISyntaxException {
        HttpClient client = new DefaultHttpClient(); //The client object which will do the upload
        HttpPost httpPost = new HttpPost(url.toURI()); //The POST request to send

        FileBody fileB = new FileBody(file);

        MultipartEntity request = new MultipartEntity(); //The HTTP entity which will holds the different body parts, here the file
        request.addPart("file", fileB);

        httpPost.setEntity(request);
        HttpResponse response = client.execute(httpPost); //Once the upload is complete (successful or not), the client will return a response given by the server

        if(response.getStatusLine().getStatusCode()==200) { //If the code contained in this response equals 200, then the upload is successful (and ready to be processed by the php code)
            System.out.println("Upload successful !");
        }
    }

}
