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

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.country.City;
import org.neodatis.odb.test.vo.country.Country;

public class TestCyclicReference2 extends ODBTest {

	public void setUp() throws Exception {
		super.setUp();
		ODB odb = open(getBaseName());
		Country brasil = new Country("Brasil");

		for (int i = 0; i < 10; i++) {
			City city = new City("city" + i);

			city.setCountry(brasil);
			brasil.addCity(city);
		}
		odb.store(brasil);
		odb.close();
	}

	public void test1() throws Exception {

		ODB odb = open(getBaseName());
		Objects l = odb.query(Country.class).objects();
		
		Country country = (Country) l.first();
		assertEquals("Brasil", country.getName());
		odb.close();
		
	}

	public void test2() throws Exception {
		println("-------------------");
		// LogUtil.logOn(ObjectWriter.LOG_ID, true);
		// LogUtil.logOn(ObjectReader.LOG_ID, true);
		ODB odb = open(getBaseName());
		Objects l = odb.query(Country.class).objects();
		Country country = (Country) l.first();
		assertEquals(10, country.getCities().size());
		
		odb.query(Country.class).getQueryParameters().setLoadDepth(1);
		
		odb.close();

	}

	public void testLazyLoad() throws Exception {
		ODB odb = open(getBaseName());
		Query q = odb.query(Country.class);
		q.getQueryParameters().setLoadDepth(1);
		
		Objects l = q.objects();
		Country country = (Country) l.first();
		assertEquals(10, country.getCities().size());

		odb.close();
		
		/*
		Criterion c = W.equal("string1", "test class 2").or(W.equal("string1", "test class 3")).or(W.equal("string1", "test class 4")).or(W.equal("string1", "test class 5"));
		Query query = odb.query(AllAttributeClass.class, c);
		query.orderByDesc("boolean1,int1");
		*/

	}

}
