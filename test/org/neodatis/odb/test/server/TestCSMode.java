/**
 * 
 */
package org.neodatis.odb.test.server;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

/**
 * @author olivier
 * 
 */
public class TestCSMode extends ODBTest {
	public void testMainId() throws Exception {
		ODB odb = open(getBaseName());

		Function login = new Function("login");
		Profile profile1 = new Profile("operator 1", login);
		User user = new User("olivier smadja", "olivier@neodatis.com", profile1);

		ObjectOid oid = odb.store(user);

		SessionEngine se = Dummy.getEngine(odb);
		ClassInfo ci = se.getSession().getMetaModel().getClassInfoFromId(oid.getClassOid());
		
		odb.close();
		assertEquals(User.class.getName(), ci.getFullClassName());
	}

}
