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
package org.neodatis.odb.test.crossSessionCache;

import org.junit.Test;
import org.neodatis.odb.*;
import org.neodatis.odb.core.session.cross.CacheFactory;
import org.neodatis.odb.core.session.cross.CrossSessionCache;
import org.neodatis.odb.core.session.cross.ICrossSessionCache;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;
import org.neodatis.odb.tool.MemoryMonitor;

public class TestInsertWithCrossSessionCache extends ODBTest {
    @Test
	public void test1() throws Exception {

		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODB odb = open(baseName,config);

		Function login = new Function("login");
		Function logout = new Function("logout");
		OID oid1 = odb.store(login);
		OID oid2 = odb.store(logout);

		odb.close();
		ICrossSessionCache cache = CacheFactory.getCrossSessionCache(odb.getName());

		assertEquals(oid1, cache.getOid(login));
		assertEquals(oid2, cache.getOid(logout));
	}
    @Test
	public void testDisconnect() throws Exception {
		NeoDatisConfig config = NeoDatis.getConfig();
		
		// Automatical reconnect off
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Function login = new Function("login");
		Function logout = new Function("logout");
		OID oid1 = odb.store(login);
		OID oid2 = odb.store(logout);


		odb.close();

		odb = open(baseName);
		Objects objects = odb.query(Function.class).objects();
		assertEquals(2, objects.size());
		Function f = (Function) objects.first();
		odb.disconnect(f);
		// Storing after disconnect should create a new one
		ObjectOid oid3 = odb.store(f);
		odb.close();

		odb = open(baseName);
		objects = odb.query(Function.class).objects();
		odb.close();
		println(objects.size() + " objects");

		assertEquals(3, objects.size());

		odb = open(baseName);
		
		f.setName("This is a reconnected function!");
		odb.store(f);
		odb.close();

		odb = open(baseName);
		objects = odb.query(Function.class).objects();
		Function ff = (Function) odb.getObjectFromId(oid3);
		odb.close();
		assertEquals(4, objects.size());
		assertEquals("login", ff.getName());
		println(objects.size() + " objects");

	}
    @Test
	public void testReconnect() throws InterruptedException {

		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		
		String baseName = getBaseName();
		ODB odb = open(baseName,config);

		Function f1 = new Function("f1");
		odb.store(f1);
		odb.close();

		odb = open(baseName);
		f1.setName("Function 1");
		odb.store(f1);
		odb.close();

		odb = open(baseName);
		Objects os = odb.getObjects(Function.class);
		assertEquals(1, os.size());
		Function ff1 = (Function) os.first();
		odb.close();

		assertEquals("Function 1", ff1.getName());

	}
    @Test
	public void testReconnectXXFunctions() {

		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		
		String baseName = getBaseName();
		ODB odb = open(baseName,config);

		Function f1 = new Function("f1");
		odb.store(f1);
		odb.close();

		for (int i = 0; i < 1000; i++) {
			odb = open(baseName);
			f1.setName("Function " + i);
			odb.store(f1);
			odb.close();

			odb = open(baseName);
			assertEquals("Function " + i, f1.getName());
			assertEquals(1, odb.query(Function.class).count().longValue());
			odb.close();
		}
	}
    @Test
	public void testAutoReconnectXXFunctions() {

		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODB odb = open(baseName,config);

		Function f1 = new Function("f1");
		odb.store(f1);
		odb.close();

		for (int i = 0; i < 1000; i++) {
			odb = open(baseName);
			f1.setName("Function " + i);
			odb.store(f1);
			odb.close();

			odb = open(baseName);
			assertEquals("Function " + i, f1.getName());
			assertEquals(1, odb.query(Function.class).objects().size());
			odb.close();
		}
	}
    @Test
	public void testReconnectUser() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		
		String baseName = getBaseName();
		ODB odb = open(baseName,config);

		User user1 = new User("user1", "user@neodatis.org", new Profile("profile1", new Function("f1")));
		odb.store(user1);
		odb.close();

		odb = open(baseName);
		user1.setName("USER 11");
		odb.store(user1);
		odb.close();

		odb = open(baseName);
		Objects os = odb.getObjects(User.class);
		assertEquals(1, os.size());
		User uu1 = (User) os.first();
		odb.close();

