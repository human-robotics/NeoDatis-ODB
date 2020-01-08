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
package org.neodatis.odb.test.server.trigger.oid;

import org.neodatis.odb.*;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Profile;

public class TestOIDTrigger extends ODBTest {


	public void testSettingOIDToField() throws Exception {
		ODB odb = null;
		String baseName = getBaseName();
		ODBServer myServer = null;
		int port = 12003;
		try {

			// Creates the server
			myServer = NeoDatis.openServer(port);
			// Adds the base for the test
			myServer.addBase(baseName, baseName);
			// Adds the insert trigger
			myServer.addOidTrigger(baseName, Tracklet.class.getName(), new MyOidTrigger());
			// Starts the server
			myServer.startServer(true);

			// Then open the client
			odb = NeoDatis.openClient("localhost", port, baseName);

			// Creates the object to be inserted
			Tracklet t = new Tracklet("tracklet 1");

			// Call the store
			OID oid = odb.store(t);

			assertEquals(oid.oidToString(), t.getId());
			
			odb.close();

			odb = NeoDatis.openClient("localhost", port, baseName);
			Tracklet t2 = (Tracklet) odb.query(Tracklet.class).objects().first();
			assertEquals(oid.oidToString(), t2.getId());

		} finally {
			if (odb != null) {
				odb.close();
			}
			if (myServer != null) {
				myServer.close();
			}
		}
	}
	
	public void testSettingOIDToFieldIn2Connections() throws Exception {
		ODB odb = null;
		String baseName = getBaseName();
		ODBServer myServer = null;
		int port = 12003;
		try {

			// Creates the server
			myServer = NeoDatis.openServer(port);
			// Adds the base for the test
			myServer.addBase(baseName, baseName);
			// Adds the insert trigger
			myServer.addOidTrigger(baseName, Tracklet.class.getName(), new MyOidTrigger());
			// Starts the server
			myServer.startServer(true);

			// Then open the client
			odb = NeoDatis.openClient("localhost", port, baseName);

			// Creates the object to be inserted
			Tracklet t = new Tracklet("tracklet 1");

			// Call the store
			OID oid = odb.store(t);

			assertEquals(oid.oidToString(), t.getId());
			
			odb.close();

			odb = NeoDatis.openClient("localhost", port, baseName);
			
			// Creates the object to be inserted
			Tracklet t2 = new Tracklet("tracklet 2");

			// Call the store
			OID oid2 = odb.store(t2);

			odb.close();

			odb = NeoDatis.openClient("localhost", port, baseName);
			assertEquals(2, odb.query(Tracklet.class, W.isNotNull("id")).count().intValue());
			
		} finally {
			if (odb != null) {
				odb.close();
			}
			if (myServer != null) {
				myServer.close();
			}
		}
	}
	
	public void testSettingOIDToField2() throws Exception {
		ODB odb = null;
		String baseName = getBaseName();
		ODBServer myServer = null;
		int port = 12003;
		try {

			// Creates the server
			myServer = NeoDatis.openServer(port);
			// Adds the base for the test
			myServer.addBase(baseName, baseName);
			// Adds the insert trigger
			myServer.addOidTrigger(baseName, null, new MyOidTrigger());
			// Starts the server
			myServer.startServer(true);

			// Then open the client
			odb = NeoDatis.openClient("localhost", port, baseName);

			ObjectWithAutoIncrementId o1 = new ObjectWithAutoIncrementId("Object 1");
			ObjectWithAutoIncrementId o2 = new ObjectWithAutoIncrementId("Object 2");
			o1.setO(o2);

			// store the object
			OID oid = odb.store(o1);

			// Call the store twice to test update!
			oid = odb.store(o1);

			assertEquals(oid.oidToString(), o1.getId());
			assertNotNull(o2.getId());
			
			odb.close();

			odb = NeoDatis.openClient("localhost", port, baseName);
			ObjectWithAutoIncrementId t2 = (ObjectWithAutoIncrementId) odb.query(ObjectWithAutoIncrementId.class, W.equal("name", o1.getName())).objects().first();
			assertEquals(oid.oidToString(), t2.getId());

		} finally {
			if (odb != null) {
				odb.close();
			}
			if (myServer != null) {
				myServer.close();
			}
		}
	}
	public void testSettingOIDToFieldAsObject() throws Exception {
		ODB odb = null;
		ODBServer myServer = null;
		String baseName = getBaseName();
		int port = 12003;
		try {

			// Creates the server
			myServer = NeoDatis.openServer(port);
			// Adds the base for the test
			myServer.addBase(baseName, baseName);
			// Adds the insert trigger
			myServer.addOidTrigger(baseName, ClassWithOid.class.getName(), new MyOidTriggerAsObject());
			// Starts the server
			myServer.startServer(true);

			// Then open the client
			odb = NeoDatis.openClient("localhost", port, baseName);

			// Creates the object to be inserted
			ClassWithOid coi = new ClassWithOid("object 1");

			// Call the store
			ObjectOid oid = odb.store(coi);

			//assertEquals(oid, coi.getOid());
			
			odb.close();

			odb = NeoDatis.openClient("localhost", port, baseName);
			ClassWithOid coi2 = (ClassWithOid) odb.getObjectFromId(oid);
			assertEquals(oid.oidToString(), coi2.getOid().oidToString());

		} finally {
			if (odb != null) {
				odb.close();
			}
			if (myServer != null) {
				myServer.close();
			}
		}
	}
	
