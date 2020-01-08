/**
 * 
 */
package org.neodatis.odb.test.config;

import org.junit.Test;
import org.neodatis.odb.ODB;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.core.session.info.DatabaseInfo;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

/**
 * @author olivier
 *
 */
public class TestDatabaseInfo extends ODBTest{

    @Test
	public void test1() throws Exception{

		String baseName = getBaseName();
		
		ODB odb = open(baseName);
		
		SessionEngine engine = Dummy.getEngine(odb);
		Session session = engine.getSession();
		
		DatabaseInfo di1 = session.getDatabaseInfo();
		odb.store(new Function("f1"));
		
		odb.close();
		Thread.sleep(30);
		odb = open(baseName);
		engine = Dummy.getEngine(odb);
		session = engine.getSession();
		DatabaseInfo di2 = session.getDatabaseInfo();
		System.out.println(odb.query(Function.class).objects());
		odb.close();
		
		System.out.println(di1.toString(","));
		System.out.println(di2.toString(","));
		assertEquals(di1.toString(","), di2.toString(","));
		
	}

}
