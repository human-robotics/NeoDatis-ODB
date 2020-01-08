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
package org.neodatis.odb.test.io;

import org.neodatis.odb.ODB;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.tool.wrappers.OdbString;

public class TestUseAfterClose extends ODBTest {

	public void test() throws Exception {
		ODB odb = open(getBaseName());
		odb.close();

		try {
			odb.store(new Function("login"));
		} catch (Exception e) {
			String s = OdbString.exceptionToString(e, false);
			assertTrue(e.getMessage().indexOf("has already been closed") != -1);
		}
	}

	public void testTwoCloses() throws Exception {
		ODB odb = open(getBaseName());
		odb.close();

		try {
			odb.close();
		} catch (Exception e) {
			String s = OdbString.exceptionToString(e, false);
			assertTrue(e.getMessage().indexOf("has already been closed") != -1);
		}
	}

	public void testReOpenWithoutClose() throws Exception {
		ODB odb = open(getBaseName());

		try {
			odb = open(getBaseName());
		} catch (Exception e) {
			e.printStackTrace();
			String s = OdbString.exceptionToString(e, false);
			assertTrue(e.getMessage().indexOf(
					"file is locked by the current Virtual machine - check if the database has not been opened in the current VM!") != -1);
		}
		odb.close();
	}

}
