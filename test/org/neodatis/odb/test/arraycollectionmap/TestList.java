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
import org.neodatis.odb.test.vo.arraycollectionmap.PlayerWithList;
import org.neodatis.odb.test.vo.sport.Player;
import org.neodatis.odb.test.vo.sport.Sport;
import org.neodatis.odb.test.vo.sport.Team;

import java.util.Date;

public class TestList extends ODBTest {

    @Test
	public void testList1() throws Exception {
		NeoDatisConfig config = NeoDatis.getConfig().setDebugLayers(false);
		ODB odb = open(getBaseName(),config);

		long nb = odb.query(PlayerWithList.class).count().longValue();
		PlayerWithList player = new PlayerWithList("kiko");
		player.addGame("volley-ball");
		player.addGame("squash");
		player.addGame("tennis");
		player.addGame("ping-pong");

		odb.store(player);
		odb.close();

		ODB odb2 = open(getBaseName(),config);
		Objects l = odb2.query(PlayerWithList.class).objects();
		println(l);
		assertEquals(nb + 1, l.size());

		// gets last player
		PlayerWithList player2 = (PlayerWithList) l.first();
		assertEquals(player.toString(), player2.toString());
		odb2.close();
	}
    @Test
	public void testList1WithNull() throws Exception {
		ODB odb = open(getBaseName());

		long nb = odb.query(PlayerWithList.class).count().longValue();
		PlayerWithList player = new PlayerWithList("kiko");
		player.addGame("volley-ball");
		player.addGame("squash");
		player.addGame("tennis");
		player.addGame(null);

		odb.store(player);
		odb.close();

		ODB odb2 = open(getBaseName());
		Objects l = odb2.getObjects(PlayerWithList.class);

		assertEquals(nb + 1, l.size());

		// gets last player
		PlayerWithList player2 = (PlayerWithList) l.first();
		assertEquals(player.getGame(2), player2.getGame(2));
		odb2.close();
	}
    @Test
	public void testList2() throws Exception {
		ODB odb = open(getBaseName());

		long nb = odb.query(PlayerWithList.class).count().longValue();
		PlayerWithList player = new PlayerWithList("kiko");
		player.setGames(null);

		odb.store(player);
		odb.close();
		ODB odb2 = open(getBaseName());
		Objects l = odb2.getObjects(PlayerWithList.class);

		assertEquals(nb + 1, l.size());

		odb2.close();
	}
    @Test
	public void testList3() throws Exception {
		ODB odb = open(getBaseName());
		long nb = odb.query(MyObject.class).count().longValue();
		MyList l1 = new MyList();
		l1.add("object1");
		l1.add("object2");
		MyObject myObject = new MyObject("o1", l1);

		odb.store(myObject);
		odb.close();

		ODB odb2 = open(getBaseName());
		Objects l = odb2.getObjects(MyObject.class);

		assertEquals(nb + 1, l.size());

		odb2.close();
	}

	/** Test update object list. Removing one, adding other */
    @Test
	public void testList4Update() throws Exception {
		ODB odb = open(getBaseName());
		long nb = odb.query(MyObject.class).count().longValue();
		MyList l1 = new MyList();
		l1.add("object1");
		l1.add("object2");
		MyObject myObject = new MyObject("o1", l1);

		odb.store(myObject);
		odb.close();

		ODB odb2 = open(getBaseName());
		Objects l = odb2.getObjects(MyObject.class);
		MyObject mo = (MyObject) l.first();
		mo.getList().remove(1);
		mo.getList().add("object 2bis");
		odb2.store(mo);
		odb2.close();

		odb2 = open(getBaseName());
		l = odb2.getObjects(MyObject.class);
		assertEquals(nb + 1, l.size());

		MyObject mo2 = (MyObject) l.first();
		assertEquals("object1", mo2.getList().get(0));
		assertEquals("object 2bis", mo2.getList().get(1));
		odb2.close();
	}

	/** Test update object list. adding 2 elements */
    @Test
	public void testList4Update2() throws Exception {
		ODB odb = open(getBaseName());
		long nb = odb.query(MyObject.class).count().longValue();
		MyList l1 = new MyList();
		l1.add("object1");
		l1.add("object2");
		MyObject myObject = new MyObject("o1", l1);

		odb.store(myObject);
		odb.close();

		ODB odb2 = open(getBaseName());
		Objects l = odb2.getObjects(MyObject.class);
		MyObject mo = (MyObject) l.first();
		mo.getList().add("object3");
		mo.getList().add("object4");
		odb2.store(mo);
		odb2.close();

		odb2 = open(getBaseName());
		l = odb2.getObjects(MyObject.class);
		assertEquals(nb + 1, l.size());

		MyObject mo2 = (MyObject) l.first();
		assertEquals(4, mo2.getList().size());
		assertEquals("object1", mo2.getList().get(0));
		assertEquals("object2", mo2.getList().get(1));
		assertEquals("object3", mo2.getList().get(2));
		assertEquals("object4", mo2.getList().get(3));
		odb2.close();
	}

