/**
 * 
 */
package org.neodatis.odb.test.ext;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.test.vo.login.Function;

/**
 * @author olivier
 *
 */
public class LongThread extends Thread {
	private String baseName;
	private ODBServer server;
	private ObjectOid oid;
	private boolean objectVersionIsOk;
	public LongThread(String baseName, ODBServer server, ObjectOid oid) {
		super();
		this.baseName = baseName;
		this.server = server;
		this.oid = oid;
	}
	
	public void run(){
		
		ODB odb = server.openClient(baseName);
		long version1 = odb.ext().getObjectVersion(oid,true); 
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		//odb.close();
		//odb = server.openClient(baseName);
		long version2 = odb.ext().getObjectVersion(oid,false);
		objectVersionIsOk = version2 == 2;
		
		Function f = (Function) odb.getObjectFromId(oid);
		System.out.println(String.format("in thread, Function name is %s", f.getName()));
		System.out.println(String.format("version 1 is %d and version 2 is %d", version1, version2));
		odb.close();
	}

	public boolean objectVersionIsOk() {
		return objectVersionIsOk;
	}

}
