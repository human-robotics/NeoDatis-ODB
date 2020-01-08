/**
 * 
 */
package org.neodatis.odb.test.ee.session;

import org.neodatis.odb.ClassOid;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.session.Cache;
import org.neodatis.odb.core.session.CacheImpl;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.oid.OIDFactory;
import org.neodatis.odb.test.vo.login.Function;

/**
 * @author olivier
 *
 */
public class TestCache extends ODBTest{
	
	/** Test the weak reference behavior**/
	public void testCache1(){
		int size = 10000;
		Cache cache = new CacheImpl();
		ClassOid coid = OIDFactory.buildClassOID();
		
		
		Function f1 = new Function("f1");
		Function f2 = new Function("f2");
		ObjectOid oidf1 = OIDFactory.buildObjectOID(coid );
		ObjectOid oidf2 = OIDFactory.buildObjectOID(coid);
		cache.addObject(oidf1, f1);
		cache.addObject(oidf2, f2);
		
		for(int i=0;i<size;i++){
			Function f = new Function("function f " + (i+10));
			cache.addObject(OIDFactory.buildObjectOID(coid), f);
			if(i%100000==0){
				System.out.println("i="+i + "  |  size="+cache.getSize());
			}
		}
		
		Function f11 = (Function) cache.getObjectWithOid(oidf1);
		Function f22 = (Function) cache.getObjectWithOid(oidf2);
		assertNotNull(f11);
		assertNotNull(f22);
		assertEquals(f1,f11);
		assertEquals(f2,f22);
		
		ObjectOid oidf11 = cache.getOid(f1, false);
		ObjectOid oidf22 = cache.getOid(f2, false);
		assertNotNull(oidf11);
		assertNotNull(oidf22);
		assertEquals(oidf1, oidf11);
		assertEquals(oidf2, oidf22);
		
		
		
	}

}
