package org.neodatis.odb.test.server.trigger.oid;

import org.neodatis.odb.ObjectOid;

public class ClassWithObjectOid {
	protected ObjectOid oid;
	protected String name;
	
	public ClassWithObjectOid(String name){
		this.name = name;
	}

	public ObjectOid getOid() {
		return oid;
	}

	public void setOid(ObjectOid oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
