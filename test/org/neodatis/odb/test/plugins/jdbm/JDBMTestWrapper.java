/**
 * 
 */
package org.neodatis.odb.test.plugins.jdbm;

import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.UnitTestWrapper;

/**
 * @author olivier
 *
 */
public class JDBMTestWrapper implements UnitTestWrapper {
	public static final int PORT = 10000;
	public static final String HOST = "localhost";

	public void end() {
	}

	public void start(String baseName, NeoDatisConfig config) {
		// nothing to do
	}

}
