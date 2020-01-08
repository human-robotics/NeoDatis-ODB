package org.neodatis.odb.test.fromusers.wonman.real;

import java.io.Serializable;
import java.util.Date;

public interface Entity extends Serializable{

	
	public String getId();
	
	public void setId(String id);
	
	/**
	 * Validates individual attributes and relationship with other entity.
	 */
	public void validateState();
	
	public void setTimeUpdated(Date date);
	public Date getTimeUpdated();
	
	public void setUserUpdated(String userUpdated);
	public String getUserUpdated();
	
	public String getUpdatedAtBy();
}
