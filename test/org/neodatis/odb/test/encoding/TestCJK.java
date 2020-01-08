/**
 * 
 */
package org.neodatis.odb.test.encoding;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

import java.io.UnsupportedEncodingException;

/**
 * @author olivier
 * 
 */
public class TestCJK extends ODBTest {

	public void testCjk() throws UnsupportedEncodingException {
		
		try{
		String baseName = getBaseName();
		NeoDatisConfig config = NeoDatis.getConfig();
		config.setDatabaseCharacterEncoding("UTF-8");
		ODB odb = open(baseName,config);

		// Store the object
		//@TODO
		Function f1= new Function("");//æµœå´Žã�‚ã‚†ã�¿ "); 
		Function f2= new Function("");//æ»¨å´Žæ­¥");
		odb.store(f1);
		odb.store(f2);
		odb.commit();

		Objects<Function> datas = odb.query(Function.class).objects();
		odb.close();
		Function ff1 = datas.next();
		Function ff2 = datas.next();
		
		println(ff1);
		println(ff2);
		
		assertEquals(f1.getName(), ff1.getName());
		assertEquals(f2.getName(), ff2.getName());
		}finally{
		}
	}

}
