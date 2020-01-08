package org.neodatis.odb.test.fromusers.wonman.real;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MenuGroup extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private int lineOrder;

	private Map<String, Menu> menuMap = new TreeMap<String, Menu>();
	
	@Override
	protected void validateEntityState() {
	}

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

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MenuGroup [name=" + name + ", description=" + description
				+ ", getId()="
				+ getId() + "]";
	}

	/**
	 * @return the lineOrder
	 */
	public int getLineOrder() {
		return lineOrder;
	}

	/**
	 * @param lineOrder the lineOrder to set
	 */
	public void setLineOrder(int lineOrder) {
		this.lineOrder = lineOrder;
	}

	public void addMenu(Menu menu) {
		if(menuMap.containsKey(menu.getId()) == false)
			menuMap.put(menu.getId(), menu);
	}
	
	public List<Menu> getMenus() {
		return new ArrayList<Menu>(menuMap.values());
	}
}
