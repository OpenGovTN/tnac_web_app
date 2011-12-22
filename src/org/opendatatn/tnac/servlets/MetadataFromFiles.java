package org.opendatatn.tnac.servlets;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class MetadataFromFiles {
	private static class MetadataHolder {
        public static MetadataFromFiles metadata = new MetadataFromFiles();
    }
	
	public static MetadataFromFiles getInstance() {
        return MetadataHolder.metadata;
    }
	
	JSONObject root=new JSONObject();
	static String filePrefix="raw";
	static String filePostfix=".csv";
	static File container=(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)?new File("D:/tnacData"):new File("/root/tomcat/ROOT/WEB-INF");
	
	public synchronized void initialize(){
		File persisted=new File(container,"root.json");
		if (persisted.exists()){
			try {
				String json=FileUtils.readFileToString(persisted,"UTF-8");
				root=new JSONObject(new JSONTokener(json));
				return;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		long t=System.currentTimeMillis();
		int countC=0;
		int countD=0;
		int countCt=0;
		int countB=0;
		try {
			JSONArray circonsriptions=readArrayFromCsv(new File(container,filePrefix+filePostfix));
			for (int circIndex=0;circIndex<circonsriptions.length();circIndex++){
				countC++;
				JSONObject obj=circonsriptions.getJSONObject(circIndex);
				JSONArray delegations=readArrayFromCsv(new File(container,filePrefix+"-"+obj.getString("code")+filePostfix));
				for (int delegIndex=0;delegIndex<delegations.length();delegIndex++){
					countD++;
					JSONObject objD=delegations.getJSONObject(delegIndex);
					JSONArray centres=readArrayFromCsv(new File(container,filePrefix+"-"+obj.getString("code")+"-"+objD.getString("code")+filePostfix));
					for (int centrIndex=0;centrIndex<centres.length();centrIndex++){
						countCt++;
						JSONObject objC=centres.getJSONObject(centrIndex);
						JSONArray bureaux=readArrayFromCsv(new File(container,filePrefix+"-"+obj.getString("code")+"-"+objD.getString("code")+"-"+objC.getString("code")+filePostfix));
						setBuureauPath(bureaux,obj.getString("code")+"/"+objD.getString("code")+"/"+objC.getString("code"));
						countB+=bureaux.length();
						objC.put("children", bureaux);
						objC.put("resource","centre");
						objC.put("path",obj.getString("code")+"/"+objD.getString("code")+"/"+objC.getString("code"));
			}
				objD.put("children", centres);
				objD.put("resource","delegation");
				objD.put("path",obj.getString("code")+"/"+objD.getString("code"));
			}
				obj.put("children", delegations);
				obj.put("resource","circonscription");
				obj.put("path",obj.getString("code"));
			}
			root.put("children",circonsriptions);
			persist(root);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("circ:"+countC);
		System.out.println("del:"+countD);
		System.out.println("centr:"+countCt);
		System.out.println("bur:"+countB);
		System.out.println("total time:"+(System.currentTimeMillis()-t)/1000);
	}


	private void setBuureauPath(JSONArray bureaux, String paretnPath) throws JSONException {
		for (int index=0;index<bureaux.length();index++){
			JSONObject obj=bureaux.getJSONObject(index);
			obj.put("path", paretnPath+"/"+obj.getString("code"));
		}
		
	}


	private void persist(JSONObject e) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(e.toString());
		String prettyJsonString = gson.toJson(je);
		try {
			FileUtils.writeStringToFile(new File(container,"root.json"), prettyJsonString, "UTF-8");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	public JSONObject getMetadata(){
		return root;
	}
	
	
	private JSONArray readArrayFromCsv(File file){
		try {
			@SuppressWarnings("unchecked")
			List<String> l=FileUtils.readLines(file, "UTF-8");
			JSONArray array=new JSONArray();
			Iterator<String> iter=l.iterator();
			while (iter.hasNext()){
				String s=iter.next();
				String[]parts=s.split(",");
				if (parts!=null && parts.length==2){
					JSONObject obj=new JSONObject();
					obj.put("name",parts[0]);
					obj.put("code",parts[1]);
					array.put(obj);
				}
			}
			return array;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
