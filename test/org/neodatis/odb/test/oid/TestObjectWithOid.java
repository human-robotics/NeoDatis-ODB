package org.neodatis.odb.test.oid;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.other.ObjectWithOid;

/**
 * Reported bug by Moises > on 1.5.6
 * 
 * @author osmadja
 * 
 */
public class TestObjectWithOid extends ODBTest {

	public void test1() throws Exception {
		ODB odb = open(getBaseName());
		ObjectWithOid o = new ObjectWithOid("15", "test");
		ObjectOid oid = odb.store(o);
		odb.close();

		odb = open(getBaseName());
		ObjectWithOid o2 = (ObjectWithOid) odb.getObjectFromId(oid);
		odb.close();
		assertEquals(o.getOid(), o2.getOid());
		assertEquals(o.getName(), o2.getName());
	}
}
