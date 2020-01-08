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
package org.neodatis.odb.test.insert;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.attribute.AllAttributeClass;
import org.neodatis.odb.test.vo.attribute.ObjectWithDates;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestInsert extends ODBTest {
	public TestInsert(String name) {
		super(name);
	}
	public void testCompositeCollection2DifferentObjects() throws Exception {
		ODB odb = open(getBaseName(),NeoDatis.getConfig());

		int nbUsers = odb.query(User.class).count().intValue();
		int nbProfiles = odb.query(Profile.class).count().intValue();
		int nbFunctions = odb.query(Function.class).count().intValue();

		Function login = new Function("login");
		Function logout = new Function("logout");
		Function disconnect = new Function("disconnect");
		List list = new ArrayList();
		list.add(login);
		list.add(logout);

		List list2 = new ArrayList();
		list.add(login);
		list.add(logout);
		Profile profile1 = new Profile("operator 1", list);
		Profile profile2 = new Profile("operator 2", list2);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile1);
		User userB = new User("Aisa Galvao Smadja", "aisa@neodatis.com", profile2);

		odb.store(user);
		odb.store(userB);

		Objects functions = odb.query(Function.class, W.like("name", "log%")).objects();
		Objects profiles = odb.query(Profile.class).objects();
		Objects users = odb.query(User.class, W.equal("name", "olivier smadja")).objects();

		
		// assertEquals(nbUsers+2,users.size());
		User user2 = (User) users.first();
		odb.commit();

		odb.close();
		

		assertEquals(user.toString(), user2.toString());
		assertEquals(nbProfiles + 2, profiles.size());
		assertEquals(nbFunctions + 2, functions.size());

	}
	public void testCompositeCollection1() throws Exception {

		ODB odb = open(getBaseName());

		Function login = new Function("login");

		List list = new ArrayList();
		list.add(login);

		Profile profile1 = new Profile("operator 1", list);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile1);

		odb.store(user);
		odb.close();

		odb = open(getBaseName());

		Objects users = odb.getObjects(User.class);

		// assertEquals(nbUsers+2,users.size());
		User user2 = (User) users.first();
		odb.close();

		assertEquals(user.toString(), user2.toString());

	}

	public void test1() throws Exception {
		String baseName = getBaseName();
		// LogUtil.allOn(true);
		ODB odb = open(baseName);

		// LogUtil.objectWriterOn(true);
		Function login = new Function("login");

		List list = new ArrayList();
		list.add(login);

		Profile profile1 = new Profile("operator 1", list);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile1);

		odb.store(user);
		odb.close();

		odb = open(baseName);

		Objects users = odb.getObjects(User.class);

		// assertEquals(nbUsers+2,users.size());
		User user2 = (User) users.first();
		odb.close();

		assertEquals(user.toString(), user2.toString());

	}

	public void testCompositeCollection2() throws Exception {
		// LogUtil.objectWriterOn(true);
		ODB odb = open(getBaseName());
		int nbUsers = odb.getObjects(User.class).size();
		int nbProfiles = odb.getObjects(Profile.class).size();
		int nbFunctions = odb.getObjects(Function.class).size();

		Function login = new Function("login");
		Function logout = new Function("logout");

		List list = new ArrayList();
		list.add(login);
		list.add(logout);

		Profile profile1 = new Profile("operator 1", list);
		Profile profile2 = new Profile("operator 2", list);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile1);
		User userB = new User("Aisa Galvao Smadja", "aisa@neodatis.com", profile2);

		odb.store(user);
		odb.store(userB);
		odb.close();

		odb = open(getBaseName());

		Objects users = odb.query(User.class,W.like("name", "olivier%")).objects();
		Objects profiles = odb.query(Profile.class).objects();
		Objects functions = odb.query(Function.class).objects();

		// assertEquals(nbUsers+2,users.size());
		User user2 = (User) users.first();
		odb.close();
		assertEquals(user.toString(), user2.toString());
		assertEquals(nbProfiles + 2, profiles.size());
		assertEquals(nbFunctions + 2, functions.size());
		

	}

	public void testCompositeCollection3() throws Exception {

		ODB odb = open(getBaseName());
		// Configuration.addLogId("ObjectWriter");
		// Configuration.addLogId("ObjectReader");
		// Configuration.addLogId("FileSystemInterface");

		int nbUsers = odb.getObjects(User.class).size();
		int nbProfiles = odb.getObjects(Profile.class).size();
		int nbFunctions = odb.getObjects(Function.class).size();

		Function login = new Function("login");
		Function logout = new Function("logout");

		List list = new ArrayList();
		list.add(login);
		list.add(logout);

		Profile profile1 = new Profile("operator 1", list);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile1);
		User userB = new User("Aisa Galvao Smadja", "aisa@neodatis.com", profile1);

		odb.store(user);
		odb.store(userB);
		odb.close();

		odb = open(getBaseName());

		Objects users = odb.query(User.class, W.like("name", "oli%sma%")).objects();
		Objects profiles = odb.query(Profile.class).objects();
		Objects functions = odb.query(Function.class).objects();

		// assertEquals(nbUsers+2,users.size());
		User user2 = (User) users.first();
		odb.close();
		assertEquals(user.toString(), user2.toString());
		assertEquals(nbProfiles + 1, profiles.size());
		assertEquals(nbFunctions + 2, functions.size());
		

	}

	public void testCompositeCollection4() throws Exception {
		ODB odb = open(getBaseName());

		int nbUsers = odb.query(User.class).objects().size();
		int nbProfiles = odb.query(Profile.class).objects().size();
		int nbFunctions = odb.query(Function.class).objects().size();

		Function login = new Function("login");
		Function logout = new Function("logout");

		List list = new ArrayList();
		list.add(login);
		list.add(logout);

		Profile profile1 = new Profile("operator 1", list);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile1);
		User userB = new User("Aisa Galvao Smadja", "aisa@neodatis.com", profile1);

		odb.store(user);
		odb.store(userB);
		

		Objects users = odb.query(User.class,W.equal("name", "olivier smadja")).objects();
		Objects profiles = odb.query(Profile.class).objects();
		Objects functions = odb.query(Function.class).objects();
		
		// assertEquals(nbUsers+2,users.size());
		User user2 = (User) users.first();
		odb.commit();
		odb.close();
		assertEquals(user.toString(), user2.toString());
		assertEquals(nbProfiles + 1, profiles.size());
		assertEquals(nbFunctions + 2, functions.size());
		
		odb = open(getBaseName());
		users = odb.query(User.class).objects();
		odb.close();
		assertEquals(nbUsers+2, users.size());

	}

	public void testSimple() throws Exception {

		ODB odb = open(getBaseName());

		int nbFunctions = odb.getObjects(Function.class).size();

		Function login = new Function("login");
		Function logout = new Function("logout");

		odb.store(login);
		odb.store(logout);
		odb.close();

		odb = open(getBaseName());
		Objects functions = odb.getObjects(Function.class);
		Function f1 = (Function) functions.first();
		f1.setName("login1");

		odb.store(f1);

		odb.close();

		try{
			odb = open(getBaseName());
			functions = odb.getObjects(Function.class);
			assertEquals(2, functions.size());
			assertEquals("login1", ((Function) functions.first()).getName());
		}finally{
			odb.close();
		}
	}

	public void testBufferSize() throws Exception {
		ODB odb = open(getBaseName());

		StringBuffer b = new StringBuffer();

		for (int i = 0; i < 1000; i++) {
			b.append("login - login ");
		}

		Function login = new Function(b.toString());
		Profile profile1 = new Profile("operator 1", login);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile1);

		odb.store(user);
		

		Objects users = odb.query(User.class).objects();
		Objects profiles = odb.query(Profile.class).objects();
		Objects functions = odb.query(Function.class).objects();
		
		// assertEquals(nbUsers+2,users.size());
		User user2 = (User) users.first();
		odb.commit();
		odb.close();

		assertEquals(user.toString(), user2.toString());
		assertEquals(b.toString(), user2.getProfile().getFunctions().iterator().next().toString());

	}

	public void testDatePersistence() throws Exception {
		ODB odb = null;
		try {
			odb = open(getBaseName());
			AllAttributeClass tc1 = new AllAttributeClass();
			tc1.setDate1(new Date());
			long t1 = tc1.getDate1().getTime();
			odb.store(tc1);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.getObjects(AllAttributeClass.class);
			assertEquals(1, l.size());
			AllAttributeClass tc2 = (AllAttributeClass) l.first();
			assertEquals(t1, tc2.getDate1().getTime());
			assertEquals(tc1.getDate1(), tc2.getDate1());

		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void testStringPersistence() throws Exception {
		ODB odb = null;
		try {
			odb = open(getBaseName());
			println(getBaseName());
			AllAttributeClass tc1 = new AllAttributeClass();
			tc1.setString1("");
			odb.store(tc1);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.query(AllAttributeClass.class).objects();
			assertEquals(1, l.size());
			AllAttributeClass tc2 = (AllAttributeClass) l.first();
			assertEquals("", tc2.getString1());
			assertEquals(null, tc2.getBigDecimal1());
			assertEquals(null, tc2.getDouble1());

		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void test6() throws Exception {
		ODB odb = open(getBaseName());

		Function login = new Function("login");
		Function logout = new Function("logout");
		ObjectOid oid1 = odb.store(login);
		ObjectOid oid2 =  odb.store(logout);

		println(oid1);
		println(oid2);
		odb.close();

		odb = open(getBaseName());
		Function login2 = new Function("login2");
		Function logout2 = new Function("logout2");
		oid1 =odb.store(login2);
		oid2 = odb.store(logout2);
		println(oid1);
		println(oid2);

		// select without committing
		Objects l = odb.getObjects(Function.class);
		assertEquals(4, l.size());
		// println(l);

		odb.close();

		odb = open(getBaseName());
		l = odb.getObjects(Function.class);
		odb.close();
		assertEquals(4, l.size());
		// println(l);
		
	}

	public void test7() throws Exception {
		ODB odb = open(getBaseName());

		Function login = new Function("login");
		Function logout = new Function("logout");
		odb.store(login);
		odb.store(logout);

		
		Function input = new Function("input");
		odb.store(input);
		odb.commit();
		odb.close();

		odb = open(getBaseName());
		Objects l = odb.getObjects(Function.class);
		
		// println(l);

		odb.close();
		assertEquals(3, l.size());
	}

	/**
	 * Test with java util Date and java sql Date
	 * 
	 */
	public void test8() {
		String baseName = getBaseName();
		println(baseName);

		ODB odb = null;

		Date utilDate = new Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime() + 10000);
		Timestamp timestamp = new Timestamp(utilDate.getTime() + 20000);

		try {
			odb = open(baseName);
			ObjectWithDates o = new ObjectWithDates("object1", utilDate, sqlDate, timestamp);
			odb.store(o);
			odb.close();

			odb = open(baseName);
			Objects<ObjectWithDates> dates = odb.getObjects(ObjectWithDates.class);

			ObjectWithDates o2 = dates.first();

			println(o2.getName());
			println(o2.getJavaUtilDate());
			println(o2.getJavaSqlDte());
			println(o2.getTimestamp());

			assertEquals("object1", o2.getName());
			assertEquals(utilDate, o2.getJavaUtilDate());
			assertEquals(sqlDate, o2.getJavaSqlDte());
			assertEquals(timestamp, o2.getTimestamp());

		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}
}
