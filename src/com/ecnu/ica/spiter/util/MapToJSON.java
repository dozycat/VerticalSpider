package com.ecnu.ica.spiter.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map;

import net.sf.json.JSONObject;

public class MapToJSON {
	private JSONObject jsonObject;

	public String mapToJSON(Map map) {
        jsonObject = JSONObject.fromObject( map );  
		return jsonObject.toString();
	}
	
    public void testMapToJSON(){
        Map map = new HashMap();  
        map.put( "姓名", "json" );  
        map.put( "年龄", Boolean.TRUE );  
        map.put( "int", new Integer(1) );  
        map.put( "arr", new String[]{"a","b"} );  

        jsonObject = JSONObject.fromObject( map );  
        System.out.println( jsonObject );  
    }
    
    public static  void  main(String[] args){
    	MapToJSON mtj=new MapToJSON();
    	mtj.testMapToJSON();
    }
}
