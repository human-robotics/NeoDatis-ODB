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
package org.neodatis.odb.test.delete;

import org.junit.Test;
import org.neodatis.odb.*;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.tool.wrappers.OdbTime;

import java.util.ArrayList;
import java.util.List;

public class TestDelete extends ODBTest {
	public static long start = OdbTime.getCurrentTimeInMs();
    @Test
	public void test1() throws Exception {

		String baseName = getBaseName();
		ODB odb = open(baseName);

		long n = odb.query(Function.class).count().longValue();
		Function function1 = new Function("function1");
		Function function2 = new Function("function2");
		Function function3 = new Function("function3");

		odb.store(function1);
		odb.store(function2);
		odb.store(function3);

		odb.close();

		odb = open(baseName);
		Objects l = odb.query(Function.class, W.equal("name", "function2")).objects();
		Function function = (Function) l.first();
		odb.delete(function);
		odb.close();

		odb = open(baseName);
		Objects l2 = odb.getObjects(Function.class);
		assertEquals(n + 2, odb.query(Function.class).count().longValue());

		odb.close();
	}
    @Test
	public void test2() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		long nbFunctions = odb.query(Function.class).count().longValue();
		long nbProfiles = odb.query(Profile.class).count().longValue();

		Function function1 = new Function("function1");
		Function function2 = new Function("function2");
		Function function3 = new Function("function3");

		List functions = new ArrayList();
		functions.add(function1);
		functions.add(function2);
		functions.add(function3);
		Profile profile1 = new Profile("profile1", functions);
		Profile profile2 = new Profile("profile2", function1);

		odb.store(profile1);
		odb.store(profile2);

		odb.close();

		odb = open(baseName);
		// checks functions
		Objects lfunctions = odb.getObjects(Function.class);
		assertEquals(nbFunctions + 3, lfunctions.size());

		Objects l = odb.query(Function.class, W.equal("name", "function2")).objects();
		Function function = (Function) l.first();
		odb.delete(function);
		odb.close();

		odb = open(baseName);
		assertEquals(nbFunctions + 2, odb.query(Function.class).count().longValue());
		Objects l2 = odb.getObjects(Function.class);

		// check Profile 1
		Objects lprofile = odb.query(Profile.class, W.equal("name", "profile1")).objects();
		Profile p1 = (Profile) lprofile.first();

