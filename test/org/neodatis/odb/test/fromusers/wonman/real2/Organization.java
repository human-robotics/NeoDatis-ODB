/**
 * 
 */
package org.neodatis.odb.test.fromusers.wonman.real2;


/**
 * @author WL5989
 *
 */
abstract public class Organization extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
