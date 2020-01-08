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

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

import java.util.ArrayList;
import java.util.List;

public class TestSimpleUpdateObject extends ODBTest {

	public void test1() throws Exception {
		ODB odb = open(getBaseName());

		Function login = new Function("login");
		Function logout = new Function("logout");
		odb.store(login);
		println("--------");
		odb.store(login);
		odb.store(logout);

		// odb.commit();
		odb.close();

		odb = open(getBaseName());

		Objects l = odb.getObjects(Function.class);
		Function f2 = (Function) l.first();
		f2.setName("login function");
		odb.store(f2);
		odb.close();
		ODB odb2 = open(getBaseName());
		Function f = (Function) odb2.getObjects(Function.class).first();
		assertEquals("login function", f.getName());
		odb2.close();

	}

	public void test2() throws Exception {

		ODB odb = open(getBaseName());

		int nbUsers = odb.getObjects(User.class).size();
		int nbProfiles = odb.getObjects(Profile.class).size();
		int nbFunctions = odb.getObjects(Function.class).size();

		Function login = new Function("login");
		Function logout = new Function("logout");
		List list = new ArrayList();
		list.add(login);
		list.add(logout);
		Profile profile = new Profile("operator", list);
		User olivier = new User("olivier smadja", "olivier@neodatis.com", profile);
		User aisa = new User("Aísa Galvão Smadja", "aisa@neodRMuatis.com", profile);

		odb.store(olivier);
		odb.store(aisa);
		odb.commit();

		Objects users = odb.getObjects(User.class);
		Objects profiles = odb.getObjects(Profile.class);
		Objects functions = odb.getObjects(Function.class);
		odb.close();
		// println("Users:"+users);
		println("Profiles:" + profiles);
		println("Functions:" + functions);

		odb = open(getBaseName());
		Objects l = odb.getObjects(User.class);
		odb.close();

		assertEquals(nbUsers + 2, users.size());
		User user2 = (User) users.first();

		assertEquals(olivier.toString(), user2.toString());
		assertEquals(nbProfiles + 1, profiles.size());
		assertEquals(nbFunctions + 2, functions.size());

		ODB odb2 = open(getBaseName());
		l = odb2.getObjects(Function.class);
		Function function = (Function) l.first();
		function.setName("login function");
		odb2.store(function);

		odb2.close();

		ODB odb3 = open(getBaseName());
		Objects l2 = odb3.getObjects(User.class);

		int i = 0;
		while (l2.hasNext() && i < Math.min(2, l2.size())) {
			User user = (User) l2.next();
			assertEquals("login function", "" + user.getProfile().getFunctions().get(0));
			i++;
		}
		odb3.close();
	}

	public void test3() throws Exception {
		ODB odb = open(getBaseName());

		Function login = new Function(null);
		odb.store(login);
		odb.close();

		odb = open(getBaseName());

		login = (Function) odb.query(Function.class, W.isNull("name")).objects().first();
		assertTrue(login.getName() == null);
		login.setName("login");
		odb.store(login);
		odb.close();

		odb = open(getBaseName());

		login = (Function) odb.getObjects(Function.class).first();
		assertTrue(login.getName().equals("login"));
		odb.close();
	}

	public void test5() throws Exception {
		ODB odb = open(getBaseName());
		long nbFunctions = odb.query(Function.class).count().longValue();
		long nbProfiles = odb.query(Profile.class).count().longValue();
		long nbUsers = odb.query(User.class).count().longValue();
		Function login = new Function("login");
		Function logout = new Function("logout");
		List list = new ArrayList();
		list.add(login);
		list.add(logout);
		Profile profile = new Profile("operator", list);
		User olivier = new User("olivier smadja", "olivier@neodatis.com", profile);
		User aisa = new User("Aísa Galvão Smadja", "aisa@neodatis.com", profile);

		odb.store(olivier);
		odb.store(profile);
		odb.commit();

		odb.close();

		odb = open(getBaseName());

		Objects users = odb.getObjects(User.class);
		Objects profiles = odb.getObjects(Profile.class);
		Objects functions = odb.getObjects(Function.class);
		odb.close();
		assertEquals(nbUsers + 1, users.size());
		assertEquals(nbProfiles + 1, profiles.size());
		assertEquals(nbFunctions + 2, functions.size());

	}

	public void test6() throws Exception {
		ODB odb = open(getBaseName());

		Function login = new Function("login");
		Function logout = new Function("logout");
		List list = new ArrayList();
		list.add(login);
		list.add(logout);
		Profile profile = new Profile("operator", list);
		User olivier = new User("olivier smadja", "olivier@neodatis.com", profile);

		odb.store(olivier);
		odb.close();
		println("----------");
		odb = open(getBaseName());
		Objects users = odb.getObjects(User.class);
		User u1 = (User) users.first();
		u1.getProfile().setName("operator 234567891011121314");
		odb.store(u1);
		odb.close();

		odb = open(getBaseName());
		Objects profiles = odb.getObjects(Profile.class);
		odb.close();
		assertEquals(1, profiles.size());
		Profile p1 = (Profile) profiles.first();
		assertEquals(u1.getProfile().getName(), p1.getName());

	}
}
