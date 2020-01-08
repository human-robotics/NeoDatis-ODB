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
package org.neodatis.odb.test.update;

import org.neodatis.odb.*;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.core.query.nq.NativeQuery;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;
import org.neodatis.tool.wrappers.OdbTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestUpdate extends ODBTest {
	public static int NB_OBJECTS = 50;
	private static boolean first = true;

	public TestUpdate(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp(String baseName) throws Exception {
		super.setUp();
		ODB odb = open(baseName);
		for (int i = 0; i < NB_OBJECTS; i++) {
			odb.store(new Function("function " + (i + i)));
			odb.store(new User("olivier " + i, "olivier@neodatis.com " + i,
					new Profile("profile " + i, new Function("inner function " + i))));
		}
		odb.close();

		odb = open(baseName);
		Objects l = odb.query(Function.class).objects();
		println(l.size());
		assertEquals(2 * NB_OBJECTS, l.size());
		odb.close();

	}

	public void test1() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query query = odb.query(Function.class, W.equal("name", "function 10"));
		Objects l = odb.getObjects(query);
		int size = l.size();
		assertFalse(l.isEmpty());
		Function f = (Function) l.first();
		OID id = odb.getObjectId(f);

		assertEquals("function 10", f.getName());
		String newName = String.valueOf(OdbTime.getCurrentTimeInMs());
		f.setName(newName);
		odb.store(f);
		odb.close();

		odb = open(baseName);

		l = odb.getObjects(query);

		query = odb.query(Function.class, W.equal("name", newName));

		assertTrue(size == l.size() + 1);

		l = odb.getObjects(query);

		assertFalse(l.isEmpty());
		assertEquals(1, l.size());
		assertEquals(id, odb.getObjectId(l.first()));
		odb.close();

	}

	public void test2() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);

		ODB odb = open(baseName);
		int nbProfiles = odb.getObjects(Profile.class).size();
		Query query = odb.query(User.class, W.equal("profile.name", "profile 10"));
		Objects l = odb.getObjects(query);
		int size = l.size();

		assertFalse(l.isEmpty());
		User u = (User) l.first();

		assertEquals("profile 10", u.getProfile().getName());
		Profile p2 = u.getProfile();
		final String newName = String.valueOf(OdbTime.getCurrentTimeInMs()) + "-";
		p2.setName(newName);
		odb.store(p2);
		odb.close();

		odb = open(baseName);

		l = odb.getObjects(query);

		assertTrue(l.size() == size - 1);

		if (!isLocal) {
			query = odb.query(User.class, W.equal("profile.name", newName));
		} else {
			query = new NativeQuery<User>() {
				public boolean match(User user) {
					return user.getProfile().getName().equals(newName);
				}
			};
		}

		l = odb.query(query).objects();

		assertFalse(l.isEmpty());
		Query q = odb.query(Profile.class);
		q.getQueryParameters().setInMemory(false);
		l = odb.getObjects(q);
		assertEquals(nbProfiles, l.size());
		odb.close();

	}

	public void test3() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);

		ODB odb = open(baseName);
		Query pquery = odb.query(Profile.class, W.equal("name", "profile 10"));
		long nbProfiles = odb.query(Profile.class).count().longValue();
		long nbProfiles10 = odb.getObjects(pquery).size();
		Query query = odb.query(User.class, W.equal("profile.name", "profile 10"));

		Objects l = odb.getObjects(query);
		int size = l.size();
		assertFalse(l.isEmpty());
		User u = (User) l.first();

		assertEquals("profile 10", u.getProfile().getName());
		final String newName = String.valueOf(OdbTime.getCurrentTimeInMs()) + "+";
		Profile p2 = u.getProfile();
		p2.setName(newName);
		odb.store(u);
		odb.close();

		odb = open(baseName);

		l = odb.query(query).objects();

		assertEquals(l.size() + 1, size);
		assertEquals(nbProfiles10, odb.query(pquery).objects().size() + 1);

		if (!isLocal) {
			query = odb.query(User.class, W.equal("profile.name", newName));
		} else {
			query = new NativeQuery<User>() {
				public boolean match(User user) {
					return user.getProfile().getName().equals(newName);
				}
			};
		}

		l = odb.query(query).objects();

		assertEquals(1, l.size());
		Query q = odb.query(Profile.class);
		q.getQueryParameters().setInMemory(false);
		l = odb.getObjects(q);
		assertEquals(nbProfiles, l.size());
		odb.close();

	}

	public void test4() throws Exception {
		String baseName = getBaseName();

		NeoDatisConfig config = NeoDatis.getConfig().setMaxNumberOfObjectInCache(10);
		ODB odb = open(baseName, config);
		try {
			List list = new ArrayList();
			for (int i = 0; i < 15; i++) {
				Function function = new Function("function " + i);
				try {
					odb.store(function);
				} catch (Exception e) {
					odb.rollback();
					odb.close();
					assertTrue(e.getMessage().indexOf("Cache is full!") != -1);
					return;
				}
				list.add(function);
			}
			odb.close();

			odb = open(baseName);
			Objects l = odb.getObjects(Function.class);
			l.next();
			l.next();
			odb.store(l.next());
			odb.close();

			odb = open(baseName);
			assertEquals(15, odb.query(Function.class).count().longValue());
			odb.close();

		} finally {
		}

	}

	public void test5() throws Exception {
		String baseName = getBaseName();

		try {
			ODB odb = open(baseName);
			List list = new ArrayList();
			for (int i = 0; i < 15; i++) {
				Function function = new Function("function " + i);
				odb.store(function);
				list.add(function);
			}
			odb.close();

			NeoDatisConfig config = NeoDatis.getConfig().setMaxNumberOfObjectInCache(15);
			odb = open(baseName);
			Query query = odb.query(Function.class, W.or().add(W.like("name", "%9")).add(W.like("name", "%8")));
			Objects l = odb.getObjects(query, false);

			assertEquals(2, l.size());
			l.next();
			odb.store(l.next());
			odb.close();

			odb = open(baseName);
			assertEquals(15, odb.query(Function.class).count().longValue());
			odb.close();
		} finally {
		}

	}

	public void test6() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);

		MyObject mo = null;
		ODB odb = open(baseName);
		mo = new MyObject(15, "oli");
		mo.setDate(new Date());
		odb.store(mo);
		odb.close();

		odb = open(baseName);
		MyObject mo2 = (MyObject) odb.getObjects(MyObject.class).first();
		mo2.setDate(new Date(mo.getDate().getTime() + 10));
		mo2.setSize(mo.getSize() + 1);
		odb.store(mo2);
		odb.close();

		odb = open(baseName);
		MyObject mo3 = (MyObject) odb.getObjects(MyObject.class).first();
		assertEquals(mo3.getDate().getTime(), mo2.getDate().getTime());
		assertTrue(mo3.getDate().getTime() > mo.getDate().getTime());
		assertTrue(mo3.getSize() == mo.getSize() + 1);
		odb.close();

		// println("before:" + mo.getDate().getTime() + " - " + mo.getSize());
		// println("after:" + mo3.getDate().getTime() + " - " + mo3.getSize());

	}

	/**
	 * When an object an a collection attribute, and this collection is changed
	 * (adding one object),no update in place is possible for instance.
	 * 
	 * @throws Exception
	 */
	public void test7() throws Exception {
		String baseName = getBaseName();
		NeoDatisConfig config = NeoDatis.getConfig().setOidGeneratorUseCache(false);
		println(baseName);
		ODB odb = open(baseName,config);

		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);

		odb.store(user);
		odb.close();

		odb = open(baseName, config);
		User user2 = (User) odb.query(User.class).objects().first();
		user2.getProfile().addFunction(new Function("new Function"));
		odb.store(user2);
		odb.close();

		odb = open(baseName,config);
		User user3 = (User) odb.query(User.class).objects().first();
		assertEquals(2, user3.getProfile().getFunctions().size());
		Function f1 = (Function) user3.getProfile().getFunctions().get(0);
		Function f2 = (Function) user3.getProfile().getFunctions().get(1);
		assertEquals("login", f1.getName());
		assertEquals("new Function", f2.getName());
		odb.close();

	}

	/**
	 * setting one attribute to null
	 * 
	 * 
	 * @throws Exception
	 */
	public void test8() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);

		ODB odb = open(baseName);

		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);

		odb.store(user);
		odb.close();

		odb = open(baseName);
		User user2 = (User) odb.query(User.class).objects().first();
		user2.setProfile(null);
		odb.store(user2);
		odb.close();

		odb = open(baseName);
		User user3 = (User) odb.query(User.class).objects().first();
		assertNull(user3.getProfile());
		odb.close();
	}

	/** Test updaing a non native attribute with a new non native object */
	public void testUpdateObjectReference() throws Exception {
		String baseName = getBaseName();

		ODB odb = open(baseName);

		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);

		odb.store(user);
		odb.close();

		Profile profile2 = new Profile("new operator", function);

		odb = open(baseName);
		User user2 = (User) odb.getObjects(User.class).first();
		user2.setProfile(profile2);
		odb.store(user2);
		odb.close();

		odb = open(baseName);
		user2 = (User) odb.getObjects(User.class).first();
		assertEquals("new operator", user2.getProfile().getName());
		assertEquals(2, odb.getObjects(Profile.class).size());
		odb.close();

	}

	/**
	 * Test updaing a non native attribute with an already existing non native
	 * object - with commit
	 */
	public void testUpdateObjectReference2() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);

		odb.store(user);
		odb.close();

		Profile profile2 = new Profile("new operator", function);

		odb = open(baseName);
		odb.store(profile2);
		odb.close();

		odb = open(baseName);
		profile2 = (Profile) odb.query(Profile.class, W.equal("name", "new operator")).objects().first();
		User user2 = (User) odb.getObjects(User.class).first();

		user2.setProfile(profile2);
		odb.store(user2);
		odb.close();

		odb = open(baseName);
		user2 = (User) odb.getObjects(User.class).first();
		assertEquals("new operator", user2.getProfile().getName());
		assertEquals(2, odb.getObjects(Profile.class).size());
		odb.close();
	}

	/**
	 * Test updating a non native attribute with an already existing non native
	 * object without comit
	 */
	public void testUpdateObjectReference3() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);

		odb.store(user);
		odb.close();

		Profile profile2 = new Profile("new operator", function);

		odb = open(baseName);
		odb.store(profile2);

		User user2 = (User) odb.getObjects(User.class).first();

		user2.setProfile(profile2);
		odb.store(user2);
		odb.close();

		odb = open(baseName);
		user2 = (User) odb.getObjects(User.class).first();
		assertEquals("new operator", user2.getProfile().getName());
		assertEquals(2, odb.getObjects(Profile.class).size());
		odb.close();

	}

	/**
	 * Test updating a non native attribute than wall null with an already
	 * existing non native object without comit
	 */
	public void testUpdateObjectReference4() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Function function = new Function("login");
		User user = new User("olivier smadja", "olivier@neodatis.com", null);

		odb.store(user);
		odb.close();

		Profile profile2 = new Profile("new operator", function);

		odb = open(baseName);
		odb.store(profile2);

		User user2 = (User) odb.getObjects(User.class).first();

		user2.setProfile(profile2);
		odb.store(user2);
		odb.close();

		odb = open(baseName);
		user2 = (User) odb.getObjects(User.class).first();
		assertEquals("new operator", user2.getProfile().getName());
		assertEquals(1, odb.getObjects(Profile.class).size());
		odb.close();
	}

	public void testDirectSave() throws Exception {
		if (!isLocal) {
			return;
		}

		String baseName = getBaseName();
		setUp(baseName);

		ODB odb = open(baseName);

		Function function = new Function("f1");
		odb.store(function);
		for (int i = 0; i < 2; i++) {
			function.setName(function.getName() + function.getName() + function.getName() + function.getName());
			odb.store(function);
		}
		SessionEngine engine = Dummy.getEngine(odb);
		if (isLocal) {

		}
		ClassInfo ci = engine.getSession().getMetaModel().getClassInfo(Function.class.getName(), true);
		println(ci);
		odb.close();
	}

	public void testUpdateRelation() throws Exception {
		String baseName = getBaseName();

		ODB odb = open(baseName);

		// first create a function
		Function f = new Function("f1");
		odb.store(f);
		odb.close();

		odb = open(baseName);

		// reloads the function
		Objects<Function> functions = odb.query(Function.class, W.equal("name", "f1")).objects();
		Function f1 = functions.first();

		// Create a profile with the loaded function
		Profile profile = new Profile("test", f1);

		odb.store(profile);
		odb.close();

		odb = open(baseName);
		Objects<Profile> profiles = odb.getObjects(Profile.class);
		functions = odb.getObjects(Function.class);

		odb.close();
		deleteBase(baseName);
		assertEquals(1, functions.size());
		assertEquals(1, profiles.size());
	}
}
