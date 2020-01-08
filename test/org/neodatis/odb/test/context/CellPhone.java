package org.neodatis.odb.test.context;

import org.neodatis.odb.NeoDatisContext;
import org.neodatis.odb.NeoDatisObject;

public class CellPhone implements NeoDatisObject{
	
	protected NeoDatisContext neoDatisContext;
	private String name;
	
	public NeoDatisContext getNeoDatisContext() {
		return neoDatisContext;
	}

	public void setNeoDatisContext(NeoDatisContext context) {
		this.neoDatisContext = context;
	}

	public CellPhone(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
