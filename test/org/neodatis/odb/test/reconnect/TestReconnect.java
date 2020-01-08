package org.neodatis.odb.test.reconnect;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

public class TestReconnect extends ODBTest {
	
	/** To test setting reconnectObjectsToSession to false to be able to save an object into a different db*/
	public void testNoReconnectIn2DBs(){
		String baseName = getBaseName();
		String baseName2 = getBaseName()+"-2";
		
		
		ODB odb1 = open(baseName, NeoDatis.getConfig().setReconnectObjectsToSession(true));
		MyObject mo = new MyObject("name1"); 
		odb1.store(mo);
		odb1.close();
		
		ODB odb2 = open(baseName2, NeoDatis.getConfig().setReconnectObjectsToSession(false));
		odb2.store(mo);
		odb2.close();
		
		odb2 = open(baseName2, NeoDatis.getConfig().setReconnectObjectsToSession(false));
		Objects<MyObject> oos = odb2.query(MyObject.class).objects();
		odb2.close();
		
		assertEquals(1, oos.size());
		
		
		
		
	}

}
