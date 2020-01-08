package org.neodatis.odb.test.fromusers.wonman.real;


import java.util.ArrayList;
import java.util.List;

public class Barloworld extends Organization {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<User> users = new ArrayList<User>();
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();
	
	@Override
	protected void validateEntityState() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the menuItems
	 */
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	/**
	 * @param menuItems the menuItems to set
	 */
	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Barloworld [getId()=" + getId() + "]";
	}

	@Override
	public String getName() {
		return "Barloworld";
	}

}
