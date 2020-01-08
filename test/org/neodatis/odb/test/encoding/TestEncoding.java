package org.neodatis.odb.test.encoding;

import org.neodatis.odb.*;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

import java.io.UnsupportedEncodingException;

public class TestEncoding extends ODBTest {

	public void test1() throws UnsupportedEncodingException {
		String baseName = getBaseName();
		println(baseName);
		NeoDatisConfig config = NeoDatis.getConfig().setDatabaseCharacterEncoding("ISO8859-5");
		ODB odb = null;
		try{
			odb = open(baseName,config);
			String nameWithCyrillicCharacters = "\u0410 \u0430 \u0431 \u0448 \u0429";
			Function f = new Function(nameWithCyrillicCharacters);
			ObjectOid oid = odb.store(f);
			odb.close();

			println(f);

			odb = open(baseName,config);
			Function f2 = (Function) odb.getObjectFromId(oid);
			odb.close();
			assertEquals(nameWithCyrillicCharacters, f2.getName());

			assertEquals('\u0410', f2.getName().charAt(0));
			assertEquals('\u0430', f2.getName().charAt(2));
			assertEquals('\u0431', f2.getName().charAt(4));
			assertEquals('\u0448', f2.getName().charAt(6));
			assertEquals('\u0429', f2.getName().charAt(8));

		}finally{
		}

	}

	public void test2_ClientServer() throws UnsupportedEncodingException, InterruptedException {
		String baseName = getBaseName();
		println(baseName);
		NeoDatisConfig config = NeoDatis.getConfig().setDatabaseCharacterEncoding("ISO8859-5");
		ODBServer server = null;
		
		try{
			server = NeoDatis.openServer(ODBTest.PORT+1,config);
			server.addBase(baseName, baseName);
			server.startServer(true);
			Thread.sleep(200);
			ODB odb = NeoDatis.openClient("localhost",ODBTest.PORT+1, baseName);
			String nameWithCyrillicCharacters = "\u0410 \u0430 \u0431 \u0448 \u0429";
			Function f = new Function(nameWithCyrillicCharacters);
			ObjectOid oid = odb.store(f);
			odb.close();

			println(f);

			odb = NeoDatis.openClient("localhost",ODBTest.PORT+1, baseName);
			Function f2 = (Function) odb.getObjectFromId(oid);
			odb.close();
			assertEquals(nameWithCyrillicCharacters, f2.getName());

			assertEquals('\u0410', f2.getName().charAt(0));
			assertEquals('\u0430', f2.getName().charAt(2));
			assertEquals('\u0431', f2.getName().charAt(4));
			assertEquals('\u0448', f2.getName().charAt(6));
			assertEquals('\u0429', f2.getName().charAt(8));

		}finally{
			if(server!=null){
				server.close();
			}
		}

	}

}
