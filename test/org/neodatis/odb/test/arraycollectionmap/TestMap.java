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
package org.neodatis.odb.test.arraycollectionmap;

import org.junit.Before;
import org.junit.Test;
import org.neodatis.odb.*;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.arraycollectionmap.Dictionnary;
import org.neodatis.odb.test.vo.login.Function;

public class TestMap extends ODBTest {
    @Test
	public void test0(){
		String baseName = getBaseName();
		NeoDatisConfig config = NeoDatis.getConfig().setDebugLayers(false);
		ODB odb = open(baseName,config);
		
		ClassWithMap cwm = new ClassWithMap("test", "key1", "value1");
		odb.store(cwm);
		odb.close();

		odb = open(baseName,config);
		Objects<ClassWithMap> oos = odb.query(ClassWithMap.class).objects();
		odb.close();
		
	}

	@Before
	public void setUp(String baseName) throws Exception {
		ODB odb = open(baseName);

		Dictionnary dictionnary1 = new Dictionnary("test1");
		dictionnary1.addEntry("olivier", "Smadja");
		dictionnary1.addEntry("kiko", "vidal");
		dictionnary1.addEntry("karine", "galvao");

		Dictionnary dictionnary2 = new Dictionnary("test2");
		dictionnary2.addEntry("f1", new Function("function1"));
		dictionnary2.addEntry("f2", new Function("function2"));
		dictionnary2.addEntry("f3", new Function("function3"));
		dictionnary2.addEntry(dictionnary1, new Function("function4"));
		dictionnary2.addEntry(null, new Function("function3"));
		dictionnary2.addEntry(null, null);
		dictionnary2.addEntry("f4", null);
		odb.store(dictionnary1);
		odb.store(dictionnary2);
		odb.store(new Function("login"));
		odb.close();
	}

    @Test
	public void test1() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);
		
		Objects l = odb.query(Dictionnary.class, W.equal("name", "test1")).objects();
		// assertEquals(2,l.size());
		Dictionnary dictionnary = (Dictionnary) l.first();

		assertEquals("Smadja", dictionnary.get("olivier"));
		odb.close();

	}

    @Test
	public void test2() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);
		
		Objects l = odb.query(Dictionnary.class).objects();
		Query aq = odb.query(Dictionnary.class, W.equal("name", "test2"));
		l = aq.objects();
		Dictionnary dictionnary = (Dictionnary) l.first();

		assertEquals(new Function("function2").getName(), ((Function) dictionnary.get("f2")).getName());
		odb.close();

	}

    @Test
	public void test3() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);
		
		long size = odb.query(Dictionnary.class).count().longValue();
		Dictionnary dictionnary1 = new Dictionnary("test1");
		dictionnary1.setMap(null);
		odb.store(dictionnary1);
		odb.close();

		odb = open(baseName);
		assertEquals(size + 1, odb.query(Dictionnary.class).objects().size());
		assertEquals(size + 1, odb.query(Dictionnary.class).count().longValue());
		odb.close();

	}

    @Test
	public void test4() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);
		
		long n = odb.query(Dictionnary.class).count().longValue();
		Query query = odb.query(Dictionnary.class, W.equal("name", "test2"));
		Objects l = odb.getObjects(query);
		Dictionnary dictionnary = (Dictionnary) l.first();
		dictionnary.setMap(null);
		odb.store(dictionnary);
		odb.close();

		odb = open(baseName);
		assertEquals(n, odb.query(Dictionnary.class).count().longValue());
		Dictionnary dic = (Dictionnary) odb.getObjects(query).first();
		assertEquals(null, dic.getMap());
		odb.close();

	}

    @Test
	public void test5updateIncreasingSize() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

	
		long n = odb.query(Dictionnary.class).count().longValue();
		Query query = odb.query(Dictionnary.class, W.equal("name", "test2"));
		Objects l = odb.getObjects(query);
		Dictionnary dictionnary = (Dictionnary) l.first();
		dictionnary.setMap(null);
		odb.store(dictionnary);
		odb.close();

		odb = open(baseName);
		assertEquals(n, odb.query(Dictionnary.class).count().longValue());
		Dictionnary dic = (Dictionnary) odb.getObjects(query).first();
		assertNull(dic.getMap());
		odb.close();

		odb = open(baseName);
		dic = (Dictionnary) odb.getObjects(query).first();
		dic.addEntry("olivier", "Smadja");
		odb.store(dic);
		odb.close();

		odb = open(baseName);
		dic = (Dictionnary) odb.getObjects(query).first();

		assertNotNull(dic.getMap());
		assertEquals("Smadja", dic.getMap().get("olivier"));
		odb.close();

	}

    @Test
	public void test6updateDecreasingSize() throws Exception {
		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);

		long n = odb.query(Dictionnary.class).count().longValue();
		Query query = odb.query(Dictionnary.class, W.equal("name", "test2"));
		Objects l = query.objects();
		Dictionnary dictionnary = (Dictionnary) l.first();
		int mapSize = dictionnary.getMap().size();
		dictionnary.getMap().remove("f1");
		odb.store(dictionnary);
		odb.close();

		odb = open(baseName);
		assertEquals(n, odb.query(Dictionnary.class).count().longValue());
		Dictionnary dic = (Dictionnary) odb.query(query).objects().first();
		assertEquals(mapSize - 1, dic.getMap().size());
		odb.close();
	}

    @Test
	public void test6updateChangingKeyValue() throws Exception {

		String baseName = getBaseName();
		setUp(baseName);
		ODB odb = open(baseName);
		
		long n = odb.query(Dictionnary.class).count().longValue();
		Query query = odb.query(Dictionnary.class, W.equal("name", "test2"));
		Objects l = odb.getObjects(query);
		Dictionnary dictionnary = (Dictionnary) l.first();
		dictionnary.getMap().put("f1", "changed function");
		odb.store(dictionnary);
		odb.close();

		odb = open(baseName);
		assertEquals(n, odb.query(Dictionnary.class).count().longValue());
		Dictionnary dic = (Dictionnary) odb.getObjects(query).first();
		assertEquals("changed function", dic.getMap().get("f1"));
		odb.close();
	}

}
