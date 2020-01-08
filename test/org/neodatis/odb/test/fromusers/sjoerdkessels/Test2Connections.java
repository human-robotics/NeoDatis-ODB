/**
 * 
 */
package org.neodatis.odb.test.fromusers.sjoerdkessels;

import org.neodatis.odb.*;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

import java.util.Date;

/**
 * @author olivier
 * 
 */
public class Test2Connections extends ODBTest {

	public void test1() {
		if (!testNewFeature) {
			return;
		}

		int port = 15000;
		String baseName = getBaseName();

		// create the server
		ODBServer server = NeoDatis.openServer(port);
		// Start the server
		server.startServer(true);

		// Create the index
		ODB c1 = NeoDatis.openClient("localhost", port, baseName);
		ODB c2 = NeoDatis.openClient("localhost", port, baseName);
		ClassRepresentation classRepresentation = c1.getClassRepresentation(Person.class);
		if (!classRepresentation.existIndex("name-index")) {
			classRepresentation.addIndexOn("name-index", new String[] { "name" }, true);
		}

		c1.store(new Person("myname", "myemail", new Date()));
		c1.close();
		Query q = c1.query(Person.class, W.equal("name", "myname"));
		Objects<Person> people = q.objects();
		c2.close();
		server.close();

		assertEquals(true, q.getExecutionPlan().useIndex());

		assertEquals(1, people.size());

	}

	public void test2() {

		int port = 15000;
		String baseName = getBaseName();

		// create the server
		ODBServer server = NeoDatis.openServer(port);
		// Start the server
		server.startServer(true);

		// Create the index
		ODB c1 = NeoDatis.openClient("localhost", port, baseName);

		c1.getClassRepresentation(Person.class).addIndexOn("name-index", new String[] { "name" }, true);
		c1.store(new Person("name", "email", new Date()));
		c1.close();

		ODB c2 = NeoDatis.openClient("localhost", port, baseName);

		Objects<Person> people = c2.getObjects(Person.class);
		c2.close();
		server.close();

		assertEquals(1, people.size());

	}

	public void test3() {
		if (!testNewFeature) {
			return;
		}

		int port = 15000;
		String baseName = getBaseName();

		// create the server
		ODBServer server = NeoDatis.openServer(port);
		// Start the server
		server.startServer(true);

		// Create the index
		ODB c1 = NeoDatis.openClient("localhost", port, baseName);
		ODB c2 = NeoDatis.openClient("localhost", port, baseName);

		c1.store(new Person("name", "email", new Date()));
		c1.close();

		Objects<Person> people = c2.getObjects(Person.class);
		c2.close();
		server.close();

		assertEquals(1, people.size());

	}

	public void test5() {
		if (!testNewFeature) {
			return;
		}

		int port = 15000;
		String baseName = getBaseName();

		// create the server
		ODBServer server = NeoDatis.openServer(port);
		// Start the server
		server.startServer(true);

		ODB c1 = NeoDatis.openClient("localhost", port, baseName);
		c1.store(new Person("name", "email", new Date()));
		c1.close();

		c1 = NeoDatis.openClient("localhost", port, baseName);
		ODB c2 = NeoDatis.openClient("localhost", port, baseName);

		c1.store(new Person("name", "email", new Date()));
		c1.close();

		Objects<Person> people = c2.getObjects(Person.class);
		c2.close();
		server.close();
		assertEquals(2, people.size());

	}

	public void test4() {

		int port = 15000;
		String baseName = getBaseName();

		// create the server
		ODBServer server = NeoDatis.openServer(port);
		// Start the server
		server.startServer(true);

		// Create the index
		ODB c1 = NeoDatis.openClient("localhost", port, baseName);

		c1.store(new Person("name", "email", new Date()));
		c1.close();

		ODB c2 = NeoDatis.openClient("localhost", port, baseName);
		Objects<Person> people = c2.getObjects(Person.class);
		c2.close();
		server.close();
		assertEquals(1, people.size());

	}

}
