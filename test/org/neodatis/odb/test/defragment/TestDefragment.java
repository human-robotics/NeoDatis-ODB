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
package org.neodatis.odb.test.defragment;

import org.junit.Test;
import org.neodatis.odb.ODB;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

import java.math.BigInteger;

public class TestDefragment extends ODBTest {
	/** The name of the database file */
    @Test
	public void test1() throws Exception {
		if (!isLocal) {
			return;
		}
		ODB odb = open(getBaseName());
		User user = new User("olivier", "olivier@neodatis.com", null);
		odb.store(user);
		odb.close();
		

		odb = open(getBaseName());
		odb.defragmentTo(getBaseName()+"2");

		ODB newOdb = open(getBaseName()+"2");

		// int n = odb.getObjects(User.class).size();
		// println("n="+n);
		BigInteger nbUser = odb.query(User.class).count();
		BigInteger nbNewUser = newOdb.query(User.class).count();

		assertEquals(nbUser, nbNewUser);

		assertEquals(odb.query(Profile.class).count(), newOdb.query(Profile.class).count());
		odb.close();
		newOdb.close();

	}
    @Test
	public void test2() throws Exception {
		if (!isLocal) {
			return;
		}

		ODB odb = open(getBaseName());
		Profile p = new Profile("profile");
		for (int i = 0; i < 500; i++) {
			User user = new User("olivier " + i, "olivier@neodatis.com " + i, p);
			odb.store(user);
		}
		odb.close();

		odb = open(getBaseName());
		odb.defragmentTo(DIRECTORY + getBaseName()+"2");

		ODB newOdb = open(getBaseName()+"2");

		assertEquals(odb.query(User.class).count(), newOdb.query(User.class).count());
		assertEquals(odb.query(Profile.class).count(), newOdb.query(Profile.class).count());
		odb.close();
		newOdb.close();

	}
    @Test
	public void test3() throws Exception {
		if (!isLocal) {
			return;
		}
		ODB odb = open(getBaseName());
		for (int i = 0; i < 1500; i++) {
			User user = new User("olivier " + i, "olivier@neodatis.com " + i, new Profile("profile" + i));
			odb.store(user);
			/*
			 * if(i>996){ Configuration.setDebugEnabled(true); }
			 */
		}
		odb.close();

		odb = open(getBaseName());
		odb.defragmentTo(DIRECTORY + getBaseName()+"2");

		ODB newOdb = open(getBaseName()+"2");

		assertEquals(odb.query(User.class).count(), newOdb.query(User.class).count());
		odb.close();
		newOdb.close();

	}

}
