/**
 * 
 */
package org.neodatis.odb.test.classloader;

import org.junit.Test;
import org.neodatis.odb.NeoDatisGlobalConfig;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 *
 */
public class TestClassLoader extends ODBTest{
    @Test
	public void test1(){
		String s = "Test";
		ClassLoader cl2 = Thread.currentThread().getContextClassLoader();
		ClassLoader cl = s.getClass().getClassLoader();
		NeoDatisGlobalConfig.get().setClassLoader(cl2);
	}

}
