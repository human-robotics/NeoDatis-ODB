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
package org.neodatis.odb.core.server.message;

import org.neodatis.odb.core.layers.layer3.OidAndBytes;
import org.neodatis.tool.wrappers.list.IOdbList;

public class ConnectMessageResponse extends Message{
	private IOdbList<OidAndBytes> oabsOfMetaModel;
	
	/** The server sends the oid generator class name to the client*/
	private String oidGeneratorClassName;
	private int version;
	private String databaseId;
	
	
	public ConnectMessageResponse(String baseId,String connectionId, String error){
		super(MessageType.CONNECT_RESPONSE,baseId,connectionId);
		setError(error);
	}

	public ConnectMessageResponse(String baseId,String sessionId, IOdbList<OidAndBytes> oabs, String oidGeneratorClassName, int version, String databaseId){
		super(MessageType.CONNECT_RESPONSE,baseId,sessionId);
		this.oabsOfMetaModel = oabs;
		this.oidGeneratorClassName = oidGeneratorClassName;
		this.version = version;
		this.databaseId = databaseId;
	}

	/**
	 * 
	 */
	public ConnectMessageResponse() {
		super();
	}

	public IOdbList<OidAndBytes> getOabsOfMetaModel() {
		return oabsOfMetaModel;
	}

	public void setOabsOfMetaModel(IOdbList<OidAndBytes> oabsOfMetaModel) {
		this.oabsOfMetaModel = oabsOfMetaModel;
	}

	public String getOidGeneratorClassName() {
		return oidGeneratorClassName;
	}

	public void setOidGeneratorClassName(String oidGeneratorClassName) {
		this.oidGeneratorClassName = oidGeneratorClassName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

}
