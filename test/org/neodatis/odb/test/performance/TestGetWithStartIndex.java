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
package org.neodatis.odb.test.performance;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

public class TestGetWithStartIndex extends ODBTest {

	public void test1() throws Exception {
		ODB odb = open(getBaseName());
		for (int i = 0; i < 10; i++) {
			odb.store(new Function("function " + i));
		}
		odb.close();

		odb = open(getBaseName());
		String s = null;
		Query q = odb.query(Function.class).orderByAsc("name");
		q.getQueryParameters().setStartIndex(4).setEndIndex(7);
		Objects l = q.objects();
		System.out.println(l);
		assertEquals(3, l.size());
		assertEquals("function 4", l.first().toString());
		odb.close();

	}

}
