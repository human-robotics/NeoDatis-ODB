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
package org.neodatis.odb.test.query.nq;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.nq.NativeQuery;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

public class TestNQQuery extends ODBTest {
	public static int NB_OBJECTS = 10;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp(String baseName) throws Exception {
		super.setUp();
		ODB odb = open(baseName);
		for (int i = 0; i < NB_OBJECTS; i++) {
			odb.store(new Function("function " + i));
			odb.store(new User("olivier " + i, "olivier@neodatis.org " + "1", new Profile("profile " + i, new Function("inner function "
					+ i))));
			odb.store(new User("olivier " + i, "olivier@neodatis.org " + "2", new Profile("profile " + i, new Function("inner function "
					+ i))));
			odb.store(new User("olivier " + i, "olivier@neodatis.org " + "3", new Profile("profile " + i, new Function("inner function "
					+ i))));
		}
		odb.close();
		// println("NbFunctions " + odb.count(Function.class));
		// println("NbUsers " + odb.count(User.class));

	}

	public void test1() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);
		Objects<Function> functions = odb.query(Function.class).objects();
		odb.close();

		odb = open(baseName);
		// println("TestNQQuery.test1:"+odb.getObjects (Function.class,true));
		Objects l = odb.query(new NativeQuery<Function>() {
			public boolean match(Function function) {
				return true;
			}
		}).objects();
		odb.close();
		assertFalse(l.isEmpty());
		assertEquals(NB_OBJECTS * 4, l.size());
	}

	public void test2() throws Exception {
		if (!isLocal) {
			return;
		}

		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		// println("++++TestNQQuery.test2:"+odb.getObjects
		// (Function.class,true));
		Objects l = odb.query(new NativeQuery<Function>() {
			public boolean match(Function function) {
				return function.getName().equals("function 5");
			}
		}).objects();
		odb.close();
		assertFalse(l.isEmpty());
		assertEquals(1, l.size());
	}

	public void test3() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Objects l = odb.query(new NativeQuery<User>() {
			public boolean match(User user) {
				return user.getProfile().getName().equals("profile 5");
			}
		}).objects();
		odb.close();
		assertFalse(l.isEmpty());
		assertEquals(3, l.size());
	}

	public void test4() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		NativeQuery query = new NativeQuery<User>() {
			public boolean match(User user) {
				return user.getProfile().getName().startsWith("profile");
			}
		};

		query.getQueryParameters().setStartIndex(0).setEndIndex(5);
		Objects l = odb.query(query).objects();
		odb.close();
		assertFalse(l.isEmpty());
		assertEquals(5, l.size());
	}

	public void test5() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		NativeQuery query = new NativeQuery<User>() {
			public boolean match(User user) {
				return user.getProfile().getName().startsWith("profile");
			}
		};
		query.getQueryParameters().setStartIndex(5).setEndIndex(6);
		Objects l = odb.query(query).objects();
		odb.close();
		assertFalse(l.isEmpty());
		assertEquals(1, l.size());

	}

	public void test6Ordering() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		NativeQuery query = new NativeQuery<User>() {
			public boolean match(User user) {
				return user.getProfile().getName().startsWith("profile") && user.getEmail().startsWith("olivier@neodatis.org 1");
			}
		};
		query.orderByAsc("name");

		Objects l = odb.query(query).objects();
		int i = 0;
		while (l.hasNext()) {
			User user = (User) l.next();
			assertEquals("olivier " + i, user.getName());
			// println(user.getName());
			i++;
		}

		odb.close();
	}

	public void test7Ordering() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		NativeQuery query = new NativeQuery<User>() {
			public boolean match(User user) {
				return user.getProfile().getName().startsWith("profile") && user.getEmail().startsWith("olivier@neodatis.org 2");
			}
		};
		query.orderByDesc("name");

		Objects l = odb.query(query).objects();
		int i = l.size() - 1;
		while (l.hasNext()) {
			User user = (User) l.next();
			assertEquals("olivier " + i, user.getName());
			// println(user.getName());
			i--;
		}

		odb.close();
	}

	public void test8Ordering() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		NativeQuery query = new NativeQuery<User>() {
			public boolean match(User user) {
				return user.getProfile().getName().startsWith("profile");
			}
		};
		query.orderByAsc("name,email");

		Objects l = odb.query(query).objects();
		int i = 0;
		while (l.hasNext()) {
			User user = (User) l.next();
			// println(user.getName() + " / " + user.getEmail());
			assertEquals("olivier " + i / 3, user.getName());
			assertEquals("olivier@neodatis.org " + ((i % 3) + 1), user.getEmail());

			i++;
		}

		odb.close();
	}

	public void test9Ordering() throws Exception {
		if (!isLocal) {
			return;
		}
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		NativeQuery query = new NativeQuery<User>() {
			public boolean match(User user) {
				return user.getProfile().getName().startsWith("profile");
			}
		};
		query.orderByDesc("name,email");

		Objects l = odb.query(query).objects();
		int i = l.size() - 1;
		;
		while (l.hasNext()) {
			User user = (User) l.next();
			// println(user.getName() + " / " + user.getEmail());
			assertEquals("olivier " + i / 3, user.getName());
			assertEquals("olivier@neodatis.org " + ((i % 3) + 1), user.getEmail());

			i--;
		}

		odb.close();
	}

}
