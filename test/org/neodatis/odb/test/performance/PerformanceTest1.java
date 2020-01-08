package org.neodatis.odb.test.performance;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.layers.layer2.instance.FullInstantiationHelper;
import org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo;
import org.neodatis.odb.core.layers.layer2.meta.ODBType;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.tool.wrappers.OdbTime;

import java.io.File;
import java.util.Date;

public class PerformanceTest1 extends ODBTest {
	public static int TEST_SIZE = 20000;
	public static final String ODB_FILE_NAME = "perf-v2";

	public void testEmpty() {
		// to avoid junit junit.framework.AssertionFailedError: No tests found
		// in ...
	}

	public void testInsertSimpleObjectODB(boolean force) throws Exception {
		if (!force && !runAll) {
			return;
		}
		// OdbConfiguration.setReconnectObjectsToSession(false);
		// Thread.sleep(20000);
		boolean doUpdate = true;
		boolean doDelete = true;
		boolean doSelect = true;

		//testConfig.setDebugLayers(true);

		// Configuration.setDatabaseCharacterEncoding(null);
		// LogUtil.logOn(FileSystemInterface.LOG_ID,true);
		// LogUtil.logOn(ObjectReader.LOG_ID,true);
		// Configuration.setUseLazyCache(true);
		boolean inMemory = true;
		// Configuration.monitorMemory(true);
		// Configuration.setUseModifiedClass(true);
		// Deletes the database file
		deleteBase(ODB_FILE_NAME);
		long t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0, t6 = 0, t7 = 0, t77 = 0, t8 = 0;
		ODB odb = null;
		Objects l = null;
		SimpleObject so = null;

		// Insert TEST_SIZE objects
		println("Inserting " + TEST_SIZE + " objects");

		t1 = OdbTime.getCurrentTimeInMs();
		odb = open(ODB_FILE_NAME);
		int i = 0;
		// odb.getClassRepresentation(SimpleObject.class).addFullInstantiationHelper(new
		// SimpleObjectFullInstantiationHelper());

		for (i = 0; i < TEST_SIZE; i++) {
			Object o = getSimpleObjectInstance(i);
			ObjectOid oid = odb.store(o);
			if (i % 100 == 0) {
				//println("i="+i + " - oid="+oid);
				// Monitor.displayCurrentMemory(""+i,true);
				// println("Cache="+Dummy.getEngine(odb).getSession().getCache().toString());
			}
		}
		t2 = OdbTime.getCurrentTimeInMs();
		// Closes the database
		odb.close();
		// if(true)return;
		t3 = OdbTime.getCurrentTimeInMs();

		println("Retrieving " + TEST_SIZE + " objects");
		// Reopen the database
		odb = open(ODB_FILE_NAME);

		if (doSelect) {
			// Gets the TEST_SIZE objects
			Query q = odb.query(SimpleObject.class);
			q.getQueryParameters().setInMemory(inMemory);
			l = odb.getObjects(q);
			t4 = OdbTime.getCurrentTimeInMs();

			/*
			 * // Actually get objects while (l.hasNext()) { Object o =
			 * l.next(); }
			 */
			i = 0;
			while (l.hasNext()) {
				Object o = l.next();
				if (i % 10000 == 0) {
					// Monitor.displayCurrentMemory("select "+i,true);
				}
				i++;
			}
		}
		t5 = OdbTime.getCurrentTimeInMs();

		if (doUpdate) {
			println("Updating " + TEST_SIZE + " objects");
			i = 0;
			so = null;
			l.reset();
			while (l.hasNext()) {
				so = (SimpleObject) l.next();
				so.setName(so.getName() + " updated");
				odb.store(so);
				if (i % 10000 == 0) {
					// Monitor.displayCurrentMemory(""+i);
				}
				i++;
			}
		}

		t6 = OdbTime.getCurrentTimeInMs();

		odb.close();
		t7 = OdbTime.getCurrentTimeInMs();

		if (doDelete) {

			println("Deleting " + TEST_SIZE + " objects");
			odb = open(ODB_FILE_NAME);

			Query q = odb.query(SimpleObject.class);
			q.getQueryParameters().setInMemory(inMemory);
			l = q.objects();
			t77 = OdbTime.getCurrentTimeInMs();
			// println("After getting objects - before delete");

			i = 0;

			while (l.hasNext()) {
				so = (SimpleObject) l.next();
				if (!so.getName().endsWith("updated")) {
					throw new RuntimeException("Update  not ok for " + so.getName());
				}
				odb.delete(so);
				//if (i % 10000 == 0) {
					
				//}
				i++;
			}
			odb.close();

		}
		t8 = OdbTime.getCurrentTimeInMs();
		System.out.println("File size=" +new File(DIRECTORY+"/perf.neodatis").length() );
		displayResult("ODB " + TEST_SIZE + " SimpleObject objects ", t1, t2, t3, t4, t5, t6, t7, t77, t8);
		
	}

	private SimpleObject getSimpleObjectInstance(int i) {
		SimpleObject so = new SimpleObject();
		so.setDate(new Date());
		so.setDuration(i);
		so.setName("Bonjour, comment allez vous?" + i);
		return so;
	}

	private void displayResult(String string, long t1, long t2, long t3, long t4, long t5, long t6, long t7, long t77, long t8) {

		String s1 = " total=" + (t8 - t1);
		String s2 = " total insert=" + (t3 - t1) + " -- " + "insert=" + (t2 - t1) + " commit=" + (t3 - t2) + " o/s=" + (float) TEST_SIZE
				/ (float) ((t3 - t1)) * 1000;
		String s3 = " total select=" + (t5 - t3) + " -- " + "select=" + (t4 - t3) + " get=" + (t5 - t4) + " o/s=" + (float) TEST_SIZE
				/ (float) ((t5 - t3)) * 1000;
		String s4 = " total update=" + (t7 - t5) + " -- " + "update=" + (t6 - t5) + " commit=" + (t7 - t6) + " o/s=" + (float) TEST_SIZE
				/ (float) ((t7 - t5)) * 1000;
		String s5 = " total delete=" + (t8 - t7) + " -- " + "select=" + (t77 - t7) + " - delete=" + (t8 - t77) + " o/s="
				+ (float) TEST_SIZE / (float) ((t8 - t7)) * 1000;

		println(string + s1 + " | " + s2 + " | " + s3 + " | " + s4 + " | " + s5);

		long tinsert = t3 - t1;
		long tselect = t5 - t3;
		long tupdate = t7 - t5;
		long tdelete = t8 - t7;

		println("Nb  =" + ODBType.nb);
		assertTrue("Bad Performance on insert", tinsert < 1050);
		assertTrue("Bad Performance on select", tselect < 535);
		assertTrue("Bad Performance on update", tupdate < 582);
		assertTrue("Bad Performance on delete", tdelete < 740);

	}

	public static void main(String[] args) throws Exception {
        System.out.println("NeoDatisV2");
		// Thread.sleep(15000);
		// OdbConfiguration.setMessageStreamerClass(HessianMessageStreamer.class);
		PerformanceTest1 pt = new PerformanceTest1();
		pt.testInsertSimpleObjectODB(true);
	}

}

class SimpleObjectFullInstantiationHelper implements FullInstantiationHelper {

	public Object instantiate(NonNativeObjectInfo nnoi) {
		SimpleObject so = new SimpleObject();
		/*
		 * so.setDate((Date) nnoi.getValueOf("date")); so.setDuration(((Integer)
		 * nnoi.getValueOf("duration")).intValue()); so.setName((String)
		 * nnoi.getValueOf("name"));
		 */
		return so;
	}

}
