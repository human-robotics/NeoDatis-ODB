/**
 * 
 */
package org.neodatis.odb.test.performance.oid;

import org.neodatis.odb.ClassOid;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.oid.uuid.ObjectOidImpl;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.oid.OIDFactory;

/**
 * @author olivier
 * 
 */
public class TestOidPerformance extends ODBTest {
	public void test1() {

		int size = 1000000;

		ClassOid coid = OIDFactory.buildClassOID();

		long start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			ObjectOid oid = OIDFactory.buildObjectOID(coid);
			oid.oidToString();
		}
		long end = System.currentTimeMillis();
		println(end - start);
	}

	public void test2() {

		int size = 1000000;

		ClassOid coid = OIDFactory.buildClassOID();
		ObjectOidImpl oid = (ObjectOidImpl) OIDFactory.buildObjectOID(coid);

		long start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			oid.oidToString();
		}
		long end = System.currentTimeMillis();
		println(end - start);
	}

}
