package org.neodatis.odb.test.fromusers.wonman.real2;

public class User extends BaseEntity {

	private Organization organization;
	
	private String name;

	public User(Organization organization, String name) {
		super();
		this.organization = organization;
		this.name = name;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
