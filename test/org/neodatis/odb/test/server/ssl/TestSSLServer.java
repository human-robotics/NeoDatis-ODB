package org.neodatis.odb.test.server.ssl;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

public class TestSSLServer extends ODBTest {
	public void test1(){
		int port = 10089;
		String baseName = getBaseName();
		
		ODBServer server = NeoDatis.openSSLServer(port);
		
		try{
			server.automaticallyCreateDatabase();
			
			server.startServer(true);
			
			ODB odb = NeoDatis.openSSLClient("localhost", port, baseName);
			odb.store(new Function("f1"));
			odb.close();
			
			odb = NeoDatis.openSSLClient("localhost", port, baseName);
			assertEquals(1, odb.query(Function.class).count().intValue());
			odb.close();
			
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally{
			if(server!=null){
				server.close();
			}
		}
	}
}
