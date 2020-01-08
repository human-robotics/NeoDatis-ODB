package org.neodatis.odb.test.server.trigger.autoincrement;

import org.neodatis.odb.*;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.tool.mutex.Mutex;
import org.neodatis.tool.mutex.MutexFactory;

public class ServerAutoIncrementTrigger extends org.neodatis.odb.core.server.trigger.ServerInsertTrigger {

	public void afterInsert(ObjectRepresentation objectRepresentation, ObjectOid oid) {
	}

	public boolean beforeInsert(ObjectRepresentation objectRepresentation) {
		String className = objectRepresentation.getObjectClassName();

		// Check if it is the right class
		//if (!className.equals(ObjectWithAutoIncrementId.class.getName())) {
		//	return false;
		//}
		// The mutex is used to avoid concurrent access for this operation
		Mutex mutex = MutexFactory.get("auto increment mutex");
		try {
			try {
				// Acquire the trigger
				mutex.acquire("trigger");

				long id = getNextId(className);

				// Sets the value on the user object
				objectRepresentation.setValueOf("id", new Long(id));
				// System.out.println("setting new id "+ id);
				return true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				throw new NeoDatisRuntimeException(NeoDatisError.INTERNAL_ERROR, e);
			}
		} finally {
			// Release the mutex
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
		Objects<ID> objects = odb.query(ID.class, W.equal("idName", idName)).objects();

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
