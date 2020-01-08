package org.neodatis.odb.test.arraycollectionmap;

import java.util.HashMap;
import java.util.Map;

public class ClassWithMap {
	protected String name;
	protected Map<String , String> map;
	
	public ClassWithMap(String name , String key , String value){
		this.name = name;
		map = new HashMap<String, String>();
		map.put(key, value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

}
