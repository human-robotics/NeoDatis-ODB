package org.neodatis.odb.test.fromusers.wonman.real;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer extends Organization {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String shortName;
	private long customerNumber;	// sold-to number
	private Date since;
	
	private List<User> users = new ArrayList<User>();
	private List<Location> locations = new ArrayList<Location>();
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
	 * @return the locations
	 */
	public List<Location> getLocations() {
		return locations;
	}

	/**
	 * @param locations the locations to set
	 */
	public void setLocations(List<Location> locations) {
		this.locations = locations;
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

	/**
	 * @return the customerNumber
	 */
	public long getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(long customerNumber) {
		this.customerNumber = customerNumber;
	}

	/**
	 * @return the since
	 */
	public Date getSince() {
		return since;
	}

	/**
	 * @param since the since to set
	 */
	public void setSince(Date since) {
		this.since = since;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer [" + "shortName=" + shortName
				+ ", customerNumber=" + customerNumber + ", since=" + since
				+ ", getId()=" + getId() + "]";
	}

}
