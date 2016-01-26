package boar401s2.marktime.storage.database;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by John Board on 26/01/2016.
 */
public class DBContract {


    public static String getAttendanceTableName(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
        String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);    //For whatever reason, January starts at 0
        String year = String.format("%02d", cal.get(Calendar.YEAR) - 2000);
        return "attn"+String.valueOf(day)+String.valueOf(month)+String.valueOf(year);
    }

    public static String getCreateAttendanceTableQuery(){
        return "CREATE TABLE IF NOT EXISTS "+getAttendanceTableName()+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "                          boyID         INTEGER," +
                "                          attendance    INTEGER," +
                "                          church        INTEGER," +
                "                          hat           INTEGER," +
                "                          tie           INTEGER," +
                "                          havasac       INTEGER," +
                "                          badges        INTEGER," +
                "                          belt          INTEGER," +
                "                          pants         INTEGER," +
                "                          socks         INTEGER," +
                "                          shoes         INTEGER)";
    }

    public static String getCreateBoyTableQuery(){
        return "CREATE TABLE IF NOT EXISTS boys (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
    }

    public static String getCreateSectionTableQuery(){
        return "CREATE TABLE IF NOT EXISTS sections (_id INTEGER PRIMARY KEY AUTOINCREMENT, sectionName TEXT);";
    }

    public static String getCreateSquadTableQuery(){
        //squadParent is the _id of a section in the sections table
        return "CREATE TABLE IF NOT EXISTS squads (_id INTEGER PRIMARY KEY AUTOINCREMENT, squadName TEXT, squadParent INTEGER);";
    }


}
