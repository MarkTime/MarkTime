package boar401s2.marktime.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

}