		assertEquals("USER 11", uu1.getName());

	}
    @Test
	public void testReconnectXXUsers() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODB odb = open(baseName,config);
		User user1 = new User("user1", "user@neodatis.org", new Profile("profile1", new Function("f1")));
		ObjectOid oid = odb.store(user1);
		odb.close();
		ICrossSessionCache cache = CacheFactory.getCrossSessionCache(odb.getName());
		for (int i = 0; i < 1000; i++) {
			if(i%1000==0){
				println(String.format("i=%d  ,  cache size=%s", i, cache.toString()));
			}
			odb = open(baseName);
			user1.setName("USER " + i);
			odb.store(user1);
			odb.close();

			// check value
			odb = open(baseName);
			User u = (User) odb.getObjectFromId(oid);
			assertEquals("USER " + i, u.getName());
			assertEquals(1, odb.query(User.class).count().longValue());
			assertEquals(1, odb.query(Profile.class).count().longValue());
			assertEquals(1, odb.query(Function.class).count().longValue());
			odb.close();
		}
	}

	/**
	 * to test automatical reconnect
	 * 
	 */
    @Test
	public void testAutoReconnectXXUsers() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODB odb = open(baseName,config);

		User user1 = new User("user1", "user@neodatis.org", new Profile("profile1", new Function("f1")));
		ObjectOid oid = odb.store(user1);
		odb.close();

		for (int i = 0; i < 1000; i++) {
			odb = open(baseName);
			user1.setName("USER " + i);
			odb.store(user1);
			odb.close();

			// check value
			odb = open(baseName);
			User u = (User) odb.getObjectFromId(oid);
			assertEquals("USER " + i, u.getName());
			assertEquals(1, odb.query(User.class).count().longValue());
			assertEquals(1, odb.query(Profile.class).count().longValue());
			assertEquals(1, odb.query(Function.class).count().longValue());
			odb.close();
		}
	}
    @Test
	public void testAutoReconnectXXUsersWithNullProfile() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODB odb = open(baseName,config);

		User user1 = new User("user1", "user@neodatis.org", new Profile("profile1", new Function("f1")));
		ObjectOid oid = odb.store(user1);
		odb.close();

		for (int i = 0; i < 1000; i++) {
			odb = open(baseName);
			user1.setName("USER " + i);
			odb.store(user1);
			odb.close();

			// check value
			odb = open(baseName);
			User u = (User) odb.getObjectFromId(oid);
			assertEquals("USER " + i, u.getName());
			assertEquals(1, odb.query(User.class).count().longValue());
			assertEquals(1, odb.query(Profile.class).count().longValue());
			assertEquals(1, odb.query(Function.class).count().longValue());
			odb.close();
		}
	}
    @Test
	public void testAutoReconnectXXUsersNoModificationWithClose() throws InterruptedException {

		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		CrossSessionCache.clearAll();
		String baseName = getBaseName();
		ODB odb = open(baseName,config);

		User user1 = new User("user1", "user@neodatis.org", null);
		ObjectOid oid = odb.store(user1);
		odb.close();
		

		for (int i = 0; i < 1000; i++) {
			odb = open(baseName);
			odb.store(user1);
			odb.close();

			// check value
			odb = open(baseName);
			User u = (User) odb.getObjectFromId(oid);
			assertEquals("user1", u.getName());
			assertEquals(1, odb.query(User.class).count().longValue());
			assertEquals(0, odb.query(Profile.class).count().longValue());
			assertEquals(0, odb.query(Function.class).count().longValue());
			odb.close();
		}
	}
    @Test
	public void testAutoReconnectXXUsersNoModificationWithCommit() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODB odb = open(baseName,config);

		User user1 = new User("user1", "user@neodatis.org", null);
		ObjectOid oid = odb.store(user1);
		odb.close();

		odb = open(baseName);
		for (int i = 0; i < 1000; i++) {
			
			odb.store(user1);
			odb.commit();
			if (i % 500 == 0) {
				MemoryMonitor.displayCurrentMemory(""+i, false);
			}

			User u = (User) odb.getObjectFromId(oid);
			assertEquals("user1", u.getName());
			assertEquals(1, odb.query(User.class).count().longValue());
			assertEquals(0, odb.query(Profile.class).count().longValue());
			assertEquals(0, odb.query(Function.class).count().longValue());
			
		}
		odb.close();

	}
    @Test
	public void testCacheWithSameDb() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		
		if(!testKnownProblems){
			return;
		}

		
		String baseName = getBaseName();
		ODB odb = open(baseName,config);
		Function f = new Function("function");
		OID oid = odb.store(f);
		odb.close();

		// delete database, but cross session cache is not cleared => object is still in the cache
		odb = open(baseName);
		try{
			odb.store(f);
		}catch (Exception e) {
			e.printStackTrace();
			fail("it should have worked. It failed because of the cross session cache that kept the object reference");
		}

		odb.close();

	}
    @Test
	public void t1() {
		String s = "neodatis";
		assertTrue(s.startsWith("neodatis"));
	}
	public static void main(String[] args) throws InterruptedException {
		TestInsertWithCrossSessionCache t = new TestInsertWithCrossSessionCache();
		t.testAutoReconnectXXUsersNoModificationWithClose();
	}
}
