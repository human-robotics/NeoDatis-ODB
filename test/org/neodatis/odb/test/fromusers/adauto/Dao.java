/**
 * 
 */
package org.neodatis.odb.test.fromusers.adauto;

import org.neodatis.odb.*;

/**
 * @author olivier
 * 
 */
public class Dao<T> {

	private ODB odb = null;

	public Dao(String fileName) {

		odb = NeoDatis.open(fileName);

	}

	public void create(T object) {
		OID oid = odb.store(object);

	}

	public Objects<T> read(Query query) {

		Objects<T> obj = odb.getObjects(query);
		return obj;

	}

	public void update(T object) {

		create(object);

	}

	public void close() {
		odb.close();
	}

}
