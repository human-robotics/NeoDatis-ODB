package org.neodatis.odb.core.mock;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.core.layers.layer4.BaseIdentification;

public class MockBaseIdentification implements BaseIdentification {

	public boolean canWrite() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getBaseId() {
		return "mock";
	}

	public boolean isLocal() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.layers.layer3.IBaseIdentification#getDirectory()
	 */
	public String getDirectory() {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.layers.layer3.IBaseIdentification#getPassword()
	 */
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.layers.layer3.IBaseIdentification#getUserName()
	 */
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.layers.layer4.IBaseIdentification#getFullIdentification
	 * ()
	 */
	public String getFullIdentification() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.layers.layer4.IBaseIdentification#getProperties()
	 */
	public NeoDatisConfig getConfig() {
		// TODO Auto-generated method stub
		return NeoDatis.getConfig();
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.layers.layer4.BaseIdentification#copy()
	 */
	public BaseIdentification copy() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.layers.layer4.BaseIdentification#setConfig(org.neodatis.odb.NeoDatisConfig)
	 */
	public void setConfig(NeoDatisConfig neoDatisConfig) {
		// TODO Auto-generated method stub
		
	}

}
