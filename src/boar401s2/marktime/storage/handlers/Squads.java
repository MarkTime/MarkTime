package boar401s2.marktime.storage.handlers;

import java.util.ArrayList;
import java.util.List;

import boar401s2.marktime.MarkTime;
import boar401s2.marktime.exceptions.NonexistantSquadException;
import boar401s2.marktime.interfaces.SynchroniseInterface;
import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.spreadsheet.Spreadsheet;

public class Squads {
	
	GDrive gdrive;
	SynchroniseInterface eventsParent;
	Spreadsheet spreadsheet;
	
	/**
	 * Used as a handler for squads
	 * @param eventsParent Parent that implements "SynchroniseEvents" (Generally Synchronise)
	 * @param gdrive Google Drive object
	 * @param spreadsheet Spreadsheet object
	 */
	public Squads(SynchroniseInterface eventsParent, GDrive gdrive, Spreadsheet spreadsheet){
		this.eventsParent = eventsParent;
		this.gdrive = gdrive;
		this.spreadsheet = spreadsheet;
	}
	
	/**
	 * Checks to see if squad exists locally
	 * @param 	name	The name of the squad to return
	 * @return	boolean	If the squad exists
	 */
	public boolean squadExists(String name){
		return getLocalSquads().contains(name);
	}
	
	/**
	 * Checks to see if the squad exists online
	 * @param 	name	Name of squad to check for
	 * @return	boolean	If the squad exists online
	 */
	public boolean squadExistsOnline(String name){
		return getRemoteSquads().contains(name);
	}
	
	/**
	 * Gets squad
	 * @param name name of squad
	 * @return Squad if exists
	 * @throws NonexistantSquadException
	 */
	public Squad getSquad(String name) throws NonexistantSquadException{
		return new Squad(eventsParent, gdrive, name);
	}
	
	/**
	 * Gets a list of the squads locally
	 * @return A List of the local squads
	 */
	public List<String> getLocalSquads(){
		List<String> localSquads = new ArrayList<String>();
		for(String s: MarkTime.activity.getApplicationContext().getFilesDir().list()){
			if(s.startsWith("Squad-")){
				localSquads.add(s);
			}
		}
		return localSquads;
	}
	
	/**
	 * Gets list of the remote squads
	 * @return A list of the remote squiads
	 */
	public List<String> getRemoteSquads(){
		List<String> remoteSquads = new ArrayList<String>();
		for (String name: spreadsheet.getWorksheetNames()){
			if(name.startsWith("Squad-")){
				remoteSquads.add(name);
			}
		}
		return remoteSquads;
	}

}
