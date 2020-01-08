package org.neodatis.odb.test.reconnect;

import org.neodatis.odb.core.context.NeoDatisObjectAdapter;

public class MyObject extends NeoDatisObjectAdapter {
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MyObject(String name) {
		super();
		this.name = name;
	}
	
	

}
