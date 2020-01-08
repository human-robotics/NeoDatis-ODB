package org.neodatis.odb.test.server.trigger.oid;

import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.ObjectRepresentation;
import org.neodatis.odb.core.server.trigger.ServerOidTrigger;

public class MyOidTriggerAsObject extends ServerOidTrigger {

	public void setOid(ObjectRepresentation o, ObjectOid oid) {
		o.setValueOf("oid", oid);
	}
}
