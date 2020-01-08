package org.neodatis.odb.test.fromusers.wonman.real;

import java.util.ArrayList;
import java.util.List;

public class Location extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long locationNumber;
	private String locationName;
	private String shortName;
	
	private String street1;
	private String street2;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	private String timeZone;
	
	private List<SubLocation> subLocations = new ArrayList<SubLocation>();
	
	@Override
	protected void validateEntityState() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the subLocations
	 */
	public List<SubLocation> getSubLocations() {
		return subLocations;
	}

	/**
	 * @param subLocations the subLocations to set
	 */
	public void setSubLocations(List<SubLocation> subLocations) {
		this.subLocations = subLocations;
	}

	/**
	 * @return the locationNumber
	 */
	public long getLocationNumber() {
		return locationNumber;
	}

	/**
	 * @param locationNumber the locationNumber to set
	 */
	public void setLocationNumber(long locationNumber) {
		this.locationNumber = locationNumber;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
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

	/**
	 * @return the street1
	 */
	public String getStreet1() {
		return street1;
	}

	/**
	 * @param street1 the street1 to set
	 */
	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	/**
	 * @return the street2
	 */
	public String getStreet2() {
		return street2;
	}

	/**
	 * @param street2 the street2 to set
	 */
	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Location [locationNumber=" + locationNumber + ", locationName="
				+ locationName + ", shortName=" + shortName + ", street1="
				+ street1 + ", street2=" + street2 + ", city=" + city
				+ ", province=" + province + ", country=" + country
				+ ", postalCode=" + postalCode + ", timeZone=" + timeZone
				+ ", subLocations=" + subLocations + ", getId()=" + getId()
				+ "]";
	}

}
