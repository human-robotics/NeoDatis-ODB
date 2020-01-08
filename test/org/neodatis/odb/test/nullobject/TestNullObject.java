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
package org.neodatis.odb.test.nullobject;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.User;

public class TestNullObject extends ODBTest {

	public void test1() throws Exception {
		ODB odb = open(getBaseName());
		User user1 = new User("oli", "oli@sdsadf", null);
		User user2 = new User("karine", "karine@sdsadf", null);
		User user3 = new User(null, null, null);

		odb.store(user1);
		odb.store(user2);
		odb.store(user3);
		odb.close();

		odb = open(getBaseName());
		Objects l = odb.query(User.class, W.equal("name", "oli")).objects();

		assertEquals(1, l.size());

		user1 = (User) l.first();
		assertEquals("oli", user1.getName());
		assertEquals("oli@sdsadf", user1.getEmail());
		assertEquals(null, user1.getProfile());

		l = odb.query(User.class, W.equal("name", "karine")).objects();
		user2 = (User) l.first();
		assertEquals("karine", user2.getName());
		assertEquals("karine@sdsadf", user2.getEmail());
		assertEquals(null, user2.getProfile());

		l = odb.query(User.class, W.isNull("name")).objects();
		user3 = (User) l.next();
		assertEquals(null, user3.getName());
		assertEquals(null, user3.getEmail());
		assertEquals(null, user3.getProfile());

		odb.close();

	}

	/**
	 * Test generic attribute of type Object receving a native type
	 * 
	 * @throws Exception
	 */
	public void test2() throws Exception {
		String baseName = getBaseName();
		GenericClass gc = new GenericClass(null);

		ODB odb = open(baseName);
		odb.store(gc);
		odb.close();

		odb = open(baseName);
		Objects objects = odb.query(GenericClass.class).objects();
		GenericClass gc2 = (GenericClass) objects.first();
		gc2.setObject("Ola");
		odb.store(gc2);
		odb.close();

		odb = open(baseName);
		objects = odb.query(GenericClass.class).objects();
		assertEquals(1, objects.size());
		GenericClass gc3 = (GenericClass) objects.first();
		assertEquals("Ola", gc3.getObject());
		odb.close();

	}

	public void test21() throws Exception {
		NeoDatisConfig c = NeoDatis.getConfig();
		GenericClass gc = new GenericClass(null);
		println(getBaseName());
		ODB odb = open(getBaseName(),c);
		odb.store(gc);
		odb.close();

		println(getBaseName());
		odb = open(getBaseName(),c);
		Objects objects = odb.query(GenericClass.class).objects();
		GenericClass gc2 = (GenericClass) objects.first();
		Long[] longs = { new Long(1), new Long(2) };
		gc2.setObjects(longs);
		odb.store(gc2);
		odb.close();
		
		println(getBaseName());
		odb = open(getBaseName());
		objects = odb.query(GenericClass.class).objects();
		assertEquals(1, objects.size());
		GenericClass gc3 = (GenericClass) objects.first();
		Long[] longs2 = (Long[]) gc3.getObjects();
		assertEquals(2, longs2.length);
		assertEquals(new Long(1), longs2[0]);
		assertEquals(new Long(2), longs2[1]);
		odb.close();

	}

	public void test22() throws Exception {
		GenericClass gc = new GenericClass(null);

		ODB odb = open(getBaseName());
		odb.store(gc);
		odb.close();

		odb = open(getBaseName());
		Objects objects = odb.getObjects(GenericClass.class);
		GenericClass gc2 = (GenericClass) objects.first();
		gc2.getObjects()[0] = new Long(1);
		gc2.getObjects()[1] = new Long(2);
		odb.store(gc2);
		odb.close();

		odb = open(getBaseName());
		objects = odb.getObjects(GenericClass.class);
		assertEquals(1, objects.size());
		GenericClass gc3 = (GenericClass) objects.first();
		Object[] longs2 = (Object[]) gc3.getObjects();
		assertEquals(10, longs2.length);
		assertEquals(new Long(1), longs2[0]);
		assertEquals(new Long(2), longs2[1]);
		odb.close();

	}

	public void test23() throws Exception {
		GenericClass gc = new GenericClass(null);
		gc.getObjects()[0] = new Function("f1");
		ODB odb = open(getBaseName());
		odb.store(gc);
		odb.close();

		odb = open(getBaseName());
		Objects objects = odb.getObjects(GenericClass.class);
		GenericClass gc2 = (GenericClass) objects.first();
		gc2.getObjects()[0] = new Long(1);
		gc2.getObjects()[1] = new Long(2);
		odb.store(gc2);
		odb.close();

		odb = open(getBaseName());
		objects = odb.getObjects(GenericClass.class);
		assertEquals(1, objects.size());
		GenericClass gc3 = (GenericClass) objects.first();
		Object[] longs2 = (Object[]) gc3.getObjects();
		assertEquals(10, longs2.length);
		assertEquals(new Long(1), longs2[0]);
		assertEquals(new Long(2), longs2[1]);
		odb.close();

	}

	public void test3() throws Exception {
		GenericClass gc = new GenericClass(null);
		String[] strings = { "OBJ1", "obj2" };
		gc.setObjects(strings);

		ODB odb = open(getBaseName());
		odb.store(gc);
		odb.close();

		odb = open(getBaseName());
		Objects objects = odb.query(GenericClass.class).objects();
		GenericClass gc2 = (GenericClass) objects.first();
		gc2.setObject("Ola");
		odb.store(gc2);
		odb.close();

	}

	public void test4() throws Exception {
		GenericClass gc = new GenericClass(null);
		String[] strings = { "OBJ1", "obj2" };
		gc.setObject(strings);

		ODB odb = open(getBaseName());
		odb.store(gc);
		odb.close();

		odb = open(getBaseName());
		Objects objects = odb.query(GenericClass.class).objects();
		GenericClass gc2 = (GenericClass) objects.first();
		gc2.setObject("Ola");
		odb.store(gc2);
		odb.close();

	}

	public void test5() throws Exception {
		Function f = new Function("a simple value");

		ODB odb = open(getBaseName());
		odb.store(f);
		odb.close();

		odb = open(getBaseName());
		Objects objects = odb.getObjects(Function.class);
		Function f2 = (Function) objects.first();
		f2.setName(null);
		odb.store(f2);
		odb.close();

		odb = open(getBaseName());
		objects = odb.getObjects(Function.class);
		f2 = (Function) objects.first();

		odb.close();
		assertEquals(null, f2.getName());

	}
}
