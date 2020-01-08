package org.neodatis.odb.test.server.trigger.oid;

import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.ObjectRepresentation;
import org.neodatis.odb.core.server.trigger.ServerOidTrigger;
import org.neodatis.odb.test.vo.login.Function;

public class MyOidTrigger extends ServerOidTrigger {

	public void setOid(ObjectRepresentation o, ObjectOid oid) {
		o.setValueOf("id", oid.oidToString());
		// just to test odb connection
		getOdb().query(Function.class);
	}


}
