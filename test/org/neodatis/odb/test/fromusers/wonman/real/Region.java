package org.neodatis.odb.test.fromusers.wonman.real;

import java.util.ArrayList;
import java.util.List;

public class Region extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Customer customer;
	private long regionNumber;
	private String regionName;
	private String shortName;
	
	private List<Location> locations = new ArrayList<Location>();
	
	@Override
	protected void validateEntityState() {
		// TODO Auto-generated method stub

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
	 * @return the regionNumber
	 */
	public long getRegionNumber() {
		return regionNumber;
	}

	/**
	 * @param regionNumber the regionNumber to set
	 */
	public void setRegionNumber(long regionNumber) {
		this.regionNumber = regionNumber;
	}

	/**
	 * @return the regionName
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * @param regionName the regionName to set
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
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
		return "Region [customer=" + customer + ", regionNumber="
				+ regionNumber + ", regionName=" + regionName + ", shortName="
				+ shortName + ", locations=" + locations + ", getId()="
				+ getId() + "]";
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
