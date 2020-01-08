/*
NeoDatis ODB : Native Object Database (odb.info@neodatis.org)
Copyright (C) 2007 NeoDatis Inc. http://www.neodatis.org

This file is part of the db4o open source object database.

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
package org.neodatis.odb.test.tutorial;

import org.neodatis.odb.*;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.core.query.nq.NativeQuery;
import org.neodatis.odb.core.session.cross.CacheFactory;
import org.neodatis.odb.core.session.cross.ICrossSessionCache;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.xml.XMLExporter;
import org.neodatis.odb.xml.XMLImporter;
import org.neodatis.tool.IOUtil;

import java.util.Date;

public class Tutorial1 extends ODBTest {
	protected String baseName;
	protected String baseName2;
	public Tutorial1() throws Exception {
	}

	public void step1() throws Exception {

		// Create instance
		Sport sport = new Sport("volley-ball");

		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
            odb.getConfig().setDebugEnabled(true);
			
			// Store the object
			odb.store(sport);
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	public void step2() throws Exception {

		// Create instance
		Sport volleyball = new Sport("beach-volley-ball");

		// Create 4 players
		Player player1 = new Player("olivier", new Date(), volleyball);
		Player player2 = new Player("pierre", new Date(), volleyball);
		Player player3 = new Player("elohim", new Date(), volleyball);
		Player player4 = new Player("minh", new Date(), volleyball);

		// Create two teams
		Team team1 = new Team("Paris");
		Team team2 = new Team("Montpellier");

		// Set players for team1
		team1.addPlayer(player1);
		team1.addPlayer(player2);

		// Set players for team2
		team2.addPlayer(player3);
		team2.addPlayer(player4);

		// Then create a volley ball game for the two teams
		Game game = new Game(new Date(), volleyball, team1, team2);

		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);

			// Store the object
			odb.store(game);
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/** Criteria Query */
	public void step3() throws Exception {
		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			Objects players = odb.query(Player.class,W.equal("name", "olivier")).objects();

			System.out.println("\nStep 3 : Players with name olivier");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Where query with relations
	 * 
	 */
	public void step4() throws Exception {

		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			// Let's insert a tennis player
			Player agassi = new Player("Andr\u00E9 Agassi", new Date(), new Sport("Tennis"));
			odb.store(agassi);


			Objects players = odb.query(Player.class, W.equal("favoriteSport.name", "beach-volley-ball")).objects();
			
			Objects<Sport> sports = odb.query(Sport.class).objects();

			System.out.println("\nStep 4 : Players of Volley-ball");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Criteria query with relations
	 * 
	 */
	public void step5() throws Exception {

		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			// retrieve the volley ball sport object
			Query query = odb.query(Sport.class, W.equal("name", "beach-volley-ball"));
			Sport volleyBall = (Sport) query.objects().first();

			// Now build a query to get all players that play volley ball, using
			// the volley ball object
			query = odb.query(Player.class, W.equal("favoriteSport", volleyBall));

			Objects players = odb.getObjects(query);

			System.out.println("\nStep 5: Players of Voller-ball");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}

		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Criteria query with relations and OR
	 * 
	 */
	public void step6() throws Exception {
		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			Query query = odb.query(Player.class, W.or().add(W.equal("favoriteSport.name", "volley-ball")).add(
					W.like("favoriteSport.name", "%nnis")));

			Objects players = odb.getObjects(query);

			System.out.println("\nStep 6 : Volley-ball and Tennis Players");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Criteria query with relations and NOT
	 * 
	 */
	public void step7() throws Exception {
		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			Query query = odb.query(Player.class, W.not(W.equal("favoriteSport.name", "volley-ball")));

			Objects players = odb.getObjects(query);

			System.out.println("\nStep 7 : Players that don't play Volley-ball");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}

		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Native query
	 * 
	 */
	public void step8() throws Exception {
		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			Query query = new NativeQuery<Player>() {
				public boolean match(Player player) {
					return player.getFavoriteSport().getName().toLowerCase().startsWith("volley");
				}
			};

			Objects players = odb.query(query).objects();

			System.out.println("\nStep 8 bis: Players that play Volley-ball");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}

		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Native query with Objectss,
	 * 
	 */
	public void step9() throws Exception {
		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);

			// first retrieve the player Minh
			Query query = odb.query(Player.class, W.equal("name", "minh"));
			Player minh = (Player) odb.getObjects(query).first();

			// builds a query to get all teams where mihn plays
			query = odb.query(Team.class, W.contain("players", minh));
			Objects teams = odb.getObjects(query);

			System.out.println("\nStep 9: Team where minh plays");

			int i = 1;
			// display each object
			while (teams.hasNext()) {
				System.out.println((i++) + "\t: " + teams.next());
			}

		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Ordering query result
	 * 
	 * @throws Exception
	 */
	public void step10() throws Exception {
		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			Query query = odb.query(Player.class);
			query.orderByAsc("name");

			Objects players = odb.getObjects(query);

			System.out.println("\nStep 10: Players ordered by name asc");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}

			query.orderByDesc("name");

			players = odb.getObjects(query);

			System.out.println("\nStep 10: Players ordered by name desc");

			i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}

		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	/**
	 * Using indexes
	 * 
	 */
	public void step11() throws Exception {
		System.out.println("\nStart of test 11");
		// Open the database
		ODB odb = null;
		try {
			odb = NeoDatis.open(baseName);
			ICrossSessionCache c = CacheFactory.getCrossSessionCache(odb.getName());
			System.out.println("cross cache id = " + System.identityHashCode(c));
			String[] fieldNames = { "name" };
			System.out.println(odb.query(Sport.class).objects());
			System.out.println("Exist index " + odb.getClassRepresentation(Sport.class).existIndex("sport-index"));
			odb.getClassRepresentation(Sport.class).addUniqueIndexOn("sport-index", fieldNames, true);
			odb.close();

			odb = NeoDatis.open(baseName);
			Query query = odb.query(Sport.class, W.equal("name", "volley-ball"));
			
			c = CacheFactory.getCrossSessionCache(odb.getName());
			System.out.println("cross cache id = " + System.identityHashCode(c));

			Objects sports = odb.query(query).objects();

			System.out.println("\nStep 11 : Using index");

			int i = 1;
			// display each object
			while (sports.hasNext()) {
				System.out.println((i++) + "\t: " + sports.next());
			}

		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Updating objects
	 * 
	 */
	public void step12() throws Exception {
		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			Query query = odb.query(Sport.class, W.equal("name", "volley-ball"));

			Objects sports = odb.getObjects(query);

			// Gets the first sport (there is only one!)
			Sport volley = (Sport) sports.first();

			// Changes the name
			volley.setName("Beach-Volley");

			// Actually updates the object
			odb.store(volley);

			// Commits the changes
			odb.close();

			odb = NeoDatis.open(baseName);
			// Now query the database to check the change
			sports = odb.getObjects(Sport.class);

			System.out.println("\nStep 12 : Updating sport");

			int i = 1;
			// display each object
			while (sports.hasNext()) {
				System.out.println((i++) + "\t: " + sports.next());
			}

		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Deleting objects
	 * 
	 */
	public void step13() throws Exception {

		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			Query query = odb.query(Player.class, W.like("name", "%Agassi"));

			Objects players = odb.getObjects(query);

			// Gets the first player (there is only one!)
			Player agassi = (Player) players.first();

			odb.delete(agassi);

			odb.close();

			odb = NeoDatis.open(baseName);
			// Now query the databas eto check the change
			players = odb.getObjects(Player.class);

			System.out.println("\nStep 13 : Deleting players");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}

		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * Deleting objects using id
	 * 
	 */
	public void step14() throws Exception {
		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);

			Sport tennis = (Sport) odb.query(Sport.class, W.equal("name", "Tennis")).objects().first();
			// Firts re-create Agassi player - it has been deleted in step 13
			Player agassi = new Player("Andr\u00E9 Agassi", new Date(), tennis);
			odb.store(agassi);
			odb.commit();

			Query query = odb.query(Player.class, W.like("name", "%Agassi"));

			Objects players = odb.getObjects(query);

			// Gets the first player (there is only one!)
			agassi = (Player) players.first();
			ObjectOid agassiId = odb.getObjectId(agassi);

			odb.deleteObjectWithId(agassiId);

			odb.close();

			odb = NeoDatis.open(baseName);
			// Now query the databas eto check the change
			players = odb.getObjects(Player.class);

			System.out.println("\nStep 14 : Deleting players");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}

		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	/**
	 * exporting to XML
	 * 
	 */
	public void step15() throws Exception {
		//fail("test is commented");
		
		ODB odb = null;

		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			// Creates the exporter
			XMLExporter exporter = new XMLExporter(odb);

			// Actually export to current directory into the sports.xml file
			exporter.export(DIRECTORY, baseName+".xml");
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
		System.out.println("\nStep 15 : exporting database to " +baseName+".xml");
		
	}

	/**
	 * importing from XML
	 * 
	 */
	public void step16() throws Exception {
		//fail("test is commented");
		
		ODB odb = null;

		try {
            System.out.println("Trying to import " + "/"+baseName+".xml");
			// Open a database to receive imported data
			odb = NeoDatis.open(DIRECTORY+ "imported-" + baseName);
			// Creates the exporter
			XMLImporter importer = new XMLImporter(odb);

			// Actually import data from sports.xml file
			importer.importFile(DIRECTORY, baseName+ ".xml");

			// Closes the database
			odb.close();

			// Re open the database
			odb = NeoDatis.open(DIRECTORY+ "imported-" + baseName);
			// Now query the databas eto check the change
			Objects players = odb.query(Player.class).objects();

			System.out.println("\nStep 16 : getting players of imported database");

			int i = 1;
			// display each object
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
		
	}

	/**
	 * Using user and password to protect the database file
	 * 
	 */
	public void step17() throws Exception {
		ODB odb = null;
		IOUtil.deleteFile(baseName2);
		try {
			// Open the database
			odb = NeoDatis.open(baseName2, "user", "password");
			odb.store(new Sport("Tennis"));
			// Commits the changes
			odb.close();

			try {
				// try to open the database without user/password
				odb = NeoDatis.open(baseName2);
			} catch (ODBAuthenticationRuntimeException e) {
				System.out.println("\nStep 17 : invalid user/password : database could not be opened");
			}
			try{
				// then open the database with correct user/password
				odb = NeoDatis.open(baseName2, "user", "password");
			}catch (Exception e) {
				e.printStackTrace();
				
			}
			System.out.println("\nStep 17 : user/password : database opened");
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	public void displayObjectsOf(Class clazz, String label1, String label2) throws Exception {
		// Open the database
		ODB odb = null;

		try {
			odb = NeoDatis.open(baseName);
			// Get all object of type clazz
			Objects objects = odb.getObjects(clazz);

			System.out.println("\n" + label1 + " : " + objects.size() + label2);

			int i = 1;
			// display each object
			while (objects.hasNext()) {
				System.out.println((i++) + "\t: " + objects.next());
			}

		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	public void clearDatabase() throws Exception {
		Objects objects = null;

		// Open the database
		ODB odb = null;

		try {
			odb = NeoDatis.open(baseName);
			objects = odb.getObjects(Game.class);
			System.out.println("Deleting "+  objects.size()+" games");
			while (objects.hasNext()) {
				odb.delete(objects.next());
			}
			objects = odb.getObjects(Player.class);
			System.out.println("Deleting "+objects.size()+" players");
			while (objects.hasNext()) {
				odb.delete(objects.next());
			}
			objects = odb.getObjects(Sport.class);
			System.out.println("Deleting "+objects.size()+" sports");
			while (objects.hasNext()) {
				odb.delete(objects.next());
			}
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	public void step13bis() throws Exception {
		if (!testNewFeature) {
			return;
		}

		ODB odb = null;
		System.out.println("\nStep 13 : Deleting players");
		try {
			// Open the database
			odb = NeoDatis.open(baseName);
			Query query = odb.query(Player.class, W.like("name", "%Agassi"));
			Objects players = odb.getObjects(query);
			// Gets the first player (there is only one!)
			Player agassi = (Player) players.first();
			// query = odb.query(Team.class);
			System.out.println("\nStep 13.1 : Add player Agassi to Teams");
			Objects teams = odb.getObjects(Team.class);
			while (teams.hasNext()) {
				Team team = (Team) teams.next();
				team.addPlayer(agassi);
				odb.store(team);
			}
			odb.commit();
			teams = odb.getObjects(Team.class);
			while (teams.hasNext()) {
				Team team = (Team) teams.next();
				System.out.println(team);
				assertEquals(2, team.getPlayers().size());
			}
			System.out.println("\nStep 13.2 : Change player Agassi's name to aaaa");
			agassi.setName("aaaa");
			odb.store(agassi);
			System.out.println("\nStep 13.3 : Delete player Agassi");
			odb.delete(agassi);
			odb.commit();
			query = odb.query(Team.class, W.equal("name", "Paris"));
			teams = odb.getObjects(query);
			System.out.println("\nStep 13.4 : Show all Teams");
			while (teams.hasNext()) {
				System.out.println(teams.next());
			}
			players = odb.getObjects(Player.class);
			System.out.println("\nStep 13.5 : Show all players");
			while (players.hasNext()) {
				System.out.println(players.next());
			}

			odb.close();
			odb = NeoDatis.open(baseName);
			System.out.println("\nStep 13.6 : ODB closed and reopened");
			// Now query the database to check the change
			players = odb.getObjects(Player.class);
			int i = 1;
			// display each object
			System.out.println("\nStep 13.7 : Show all players");
			while (players.hasNext()) {
				System.out.println((i++) + "\t: " + players.next());
			}
			System.out.println("\nStep 13.8 : Show all Teams");
			teams = odb.getObjects(Team.class);
			while (teams.hasNext()) {
				System.out.println(teams.next());
			}
		} finally {
			if (odb != null) {
				// Close the database
				odb.close();
			}
		}
	}

	public void test1() throws Exception {
		baseName = getBaseName();
		baseName2 = baseName+"_2";
		step1();
		displayObjectsOf(Sport.class, "Step 1", " sport(s):");
		deleteBase(baseName);

		step2();
		displayObjectsOf(Game.class, "Step 2", " games(s):");
		displayObjectsOf(Team.class, "Step 2", " team(s):");
		displayObjectsOf(Player.class, "Step 2", " player(s):");
		displayObjectsOf(Sport.class, "Step 2", " sport(s):");

		step3();

		step4();
		step5();
		step6();
		step7();
		step8();
		step9();
		step10();
		step11();
		step12();
		// step13();

		step13bis();

		step14();
		step15();
		step16();
		step17();

	}
}
