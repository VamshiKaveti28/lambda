package com.external;

import org.json.JSONObject;

public class HVSTest {
	public static void main(String[] args){
		
		JSONObject obj = new JSONObject();
		obj.put("state", "AL");
		obj.put("futureCost", "2036");
		obj.put("age", "23");
		
		obj.put("inflationRate", 5);

		obj.put("region", 7);
		
		
		//HVS.getHVSData(obj);
		
		LTCG.getLTCGData(obj);
	}
}