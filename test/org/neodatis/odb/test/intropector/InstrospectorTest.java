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
package org.neodatis.odb.test.intropector;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.core.layers.layer1.*;
import org.neodatis.odb.core.layers.layer2.meta.*;
import org.neodatis.odb.core.layers.layer2.meta.compare.ChangedNativeAttributeAction;
import org.neodatis.odb.core.layers.layer2.meta.compare.IObjectInfoComparator;
import org.neodatis.odb.core.layers.layer2.meta.compare.ObjectInfoComparator;
import org.neodatis.odb.core.layers.layer2.meta.compare.SetAttributeToNullAction;
import org.neodatis.odb.core.layers.layer4.plugin.memory.InMemoryStorageEngine;
import org.neodatis.odb.core.mock.MockSession;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.oid.OIDFactory;
import org.neodatis.odb.test.vo.inheritance.FootballPlayer;
import org.neodatis.odb.test.vo.inheritance.OutdoorPlayer;
import org.neodatis.odb.test.vo.inheritance.Player;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;
import org.neodatis.tool.wrappers.list.IOdbList;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InstrospectorTest extends ODBTest {
	ClassIntrospector classIntrospector;
	ObjectIntrospector objectIntrospector;
	IntrospectionCallback callback;
	Session session;
	ClassInfo ci;
	
	public void setUp() throws Exception {
		super.setUp();
		InMemoryStorageEngine layer4 = new InMemoryStorageEngine();
		NeoDatisConfig config = NeoDatis.getConfig();
		layer4.init(config);
		
		layer4.open("test",config);
		session = new MockSession("test");
		classIntrospector = config.getCoreProvider().getClassIntrospector(session, layer4.getOidGenerator());
		
		objectIntrospector = config.getCoreProvider().getLocalObjectIntrospector(session, classIntrospector, layer4.getOidGenerator());
		callback = new DefaultInstrospectionCallbackForStore(session,null);
		
		ClassInfoList cilist = classIntrospector.introspect(User.class);
		ci = cilist.getMainClassInfo();
		session.addClasses(cilist);
		session.addClasses(classIntrospector.introspect(Function.class));
	}

	public void testClassInfo() {
		User user = new User("olivier smadja", "olivier@neodatis.com", new Profile("operator", new Function("login")));
		ClassInfoList classInfoList = classIntrospector.introspect(user.getClass());
		assertEquals(user.getClass().getName(), classInfoList.getMainClassInfo().getFullClassName());
		assertEquals(3, classInfoList.getMainClassInfo().getAttributes().size());
		assertEquals(2, classInfoList.getClassInfos().size());

	}

	public void testInstanceInfo() throws Exception {
		User user = new User("olivier smadja", "olivier@neodatis.com", new Profile("operator", new Function("login")));

		NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) objectIntrospector.getMetaRepresentation(user, callback);

		assertEquals(user.getClass().getName(), instanceInfo.getClassInfo().getFullClassName());
		assertEquals("olivier smadja", instanceInfo.getAttributeValueFromId(ci.getAttributeId("name")).toString());
		assertEquals(AtomicNativeObjectInfo.class, instanceInfo.getAttributeValueFromId(ci.getAttributeId("name")).getClass());

	}

	public void testInstanceInfo2() throws Exception {
		User user = new User("olivier smadja", "olivier@neodatis.com", new Profile("operator", new Function("login")));

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
		
		assertEquals(instanceInfo.getClassInfo().getFullClassName(), user.getClass().getName());
		assertEquals(instanceInfo.getAttributeValueFromId(ci.getAttributeId("name")).toString(), "olivier smadja");

	}

	public void testSuperClass() {
		Class collection = Collection.class;
		Class arrayList = ArrayList.class;
		Class string = String.class;

		boolean b1 = collection.isAssignableFrom(arrayList);
		boolean b2 = arrayList.isAssignableFrom(collection);
		boolean b3 = collection.isAssignableFrom(string);
	}

	public void testCompareCollection1() throws Exception {
		User user = new User("olivier smadja", "olivier@neodatis.com", new Profile("operator", new Function("login")));
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		
		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(2);

		user.setName("Olivier Smadja");

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);

		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(1, comparator.getNbChanges());
		assertEquals(1, comparator.getChangedAttributeActions().size());

		ChangedNativeAttributeAction cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(0);
		assertEquals("Olivier Smadja", cnaa.getNoiWithNewValue().getObject());
	}

	public void testCompareCollection11() throws Exception {
		User user = new User("olivier smadja", "olivier@neodatis.com", new Profile("operator", new Function("login")));
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		Object o = instanceInfo.getAttributeValueFromId(2);

		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) o;
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		user.setName("Olivier Smadja");
		user.setEmail("olivier@neodatis.org");

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);

		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(2, comparator.getNbChanges());
		assertEquals(2, comparator.getChangedAttributeActions().size());

		ChangedNativeAttributeAction cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(0);
		assertEquals("Olivier Smadja", cnaa.getNoiWithNewValue().getObject());
		cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(1);
		assertEquals("olivier@neodatis.org", cnaa.getNoiWithNewValue().getObject());
	}

	public void testCompareCollection2() throws Exception {
		User user = new User("olivier smadja", "olivier@neodatis.com", new Profile("operator", new Function("login")));
		IObjectInfoComparator comparator = new ObjectInfoComparator();
		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);

		// Sets attributes offsets - this is normally done by reading them from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		user.setName(null);

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);

		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(1, comparator.getNbChanges());
		assertEquals(1, comparator.getChangedAttributeActions().size());

		ChangedNativeAttributeAction cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(0);
		assertEquals(null, cnaa.getNoiWithNewValue().getObject());

	}

	public void testCompareCollection3CollectionContentChange() throws Exception {
		Function function = new Function("login");
		User user = new User("olivier smadja", "olivier@neodatis.com", new Profile("operator", function));
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		NonNativeObjectInfo nnoi = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(ci.getAttributeId("profile"));
		nnoi.getHeader().setAttributesIdentification(offsets);
		nnoi.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		CollectionObjectInfo nnoi2 = (CollectionObjectInfo) nnoi.getAttributeValueFromId(nnoi.getClassInfo().getAttributeId("functions"));
		NonNativeObjectInfo nnoi3 = (NonNativeObjectInfo) nnoi2.getCollection().iterator().next();
		nnoi3.getHeader().setAttributesIdentification(offsets);

		function.setName("login function");

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);

		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(1, comparator.getNbChanges());
		ChangedNativeAttributeAction cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(0);
		assertEquals(1, comparator.getChangedAttributeActions().size());
		assertEquals(function.getName(), cnaa.getNoiWithNewValue().getObject());

	}

	public void testCompareCollection4CollectionContentChange() throws Exception {
		if (!testNewFeature) {
			return;
		}

		Function function = new Function("login");
		User user = new User("olivier smadja", "olivier@neodatis.com", new Profile("operator", function));
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		function.setName(null);

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);

		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(1, comparator.getNbChanges());
		ChangedNativeAttributeAction cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(0);
		assertEquals(1, comparator.getChangedAttributeActions().size());
		assertEquals(function.getName(), cnaa.getNoiWithNewValue().getObject());

	}

	public void testCompareCollection5() throws Exception {
		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
				

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		profile.getFunctions().add(new Function("logout"));

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);
				
		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(1, comparator.getNbChanges());
		NonNativeObjectInfo nnoi = (NonNativeObjectInfo) comparator.getChangedObjectMetaRepresentation(0);
		assertEquals(2, ((List) nnoi.getValueOf("functions")).size());

	}

	public void testCompareCollection6() throws Exception {
		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
				

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		NonNativeObjectInfo nnoi = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(ci.getAttributeId("profile"));
		nnoi.getHeader().setAttributesIdentification(offsets);

		profile.setName("ope");

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);
				
		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(1, comparator.getNbChanges());
		ChangedNativeAttributeAction cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(0);
		assertEquals(1, comparator.getChangedAttributeActions().size());
		assertEquals(profile.getName(), cnaa.getNoiWithNewValue().getObject());

	}

	public void testCompareCollection7() throws Exception {
		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
				

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		// / Set the same name
		profile.setName("operator");
		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);
				
		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertFalse(comparator.hasChanged(instanceInfo, instanceInfo3));

		assertEquals(0, comparator.getNbChanges());
	}

	public void testCompareCollection8() throws Exception {
		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
				

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		

		user.setProfile(null);

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);
				

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(1, comparator.getNbChanges());
		assertEquals(1, comparator.getAttributeToSetToNull().size());
		SetAttributeToNullAction o = (SetAttributeToNullAction) comparator.getAttributeToSetToNull().get(0);
		assertEquals(0, comparator.getChangedAttributeActions().size());

		assertEquals(2, o.getAttributeId());
	}

	public void testCompareCollection9() throws Exception {
		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
				

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		user.setName("Kiko");

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);
				
		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(1, comparator.getNbChanges());
		ChangedNativeAttributeAction cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(0);
		assertEquals(1, comparator.getChangedAttributeActions().size());
		assertEquals(user.getName(), cnaa.getNoiWithNewValue().getObject());
	}

	public void testGetSuperClasses() {
		List superclasses = classIntrospector.getSuperClasses(FootballPlayer.class.getName(),true);
		assertEquals(3, superclasses.size());
		assertEquals(FootballPlayer.class, superclasses.get(0));
		assertEquals(OutdoorPlayer.class, superclasses.get(1));
		assertEquals(Player.class, superclasses.get(2));
	}

	public void testGetAllFields() {
		List allFields = classIntrospector.getAllFields(FootballPlayer.class.getName());
		assertEquals(3, allFields.size());
		assertEquals("role", ((Field) allFields.get(0)).getName());
		assertEquals("groundName", ((Field) allFields.get(1)).getName());
		assertEquals("name", ((Field) allFields.get(2)).getName());
	}

	public void testIntrospectWithNull() throws Exception {
		User user = new User("olivier smadja", "olivier@neodatis.com", null);
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
				

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		Object o = instanceInfo.getAttributeValueFromId(2);

		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) o;
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		user.setName("Olivier Smadja");
		user.setEmail("olivier@neodatis.org");
		user.setProfile(new Profile("pname"));

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);
				
		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		assertTrue(comparator.hasChanged(instanceInfo, instanceInfo3));
		assertEquals(3, comparator.getNbChanges());
		assertEquals(2, comparator.getChangedAttributeActions().size());

		ChangedNativeAttributeAction cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(0);
		assertEquals("Olivier Smadja", cnaa.getNoiWithNewValue().getObject());
		cnaa = (ChangedNativeAttributeAction) comparator.getChangedAttributeActions().get(1);
		assertEquals("olivier@neodatis.org", cnaa.getNoiWithNewValue().getObject());
	}

	public void testIntrospectWithNull2() throws Exception {
		User user = new User("olivier smadja", "olivier@neodatis.com", null);
		IObjectInfoComparator comparator = new ObjectInfoComparator();

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
				

		// Sets attributes offsets - this is normally done by reading then from
		// disk, but in this junit,
		// we must set them manually
		AttributeIdentification[] offsets = { new AttributeIdentification(1,1), new AttributeIdentification(2,2), new AttributeIdentification(3,3) };
		int[] ids = { 1, 2, 3 };

		instanceInfo.getHeader().setAttributesIdentification(offsets);
		
		instanceInfo.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		Object o = instanceInfo.getAttributeValueFromId(2);

		NonNativeObjectInfo nnoiProfile = (NonNativeObjectInfo) o;
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		user.setProfile(new Profile("pname"));

		NonNativeObjectInfo instanceInfo3 = objectIntrospector.getMetaRepresentation(user,  callback);
				
		instanceInfo3.getHeader().setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));
		nnoiProfile = (NonNativeObjectInfo) instanceInfo3.getAttributeValueFromId(2);
		nnoiProfile.setOid(OIDFactory.buildObjectOID(OIDFactory.buildClassOID()));

		boolean b = comparator.hasChanged(instanceInfo, instanceInfo3);
		assertTrue(b);
		assertEquals(1, comparator.getNbChanges());
		assertEquals(0, comparator.getChangedAttributeActions().size());
		assertEquals(1, comparator.getNewObjectMetaRepresentations().size());

	}

	public void testGetDependentObjects() throws Exception {
		User user = new User("olivier smadja", "olivier@neodatis.com", new Profile("operator", new Function("login")));
		GetDependentObjectIntrospectingCallback callback = new GetDependentObjectIntrospectingCallback();
		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
		assertEquals(user.getClass().getName(), instanceInfo.getClassInfo().getFullClassName());
		assertEquals("olivier smadja", instanceInfo.getAttributeValueFromId(ci.getAttributeId("name")).toString());
		assertEquals(AtomicNativeObjectInfo.class, instanceInfo.getAttributeValueFromId(ci.getAttributeId("name")).getClass());

		Collection objects = callback.getObjects();
		assertEquals(3, objects.size());

		assertTrue(objects.contains(user.getProfile()));
		assertTrue(objects.contains(user.getProfile().getFunctions().get(0)));
		
	}

	public void testGetNonNativeObjects() throws Exception {
		Profile profile = new Profile("operator", new Function("login"));
		for(int i=0;i<1000;i++){
			profile.addFunction(new Function("login "+i));
		}
		User user = new User("olivier smadja", "olivier@neodatis.com", profile);

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
		assertEquals(user.getClass().getName(), instanceInfo.getClassInfo().getFullClassName());
		assertEquals("olivier smadja", instanceInfo.getAttributeValueFromId(ci.getAttributeId("name")).toString());
		assertEquals(AtomicNativeObjectInfo.class, instanceInfo.getAttributeValueFromId(ci.getAttributeId("name")).getClass());

		IOdbList<NonNativeObjectInfo> objects  = instanceInfo.getDirectNonNativeAttributes();
		
		assertEquals(1,objects.size());
		
		// Gets the profile meta representation
		NonNativeObjectInfo profileNnoi = (NonNativeObjectInfo) instanceInfo.getAttributeValueFromId(ci.getAttributeId("profile"));
		
		// Get all non native objects of profile
		objects = profileNnoi.getDirectNonNativeAttributes();
		assertEquals(0, objects.size());
		
		// Profile contains 1001 functions, so we should have 1001 non direct non native objects 
		objects = profileNnoi.getNonDirectNonNativeObjects();
		assertEquals(1001, objects.size());
		
	}

	public void testCopy() throws Exception {
		Function function = new Function("login");
		Profile profile = new Profile("operator", function);
		User user = new User("olivier smadja", "olivier@neodatis.org", profile);

		ClassInfo ci = classIntrospector.introspect(user.getClass()).getMainClassInfo();
		NonNativeObjectInfo instanceInfo = objectIntrospector.getMetaRepresentation(user,  callback);
				

		NonNativeObjectInfo copy = (NonNativeObjectInfo) instanceInfo.createCopy(new OdbHashMap(),true);

		assertEquals(3, copy.getAttributeValues().length);

		AbstractObjectInfo[] aois = copy.getAttributeValues();

		for (int i = 0; i < aois.length; i++) {
			AbstractObjectInfo aoi = aois[i];

			assertEquals(instanceInfo.getAttributeValues()[i].getOdbTypeId(), aoi.getOdbTypeId());
		}

	}

}
