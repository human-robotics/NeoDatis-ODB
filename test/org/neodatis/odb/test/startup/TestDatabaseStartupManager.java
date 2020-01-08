/**
 * 
 */
package org.neodatis.odb.test.startup;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 *
 */
public class TestDatabaseStartupManager extends ODBTest {
	public void test1(){
		MyDatabaseStartupManager manager = new MyDatabaseStartupManager();
		NeoDatisConfig config = NeoDatis.getConfig().registerDatabaseStartupManager(manager);
		String baseName = getBaseName();
		ODB odb = open(baseName,config);
		odb.close();
		
		assertTrue(manager.called);
	}

}
