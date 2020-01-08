/*
 NeoDatis ODB : Native Object Database (odb.info@neodatis.org)
 Copyright (C) 2007 NeoDatis Inc. http://www.neodatis.org

 "This file is part of the NeoDatis ODB open source object database".

 NeoDatis ODB is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 NeoDatis ODB is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.neodatis.odb.test.index;

import org.neodatis.odb.*;
import org.neodatis.odb.core.btree.LazyODBBTreePersister;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfoIndex;
import org.neodatis.odb.core.layers.layer2.meta.MetaModel;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.tool.wrappers.OdbTime;

import java.math.BigInteger;
import java.util.Date;

public class TestIndex extends ODBTest {
	public void testSaveIndex() throws Exception {
		String baseName = getBaseName();
		
		ODB odb = open(baseName);
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name", "duration" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		String[] indexFields2 = { "name", "creation" };
		clazz.addUniqueIndexOn("index2", indexFields2, true);

		String[] indexFields3 = { "duration", "creation" };
		clazz.addUniqueIndexOn("index3", indexFields3, true);
		odb.close();

		odb = open(baseName);

		Session session = Dummy.getEngine(odb).getSession();
		MetaModel metaModel = session.getMetaModel();
		ClassInfo ci = metaModel.getClassInfo(IndexedObject.class.getName(), true);
		assertEquals(3, ci.getNumberOfIndexes());

		assertEquals(ci.getIndex(0).getName(), "index1");
		assertEquals(1, ci.getIndex(0).getAttributeIds()[0]);
		assertEquals(ClassInfoIndex.ENABLED, ci.getIndex(0).getStatus());

		assertEquals(ci.getIndex(1).getName(), "index2");
		assertEquals(1, ci.getIndex(1).getAttributeIds()[0]);
		assertEquals(ClassInfoIndex.ENABLED, ci.getIndex(1).getStatus());

		assertEquals(ci.getIndex(2).getName(), "index3");
		assertEquals(2, ci.getIndex(2).getAttributeIds()[0]);
		assertEquals(ClassInfoIndex.ENABLED, ci.getIndex(0).getStatus());
		odb.close();
		
	}

	public void testInsertWithIndex() throws Exception {
		String baseName = getBaseName();
		
		ODB odb = open(baseName);

		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name", "duration" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		/*
		 * String[] indexFields2 = { "name", "creation" };
		 * clazz.addUniqueIndexOn("index2", indexFields2);
		 * 
		 * String[] indexFields3 = { "duration", "creation" };
		 * clazz.addUniqueIndexOn("index3", indexFields3);
		 */
		odb.close();

		odb = open(baseName);

		IndexedObject io1 = new IndexedObject("olivier", 15, new Date());
		odb.store(io1);
		odb.close();

		odb = open(baseName);
		Query q = odb.query(IndexedObject.class, W.isNotNull("name"));
		Objects objects = odb.getObjects(q, true);
		odb.close();
		assertEquals(false, q.getExecutionPlan().useIndex());

		assertEquals(1, objects.size());
		IndexedObject io2 = (IndexedObject) objects.first();
		assertEquals("olivier", io2.getName());
		assertEquals(15, io2.getDuration());
		assertFalse(q.getExecutionPlan().getDetails().indexOf("index1") != -1);

		// 
	}

	public void testIndexWithOneFieldAndQueryWithTwoFields() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		odb.close();

		odb = open(baseName);

		IndexedObject io1 = new IndexedObject("olivier", 15, new Date());
		odb.store(io1);
		odb.close();

		odb = open(baseName);
		Query q = odb.query(IndexedObject.class, W.and().add(W.equal("name", "olivier")).add(W.equal("duration", 15)));
		Objects objects = odb.getObjects(q, true);
		odb.close();
		println(q.getExecutionPlan().toString());
		assertEquals(false, q.getExecutionPlan().useIndex());
		assertEquals(1, objects.size());
	}

	public void testInsertWithIndex1() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		/*
		 * String[] indexFields2 = { "name", "creation" };
		 * clazz.addUniqueIndexOn("index2", indexFields2);
		 * 
		 * String[] indexFields3 = { "duration", "creation" };
		 * clazz.addUniqueIndexOn("index3", indexFields3);
		 */
		odb.close();

		odb = open(baseName);

		int size = 500;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + i, new Date());
			odb.store(io1);
			if (i % 1000 == 0)
				println(i);
		}
		long tt0 = OdbTime.getCurrentTimeInMs();
		odb.close();
		long tt1 = OdbTime.getCurrentTimeInMs();
		long end0 = OdbTime.getCurrentTimeInMs();
		odb = open(baseName);

		long totalTime = 0;
		long maxTime = 0;
		long minTime = 100000;
		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + (i + 1)));
			long start = OdbTime.getCurrentTimeInMs();
			Objects objects = odb.getObjects(q, true);
			long end = OdbTime.getCurrentTimeInMs();
			assertEquals(1, objects.size());
			IndexedObject io2 = (IndexedObject) objects.first();
			assertEquals("olivier" + (i + 1), io2.getName());
			assertEquals(15 + i, io2.getDuration());
			long d = end - start;
			totalTime += d;

			if (d > maxTime) {
				maxTime = d;
			}
			if (d < minTime) {
				minTime = d;
			}

		}
		odb.close();

		println("total duration=" + totalTime + " / " + (double) totalTime / size);
		println("duration max=" + maxTime + " / min=" + minTime);
		if (testPerformance&& totalTime / size > 2) {
			fail("Total/size is > than 2 : " + totalTime);
		}
		if (testPerformance) {
			// TODO Try to get maxTime < 10!
			assertTrue(maxTime < 100);
			assertTrue(minTime < 1);
		}
	}

	public void testInsertWithIndex2() throws Exception {
		String baseName = getBaseName();
		if (!runAll) {
			return;
		}
		ODB odb = open(baseName);

		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		int size = 10000;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + i, new Date());
			odb.store(io1);
			if (i % 1000 == 0) {
				println(i);
			}
		}
		long tt0 = OdbTime.getCurrentTimeInMs();
		odb.close();
		long tt1 = OdbTime.getCurrentTimeInMs();
		long end0 = OdbTime.getCurrentTimeInMs();
		odb = open(baseName);

		long totalTime = 0;
		long maxTime = 0;
		long minTime = 100000;
		long t0 = OdbTime.getCurrentTimeInMs();
		long t1 = 0;
		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + (i + 1)));
			long start = OdbTime.getCurrentTimeInMs();
			Objects objects = odb.getObjects(q, true);
			long end = OdbTime.getCurrentTimeInMs();
			assertEquals(1, objects.size());
			IndexedObject io2 = (IndexedObject) objects.first();
			assertEquals("olivier" + (i + 1), io2.getName());
			assertEquals(15 + i, io2.getDuration());
			long d = end - start;
			totalTime += d;

			if (d > maxTime) {
				maxTime = d;
			}
			if (d < minTime) {
				minTime = d;
			}
			if (i % 1000 == 0) {
				t1 = OdbTime.getCurrentTimeInMs();
				println("i=" + i + " - time=" + (t1 - t0));
				t0 = t1;
				// /println(LazyODBBTreePersister.counters());
			}
		}
		odb.close();

		// println("total duration=" + totalTime + " / " + (double) totalTime /
		// size);
		// println("duration max=" + maxTime + " / min=" + minTime);
		if (totalTime / size > 1) {
			fail("Total/size is > than 1 : " + (float) ((float) totalTime / (float) size));
		}

		println("Max time=" + maxTime);
		println("Min time=" + minTime);
		// TODO Try to get maxTime < 10!
		assertTrue(maxTime < 250);
		assertTrue(minTime < 1);
	}

	/** Test with on e key index */
	public void testInsertWithIndex3() throws Exception {
		String baseName = getBaseName();
		// LogUtil.logOn(LazyODBBTreePersister.LOG_ID, true);
		ODB odb = open(baseName);
		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		/*
		 * String[] indexFields2 = { "name", "creation" };
		 * clazz.addUniqueIndexOn("index2", indexFields2);
		 * 
		 * String[] indexFields3 = { "duration", "creation" };
		 * clazz.addUniqueIndexOn("index3", indexFields3);
		 */
		odb.close();

		odb = open(baseName);

		int size = isLocal ? 130 : 30;
		int commitInterval = 10;
		long start0 = OdbTime.getCurrentTimeInMs();
		SessionEngine engine = Dummy.getEngine(odb);
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + size, new Date());
			odb.store(io1);
			if (i % commitInterval == 0) {
				odb.commit();
				odb.close();
				odb = open(baseName);
				engine = Dummy.getEngine(odb);
			}
			if (io1.getName().equals("olivier" + size)) {
				println("Ola chico");
			}
		}

		engine = Dummy.getEngine(odb);
		// println(new
		// BTreeDisplay().build(engine.getSession(true).getMetaModel().getClassInfo(IndexedObject.class.getName(),
		// true).getIndex(0).getBTree(), true));

		odb.close();
		long end0 = OdbTime.getCurrentTimeInMs();
		// println("IPU=" + ObjectWriter.getNbInPlaceUpdates() + " - NU=" +
		// ObjectWriter.getNbNormalUpdates());
		// println("inserting time with index=" + (end0 - start0));

		odb = open(baseName);

		engine = Dummy.getEngine(odb);

		// println("After load = unconnected : "+
		// engine.getSession(true).getMetaModel().getClassInfo(IndexedObject.class.getName(),
		// true).getUncommittedZoneInfo());
		// println("After Load = connected : "+
		// engine.getSession(true).getMetaModel().getClassInfo(IndexedObject.class.getName(),
		// true).getCommitedZoneInfo());
		// println(new
		// BTreeDisplay().build(engine.getSession(true).getMetaModel().getClassInfo(IndexedObject.class.getName(),
		// true).getIndex(0).getBTree(), true));

		Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + size));
		long start = OdbTime.getCurrentTimeInMs();
		Objects objects = odb.getObjects(q, false);
		long end = OdbTime.getCurrentTimeInMs();
		/*
		 * engine = Dummy.getEngine(odb); ClassInfo ci =
		 * engine.getSession(true)
		 * .getMetaModel().getClassInfo(IndexedObject.class.getName(), true);
		 * long sizebtree = ci.getIndex(0).getBTree().getSize();
		 * println("Size btree="+sizebtree); //println(new
		 * BTreeDisplay().build(ci.getIndex(0).getBTree(), false));
		 */

		try {
			assertEquals(1, objects.size());
			IndexedObject io2 = (IndexedObject) objects.first();
			assertEquals("olivier" + size, io2.getName());
			assertEquals(15 + size, io2.getDuration());
			long duration = end - start;

			println("duration=" + duration);

			if (testPerformance) {
				if (isLocal) {
					if (duration > 2) {
						fail("Time of search in index is greater than 2ms : " + duration);
					}
				} else {
					if (duration > 32) {
						fail("Time of search in index is greater than 2ms : " + duration);
					}
				}
			}

		} finally {
			odb.close();
		}

	}

	public void testInsertWithIndex3With2Parts() throws Exception {
		insertWithIndex3Part1();
		insertWithIndex3Part2();
	}
	public void insertWithIndex3Part1() throws Exception {
		String baseName = getBaseName();
		// LogUtil.logOn(LazyODBBTreePersister.LOG_ID, true);
		ODB odb = open(baseName);
		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		/*
		 * String[] indexFields2 = { "name", "creation" };
		 * clazz.addUniqueIndexOn("index2", indexFields2);
		 * 
		 * String[] indexFields3 = { "duration", "creation" };
		 * clazz.addUniqueIndexOn("index3", indexFields3);
		 */
		odb.close();

		odb = open(baseName);

		int size = 130;
		int commitInterval = 10;
		long start0 = OdbTime.getCurrentTimeInMs();
		SessionEngine engine = Dummy.getEngine(odb);
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + size, new Date());
			odb.store(io1);
			if (i % commitInterval == 0) {
				odb.commit();
				odb.close();
				odb = open(baseName);
				engine = Dummy.getEngine(odb);
			}
			if (io1.getName().equals("olivier" + size)) {
				println("Ola chico");
			}
		}

		engine = Dummy.getEngine(odb);
		// println(new
		// BTreeDisplay().build(engine.getSession(true).getMetaModel().getClassInfo(IndexedObject.class.getName(),
		// true).getIndex(0).getBTree(), true));

		odb.close();
		long end0 = OdbTime.getCurrentTimeInMs();

	}

	public void insertWithIndex3Part2() throws Exception {
		String baseName = getBaseName();
		int size = 1300;
		ODB odb = open(baseName);

		Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + size));
		long start = OdbTime.getCurrentTimeInMs();
		Objects objects = odb.getObjects(q, false);
		long end = OdbTime.getCurrentTimeInMs();
		/*
		 * engine = Dummy.getEngine(odb); ClassInfo ci =
		 * engine.getSession(true)
		 * .getMetaModel().getClassInfo(IndexedObject.class.getName(), true);
		 * long sizebtree = ci.getIndex(0).getBTree().getSize();
		 * println("Size btree="+sizebtree); //println(new
		 * BTreeDisplay().build(ci.getIndex(0).getBTree(), false));
		 */

		try {
			assertEquals(1, objects.size());
			IndexedObject io2 = (IndexedObject) objects.first();
			assertEquals("olivier" + size, io2.getName());
			assertEquals(15 + size, io2.getDuration());
			long duration = end - start;

			println("duration=" + duration);

			if (testPerformance) {
				if (isLocal) {
					if (duration > 2) {
						fail("Time of search in index is greater than 2ms : " + duration);
					}
				} else {
					if (duration > 32) {
						fail("Time of search in index is greater than 2ms : " + duration);
					}
				}
			}

		} finally {
			odb.close();
		}

	}

	/** Test with one key index */
	public void testInsertWithIntIndex3CheckAll() throws Exception {
		
		//LogUtil.enable(LazyODBBTreePersister.LOG_ID);
		
		String baseName = getBaseName();
		ODB odb = open(baseName);
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "duration" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		/*
		 * String[] indexFields2 = { "name", "creation" };
		 * clazz.addUniqueIndexOn("index2", indexFields2);
		 * 
		 * String[] indexFields3 = { "duration", "creation" };
		 * clazz.addUniqueIndexOn("index3", indexFields3);
		 */
		odb.close();

		odb = open(baseName);

		int size = 500;
		int commitInterval = 10;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), i, new Date());
			odb.store(io1);
			if (i % commitInterval == 0) {
				//odb.commit();
				//println(i+" : commit / " + size);
			}
		}
		odb.close();
		long end0 = OdbTime.getCurrentTimeInMs();
		// println("IPU=" + ObjectWriter.getNbInPlaceUpdates() + " - NU=" +
		// ObjectWriter.getNbNormalUpdates());
		println("inserting time with index=" + (end0 - start0));

		odb = open(baseName);
		/*
		 * IStorageEngine engine = Dummy.getEngine(odb); ClassInfo ci =
		 * engine.getSession
		 * (true).getMetaModel().getClassInfo(IndexedObject.class.getName(),
		 * true); long sizebtree = ci.getIndex(0).getBTree().getSize();
		 * println("Size btree="+sizebtree); //println(new
		 * BTreeDisplay().build(ci.getIndex(0).getBTree(), false));
		 */
		
		Query q = odb.query(IndexedObject.class);
		Objects objects = odb.getObjects(q, false);
		assertEquals(size, objects.size());
		long start = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			q = odb.query(IndexedObject.class, W.equal("duration", i));
			objects = odb.getObjects(q, false);
			//println("olivier" + (i+1));
			assertEquals(1, objects.size());
		}
		long end = OdbTime.getCurrentTimeInMs();

		try {
			float duration = (float) (end - start) / (float) size;
			println("mean duration for search is " + duration);
			if (testPerformance&& duration > 2) {
				fail("Time of search in index is greater than 2ms : " + duration);
			}

		} finally {
			odb.close();
		}

	}

	/** Test with one key index */
	public void testInsertWithDateIndex3CheckAll() throws Exception {
		String baseName = getBaseName();
		// LogUtil.logOn(LazyODBBTreePersister.LOG_ID, true);
		ODB odb = open(baseName);
		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "creation" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		/*
		 * String[] indexFields2 = { "name", "creation" };
		 * clazz.addUniqueIndexOn("index2", indexFields2);
		 * 
		 * String[] indexFields3 = { "duration", "creation" };
		 * clazz.addUniqueIndexOn("index3", indexFields3);
		 */
		odb.close();

		odb = open(baseName);

		int size = 1300;
		int commitInterval = 1000;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), i, new Date(start0 + i));
			odb.store(io1);
			if (i % commitInterval == 0) {
				odb.commit();
				// println(i+" : commit / " + size);
			}
		}
		odb.close();
		long end0 = OdbTime.getCurrentTimeInMs();
		// println("IPU=" + ObjectWriter.getNbInPlaceUpdates() + " - NU=" +
		// ObjectWriter.getNbNormalUpdates());
		// println("inserting time with index=" + (end0 - start0));

		odb = open(baseName);
		/*
		 * IStorageEngine engine = Dummy.getEngine(odb); ClassInfo ci =
		 * engine.getSession
		 * (true).getMetaModel().getClassInfo(IndexedObject.class.getName(),
		 * true); long sizebtree = ci.getIndex(0).getBTree().getSize();
		 * println("Size btree="+sizebtree); //println(new
		 * BTreeDisplay().build(ci.getIndex(0).getBTree(), false));
		 */
		long start = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("creation", new Date(start0 + i)));
			Objects objects = q.objects();
			// println("olivier" + (i+1));
			assertEquals(1, objects.size());
		}
		long end = OdbTime.getCurrentTimeInMs();

		try {
			float duration = (float) (end - start) / (float) size;
			println(duration);
			double d = 0.144;
			if (!isLocal) {
				d = 1.16;
			}
			if (testPerformance && duration > d) {
				fail("Time of search in index is greater than " + d + " ms : " + duration);
			}

		} finally {
			odb.close();
		}

	}

	/** Test with 3 indexes */
	public void testInsertWith3IndexesCheckAll() throws Exception {
		String baseName = getBaseName();
		// LogUtil.logOn(LazyODBBTreePersister.LOG_ID, true);
		ODB odb = open(baseName);
		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "duration" };
		clazz.addIndexOn("index1", indexFields, true);

		String[] indexFields2 = { "creation" };
		clazz.addIndexOn("index2", indexFields2, true);

		String[] indexFields3 = { "name" };
		clazz.addIndexOn("index3", indexFields3, true);

		/*
		 * String[] indexFields2 = { "name", "creation" };
		 * clazz.addUniqueIndexOn("index2", indexFields2);
		 * 
		 * String[] indexFields3 = { "duration", "creation" };
		 * clazz.addUniqueIndexOn("index3", indexFields3);
		 */
		odb.close();

		odb = open(baseName);

		int size = 500;
		int commitInterval = 10;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), i, new Date());
			odb.store(io1);
			if (i % commitInterval == 0) {
				//odb.commit();
				//println(i + " : commit / " + size);
			}
		}
		odb.close();
		long end0 = OdbTime.getCurrentTimeInMs();
		// println("IPU=" + ObjectWriter.getNbInPlaceUpdates() + " - NU=" +
		// ObjectWriter.getNbNormalUpdates());
		println("inserting time with index=" + (end0 - start0));

		odb = open(baseName);
		/*
		 * IStorageEngine engine = Dummy.getEngine(odb); ClassInfo ci =
		 * engine.getSession(true).getMetaModel().getClassInfo(
		 * IndexedObject.class.getName(), true); long sizebtree =
		 * ci.getIndex(0).getBTree().getSize(); println("Size btree=" +
		 * sizebtree); // println(new
		 * BTreeDisplay().build(ci.getIndex(0).getBTree(), false));
		 */
		long start = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("duration", i));
			Objects objects = odb.getObjects(q, false);
			// println("olivier" + (i+1));
			assertEquals(1, objects.size());
		}
		long end = OdbTime.getCurrentTimeInMs();

		try {
			float duration = (float) (end - start) / (float) size;

			println(duration);
			double d = 0.144;
			if (!isLocal) {
				d = 1.16;
			}
			if (testPerformance && duration > d) {
				fail("Time of search in index is greater than " + d + " ms : " + duration);
			}

		} finally {
			odb.close();
		}

	}

	/** Test with on e key index */
	public void testInsertWithoutIndex3() throws Exception {
		String baseName = getBaseName();
		if (!runAll) {
			return;
		}
		/*
		 * ODB odb = open(baseName); Configuration.setUseLazyCache(true);
		 * //odb.store(new IndexedObject()); ClassRepresentation clazz =
		 * odb.getClassRepresentation(IndexedObject.class); String[]
		 * indexFields = { "name" }; clazz.addUniqueIndexOn("index1",
		 * indexFields, true);
		 * 
		 * odb.close();
		 */

		ODB odb = open(baseName);

		int size = 30000;
		int commitInterval = 1000;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + size, new Date());
			odb.store(io1);
			if (i % commitInterval == 0) {
				odb.commit();
				// println(i+" : commit");
			}
		}
		odb.close();
		long end0 = OdbTime.getCurrentTimeInMs();

		odb = open(baseName);
		Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + size));
		long start = OdbTime.getCurrentTimeInMs();
		Objects objects = odb.getObjects(q, false);
		long end = OdbTime.getCurrentTimeInMs();
		assertEquals(1, objects.size());
		IndexedObject io2 = (IndexedObject) objects.first();
		assertEquals("olivier" + size, io2.getName());
		assertEquals(15 + size, io2.getDuration());
		long duration = end - start;
		println("duration=" + duration);

		odb.close();
		println(duration);
		double d = 408;
		if (!isLocal) {
			d = 3500;
		}
		if (duration > d) {
			fail("Time of search in index is greater than " + d + " ms : " + duration);
		}

	}

	/** Test with two key index */
	public void testInsertWith3Indexes() throws Exception {
		String baseName = getBaseName();

		ODB odb = open(baseName);
		// Configuration.setUseLazyCache(true);
		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);

		String[] indexFields3 = { "name" };
		clazz.addUniqueIndexOn("index3", indexFields3, true);

		String[] indexFields2 = { "name", "creation" };
		clazz.addUniqueIndexOn("index2", indexFields2, true);

		String[] indexField4 = { "duration", "creation" };
		clazz.addUniqueIndexOn("inde3", indexField4, true);

		odb.close();

		odb = open(baseName);

		int size = isLocal ? 100 : 10;
		long start0 = OdbTime.getCurrentTimeInMs();

		Date[] dates = new Date[size];
		for (int i = 0; i < size; i++) {
			// println(i);
			dates[i] = new Date();
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), i, dates[i]);
			odb.store(io1);
			if (i % 100 == 0) {
				println(i);
			}
		}
		odb.close();
		long end0 = OdbTime.getCurrentTimeInMs();

		odb = open(baseName);

		long start = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			long t0 = System.currentTimeMillis();
			Query q = odb.query(IndexedObject.class, W.and().add(W.equal("duration", i)).add(
					W.equal("creation", dates[i])));
			Objects objects = odb.getObjects(q, true);
			assertEquals(1, objects.size());
			assertTrue(q.getExecutionPlan().useIndex());
			long t1 = System.currentTimeMillis();
			//println(i+" : time=" +(t1-t0) + "  - query time="+q.getExecutionPlan().getDuration() );
			
		}
		long end = OdbTime.getCurrentTimeInMs();
		double duration = (end - start);
		duration = duration / size;
		println("duration=" + duration);
		odb.close();

		println(duration);
		double d = 0.11;
		if (!isLocal) {
			d = 10;
		}
		if (testPerformance && duration > d) {
			fail("Time of search in index is greater than " + d + " ms : " + duration);
		}
	}

	/** Test with two key index */
	public void testInsertWith4IndexesAndCommits() throws Exception {
		String baseName = getBaseName();
		if (!runAll) {
			return;
		}
		ODB odb = open(baseName);
		// Configuration.setUseLazyCache(true);
		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);

		String[] indexField1 = { "duration" };
		clazz.addUniqueIndexOn("inde1", indexField1, true);

		String[] indexFields3 = { "name" };
		clazz.addUniqueIndexOn("index3", indexFields3, true);

		String[] indexFields2 = { "name", "creation" };
		clazz.addUniqueIndexOn("index2", indexFields2, true);

		String[] indexField4 = { "duration", "creation" };
		clazz.addUniqueIndexOn("inde4", indexField4, true);

		odb.close();

		odb = open(baseName);

		int size = 10000;
		int commitInterval = 10;
		long start0 = OdbTime.getCurrentTimeInMs();

		for (int i = 0; i < size; i++) {
			// println(i);
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), i, new Date());
			odb.store(io1);
			if (i % 1000 == 0) {
				println(i);
			}
			if (i % commitInterval == 0) {
				odb.commit();
			}
		}
		odb.close();
		long end0 = OdbTime.getCurrentTimeInMs();

		odb = open(baseName);

		long start = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("duration", i));
			Objects objects = odb.getObjects(q, false);
			// println("olivier" + (i+1));
			assertEquals(1, objects.size());
		}
		long end = OdbTime.getCurrentTimeInMs();
		long duration = end - start;
		println("duration=" + duration);
		odb.close();
		if (testPerformance && duration > 111) {
			fail("Time of search in index : " + duration + ", should be less than 111");
		}
	}

	/** Test with two key index */
	public void testInsertWithIndex4() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);

		String[] indexFields3 = { "name" };
		clazz.addUniqueIndexOn("index3", indexFields3, true);

		String[] indexFields2 = { "name", "creation" };
		clazz.addUniqueIndexOn("index2", indexFields2, true);

		String[] indexField4 = { "duration", "creation" };
		clazz.addUniqueIndexOn("inde3", indexField4, true);

		odb.close();

		odb = open(baseName);

		int size = isLocal ? 500 : 110;
		int commitInterval = 100;
		long start0 = OdbTime.getCurrentTimeInMs();

		for (int i = 0; i < size; i++) {
			// println(i);
			IndexedObject ioio = new IndexedObject("olivier" + (i + 1), i + 15 + size, new Date());
			odb.store(ioio);
			if (i % commitInterval == 0) {
				long t0 = OdbTime.getCurrentTimeInMs();
				odb.commit();
				long t1 = OdbTime.getCurrentTimeInMs();
				println(i + " : commit - ctime " + (t1 - t0) + " -ttime=");
				// println(LazyODBBTreePersister.counters());
				LazyODBBTreePersister.resetCounters();
			}
		}
		Date theDate = new Date();
		String theName = "name indexed";
		IndexedObject io1 = new IndexedObject(theName, 45, theDate);
		odb.store(io1);
		odb.close();
		long end0 = OdbTime.getCurrentTimeInMs();

		odb = open(baseName);
		// IQuery q = new
		// CriteriaQuery(IndexedObject.class,Restrictions.and().add(Restrictions.equal("name",theName)).add(Restrictions.equal("creation",
		// theDate)));
		Query q = odb.query(IndexedObject.class, W.equal("name", theName));

		long start = OdbTime.getCurrentTimeInMs();
		Objects objects = q.objects();
		long end = OdbTime.getCurrentTimeInMs();

		if (isLocal) {
			assertEquals("index3", q.getExecutionPlan().getIndex().getName());
		}

		assertEquals(1, objects.size());
		IndexedObject io2 = (IndexedObject) objects.first();
		assertEquals(theName, io2.getName());
		assertEquals(45, io2.getDuration());
		assertEquals(theDate, io2.getCreation());
		long duration = end - start;
		println("duration=" + duration);
		odb.close();
		if (testPerformance && duration > 1) {
			fail("Time of search in index > 1 : " + duration);
		}
	}

	public void testInsertAndDeleteWithIndex() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		odb.close();

		odb = open(baseName);

		int size = 500;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + i, new Date());
			odb.store(io1);
			if (i % 1000 == 0)
				println(i);
		}
		long tt0 = OdbTime.getCurrentTimeInMs();
		odb.close();
		long tt1 = OdbTime.getCurrentTimeInMs();
		long end0 = OdbTime.getCurrentTimeInMs();
		odb = open(baseName);

		long totalTime = 0;
		long maxTime = 0;
		long minTime = 100000;
		for (int i = 0; i < size; i++) {
			Query query = odb.query(IndexedObject.class, W.equal("name", "olivier" + (i + 1)));
			long start = OdbTime.getCurrentTimeInMs();
			Objects objects = odb.getObjects(query, true);
			long end = OdbTime.getCurrentTimeInMs();
			assertEquals(1, objects.size());
			IndexedObject io2 = (IndexedObject) objects.first();
			assertEquals("olivier" + (i + 1), io2.getName());
			assertEquals(15 + i, io2.getDuration());
			long d = end - start;
			totalTime += d;

			if (d > maxTime) {
				maxTime = d;
			}
			if (d < minTime) {
				minTime = d;
			}
			odb.delete(io2);

		}
		odb.close();

		odb = open(baseName);

		Query q = odb.query(IndexedObject.class);
		Objects oos = odb.getObjects(q, true);

		for (int i = 0; i < size; i++) {
			q = odb.query(IndexedObject.class, W.equal("name", "olivier" + (i + 1)));
			oos = odb.getObjects(q, true);
			assertEquals(0, oos.size());
		}
		odb.close();

		println("total duration=" + totalTime + " / " + (double) totalTime / size);
		println("duration max=" + maxTime + " / min=" + minTime);
		if(testPerformance){
			assertTrue(totalTime / size < 0.9);
			// TODO Try to get maxTime < 10!
			assertTrue(maxTime < 20);
			assertTrue(minTime == 0);
		}

	}

	public void testInsertAndDeleteWithIndex1() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		odb.close();

		odb = open(baseName);

		int size = 1400;
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + i, new Date());
			odb.store(io1);
		}
		odb.close();
		System.out.println("----ola");
		odb = open(baseName);

		Query q = odb.query(IndexedObject.class);
		Objects<IndexedObject> objects = odb.getObjects(q);
		while (objects.hasNext()) {
			IndexedObject io = objects.next();
			println(io);
			odb.delete(io);
		}
		odb.close();

	}

	public void testInsertAndDeleteWithIndexWith10000() throws Exception {
		String baseName = getBaseName();
		if (!runAll) {
			return;
		}

		ODB odb = open(baseName);

		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		odb.close();

		odb = open(baseName);

		int size = 10000;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + i, new Date());
			odb.store(io1);
			if (i % 1000 == 0)
				println(i);
		}
		long tt0 = OdbTime.getCurrentTimeInMs();
		odb.close();
		long tt1 = OdbTime.getCurrentTimeInMs();
		long end0 = OdbTime.getCurrentTimeInMs();
		odb = open(baseName);

		long totalSelectTime = 0;
		long maxTime = 0;
		long minTime = 100000;
		long t0 = OdbTime.getCurrentTimeInMs();
		long t1 = 0;
		long ta1 = 0;
		long ta2 = 0;
		long totalTimeDelete = 0;
		long totalTimeSelect = 0;

		for (int j = 0; j < size; j++) {

			Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + (j + 1)));

			long start = OdbTime.getCurrentTimeInMs();
			Objects objects = odb.getObjects(q, true);
			long end = OdbTime.getCurrentTimeInMs();
			assertEquals(1, objects.size());
			IndexedObject io2 = (IndexedObject) objects.first();
			assertEquals("olivier" + (j + 1), io2.getName());
			assertEquals(15 + j, io2.getDuration());
			long d = end - start;
			totalSelectTime += d;

			if (d > maxTime) {
				maxTime = d;
			}
			if (d < minTime) {
				minTime = d;
			}
			ta1 = OdbTime.getCurrentTimeInMs();
			odb.delete(io2);
			ta2 = OdbTime.getCurrentTimeInMs();
			totalTimeDelete += (ta2 - ta1);
			totalTimeSelect += (end - start);
			if (j % 100 == 0 && j > 0) {
				t1 = OdbTime.getCurrentTimeInMs();
				println(j + " - t= " + (t1 - t0) + " - delete=" + (totalTimeDelete / j) + " / select=" + (totalTimeSelect / j));
				println(LazyODBBTreePersister.counters());
				LazyODBBTreePersister.resetCounters();
				t0 = t1;
			}
		}
		odb.close();

		println("total select=" + totalSelectTime + " / " + (double) totalSelectTime / size);
		println("total delete=" + totalTimeDelete + " / " + (double) totalTimeDelete / size);
		println("duration max=" + maxTime + " / min=" + minTime);

		odb = open(baseName);

		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + (i + 1)));
			long start = OdbTime.getCurrentTimeInMs();
			Objects objects = odb.getObjects(q, true);
			long end = OdbTime.getCurrentTimeInMs();
			assertEquals(0, objects.size());
			if (i % 100 == 0)
				println(i);
		}

		odb.close();
		float timePerObject = (float) totalSelectTime / (float) size;
		println("Time per object = " + timePerObject);
		if (timePerObject > 1) {
			println("Time per object = " + timePerObject);
		}
		assertTrue(timePerObject < 0.16);

		// TODO Try to get maxTime < 10!
		assertTrue(maxTime < 250);
		assertTrue(minTime < 1);

	}

	public void testInsertAndDeleteWithIndexWith4Elements() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		odb.close();

		odb = open(baseName);

		int size = 4;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + i, new Date());
			odb.store(io1);
			if (i % 1000 == 0)
				println(i);
		}
		long tt0 = OdbTime.getCurrentTimeInMs();
		odb.close();
		long tt1 = OdbTime.getCurrentTimeInMs();
		long end0 = OdbTime.getCurrentTimeInMs();
		odb = open(baseName);
		long totalTime = 0;
		long maxTime = 0;
		long minTime = 100000;
		long t0 = OdbTime.getCurrentTimeInMs();
		long t1 = 0;
		/*
		 * IStorageEngine e = Dummy.getEngine(odb); ClassInfoIndex cii =
		 * e.getSession(true).getMetaModel().getClassInfo(
		 * IndexedObject.class.getName(), true).getIndex(0); // println(new
		 * BTreeDisplay().build(cii.getBTree(),true));
		 */

		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + (i + 1)));
			long start = OdbTime.getCurrentTimeInMs();
			Objects objects = odb.getObjects(q, true);
			long end = OdbTime.getCurrentTimeInMs();
			assertEquals(1, objects.size());
			IndexedObject io2 = (IndexedObject) objects.first();
			assertEquals("olivier" + (i + 1), io2.getName());
			assertEquals(15 + i, io2.getDuration());
			long d = end - start;
			totalTime += d;

			if (d > maxTime) {
				maxTime = d;
			}
			if (d < minTime) {
				minTime = d;
			}
			odb.delete(io2);

			if (i % 100 == 0) {
				t1 = OdbTime.getCurrentTimeInMs();
				println(i + " - t= " + (t1 - t0));
				t0 = t1;
			}
		}

		odb.close();

		odb = open(baseName);

		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + (i + 1)));
			long start = OdbTime.getCurrentTimeInMs();
			Objects objects = odb.getObjects(q, true);
			long end = OdbTime.getCurrentTimeInMs();
			assertEquals(0, objects.size());
			if (i % 100 == 0)
				println(i);
		}
		odb.close();
		
		double unitTime = (double) totalTime / size;
		println("total duration=" + totalTime + " / " + (double) totalTime / size);
		println("duration max=" + maxTime + " / min=" + minTime);

		if (isLocal) {
			assertTrue(unitTime < 1);
		} else {
			assertTrue(unitTime < 6);
		}

		// TODO Try to get maxTime < 10!
		if(testPerformance){
			assertTrue(maxTime < 250);
			assertTrue(minTime <= 1);
		}
	}

	public void testInsertAndDeleteWithIndexWith40Elements() throws Exception {
		String baseName = getBaseName();
		NeoDatisConfig config = NeoDatis.getConfig();
		config.setDefaultIndexBTreeDegree(3);
		
		ODB odb = open(baseName,config);

		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		odb.close();

		odb = open(baseName);

		int size = 6;
		long start0 = OdbTime.getCurrentTimeInMs();
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + i, new Date());
			odb.store(io1);
			if (i % 1000 == 0)
				println(i);
		}
		long tt0 = OdbTime.getCurrentTimeInMs();
		/*
		 * IStorageEngine e = Dummy.getEngine(odb); ClassInfoIndex cii =
		 * e.getSession(true).getMetaModel().getClassInfo(
		 * IndexedObject.class.getName(), true).getIndex(0); // println(new
		 * BTreeDisplay().build(cii.getBTree(),true));
		 */
		odb.close();
		long tt1 = OdbTime.getCurrentTimeInMs();
		long end0 = OdbTime.getCurrentTimeInMs();
		odb = open(baseName);
		long totalTime = 0;
		long maxTime = 0;
		long minTime = 100000;
		long t0 = OdbTime.getCurrentTimeInMs();
		long t1 = 0;
		/*
		 * e = Dummy.getEngine(odb); cii =
		 * e.getSession(true).getMetaModel().getClassInfo(
		 * IndexedObject.class.getName(), true).getIndex(0); println(new
		 * BTreeDisplay().build(cii.getBTree(), true));
		 */
		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + (i + 1)));
			long start = OdbTime.getCurrentTimeInMs();
			Objects objects = odb.getObjects(q, true);
			long end = OdbTime.getCurrentTimeInMs();
			assertEquals(1, objects.size());
			IndexedObject io2 = (IndexedObject) objects.first();
			assertEquals("olivier" + (i + 1), io2.getName());
			assertEquals(15 + i, io2.getDuration());
			long d = end - start;
			totalTime += d;

			if (d > maxTime) {
				maxTime = d;
			}
			if (d < minTime) {
				minTime = d;
			}
			odb.delete(io2);

			if (i % 100 == 0) {
				t1 = OdbTime.getCurrentTimeInMs();
				println(i + " - t= " + (t1 - t0));
				t0 = t1;
			}
		}
		// println(new BTreeDisplay().build(cii.getBTree(), true));

		odb.close();

		odb = open(baseName);

		for (int i = 0; i < size; i++) {
			Query q = odb.query(IndexedObject.class, W.equal("name", "olivier" + (i + 1)));
			long start = OdbTime.getCurrentTimeInMs();
			Objects objects = odb.getObjects(q, true);
			long end = OdbTime.getCurrentTimeInMs();
			assertEquals(0, objects.size());
			if (i % 100 == 0)
				println(i);
		}
		double unitTime = (double) totalTime / size;
		println("total duration=" + totalTime + " / " + unitTime);
		println("duration max=" + maxTime + " / min=" + minTime);
		odb.close();
		

		if (isLocal) {
			assertTrue(unitTime < 1);
		} else {
			assertTrue(unitTime < 6);
		}

		// TODO Try to get maxTime < 10!
		if(testPerformance){
			assertTrue(maxTime < 250);
			assertTrue(minTime <= 1);
		}
	}

	public void testSizeBTree() throws Exception {

		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		
		ODB odb = open(baseName);

		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);
		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index1", indexFields, true);

		odb.close();

		odb = open(baseName);

		int size = 4;
		for (int i = 0; i < size; i++) {
			IndexedObject io1 = new IndexedObject("olivier" + (i + 1), 15 + i, new Date());
			odb.store(io1);
			if (i % 1000 == 0)
				println(i);
		}
		odb.close();
		odb = open(baseName);
		SessionEngine e = Dummy.getEngine(odb);
		ClassInfoIndex cii = e.getSession().getMetaModel().getClassInfo(IndexedObject.class.getName(), true).getIndex(0);
		odb.close();
		
		assertEquals(size, cii.getBTree().getSize());
	}

	/**
	 * Test index with 3 keys .
	 * 
	 * Select using only one field to verify that query does not use index, then
	 * execute a query with the 3 fields and checks than index is used
	 */
	public void testInsertWith3Keys() throws Exception {
		String baseName = getBaseName();
		
		ODB odb = open(baseName);
		// odb.store(new IndexedObject());
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);

		String[] indexFields = { "name", "duration", "creation" };
		clazz.addUniqueIndexOn("the index", indexFields, true);

		odb.close();

		odb = open(baseName);

		int size = isLocal ? 500 : 50;
		int commitInterval = isLocal ? 100 : 10;
		long start0 = OdbTime.getCurrentTimeInMs();

		for (int i = 0; i < size; i++) {
			IndexedObject io2 = new IndexedObject("olivier" + (i + 1), i + 15 + size, new Date());
			odb.store(io2);
			if (i % commitInterval == 0) {
				long t0 = OdbTime.getCurrentTimeInMs();
				odb.commit();
				long t1 = OdbTime.getCurrentTimeInMs();
				println(i + " : commit - ctime " + (t1 - t0) + " -ttime=");
				// println(LazyODBBTreePersister.counters());
				LazyODBBTreePersister.resetCounters();
			}
		}
		Date theDate = new Date();
		String theName = "name indexed";
		int theDuration = 45;
		IndexedObject io1 = new IndexedObject(theName, theDuration, theDate);
		odb.store(io1);
		odb.close();

		odb = open(baseName);

		// first search without index
		Query q = odb.query(IndexedObject.class, W.equal("name", theName));

		Objects objects = q.objects();
		assertFalse(q.getExecutionPlan().useIndex());
		println(q.getExecutionPlan().getDetails());
		assertEquals(1, objects.size());

		IndexedObject io3 = (IndexedObject) objects.first();
		assertEquals(theName, io3.getName());
		assertEquals(theDuration, io3.getDuration());
		assertEquals(theDate, io3.getCreation());
		odb.close();

		odb = open(baseName);

		// Then search usin index
		q = odb.query(IndexedObject.class, W.and().add(W.equal("name", theName)).add(W.equal("creation", theDate)).add(
				W.equal("duration", theDuration)));

		objects = q.objects();
		assertEquals(true, q.getExecutionPlan().useIndex());
		if (isLocal) {
			assertEquals("the index", q.getExecutionPlan().getIndex().getName());
		}
		println(q.getExecutionPlan().getDetails());
		assertEquals(1, objects.size());

		io3 = (IndexedObject) objects.first();
		assertEquals(theName, io3.getName());
		assertEquals(theDuration, io3.getDuration());
		assertEquals(theDate, io3.getCreation());
		odb.close();

	}

	/**
	 * Test index. Creates 1000 objects. Take 10 objects to update 10000 times.
	 * Then check if all objects are ok
	 * 
	 */
	public void testXUpdatesWithIndex() throws Exception {
		String baseName = getBaseName();
		try {
			
			ODB odb = open(baseName);
			// odb.store(new IndexedObject());
			ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject.class);

			String[] indexFields = { "name" };
			clazz.addUniqueIndexOn("index", indexFields, true);

			odb.close();

			odb = open(baseName);

			long start = System.currentTimeMillis();
			int size = 100;
			int nbObjects = 10;
			int nbUpdates = isLocal ? 10 : 5;
			for (int i = 0; i < size; i++) {
				IndexedObject io1 = new IndexedObject("IO-" + i + "-0", i + 15 + size, new Date());
				odb.store(io1);
			}
			odb.close();
			println("Time of insert " + size + " objects = " + size);

			String[] indexes = { "IO-0-0", "IO-100-0", "IO-200-0", "IO-300-0", "IO-400-0", "IO-500-0", "IO-600-0", "IO-700-0", "IO-800-0",
					"IO-900-0" };

			long t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0, t6 = 0;

			for (int i = 0; i < nbUpdates; i++) {
				start = OdbTime.getCurrentTimeInMs();

				for (int j = 0; j < nbObjects; j++) {
					t1 = System.currentTimeMillis();
					odb = open(baseName);
					t2 = System.currentTimeMillis();
					Query q = odb.query(IndexedObject.class, W.equal("name", indexes[j]));
					Objects os = odb.getObjects(q);
					t3 = System.currentTimeMillis();
					assertTrue(q.getExecutionPlan().useIndex());
					assertEquals(1, os.size());
					// check if index has been used
					assertTrue(q.getExecutionPlan().useIndex());
					IndexedObject io = (IndexedObject) os.first();
					if (i > 0) {
						assertTrue(io.getName().endsWith(("-" + (i - 1))));
					}
					io.setName(io.getName() + "-updated-" + i);
					odb.store(io);
					t4 = System.currentTimeMillis();
					if (isLocal && j == 0) {
						SessionEngine engine = Dummy.getEngine(odb);
						ClassInfo ci = engine.getSession().getMetaModel().getClassInfo(IndexedObject.class.getName(), true);
						ClassInfoIndex cii = ci.getIndex(0);
						assertEquals(size, cii.getBTree().getSize());
					}
					indexes[j] = io.getName();
					assertEquals(new BigInteger("" + size), odb.query(IndexedObject.class).count());
					t5 = System.currentTimeMillis();
					odb.commit();
					odb.close();
					t6 = System.currentTimeMillis();

				}
				long end = OdbTime.getCurrentTimeInMs();
				System.out.println("Nb Updates of " + nbObjects + " =" + i + " - " + (end - start) + "ms  -- open=" + (t2 - t1)
						+ " - getObjects=" + (t3 - t2) + " - update=" + (t4 - t3) + " - count=" + (t5 - t4) + " - close=" + (t6 - t5));
			}
		} finally {
		}
	}

	public void simpleUniqueIndex() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ClassRepresentation clazz = odb.getClassRepresentation(Function.class);

		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("index", indexFields, true);

		odb.close();

		odb = open(baseName);

		// inserting 3 objects with 3 different index keys
		odb.store(new Function("function1"));
		odb.store(new Function("function2"));
		odb.store(new Function("function3"));

		odb.close();

		odb = open(baseName);
		try {
			// Tries to store another function with name function1 => send an
			// exception because of duplicated keys
			odb.store(new Function("function1"));
			fail("Should have thrown Exception");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testIndexExist1() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ClassRepresentation clazz = odb.getClassRepresentation(Function.class);

		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("my-index", indexFields, true);
		odb.store(new Function("test"));
		odb.close();

		odb = open(baseName);
		assertTrue(odb.getClassRepresentation(Function.class).existIndex("my-index"));
		assertFalse(odb.getClassRepresentation(Function.class).existIndex("my-indexdhfdjkfhdjkhj"));

		odb.close();
	}

	public void testIndexExist2() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ClassRepresentation clazz = odb.getClassRepresentation(Function.class);

		String[] indexFields = { "name" };
		clazz.addUniqueIndexOn("my-index", indexFields, true);

		odb.close();

		odb = open(baseName);
		assertTrue(odb.getClassRepresentation(Function.class).existIndex("my-index"));
		assertFalse(odb.getClassRepresentation(Function.class).existIndex("my-indexdhfdjkfhdjkhj"));

		odb.close();
	}

	public static void main(String[] args) throws Exception {
		TestIndex ti = new TestIndex();
		//ti.testInsertWithIndex3Part2();
	}
}
