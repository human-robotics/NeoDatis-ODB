package org.neodatis.odb.test.update;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

public class TestUnconnectedZone extends ODBTest {

	public void test1() throws Exception {
		if (!isLocal) {
			return;
		}
		ODB odb = open(getBaseName());
		ObjectOid oid = odb.store(new Function("f1"));
		odb.close();

		println("Oid=" + oid);
		odb = open(getBaseName());
		Function f2 = (Function) odb.getObjectFromId(oid);
		f2.setName("New Function");
		odb.store(f2);

		SessionEngine storageEngine = Dummy.getEngine(odb);

		// retrieve the class info to check connected and unconnected zone
		ClassInfo ci = storageEngine.getSession().getMetaModel().getClassInfo(Function.class.getName(), true);

		odb.close();


		odb = open(getBaseName());
		assertEquals(1, odb.getObjects(Function.class).size());
		odb.close();

	}
}
