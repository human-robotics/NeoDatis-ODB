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

import org.junit.Test;
import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.arraycollectionmap.PlayerWithArray;

import java.math.BigDecimal;
import java.util.Date;

public class TestArray extends ODBTest {


    @Test
	public void testArray0() throws Exception {
		ODB odb = null;
		try {
			NeoDatisConfig config = NeoDatis.getConfig().setDebugLayers(false);
			odb = open(getBaseName(),config);
			ClassWithSimpleArray c = new ClassWithSimpleArray("kiko",1,2,10);

			odb.store(c);
			odb.close();

			odb = open(getBaseName(),config);
			Objects l = odb.query(ClassWithSimpleArray.class).objects();

			assertEquals(1, l.size());

		} catch (Exception e) {
			if (odb != null) {
				odb.rollback();
				odb = null;
			}
			throw e;
		} finally {
			if (odb != null) {
				odb.close();
			}
		}

	}
    @Test
	public void testArray1() throws Exception {
		ODB odb = null;
		try {
			odb = open(getBaseName());
			long nb = odb.query(PlayerWithArray.class).count().longValue();
			PlayerWithArray player = new PlayerWithArray("kiko");
			player.addGame("volley-ball");
			player.addGame("squash");
			player.addGame("tennis");
			player.addGame("ping-pong");

			odb.store(player);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.query(PlayerWithArray.class).objects();

			assertEquals(nb + 1, l.size());

			// gets first player
			PlayerWithArray player2 = (PlayerWithArray) l.first();
			assertEquals(player.toString(), player2.toString());
		} catch (Exception e) {
			if (odb != null) {
				odb.rollback();
				odb = null;
			}
			throw e;
		} finally {
			if (odb != null) {
				odb.close();
			}
		}

	}

