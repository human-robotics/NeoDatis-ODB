package org.neodatis.odb.test.enumeration;

public class User implements IUser{
	private UserRole role;
	private String name;
	private int index;

	public User(UserRole role, String name, int index) {
		super();
		this.role = role;
		this.name = name;
		this.index = index;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
