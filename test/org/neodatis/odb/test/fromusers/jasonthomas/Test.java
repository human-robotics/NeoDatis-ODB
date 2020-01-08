package org.neodatis.odb.test.fromusers.jasonthomas;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.test.ODBTest;

public class Test extends ODBTest {

	/**
	 * This test fails because each client should execute in different threads
	 * as the session id is stored in a thread specific map
	 * 
	 */
	public void testSameVm() {
		if(!testNewFeature){
			return;
		}
		
		TestObject object1 = new TestObject();
		TestObject object2 = new TestObject();

		ODBServer server = null;

		String baseName = getBaseName();
		try {
			server = NeoDatis.openServer(8000);
			server.addBase(baseName, baseName);
			server.startServer(true);

			ODB client1 = server.openClient(baseName);
			ODB client2 = server.openClient(baseName);

			client2.store(object2);
			client2.close();

			client1.store(object1);
			client1.close();

		} finally {
			server.close();
		}
	}

	public void testRealClientServer() {
		TestObject object1 = new TestObject();
		TestObject object2 = new TestObject();
		String baseName = getBaseName();
		ODBServer server = null;

		try {
			server = NeoDatis.openServer(8000);
			server.addBase(baseName, baseName);
			server.startServer(true);

			ODB client1 = NeoDatis.openClient("localhost", 8000, baseName);

			ODB client2 = NeoDatis.openClient("localhost", 8000, baseName);
			client2.store(object2);
			client2.close();

			client1.store(object1);
			client1.close();

		} finally {
			server.close();
		}
	}

}

class TestObject {
	int i = 0;
}
