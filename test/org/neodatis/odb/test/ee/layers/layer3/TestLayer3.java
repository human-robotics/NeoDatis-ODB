package org.neodatis.odb.test.ee.layers.layer3;

import org.neodatis.odb.*;
import org.neodatis.odb.core.layers.layer2.instance.InstanceBuilderContext;
import org.neodatis.odb.core.layers.layer2.meta.AbstractObjectInfo;
import org.neodatis.odb.core.layers.layer2.meta.CollectionObjectInfo;
import org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo;
import org.neodatis.odb.core.layers.layer3.OidAndBytes;
import org.neodatis.odb.core.layers.layer4.IOFileParameter;
import org.neodatis.odb.core.layers.layer4.plugin.memory.InMemoryStorageEngine;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.core.session.SessionImpl;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.tool.wrappers.list.IOdbList;

import java.util.Iterator;

public class TestLayer3 extends ODBTest {
	private Class defaultLayer4Class;

	public void setUp() throws Exception {
		defaultLayer4Class = NeoDatisGlobalConfig.get().getStorageEngineClass();
		super.setUp();
	}

	public void tearDown() throws Exception {
		NeoDatisGlobalConfig.get().setStorageEngineClass(defaultLayer4Class);
		super.tearDown();
	}

	public void testObjects() {

		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);

		Function function = new Function("function");
		Session session = new SessionImpl(new IOFileParameter("test", true, config));
		SessionEngine engine = session.getEngine();
		NonNativeObjectInfo nnoi = engine.layer1ToLayer2(function);
		Function f = (Function) engine.layer2ToLayer1(nnoi, new InstanceBuilderContext());
		assertEquals(Function.class.getName(), nnoi.getClassInfo().getFullClassName());

		assertEquals(function.getName(), f.getName());

		IOdbList<OidAndBytes> oidAndBytes = engine.layer2ToLayer3(nnoi);
		engine.layer3ToLayer4(oidAndBytes);
		assertEquals(1, oidAndBytes.size());

		NonNativeObjectInfo nnoi2 = engine.layer3ToLayer2(oidAndBytes.get(0), true, 0);
		assertEquals(Function.class.getName(), nnoi2.getClassInfo().getFullClassName());

		Function f2 = (Function) engine.layer2ToLayer1(nnoi2, new InstanceBuilderContext());

		assertEquals(function.getName(), f2.getName());

	}

	public void testObjects11() {

		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);
		Session session = new SessionImpl(new IOFileParameter("test", true, config));
		SessionEngine engine = session.getEngine();
		Function function = new Function("function");

		NonNativeObjectInfo nnoi = engine.layer1ToLayer2(function);
		IOdbList<OidAndBytes> oidAndBytes = engine.layer2ToLayer3(nnoi);
		engine.layer3ToLayer4(oidAndBytes);
		assertEquals(1, oidAndBytes.size());

		OID oid = nnoi.getOid();

		OidAndBytes oab = engine.layer4ToLayer3(oid);
		NonNativeObjectInfo nnoi2 = engine.layer3ToLayer2(oab, true, 0);
		Function f2 = (Function) engine.layer2ToLayer1(nnoi2, new InstanceBuilderContext());

		assertEquals(function.getName(), f2.getName());

	}

	public void testObjects1() {

		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);
		Session session = new SessionImpl(new IOFileParameter("test", true, config));
		SessionEngine engine = session.getEngine();
		Function function = new Function("function");

		ObjectOid oid = engine.store(function);
		Function f2 = (Function) engine.getObjectFromOid(oid, true, new InstanceBuilderContext());

		assertEquals(function.getName(), f2.getName());

	}

	public void testObjects2() {
		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);
		Function function = new Function("function");
		Profile profile = new Profile("profile", function);
		profile.addFunction(new Function("f2"));

		Session session = new SessionImpl(new IOFileParameter("test", true, config));
		SessionEngine engine = session.getEngine();

		NonNativeObjectInfo nnoi = engine.layer1ToLayer2(profile);
		assertEquals(Profile.class.getName(), nnoi.getClassInfo().getFullClassName());
		assertEquals(2, nnoi.getAttributeValues().length);
		CollectionObjectInfo coi = (CollectionObjectInfo) nnoi.getAttributeValues()[1];
		assertEquals(2, coi.getCollection().size());
		Iterator<AbstractObjectInfo> iterator = coi.getCollection().iterator();
		assertEquals(Function.class.getName(), ((NonNativeObjectInfo) iterator.next()).getClassInfo().getFullClassName());

		IOdbList<OidAndBytes> oidAndBytes = engine.layer2ToLayer3(nnoi);
		assertEquals(3, oidAndBytes.size());
		println(oidAndBytes);

		Profile p = (Profile) engine.layer2ToLayer1(nnoi, new InstanceBuilderContext());
		assertEquals("profile", p.getName());
		assertEquals(profile.getFunctions().size(), p.getFunctions().size());
	}

	public void testObjects3BigObject() {
		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);

		StringBuffer b = new StringBuffer();
		for (int i = 0; i < 50000; i++) {
			b.append(i);
		}

		Function function = new Function(b.toString());
		Session session = new SessionImpl(new IOFileParameter("test", true, config));
		SessionEngine engine = session.getEngine();

		NonNativeObjectInfo nnoi = engine.layer1ToLayer2(function);
		assertEquals(Function.class.getName(), nnoi.getClassInfo().getFullClassName());
		Function f = (Function) engine.layer2ToLayer1(nnoi, new InstanceBuilderContext());
		assertEquals(function.getName(), f.getName());

		IOdbList<OidAndBytes> oidAndBytes = engine.layer2ToLayer3(nnoi);
		assertEquals(1, oidAndBytes.size());
		OidAndBytes oab = oidAndBytes.get(0);
		// assertEquals(50000, oab.realObjectSize);

	}

	public void testObjectWithSuperClass() {
		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);

		ClassB classB = new ClassB();
		Session session = new SessionImpl(new IOFileParameter("test", true, config));
		SessionEngine engine = session.getEngine();

		NonNativeObjectInfo nnoi = engine.layer1ToLayer2(classB);
		assertEquals(ClassB.class.getName(), nnoi.getClassInfo().getFullClassName());
		assertNotNull(nnoi.getClassInfo().getSuperClassOid());
		println(nnoi.getClassInfo().getSuperClassOid());

	}

}
