/**
 * 
 */
package org.neodatis.odb.test.oid;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;

/**
 * @author olivier
 * 
 */
public class TestStoreWithOID extends ODBTest {

	public void test1() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ObjectOid oid = odb.store(new Function("f1"));
		odb.close();

		odb = open(baseName);
		Function f2 = new Function("f2");
		odb.ext().store(oid,f2);
		odb.close();

		odb = open(baseName);
		Function f = (Function) odb.getObjectFromId(oid);
		odb.close();

		assertEquals("f2", f.getName());
		
		odb = open(baseName);
		f = (Function) odb.query(Function.class).objects().first();
		odb.close();

		assertEquals("f2", f.getName());
	}
	public void test2() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ObjectOid oid = odb.store(new Function("f1"));
		
		for(int i=0;i<100;i++){
			odb.store(new Function("function " + i));
		}
		odb.close();

		odb = open(baseName);
		Function f2 = new Function("this is a new function");
		odb.ext().store(oid,f2);
		odb.close();

		odb = open(baseName);
		Function f = (Function) odb.getObjectFromId(oid);
		odb.close();

		assertEquals("this is a new function", f.getName());
		
		odb = open(baseName);
		f = (Function) odb.query(Function.class, W.equal("name", "this is a new function")).objects().first();
		odb.close();

		assertEquals("this is a new function", f.getName());
	}
	
	public void test3() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ObjectOid oid = odb.store(new Profile("name", new Function("f1")));
		odb.close();

		odb = open(baseName);
		Profile p2 = new Profile("name2", new Function("f2"));
		odb.ext().store(oid,p2);
		odb.close();

		odb = open(baseName);
		Profile p = (Profile) odb.getObjectFromId(oid);
		odb.close();

		assertEquals("name2", p.getName());
		assertEquals("f2", p.getFunctions().get(0).getName());
		
		odb = open(baseName);
		p = (Profile) odb.query(Profile.class).objects().first();
		odb.close();

		assertEquals("name2", p.getName());
		assertEquals("f2", p.getFunctions().get(0).getName());
	}
	
}
