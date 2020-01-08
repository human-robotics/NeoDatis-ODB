package org.neodatis.odb.test.query.factory;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.QueryFactory;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

public class TestqueryFactory extends ODBTest {

	public void testQuery() throws Exception {
		
		String baseName = getBaseName();
		ODB odb = open(baseName);

		
		odb.store(new Function("Function1"));
		odb.store(new Function("Function2"));
		odb.store(new Function("Function3"));
		odb.store(new Function("Function4"));
		
		odb.close();
		
		
		// create some disconnected queries
		Query q1 = QueryFactory.query(Function.class);

		odb = open(baseName);

		assertEquals(4,odb.query(q1).objects().size());
		odb.close();
	}
	
	

}
