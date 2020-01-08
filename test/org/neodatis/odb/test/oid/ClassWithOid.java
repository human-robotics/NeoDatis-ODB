package org.neodatis.odb.test.oid;

import org.neodatis.odb.ObjectOid;

public class ClassWithOid {
	private String name;
	private ObjectOid oid;

	public ClassWithOid(String name, ObjectOid oid) {
		super();
		this.name = name;
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectOid getOid() {
		return oid;
	}

	public void setOid(ObjectOid oid) {
		this.oid = oid;
	}

}