	/** Test update object list. A list of Integer */
    @Test
	public void testList4Update3() throws Exception {
		ODB odb = open(getBaseName());

		ObjectWithListOfInteger o = new ObjectWithListOfInteger("test");
		o.getListOfIntegers().add(new Integer("100"));
		odb.store(o);
		odb.close();

		ODB odb2 = open(getBaseName());
		Objects l = odb2.getObjects(ObjectWithListOfInteger.class);
		ObjectWithListOfInteger o2 = (ObjectWithListOfInteger) l.first();

		o2.getListOfIntegers().clear();
		o2.getListOfIntegers().add(new Integer("200"));
		odb2.store(o2);
		odb2.close();

		odb2 = open(getBaseName());
		l = odb2.getObjects(ObjectWithListOfInteger.class);
		assertEquals(1, l.size());

		ObjectWithListOfInteger o3 = (ObjectWithListOfInteger) l.first();
		assertEquals(1, o3.getListOfIntegers().size());
		assertEquals(new Integer("200"), o3.getListOfIntegers().get(0));
		odb2.close();
	}

	/** Test update object list. A list of Integer. 1000 updates */
    @Test
	public void testList4Update4() throws Exception {
		ODB odb = open(getBaseName());

		ObjectWithListOfInteger o = new ObjectWithListOfInteger("test");
		o.getListOfIntegers().add(new Integer("100"));
		odb.store(o);
		odb.close();

		int size = isLocal ? 100 : 100;
		for (int i = 0; i < size; i++) {
			ODB odb2 = open(getBaseName());
			Objects ll = odb2.getObjects(ObjectWithListOfInteger.class);
			ObjectWithListOfInteger o2 = (ObjectWithListOfInteger) ll.first();

			o2.getListOfIntegers().clear();
			o2.getListOfIntegers().add(new Integer(200 + i));
			odb2.store(o2);
			odb2.close();
		}

		ODB odb3 = open(getBaseName());
		Objects l = odb3.getObjects(ObjectWithListOfInteger.class);
		assertEquals(1, l.size());

		ObjectWithListOfInteger o3 = (ObjectWithListOfInteger) l.first();
		assertEquals(1, o3.getListOfIntegers().size());
		assertEquals(new Integer(200 + size - 1), o3.getListOfIntegers().get(0));
		odb3.close();
	}

	/**
	 * Test update object list. A list of Integer. 1000 updates of an object
	 * that is the middle of the list
	 */
    @Test
	public void testList4Update4Middle() throws Exception {
		ODB odb = open(getBaseName());

		ObjectWithListOfInteger o = new ObjectWithListOfInteger("test1");
		o.getListOfIntegers().add(new Integer("101"));
		odb.store(o);

		o = new ObjectWithListOfInteger("test2");
		o.getListOfIntegers().add(new Integer("102"));
		odb.store(o);

		o = new ObjectWithListOfInteger("test3");
		o.getListOfIntegers().add(new Integer("103"));
		odb.store(o);

		odb.close();

		int size = isLocal ? 50 : 10;
		for (int i = 0; i < size; i++) {
			ODB odb2 = open(getBaseName());
			Objects ll = odb2.query(ObjectWithListOfInteger.class, W.equal("name", "test2")).objects();
			ObjectWithListOfInteger o2 = (ObjectWithListOfInteger) ll.first();

			o2.getListOfIntegers().clear();
			o2.getListOfIntegers().add(new Integer(200 + i));
			odb2.store(o2);
			odb2.close();
		}

		ODB odb3 = open(getBaseName());
		Objects l = odb3.query(ObjectWithListOfInteger.class, W.equal("name", "test2")).objects();
		assertEquals(1, l.size());

		ObjectWithListOfInteger o3 = (ObjectWithListOfInteger) l.first();
		assertEquals(1, o3.getListOfIntegers().size());
		assertEquals(new Integer(200 + size - 1), o3.getListOfIntegers().get(0));
		odb3.close();
	}

