
package org.neodatis.odb.test.oid;

import org.neodatis.odb.ClassOid;
import org.neodatis.odb.ODB;
import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.oid.uuid.ClassOidImpl;
import org.neodatis.odb.core.oid.uuid.ObjectOidImpl;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.util.Map;
import java.util.UUID;

public class TestOid extends ODBTest {
	public void testEquals() {
		UUID uuid1 = UUID.randomUUID();
		UUID uuid2 = UUID.randomUUID();
		ClassOid coid1 = new ClassOidImpl(uuid1);
		ClassOid coid2 = new ClassOidImpl(uuid1);
		OID oid1 = new ObjectOidImpl(uuid2, coid1);
		OID oid2 = new ObjectOidImpl(uuid2, coid2);

		assertEquals(oid1, oid2);
	}

	public void testOIdInMap() {
		UUID uuid1 = UUID.randomUUID();
		UUID uuid2 = UUID.randomUUID();
		ClassOid coid1 = new ClassOidImpl(uuid1);
		ClassOid coid2 = new ClassOidImpl(uuid1);
		OID oid1 = new ObjectOidImpl(uuid2, coid1);
		OID oid2 = new ObjectOidImpl(uuid2, coid2);

		Map map = new OdbHashMap();
		map.put(oid1, "oid1");

		Map map2 = new OdbHashMap();
		map2.put(coid1, "coid1");

		assertNotNull(map.get(oid2));
		assertNotNull(map2.get(coid2));
	}


	public void testAndy1() {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		B b1 = new B("b");
		A a1 = new A("a", b1);

		odb.store(a1);
		ObjectOid oida = odb.getObjectId(a1);
		ObjectOid oidb = odb.getObjectId(b1);
		odb.close();

		odb = open(baseName);
		A a2 = (A) odb.getObjectFromId(oida);
		B b2 = (B) odb.getObjectFromId(oidb);
		odb.close();
		assertNotNull(a2);
		assertNotNull(b2);
		assertNotNull(a2.getB());

	}

	public void testAndy2() {
		String baseName = getBaseName();
		// LogUtil.allOn(true);
		ODB odb = open(baseName);
		B b1 = new B("b");
		A a1 = new A("a", b1);

		ObjectOid oida = odb.store(a1);

		ObjectOid oidb = odb.getObjectId(b1);
		odb.close();

		odb = open(baseName);
		A a2 = (A) odb.getObjectFromId( oida);
		B b2 = (B) odb.getObjectFromId( oidb);
		odb.close();
		assertNotNull(a2);
		assertNotNull(b2);
		assertNotNull(a2.getB());

	}

	public void testAndy3() {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		try {
			UUID uuid1 = UUID.randomUUID();
			UUID uuid2 = UUID.randomUUID();
			ClassOid coid1 = new ClassOidImpl(uuid1);
			ObjectOid oid1 = new ObjectOidImpl(uuid2, coid1);
			A a2 = (A) odb.getObjectFromId(oid1);
			fail("Should have thrown Exception");
		} catch (Exception e) {
			// ok must enter the catch block
		}
	}

}
