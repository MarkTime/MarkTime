package boar401s2.marktime.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.util.Base64;

public class HashMapSerializer {

	@SuppressWarnings("rawtypes")
	public static String serialize(HashMap<String, String> map){
		String result = "";
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			result = result + (String)pairs.getKey()+","+(String)pairs.getValue()+";";
		}
		return new String(Base64.encode(result.getBytes(), Base64.DEFAULT));
	}
	
	public static HashMap<String, String> deserialize(String serializedHashmap){
		String decoded = new String(Base64.decode(serializedHashmap, Base64.DEFAULT));
		String[] parts = decoded.split(";");
		HashMap<String, String> result = new HashMap<String, String>();
		for (String s: parts){
			String[] pair = s.split(",");
			result.put(pair[0], pair[1]);
		}
		return result;
	}
	
}
