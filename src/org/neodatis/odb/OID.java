/*
 NeoDatis ODB : Native Object Database (odb.info@neodatis.org)
 Copyright (C) 2007 NeoDatis Inc. http://www.neodatis.org

 "This file is part of the NeoDatis ODB open source object database".

 NeoDatis ODB is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 NeoDatis ODB is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.neodatis.odb;

import java.io.Serializable;

/**
 * The interface to represent OID. OID is a unique identifier for NeoDatis ODB
 * entities like objects and classes. The id is generated by NeoDatis
 * 
 * 
 * <pre>
 * 
 * OID have an owner id and an object id
 *
 * example:
 *
 * an OID has 3 attributes:
 * - a type(int)
 * - an owner of type long
 * - an object id of type long
 * OID oid = OID(type,owner id (long) , object id ( long) )
 * yy
 * OID databaseId( 1,0,1);
 * OID parametersOwner = OID(2,1,1); 
 * OID metaModelId = OID( parametersOwned,2);  // MetaModelId = ClassInfoOwnerId
 *
 * OID databaseHeaderOid = OID(parameterOwner,1);
 * OID storageEngineOid = OID(parameterOwner,2);
 *
 * OID of ClassInfo (Function, for example):
 * OID functionCiOid = OID( metaModelId, 1);
 * OID profileCiOid = OID( metaModelId, 2);
 * OID userCiOid = OID( metaModelId, 3);
 * 
 * So all ClassInfos of the metaModel can be retrieved using the metaModelId as owner and all objects of a specific class info can be retrieved using the class info oid
 *
 * 
 * </pre>
 * 
 * @author osmadja
 * 
 */
public interface OID extends Comparable, Serializable {

	/**
	 * To retrieve a string representation of an OID
	 * 
	 * @return
	 */
	String oidToString();
	
	byte[] toByte();
	
	
	boolean isNull();
	
	void setIsNew(boolean isNew);
	boolean isNew();

	//int getType();

}
