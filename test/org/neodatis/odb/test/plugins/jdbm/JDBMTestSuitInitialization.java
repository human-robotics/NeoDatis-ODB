/**
 * 
 */
package org.neodatis.odb.test.plugins.jdbm;

import org.neodatis.odb.NeoDatisGlobalConfig;
import org.neodatis.odb.SuiteInitialization;
import org.neodatis.odb.core.layers.layer4.plugin.jdbmv3.JDBM3Plugin;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 *
 */
public class JDBMTestSuitInitialization implements SuiteInitialization {

	public void init() {
		NeoDatisGlobalConfig.get().setStorageEngineClass(JDBM3Plugin.class);
		ODBTest.testWrapperClass = JDBMTestWrapper.class;
		NeoDatisGlobalConfig.get().setBaseDirectory("unit-test-data");
	}

}
