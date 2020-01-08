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

public class TestPerfObjectValuesVsCriteriaQuery extends ODBTest {

	public void test() {

	}

	public void populate() throws Exception {

		ODB odb = open("perfOValuesVsCriteria");

		int nbProfiles = 200;
		int nbUsers = 500000;
		Profile[] profiles = new Profile[nbProfiles];
		User2[] users = new User2[nbUsers];

		// First creates profiles
		for (int i = 0; i < nbProfiles; i++) {
			profiles[i] = new Profile("profile " + i, new Function("function Profile" + i));
			odb.store(profiles[i]);
		}

		// Then creates users
		for (int i = 0; i < nbUsers; i++) {
			users[i] = new User2("user" + i, "user mail" + i, profiles[getProfileIndex(nbProfiles)], i);
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
		TestPerfObjectValuesVsCriteriaQuery t = new TestPerfObjectValuesVsCriteriaQuery();
		// t.populate();
		t.t1estA();
	}

	public void t1est() throws Exception {
		ODB odb = open("perfOValuesVsCriteria");
		Query q = odb.query(User2.class);
		BigInteger b = odb.query(User2.class).count();
		println(b);
		System.out.println(q.getExecutionPlan().getDetails());
		assertEquals(new BigInteger("500000"), b);

		odb.close();
	}

	public void t1estA() throws Exception {
		ODB odb = open("perfOValuesVsCriteria");
		Query q = odb.query(User2.class);
		Objects objects = odb.getObjects(q, false);
		println(objects.size());
		System.out.println(q.getExecutionPlan().getDetails());
		assertEquals(2000000, objects.size());

		odb.close();
	}

	public void t1est1() throws Exception {
		ODB odb = open("perfOValuesVsCriteria");
		ValuesQuery q = odb.queryValues(User2.class, W.equal("nbLogins", 100)).field("name");
		Values v = q.values();
		println(v.size());
		System.out.println(q.getExecutionPlan().getDetails());
		assertEquals(2000000, v.size());

		odb.close();
	}

}