		assertEquals(2, p1.getFunctions().size());
		odb.close();

	}
    @Test
	public void test30() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		ObjectOid oid1 = odb.store(new Function("function 1"));
		ObjectOid oid2 = odb.store(new Function("function 2"));
		odb.close();

		println(oid1);
		println(oid2);

		odb = open(baseName);
		odb.delete(odb.getObjects(Function.class).first());
		odb.close();

		odb = open(baseName);
		Function f = (Function) odb.getObjects(Function.class).first();
		odb.close();
		assertEquals("function 2", f.getName());

	}
    @Test
	public void test3() throws Exception {
		String baseName = getBaseName();
		String baseName2 = "2"+baseName;
		ODB odb = open(baseName);
		int size = 1000;
		for (int i = 0; i < size; i++) {
			odb.store(new Function("function " + i));
		}

		odb.close();

		odb = open(baseName);
		Query q = odb.query(Function.class);
		q.getQueryParameters().setInMemory(false);
		Objects objects = odb.getObjects(q);
		int j = 0;
		while (objects.hasNext() && j < objects.size() - 1) {
			odb.delete(objects.next());
			j++;
		}
		odb.close();
		odb = open(baseName);
		assertEquals(1, odb.query(Function.class).count().longValue());
		odb.close();

		if (isLocal && testNewFeature) {
			odb = open(baseName);
			odb.defragmentTo(ODBTest.DIRECTORY+ baseName2);
			odb.close();
			odb = open(baseName2);
			assertEquals(1, odb.query(Function.class).count().longValue());
			odb.close();
		}

	}
    @Test
	public void test4() throws Exception {
		String baseName = getBaseName();
		int n = isLocal ? 100 : 10;
		ODB odb = open(baseName);
		long size = odb.query(Function.class).count().longValue();

		for (int i = 0; i < n; i++) {
			Function login = new Function("login - " + (i + 1));
			odb.store(login);
			assertEquals(size + i + 1, odb.query(Function.class).count().longValue());
		}

		odb.commit();
		Objects l = odb.getObjects(Function.class);

		int j = 0;
		while (l.hasNext()) {
			// println("i="+i);
			Function f = (Function) l.next();
			odb.delete(f);
			Objects l2 = odb.getObjects(Function.class);
			assertEquals(size + n - (j + 1), l2.size());
			j++;
		}
		odb.commit();
		odb.close();

	}
    @Test
	public void test5() throws Exception {

		ODB odb = null;
		String baseName = getBaseName();
		odb = open(baseName);
		Function f = new Function("function1");
		odb.store(f);
		ObjectOid id = odb.getObjectId(f);

		try {
			odb.delete(f);
			ObjectOid id2 = odb.getObjectId(f);
			fail("The object has been deleted, the id should have been marked as deleted");
		} catch (NeoDatisRuntimeException e) {
			odb.close();
		}

	}
    @Test
	public void test5_byOid() throws Exception {

		ODB odb = null;
		String baseName = getBaseName();
		odb = open(baseName);
		Function f = new Function("function1");
		odb.store(f);
		ObjectOid oid = odb.getObjectId(f);

		try {
			odb.deleteObjectWithId(oid);
			ObjectOid id2 = odb.getObjectId(f);
			fail("The object has been deleted, the id should have been marked as deleted");
		} catch (NeoDatisRuntimeException e) {
			odb.close();
		}

	}
    @Test
	public void test5_deleteNullObject() throws Exception {

		ODB odb = null;
		String baseName = getBaseName();
		odb = open(baseName);
		Function f = new Function("function1");
		odb.store(f);
		ObjectOid oid = odb.getObjectId(f);

		try {
			odb.delete(null);
			fail("Should have thrown an exception: trying to delete a null object");
		} catch (NeoDatisRuntimeException e) {
			odb.close();
		}catch (Exception e) {
			fail("Should have thrown an OdbRuntimeException: trying to delete a null object");
		}

	}

    @Test
	public void test6() throws Exception {

		ODB odb = null;
		String baseName = getBaseName();
		odb = open(baseName);
		Function f = new Function("function1");
		odb.store(f);
		ObjectOid id = odb.getObjectId(f);
		odb.commit();
		try {
			odb.delete(f);
			odb.getObjectFromId(id);
			fail("The object has been deleted, the id should have been marked as deleted");
		} catch (NeoDatisRuntimeException e) {
			odb.close();
		}

	}
    @Test
	public void test7() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		Function f3 = new Function("function3");
		odb.store(f1);
		odb.store(f2);
		odb.store(f3);
		ObjectOid id = odb.getObjectId(f3);
		odb.close();
		try {
			odb = open(baseName);
			Function f3bis = (Function) odb.getObjectFromId(id);
			odb.delete(f3bis);
			odb.close();
			odb = open(baseName);
			Objects l = odb.getObjects(Function.class);
			odb.close();
			assertEquals(2, l.size());
		} catch (NeoDatisRuntimeException e) {
			odb.close();
		}

	}

	/**
	 * Test : delete the last object and insert a new one in the same
	 * transaction - detected by Alessandra
	 * 
	 * @throws Exception
	 */
    @Test
	public void test8() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		Function f3 = new Function("function3");
		odb.store(f1);
		odb.store(f2);
		odb.store(f3);
		ObjectOid id = odb.getObjectId(f3);
		odb.close();

		odb = open(baseName);
		Function f3bis = (Function) odb.getObjectFromId(id);
		odb.delete(f3bis);
		odb.store(new Function("last function"));
		odb.close();

		odb = open(baseName);
		Objects l = odb.getObjects(Function.class);
		odb.close();
		assertEquals(3, l.size());

	}

	/**
	 * Test : delete the last object and insert a new one in another transaction
	 * - detected by Alessandra
	 * 
	 * @throws Exception
	 */
    @Test
	public void test9() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		Function f3 = new Function("function3");
		odb.store(f1);
		odb.store(f2);
		odb.store(f3);
		ObjectOid id = odb.getObjectId(f3);
		odb.close();
		odb = open(baseName);
		Function f3bis = (Function) odb.getObjectFromId(id);
		odb.delete(f3bis);
		odb.close();

		odb = open(baseName);
		odb.store(new Function("last function"));
		odb.close();

		odb = open(baseName);
		Objects l = odb.getObjects(Function.class);
		odb.close();
		assertEquals(3, l.size());

	}

	/**
	 * Test : delete the unique object
	 * 
	 * 
	 * @throws Exception
	 */
    @Test
	public void test10() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);

		long size = odb.getObjects(Function.class).size();

		Function f1 = new Function("function1");
		odb.store(f1);
		odb.close();
		odb = open(baseName);
		Function f1bis = (Function) odb.getObjects(Function.class).first();
		odb.delete(f1bis);
		odb.close();

		odb = open(baseName);
		assertEquals(size, odb.getObjects(Function.class).size());
		odb.store(new Function("last function"));
		odb.close();

		odb = open(baseName);
		Objects l = odb.getObjects(Function.class);
		odb.close();
		assertEquals(size + 1, l.size());

	}

	/**
	 * Test : delete the unique object
	 * 
	 * 
	 * @throws Exception
	 */
    @Test
	public void test11() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		long size = odb.query(Function.class).count().longValue();
		Function f1 = new Function("function1");
		odb.store(f1);
		odb.close();
		odb = open(baseName);
		Function f1bis = (Function) odb.getObjects(Function.class).first();
		odb.delete(f1bis);
		odb.store(new Function("last function"));
		odb.close();

		odb = open(baseName);
		assertEquals(size + 1, odb.getObjects(Function.class).size());

		odb.close();

	}

	/**
	 * Bug detected by Olivier using the ODBMainExplorer, deleting many objects
	 * without commiting,and commiting at the end
	 * 
	 * @throws Exception
	 */
    @Test
	public void test12() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		Function f3 = new Function("function3");
		odb.store(f1);
		odb.store(f2);
		odb.store(f3);
		ObjectOid idf1 = odb.getObjectId(f1);
		ObjectOid idf2 = odb.getObjectId(f2);
		ObjectOid idf3 = odb.getObjectId(f3);


		odb.close();
		try {
			odb = open(baseName);

			odb.deleteObjectWithId(idf3);
			odb.deleteObjectWithId(idf2);

			odb.close();

			odb = open(baseName);
			Objects l = odb.getObjects(Function.class);
			odb.close();
			assertEquals(1, l.size());
		} catch (NeoDatisRuntimeException e) {
			throw e;
		}

	}

	/**
	 * Bug detected by Olivier using the ODBMainExplorer, deleting many objects
	 * without commiting,and commiting at the end
	 * 
	 * @throws Exception
	 */
    @Test
	public void test13() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();

		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		Function f3 = new Function("function3");
		odb.store(f1);
		odb.store(f2);
		odb.store(f3);
		ObjectOid idf1 = odb.getObjectId(f1);
		ObjectOid idf2 = odb.getObjectId(f2);
		ObjectOid idf3 = odb.getObjectId(f3);

		odb.close();
		try {
			odb = open(baseName);

			f1 = (Function) odb.getObjectFromId(idf1);
			f2 = (Function) odb.getObjectFromId(idf2);
			f3 = (Function) odb.getObjectFromId(idf3);

			odb.delete(f3);
			odb.delete(f2);
			odb.close();

			odb = open(baseName);
			Objects l = odb.getObjects(Function.class);
			odb.close();
			assertEquals(1, l.size());
		} catch (NeoDatisRuntimeException e) {
			throw e;
		}

	}

	/**
	 * creates 5 objects,commit. Then create 2 new objects and delete 4 existing
	 * objects without committing,and committing at the end
	 * 
	 * @throws Exception
	 */
    @Test
	public void test14() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		Function f3 = new Function("function3");
		Function f4 = new Function("function4");
		Function f5 = new Function("function5");
		odb.store(f1);
		odb.store(f2);
		odb.store(f3);
		odb.store(f4);
		odb.store(f5);
		assertEquals(5, odb.query(Function.class).count().longValue());
		odb.close();
		try {
			odb = open(baseName);

			Function f6 = new Function("function6");
			Function f7 = new Function("function7");

			odb.store(f6);
			odb.store(f7);

			assertEquals(7, odb.query(Function.class).count().longValue());

			Objects objects = odb.getObjects(Function.class);
			int i = 0;
			while (objects.hasNext() && i < 4) {
				odb.delete(objects.next());
				i++;
			}

			assertEquals(3, odb.query(Function.class).count().longValue());
			odb.close();

			odb = open(baseName);
			assertEquals(3, odb.query(Function.class).count().longValue());
			objects = odb.getObjects(Function.class);
			// println(objects);
			assertEquals("function5", ((Function) objects.next()).getName());
			assertEquals("function6", ((Function) objects.next()).getName());
			assertEquals("function7", ((Function) objects.next()).getName());
			odb.close();
		} catch (NeoDatisRuntimeException e) {
			throw e;
		}

	}

	/**
	 * creates 2 objects. Delete them. And create 2 new objects
	 * 
	 * @throws Exception
	 */
    @Test
	public void test15() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		ObjectOid oid1 = odb.store(f1);
		ObjectOid oid2 = odb.store(f2);
		//assertEquals(2, odb.query(Function.class).count().longValue());

		odb.delete(f1);
		odb.delete(f2);

		assertEquals(0, odb.query(Function.class).count().longValue());

		odb.store(f1);
		odb.store(f2);

		assertEquals(2, odb.query(Function.class).count().longValue());

		odb.close();

		odb = open(baseName);
		assertEquals(2, odb.getObjects(Function.class).size());
		odb.close();

	}
	
	/**
	 * creates 2 objects. Delete them by oid. And create 2 new objects
	 * 
	 * @throws Exception
	 */
    @Test
	public void test15_by_oid() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		ObjectOid oid1 = odb.store(f1);
		ObjectOid oid2 = odb.store(f2);
		assertEquals(2, odb.query(Function.class).count().longValue());

		odb.deleteObjectWithId(oid1);
		odb.deleteObjectWithId(oid2);

		assertEquals(0, odb.query(Function.class).count().longValue());

		odb.store(f1);
		odb.store(f2);

		assertEquals(2, odb.query(Function.class).count().longValue());

		odb.close();

		odb = open(baseName);
		assertEquals(2, odb.getObjects(Function.class).size());
		odb.close();

	}

	/**
	 * creates 2 objects. Delete them by oid. And create 2 new objects
	 * 
	 * @throws Exception
	 */
    @Test
	public void test15_by_oid_2() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		ObjectOid oid1 = odb.store(f1);
		ObjectOid oid2 = odb.store(f2);
		assertEquals(2, odb.query(Function.class).count().longValue());
		odb.close();
		

		odb = open(baseName);
		odb.deleteObjectWithId(oid1);
		odb.deleteObjectWithId(oid2);

		assertEquals(0, odb.query(Function.class).count().longValue());

		odb.store(f1);
		odb.store(f2);

		assertEquals(2, odb.query(Function.class).count().longValue());

		odb.close();

		odb = open(baseName);
		assertEquals(2, odb.getObjects(Function.class).size());
		odb.close();

	}

	/**
	 * creates x objects. Delete them. And create x new objects
	 * 
	 * @throws Exception
	 */
    @Test
	public void test16() throws Exception {
		String baseName = getBaseName();
		int size = isLocal ? 10000 : 100;
		ODB odb = null;
		odb = open(baseName);
		ObjectOid[] oids = new ObjectOid[size];
		for (int i = 0; i < size; i++) {
			oids[i] = odb.store(new Function("function" + i));
		}
		assertEquals(size, odb.query(Function.class).count().longValue());

		for (int i = 0; i < size; i++) {
			odb.deleteObjectWithId(oids[i]);
		}

		assertEquals(0, odb.query(Function.class).count().longValue());

		for (int i = 0; i < size; i++) {
			oids[i] = odb.store(new Function("function" + i));
		}

		assertEquals(size, odb.query(Function.class).count().longValue());

		odb.close();

		odb = open(baseName);
		assertEquals(size, odb.getObjects(Function.class).size());
		odb.close();

	}

	/**
	 * creates 3 objects. Delete the 2th. And create 3 new objects
	 * 
	 * @throws Exception
	 */
    @Test
	public void test17() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		Function f3 = new Function("function2");
		odb.store(f1);
		odb.store(f2);
		odb.store(f3);
		assertEquals(3, odb.query(Function.class).count().longValue());

		odb.delete(f2);

		assertEquals(2, odb.query(Function.class).count().longValue());

		// odb.store(f1);
		odb.store(f2);
		// odb.store(f3);

		assertEquals(3, odb.query(Function.class).count().longValue());

		odb.close();

		odb = open(baseName);
		assertEquals(3, odb.getObjects(Function.class).size());
		odb.close();
	}

	/**
	 * creates 3 objects. commit. Creates 3 new . Delete the 2th commited. And
	 * create 3 new objects
	 * 
	 * @throws Exception
	 */
    @Test
	public void test18() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		Function f2 = new Function("function2");
		Function f3 = new Function("function2");
		ObjectOid oid1 = odb.store(f1);
		ObjectOid oid2 = odb.store(f2);
		ObjectOid oid3 = odb.store(f3);
		assertEquals(3, odb.query(Function.class).count().longValue());
		odb.close();

		odb = open(baseName);
		odb.deleteObjectWithId(oid2);

		assertEquals(2, odb.query(Function.class).count().longValue());

		// odb.store(f1);
		odb.store(new Function("f11"));
		odb.store(new Function("f12"));
		odb.store(new Function("f13"));
		// odb.store(f3);

		assertEquals(5, odb.query(Function.class).count().longValue());

		odb.close();

		odb = open(baseName);
		assertEquals(5, odb.getObjects(Function.class).size());
		odb.close();
	}

	/**
	 * Stores an object, closes the base. Loads the object, gets its oid and
	 * delete by oid.
	 * 
	 * @throws Exception
	 */
    @Test
	public void test19() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f1 = new Function("function1");
		odb.store(f1);
		odb.close();

		odb = open(baseName);
		Objects objects = odb.getObjects(Function.class);
		assertEquals(1, objects.size());

		Function f2 = (Function) objects.first();
		ObjectOid oid = odb.getObjectId(f2);

		odb.deleteObjectWithId(oid);
		assertEquals(0, odb.getObjects(Function.class).size());
		odb.close();

		odb = open(baseName);
		objects = odb.getObjects(Function.class);
		assertEquals(0, objects.size());

	}

	/**
	 * Stores on object and close database then Stores another object, commits
	 * without closing. Loads the object, gets its oid and delete by oid. In the
	 * case the commit has no write actions. And there was a bug : when there is
	 * no write actions, the commit process is much more simple! but in this the
	 * cache was not calling the transaction.clear and this was a reason for
	 * some connected/unconnected zone problem! (step14 of the turotial.)
	 * 
	 * @throws Exception
	 */
    @Test
	public void test20() throws Exception {
		String baseName = getBaseName();
		ODB odb = null;
		odb = open(baseName);
		Function f0 = new Function("1function0");
		odb.store(f0);
		odb.close();

		odb = open(baseName);
		Function f1 = new Function("function1");
		odb.store(f1);
		odb.commit();

		Objects objects = odb.query(Function.class, W.like("name", "func%")).objects();
		assertEquals(1, objects.size());

		Function f2 = (Function) objects.first();
		ObjectOid oid = odb.getObjectId(f2);

		odb.deleteObjectWithId(oid);
		assertEquals(1, odb.getObjects(Function.class).size());
		odb.close();

		odb = open(baseName);
		objects = odb.getObjects(Function.class);
		assertEquals(1, objects.size());

	}

	/**
	 * Bug when deleting the first object of unconnected zone when commited zone
	 * already have at least one object.
	 * 
	 * Detected running the polePosiiton Bahrain circuit.
	 * 
	 * @throws Exception
	 */
    @Test
	public void test21() throws Exception {
		ODB odb = null;
		String baseName = getBaseName();
		odb = open(baseName);
		Function f0 = new Function("function0");
		odb.store(f0);
		odb.close();

		odb = open(baseName);

		Function f1 = new Function("function1");
		odb.store(f1);

		Function f2 = new Function("function2");
		odb.store(f2);

		odb.delete(f1);
		odb.close();

		odb = open(baseName);

		Objects objects = odb.query(Function.class).objects();
		assertEquals(2, objects.size());
		odb.close();
	}

    @Test
	public void test22Last_toCheckDuration() throws Exception {
		long duration = OdbTime.getCurrentTimeInMs() - start;
		long d = 2200;
		if (!isLocal) {
			d = 2700;
		}
		println("duration=" + duration);
		if (testPerformance && duration > d) {

			fail("Duration is higher than " + d + " : " + duration);
		}
	}

}