    @Test
	public void testArray2() throws Exception {
		ODB odb = null;
		int size = 50;

		try {
			odb = open(getBaseName());
			int[] intArray = new int[size];
			for (int i = 0; i < size; i++) {
				intArray[i] = i;
			}
			ObjectWithNativeArrayOfInt owna = new ObjectWithNativeArrayOfInt("t1", intArray);
			odb.store(owna);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.getObjects(ObjectWithNativeArrayOfInt.class);
			ObjectWithNativeArrayOfInt owna2 = (ObjectWithNativeArrayOfInt) l.first();
			assertEquals(owna.getName(), owna2.getName());
			for (int i = 0; i < size; i++) {
				assertEquals(owna.getNumbers()[i], owna2.getNumbers()[i]);
			}
			odb.close();
			odb = null;
		} catch (Exception e) {
			if (odb != null) {
				odb.rollback();
				odb = null;
			}
			throw e;
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}
    @Test
	public void testArray3() throws Exception {
		ODB odb = null;
		int size = 50;

		try {
			odb = open(getBaseName());
			short[] array = new short[size];
			for (int i = 0; i < size; i++) {
				array[i] = (short) i;
			}
			ObjectWithNativeArrayOfShort owna = new ObjectWithNativeArrayOfShort("t1", array);
			odb.store(owna);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.getObjects(ObjectWithNativeArrayOfShort.class);
			ObjectWithNativeArrayOfShort owna2 = (ObjectWithNativeArrayOfShort) l.first();
			assertEquals(owna.getName(), owna2.getName());
			for (int i = 0; i < size; i++) {
				assertEquals(owna.getNumbers()[i], owna2.getNumbers()[i]);
			}
			odb.close();
			odb = null;
		} catch (Exception e) {
			if (odb != null) {
				odb.rollback();
				odb = null;
			}
			throw e;
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}
    @Test
	public void testArrayQuery() throws Exception {
		ODB odb = null;

		try {
			odb = open(getBaseName());
			long nb = odb.query(PlayerWithArray.class).count().longValue();
			PlayerWithArray player = new PlayerWithArray("kiko");
			player.addGame("volley-ball");
			player.addGame("squash");
			player.addGame("tennis");
			player.addGame("ping-pong");

			odb.store(player);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.query(PlayerWithArray.class, W.contain("games", "tennis")).objects();
			assertEquals(nb + 1, l.size());

		} catch (Exception e) {
			if (odb != null) {
				odb.rollback();
				odb = null;
			}
			throw e;
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}
    @Test
	public void testArray4() throws Exception {
		ODB odb = null;
		int size = 50;

		try {
			odb = open(getBaseName());
			BigDecimal[] array = new BigDecimal[size];
			for (int i = 0; i < size; i++) {
				array[i] = new BigDecimal(((double) i) * 78954545 / 89);
			}
			ObjectWithNativeArrayOfBigDecimal owna = new ObjectWithNativeArrayOfBigDecimal("t1", array);
			odb.store(owna);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.getObjects(ObjectWithNativeArrayOfBigDecimal.class);
			ObjectWithNativeArrayOfBigDecimal owna2 = (ObjectWithNativeArrayOfBigDecimal) l.first();
			assertEquals(owna.getName(), owna2.getName());
			for (int i = 0; i < size; i++) {
				assertEquals(owna.getNumbers()[i], owna2.getNumbers()[i]);
			}
			odb.close();
			odb = null;
		} catch (Exception e) {
			if (odb != null) {
				odb.rollback();
				odb = null;
			}
			throw e;
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}
    @Test
	public void testArrayOfDate() throws Exception {
		ODB odb = null;
		int size = 50;

		try {
			odb = open(getBaseName());
			Date[] array = new Date[size];
			Date now = new Date();
			for (int i = 0; i < size; i++) {
				array[i] = new Date(now.getTime() + i);
			}
			ObjectWithNativeArrayOfDate owna = new ObjectWithNativeArrayOfDate("t1", array);
			odb.store(owna);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.getObjects(ObjectWithNativeArrayOfDate.class);
			ObjectWithNativeArrayOfDate owna2 = (ObjectWithNativeArrayOfDate) l.first();
			assertEquals(owna.getName(), owna2.getName());
			for (int i = 0; i < size; i++) {
				assertEquals(owna.getNumbers()[i], owna2.getNumbers()[i]);
			}
			odb.close();
			odb = null;
		} catch (Exception e) {
			if (odb != null) {
				odb.rollback();
				odb = null;
			}
			throw e;
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}




	/**
	 * Increasing array size
	 * 
	 * @throws Exception
	 */
    @Test
	public void testArray6UpdateIncreasingArraySize() throws Exception {
		ODB odb = null;
		int size = 50;

		try {
			odb = open(getBaseName());
			BigDecimal[] array = new BigDecimal[size];
			BigDecimal[] array2 = new BigDecimal[size + 1];
			for (int i = 0; i < size; i++) {
				array[i] = new BigDecimal(((double) i) * 78954545 / 89);
				array2[i] = new BigDecimal(((double) i) * 78954545 / 89);
			}
			array2[size] = new BigDecimal("100");

			ObjectWithNativeArrayOfBigDecimal owna = new ObjectWithNativeArrayOfBigDecimal("t1", array);
			odb.store(owna);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.getObjects(ObjectWithNativeArrayOfBigDecimal.class);
			ObjectWithNativeArrayOfBigDecimal owna2 = (ObjectWithNativeArrayOfBigDecimal) l.first();
			owna2.setNumbers(array2);
			odb.store(owna2);
			odb.close();

			odb = open(getBaseName());
			l = odb.getObjects(ObjectWithNativeArrayOfBigDecimal.class);
			ObjectWithNativeArrayOfBigDecimal o = (ObjectWithNativeArrayOfBigDecimal) l.first();
			assertEquals(size + 1, o.getNumbers().length);
			assertEquals(new BigDecimal("100"), o.getNumber(size));
			assertEquals(owna2.getNumber(1), o.getNumber(1));

		} catch (Exception e) {
			if (odb != null) {
				odb.rollback();
				odb = null;
			}
			throw e;
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	/**
	 * Decreasing array size
	 * 
	 * @throws Exception
	 */
    @Test
	public void testArrayUpdateDecreasingArraySize() throws Exception {
		ODB odb = null;
		int size = 50;

		try {
			odb = open(getBaseName());
			BigDecimal[] array = new BigDecimal[size];
			BigDecimal[] array2 = new BigDecimal[size + 1];
			for (int i = 0; i < size; i++) {
				array[i] = new BigDecimal(((double) i) * 78954545 / 89);
				array2[i] = new BigDecimal(((double) i) * 78954545 / 89);
			}
			array[size - 1] = new BigDecimal("99");
			array2[size] = new BigDecimal("100");

			ObjectWithNativeArrayOfBigDecimal owna = new ObjectWithNativeArrayOfBigDecimal("t1", array2);
			odb.store(owna);
			odb.close();

			odb = open(getBaseName());
			Objects l = odb.getObjects(ObjectWithNativeArrayOfBigDecimal.class);
			ObjectWithNativeArrayOfBigDecimal owna2 = (ObjectWithNativeArrayOfBigDecimal) l.first();
			owna2.setNumbers(array);
			odb.store(owna2);
			odb.close();

			odb = open(getBaseName());
			l = odb.getObjects(ObjectWithNativeArrayOfBigDecimal.class);
			ObjectWithNativeArrayOfBigDecimal o = (ObjectWithNativeArrayOfBigDecimal) l.first();
			assertEquals(size, o.getNumbers().length);
			assertEquals(new BigDecimal("99"), o.getNumber(size - 1));
			assertEquals(owna2.getNumber(1), o.getNumber(1));

			odb = null;
		} catch (Exception e) {
			if (odb != null) {
				odb.rollback();
				odb = null;
			}
			throw e;
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	
}
