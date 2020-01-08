package org.neodatis.odb.test.fromusers.wonman.real;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class User extends BaseEntity {

	/**
	 * 
	 */
	public static final int MASTER_USER = 1;
	public static final int LOGIN_USER = 2;
	public static final String[] userTypes = {"Master User", "Login User"};
	
	public static final int ROLE_ADMIN = 1;
	public static final int ROLE_GENERAL = 2;
	public static final String[] roles = {"Admin Role", "General Role"};
	
	public static final String[] DATE_PATTERN = {"MM/dd/yyyy","dd.MM.yyyy"}; 
	
	private static final long serialVersionUID = 1L;

	private Organization organization;
	private int userType;
	private int role;
	
	private String loginID;
	private String password = "welcome1";
	private String firstName;
	private String lastName;
	private String title;
	private String datePattern;
	private String timePattern;
	private String language;
	private String emailAddress;
	
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();
	
	private Map<String, MenuGroup> menuGroupMap = null;
	
	private List<SubLocation> subLocations = new ArrayList<SubLocation>();
	
	@Override
	protected void validateEntityState() {
	}

	/**
	 * @return the organization
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the userType
	 */
	public int getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(int userType) {
		this.userType = userType;
	}

	/**
	 * @return the role
	 */
	public int getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(int role) {
		this.role = role;
	}

	/**
	 * @return the loginUser
	 */
	public static int getLoginUser() {
		return LOGIN_USER;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the datePattern
	 */
	public String getDatePattern() {
		return datePattern;
	}

	/**
	 * @param datePattern the datePattern to set
	 */
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	/**
	 * @return the timePattern
	 */
	public String getTimePattern() {
		return timePattern;
	}

	/**
	 * @param timePattern the timePattern to set
	 */
	public void setTimePattern(String timePattern) {
		this.timePattern = timePattern;
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
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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

	public String getRoleString() {
		return roles[role - 1];
	}
	
	public String getUserTypeString() {
		return userTypes[userType - 1];
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the loginID
	 */
	public String getLoginID() {
		return loginID;
	}

	/**
	 * @param loginID the loginID to set
	 */
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	
	public List<MenuGroup> getMenuGroups() {
		if(menuGroupMap == null) {
			menuGroupMap  = new TreeMap<String, MenuGroup>();
			
			for(MenuItem menuItem : menuItems) {
				MenuGroup menuGroup = menuItem.getMenuGroup();
				
				if(menuGroupMap.containsKey(menuGroup.getId()) == false) {
					menuGroupMap.put(menuGroup.getId(), menuGroup);
				}
				
				Menu menu = menuItem.getMenu();
				menuGroup.addMenu(menu);
				
				menu.addMenuItem(menuItem);
			}
		}
		//
		return new ArrayList<MenuGroup>(menuGroupMap.values());
	}
	
	public void addMenuGroup(MenuGroup menuGroup) {
		menuGroupMap.put(menuGroup.getId(), menuGroup);
	}
	
	public String getName() {
		return firstName + " " + lastName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [organization=" + organization + "]";
	}
}
