package org.neodatis.odb.test.query.criteria;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

public class TestRelation extends ODBTest {

	public void testNullRelation() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		odb.store(new Class2());
		odb.close();

		odb = open(baseName);
		Query q = odb.query(Class2.class, W.isNull("class1.name"));
		Objects os = odb.getObjects(q);
		odb.close();
		assertEquals(1, os.size());
		Class2 c2 = (Class2) os.first();

		assertEquals(null, c2.getClass1());
		

	}
	
	

}
