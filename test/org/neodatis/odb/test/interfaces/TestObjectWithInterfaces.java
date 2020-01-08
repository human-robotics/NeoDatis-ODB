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
package org.neodatis.odb.test.interfaces;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.interfaces.MyObject;
import org.neodatis.odb.test.vo.interfaces.ObjectWithInterfaces;

import java.io.Serializable;

public class TestObjectWithInterfaces extends ODBTest {
	public void testInsert() throws Exception {
		ODB odb = open(getBaseName());

		ObjectWithInterfaces owi = new ObjectWithInterfaces("Olá chico");
		odb.store(owi);
		odb.commit();
		odb.close();
	}

	public void testInsertAndSelect() throws Exception {
		ODB odb = open(getBaseName());

		ObjectWithInterfaces owi = new ObjectWithInterfaces("Olá chico");
		odb.store(owi);
		odb.close();
		odb = open(getBaseName());
		Objects os = odb.getObjects(ObjectWithInterfaces.class);
		assertEquals(1, os.size());
		odb.close();

	}

	public void testStoreObjectByInterfaces() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Serializable o = new MyObject("f");
		odb.store(o);
		odb.close();
		odb = open(baseName);
		Objects os = odb.query(MyObject.class).objects();
		assertEquals(1, os.size());
		odb.close();

	}
	
	/** test retrieving objects with interfaces*/
	public void testStoringInterfaces(){
		String baseName = getBaseName();
		ODB odb = open(baseName);

		MyInterface mi = new Class1("t1");
		odb.store(mi);
		odb.close();
		odb = open(baseName);
		Objects<MyInterface> os = odb.query(MyInterface.class).setPolymorphic(true).objects();
		odb.close();
		assertEquals(1, os.size());
		
	}
}
