package org.neodatis.odb.test.context;

import org.junit.Test;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

public class TestContextWithNonTransientContext extends ODBTest{
    @Test
	public void test1() {

		String baseName = getBaseName();

		ODB odb = open(baseName);

		// Create a computer with some components
		CellPhone nexusOne = new CellPhone("NexusOne");
		odb.store(nexusOne);
		odb.close();

		odb = open(baseName);
		Objects<CellPhone> phones = odb.query(CellPhone.class).objects();
		odb.close();
		
		assertEquals(1, phones.size());
	}
}
