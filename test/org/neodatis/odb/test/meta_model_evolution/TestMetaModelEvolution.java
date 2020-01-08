/**
 * 
 */
package org.neodatis.odb.test.meta_model_evolution;

import org.neodatis.odb.ODB;
import org.neodatis.odb.core.refactor.MetaModelEvolutionManager;
import org.neodatis.odb.main.NeoDatisTools;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

/** A class to test meta model evolution
 * @author olivier
 *
 */
public class TestMetaModelEvolution extends ODBTest{

	/**
	 * A simple test just to check when there is no changed classes
	 */
	public void testNoChange(){
		
		String baseName = getBaseName();

		// Creates a database with 3 objects => 3 classes
		ODB odb = open(baseName);
		odb.store(new User("user name", "user mail", new Profile("profile name", new Function("function name"))));
		odb.close();
		
		// Gets the meta model evolution manager
		MetaModelEvolutionManager manager = NeoDatisTools.getMetaModelEvolutionManager(baseName);
		
		// Create our listener to receive events
		MyMetaModelHasChangedListener listener = new MyMetaModelHasChangedListener();
		// Adds the listener
		manager.addListener(listener);
		
		// Check the meta model
		// false, false , true = updateDatabaseMetaModelIfPossible, abortIfChangesAreNotCompatible,  verbose 
		manager.check(false, false, true);
		assertEquals(3, listener.noChanges);
		
	}
	
	
}
