/**
 * 
 */
package org.neodatis.odb;


/**
 * @author olivier
 *
 */
public interface UnitTestWrapper {

	void start(String baseName, NeoDatisConfig config);
	void end();
}
