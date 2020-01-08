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

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.attribute.AllAttributeClass;
import org.neodatis.tool.wrappers.OdbTime;

import java.math.BigDecimal;
import java.util.Date;

public class TestCriteriaQuery4 extends ODBTest {

	private Date correctDate;

	public void testSodaWithDate() throws Exception {
		ODB odb = open(getBaseName());

		Query query = odb.query(AllAttributeClass.class, W.and().add(W.equal("string1", "test class with values")).add(
				W.equal("date1", new Date(correctDate.getTime()))));

		Objects l = odb.getObjects(query);
		// assertEquals(1,l.size());

		query = odb.query(AllAttributeClass.class, W.and().add(W.equal("string1", "test class with values")).add(
				W.ge("date1", new Date(correctDate.getTime()))));
		l = odb.getObjects(query);
		if (l.size() != 1) {
			query = odb.query(AllAttributeClass.class, W.equal("string1", "test class with null BigDecimal"));
			Objects l2 = odb.getObjects(query);
			println(l2);
			println(correctDate.getTime());
			l = odb.getObjects(query);
		}

		assertEquals(1, l.size());

		odb.close();
	}

	public void testSodaWithBoolean() throws Exception {
		ODB odb = open(getBaseName());

		Query query = odb.query(AllAttributeClass.class, W.equal("boolean1", true));
		Objects l = odb.getObjects(query);
		assertTrue(l.size() > 1);

		query = odb.query(AllAttributeClass.class, W.equal("boolean1", Boolean.TRUE));
		l = odb.getObjects(query);
		assertTrue(l.size() > 1);

		odb.close();
	}

	public void testSodaWithInt() throws Exception {
		ODB odb = open(getBaseName());

		Query query = odb.query(AllAttributeClass.class, W.equal("int1", 190));
		Objects l = odb.getObjects(query);
		assertEquals(1, l.size());

		query = odb.query(AllAttributeClass.class, W.gt("int1", 189));
		l = odb.getObjects(query);
		assertTrue(l.size() >= 1);

		query = odb.query(AllAttributeClass.class, W.lt("int1", 191));
		l = odb.getObjects(query);
		assertTrue(l.size() >= 1);

		odb.close();
	}

	public void testSodaWithDouble() throws Exception {
		ODB odb = open(getBaseName());

		Query query = odb.query(AllAttributeClass.class, W.equal("double1", 190.99));
		Objects l = odb.getObjects(query);
		assertEquals(1, l.size());

		query = odb.query(AllAttributeClass.class, W.gt("double1", (double) 189));
		l = odb.getObjects(query);
		assertTrue(l.size() >= 1);

		query = odb.query(AllAttributeClass.class, W.lt("double1", (double) 191));
		l = odb.getObjects(query);
		assertTrue(l.size() >= 1);

		odb.close();
	}

	public void testIsNull() throws Exception {
		ODB odb = null;

		try {
			odb = open(getBaseName());
			Query query = odb.query(AllAttributeClass.class, W.isNull("bigDecimal1"));
			Objects l = odb.getObjects(query);
			assertEquals(2, l.size());

		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void testIsNotNull() throws Exception {
		ODB odb = null;

		try {
			odb = open(getBaseName());
			Query query = odb.query(AllAttributeClass.class, W.isNotNull("bigDecimal1"));
			Objects l = odb.getObjects(query);
			assertEquals(51, l.size());

		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void setUp() throws Exception {
		super.setUp();
		ODB odb = open(getBaseName());
		long start = OdbTime.getCurrentTimeInMs();
		int size = 50;
		for (int i = 0; i < size; i++) {
			AllAttributeClass tc = new AllAttributeClass();
			tc.setBigDecimal1(new BigDecimal(i));
			tc.setBoolean1(i % 3 == 0);
			tc.setChar1((char) (i % 5));
			tc.setDate1(new Date(1000 + start + i));
			tc.setDouble1(new Double(((double) (i % 10)) / size));
			tc.setInt1(size - i);
			tc.setString1("test class " + i);

			odb.store(tc);
		}
		AllAttributeClass testClass = new AllAttributeClass();
		testClass.setBigDecimal1(new BigDecimal("190.95"));
		testClass.setBoolean1(true);
		testClass.setChar1('s');
		correctDate = new Date();
		testClass.setDate1(correctDate);
		testClass.setDouble1(new Double(190.99));
		testClass.setInt1(190);
		testClass.setString1("test class with values");
		odb.store(testClass);

		AllAttributeClass testClass2 = new AllAttributeClass();
		testClass2.setBigDecimal1(null);
		testClass2.setBoolean1(true);
		testClass2.setChar1('s');
		correctDate = new Date();
		testClass2.setDate1(correctDate);
		testClass2.setDouble1(new Double(191.99));
		testClass2.setInt1(1901);
		testClass2.setString1("test class with null BigDecimal");
		odb.store(testClass2);

		AllAttributeClass testClass3 = new AllAttributeClass();
		odb.store(testClass3);
		odb.close();
	}

}
