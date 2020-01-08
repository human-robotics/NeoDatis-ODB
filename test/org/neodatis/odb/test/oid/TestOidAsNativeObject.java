package org.neodatis.odb.test.oid;

import org.neodatis.odb.*;
import org.neodatis.odb.core.oid.sequential.ClassOidImpl;
import org.neodatis.odb.core.oid.uuid.UniqueOidGeneratorImpl;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

public class TestOidAsNativeObject extends ODBTest {

	public void testClassOid() throws Exception {
		NeoDatisConfig config = NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class);
		
		ClassOid coid1 = OIDFactory.buildClassOID();
		ClassWithClassOid cwo = new ClassWithClassOid("test", coid1);
		
		String baseName = getBaseName();
		ODB odb = open(baseName, config);
		odb.store(cwo);
		odb.close();

		odb = open(baseName, config);
		Objects<ClassWithClassOid> objects = odb.query(ClassWithClassOid.class).objects();
		assertEquals(1, objects.size());
		ClassWithClassOid cwo2 = objects.first();
		
		println (cwo.getCoid());
		println (cwo2.getCoid());
		assertEquals(cwo.getCoid().oidToString(), cwo2.getCoid().oidToString());

	}
	
	public void testClassOidWhere() throws Exception {
		NeoDatisConfig config = NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class);
		
		ClassOid coid1 = OIDFactory.buildClassOID();
		ClassWithClassOid cwo = new ClassWithClassOid("test", coid1);
		
		String baseName = getBaseName();
		ODB odb = open(baseName, config);
		odb.store(cwo);
		odb.close();

		odb = open(baseName, config);
		Objects<ClassWithClassOid> objects = odb.query(ClassWithClassOid.class, W.equal("oid", coid1)).objects();
		assertEquals(1, objects.size());
		ClassWithClassOid cwo2 = objects.first();
		
		println (cwo.getCoid());
		println (cwo2.getCoid());
		assertEquals(cwo.getCoid().oidToString(), cwo2.getCoid().oidToString());

	}

	public void testOid() throws Exception {
		NeoDatisConfig config = NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class);
		ClassOid coid1 = OIDFactory.buildClassOID();
		ClassWithOid cwo = new ClassWithOid("test", OIDFactory.buildObjectOID(coid1));
		
		String baseName = getBaseName();
		ODB odb = open(baseName, config);
		odb.store(cwo);
		odb.close();

		odb = open(baseName, config);
		Objects<ClassWithOid> objects = odb.query(ClassWithOid.class).objects();
		assertEquals(1, objects.size());
		ClassWithOid cwo2 = objects.first();
		
		assertEquals(cwo.getOid().oidToString(), cwo2.getOid().oidToString());

	}
	
	public void testOidWhere() throws Exception {
		NeoDatisConfig config = NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class);
		ClassOid coid1 = OIDFactory.buildClassOID();
		ClassWithOid cwo = new ClassWithOid("test", OIDFactory.buildObjectOID(coid1));
		
		String baseName = getBaseName();
		ODB odb = open(baseName, config);
		odb.store(cwo);
		odb.close();

		odb = open(baseName, config);
		Objects<ClassWithOid> objects = odb.query(ClassWithOid.class, W.equal("oid", cwo.getOid())).objects();
		assertEquals(1, objects.size());
		ClassWithOid cwo2 = objects.first();
		
		assertEquals(cwo.getOid().oidToString(), cwo2.getOid().oidToString());

	}

	
	public void testClassInfoIndex() throws Exception {
		
		ClassOidImpl coid = new ClassOidImpl(1);
		
		ClassWithClassOid cwo = new ClassWithClassOid("name",coid);

		String baseName = getBaseName();
		ODB odb = open(baseName);
		println(odb.store(cwo));
		odb.close();

		odb = open(baseName);
		Objects<ClassWithClassOid> objects = odb.query(ClassWithClassOid.class, W.equal("oid", cwo.getCoid())).objects();
		assertEquals(1, objects.size());
		ClassWithClassOid cwo2 = objects.first();
		
		assertEquals(cwo.getCoid().oidToString(), cwo2.getCoid().oidToString());

	}

}
