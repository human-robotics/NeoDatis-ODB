/**
 * 
 */
package org.neodatis.odb.test.oid;

import org.neodatis.odb.ExternalOID;
import org.neodatis.odb.ODB;
import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

/**
 * @author olivier
 * 
 */
public class TestOIDToString extends ODBTest {
	public void test1() {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		OID oid = odb.store(new Function("My Function"));
		odb.close();
		String soid = oid.oidToString();

		odb = open(baseName);
		ObjectOid oid2 = odb.ext().objectOidFromString(soid);
		Function f = (Function) odb.getObjectFromId(oid2);
		odb.close();
		assertEquals("My Function", f.getName());

	}

	public void test3() {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		ObjectOid oid = odb.store(new Function("My Function"));
		ExternalOID eoid = odb.ext().convertToExternalOID(oid);
		odb.close();
		String soid = eoid.oidToString();
		println(soid);

		odb = open(baseName);
		ObjectOid oid2 = odb.ext().objectOidFromString(soid);
		odb.close();
		

		assertEquals(eoid, oid2);

	}


}
