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
package org.neodatis.odb.test;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.nq.NativeQuery;
import org.neodatis.odb.test.vo.attribute.AllAttributeClass;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StorageEngineTest extends ODBTest {

	public void testSimpleInstance() throws Exception {
		String baseName = getBaseName();
		println(baseName);
		ODB odb = open(baseName);

		AllAttributeClass tc1 = new AllAttributeClass();
		tc1.setBigDecimal1(new BigDecimal("1.123456"));
		tc1.setBoolean1(true);
		tc1.setChar1('d');
		tc1.setDouble1(new Double(154.78998989));
		tc1.setInt1(78964);
		tc1.setString1("Ola chico como vc est\u00E1 ???");
		tc1.setDate1(new Date());
		tc1.setBoolean2(Boolean.FALSE);

		AllAttributeClass tc2 = new AllAttributeClass();
		tc2.setBigDecimal1(new BigDecimal("1.1234565454"));
		tc2.setBoolean1(false);
		tc2.setChar1('c');
		tc2.setDouble1(new Double(78454.8779));
		tc2.setInt1(1254);
		tc2.setString1("Ola chico como ca va ???");
		tc2.setDate1(new Date());
		tc2.setBoolean2(Boolean.TRUE);

		odb.store(tc1);
		//odb.store(tc2);

		odb.close();

		odb = open(baseName);

		Objects l = odb.getObjects(AllAttributeClass.class);
		AllAttributeClass tc12 = (AllAttributeClass) l.first();
		// println("#### " + l.size() + " : " + l);
		assertEquals(tc1.getBigDecimal1(), tc12.getBigDecimal1());
		assertEquals(tc1.getString1(), tc12.getString1());
		assertEquals(tc1.getChar1(), tc12.getChar1());
		assertEquals(tc1.getDouble1(), tc12.getDouble1());
		assertEquals(tc1.getInt1(), tc12.getInt1());
		assertEquals(tc1.isBoolean1(), tc12.isBoolean1());
		assertEquals(Boolean.FALSE, tc12.getBoolean2());
		if (l.size() < 3) {
			assertEquals(tc1.getDate1(), tc12.getDate1());
		}
		/*
		//l.next();
		TestClass tc22 = (TestClass) l.next();
		assertEquals(tc2.getBigDecimal1(), tc22.getBigDecimal1());
		assertEquals(tc2.getString1(), tc22.getString1());
		assertEquals(tc2.getChar1(), tc22.getChar1());
		assertEquals(tc2.getDouble1(), tc22.getDouble1());
		assertEquals(tc2.getInt1(), tc22.getInt1());
		assertEquals(tc2.isBoolean1(), tc22.isBoolean1());
		assertEquals(Boolean.TRUE, tc2.getBoolean2());
		if (l.size() < 3) {
			assertEquals(tc2.getDate1(), tc22.getDate1());
		}
		*/

		odb.close();
	}

	public void testSimpleInstanceRetrievingWithNQ() throws Exception {
		if (!isLocal || !useSameVmOptimization) {
			return;
		}

		ODB odb = open(getBaseName());

		AllAttributeClass tc1 = new AllAttributeClass();
		tc1.setBigDecimal1(new BigDecimal("1.123456"));
		tc1.setBoolean1(true);
		tc1.setChar1('d');
		tc1.setDouble1(new Double(154.78998989));
		tc1.setInt1(78964);
		tc1.setString1("Ola chico como vc est\u00E1 ???");
		tc1.setDate1(new Date());
		tc1.setBoolean2(Boolean.FALSE);

		AllAttributeClass tc2 = new AllAttributeClass();
		tc2.setBigDecimal1(new BigDecimal("1.1234565454"));
		tc2.setBoolean1(false);
		tc2.setChar1('c');
		tc2.setDouble1(new Double(78454.8779));
		tc2.setInt1(1254);
		tc2.setString1("Ola chico como ca va ???");
		tc2.setDate1(new Date());
		tc2.setBoolean2(Boolean.TRUE);

		odb.store(tc1);
		odb.store(tc2);

		odb.close();

		odb = open(getBaseName());

		Query q = new NativeQuery<AllAttributeClass>() {
			public boolean match(AllAttributeClass object) {
				return true;
			};
		};
		Objects l = odb.query(q).objects();
		AllAttributeClass tc12 = (AllAttributeClass) l.first();
		// println("#### " + l.size() + " : " + l);
		assertEquals(tc1.getBigDecimal1(), tc12.getBigDecimal1());
		assertEquals(tc1.getString1(), tc12.getString1());
		assertEquals(tc1.getChar1(), tc12.getChar1());
		assertEquals(tc1.getDouble1(), tc12.getDouble1());
		assertEquals(tc1.getInt1(), tc12.getInt1());
		assertEquals(tc1.isBoolean1(), tc12.isBoolean1());
		assertEquals(Boolean.FALSE, tc12.getBoolean2());
		if (l.size() < 3) {
			assertEquals(tc1.getDate1(), tc12.getDate1());
		}

		l.next();
		AllAttributeClass tc22 = (AllAttributeClass) l.next();
		assertEquals(tc2.getBigDecimal1(), tc22.getBigDecimal1());
		assertEquals(tc2.getString1(), tc22.getString1());
		assertEquals(tc2.getChar1(), tc22.getChar1());
		assertEquals(tc2.getDouble1(), tc22.getDouble1());
		assertEquals(tc2.getInt1(), tc22.getInt1());
		assertEquals(tc2.isBoolean1(), tc22.isBoolean1());
		assertEquals(Boolean.TRUE, tc2.getBoolean2());
		if (l.size() < 3) {
			assertEquals(tc2.getDate1(), tc22.getDate1());
		}

		odb.close();
	}

	public void testSimpleInstanceRetrievingWithNQWithNullBoolean() throws Exception {
		if (!isLocal || !useSameVmOptimization) {
			// native must be serializable to be executed in cs mode
			return;
		}
		ODB odb = open(getBaseName());

		AllAttributeClass tc1 = new AllAttributeClass();
		tc1.setBigDecimal1(new BigDecimal("1.123456"));
		tc1.setBoolean1(true);
		tc1.setChar1('d');
		tc1.setDouble1(new Double(154.78998989));
		tc1.setInt1(78964);
		tc1.setString1("Ola chico como vc está ???");
		tc1.setDate1(new Date());
		tc1.setBoolean2(null);

		AllAttributeClass tc2 = new AllAttributeClass();
		tc2.setBigDecimal1(new BigDecimal("1.1234565454"));
		tc2.setBoolean1(false);
		tc2.setChar1('c');
		tc2.setDouble1(new Double(78454.8779));
		tc2.setInt1(1254);
		tc2.setString1("Ola chico como ca va ???");
		tc2.setDate1(new Date());
		tc2.setBoolean2(Boolean.TRUE);

		odb.store(tc1);
		odb.store(tc2);

		odb.close();

		odb = open(getBaseName());

		Query q = new NativeQuery<AllAttributeClass>() {
			public boolean match(AllAttributeClass object) {
				return true;
			};
		};
		Objects l = odb.query(q).objects();
		AllAttributeClass tc12 = (AllAttributeClass) l.first();
		// println("#### " + l.size() + " : " + l);
		assertEquals(tc1.getBigDecimal1(), tc12.getBigDecimal1());
		assertEquals(tc1.getString1(), tc12.getString1());
		assertEquals(tc1.getChar1(), tc12.getChar1());
		assertEquals(tc1.getDouble1(), tc12.getDouble1());
		assertEquals(tc1.getInt1(), tc12.getInt1());
		assertEquals(tc1.isBoolean1(), tc12.isBoolean1());
		assertEquals(null, tc12.getBoolean2());
		if (l.size() < 3) {
			assertEquals(tc1.getDate1(), tc12.getDate1());
		}

		l.next();
		AllAttributeClass tc22 = (AllAttributeClass) l.next();
		assertEquals(tc2.getBigDecimal1(), tc22.getBigDecimal1());
		assertEquals(tc2.getString1(), tc22.getString1());
		assertEquals(tc2.getChar1(), tc22.getChar1());
		assertEquals(tc2.getDouble1(), tc22.getDouble1());
		assertEquals(tc2.getInt1(), tc22.getInt1());
		assertEquals(tc2.isBoolean1(), tc22.isBoolean1());
		assertEquals(Boolean.TRUE, tc2.getBoolean2());
		if (l.size() < 3) {
			assertEquals(tc2.getDate1(), tc22.getDate1());
		}

		odb.close();
	}

	public void testComplexInstanceProfile() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Function login = new Function("login");
		Function logout = new Function("logout");
		List functions = new ArrayList();
		functions.add(login);
		//functions.add(logout);
		Profile profile = new Profile("profile1", functions);

		odb.store(profile);
		odb.close();

		odb = open(baseName);

		Objects<Profile> l = odb.getObjects(Profile.class);

		Profile p = l.first();
		// println("#### " + l.size() + " : " + l);
		assertEquals(profile.getName(), p.getName());
		assertEquals(profile.getFunctions().get(0).toString(), p.getFunctions().get(0).toString());

		odb.close();

	}
	public void testComplexInstance() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Function login = new Function("login");
		Function logout = new Function("logout");
		List functions = new ArrayList();
		functions.add(login);
		functions.add(logout);
		Profile profile = new Profile("profile1", functions);
		User user = new User("oliver", "olivier@neodatis.com", profile);
		User user22 = new User("oliver2", "olivier2@neodatis.com", profile);

		ObjectOid oid1 =  odb.store(user);
		ObjectOid oid2 = odb.store(user22);
		odb.close();

		odb = open(baseName);

		Objects l = odb.query(User.class).orderByAsc("name").objects();

		User user2 = (User) l.first();
		// println("#### " + l.size() + " : " + l);
		assertEquals(user.getName(), user2.getName());
		assertEquals(user.getEmail(), user2.getEmail());
		assertEquals(user.getProfile().getName(), user2.getProfile().getName());
		assertEquals(user.getProfile().getFunctions().get(0).toString(), user2.getProfile().getFunctions().get(0).toString());

		odb.close();

	}

}
