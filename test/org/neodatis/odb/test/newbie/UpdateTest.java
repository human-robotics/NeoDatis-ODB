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
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.newbie.vo.Car;
import org.neodatis.odb.test.newbie.vo.Driver;

/**
 * It is just a simple test to help the newbies
 * 
 * @author mayworm at <xmpp://mayworm@gmail.com>
 * 
 */
public class UpdateTest extends ODBTest {

	protected static ODB odb;

	/**
	 * This method is considering the {@link InsertTest}, so it should be called
	 * in a correct order
	 */
	public void testUpdate() {
		try {
			odb = open(getBaseName());
			Driver marcelo = new Driver("marcelo");
			Car car = new Car("car1", 4, "ranger", marcelo);
			odb.store(car);
			Query query = odb.query(Car.class, W.equal("driver.name", "marcelo"));
			Car newCar = (Car) query.objects().first();
			newCar.setDriver(new Driver("dani"));
			odb.store(newCar);
			odb.commit();
			query = odb.query(Car.class, W.equal("driver.name", "dani"));
			assertEquals(1, query.objects().size());
			odb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
