package boar401s2.marktime.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

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
        //System.out.println("Output: " + String.valueOf(uploadFile("/data/data/boar401s2.marktime/databases/MarkTime.db")));

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



    public static int uploadFile(String sourceFileUri) {

        int serverResponseCode = 0;
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        try {

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL("http://johnrobboard.com/MarkTime/upload.php");

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename='"
                            + fileName + "'" + lineEnd);

                    dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
            System.out.println("UPLOAD: "+serverResponseMessage);

            if(serverResponseCode == 200){
                System.out.println("Finished.");
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponseCode;
    } // End else block

    public static void downloadFile(String uri){
        int count = 0;
        try {
            URL url = new URL(uri);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int fileLength = conexion.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream("/data/data/boar401s2.marktime/databases/MarkTime.db");

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                System.out.println("" + (int) ((total * 100) / fileLength));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
