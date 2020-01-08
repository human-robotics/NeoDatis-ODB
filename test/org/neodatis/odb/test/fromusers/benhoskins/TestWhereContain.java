package org.neodatis.odb.test.fromusers.benhoskins;

import junit.framework.Assert;
import org.neodatis.odb.*;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

import java.util.ArrayList;
import java.util.Collection;

public class TestWhereContain extends ODBTest {


	public void testShouldRetrievePondByDuck() throws Exception {
		String baseName = getBaseName();
		
		//OdbConfiguration.setReconnectObjectsToSession(true);
		//OdbConfiguration.useMultiThread(true, 20);
		//OdbConfiguration.setDatabaseCharacterEncoding("UTF-8");
		ODBServer server = NeoDatis.openServer(19998);
		server.addBase(baseName, baseName, "elmer", "blunderbus");
		server.startServer(true);
		ODB odb = server.openClient(baseName);

		Duck daffy = new Duck("daffy");
		Duck donald = new Duck("donald");

		Pond pond = new Pond("swan_lake");
		pond.land(daffy);

		odb.store(daffy);
		odb.store(donald);
		odb.store(pond);
		odb.commit();

		// check the duck has safely landed on the pond
		Pond retrievedPond = (Pond) odb.query(Pond.class, W.equal("name", "swan_lake")).objects().first();
		Assert.assertEquals(daffy, retrievedPond.ducks.iterator().next());

		// check can retrieve the pond by the duck swimming on it
		Duck retrievedDaffy = (Duck) odb.query(Duck.class, W.equal("name", "daffy")).objects().first();
		Assert.assertEquals("daffy", retrievedDaffy.name);

		// fails with "No more object in collection" when executing the line
		// below
		Pond retrievedByContainsPond1 = odb.query(Pond.class).<Pond>objects().first();
		
		Query q = odb.query(Pond.class, W.contain("ducks", retrievedDaffy));
		
		Pond retrievedByContainsPond = odb.query(Pond.class).<Pond>objects().first();
		Assert.assertEquals(pond, retrievedByContainsPond);

		Objects<Pond> objects = odb.query(Pond.class, W.contain("ducks", donald)).objects();
		Assert.assertFalse(objects.hasNext());

	}

	private static final class Pond {
		private Collection<Duck> ducks;
		private final String name;

		public Pond(String name) {
			this.name = name;
			ducks = new ArrayList<Duck>();
		}

		private void land(Duck duck) {
			ducks.add(duck);
		}
	}

	private static final class Duck {
		private String name;

		public Duck(String name) {
			this.name = name;
		}
	}

}
