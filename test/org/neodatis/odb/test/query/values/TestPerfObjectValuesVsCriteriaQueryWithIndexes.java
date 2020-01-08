package org.neodatis.odb.test.query.values;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.ValuesQuery;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User2;

import java.math.BigInteger;

public class TestPerfObjectValuesVsCriteriaQueryWithIndexes extends ODBTest {

	public void populate() throws Exception {

		ODB odb = open("perfOValuesVsCriteriaIndex");
		String[] atts = { "name" };
		try {
			odb.getClassRepresentation(User2.class).addUniqueIndexOn("Index", atts, true);
		} catch (Exception e) {
			// TODO: handle exception
		}
		int nbProfiles = 200;
		int nbUsers = 500000;
		Profile[] profiles = new Profile[nbProfiles];
		User2[] users = new User2[nbUsers];

		int userStart = 1500000;
		int profileStart = 600;
		// First creates profiles
		for (int i = 0; i < nbProfiles; i++) {
			profiles[i] = new Profile("profile " + (i + profileStart), new Function("function Profile" + i));
			odb.store(profiles[i]);
		}

		// Then creates users
		for (int i = 0; i < nbUsers; i++) {
			users[i] = new User2("user" + (i + userStart), "user mail" + i, profiles[getProfileIndex(nbProfiles)], i);
			odb.store(users[i]);
			if (i % 10000 == 0) {
				println(i);
			}
		}
		odb.close();

	}

	private int getProfileIndex(int nbProfiles) {
		return (int) Math.random() * nbProfiles;
	}

	public static void main(String[] args) throws Exception {
		TestPerfObjectValuesVsCriteriaQueryWithIndexes t = new TestPerfObjectValuesVsCriteriaQueryWithIndexes();
		// t.populate();
		t.t1est1();
		t.t1estA();
	}

	public void t1est() throws Exception {
		ODB odb = open("perfOValuesVsCriteriaIndex");
		Query q = odb.query(User2.class);
		BigInteger b = odb.query(User2.class).count();
		println(b);
		System.out.println(q.getExecutionPlan().getDetails());
		assertEquals(new BigInteger("500000"), b);

		odb.close();
	}

	public void t1estA() throws Exception {
		ODB odb = open("perfOValuesVsCriteriaIndex");
		Query q = odb.query(User2.class, W.equal("name", "user1999999"));
		Objects objects = odb.getObjects(q, false);
		println(objects.size());
		System.out.println(q.getExecutionPlan().getDetails());
		assertEquals(1, objects.size());

		objects = odb.getObjects(q, false);
		println(objects.size());
		System.out.println(q.getExecutionPlan().getDetails());
		assertEquals(1, objects.size());

		odb.close();
	}

	public void t1est1() throws Exception {
		ODB odb = open("perfOValuesVsCriteriaIndex");
		ValuesQuery q = odb.queryValues(User2.class, W.equal("name", "user1999999")).field("name");
		Values v = q.values();
		println(v.size());
		System.out.println(q.getExecutionPlan().getDetails());
		assertEquals(1, v.size());

		odb.close();
	}

	public void test() {

	}

}
