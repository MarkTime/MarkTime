package boar401s2.marktime.storage.sqlite.schema;

import android.provider.BaseColumns;

/* This serves as the template schema */
public class DatabaseContract {
	
	/* Increment DATABASE_VERSION whenever the schema changes */
	public static int DATABASE_VERSION = 1;
	
	/* Filename of the database on the device */
	public static String DATABASE_NAME = "MarkTime.db";
	
	/* Schema for tblBoys */
	public static abstract class BoysSchema implements BaseColumns {
		//Essentials
		public static final String TABLE_NAME = "tblBoys";
		
		public static final String COLUMN_NAME_BOY_ID = "BoyID";
		public static final String COLUMN_TYPE_BOY_ID = "INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE";
		public static final String COLUMN_NAME_GIVEN_NAMES = "GivenNames";
		public static final String COLUMN_NAME_FAMILY_NAME = "FamilyName";
		public static final String COLUMN_NAME_SECTION = "Section";
		public static final String FOREIGN_KEY_SECTION = "FOREIGN KEY(" + COLUMN_NAME_SECTION + ") REFERENCES " + SectionsSchema.TABLE_NAME + "(" + SectionsSchema.COLUMN_NAME_SECTION_ID + ")";
		public static final String COLUMN_NAME_SQUAD = "Squad";
		public static final String FOREIGN_KEY_SQUAD = "FOREIGN KEY("+ COLUMN_NAME_SQUAD +") REFERENCES " + SquadsSchema.TABLE_NAME + "(" + SquadsSchema.COLUMN_NAME_SQUAD_ID + ")";
		
		//For later use
		public static final String COLUMN_NAME_RANK = "Rank";
		public static final String COLUMN_NAME_AGE = "Age";
		public static final String COLUMN_NAME_EMAIL = "EmailAddress";
		public static final String COLUMN_NAME_PHONE = "PhoneNumber";
		public static final String COLUMN_NAME_DOB = "DOB"; //Date of Birth
	}
	
	/* Schema for tblAttendance */
	public static abstract class AttendanceSchema implements BaseColumns {
		//Essentials
		public static final String TABLE_NAME = "tblAttendance";
		public static final String COLUMN_NAME_ENTRY_ID = "EntryID";
		public static final String COLUMN_NAME_DATE = "Date";
		public static final String COLUMN_NAME_BOY_ID = "BoyID";
		public static final String COLUMN_NAME_HAT = "Hat";
		public static final String COLUMN_NAME_TIE = "Tie";
		public static final String COLUMN_NAME_HAVASACK = "Havasack";
		public static final String COLUMN_NAME_BADGES = "Badges";
		public static final String COLUMN_NAME_BELT = "Belt";
		public static final String COLUMN_NAME_PANTS = "Pants";
		public static final String COLUMN_NAME_SOCKS = "Socks";
		public static final String COLUMN_NAME_SHOES = "Shoes";
		public static final String COLUMN_NAME_CHURCH = "Church";
		public static final String COLUMN_NAME_ATTENDANCE = "Attendance";
		public static final String FOREIGN_KEY_SQUAD = "FOREIGN KEY("+ COLUMN_NAME_BOY_ID +") REFERENCES " + BoysSchema.TABLE_NAME + "(" + BoysSchema.COLUMN_NAME_BOY_ID + ")";
	}
	
	/* Schema for tblSquads */
	public static abstract class SquadsSchema implements BaseColumns {
		public static final String TABLE_NAME = "tblSquads";
		public static final String COLUMN_NAME_SQUAD_ID = "SquadID";
		public static final String COLUMN_NAME_SQUAD_NAME = "Name";
		public static final String COLUMN_NAME_SQUAD_LEADER = "SquadLeader";
		public static final String COLUMN_NAME_SQUAD_2IC = "Squad2IC";				//Squad second in charge
		public static final String COLUMN_NAME_SECTION = "Section";
		public static final String FOREIGN_KEY_SQUAD_LEADER = "FOREIGN KEY("+ COLUMN_NAME_SQUAD_LEADER +") REFERENCES " + BoysSchema.TABLE_NAME + "(" + BoysSchema.COLUMN_NAME_BOY_ID + ")";
		public static final String FOREIGN_KEY_SQUAD_2IC = "FOREIGN KEY("+ COLUMN_NAME_SQUAD_2IC +") REFERENCES " + BoysSchema.TABLE_NAME + "(" + BoysSchema.COLUMN_NAME_BOY_ID + ")";
		public static final String FOREIGN_KEY_SQUAD = "FOREIGN KEY("+ COLUMN_NAME_SECTION +") REFERENCES " + SectionsSchema.TABLE_NAME + "(" + SectionsSchema.COLUMN_NAME_SECTION_ID + ")";
	}
	
	/* Schema for tblSections */
	public static abstract class SectionsSchema implements BaseColumns {
		public static final String TABLE_NAME = "tblSections";
		public static final String COLUMN_NAME_SECTION_ID = "SectionID";
		public static final String COLUMN_NAME_SECTION_NAME = "Name";
	}

}
