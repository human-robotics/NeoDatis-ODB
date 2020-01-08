/**
 * 
 */
package org.neodatis.odb.test.manual;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;

import java.math.BigInteger;

/**
 * @author olivier
 * 
 */
public class BigTest {
	public static void count() {
		NeoDatisConfig config = NeoDatis.getConfig();
		config.setBaseDirectory("/Volumes/Work/NeoDatis");

		long t0 = System.currentTimeMillis();
		ODB odb = NeoDatis.open("big.neodatis",config);
		long t1 = System.currentTimeMillis();

		BigInteger nbFunctions = odb.query(Function.class).count();
		long t2 = System.currentTimeMillis();
		odb.close();

		System.out.println("Open time = " + (t1 - t0));
		System.out.println("Close time = " + (t2 - t1));
		System.out.println("Number of fucntions = " + (nbFunctions));

	}

	public static void populate() {
		NeoDatisConfig config = NeoDatis.getConfig();
		config.setBaseDirectory("/Volumes/Work/NeoDatis");

		ODB odb = NeoDatis.open("big.neodatis",config);

		if (!odb.getClassRepresentation(Function.class).existIndex("name")) {
			odb.getClassRepresentation(Function.class).addIndexOn("name", new String[] { "name" }, true);
			odb.getClassRepresentation(Profile.class).addIndexOn("name", new String[] { "name" }, true);
			odb.getClassRepresentation(User.class).addIndexOn("name", new String[] { "name" }, true);
		}

		int size = 10000000;
		int closeInterval = 10000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			User user = new User("user" + i, "email" + i, new Profile("profile" + i, new Function("function" + i)));
			odb.store(user);

			if (i % closeInterval == 0) {
				odb.close();
				odb = NeoDatis.open("big.neodatis");
			}

			if (i % 10000 == 0) {
				long t1 = System.currentTimeMillis();
				System.out.println(i + " - " + (t1 - start) + "ms");
				start = t1;
			}
		}
		odb.close();
		System.out.println("End of test");

	}

	public static void main(String[] args) {
		populate();
		//count();
	}
}
