/**
 * 
 */
package org.neodatis.odb.test.other;

import org.neodatis.odb.ODB;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 *
 */
public class TestGetDatabaseName extends ODBTest {
	
	public void test1(){
		String baseName = getBaseName();
		ODB odb = open(baseName);
		SessionEngine engine = Dummy.getEngine(odb);
		String s = odb.getName();
		if(isLocal){
			assertTrue(baseName.endsWith(s));
		}else{
			assertEquals("unit-test-data/name.neodatis@127.0.0.1:13000", s);
			
		}
		
	}


}
