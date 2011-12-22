package org.opendatatn.tnac.servlets;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MetadataFromCouch {
	private static class MetadataHolder {
        public static MetadataFromCouch metadata = new MetadataFromCouch();
    }
	
	private Map<String, JSONObject> circons;
	private Map<String, JSONObject> delegations;
	private Map<String, JSONObject> centers;
	private Map<String, JSONObject> bureaux;

	public static MetadataFromCouch getInstance() {
        return MetadataHolder.metadata;
    }
	
	public synchronized void initialize(){
		circons=getAll("circonscription","","circonscriptions");
		Iterator<String> iterC=circons.keySet().iterator();
		delegations=new TreeMap<String, JSONObject>();
		while (iterC.hasNext()){
			String e=iterC.next();
			Map<String, JSONObject> m=getAll("circonscription",e,"delegations");
			delegations.putAll(m);
		}
		centers=new TreeMap<String, JSONObject>();
		iterC=delegations.keySet().iterator();
		while (iterC.hasNext()){
			String e=iterC.next();
			Map<String, JSONObject> m=getAll("delegation",e,"centres");
			centers.putAll(m);
		}
		bureaux=new TreeMap<String, JSONObject>();
		iterC=centers.keySet().iterator();
		while (iterC.hasNext()){
			String e=iterC.next();
			Map<String, JSONObject> m=getAll("centre",e,"bureaux");
			bureaux.putAll(m);
		}
//		persist();
	}

//	private void persist() {
//		try {
//			FileOutputStream fos=new FileOutputStream(new File("D:/objs"));
//			ObjectOutputStream out=new ObjectOutputStream(fos);
//			out.writeObject(circons);
//			out.writeObject(delegations);
//			out.writeObject(centers);
//			out.writeObject(bureaux);
//			out.close();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		
//	}

	private Map<String, JSONObject> getAll(String resource,String parent,String objFieldName) {
		Map<String, JSONObject> result=new TreeMap<String, JSONObject>();
		String path="/tnac/v1/"+resource+"/"+parent;
		CouchJsonLoader loader=new CouchJsonLoader("api.opendatatn.org",path, 80, false);
		String s = loader.load(false, null);
		try {
			JSONObject obj=new JSONObject(s);
			JSONArray array=obj.getJSONArray(objFieldName);
			for (int i=0;i<array.length();i++){
				JSONObject c=array.getJSONObject(i);
				result.put((parent.isEmpty()?c.getString("code"):(parent+"/"+c.getString("code"))),c);
			}
			return result;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Map<String, JSONObject> getCircons() {
		return circons;
	}

	public Map<String, JSONObject> getDelegations() {
		return delegations;
	}

	public Map<String, JSONObject> getCenters() {
		return centers;
	}

	public Map<String, JSONObject> getBureaus() {
		return bureaux;
	}

}
