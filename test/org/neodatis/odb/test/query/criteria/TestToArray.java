/**
 * 
 */
package org.neodatis.odb.test.query.criteria;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

/**
 * @author olivier
 *
 */
public class TestToArray extends ODBTest {
	
	public void test1(){
		
		String baseName = getBaseName();
		int size = 100;
		ODB odb = open(baseName);
		for(int i=0;i<size;i++){
			odb.store(new Function("f"+i));
		}
		odb.close();
		
		odb = open(baseName);
		Objects<Function> functions = odb.query(Function.class).orderByAsc("name").objects();
		
		assertEquals(size, functions.size());
		odb.close();
		for(int i=0;i<50;i++){
			Object[] a = functions.toArray();
			
			for(int j=0;j<size;j++){
				println(a[j]);
				//assertEquals("f"+j, a[j].toString());
				assertEquals(Function.class, a[j].getClass());
			}
		}
	}

}
