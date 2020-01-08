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
import org.neodatis.odb.test.vo.login.Function;

public class TestCriteriaQueryWithLike extends ODBTest {

	

	public void testILike() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query aq = odb.query(Function.class, W.ilike("name", "FUNc%"));
		aq.orderByDesc("name");

		Objects l = odb.getObjects(aq, true, -1, -1);
		assertEquals(50, l.size());
		odb.close();
	}

	public void testLike() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query aq = odb.query(Function.class, W.like("name", "func%"));
		aq.orderByDesc("name");

		Objects l = odb.getObjects(aq, true, -1, -1);
		assertEquals(50, l.size());
		odb.close();
	}
	
	/** if there is no % => no match
	 * 
	 * @throws Exception
	 */
	public void testLike1() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query aq = odb.query(Function.class, W.like("name", "func"));
		aq.orderByDesc("name");

		Objects l = odb.getObjects(aq, true, -1, -1);
		assertEquals(50, l.size());
		odb.close();
	}

	public void testLike2() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query aq = odb.query(Function.class, W.like("name", "FuNc%"));
		aq.orderByDesc("name");

		Objects l = odb.getObjects(aq, true, -1, -1);
		assertEquals(0, l.size());
		odb.close();
	}
	
	public void testLike3() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		Query aq = odb.query(Function.class, W.like("name", "*"));
		aq.orderByDesc("name");

		Objects l = odb.query(aq).objects();
		assertEquals(0, l.size());
		odb.close();
	}
	
	public void testLike4() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+");
		odb.store(f);
		Query aq = odb.query(Function.class, W.like("name", "%?$+",false));
		aq.orderByDesc("name");

		Objects l = odb.query(aq).objects();
		assertEquals(1, l.size());
		println(l.first());
		odb.close();
	}

	public void testLike5() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+");
		odb.store(f);
		Query aq = odb.query(Function.class, W.like("name", "*",true));
		aq.orderByDesc("name");

		Objects l = odb.query(aq).objects();
		assertEquals(1, l.size());
		odb.close();
	}
	public void testLike6() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+");
		odb.store(f);
		Query aq = odb.query(Function.class, W.like("name", "(.)*",true));
		aq.orderByDesc("name");

		Objects l = odb.query(aq).objects();
		assertEquals(0, l.size());
		odb.close();
	}
	/** test using regexp
	 * 
	 * @throws Exception
	 */
	public void testLikeWithRegExp() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+");
		odb.store(f);
		Query aq = odb.query(Function.class, W.like("name", "(.)*",false));
		aq.orderByDesc("name");

		Objects l = odb.query(aq).objects();
		assertEquals(1, l.size());
		odb.close();
	}

	/** test using regexp
	 * 
	 * @throws Exception
	 */
	public void testLikeWithRegExp2() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+");
		odb.store(f);
		// a regexp to check if there is a *
		Query aq = odb.query(Function.class, W.like("name", "(.)*\\*(.)*",false));
		aq.orderByDesc("name");

		Objects l = odb.query(aq).objects();
		assertEquals(1, l.size());
		odb.close();
	}
	
	/** test using regexp
	 * 
	 * @throws Exception
	 */
	public void testLikeWithRegExp3() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+");
		odb.store(f);
		// a regexp to check if there is a *
		Query aq = odb.query(Function.class, W.like("name", "(.)*\\*\\*(.)*",false));
		aq.orderByDesc("name");

		Objects l = odb.query(aq).objects();
		assertEquals(0, l.size());
		odb.close();
	}


	
	/** test with new Lines
	 * 
	 * @throws Exception
	 */
	public void testLikeWithRegExpWithTextWithNewLine() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+ for line 1\n some text for line 2\n some text for line 3");
		odb.store(f);
		// a regexp to check if there is a *
		Query aq = odb.query(Function.class, W.like("name", "%line 3%"));

		Objects l = odb.query(aq).objects();
		assertEquals(1, l.size());
		odb.close();
	}
	/** test with new Lines
	 * 
	 * @throws Exception
	 */
	public void testLikeWithRegExpWithTextWithNewLine2() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+ for line 1\n some text for line 2\n some text for line 3");
		odb.store(f);
		// a regexp to check if there is a *
		Query aq = odb.query(Function.class, W.like("name", "%line 3"));

		Objects l = odb.query(aq).objects();
		assertEquals(1, l.size());
		odb.close();
	}

	/** test with new Lines
	 * 
	 * @throws Exception
	 */
	public void testLikeWithRegExpWithTextWithNewLine3() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+ for line 1\n some text for line 2\n some text for line 3");
		odb.store(f);
		// a regexp to check if there is a *
		Query aq = odb.query(Function.class, W.like("name", "line 3"));

		Objects l = odb.query(aq).objects();
		assertEquals(1, l.size());
		odb.close();
	}
	
	public void testLikeWithRegExpWithTextWithNewLine4() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+ for line 1\n some text for line 2\n some text for line 3");
		odb.store(f);
		// a regexp to check if there is a *
		Query aq = odb.query(Function.class, W.like("name", "%line 1%line 2%line 3"));

		Objects l = odb.query(aq).objects();
		assertEquals(1, l.size());
		odb.close();
	}
	
	public void testLikeWithRegExpWithTextWithNewLine5() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		Function f= new Function("some text *?$+% for line 1\n some text for line 2\n some text for line 3");
		odb.store(f);
		// a regexp to check if there is a *
		Query aq = odb.query(Function.class, W.like("name", "%",true));

		Objects l = odb.query(aq).objects();
		assertEquals(1, l.size());
		odb.close();
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
