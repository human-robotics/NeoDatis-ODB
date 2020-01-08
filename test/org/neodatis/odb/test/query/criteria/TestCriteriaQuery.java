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

import org.neodatis.odb.*;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.core.query.list.objects.SimpleList;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

import java.util.Date;

public class TestCriteriaQuery extends ODBTest {

		public void test1() throws Exception {
			String baseName = getBaseName();
			setUp(baseName);
			ODB odb = open(baseName);
	
			Query aq = odb.query(Function.class, W.equal("name", "function 2").or(W.equal("name", "function 3"))).orderByAsc("name");
	
			Objects<Function> l = aq.objects();
			assertEquals(2, l.size());
			Function f = (Function) l.first();
			assertEquals("function 2", f.getName());
			odb.close();
	
		}
	
		public void test2() throws Exception {
			String baseName = getBaseName();
			setUp(baseName);
			ODB odb = open(baseName);
	
			Query aq = odb.query(Function.class, W.equal("name", "function 2").not()).orderByAsc("name");
	
			Objects<Function> l = aq.objects();
			assertEquals(49, l.size());
			Function f = (Function) l.first();
			assertEquals("function 0", f.getName());
			odb.close();
	
		}

	public void test3() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query aq = odb.query(Function.class, W.equal("name", "function 2").or(W.equal("name", "function 3")).not()).orderByAsc("name");

		Objects<Function> l = aq.objects();
		assertEquals(48, l.size());
		Function f = (Function) l.first();
		assertEquals("function 0", f.getName());
		odb.close();

	}

	public void test4Sort() throws Exception {
		try {
			NeoDatisConfig config = NeoDatis.getConfig().setDefaultIndexBTreeDegree(40);
			
			String baseName = getBaseName();
			setUp(baseName);
			ODB odb = open(baseName,config);

			Query q = odb.query(Function.class, (W.equal("name", "function 2").or(W.equal("name", "function 3")).not())).orderByDesc("name");
			//aq.orderByDesc("name");
			// aq.orderByAsc("name");

			Objects l = odb.getObjects(q, true, -1, -1);
			assertEquals(48, l.size());
			Function f = (Function) l.first();
			assertEquals("function 9", f.getName());
			odb.close();
		} finally {
		}

	}

	public void testDate1() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);
		MyDates myDates = new MyDates();
		Date d1 = new Date();
		Thread.sleep(100);
		Date d2 = new Date();
		Thread.sleep(100);
		Date d3 = new Date();
		myDates.setDate1(d1);
		myDates.setDate2(d3);
		myDates.setI(5);
		odb.store(myDates);
		odb.close();

		odb = open(baseName);

		Query query = odb.query(MyDates.class, W.and().add(W.le("date1", d2)).add(W.ge("date2", d2)).add(W.equal("i", 5)));

		Objects objects = odb.getObjects(query);
		assertEquals(1, objects.size());
		odb.close();

	}

	public void testIequal() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query aq = odb.query(Function.class, W.iequal("name", "FuNcTiOn 1"));
		//Query aq = odb.query(Function.class, W.equal("name", "function 1"));
		aq.orderByDesc("name");

		Objects l = aq.objects();
		assertEquals(1, l.size());
		odb.close();
	}

	public void testEqual2() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query aq = odb.query(Function.class, W.equal("name", "FuNcTiOn 1"));
		aq.orderByDesc("name");

		Objects l = odb.getObjects(aq, true, -1, -1);
		assertEquals(0, l.size());
		odb.close();
	}

	
	
	public void test1001() throws Exception {

		String baseName = getBaseName();
		biSetUp(baseName);
		ODB odb = open(baseName);


		Query aq = odb.query(Function.class);
		aq.orderByAsc("name");

		Objects l = aq.objects();
		assertEquals(1000, l.size());
		Function f = (Function) l.first();
		assertEquals("function 0", f.getName());
		odb.close();

	}

	public void test1002() throws Exception {

		try{
			NeoDatisConfig config = NeoDatis.getConfig().setUseLazyInstantiationInServerMode(false);
			
			String baseName = getBaseName();
			biSetUp(baseName);
			ODB odb = open(baseName);

			Query aq = odb.query(Function.class);

			Objects l = aq.objects();
			// to be sure a direct list has been return , without lazy instantiation
			assertEquals(l.getClass().getName(), SimpleList.class.getName());
			assertEquals(1000, l.size());
			Function f = (Function) l.first();
			odb.close();
		}finally{
			// reset setting
		}

	}

	public void setUp(String baseName) throws Exception {
		ODB odb = open(baseName);
		for (int i = 0; i < 50; i++) {
			odb.store(new Function("function " + i));
		}
		odb.close();
	}

	public void biSetUp(String baseName) throws Exception {
		ODB odb = open(baseName);
		for (int i = 0; i < 1000; i++) {
			odb.store(new Function("function " + i));
		}
		odb.close();
	}

}
