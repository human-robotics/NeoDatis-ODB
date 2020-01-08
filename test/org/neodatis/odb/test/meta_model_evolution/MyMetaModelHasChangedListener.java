/**
 * 
 */
package org.neodatis.odb.test.meta_model_evolution;

import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.refactor.ClassHasChangedListener;
import org.neodatis.odb.core.refactor.ClassInfoCompareResult;

/**
 * @author olivier
 *
 */
public class MyMetaModelHasChangedListener extends ClassHasChangedListener {
	public int noChanges;
	public boolean hasChanged(ClassInfo oldCI, ClassInfo newCI, ClassInfoCompareResult result) {

		return true;
	}

	public boolean hasNotChanged(ClassInfo oldCI, ClassInfo newCI, ClassInfoCompareResult result) {
		System.out.println(oldCI.getFullClassName()+ " has not changed");
		noChanges ++;
		return true;
	}

}
