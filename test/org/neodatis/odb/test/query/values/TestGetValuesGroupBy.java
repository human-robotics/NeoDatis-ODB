package org.neodatis.odb.test.query.values;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.ValuesQuery;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.attribute.AllAttributeClass;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;
import org.neodatis.odb.test.vo.login.User2;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TestGetValuesGroupBy extends ODBTest {

	public void test1() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		AllAttributeClass tc1 = new AllAttributeClass();
		tc1.setInt1(45);
		odb.store(tc1);

		AllAttributeClass tc2 = new AllAttributeClass();
		tc2.setInt1(45);
		odb.store(tc2);

		AllAttributeClass tc3 = new AllAttributeClass();
		tc3.setInt1(46);
		odb.store(tc3);

		odb.close();

		odb = open(baseName);
		ValuesQuery vq = odb.queryValues(AllAttributeClass.class).sum("int1", "sum of int1").groupBy("int1");
		vq.orderByAsc("int1");
		Values values = vq.values();
		assertEquals(2, values.size());

		println(values);
		ObjectValues ov = values.nextValues();

		assertEquals(BigDecimal.valueOf(90), ov.getByAlias("sum of int1"));

		ov = values.nextValues();
		assertEquals(BigDecimal.valueOf(46), ov.getByAlias("sum of int1"));

		odb.close();

		assertEquals(2, values.size());

	}

	public void test2() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		AllAttributeClass tc1 = new AllAttributeClass();
		tc1.setInt1(45);
		odb.store(tc1);

		AllAttributeClass tc2 = new AllAttributeClass();
		tc2.setInt1(45);
		odb.store(tc2);

		AllAttributeClass tc3 = new AllAttributeClass();
		tc3.setInt1(46);
		odb.store(tc3);

		odb.close();

		odb = open(baseName);
		ValuesQuery vq = odb.queryValues(AllAttributeClass.class).sum("int1", "sum of int1").count("count").groupBy("int1");
		vq.orderByAsc("int1");
		Values values = vq.values();

		println(values);
		ObjectValues ov = values.nextValues();

		assertEquals(BigDecimal.valueOf(90), ov.getByAlias("sum of int1"));
		assertEquals(BigInteger.valueOf(2), ov.getByAlias("count"));

		ov = values.nextValues();
		assertEquals(BigDecimal.valueOf(46), ov.getByAlias("sum of int1"));
		assertEquals(BigInteger.valueOf(1), ov.getByAlias("count"));

		odb.close();

		assertEquals(2, values.size());

	}

	/**
	 * Retrieving the name of the profile, the number of user for that profile
	 * and their average login number grouped by the name of the profile
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void test3() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		Profile p1 = new Profile("profile1", new Function("f1"));
		Profile p2 = new Profile("profile2", new Function("f2"));

		User u1 = new User2("user1", "user@neodatis.org", p1, 1);
		User u2 = new User2("user2", "user@neodatis.org", p1, 2);
		User u3 = new User2("user3", "user@neodatis.org", p1, 3);
		User u4 = new User2("user4", "user@neodatis.org", p2, 4);
		User u5 = new User2("user5", "user@neodatis.org", p2, 5);

		odb.store(u1);
		odb.store(u2);
		odb.store(u3);
		odb.store(u4);
		odb.store(u5);
		odb.close();

		odb = open(baseName);
		ValuesQuery q = odb.queryValues(User2.class).field("profile.name").count("count").avg("nbLogins", "avg").groupBy(
				"profile.name");
		q.orderByAsc("name");
		Values values = q.values();

		println(values);
		ObjectValues ov = values.nextValues();
		assertEquals(2, values.size());

		assertEquals("profile1", ov.getByAlias("profile.name"));
		assertEquals(new BigInteger("3"), ov.getByAlias("count"));
		assertEquals(new BigDecimal("2.00"), ov.getByAlias("avg"));

		odb.close();

		assertEquals(2, values.size());

	}
	
	public void test4() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		int size = 1000;
		for(int i=0;i<size;i++){
			odb.store(new User("user "+i,"email "+i,new Profile("profile "+i,new Function("Function "+i))));
		}
		odb.close();

		odb = open(baseName);
		ValuesQuery q = odb.queryValues(User.class).field("profile.name").field("name");
		q.orderByAsc("name");
		Values values = q.values();
		assertEquals(size, values.size());
		while(values.hasNext()){
			ObjectValues ov = values.nextValues();
		}
		odb.close();
	}
}