	/**
	 * Test update object list. A list of Integer. 1000 updates, increasing
	 * number of elements
	 */
    @Test
	public void testList4Update5() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ObjectWithListOfInteger o = new ObjectWithListOfInteger("test");
		o.getListOfIntegers().add(new Integer("100"));
		odb.store(o);
		odb.close();
		
		int size = isLocal ? 50 : 10;
		for (int i = 0; i < size; i++) {
			println(i);
			ODB odb2 = open(baseName);
			Objects ll = odb2.query(ObjectWithListOfInteger.class).objects();
			ObjectWithListOfInteger o2 = (ObjectWithListOfInteger) ll.first();

			o2.getListOfIntegers().add(new Integer(200 + i));
			odb2.store(o2);
			odb2.close();
		}

		ODB odb3 = open(baseName);
		Objects l = odb3.query(ObjectWithListOfInteger.class).objects();
		assertEquals(1, l.size());

		ObjectWithListOfInteger o3 = (ObjectWithListOfInteger) l.first();
		assertEquals(size + 1, o3.getListOfIntegers().size());
		odb3.close();
	}
    @Test
	public void testBigList() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ObjectWithListOfInteger o = new ObjectWithListOfInteger("test");
		for(int i = 0; i<1000; i++){
			o.getListOfIntegers().add(new Integer(i));
		}
		odb.store(o);
		odb.close();
		
		odb = open(baseName);
		Objects ll = odb.query(ObjectWithListOfInteger.class).objects();
		ObjectWithListOfInteger o2 = (ObjectWithListOfInteger) ll.first();
		odb.close();
		assertEquals(1000, o2.getListOfIntegers().size());
		
	}

	/**
	 * Test update object list. A list of Integer. 1000 updates of an object
	 * increasing list nb elements that is the middle of the list
	 */
    @Test
	public void testList4Update4Middle2() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ObjectWithListOfInteger o = new ObjectWithListOfInteger("test1");
		o.getListOfIntegers().add(new Integer("101"));
		odb.store(o);

		o = new ObjectWithListOfInteger("test2");
		o.getListOfIntegers().add(new Integer("102"));
		odb.store(o);

		o = new ObjectWithListOfInteger("test3");
		o.getListOfIntegers().add(new Integer("103"));
		odb.store(o);

		odb.close();

		int size = isLocal ? 100 : 10;
		for (int i = 0; i < size; i++) {
			ODB odb2 = open(baseName);
			Objects ll = odb2.query(ObjectWithListOfInteger.class, W.equal("name", "test2")).objects();
			ObjectWithListOfInteger o2 = (ObjectWithListOfInteger) ll.first();

			o2.getListOfIntegers().add(new Integer(200 + i));
			odb2.store(o2);
			odb2.close();
		}

		ODB odb3 = open(baseName);
		Objects l = odb3.query(ObjectWithListOfInteger.class, W.equal("name", "test2")).objects();
		assertEquals(1, l.size());

		ObjectWithListOfInteger o3 = (ObjectWithListOfInteger) l.first();
		assertEquals(1 + size, o3.getListOfIntegers().size());
		odb3.close();
	}
	/**
	 * one object has a list. we delete one of the object of the list of the
	 * object. And the main object still has it
	 * 
	 * @throws Exception
	 */
    @Test
	public void testDeletingOneElementOfTheList() throws Exception {
		if (!testNewFeature) {
			return;
		}

		String baseName = getBaseName();
		ODB odb = open(baseName);

		Team t1 = new Team("team1");
		t1.addPlayer(new Player("player1", new Date(), new Sport("sport1")));
		t1.addPlayer(new Player("player2", new Date(), new Sport("sport2")));
		odb.store(t1);
		odb.close();

		odb = open(baseName);
		Objects teams = odb.getObjects(Team.class);
		Team team = (Team) teams.first();

		assertEquals(2, team.getPlayers().size());

		Objects players = odb.getObjects(Player.class);
		Player p1 = (Player) players.first();
		odb.delete(p1);
		odb.close();
		assertEquals(1, team.getPlayers().size());
	}
    @Test
	public void testCollectionWithContain() throws Exception {
		ODB odb = null;
		String baseName = getBaseName();
		try {
			odb = open(baseName);
			long nb = odb.query(PlayerWithList.class).count().longValue();
			PlayerWithList player = new PlayerWithList("kiko");
			player.addGame("volley-ball");
			player.addGame("squash");
			player.addGame("tennis");
			player.addGame("ping-pong");

			odb.store(player);
			odb.close();

			odb = open(baseName);
			Objects l = odb.query(PlayerWithList.class, W.contain("games", "tennis")).objects();
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
}
