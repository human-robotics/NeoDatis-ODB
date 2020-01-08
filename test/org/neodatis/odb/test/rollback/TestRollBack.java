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
package org.neodatis.odb.test.rollback;

import org.neodatis.odb.*;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.tool.wrappers.OdbString;

public class TestRollBack extends ODBTest {
	NeoDatisConfig config = NeoDatis.getConfig().setUserAndPassword("u1", "p1");

	public void test1() throws Exception {
		
		ODB odb = open(getBaseName(), config);
		odb.store(new Function("f1"));
		odb.store(new Function("f2"));
		odb.store(new Function("f3"));
		odb.close();

		odb = open(getBaseName(), config);
		odb.store(new Function("f3"));
		odb.rollback();
		odb.close();

		odb = open(getBaseName(), config);
		assertEquals(3, odb.getObjects(Function.class).size());
		odb.close();
	}

	public void test2() throws Exception {

		ODB odb = open(getBaseName(), config);
		odb.store(new Function("f1"));
		odb.store(new Function("f2"));
		odb.store(new Function("f3"));
		odb.close();

		odb = open(getBaseName(), config);
		odb.store(new Function("f3"));
		odb.rollback();
		// odb.close();

		try {
			assertEquals(3, odb.getObjects(Function.class).size());
		} catch (NeoDatisRuntimeException e) {
			String s = OdbString.exceptionToString(e, false);
			assertFalse(s.indexOf("ODB session has been rollbacked") == -1);
		}
		odb.close();
	}

	public void test3RollbackOneStore() throws Exception {

		
		ODB odb = open(getBaseName(), config);
		odb.store(new Function("f1"));
		odb.store(new Function("f2"));
		odb.store(new Function("f3"));
		odb.close();

		odb = open(getBaseName(), config);
		odb.store(new Function("f3"));
		odb.rollback();
		odb.close();

		odb = open(getBaseName(), config);
		assertEquals(3, odb.getObjects(Function.class).size());
		odb.close();
	}

	public void test4RollbackXXXStores() throws Exception {

		
		ODB odb = open(getBaseName(), config);
		odb.store(new Function("f1"));
		odb.store(new Function("f2"));
		odb.store(new Function("f3"));
		odb.close();

		odb = open(getBaseName(), config);
		for (int i = 0; i < 500; i++) {
			odb.store(new Function("f3 - " + i));
		}
		odb.rollback();
		odb.close();

		odb = open(getBaseName(), config);
		assertEquals(3, odb.getObjects(Function.class).size());
		odb.close();
	}

	public void test5RollbackDelete() throws Exception {

		
		ODB odb = open(getBaseName(), config);
		odb.store(new Function("f1"));
		odb.store(new Function("f2"));
		odb.store(new Function("f3"));
		odb.close();

		odb = open(getBaseName(), config);
		Objects objects = odb.getObjects(Function.class);
		while (objects.hasNext()) {
			odb.delete(objects.next());
		}
		odb.rollback();
		odb.close();

		odb = open(getBaseName(), config);
		assertEquals(3, odb.getObjects(Function.class).size());
		odb.close();
	}

	public void test6RollbackDeleteAndStore() throws Exception {

			
		ODB odb = open(getBaseName(), config);
		odb.store(new Function("f1"));
		odb.store(new Function("f2"));
		odb.store(new Function("f3"));
		odb.close();

		odb = open(getBaseName(), config);
		Objects objects = odb.getObjects(Function.class);
		while (objects.hasNext()) {
			odb.delete(objects.next());
		}
		for (int i = 0; i < 500; i++) {
			odb.store(new Function("f3 - " + i));
		}

		odb.rollback();
		odb.close();

		odb = open(getBaseName(), config);
		assertEquals(3, odb.getObjects(Function.class).size());
		odb.close();
	}

	public void test7Update() throws Exception {

		
		ODB odb = open(getBaseName(), config);
		odb.store(new Function("1function"));
		odb.store(new Function("2function"));
		odb.store(new Function("3function"));
		odb.close();

		odb = open(getBaseName(), config);
		Objects objects = odb.getObjects(Function.class);
		while (objects.hasNext()) {
			Function f = (Function) objects.next();
			f.setName(f.getName().substring(1));
			odb.store(f);
		}

		odb.rollback();
		odb.close();

		odb = open(getBaseName(), config);
		assertEquals(3, odb.getObjects(Function.class).size());
		odb.close();
	}

	public void test8RollbackDeleteAndStore() throws Exception {

		
		ODB odb = open(getBaseName(), config);
		odb.store(new Function("f1"));
		odb.store(new Function("f2"));
		odb.store(new Function("f3"));
		odb.close();

		odb = open(getBaseName(), config);
		Objects objects = odb.getObjects(Function.class);

		while (objects.hasNext()) {
			Function f = (Function) objects.next();
			f.setName(f.getName().substring(1));
			odb.store(f);
		}

		objects.reset();

		while (objects.hasNext()) {
			odb.delete(objects.next());
		}
		for (int i = 0; i < 500; i++) {
			odb.store(new Function("f3 - " + i));
		}

		odb.rollback();
		odb.close();

		odb = open(getBaseName(), config);
		assertEquals(3, odb.getObjects(Function.class).size());
		odb.close();
	}


}
