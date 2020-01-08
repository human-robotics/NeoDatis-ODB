package org.neodatis.odb.test.index;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.tool.MemoryMonitor;
import org.neodatis.tool.wrappers.OdbTime;

import java.util.Date;

public class TestCreateObjectAfterInsert extends ODBTest {

	public void testLong() {
		println("" + Long.MAX_VALUE);
		long l = Long.MAX_VALUE - 1;
		l = l + 1;
		println("" + l);
		println("" + l + 1);

	}

	/**
	 * Test the creation of an index after having created objects. In this case
	 * ODB should creates the index and update it with already existing objects
	 * 
	 * @throws Exception
	 */
	public void test1Object() throws Exception {
		ODB odb = null;

		try {
			odb = open(getBaseName());

			IndexedObject io = new IndexedObject("name", 5, new Date());
			odb.store(io);
			odb.close();

			odb = open(getBaseName());
			String[] names = { "name" };
			odb.getClassRepresentation(IndexedObject.class).addUniqueIndexOn("index1", names, true);
			Objects objects = odb.query(IndexedObject.class, W.equal("name", "name")).objects();
			assertEquals(1, objects.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	/**
	 * Test the creation of an index after having created objects. In this case
	 * ODB should creates the index and update it with already existing objects
	 * 
	 * @throws Exception
	 */
	public void test2000Objects() throws Exception {
		long start = OdbTime.getCurrentTimeInMs();
		ODB odb = null;
		int size = isLocal ? 2000 : 200;
		try {
			odb = open(getBaseName());

			for (int i = 0; i < size; i++) {
				IndexedObject io = new IndexedObject("name" + i, i, new Date());
				odb.store(io);
			}
			odb.close();

			odb = open(getBaseName());
			String[] names = { "name" };
			odb.getClassRepresentation(IndexedObject.class).addUniqueIndexOn("index1", names, true);
			Objects objects = odb.query(IndexedObject.class, W.equal("name", "name0")).objects();
			assertEquals(1, objects.size());

			objects = odb.query(IndexedObject.class).objects();
			MemoryMonitor.displayCurrentMemory("BTREE", true);
			assertEquals(size, objects.size());
		} finally {
			if (odb != null) {
				odb.close();
			}
			long end = OdbTime.getCurrentTimeInMs();
			println((end - start) + "ms");
		}
	}

	/**
	 * Test the creation of an index after having created objects. In this case
	 * ODB should creates the index and update it with already existing objects
	 * 
	 * @throws Exception
	 */
	public void test1000Objects() throws Exception {
		ODB odb = null;
		int size = isLocal ? 1000 : 100;
		long start = OdbTime.getCurrentTimeInMs();
		NeoDatisConfig config = NeoDatis.getConfig().setMonitorMemory(true).setReconnectObjectsToSession(false);
		try {
			odb = open(getBaseName(),config);

			for (int i = 0; i < size; i++) {
				IndexedObject io = new IndexedObject("name" + i, i, new Date());
				odb.store(io);
				if (i % 10000 == 0) {
					MemoryMonitor.displayCurrentMemory(i + " objects created", false);
				}
			}
			odb.close();
			println("\n\n END OF INSERT \n\n");
			odb = open(getBaseName(),config);
			String[] names = { "name" };
			odb.getClassRepresentation(IndexedObject.class).addUniqueIndexOn("index1", names, true);
			println("\n\n after create index\n\n");
			Objects objects = odb.query(IndexedObject.class, W.equal("name", "name0")).objects();
			println("\n\nafter get Objects\n\n");
			assertEquals(1, objects.size());

			objects = odb.query(IndexedObject.class, W.equal("duration", 9)).objects();
			assertEquals(1, objects.size());

			objects = odb.query(IndexedObject.class).objects();
			assertEquals(size, objects.size());
		} catch (Exception e) {
			throw e;
		} finally {

			/*
			 * if(odb!=null){ odb.close(); }
			 */
			long end = OdbTime.getCurrentTimeInMs();
			println((end - start) + "ms");
			odb.close();
		}
	}

	/**
	 * Test the creation of an index after having created objects. In this case
	 * ODB should creates the index and update it with already existing objects
	 * 
	 * @throws Exception
	 */
	public void test900ObjectsIntiNdex() throws Exception {

		ODB odb = null;
		int size = isLocal ? 900 : 90;
		long start = OdbTime.getCurrentTimeInMs();
		NeoDatisConfig config = NeoDatis.getConfig().setMonitorMemory(true);
		try {

			odb = open(getBaseName(),config);

			for (int i = 0; i < size; i++) {
				IndexedObject io = new IndexedObject("name" + i, i, new Date());
				odb.store(io);
				if (i % 10000 == 0) {
					MemoryMonitor.displayCurrentMemory(i + " objects created", false);
				}
			}
			odb.close();
			println("\n\n END OF INSERT \n\n");
			odb = open(getBaseName());
			String[] names = { "duration" };
			odb.getClassRepresentation(IndexedObject.class).addUniqueIndexOn("index1", names, true);
			println("\n\n after create index\n\n");
			Objects objects = odb.query(IndexedObject.class, W.equal("name", "name0")).objects();
			println("\n\nafter get Objects\n\n");
			assertEquals(1, objects.size());

			objects = odb.query(IndexedObject.class, W.equal("duration", 100)).objects();
			assertEquals(1, objects.size());

			objects = odb.query(IndexedObject.class).objects();
			assertEquals(size, objects.size());
		} catch (Exception e) {
			throw e;
		} finally {

			/*
			 * if(odb!=null){ odb.close(); }
			 */
			long end = OdbTime.getCurrentTimeInMs();
			println((end - start) + "ms");
		}
	}
}
