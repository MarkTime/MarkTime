package boar401s2.marktime.storage.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import boar401s2.marktime.MarkTime;
import boar401s2.marktime.exceptions.NonexistantSquadException;
import boar401s2.marktime.exceptions.SquadNotFetchedException;
import boar401s2.marktime.interfaces.SynchroniseEvents;
import boar401s2.marktime.storage.GDrive;
import boar401s2.marktime.storage.spreadsheet.Spreadsheet;
import boar401s2.marktime.storage.spreadsheet.Worksheet;
import boar401s2.marktime.util.Position;

public class Squad {
	
	List<String> squad = new ArrayList<String>();
	
	GDrive gdrive;
	Spreadsheet spreadsheet;
	Worksheet sheet;
	public String squadName;
	SynchroniseEvents eventsParent;
	
	boolean fetched = false;
	
	public Squad(SynchroniseEvents eventsParent, GDrive gdrive, String squadName) throws NonexistantSquadException{
		this.gdrive = gdrive;
		this.squadName = squadName;
		this.eventsParent = eventsParent;
	}
	
	public void pullSquadFromSpreadsheet(){
		new PullSquadData().execute();
	}
	
	public void pushSquadToSpreadsheet() throws SquadNotFetchedException{
		if (!hasFetchedSquad()){
			throw new SquadNotFetchedException("Haven't fetched squads yet.");
		}
		new PushSquadData().execute();
	}
	
	public boolean pullSquadFromFile(){
		try {
			File f = new File(MarkTime.activity.getApplicationContext().getFilesDir()+"/"+squadName+".sqd");
			if (f.exists()==false){
				return false;
			}
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			while(true){
				line = br.readLine();
				if(line==null){
					break;
				}
				squad.add(line);
			}
			br.close();
			fetched = true;
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void pushSquadToFile() throws SquadNotFetchedException{
		if (!hasFetchedSquad()){
			throw new SquadNotFetchedException("Haven't fetched squads yet.");
		}
		try {
			FileOutputStream fileOut = MarkTime.activity.getApplicationContext().openFileOutput(squadName+".sqd", Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fileOut);
			for (String member: squad){
				osw.write(member+"\n");
			}
			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printSquad(){
		if (hasFetchedSquad())
		for(String s: squad){
			System.out.println(s);
		}
	}
	
	public boolean hasFetchedSquad(){
		return fetched;
	}
	
	public class PullSquadData extends AsyncTask<Void, String, List<String>>{
		
		@Override
		protected List<String> doInBackground(Void... values) {
			publishProgress("Opening spreadsheet...");
			openSpreadsheet();
			publishProgress("Fetching squad data...");
			fetchSquadData();
			return squad;
		}
		
		@Override
		protected void onPostExecute(List<String> squadmembers){
			fetched = true;
			eventsParent.onSquadFetched();
		}
		
		@Override
		protected void onProgressUpdate(String... value){
			eventsParent.onStatusChange(value[0]);
		}
		
	}

	public void openSpreadsheet(){
		spreadsheet = gdrive.getSpreadsheet(MarkTime.settings.getString("spreadsheet", ""));
		List<String> worksheets = spreadsheet.getWorksheetNames();
		if (worksheets.contains(squadName)){
			sheet = spreadsheet.getWorksheet(squadName);
		}
	}

	public void fetchSquadData(){
		for(int i=2; i<sheet.getSheetHeight(); i++){
			String firstName = sheet.getCellValue(new Position(1, i));
			if (!firstName.equalsIgnoreCase("")){
				String lastName = sheet.getCellValue(new Position(2, i));
				squad.add(firstName+" "+lastName);
			} else {
				break;
			}
		}
	}
	
	public class PushSquadData extends AsyncTask<Void, String, List<String>>{

		@Override
		protected List<String> doInBackground(Void... params) {
			return null;
		}
		
	}
}
