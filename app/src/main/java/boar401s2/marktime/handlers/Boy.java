package boar401s2.marktime.handlers;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.storage.database.DBContract;
import boar401s2.marktime.util.MarkingData;


/**
 * This class is the handler for a Boy. It opens up the appropriate files
 * to get the data about the boy, and then wraps it up in a nice wrapper.
 * @author boar401s2
 */
public class Boy {
	
	String name;
	Squad squad;
	
	public Boy(String name, Squad squad){
		this.name = name;
		this.squad = squad;
	}
	
	/**
	 * @return The boy's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Saves MarkingData to the appropriate attendance file.
	 * Use this method to set the attendance data for this boy.
	 */

    public int getID(){
        Cursor c = getCompany().database.db.rawQuery("SELECT * FROM boys WHERE boyName=?", new String[]{name});
        c.moveToFirst();
        return c.getInt(c.getColumnIndex("_id"));
    }

    public void setNightData(MarkingData data){
        String datestamp = DBContract.getDatestamp();
        if(nightDataExists(datestamp)){
            getCompany().database.db.execSQL("DELETE FROM attendance WHERE boyID=? AND datestamp=?", new String[]{String.valueOf(getID()), datestamp});
        }
        String sqlQuery = "INSERT INTO attendance (boyID, attendance, church, hat, tie, havasac, badges, belt, pants, socks, shoes, datestamp) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        getCompany().database.db.execSQL(sqlQuery, new String[]{
                String.valueOf(getID()),
                String.valueOf(data.attendance),
                String.valueOf(data.church),
                String.valueOf(data.hat),
                String.valueOf(data.tie),
                String.valueOf(data.havasac),
                String.valueOf(data.badges),
                String.valueOf(data.belt),
                String.valueOf(data.pants),
                String.valueOf(data.socks),
                String.valueOf(data.shoes),
                datestamp});
    }


    public boolean nightDataExists(String datestamp){
        Cursor c = getCompany().database.db.rawQuery("SELECT * FROM attendance WHERE datestamp=? AND boyID=?;", new String[]{datestamp, String.valueOf(getID())});
        return c.getCount()>0;
    }

    public MarkingData getNightData(String datestamp){
        Cursor c = getCompany().database.db.rawQuery("SELECT * FROM attendance WHERE datestamp=? AND boyID=?;", new String[]{datestamp, String.valueOf(getID())});
        MarkingData data = new MarkingData();
        c.moveToFirst();
        if(c.getCount() == 1){
            data.attendance = c.getInt(1);
            data.church = c.getString(2).equalsIgnoreCase("true");
            data.hat = c.getString(3).equalsIgnoreCase("true");
            data.tie = c.getString(4).equalsIgnoreCase("true");
            data.havasac = c.getString(5).equalsIgnoreCase("true");
            data.badges = c.getString(6).equalsIgnoreCase("true");
            data.belt = c.getString(7).equalsIgnoreCase("true");
            data.pants = c.getString(8).equalsIgnoreCase("true");
            data.socks = c.getString(9).equalsIgnoreCase("true");
            data.shoes = c.getString(10).equalsIgnoreCase("true");
            data.date = c.getInt(11);
            data.printData();
            return data;
        } else {
            System.out.println("Multiple entries - this code should never be called.");
        }
        return null;
    }

	//==========[Parent stuff]==========//
	
	/**
	 * @return The boy's company
	 */
	public Company getCompany(){
		return squad.getSection().getCompany();
	}
	
	/**
	 * @return The boy's section
	 */
	public Section getSection(){
		return squad.getSection();
	}
	
	
	/**
	 * @return The boy's squad
	 */
	public Squad getSquad(){
		return squad;
	}
	
}
