/**
 * 
 */
package org.neodatis.odb.ant;

/**
 * @author olivier
 *
 */
public class User {
	protected String name;

	public User(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
