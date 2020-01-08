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
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

public class TestInPlaceUpdate extends ODBTest {

	public static final int SIZE = 50;

	/**
	 * Stores an object User that has a non null reference to a Profile. Then
	 * deletes the profile. Loads the user again and updates the user profile
	 * with a new created profile. ODB did not detect the change Detected by
	 * Olivier.
	 * 
	 * @throws Exception
	 */
	public void test8() throws Exception {

		if (!isLocal) {
			return;
		}
		
		ODB odb = open(getBaseName());

		User user = new User("name", "email", new Profile("p1", new Function("function")));
		odb.store(user);
		odb.close();

		odb = open(getBaseName());
		Profile p = (Profile) odb.getObjects(Profile.class).first();
		odb.delete(p);
		odb.close();

		odb = open(getBaseName());
		User user3 = (User) odb.getObjects(User.class).first();
		assertNull(user3.getProfile());
		user3.setProfile(new Profile("new profile", new Function("f1")));
		user3.setEmail("email2");
		user3.setName("name2");
		odb.store(user3);
		odb.close();
		odb = open(getBaseName());
		User user4 = (User) odb.getObjects(User.class).first();
		odb.close();
		

		assertEquals("new profile", user4.getProfile().getName());
		assertEquals("email2", user4.getEmail());
		assertEquals("name2", user4.getName());

	}

	/**
	 * Stores an object User that has a non null reference to a Profile. Creates
	 * a new profile.
	 * 
	 * Update the last profile and sets it a the new user profile.ODB detects
	 * the reference change but does not update the profile Detected by Olivier.
	 * 22/05/2007
	 * 
	 * @throws Exception
	 */
	public void test9() throws Exception {

		
		ODB odb = open(getBaseName());

		User user = new User("name", "email", new Profile("p1", new Function("function")));
		odb.store(user);
		odb.store(new Profile("new profile"));
		odb.close();

		odb = open(getBaseName());
		Profile p = (Profile) odb.query(Profile.class, W.equal("name", "new profile")).objects().first();
		p.setName("new profile2");
		User user2 = (User) odb.getObjects(User.class).first();
		user2.setProfile(p);
		odb.store(user2);
		odb.close();

		odb = open(getBaseName());
		User user3 = (User) odb.getObjects(User.class).first();
		assertNotNull(user3.getProfile());
		odb.close();
		

		assertEquals("new profile2", user3.getProfile().getName());

	}

	/**
	 * test in place update with rollback. Bug detected by Olivier 22/02/2008.
	 * In place updates for connected object were done out of transaction,
	 * avoiding rollback (ObejctWriter.manageInPlaceUpdate()
	 */
	public void test10() {
		ODB odb = null;

		try {
			odb = open(getBaseName());
			ObjectOid oid = odb.store(new Function("function1"));
			odb.close();

			odb = open(getBaseName());
			Function f = (Function) odb.getObjectFromId(oid);
			f.setName("function2");
			odb.store(f);
			odb.rollback();
			odb.close();

			odb = open(getBaseName());
			f = (Function) odb.getObjectFromId(oid);
			odb.close();
			assertEquals("function1", f.getName());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
