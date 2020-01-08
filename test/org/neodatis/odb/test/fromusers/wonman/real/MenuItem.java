package org.neodatis.odb.test.fromusers.wonman.real;




public class MenuItem extends BaseEntity {

	private static final long serialVersionUID = 1L;
	public static final int BELONGS_TO_CUSTOMER = 1;
	public static final int BELONGS_TO_BARLOWORLD = 2;
	public static final int BELONGS_TO_BOTH = 3;
	public static final int[] belongsToCategory = new int[]{BELONGS_TO_CUSTOMER, 
		BELONGS_TO_BARLOWORLD, BELONGS_TO_BOTH};

	
	private MenuGroup menuGroup;
	private Menu menu;
	
	private String name;
	private String description;
	private String uri;
	private int belongsTo;
	private int lineOrder;
	private boolean selected;

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
	 * @throws ArgumentException ifBeyondLength("Description", description, 0, 50)
	 */
	public void setDescription(String description) throws ArgumentException {
		ifBeyondLength("Description", description, 0, 50);
		
		this.description = description;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 * @throws ArgumentException ifEmptyOrBeyondLength("URI", uri, 1, 50)
	 */
	public void setUri(String uri) throws ArgumentException {
		ifEmptyOrBeyondLength("URI", uri, 1, 50);
		
		this.uri = uri;
	}

	/**
	 * @return the belongsTo
	 */
	public int getBelongsTo() {
		return belongsTo;
	}

	/**
	 * @param belongsTo the belongsTo to set
	 * @throws ArgumentException ifNotInCategory("Belongs To", belongsTo, 
	 * 	MenuConstants.belongsToCategory)
	 */
	public void setBelongsTo(int belongsTo) throws ArgumentException {
		ifNotInCategory("Belongs To", belongsTo, belongsToCategory);
		this.belongsTo = belongsTo;
	}

	/**
	 * @return the menu
	 */
	public Menu getMenu() {
		return menu;
	}

	/**
	 * @param menu the menu to set
	 */
	public void setMenu(Menu menu) {
		this.menu = menu;
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
	 */
	public void setLineOrder(int lineOrder) {
		this.lineOrder = lineOrder;
	}
	
	public String getPage() {
		if(uri.startsWith("/")) uri = uri.substring(1);
		
		return "/faces/" + uri;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MenuItem [menuGroup=" + menuGroup + ", menu=" + menu
				+ ", name=" + name + ", description=" + description + ", uri="
				+ uri + ", belongsTo=" + belongsTo + ", lineOrder=" + lineOrder
				+ ", selected=" + selected + ", getId()=" + getId() + "]";
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
