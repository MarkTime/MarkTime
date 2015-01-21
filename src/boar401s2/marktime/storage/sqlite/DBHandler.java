package boar401s2.marktime.storage.sqlite;

//Created whilst following the tutorial below:
//https://developer.android.com/training/basics/data-storage/databases.html

import boar401s2.marktime.storage.sqlite.queries.DatabaseQueries;
import boar401s2.marktime.storage.sqlite.schema.DatabaseContract;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
	
	public DBHandler(Context context){
		super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
	}
	
	/* Called on database creation */
	public void onCreate(SQLiteDatabase db){
		System.out.println(DatabaseQueries.SQL_CREATE_TABLE_BOYS);
		System.out.println(DatabaseQueries.SQL_CREATE_TABLE_ATTENDANCE);
		System.out.println(DatabaseQueries.SQL_CREATE_TABLE_SQUADS);
		System.out.println(DatabaseQueries.SQL_CREATE_TABLE_SECTIONS);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
	
		onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
		onUpgrade(db, oldVersion, newVersion);
	}
	
}