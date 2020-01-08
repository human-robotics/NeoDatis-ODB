package org.neodatis.odb.test.oid;

import org.neodatis.odb.*;
import org.neodatis.odb.core.layers.layer4.OidGenerator;
import org.neodatis.odb.core.oid.ExternalOIDImpl;
import org.neodatis.odb.core.oid.ExternalObjectOIDImpl;
import org.neodatis.odb.core.oid.sequential.StringOidImpl;
import org.neodatis.odb.core.oid.uuid.UniqueOidGeneratorImpl;

public class OIDFactory {

	protected static OidGenerator generator = new UniqueOidGeneratorImpl();
	
	/**
	 * @param oid
	 * @return
	 */
	public static ExternalOID toExternalOid1(ObjectOid oid, DatabaseId databaseId) {
		if(oid==null){
			return null;
		}
		if(oid instanceof ObjectOid){
			return new ExternalObjectOIDImpl(oid,databaseId);
		}
		return new ExternalOIDImpl(oid,databaseId);
	}
	
	public static ObjectOid buildObjectOID(ClassOid classOid) {
		return generator.createObjectOid(classOid);
	}
	public static ClassOid buildClassOID() {
		return generator.createClassOid();
	}
	
	public static OID buildStringOid1(String s) {
		return new StringOidImpl(s);
	}
	/*
	public static OID oidFromString(String oidString) {
		boolean isExternal = oidString.indexOf("@") != -1;

		if (isExternal) {
			return ExternalOIDImpl.oidFromString(oidString);
		}

		String[] tokens = oidString.split("\\.");
		int type = Integer.parseInt(tokens[0]);

		switch (type) {
		case OIDTypes.TYPE_CLASS_OID:
			return ClassOidImpl.oidFromString(oidString);
		case OIDTypes.TYPE_OBJECT_OID:
			return ObjectOidImpl.oidFromString(oidString);
		case OIDTypes.TYPE_STRING_OID:
			return StringOidImpl.oidFromString(oidString);
		}

		throw new NeoDatisRuntimeException(NeoDatisError.INVALID_OID_REPRESENTATION.addParameter(oidString));

	}

	/**
	 * @param oid
	 * @return
	 *
	public static ExternalOID toExternalOid(OID oid, DatabaseId databaseId) {
		if(oid==null){
			return null;
		}
		if(oid instanceof ObjectOid){
			return new ExternalObjectOIDImpl((ObjectOid) oid,databaseId);
		}
		return new ExternalOIDImpl(oid,databaseId);
	}
	*/

//	public static ObjectOid objectOidFromString(String sid) {
//		return ObjectOidImpl.fromString(sid);
//	}
//	public static ClassOid classOidFromString(String sid) {
//		return ClassOidImpl.fromString(sid);
//	}
//	public static StringOidImpl stringOidFromString(String sid) {
//		return StringOidImpl.fromString(sid);
//	}
}
