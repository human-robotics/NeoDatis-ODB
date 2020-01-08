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
package org.neodatis.odb.test.query.criteria;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;
import org.neodatis.tool.wrappers.OdbTime;

import java.util.ArrayList;
import java.util.List;

public class TestCriteriaQuery3 extends ODBTest {

	public void test1() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query query = odb.query(User.class, W.equal("profile.name", "profile2"));
		Objects l = query.objects();
		assertEquals(1, l.size());
		odb.close();
	}

	

	public void testQueryQueryWithObject() throws Exception {
		String baseName = getBaseName();
		//setUp(baseName);
		ODB odb = open(baseName);

		Profile p0 = new Profile("profile0");
		p0.addFunction(null);
		p0.addFunction(new Function("f1"));
		p0.addFunction(new Function("f2"));

		Profile p1 = new Profile("profile1");
		p1.addFunction(null);
		p1.addFunction(new Function("f12"));
		p1.addFunction(new Function("f22"));
		User user = new User("The user", "themail", p0);
		User user2 = new User("The user2", "themail2", p1);
		odb.store(user);
		odb.store(user2);
		odb.close();

		odb = open(baseName);

		Objects<Profile> profiles = odb.query(Profile.class,W.equal("name", "profile0")).objects();
		Profile pp = profiles.first();
		Query query = odb.query(User.class, W.equal("profile", pp));
		Objects l = query.objects();
		assertEquals(1, l.size());
		user = (User) l.first();
		assertEquals("The user", user.getName());

		odb.close();

	}
	
	public void testQueryQueryWithValueInList() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Profile p0 = new Profile("profile0");
		p0.addFunction(null);
		p0.addFunction(new Function("f1"));
		p0.addFunction(new Function("f2"));

		Profile p1 = new Profile("profile1");
		p1.addFunction(null);
		p1.addFunction(new Function("f12"));
		p1.addFunction(new Function("f22"));
		User user = new User("The user", "themail", p0);
		User user2 = new User("The user2", "themail2", p1);
		odb.store(user);
		odb.store(user2);
		odb.close();

		odb = open(baseName);

		Function f2bis = (Function) odb.query(Function.class,W.equal("name", "f2")).objects().first();
		Query query = odb.query(User.class, W.contain("profile.functions", f2bis));
		Objects l = query.objects();
		assertEquals(1, l.size());
		user = (User) l.first();
		assertEquals("The user", user.getName());

		odb.close();

	}

	public void testQueryQueryWithValueInList3() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Profile p0 = new Profile("profile0");
		p0.addFunction(null);
		p0.addFunction(null);
		p0.addFunction(null);

		Profile p1 = new Profile("profile1");
		p1.addFunction(null);
		p1.addFunction(null);
		p1.addFunction(new Function("f22"));
		User user = new User("The user", "themail", p0);
		User user2 = new User("The user2", "themail2", p1);
		odb.store(user);
		odb.store(user2);
		odb.close();

		odb = open(baseName);

		Function f2bis = (Function) odb.query(Function.class, W.equal("name", "f22")).objects().first();
		Query query = odb.query(User.class, W.contain("profile.functions", f2bis));
		Objects l = query.objects();
		assertEquals(1, l.size());
		user = (User) l.first();
		assertEquals("The user2", user.getName());

		odb.close();

	}

	public void testQueryQueryWithValueInList2() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Profile p0 = new Profile("profile0");
		p0.addFunction(new Function("f1"));
		p0.addFunction(new Function("f2"));

		Profile p1 = new Profile("profile1");
		p0.addFunction(new Function("f12"));
		p0.addFunction(new Function("f22"));
		User user = new User("The user", "themail", p0);
		User user2 = new User("The user2", "themail2", p1);
		odb.store(user);
		odb.store(user2);
		odb.close();

		odb = open(baseName);

		Function f2bis = (Function) odb.query(Function.class,W.equal("name", "f2")).objects().first();
		Query query = odb.query(Profile.class, W.contain("functions", f2bis));
		Objects<Profile> l = query.objects();
		assertEquals(1, l.size());
		p1 = l.first();
		assertEquals("profile0", p1.getName());

		odb.close();

	}

	public void testQueryQueryWithValueInList2_with_null_object() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Profile p0 = new Profile("profile0");
		p0.addFunction(new Function("f1"));
		p0.addFunction(new Function("f2"));

		Profile p1 = new Profile("profile1");
		p0.addFunction(new Function("f12"));
		p0.addFunction(new Function("f22"));
		User user = new User("The user", "themail", p0);
		User user2 = new User("The user2", "themail2", p1);
		odb.store(user);
		odb.store(user2);
		odb.close();

		odb = open(baseName);

		Function f2bis = new Function("f2");
		Query query = odb.query(Profile.class, W.contain("functions", null));
		Objects<Profile> l = query.objects();
		assertEquals(1, l.size());
		p1 = l.first();
		assertEquals("profile1", p1.getName());

		odb.close();

	}

	public void testListSize0() throws Exception {

		ODB odb = null;
		String baseName = getBaseName();
		setUp(baseName);

		try {
			odb = open(baseName);
			Query query = odb.query(User.class, W.sizeEq("profile.functions", 0));
			Objects l = query.objects();
			assertEquals(1, l.size());
			User u = (User) l.first();
			assertEquals("profile no function", u.getProfile().getName());
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void testListSize4() throws Exception {

		ODB odb = null;
		String baseName = getBaseName();
		setUp(baseName);

		try {
			odb = open(baseName);
			Query query = odb.query(User.class, W.sizeEq("profile.functions", 4));
			Objects l = query.objects();
			assertEquals(1, l.size());
			User u = (User) l.first();
			assertEquals("big profile", u.getProfile().getName());
			assertEquals(4, u.getProfile().getFunctions().size());
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void testListSize1() throws Exception {

		ODB odb = null;
		String baseName = getBaseName();
		setUp(baseName);

		try {
			odb = open(baseName);
			Query query = odb.query(User.class, W.sizeEq("profile.functions", 1));
			Objects l = query.objects();
			assertEquals(10, l.size());
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void testListSizeGt2() throws Exception {

		ODB odb = null;
		String baseName = getBaseName();
		setUp(baseName);

		try {
			odb = open(baseName);
			Query query = odb.query(User.class, W.sizeGt("profile.functions", 2));
			Objects l = query.objects();
			assertEquals(1, l.size());
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void testListSizeNotEqulTo1() throws Exception {

		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = null;

		try {
			odb = open(baseName);
			Query query = odb.query(User.class, W.sizeNe("profile.functions", 1));
			Objects l = query.objects();
			assertEquals(2, l.size());
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void setUp(String baseName) throws Exception {
		ODB odb = open(baseName);
		long start = OdbTime.getCurrentTimeInMs();
		int size = 10;
		for (int i = 0; i < size; i++) {
			User u = new User("user" + i, "email" + i, new Profile("profile" + i, new Function("function " + i)));
			odb.store(u);
		}

		User user = new User("big user", "big email", new Profile("big profile", new Function("big function 1")));
		user.getProfile().addFunction(new Function("big function 2"));
		user.getProfile().addFunction(new Function("big function 3"));
		user.getProfile().addFunction(new Function("big function 4"));
		odb.store(user);

		user = new User("user no function", "email no function", new Profile("profile no function"));
		odb.store(user);

		odb.close();
	}

	public void testQueryQueryWithValueInList4() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		List<String> strings = new ArrayList<String>();
		ClassWithListOfString c = new ClassWithListOfString("name",strings);
		c.getStrings().add("s1");
		c.getStrings().add("s2");
		c.getStrings().add("s3");
		
		List<String> strings2 = new ArrayList<String>();
		ClassWithListOfString c2 = new ClassWithListOfString("name",strings2);
		c2.getStrings().add("s1");
		c2.getStrings().add("s2");
		c2.getStrings().add("s3");
		
		odb.store(c);
		odb.store(c2);
		odb.close();

		odb = open(baseName);

		Query query = odb.query(ClassWithListOfString.class, W.contain("strings", "s2222"));
		Objects<ClassWithListOfString> l = query.objects();
		assertEquals(0, l.size());
		odb.close();

		

	}
	
	public void testQueryQueryWithValueInList41() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName, NeoDatis.getConfig().setDebugLayers(false));

		List<String> strings = new ArrayList<String>();
		ClassWithListOfString c = new ClassWithListOfString("name",strings);
		c.getStrings().add("s1");
		
		odb.store(c);
		odb.close();

		odb = open(baseName,NeoDatis.getConfig().setDebugLayers(true));

		Query query = odb.query(ClassWithListOfString.class);
		Objects<ClassWithListOfString> l = query.objects();
		assertEquals(1, l.size());
		odb.close();

		

	}
	
	public void testQueryQueryWithValueInList5() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		List<String> strings = new ArrayList<String>();
		ClassWithListOfString c = new ClassWithListOfString("name",strings);
		c.getStrings().add("s1");
		c.getStrings().add(null);
		c.getStrings().add("s3");
		
		List<String> strings2 = new ArrayList<String>();
		ClassWithListOfString c2 = new ClassWithListOfString("name",null);
		
		
		odb.store(c);
		odb.store(c2);
		odb.close();

		odb = open(baseName);

		Query query = odb.query(ClassWithListOfString.class, W.contain("strings", null));
		Objects<ClassWithListOfString> l = odb.getObjects(query);
		odb.close();
		assertEquals(1, l.size());
		

		

	}
	
	public void testQueryQueryWithValueInList6() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		List<String> strings = new ArrayList<String>();
		ClassWithListOfString c = new ClassWithListOfString("name",strings);
		c.getStrings().add("s1");
		c.getStrings().add(null);
		c.getStrings().add("s3");
		
		List<String> strings2 = new ArrayList<String>();
		ClassWithListOfString c2 = new ClassWithListOfString("name",null);
		
		
		odb.store(c);
		odb.store(c2);
		odb.close();

		odb = open(baseName);

		Query query = odb.query(ClassWithListOfString.class, W.contain("strings", "s4"));
		Objects<ClassWithListOfString> l = odb.getObjects(query);
		odb.close();
		assertEquals(0, l.size());
		

		

	}
}
