package org.neodatis.odb.test.trigger;

import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.core.trigger.OIDTrigger;
import org.neodatis.tool.mutex.Mutex;
import org.neodatis.tool.mutex.MutexFactory;

public class LocalAutoIncrementTrigger extends OIDTrigger {

	
	@Override
	public void setOid(final Object object, ObjectOid oid) {
		if (object.getClass() != ObjectWithAutoIncrementId.class) {
			return ;
		}
		ObjectWithAutoIncrementId o = (ObjectWithAutoIncrementId) object;

		Mutex mutex = MutexFactory.get("auto increment mutex");
		try {
			try {
				mutex.acquire("trigger");

				long id = getNextId(object.getClass().getName());
				o.setId(id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				throw new NeoDatisRuntimeException(NeoDatisError.INTERNAL_ERROR, e);
			}
		} finally {
			if (mutex != null) {
				mutex.release("trigger");
			}
		}
		
	}


	
	/**
	 * Actually gets the next id Gets the object of type ID from the database
	 * with the specific name. Then increment the id value and returns. If
	 * object does not exist, creates t.
	 * 
	 * @param idName
	 * @return
	 */
	private long getNextId(String idName) {
		ODB odb = getOdb();
		Objects objects = odb.query(ID.class, W.equal("idName", idName)).objects();

		if (objects.isEmpty()) {
			ID id1 = new ID(idName, 1);
			odb.store(id1);
			return 1;
		}

		ID id = (ID) objects.first();
		long lid = id.getNext();
		odb.store(id);
		return lid;
	}

	

}
