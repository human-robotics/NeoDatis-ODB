package org.neodatis.odb.test.trigger;

import org.neodatis.odb.ODB;
import org.neodatis.odb.test.ODBTest;

public class TestCloseListener extends ODBTest {

	
	public void test1(){
		String baseName = getBaseName();
		
		ODB odb = open(baseName);
		MyCloseListener l = new MyCloseListener();
		odb.ext().addCloseListener(l);
		odb.close();
		
		odb = open(baseName);
		odb.close();
		assertEquals(1, l.nbCloses);
	}
}
