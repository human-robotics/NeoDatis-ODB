/**
 * 
 */
package org.neodatis.odb.test.oid;

import org.neodatis.odb.ClassOid;
import org.neodatis.odb.test.ODBTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author olivier
 * 
 */
public class TestOIDInMap extends ODBTest {

	public void testClassOidInMap() {
		ClassOid coid =  OIDFactory.buildClassOID();
		Map<ClassOid, String> oids = new HashMap<ClassOid, String>();
		
		oids.put(coid, coid.oidToString());
		println(oids);
		
		assertNotNull(oids.get(coid));
		
		
	}

	
	
}
