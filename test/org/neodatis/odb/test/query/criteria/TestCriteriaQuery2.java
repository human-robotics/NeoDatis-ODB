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
import org.neodatis.odb.core.query.criteria.Criterion;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.attribute.AllAttributeClass;
import org.neodatis.tool.wrappers.OdbTime;

import java.math.BigDecimal;
import java.util.Date;

public class TestCriteriaQuery2 extends ODBTest {


	public void test1() throws Exception {
		ODB odb = open(getBaseName());

		Query aq = odb.query(AllAttributeClass.class, W.or().add(W.equal("string1", "test class 1")).add(
				W.equal("string1", "test class 3")));
		aq.orderByAsc("string1");

		Objects l = odb.getObjects(aq, true, -1, -1);

		assertEquals(2, l.size());
		AllAttributeClass testClass = (AllAttributeClass) l.first();
		assertEquals("test class 1", testClass.getString1());
		odb.close();

	}

	public void test2() throws Exception {
		ODB odb = open(getBaseName());

		Query aq = odb.query(AllAttributeClass.class, W.equal("string1", "test class 2").not()).orderByAsc("bigDecimal1");

		Objects l = odb.getObjects(aq, true, -1, -1);
		assertEquals(49, l.size());
		AllAttributeClass testClass = (AllAttributeClass) l.first();
		assertEquals("test class 0", testClass.getString1());
		odb.close();

	}

	public void test3() throws Exception {
		ODB odb = open(getBaseName());

		Query aq = odb.query(AllAttributeClass.class, W.equal("string1", "test class 0").or(W.equal("bigDecimal1", new BigDecimal("5"))).not()).orderByAsc("bigDecimal1");

		Objects l = aq.objects();
		assertEquals(48, l.size());
		AllAttributeClass testClass = (AllAttributeClass) l.first();
		assertEquals("test class 1", testClass.getString1());
		odb.close();

	}

	public void test4Sort() throws Exception {
		ODB odb = open(getBaseName());

		Query aq = odb.query(AllAttributeClass.class, W.not(W.or().add(W.equal("string1", "test class 2")).add(
				W.equal("string1", "test class 3"))));
		aq.orderByDesc("double1,int1");

		Objects l = odb.getObjects(aq, true, -1, -1);
		// println(l);
		assertEquals(48, l.size());
		AllAttributeClass testClass = (AllAttributeClass) l.first();
		assertEquals("test class 9", testClass.getString1());
		odb.close();

	}

	public void test5Sort() throws Exception {
		ODB odb = open(getBaseName());

		Query aq = odb.query(AllAttributeClass.class, W.not(W.or().add(W.equal("string1", "test class 2")).add(
				W.equal("string1", "test class 3"))));
		// aq.orderByDesc("double1,boolean1,int1");
		aq.orderByDesc("double1,int1");

		Objects l = odb.getObjects(aq, true, -1, -1);
		assertEquals(48, l.size());
		AllAttributeClass testClass = (AllAttributeClass) l.first();
		assertEquals("test class 9", testClass.getString1());
		odb.close();

	}

	public void test6Sort() throws Exception {
		ODB odb = open(getBaseName());

		Criterion c = W.or().add(W.equal("string1", "test class 2")).add(W.equal("string1", "test class 3")).add(
				W.equal("string1", "test class 4")).add(W.equal("string1", "test class 5"));
		Query aq = odb.query(AllAttributeClass.class, c);
		aq.orderByDesc("boolean1,int1");

		Objects l = odb.getObjects(aq, true, -1, -1);
		assertEquals(4, l.size());
		AllAttributeClass testClass = (AllAttributeClass) l.first();
		assertEquals("test class 3", testClass.getString1());
		odb.close();

	}

	public void setUp() throws Exception {
		super.setUp();
		ODB odb = open(getBaseName());
		long start = OdbTime.getCurrentTimeInMs();
		int size = 50;
		for (int i = 0; i < size; i++) {
			AllAttributeClass testClass = new AllAttributeClass();
			testClass.setBigDecimal1(new BigDecimal(i));
			testClass.setBoolean1(i % 3 == 0);
			testClass.setChar1((char) (i % 5));
			testClass.setDate1(new Date(start + i));
			testClass.setDouble1(new Double(((double) (i % 10)) / size));
			testClass.setInt1(size - i);
			testClass.setString1("test class " + i);

			odb.store(testClass);
			// println(testClass.getDouble1() + " | " + testClass.getString1() +
			// " | " + testClass.getInt1());
		}
		odb.close();
	}

}
