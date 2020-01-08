package org.neodatis.odb.test.ext;

import org.neodatis.odb.*;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.tool.IOUtil;

import java.util.HashMap;
import java.util.Map;

public class TestExt extends ODBTest {
	public void testTransactionId() throws Exception {
		String baseName = getBaseName();
		String fullName = baseName;
		ODB odb = open(baseName);

		TransactionId transactionId = odb.ext().getCurrentTransactionId();

		println(fullName + " "+  DIRECTORY+transactionId);

		if (isLocal) {
			assertTrue((transactionId.toString()).startsWith(fullName));
		} else {
			// In Client Server, there is a first transaction created
			// automaticaly by the server, so the first user transaction is 2
			assertTrue(transactionId.toString().startsWith("tid=02"));
		}
		odb.close();

		odb = open(baseName);
		transactionId = odb.ext().getCurrentTransactionId();

		println(transactionId);
		if (isLocal) {
			assertTrue((transactionId.toString()).startsWith(fullName));
		} else {
			assertTrue(transactionId.toString().startsWith("tid=03"));
		}
		odb.close();
	}

	public void testTransactionId2() throws Exception {
		ODB odb = null;
		TransactionId transactionId = null;
		int size = isLocal ? 100 : 10;
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < size; i++) {
			odb = open(getBaseName());
			transactionId = odb.ext().getCurrentTransactionId();
			
			boolean alreadyExist = map.containsKey(transactionId.toString());
			
			assertFalse(alreadyExist);
			map.put(transactionId.toString(), transactionId.toString());
			
			odb.close();

		}
	}

	public void testGetObjectId() throws Exception {
		ODB odb = null;

		odb = open(getBaseName());
		Function f = new Function("Test Function");
		ObjectOid oid = odb.store(f);
		ExternalObjectOid extOid = odb.ext().getObjectExternalOID(f);
		
		assertTrue(extOid.oidToString().endsWith(oid.oidToString()));
		assertEquals(odb.ext().getDatabaseId(), extOid.getDatabaseId());

		odb.close();

		odb = open(getBaseName());
		// Getting object via external oid
		Function f2 = (Function) odb.getObjectFromId(extOid);
		ObjectOid lastOid = odb.getObjectId(f2);
		assertEquals(oid, lastOid);
		assertEquals(f.getName(), f2.getName());
		odb.close();

	}

	public void testObjectVersion() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		int size = 100;
		long updateDate = 0;
		long creationDate = 0;
		ObjectOid oid = odb.store(new Function("f"));
		odb.close();
		// LogUtil.allOn(true);
		for (int i = 0; i < size; i++) {
			odb = open(baseName);
			Function f = (Function) odb.getObjectFromId(oid);
			long version = odb.ext().getObjectVersion(oid,true);
			// System.out.println("i="+i + " - v="+ version+ " - oid="+oid);
			updateDate = odb.ext().getObjectUpdateDate(oid,true);
			creationDate = odb.ext().getObjectCreationDate(oid);
			f.setName(f.getName() + "-" + i);
			// update the object, should increase the version number
			odb.store(f);
			odb.close();
			assertEquals(i + 1, version);
			//System.out.println(creationDate + " - "+ updateDate+ "- "+ OdbTime.getCurrentTimeInMs());
			// in first iteration, creation & update date may be equal
			if (i > 0) {
				assertTrue(creationDate < updateDate);
			}
			//println(version);
		}
	}

	public void testObjectVersionWithoutClose() throws Exception {
		ODB odb = open(getBaseName());
		int size = 1000;
		long updateDate = 0;
		long creationDate = 0;
		ObjectOid oid = odb.store(new Function("f"));
		odb.close();
		odb = open(getBaseName());
		Thread.sleep(100);
		for (int i = 0; i < size; i++) {
			// odb = open("ext");
			Function f = (Function) odb.getObjectFromId(oid);
			long version = odb.ext().getObjectVersion(oid,true);
			// System.out.println("i="+i + " - v="+ version+ " - oid="+oid);
			assertEquals(i + 1, version);

			f.setName("f" + i);
			// update the object, should increase the version number
			odb.store(f);
			odb.commit();
			println(version);
		}
		odb.close();
	}

	public void testObjectVersionWithoutClose2() throws Exception {
		ODB odb = open(getBaseName());
		int size = 1000;
		long updateDate = 0;
		long creationDate = 0;
		// LogUtil.allOn(true);
		ObjectOid oid = odb.store(new Function("f"));
		odb.close();
		odb = open(getBaseName());
		Thread.sleep(100);
		// LogUtil.allOn(true);
		for (int i = 0; i < size; i++) {
			// odb = open("ext");
			Function f = (Function) odb.getObjectFromId(oid);
			f.setName("f" + i);
			odb.store(f);
			odb.commit();
		}
		odb.close();
	}

	public void testConcurrentObjectVersion() throws Exception {
		if(!super.testNewFeature){
			return;
		}
		int port = PORT + 8;
		ODBServer server = NeoDatis.openServer(port);
		server.startServer(true);

		ODB odb = NeoDatis.openClient("localhost", port, DIRECTORY + "exta1");
		ObjectOid oid = odb.store(new Function("f1"));
		long version = odb.ext().getObjectVersion(oid,true);
		println(version);
		odb.close();

		ODB odb1 = NeoDatis.openClient("localhost", port, DIRECTORY + "exta1");
		ODB odb2 = NeoDatis.openClient("localhost", port, DIRECTORY + "exta1");

		long v1 = odb1.ext().getObjectVersion(oid,true);
		long v2 = odb2.ext().getObjectVersion(oid,true);

		assertEquals(1, v1);
		assertEquals(1, v2);

		println("v1=" + v1 + "- v2=" + v2);

		Function f1 = (Function) odb1.getObjectFromId(oid);
		Function f2 = (Function) odb2.getObjectFromId(oid);

		f1.setName("function 1");
		odb1.store(f1);
		v1 = odb1.ext().getObjectVersion(oid,true);
		println("after update odb1 , v1=" + v1);
		odb1.close();

		ODB odb3 = NeoDatis.openClient("localhost", port, DIRECTORY + "exta1");
		// Check committed object value
		Function f3 = (Function) odb3.getObjectFromId(oid);
		assertEquals(f1.getName(), f3.getName());
		assertEquals(2, odb3.ext().getObjectVersion(oid,true));
		odb3.close();
		assertEquals(2, v1);

		f2.setName("function 2");
		odb2.store(f2);
		v2 = odb2.ext().getObjectVersion(oid,true);
		println("after update odb2 , v2=" + v2);
		odb2.close();

		assertEquals(3, v2);

		ODB odb4 = NeoDatis.openClient("localhost", port, DIRECTORY + "exta1");
		// Check committed object value
		Function f4 = (Function) odb4.getObjectFromId(oid);
		assertEquals(f2.getName(), f4.getName());
		assertEquals(3, odb4.ext().getObjectVersion(oid,true));
		odb4.close();

		server.close();
	}

	public void testConcurrentObjectVersion2() throws Exception {
		if(!super.testNewFeature){
			return;
		}

		int port = PORT + 8;
		IOUtil.deleteFile(DIRECTORY + "exta1");
		ODBServer server = NeoDatis.openServer(port);
		server.startServer(true);

		ODB odb = NeoDatis.openClient("localhost", port, DIRECTORY + "exta1");
		ObjectOid oid = odb.store(new Function("f1"));
		long version = odb.ext().getObjectVersion(oid,true);
		println(version);
		odb.close();

		int nbThreads = 100;
		ODB[] odbs = new ODB[nbThreads];
		long[] versions = new long[nbThreads];
		Function[] functions = new Function[nbThreads];

		// Open all Odbs and get the object
		for (int i = 0; i < nbThreads; i++) {
			odbs[i] = NeoDatis.openClient("localhost", port, DIRECTORY + "exta1");
			versions[i] = odbs[i].ext().getObjectVersion(oid,true);
			functions[i] = (Function) odbs[i].getObjectFromId(oid);

			assertEquals(1, versions[i]);
			assertEquals("f1", functions[i].getName());
		}

		// Open all Odbs and get the object
		for (int i = 0; i < nbThreads; i++) {
			functions[i].setName("function " + i);
			odbs[i].store(functions[i]);
			versions[i] = odbs[i].ext().getObjectVersion(oid,true);
			println("Function with name " + functions[i].getName() + " has version " + versions[i]);
			odbs[i].close();
			assertEquals(i + 2, versions[i]);
			// Just to check the version number after commit
			odb = NeoDatis.openClient("localhost", port, DIRECTORY + "exta1");
			long committedVersionNumber = odb.ext().getObjectVersion(oid,true);
			println("After commit = " + committedVersionNumber);
			assertEquals(i + 2, committedVersionNumber);
			odb.close();
		}

		ODB odb4 = NeoDatis.openClient("localhost", port, DIRECTORY + "exta1");
		// Check committed object value
		Function f4 = (Function) odb4.getObjectFromId(oid);
		assertEquals("function " + (nbThreads - 1), f4.getName());
		assertEquals(nbThreads + 1, odb4.ext().getObjectVersion(oid,true));
		odb4.close();

		server.close();
	}

	public void testConcurrentObjectVersion3() throws Exception {
		if (isLocal|| (useSameVmOptimization&&!testNewFeature)) {
			return;
		}
		ODB odb = open(getBaseName());
		ObjectOid oid = odb.store(new Function("f1"));
		long version = odb.ext().getObjectVersion(oid,true);
		println(version);
		odb.close();

		int nbThreads = 100;
		ODB[] odbs = new ODB[nbThreads];
		long[] versions = new long[nbThreads];
		Function[] functions = new Function[nbThreads];

		// Open all Odbs and get the object
		for (int i = 0; i < nbThreads; i++) {
			odbs[i] = open("exta1");
			versions[i] = odbs[i].ext().getObjectVersion(oid,true);
			functions[i] = (Function) odbs[i].getObjectFromId(oid);

			assertEquals(1, versions[i]);
			assertEquals("f1", functions[i].getName());
		}

		// Open all Odbs and get the object
		for (int i = 0; i < nbThreads; i++) {
			functions[i].setName("function " + i);
			odbs[i].store(functions[i]);
			versions[i] = odbs[i].ext().getObjectVersion(oid,true);
			println("Function with name " + functions[i].getName() + " has version " + versions[i]);
			odbs[i].close();
			assertEquals(i + 2, versions[i]);
			// Just to check the version number after commit
			odb = open("exta1");
			long committedVersionNumber = odb.ext().getObjectVersion(oid,true);
			println("After commit = " + committedVersionNumber);
			assertEquals(i + 2, committedVersionNumber);
			odb.close();
		}

		ODB odb4 = open(getBaseName());
		// Check committed object value
		Function f4 = (Function) odb4.getObjectFromId(oid);
		assertEquals("function " + (nbThreads - 1), f4.getName());
		assertEquals(nbThreads + 1, odb4.ext().getObjectVersion(oid,true));
		odb4.close();
	}
	
	public void testConcurrentObjectVersion4() throws Exception {
		if(!super.testNewFeature){
			return;
		}

		String baseName = getBaseName();
		
		ODBServer server = null;
		
		
		try{
			server = NeoDatis.openServer(PORT+8);
			
			ODB odb = server.openClient(baseName);
			ObjectOid oid = odb.store(new Function("f1"));
			odb.close();
			
			LongThread t = new LongThread(baseName,server,oid);
			t.start();

			odb = server.openClient(baseName);
			Function f = (Function) odb.getObjectFromId(oid);
			f.setName("Updated 1");
			odb.store(f);
			odb.close();
			
			Thread.sleep(2000);
			assertEquals(true,t.objectVersionIsOk());

		}finally{
			if(server!=null){
				server.close();
			}
		}
		
		
		
		
		
	}

}
