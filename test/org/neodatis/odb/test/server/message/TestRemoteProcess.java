/**
 * 
 */
package org.neodatis.odb.test.server.message;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.core.layers.layer4.plugin.memory.InMemoryStorageEngine;
import org.neodatis.odb.core.server.message.process.DefaultProcessReturn;
import org.neodatis.odb.core.server.message.process.RemoteProcessReturn;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.tool.MemoryMonitor;

import java.io.IOException;

/**
 * @author olivier
 * 
 */
public class TestRemoteProcess extends ODBTest {
	public void test1() throws IOException {
		String baseName = getBaseName();
		int port = 15000;
		NeoDatisConfig config = NeoDatis.getConfig().setBaseDirectory("unit-test-data");
		ODBServer server = NeoDatis.openServer(port, config);
		server.setAutomaticallyCreateDatabase(true);
		server.startServer(true);

		try {
			ODB odb = NeoDatis.openClient("localhost", port, baseName);

			RemoteProcessReturn r = odb.ext().executeRemoteProcess(new TimeRemoteProcess(true,false), true);
			DefaultProcessReturn rr = (DefaultProcessReturn) r;

			assertNotNull(rr.getValue("server-time"));
			odb.close();

		} finally {
			server.close();
		}

	}
	
	public void test1InMemory() throws IOException {
		String baseName = getBaseName();
		int port = 15000;
		NeoDatisConfig config = NeoDatis.getConfig().setBaseDirectory("unit-test-data").setStorageEngineClass(InMemoryStorageEngine.class);
		ODBServer server = NeoDatis.openServer(port, config);
		server.setAutomaticallyCreateDatabase(true);
		server.startServer(true);

		try {
			NeoDatisConfig configClient = NeoDatis.getConfig().setHostAndPort("localhost", port).setStorageEngineClass(InMemoryStorageEngine.class);
			ODB odb = NeoDatis.openClient(baseName, configClient);

			RemoteProcessReturn r = odb.ext().executeRemoteProcess(new TimeRemoteProcess(true,false), true);
			DefaultProcessReturn rr = (DefaultProcessReturn) r;

			assertNotNull(rr.getValue("server-time"));
			odb.close();

			odb = NeoDatis.openClient(baseName, configClient);

			r = odb.ext().executeRemoteProcess(new TimeRemoteProcess(true,false), true);
			rr = (DefaultProcessReturn) r;

			assertNotNull(rr.getValue("server-time"));
			odb.close();

		} finally {
			server.close();
		}

	}

	public void t1est1Delete() throws IOException {
		String baseName = getBaseName();
		int port = 15000;
		NeoDatisConfig config = NeoDatis.getConfig().setBaseDirectory("unit-test-data");
		ODBServer server = NeoDatis.openServer(port, config);
		server.setAutomaticallyCreateDatabase(true);
		server.startServer(true);

		try {
			Thread.sleep(2000 * 1000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			server.close();
		}

	}

	/** with sync=true */
	public void testMemoryLeakCallingRemoteProcess() throws IOException {
		String baseName = getBaseName();
		int port = 15000;
		NeoDatisConfig config = NeoDatis.getConfig().setBaseDirectory("unit-test-data");
		ODBServer server = NeoDatis.openServer(port, config);
		server.setAutomaticallyCreateDatabase(true);
		server.startServer(true);

		try {
			int n = 100;
			for (int i = 0; i < n; i++) {
				ODB odb = NeoDatis.openClient("localhost", port, baseName);

				RemoteProcessReturn r = odb.ext().executeRemoteProcess(new TimeRemoteProcess(false,false), true);
				DefaultProcessReturn rr = (DefaultProcessReturn) r;

				assertNotNull(rr.getValue("server-time"));
				odb.close();
				if (i % 10 == 0) {
					System.gc();
					MemoryMonitor.displayCurrentMemory("" + i, false);
				}
			}

		} finally {
			server.close();
		}

	}

	public void testMemoryLeakCallingRemoteProcessAsync() throws IOException {
		String baseName = getBaseName();
		int port = 15000;
		NeoDatisConfig config = NeoDatis.getConfig().setBaseDirectory("unit-test-data");
		ODBServer server = NeoDatis.openServer(port, config);
		server.setAutomaticallyCreateDatabase(true);
		server.startServer(true);

		try {
			int n = 100;
			for (int i = 0; i < n; i++) {
				ODB odb = NeoDatis.openClient("localhost", port, baseName);

				TimeRemoteProcess t = new TimeRemoteProcess(false,i%10==1);
				RemoteProcessReturn r = odb.ext().executeRemoteProcess(t, false);
				DefaultProcessReturn rr = (DefaultProcessReturn) r;

				//assertNotNull(rr.getValue("server-time"));
				odb.close();
				if (i % 10 == 0) {
					System.gc();
					System.out.println("\n\n");
					MemoryMonitor.displayCurrentMemory("" + i, false);
					System.out.println("\n\n");
				}
			}
		} finally {
			server.close();
		}

	}

}
