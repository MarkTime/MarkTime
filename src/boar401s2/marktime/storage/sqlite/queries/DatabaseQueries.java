package boar401s2.marktime.storage.sqlite.queries;

import boar401s2.marktime.storage.sqlite.schema.DatabaseContract;

public class DatabaseQueries {
	
	public static final String SQL_CREATE_TABLE_BOYS = 
			"CREATE TABLE " + DatabaseContract.BoysSchema.TABLE_NAME + " (" +
			DatabaseContract.BoysSchema.COLUMN_NAME_BOY_ID + " INTEGER PRIMARY KEY, " +
			DatabaseContract.BoysSchema.COLUMN_NAME_GIVEN_NAMES + " TEXT, " +
			DatabaseContract.BoysSchema.COLUMN_NAME_FAMILY_NAME + " TEXT, " +
			DatabaseContract.BoysSchema.COLUMN_NAME_SECTION + " INTEGER, " +
			DatabaseContract.BoysSchema.COLUMN_NAME_SQUAD + " INTEGER, " +
			DatabaseContract.BoysSchema.FOREIGN_KEY_SECTION + ", " +						//Adds references
			DatabaseContract.BoysSchema.FOREIGN_KEY_SQUAD + 								
			");";
	
	public static final String SQL_CREATE_TABLE_SECTIONS = 
			"CREATE TABLE " + DatabaseContract.SectionsSchema.TABLE_NAME + " (" +
			DatabaseContract.SectionsSchema.COLUMN_NAME_SECTION_ID + " INTEGER PRIMARY KEY, " +
			DatabaseContract.SectionsSchema.COLUMN_NAME_SECTION_NAME + " TEXT" + 
			");";
	
	public static final String SQL_CREATE_TABLE_SQUADS =
			"CREATE TABLE " + DatabaseContract.SquadsSchema.TABLE_NAME + " (" + 
			DatabaseContract.SquadsSchema.COLUMN_NAME_SQUAD_ID + " INTEGER PRIMARY KEY, " +
			DatabaseContract.SquadsSchema.COLUMN_NAME_SQUAD_NAME + " TEXT, " +
			DatabaseContract.SquadsSchema.COLUMN_NAME_SQUAD_LEADER + " INTEGER, " +
			DatabaseContract.SquadsSchema.COLUMN_NAME_SQUAD_2IC + " INTEGER, " +
			DatabaseContract.SquadsSchema.COLUMN_NAME_SECTION + " INTEGER, " +
			DatabaseContract.SquadsSchema.FOREIGN_KEY_SQUAD + ", " + 
			DatabaseContract.SquadsSchema.FOREIGN_KEY_SQUAD_2IC + ", " + 
			DatabaseContract.SquadsSchema.FOREIGN_KEY_SQUAD_LEADER + 
			");";
	
	
	public static final String SQL_CREATE_TABLE_ATTENDANCE =
			"CREATE TABLE " + DatabaseContract.AttendanceSchema.TABLE_NAME + " (" +
			DatabaseContract.AttendanceSchema.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_DATE + " TEXT, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_BOY_ID + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_HAT + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_TIE + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_HAVASACK + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_BADGES + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_BELT + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_PANTS + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_SOCKS + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_SHOES + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_CHURCH + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.COLUMN_NAME_ATTENDANCE + " INTEGER, " + 
			DatabaseContract.AttendanceSchema.FOREIGN_KEY_SQUAD + ", " +
			");";
			

}
