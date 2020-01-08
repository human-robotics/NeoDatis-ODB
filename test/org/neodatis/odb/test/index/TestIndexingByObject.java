package org.neodatis.odb.test.index;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.tool.wrappers.OdbTime;

import java.util.Date;

/**
 * Junit to test indexing an object when the index field is an object and not a
 * native attribute
 */
public class TestIndexingByObject extends ODBTest {

	public void test1() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		String[] fields = { "object" };
		odb.getClassRepresentation(IndexedObject2.class).addUniqueIndexOn("index1", fields, true);

		IndexedObject2 o1 = new IndexedObject2("Object1", new IndexedObject("Inner Object 1", 10, new Date()));
		odb.store(o1);
		odb.close();

		odb = open(baseName);
		// First get the object used to index
		Objects objects = odb.query(IndexedObject.class).objects();
		IndexedObject io = (IndexedObject) objects.first();
		Query q = odb.query(IndexedObject2.class, W.equal("object", io));
		objects = q.objects();
		IndexedObject2 o2 = (IndexedObject2) objects.first();
		odb.close();
		assertEquals(o1.getName(), o2.getName());
		println(q.getExecutionPlan().getDetails());
		assertFalse(q.getExecutionPlan().getDetails().indexOf("index1") == -1);
	}

	public void test2() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		String[] fields = { "object" };
		odb.getClassRepresentation(IndexedObject2.class).addUniqueIndexOn("index1", fields, true);

		int size = isLocal ? 500 : 50;
		for (int i = 0; i < size; i++) {
			odb.store(new IndexedObject2("Object " + i, new IndexedObject("Inner Object " + i, i, new Date())));
		}
		odb.close();

		odb = open(baseName);

		Query q = odb.query(IndexedObject.class, W.equal("name", "Inner Object " + (size - 1)));

		// First get the object used to index, the last one. There is no index
		// on the class and field
		long start0 = OdbTime.getCurrentTimeInMs();
		Objects objects = odb.query(q).objects();
		long end0 = OdbTime.getCurrentTimeInMs();
		IndexedObject io = (IndexedObject) objects.first();
		println("d0=" + (end0 - start0));
		println(q.getExecutionPlan().getDetails());
		q = odb.query(IndexedObject2.class, W.equal("object", io));

		long start = OdbTime.getCurrentTimeInMs();
		objects = odb.query(q).objects();
		long end = OdbTime.getCurrentTimeInMs();
		println("d=" + (end - start));
		IndexedObject2 o2 = (IndexedObject2) objects.first();
		odb.close();

		assertEquals("Object " + (size - 1), o2.getName());
		println(q.getExecutionPlan().getDetails());
		assertTrue(q.getExecutionPlan().useIndex());
	}
	
	public void test3_BadAttributeInIndex() throws Exception {

		String baseName = getBaseName();
		ODB odb = null;
		String fieldName = "fkjdsfkjdhfjkdhjkdsh";
		try{
			odb = open(baseName);
			String[] fields = { fieldName };
			odb.getClassRepresentation(IndexedObject2.class).addUniqueIndexOn("index1", fields, true);
			fail("Should have thrown an exception because the field "+fieldName + " does not exist");
		}catch (Exception e) {
			// normal
		}
		finally{
			odb.close();
		}
	}

	public void test4() throws Exception {

		String baseName = getBaseName();
		ODB odb = open(baseName);
		String[] fields = { "object" };
		odb.getClassRepresentation(IndexedObject2.class).addUniqueIndexOn("index1", fields, true);

		String[] fields2 = { "name" };
		odb.getClassRepresentation(IndexedObject.class).addUniqueIndexOn("index2", fields2, true);

		int size = isLocal ? 5000 : 500;
		for (int i = 0; i < size; i++) {
			odb.store(new IndexedObject2("Object " + i, new IndexedObject("Inner Object " + i, i, new Date())));
		}
		odb.close();

		odb = open(baseName);

		Query q = odb.query(IndexedObject.class, W.equal("name", "Inner Object " + (size - 1)));

		// First get the object used to index, the last one. There is no index
		// on the class and field
		long start0 = OdbTime.getCurrentTimeInMs();
		Objects objects = odb.query(q).objects();
		long end0 = OdbTime.getCurrentTimeInMs();
		// check if index has been used
		assertTrue(q.getExecutionPlan().useIndex());

		IndexedObject io = (IndexedObject) objects.first();
		println("d0=" + (end0 - start0));
		println(q.getExecutionPlan().getDetails());
		q = odb.query(IndexedObject2.class, W.equal("object", io));

		long start = OdbTime.getCurrentTimeInMs();
		objects = odb.query(q).objects();
		long end = OdbTime.getCurrentTimeInMs();
		println("d=" + (end - start));
		IndexedObject2 o2 = (IndexedObject2) objects.first();
		odb.close();

		assertEquals("Object " + (size - 1), o2.getName());
		println(q.getExecutionPlan().getDetails());
		assertTrue(q.getExecutionPlan().useIndex());
	}

}
