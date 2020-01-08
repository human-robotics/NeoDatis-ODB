package org.neodatis.odb.test.serialization;

import org.neodatis.odb.core.server.message.process.DefaultProcessReturn;
import org.neodatis.odb.core.server.message.process.RemoteProcess;
import org.neodatis.odb.core.server.message.process.RemoteProcessReturn;

class MyRemoteProcess extends RemoteProcess{
	public RemoteProcessReturn execute(){
		return new DefaultProcessReturn("result","ok");
	}
}