/**
 * 
 */
package org.neodatis.odb.test.config;

import org.junit.Test;
import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.core.layers.layer4.plugin.jdbmv3.JDBM3Plugin;
import org.neodatis.odb.core.layers.layer4.plugin.jdbmv3.JDBM3Wrapper;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

/**
 * @author olivier
 *
 */
public class TestConfig extends ODBTest{

    @Test
	public void test1() throws Exception{
		
		NeoDatisConfig config = NeoDatis.getConfigFromFile(); 
		boolean b = config.isSessionAutoCommit();
		String storageEngine = config.getStorageEngineClass().getName();
		
		assertFalse(b);
		assertEquals("org.neodatis.odb.core.layers.layer4.plugin.jdbm.NeoDatisJdbmPlugin", storageEngine);
	}

    @Test
	public void test2(){
		String baseName = getBaseName();
		
		NeoDatisConfig config = NeoDatis.getConfig();
		config.setStorageEngineClass(JDBM3Plugin.class);
		
		ODB odb = NeoDatis.open(baseName, config);
		odb.store(new Function("test"));
		odb.close();
	}
}
