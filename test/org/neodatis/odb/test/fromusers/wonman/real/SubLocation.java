package org.neodatis.odb.test.fromusers.wonman.real;


public class SubLocation extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Location location;

	private long subLocationNumber;
	private String subLocationName;
	private String shortName;
	
	private String street1;
	private String street2;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	
	@Override
	protected void validateEntityState() {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the subLocationNumber
	 */
	public long getSubLocationNumber() {
		return subLocationNumber;
	}

	/**
	 * @param subLocationNumber the subLocationNumber to set
	 */
	public void setSubLocationNumber(long subLocationNumber) {
		this.subLocationNumber = subLocationNumber;
	}

	/**
	 * @return the subLocationName
	 */
	public String getSubLocationName() {
		return subLocationName;
	}

	/**
	 * @param subLocationName the subLocationName to set
	 */
	public void setSubLocationName(String subLocationName) {
		this.subLocationName = subLocationName;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubLocation [location=" + location + ", subLocationNumber="
				+ subLocationNumber + ", subLocationName=" + subLocationName
				+ ", shortName=" + shortName + ", street1=" + street1
				+ ", street2=" + street2 + ", city=" + city + ", province="
				+ province + ", country=" + country + ", postalCode="
				+ postalCode + ", getId()=" + getId() + "]";
	}

}
