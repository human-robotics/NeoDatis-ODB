/**
 * 
 */
package org.neodatis.odb.test.depth;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

/**
 * @author olivier
 *
 */
public class TestDepthAndActivate extends ODBTest{
	
	public void testLazyLoad() throws Exception {
		ODB odb = open(getBaseName());

		User u = new User("user name", "user email", new Profile("profile name", new Function("function name")));
		odb.store(u);
		odb.close();
		
		odb = open(getBaseName());
		Query q = odb.query(User.class);
		q.getQueryParameters().setLoadDepth(1);
		Objects<User> l = q.objects();
		u = l.first();
		
		
		assertNull(u.getProfile().getName());
		assertNull(u.getProfile().getFunctions());
		
		odb.refresh(u.getProfile(), 1);
		assertEquals( "profile name",  u.getProfile().getName());
		assertEquals( "function name",  u.getProfile().getFunctions().get(0).getName());

		odb.close();

	}
	public void testLazyLoad2() throws Exception {
		ODB odb = open(getBaseName());

		User u = new User("user name", "user email", new Profile("profile name", new Function("function name")));
		int size = 1000;
		for(int i=0;i<size;i++){
			u.getProfile().addFunction(new Function("Function added "+i));
		}
		odb.store(u);
		odb.close();
		
		long start1 = System.currentTimeMillis();
		odb = open(getBaseName());
		Query q = odb.query(User.class);
		Objects<User> l = q.objects();
		u = l.first();
		odb.close();
		long end1 = System.currentTimeMillis();
		
		long start2 = System.currentTimeMillis();
		odb = open(getBaseName());
		q = odb.query(User.class);
		q.getQueryParameters().setLoadDepth(1);
		l = q.objects();
		u = l.first();
		odb.close();
		long end2 = System.currentTimeMillis();
		
		long timeFull = end1-start1;
		long timeLazy = end2-start2;
		println("TimeFull="+timeFull + " and TimeLazy="+timeLazy);
		assertTrue(timeLazy<timeFull);

	}


}
