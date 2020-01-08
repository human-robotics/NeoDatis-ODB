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
package org.neodatis.odb.test.newbie;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.newbie.vo.Car;
import org.neodatis.odb.test.newbie.vo.Driver;

import java.io.IOException;

/**
 * It is just a simple test to help the newbies
 * 
 * @author mayworm at <xmpp://mayworm@gmail.com>
 * 
 */
public class InsertTest extends ODBTest {
	protected static ODB odb;

	/**
	 * Insert different objects on database
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public void testInsert() throws IOException, Exception {
		odb = open(getBaseName());
		Driver marcelo = new Driver("marcelo");
		Car car = new Car("car1", 4, "ranger", marcelo);
		Car car1 = new Car("car2", 2, "porche");
		Car car2 = new Car("car3", 2, "fusca");
		Car car3 = new Car("car4", 4, "opala");
		Car car4 = new Car("car5", 4, "vectra", marcelo);

		try {
			// open is called on NewbieTest
			// insert 5 car's
			odb.store(car);
			odb.store(car1);
			odb.store(car2);
			odb.store(car3);
			odb.store(car4);

			// find for all car objects
			Objects cars = odb.query(Car.class).objects();
			assertEquals("The objects weren't added correctly", 5, cars.size());

			// find for a specific car object
			Query query = odb.query(Car.class, W.equal("name", "car1"));
			cars = query.objects();
			assertEquals("The objects couldn't be found correctly", 1, cars.size());

			// find for a specific composition
			query = odb.query(Car.class, W.equal("driver.name", "marcelo"));
			cars = query.objects();
			assertEquals("The objects couldn't be found correctly", 2, cars.size());
			odb.commit();

			odb.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
