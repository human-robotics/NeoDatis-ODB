package org.neodatis.odb.test.server.concurrent;

import junit.framework.Assert;
import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

import java.io.File;

public class TestConcurrentAccess extends ODBTest {
		ODBServer server = null;

		public void setUp() {
			this.server = NeoDatis.openServer(9999);
			new File("target/test.dat").delete();
			this.server.addBase("base", "target/test.dat");
			this.server.startServer(true);
			
			ODB odb = createClient();
			odb.store(new Data());
			odb.close();
		}

		public void tearDown() {
			server.close();
		}
		public void testConcurrent() throws Exception {
			// start 3 threads inserting a simple object
			for (int i = 0; i < 3; i++) {
				Thread t = new Thread1(this);
				t.start();
				// /Thread.sleep(1000);
			}

			Thread.sleep(1000); // wait for all thread to finish
			ODB odb = createClient();
			Objects<Data> datas = odb.query(Data.class).objects();		
			odb.close();
			System.out.println("found: " + datas.first().id);
			Assert.assertEquals(3, datas.size());  // Fails with <1> instead of <3>

		}

		public ODB createClient() {
			// same error with both methods
			//return ODBFactory.openClient("localhost", 9999, "base");
			return this.server.openClient("base");
		}


}
