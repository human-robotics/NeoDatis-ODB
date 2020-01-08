/**
 * 
 */
package org.neodatis.odb.test.plugins.memory;

import org.neodatis.odb.NeoDatisGlobalConfig;
import org.neodatis.odb.SuiteInitialization;
import org.neodatis.odb.core.layers.layer4.plugin.memory.InMemoryStorageEngineFast;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 *
 */
public class MemoryTestSuitInitialization implements SuiteInitialization {

	public void init() {
		NeoDatisGlobalConfig.get().setStorageEngineClass(InMemoryStorageEngineFast.class);
		ODBTest.testWrapperClass = MemoryTestWrapper.class;
		NeoDatisGlobalConfig.get().setBaseDirectory("unit-test-data");
	}

}
