/**
 * 
 */
package org.neodatis.odb.test.context;

import org.neodatis.odb.core.context.NeoDatisObjectAdapter;

/**
 * @author olivier
 *
 */
public class Component extends NeoDatisObjectAdapter {
	protected String name;
	protected String description;
	public Component(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
