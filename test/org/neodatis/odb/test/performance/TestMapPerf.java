package org.neodatis.odb.test.performance;

import org.neodatis.odb.ClassOid;
import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.oid.OIDFactory;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.tool.StopWatch;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.util.Iterator;
import java.util.Map;

/**
 * Test map strategy
 * 
 * We need to cache loaded objects. But some of this loaded objects will be
 * modified and we need to keep track of the modified object (without
 * duplication)
 * 
 * What is the best strategy?
 * 
 * 1- having two maps, one for loaded objects and one for save objects. Knowing
 * that all saved objects are in the loaded objects
 * 
 * 2- having one map, where the value is not the object but an Object Wrapper
 * that has a boolean to indicate if it has been update and the object
 * 
 * ??
 * 
 * @author osmadja
 * 
 */
public class TestMapPerf extends ODBTest {

	public static int size = 1000;

	/**
	 * Loading x objects, x/2 are modified, using strategy 1
	 * */
	public void test1() {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map loadedObjects = new OdbHashMap();
		Map modifiedObjects = new OdbHashMap();

		Function f = null;
		ObjectOid oid = null;
		ClassOid coid = OIDFactory.buildClassOID();
		for (int i = 0; i < size; i++) {
			f = new Function("function " + i);
			oid = OIDFactory.buildObjectOID(coid);
			loadedObjects.put(oid, f);
			if (i < size / 2) {
				modifiedObjects.put(oid, f);
			}
			if (i % 10000 == 0) {
				//MemoryMonitor.displayCurrentMemory("put i", false);
			}
		}

		int j = 0;
		int nbModified = 0;
		// Now get all modified objects
		Iterator iterator = modifiedObjects.keySet().iterator();
		while (iterator.hasNext()) {
			oid = (ObjectOid) iterator.next();
			Object o = modifiedObjects.get(oid);
			if (j % 10000 == 0) {
				//MemoryMonitor.displayCurrentMemory("get i", false);
			}
			j++;
			nbModified++;

		}

		stopWatch.end();

		println("time for 2 maps =" + stopWatch.getDurationInMiliseconds());
		assertEquals(size / 2, nbModified);
	}

	/**
	 * Loading x objects, x/2 are modified, using strategy 2
	 * */
	public void test2() {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Map objects = new OdbHashMap();

		Function f = null;
		OID oid = null;
		ObjectWrapper ow = null;
		ClassOid coid = OIDFactory.buildClassOID();
		int i=0;
		for (i = 0; i < size; i++) {
			f = new Function("function " + i);
			oid = OIDFactory.buildObjectOID( coid);
			objects.put(oid, new ObjectWrapper(f, false));
			if (i < size / 2) {
				ow = (ObjectWrapper) objects.get(oid);
				ow.setModified(true);
				objects.put(oid, ow);
			}
			if (i % 10000 == 0) {
				//MemoryMonitor.displayCurrentMemory("put i", false);
			}
		}

		i = 0;
		int nbModified = 0;
		// Now get all modified objects
		Iterator iterator = objects.keySet().iterator();
		while (iterator.hasNext()) {
			oid = (OID) iterator.next();
			ow = (ObjectWrapper) objects.get(oid);
			if (ow.isModified()) {
				nbModified++;
			}
			if (i % 10000 == 0) {
				//MemoryMonitor.displayCurrentMemory("get i", false);
			}
			i++;

		}

		stopWatch.end();

		println("time for 1 map =" + stopWatch.getDurationInMiliseconds());
		assertEquals(size / 2, nbModified);
	}

}

class ObjectWrapper {
	private boolean modified;
	private Object object;

	public ObjectWrapper(Object object, boolean modified) {
		this.object = object;
		this.modified = modified;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public boolean equals(Object obj) {
		return object.equals(obj);
	}

	public int hashCode() {
		return object.hashCode();
	}

}
