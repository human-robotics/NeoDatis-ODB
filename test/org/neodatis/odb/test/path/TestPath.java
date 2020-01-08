package org.neodatis.odb.test.path;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.human.Human;

import java.io.File;

public class TestPath extends ODBTest {
	public void testWitHJDBM() {
		String baseName = getBaseName();
		NeoDatisConfig config = NeoDatis.getConfig();

		ODB odb = NeoDatis.open(baseName, config);
		odb.store(new Human("F" , "Karine"));
		odb.close();

		odb = NeoDatis.open(baseName, config);
		Objects<Human> humans = odb.query(Human.class).objects();
		odb.close();

		File f = new File(baseName);
		assertTrue(f.exists());
	}

}
