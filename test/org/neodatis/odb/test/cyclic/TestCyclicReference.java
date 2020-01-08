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
package org.neodatis.odb.test.cyclic;

import org.junit.Before;
import org.junit.Test;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.MetaModel;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.country.City;
import org.neodatis.odb.test.vo.country.Country2;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

public class TestCyclicReference extends ODBTest {

    @Before
	public void setUp() throws Exception {
		super.setUp();
		ODB odb = open(getBaseName());
		for (int i = 0; i < 1; i++) {
			City brasilia = new City("Brasilia" + i);
			Country2 brasil = new Country2("Brasil" + i);

			brasilia.setCountry(brasil);
			brasil.setCapital(brasilia);
			brasil.setPopulation(450000);

			odb.store(brasil);
		}
		odb.store(new User("name", "email", new Profile("profile")));
		odb.close();
	}
    @Test
	public void test1() throws Exception {

		ODB odb = open(getBaseName());
		Objects l = odb.query(Country2.class).objects();
		Country2 country = (Country2) l.first();
		assertEquals("Brasil0", country.getName());
		assertEquals("Brasilia0", country.getCapital().getName());
		odb.close();
	}
    @Test
	public void test15() throws Exception {
		println("-------------------");
		// LogUtil.logOn(ObjectWriter.LOG_ID, true);
		// LogUtil.logOn(ObjectReader.LOG_ID, true);
		ODB odb = open(getBaseName());
		Objects l = odb.query(Country2.class).objects();
		Country2 country = (Country2) l.first();

		City city = country.getCapital();
		city.setName("rio de janeiro");
		country.setCapital(city);

		odb.store(country);
		odb.close();

		odb = open(getBaseName());
		l = odb.query(Country2.class).objects();
		country = (Country2) l.first();
		assertEquals("rio de janeiro", country.getCapital().getName());
		l = odb.query(City.class, W.equal("name", "rio de janeiro")).objects();
		assertEquals(1, l.size());
		l = odb.query(City.class).objects();
		assertEquals(1, l.size());
		odb.close();

	}
    @Test
	public void test2() throws Exception {
		ODB odb = open(getBaseName());
		Objects l = odb.query(Country2.class).objects();
		Country2 country = (Country2) l.first();

		City city = new City("rio de janeiro");
		country.setCapital(city);

		odb.store(country);
		odb.close();

		odb = open(getBaseName());
		l = odb.query(Country2.class).objects();
		country = (Country2) l.first();
		assertEquals("rio de janeiro", country.getCapital().getName());
		l = odb.query(City.class, W.equal("name", "rio de janeiro")).objects();
		assertEquals(1, l.size());
		l = odb.query(City.class).objects();
		assertEquals(2, l.size());
		odb.close();

	}
    @Test
	public void testUniqueInstance1() throws Exception {
		ODB odb = open(getBaseName());

		Objects cities = odb.query(City.class).objects();
		Objects countries = odb.query(Country2.class).objects();

		Country2 country = (Country2) countries.first();
		City city = (City) cities.first();

		assertTrue(country == city.getCountry());
		assertTrue(city == country.getCities().get(0));

		assertTrue(city == country.getCapital());
		odb.close();

	}
    @Test
	public void testUniqueInstance2() throws Exception {
		ODB odb = open(getBaseName());

		Objects countries = odb.query(Country2.class).objects();
		Objects cities = odb.query(City.class).objects();

		Country2 country = (Country2) countries.first();
		City city = (City) cities.first();

		assertTrue(country == city.getCountry());
		assertTrue(city == country.getCities().get(0));

		assertTrue(city == country.getCapital());
		odb.close();

	}
    @Test
	public void test10() throws Exception {
		ODB odb = null;

		try {
			String baseName = getBaseName();
			odb = open(baseName);
			ClassA ca = new ClassA();
			ClassB cb = new ClassB(ca, "b");
			ca.setClassb(cb);
			ca.setName("a");
			odb.store(ca);
			MetaModel metaModel = Dummy.getEngine(odb).getSession().getMetaModel();
			assertTrue(metaModel.hasCyclicReference(metaModel.getClassInfo(ClassA.class.getName(),true)));
			assertTrue(metaModel.hasCyclicReference(metaModel.getClassInfo(ClassB.class.getName(),true)));
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}
    @Test
	public void test11() throws Exception {
		ODB odb = null;

		try {
			odb = open(getBaseName());
			odb.store(new User("user","email", new Profile("profile",new Function("f"))));
			MetaModel metaModel = Dummy.getEngine(odb).getSession().getMetaModel();
			ClassInfo ci = metaModel.getClassInfo(User.class.getName(), true);
			boolean b = metaModel.hasCyclicReference(ci);
			assertFalse(b);
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}


}
