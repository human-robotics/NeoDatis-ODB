package org.neodatis.odb.test.server.message;

import org.neodatis.odb.core.server.message.process.DefaultProcessReturn;
import org.neodatis.odb.core.server.message.process.RemoteProcess;
import org.neodatis.odb.core.server.message.process.RemoteProcessReturn;
import org.neodatis.odb.test.vo.login.Function;

import java.util.Date;

public class TimeRemoteProcess extends RemoteProcess {
	protected boolean store;
	protected boolean withException;
	public TimeRemoteProcess(boolean store, boolean withE) {
		this.store = store;
		this.withException = withE;
	}

	public RemoteProcessReturn execute() {
		// System.out.println("Retrieving data time from server");
		if (store) {
			getOdb().store(new Function(new Date().toString()));
		}
		
		
		if(withException){
			throw new RuntimeException("ops");
		}
		// System.out.println(getClientIp());
		// System.out.println(getOdb().query(Function.class).count());
		return new DefaultProcessReturn("server-time", new Date());
	}

}