	public void testSettingObjectOIDToFieldAsObject() throws Exception {
		ODB odb = null;
		ODBServer myServer = null;
		String baseName = getBaseName();
		int port = 12003;
		try {

			// Creates the server
			myServer = NeoDatis.openServer(port);
			// Adds the base for the test
			myServer.addBase(baseName, baseName);
			// Adds the insert trigger
			myServer.addOidTrigger(baseName, ClassWithObjectOid.class.getName(), new MyOidTriggerAsObject());
			// Starts the server
			myServer.startServer(true);

			// Then open the client
			odb = NeoDatis.openClient("localhost", port, baseName);

			// Creates the object to be inserted
			ClassWithObjectOid coi = new ClassWithObjectOid("object 1");

			// Call the store
			ObjectOid oid = odb.store(coi);

			//assertEquals(oid, coi.getOid());
			
			odb.close();

			odb = NeoDatis.openClient("localhost", port, baseName);
			ClassWithObjectOid coi2 = (ClassWithObjectOid) odb.getObjectFromId(oid);
			assertEquals(oid.oidToString(), coi2.getOid().oidToString());

		} finally {
			if (odb != null) {
				odb.close();
			}
			if (myServer != null) {
				myServer.close();
			}
		}
	}
	
	public void testSimpleStore() throws Exception {
		ODB odb = null;
		ODBServer myServer = null;
		String baseName = getBaseName();
		int port = 12003;
		try {

			// Creates the server
			myServer = NeoDatis.openServer(port);
			// Adds the base for the test
			myServer.addBase(baseName, baseName);
			// Adds the insert trigger
			//myServer.addOidTrigger(baseName, ClassWithOid.class.getName(), new MyOidTriggerAsObject());
			// Starts the server
			myServer.startServer(true);

			// Then open the client
			odb = NeoDatis.openClient("localhost", port, baseName);

			// Creates the object to be inserted
			ClassWithObjectOid coi = new ClassWithObjectOid("object 1");

			// Call the store
			ObjectOid oid = odb.store(coi);
			coi.setOid(oid);
			odb.store(coi);

			odb.close();

			odb = NeoDatis.openClient("localhost", port, baseName);
			Objects<ClassWithObjectOid> objects = odb.query(ClassWithObjectOid.class ).objects(); 
			assertEquals(1, objects.size());
			ClassWithObjectOid c2 = objects.first();
			assertEquals(oid, c2.getOid());

		} finally {
			if (odb != null) {
				odb.close();
			}
			if (myServer != null) {
				myServer.close();
			}
		}
	}
	public void testSettingOIDWhenSelecting() throws Exception {
		ODB odb = null;
		ODBServer myServer = null;
		String baseName = getBaseName();
		int port = 12003;
		try {

			// Creates the server
			myServer = NeoDatis.openServer(port);
			// Adds the base for the test
			myServer.addBase(baseName, baseName);
			// Adds the insert trigger
			myServer.addSelectTrigger(baseName, Tracklet.class.getName(), new MySelectTrigger());
			// Starts the server
			myServer.startServer(true);

			// Then open the client
			odb = NeoDatis.openClient("localhost", port, baseName);
			int size = 10;
			OID[] oids = new OID[size];
			for(int i=0;i<size;i++){
				// Creates the object to be inserted
				Tracklet t = new Tracklet(i+"tracklet ");
				// Call the store
				oids[i] = odb.store(t);
			}

			odb.close();
			
			odb = NeoDatis.openClient("localhost", port, baseName);
			Objects<Tracklet> tt = odb.query(Tracklet.class).orderByAsc("name").objects();
			int i=0;
			while(tt.hasNext()){
				Tracklet t = (Tracklet) tt.next();
				assertEquals(oids[i].oidToString(), t.getId());
				i++;
			}
			

			

		} finally {
			if (odb != null) {
				odb.close();
			}
			if (myServer != null) {
				myServer.close();
			}
		}
	}
	
	
	public void testSettingOIDWithNonNativeObject() throws Exception {
		if(!testNewFeature){
			return;
		}
		String baseName = getBaseName();
		ODB odb = null;
		ODBServer myServer = null;
		int port = 12003;
		try {

			// Creates the server
			myServer = NeoDatis.openServer(port);
			// Adds the base for the test
			myServer.addBase(baseName, baseName);
			// Adds the insert trigger
			myServer.addOidTrigger(baseName, Profile.class.getName(), new MyOidTrigger2());
			// Starts the server
			myServer.startServer(true);

			// Then open the client
			odb = NeoDatis.openClient("localhost", port, baseName);

			// Creates the object to be inserted
			ClassA a = new ClassA("profile");

			// Call the store
			OID oid = odb.store(a);

			assertEquals(oid.oidToString(), a.getB().getId());
			
			odb.close();

			odb = NeoDatis.openClient("localhost", port, baseName);
			ClassA a2 = (ClassA) odb.query(Profile.class).objects().first();
			assertEquals(oid.oidToString(), a2.getB().getId());

		} finally {
			if (odb != null) {
				odb.close();
			}
			if (myServer != null) {
				myServer.close();
			}
		}
	}
	
	
}
