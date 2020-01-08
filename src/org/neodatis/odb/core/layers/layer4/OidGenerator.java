/**
 * 
 */
package org.neodatis.odb.core.layers.layer4;

import org.neodatis.odb.*;

/**
 * @author olivier
 *
 */
public interface OidGenerator {
	
	
	
	ObjectOid createObjectOid(ClassOid classOid);
	ClassOid createClassOid();
	
	/**
	 * @param oid
	 * @return
	 */
	ExternalOID toExternalOid(ObjectOid oid, DatabaseId databaseId);
	ClassOid buildClassOID(byte[] bb);
	OID buildStringOid(String s);
	ObjectOid buildObjectOID(byte[] bb);
	
	ObjectOid objectOidFromString(String s);
	ClassOid classOidFromString(String s);
	
	void init(StorageEngine engine, boolean useCache);
	
	ObjectOid getNullObjectOid();
	ClassOid getNullClassOid();
	
	/** When an OidGenerator uses cache, it may have to commit the cached values on transaction commit*/
	void commit();
	
	String getSimpleName();
}
