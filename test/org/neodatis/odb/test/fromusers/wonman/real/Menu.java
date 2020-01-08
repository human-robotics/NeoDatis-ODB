package org.neodatis.odb.test.fromusers.wonman.real;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Menu extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MenuGroup menuGroup = null;
	
	private String name;
	private String description;
	private int lineOrder;
	
	private Map<String, MenuItem> menuItemMap = new TreeMap<String, MenuItem>();
	
	@Override
	protected void validateEntityState() {
		if(isEmpty(name))
			throw new RuntimeException("name was empty.");
		if(lineOrder == 0)
			throw new RuntimeException("line order wat not set.");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 * @throws ArgumentException ifEmptyOrBeyondLength("Name", name, 1, 20)
	 */
	public void setName(String name) throws ArgumentException {
		ifEmptyOrBeyondLength("Name", name, 1, 20);
		
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
	 * @throws ArgumentException 
	 */
	public void setDescription(String description) throws ArgumentException {
		ifBeyondLength("Description", description, 0, 50);
		
		this.description = description;
	}
	/**
	 * @return the menuGroup
	 */
	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	/**
	 * @param menuGroup the menuGroup to set
	 */
	public void setMenuGroup(MenuGroup menuGroup) {
		this.menuGroup = menuGroup;
	}

	/**
	 * @return the lineOrder
	 */
	public int getLineOrder() {
		return lineOrder;
	}

	/**
	 * @param lineOrder the lineOrder to set
	 * @throws ArgumentException ifZero("Line Order", lineOrder)
	 */
	public void setLineOrder(int lineOrder) throws ArgumentException {
		
		ifZero("Line Order", lineOrder);
		
		this.lineOrder = lineOrder;
	}
	
	public void addMenuItem(MenuItem menuItem) {
		if(menuItemMap.containsKey(menuItem.getId()) == false) {
			menuItemMap.put(menuItem.getId(), menuItem);
		}
	}
	
	public List<MenuItem> getMenuItems() {
		return new ArrayList<MenuItem>(menuItemMap.values()) ;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Menu [menuGroup=" + menuGroup + ", name=" + name
				+ ", description=" + description + ", lineOrder=" + lineOrder
				+ ", menuItemMap=" + menuItemMap + ", getId()=" + getId()
				+ ", getUpdatedAtBy()=" + getUpdatedAtBy() + "]";
	}
}
