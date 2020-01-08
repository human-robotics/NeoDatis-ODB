package org.neodatis.odb.test.server.trigger.oid;

import org.neodatis.odb.OID;

public class ClassWithOid {
	protected OID oid;
	protected String name;
	
	public ClassWithOid(String name){
		this.name = name;
	}

	public OID getOid() {
		return oid;
	}

	public void setOid(OID oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
