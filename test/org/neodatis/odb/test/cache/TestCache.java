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
package org.neodatis.odb.test.cache;

import org.junit.Before;
import org.junit.Test;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

public class TestCache extends ODBTest {

	public static int NB_OBJECTS = 300;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

    @Before
	public void setUp() throws Exception {
		super.setUp();
		ODB odb = open(getBaseName());
		for (int i = 0; i < NB_OBJECTS; i++) {
			odb.store(new Function("function " + (i + i)));
			odb.store(new User("olivier " + i, "olivier@neodatis.com " + i,
					new Profile("profile " + i, new Function("inner function " + i))));
		}
		odb.close();
	}

    @Test
	public void test1() throws Exception {
		ODB odb = open(getBaseName());

		Objects l = odb.query(Function.class, W.equal("name", "function 10")).objects();
		assertFalse(l.isEmpty());
		// Cache must have only one object : The function
		assertEquals(l.size(), Dummy.getEngine(odb).getSession().getCache().getSize());
		odb.close();
	}

    @Test
	public void test2() throws Exception {
		ODB odb = open(getBaseName());

		Objects l = odb.query(User.class, W.equal("name", "olivier 10")).objects();
		assertFalse(l.isEmpty());
		// Cache must have 3 times the number of Users in list l (check the
		// setup method to understand this)
		assertEquals(l.size() * 3, Dummy.getEngine(odb).getSession().getCache().getSize());
		odb.close();
	}

    @Test
	public void test3() throws Exception {
		ODB odb = open(getBaseName());

		Objects l = odb.query(User.class, W.equal("profile.name", "profile 10")).objects();
		assertFalse(l.isEmpty());
		// Cache must have 3 times the number of Users in list l (check the
		// setup method to understand this)
		assertEquals(l.size() * 3, Dummy.getEngine(odb).getSession().getCache().getSize());
		odb.close();
	}

}
