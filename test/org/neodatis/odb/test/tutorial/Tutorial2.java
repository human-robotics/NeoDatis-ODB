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

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

public class Tutorial2 extends ODBTest {
	

	public void step20() throws Exception {
		// Create instance
		Sport sport = new Sport("volley-ball");

		ODB odb = null;
		ODBServer server = null;
		try {
			// Creates the server on port 8000
			server = openServer(8000);
			// Tells the server to manage base 'base1' that points to the file
			// tutorial2.odb
			String baseName = getBaseName();
			server.addBase("base1", baseName);
			// Then starts the server to run in background
			server.startServer(true);

			// Open the databse client on the localhost on port 8000 and specify
			// which database instance
			odb = openClient("base1", NeoDatis.getConfig().setHostAndPort("localhost", 8000));

			// Store the object
			odb.store(sport);
		} finally {
			if (odb != null) {
				// First close the client
				odb.close();
			}
			if (server != null) {
				// Then close the database server
				server.close();
			}
		}
	}

	public void displayObjectsOf(Class clazz, String label1, String label2) throws Exception {
		// Open the database
		ODB odb = null;

		try {
			odb = open(getBaseName());
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

	public void test1() throws Exception {

		step20();
	}

	public static void main(String[] args) throws Exception {

		Tutorial2 tutorial2 = new Tutorial2();

		tutorial2.step20();
		tutorial2.displayObjectsOf(Sport.class, "Step 20", " sport(s):");

	}

}
