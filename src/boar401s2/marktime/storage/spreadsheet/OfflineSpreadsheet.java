package boar401s2.marktime.storage.spreadsheet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import boar401s2.marktime.storage.interfaces.Spreadsheet;
import boar401s2.marktime.storage.interfaces.Worksheet;
import boar401s2.marktime.storage.spreadsheet.worksheet.OfflineWorksheet;
import boar401s2.marktime.util.HashMapSerializer;

public class OfflineSpreadsheet implements Spreadsheet{
	
	private String name;
	String path;
	HashMap<String, OfflineWorksheet> worksheets = new HashMap<String, OfflineWorksheet>();
	
	public OfflineSpreadsheet(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Worksheet getWorksheet(String name) {
		return worksheets.get(name);
	}
	
	@Override
	public List<String> getWorksheetNames(){
		List<String> names = new ArrayList<String>();
		for(Worksheet worksheet: getWorksheets()){
			names.add(worksheet.getName());
		}
		return names;
	}

	@Override
	public List<Worksheet> getWorksheets() {
		return new ArrayList<Worksheet>(worksheets.values());
	}

	@Override
	public int getNumberOfWorksheets() {
		return worksheets.size();
	}
	
	public void insertWorksheet(OfflineWorksheet worksheet){
		worksheets.put(worksheet.getName(), worksheet);
	}

	@Override
	public void refresh() {
		load(path);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void save(String path) {
		HashMap<String, String> result = new HashMap<String, String>();
		Iterator it = worksheets.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry pairs = (Map.Entry) it.next();
			HashMap<String, String> map = ((OfflineWorksheet) pairs.getValue()).getData();
			result.put((String) pairs.getKey(), HashMapSerializer.serialize(map));
		}
		
	    FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(result);
		    oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void load(String path) {
		this.path = path;
		try {
		    FileInputStream fis = new FileInputStream(path);
		    ObjectInputStream ois;
			ois = new ObjectInputStream(fis);
		    HashMap<String, String> loaded = (HashMap<String, String>) ois.readObject();
		    worksheets = new HashMap<String, OfflineWorksheet>();
		    
		    Iterator it = loaded.entrySet().iterator();
		    while(it.hasNext()){
		    	Map.Entry pairs = (Map.Entry) it.next();
		    	OfflineWorksheet ws = new OfflineWorksheet((String) pairs.getKey(), this);
		    	ws.setData(HashMapSerializer.deserialize((String) pairs.getValue()));
		    	worksheets.put(ws.getName() , ws);
		    }
		    ois.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getParentFolder() {
		return null;
	}

	@Override
	public void duplicateSheet(String sheet, String name) {
		OfflineWorksheet a = (OfflineWorksheet) getWorksheet(sheet);
		OfflineWorksheet b = new OfflineWorksheet(name, this);
		b.setData(a.getData());
		b.setName(name);
		worksheets.put(name, b);
	}

	@Override
	public boolean worksheetExists(String name) {
		return worksheets.containsKey(name);
	}

	@Override
	public void createWorksheet(String name) {
		OfflineWorksheet worksheet = new OfflineWorksheet(name, this);
		worksheets.put(name, worksheet);
	}
	
}
