package org.neodatis.odb.test.oid;

import org.neodatis.odb.ClassOid;

public class ClassWithClassOid {
	public ClassWithClassOid(String string, ClassOid coid1) {
		this.name = string;
		this.coid = coid1;
	}
	String name;
	ClassOid coid;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ClassOid getCoid() {
		return coid;
	}
	public void setCoid(ClassOid coid) {
		this.coid = coid;
	}
	
	
}
