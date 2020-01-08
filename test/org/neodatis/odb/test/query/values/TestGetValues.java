package org.neodatis.odb.test.query.values;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.ValuesQuery;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.core.query.values.ICustomQueryFieldAction;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.attribute.AllAttributeClass;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;
import org.neodatis.tool.StopWatch;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TestGetValues extends ODBTest {

	public void test1() throws IOException, Exception {
		deleteBase("valuesA");
		ODB odb = open("valuesA");
		odb.store(new Function("f1"));
		odb.close();

		odb = open("valuesA");
		Values values = odb.queryValues(Function.class).field("name").values();

		println(values);
		ObjectValues ov = values.nextValues();
		odb.close();
		assertEquals("f1", ov.getByAlias("name"));
		assertEquals("f1", ov.getByIndex(0));
	}

	public void test2() throws IOException, Exception {
		deleteBase("valuesA");
		ODB odb = open("valuesA");
		odb.store(new Function("f1"));
		odb.close();

		odb = open("valuesA");
		Values values = odb.queryValues(Function.class).field("name", "Alias of the field").values();

		println(values);
		ObjectValues ov = values.nextValues();
		odb.close();
		assertEquals("f1", ov.getByAlias("Alias of the field"));
		assertEquals("f1", ov.getByIndex(0));
	}

	public void test3() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		odb.store(new User("user1", "email1", new Profile("profile name", new Function("f111"))));
		odb.close();

		odb = open(baseName);
		Values values = odb.queryValues(User.class).field("name").field("profile.name").values();

		println(values);
		ObjectValues ov = values.nextValues();
		odb.close();
		assertEquals("user1", ov.getByAlias("name"));
		assertEquals("user1", ov.getByIndex(0));

		assertEquals("profile name", ov.getByAlias("profile.name"));
		assertEquals("profile name", ov.getByIndex(1));
	}

	public void test4() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		AllAttributeClass tc1 = new AllAttributeClass();
		tc1.setInt1(45);
		odb.store(tc1);

		AllAttributeClass tc2 = new AllAttributeClass();
		tc2.setInt1(5);
		odb.store(tc2);
		odb.close();

		odb = open(baseName);
		Values values = odb.queryValues(AllAttributeClass.class).sum("int1", "sum of int1").count("nb objects").values();

		println(values);
		ObjectValues ov = values.nextValues();
		odb.close();
		assertEquals(BigDecimal.valueOf(50), ov.getByAlias("sum of int1"));
		assertEquals(BigInteger.valueOf(2), ov.getByAlias("nb objects"));
		assertEquals(1, values.size());

	}

	public void test5() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		StopWatch t = new StopWatch();
		t.start();
		int size = isLocal ? 1000 : 100;
		for (int i = 0; i < size; i++) {
			AllAttributeClass tc1 = new AllAttributeClass();
			tc1.setInt1(45);
			odb.store(tc1);
		}

		odb.close();
		t.end();
		println(" time for insert = " + t.getDurationInMiliseconds());
		odb = open(baseName);
		t.start();
		Values values = odb.queryValues(AllAttributeClass.class).count("nb objects").values();
		t.end();
		println(values);
		println(" time for count = " + t.getDurationInMiliseconds());
		ObjectValues ov = values.nextValues();
		odb.close();
		assertEquals(BigInteger.valueOf(size), ov.getByAlias("nb objects"));
		assertEquals(1, values.size());

	}

	public void test6() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		StopWatch t = new StopWatch();
		t.start();
		int size = isLocal ? 100 : 10;
		for (int i = 0; i < size; i++) {
			AllAttributeClass tc1 = new AllAttributeClass();
			tc1.setInt1(i);
			odb.store(tc1);
		}

		odb.close();
		t.end();
		println(" time for insert = " + t.getDurationInMiliseconds());
		odb = open(baseName);
		t.start();
		Values values = odb.queryValues(AllAttributeClass.class, W.equal("int1", 2)).count("nb objects").values();
		t.end();
		println(values);
		println(" time for count = " + t.getDurationInMiliseconds());
		ObjectValues ov = values.nextValues();
		odb.close();
		assertEquals(BigInteger.valueOf(1), ov.getByAlias("nb objects"));
		assertEquals(1, values.size());

	}

	public void test7() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		StopWatch t = new StopWatch();
		t.start();
		int size = isLocal ? 1000 : 100;
		for (int i = 0; i < size; i++) {
			AllAttributeClass tc1 = new AllAttributeClass();
			tc1.setInt1(i);
			odb.store(tc1);
		}

		odb.close();
		t.end();
		println(" time for insert = " + t.getDurationInMiliseconds());
		odb = open(baseName);
		t.start();
		long nb = odb.query(AllAttributeClass.class, W.equal("int1", 2)).count().longValue();
		t.end();
		println(nb);
		println(" time for count = " + t.getDurationInMiliseconds());
		odb.close();
		assertEquals(1, nb);
	}

	/** Max and average **/
	public void test8() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		StopWatch t = new StopWatch();
		t.start();
		int size = isLocal ? 1000 : 100;
		long sum = 0;
		for (int i = 0; i < size; i++) {
			AllAttributeClass tc1 = new AllAttributeClass();
			tc1.setInt1(i);
			odb.store(tc1);
			sum += i;
		}

		odb.close();
		t.end();
		println(" time for insert = " + t.getDurationInMiliseconds());
		odb = open(baseName);
		t.start();
		ValuesQuery q = odb.queryValues(AllAttributeClass.class).max("int1", "max of int1").avg("int1", "avg of int1").sum("int1", "sum of int1");
		Values values = q.values();
		t.end();
		ObjectValues ov = values.nextValues();
		BigDecimal max = (BigDecimal) ov.getByAlias("max of int1");
		BigDecimal avg = (BigDecimal) ov.getByAlias("avg of int1");
		BigDecimal bsum = (BigDecimal) ov.getByAlias("sum of int1");
		println(max);
		println(avg);
		println(bsum);
		println(" time for count = " + t.getDurationInMiliseconds());
		odb.close();
		assertEquals(new BigDecimal(sum), bsum);
		assertEquals(new BigDecimal(size - 1), max);
		assertEquals(bsum.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_DOWN), avg);
	}

	/** Min **/
	public void test9() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		StopWatch t = new StopWatch();
		t.start();
		int size = isLocal ? 1000 : 100;
		long sum = 0;
		for (int i = 0; i < size; i++) {
			AllAttributeClass tc1 = new AllAttributeClass();
			tc1.setInt1(i);
			odb.store(tc1);
			sum += i;
		}

		odb.close();
		t.end();
		println(" time for insert = " + t.getDurationInMiliseconds());
		odb = open(baseName);
		t.start();
		Values values = odb.queryValues(AllAttributeClass.class).min("int1", "min of int1").avg("int1", "avg of int1").sum("int1", "sum of int1").values();
		t.end();
		ObjectValues ov = values.nextValues();
		BigDecimal min = (BigDecimal) ov.getByAlias("min of int1");
		BigDecimal avg = (BigDecimal) ov.getByAlias("avg of int1");
		BigDecimal bsum = (BigDecimal) ov.getByAlias("sum of int1");
		println(min);
		println(avg);
		println(bsum);
		println(" time for count = " + t.getDurationInMiliseconds());
		odb.close();
		assertEquals(new BigDecimal(sum), bsum);
		assertEquals(new BigDecimal(0), min);
		assertEquals(bsum.divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_DOWN), avg);
	}

	/** Custom **/
	public void test10() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		StopWatch t = new StopWatch();
		t.start();
		int size = 1000;
		long sum = 0;
		for (int i = 0; i < size; i++) {
			AllAttributeClass tc1 = new AllAttributeClass();
			tc1.setInt1(i);
			odb.store(tc1);
			sum += i;
		}

		odb.close();
		t.end();
		println(" time for insert = " + t.getDurationInMiliseconds());
		odb = open(baseName);
		t.start();
		ICustomQueryFieldAction custom = new TestCustomQueryFieldAction();
		Values values = odb.queryValues(AllAttributeClass.class).custom("int1", "custom of int1", custom).values();
		t.end();
		ObjectValues ov = values.nextValues();
		BigDecimal c = (BigDecimal) ov.getByAlias("custom of int1");
		println(c);
		println(" time for count = " + t.getDurationInMiliseconds());
		odb.close();
		// assertEquals(bsum.divide(new
		// BigDecimal(size),2,BigDecimal.ROUND_HALF_DOWN), avg);
	}

	public void test16() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = null;
		StopWatch t = new StopWatch();
		int size = isLocal ? 100 : 10;
		for (int j = 0; j < 10; j++) {
			t.start();
			odb = open(baseName);
			for (int i = 0; i < size; i++) {
				AllAttributeClass tc1 = new AllAttributeClass();
				tc1.setInt1(45);
				odb.store(tc1);
			}
			odb.close();
			t.end();
			println(" time for insert = " + t.getDurationInMiliseconds());
		}

		odb = open(baseName);
		t.start();
		Values values = odb.queryValues(AllAttributeClass.class).count("nb objects").values();
		t.end();
		println(values);
		println(" time for count = " + t.getDurationInMiliseconds());
		ObjectValues ov = values.nextValues();
		odb.close();
		assertEquals(BigInteger.valueOf(size * 10), ov.getByAlias("nb objects"));
		assertEquals(1, values.size());

	}

	public void test17() throws IOException, Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		odb.store(new User("user1", "email1", new Profile("profile name", new Function("f111"))));
		odb.close();

		odb = open(baseName);
		Values values = odb.queryValues(User.class).field("name").field("profile").values();

		println(values);
		ObjectValues ov = values.nextValues();
		odb.close();
		assertEquals("user1", ov.getByAlias("name"));
		assertEquals("user1", ov.getByIndex(0));

		Profile p2 = (Profile) ov.getByAlias("profile");
		assertEquals("profile name", p2.getName());
	}

}
