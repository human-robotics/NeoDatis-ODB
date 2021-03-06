package org.neodatis.odb.test.fromusers.wonman.real;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class BaseEntity implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String userUpdated;
	private Date timeUpdated = new Date(0);
	private int deleted;
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	/**
	 * @return the timeUpdated
	 */
	public Date getTimeUpdated() {
		return timeUpdated;
	}

	/**
	 * @param timeUpdated the timeUpdated to set
	 */
	public void setTimeUpdated(Date timeUpdated) {
		checkNull(timeUpdated);
		
		this.timeUpdated = timeUpdated;
	}

	
	public String getUpdatedAtBy() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		//
		return userUpdated + " at " + dateFormat.format(timeUpdated);
	}

	
	public void validateState() {
		if(isEmpty(userUpdated, "userUpdated"))
			throw new IllegalStateException("userUpdated was empty.");
		if(timeUpdated == null)
			throw new IllegalStateException("timeUpdated was null.");
		
		validateEntityState();
	}

	abstract protected void validateEntityState();


	protected void ifEmptyOrBeyondLength(String attributeName, String value, int min, int max) throws ArgumentException {
		if(isEmpty(value))
			throw new ArgumentException(attributeName + " is Required.");
		if(isLengthNotInBetween(value, min, max))
			throw new ArgumentException("The length of " + attributeName + " must be between " +
					min + " and " + max + ".");
	}

	protected void ifBeyondLength(String attributeName, String value, int min, int max) throws ArgumentException {
		if(isEmpty(value) == false) {
			if(value.length() < min || value.length() > max) {
				throw new ArgumentException("The length of " + attributeName + " must be between " +
						min + " and " + max + ".");
			}
		}
	}
	
	protected void ifZero(String attributeName, int value) throws ArgumentException {
		if(value == 0)
			throw new ArgumentException(attributeName + " must must be greater than 0.");
	}
	
	protected void ifBeyondRange(String attributeName, int value, int min, int max) throws ArgumentException {
		if(value < min || value > max)
			throw new ArgumentException(attributeName + " must be between " + min + " and " + max + ".");
	}
	
	protected void ifNotInCategory(String attributeName, int value, int... category) throws ArgumentException {
		
		boolean matched = false;
		
		for(int cat : category) {
			if(value == cat) {
				matched = true;
				break;
			}
		}
		if(!matched) {
			StringBuffer sb = new StringBuffer();
			for(int cat : category) {
				sb.append(" or ").append(cat);
			}
			throw new ArgumentException(attributeName + " must be either " + sb.substring(4) + "."); 
		}
	}
	
	/**
	 * Check if a given object is null
	 * throws runtime exception.
	 * @param obj
	 */
	public static void checkNull(Object obj) {
		if(obj == null) 
			throw new IllegalArgumentException("A given object was null.");
	}
	
	public static void checkNull(Object obj, String attributeName) {
		if(obj == null) 
			throw new IllegalArgumentException(attributeName + " was null.");
	}
	
	public static boolean isEmpty(String value) {
		boolean empty = true;
		//
		if(value != null && value.length() > 0)
			empty = false;
		//
		return empty;
	}
	public static boolean isEmpty(Object value) {
		boolean empty = true;
		//
		if(value != null)
			empty = false;
		//
		return empty;
	}
	
	public static boolean isEmpty(String value, String attributeName) {
		checkNull(value, attributeName);
		//
		return value.length() == 0;
	}

	public static boolean isExceedMaxLength(String value, int max) {
		checkNull(value);
		if(isEmpty(value)) return false;
		
		return ( value.length() > max);
	}
	
	public static boolean isLengthNotInBetween(String value, int min, int max) {
		checkNull(value);
		if(isEmpty(value)) return false;
		
		return (value.length() < min || value.length() > max);
	}
	
	public static boolean isNumberNotInTheList(int value, Integer[] valueList) {
		boolean result = true;
		//
		List<Integer> list = Arrays.asList(valueList);
		for(Integer n : list) {
			if(n.intValue() == value) {
				result = false;
				break;
			}
		}
		//
		return result;
	}
	
	public static boolean isStringNotInTheList(String value, String...strings) {
		
		checkNull(value);
		
		List<String> list = Arrays.asList(strings);
		//
		return list.indexOf(value) > 0;
	}
	
	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	public int getDeleted() {
		return this.deleted;
	}

	/**
	 * @return the userUpdated
	 */
	public String getUserUpdated() {
		return userUpdated;
	}

	/**
	 * @param userUpdated the userUpdated to set
	 */
	public void setUserUpdated(String userUpdated) {
		this.userUpdated = userUpdated;
	}
}
