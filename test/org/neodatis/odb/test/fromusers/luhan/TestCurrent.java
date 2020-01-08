/**
 * 
 */
package org.neodatis.odb.test.fromusers.luhan;

import org.neodatis.odb.*;
import org.neodatis.tool.IOUtil;

/**
 * @author olivier
 * 
 */
public class TestCurrent {
	public static final String ODB_NAME = "testCurrent.odb";
	private static ODBServer server = null;

	public static void connectServer(NeoDatisConfig config) {
		IOUtil.deleteFile(ODB_NAME);
		server = NeoDatis.openServer(8989,config);
		server.addBase("base1", ODB_NAME);
		server.startServer(true);
	}

	public static void closeServer() {
		if (server != null) {
			server.close();
		}
	}

	public static void main(String[] args) throws Exception {
		NeoDatisConfig config = NeoDatis.getConfig();
		config.lockObjectsOnSelect(true);
		//LogUtil.enable(SessionManager.LOG_ID);
		//LogUtil.enable(SessionImpl.LOG_ID);
		//MutexFactory.setDebug(true);
		//OdbConfiguration.setDebugEnabled(true);
		connectServer(config);
		ClientThread2 c1 = new ClientThread2(1);
		ClientThread2 c2 = new ClientThread2(5);
		c1.start();
		c2.start();

		c1.join();
		c2.join();
		System.out.println("end");
		
		ODB odb = NeoDatis.openClient("localhost", 8989, "base1");
		Objects<Account> objects = odb.query(Account.class).objects();
		System.out.println("Final value = "+  objects.first().getCurrentDeposit());

		closeServer();
	}
}