package org.neodatis.odb.test.fromusers.wonman;

public class User {
	protected Organization organization;
	protected String name;

	public User(String name,Organization organization) {
		super();
		this.name = name;
		this.organization = organization;
	}
	
	@Override
	public String toString() {
		return name + " - organization id is="+organization.id;
	}
	
}
