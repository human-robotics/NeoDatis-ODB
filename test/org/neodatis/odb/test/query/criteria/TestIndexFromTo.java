package org.neodatis.odb.test.query.criteria;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

import java.util.ArrayList;
import java.util.List;

public class TestIndexFromTo extends ODBTest {

	public void testGetLimitedResult1() throws Exception {
		String baseName = getBaseName();
		int size = 1000;

		ODB odb = open(baseName);

		for (int i = 0; i < size; i++) {
			odb.store(new Function("function " + i));
		}
		odb.close();

		odb = open(baseName);
		Query q = odb.query(Function.class).orderByAsc("name");
		q.getQueryParameters().setStartAndEndIndex(0, 1);
		q.orderByAsc("name");
		Objects<Function> os = q.objects();
		assertEquals(1, os.size());
		if (testNewFeature) {
			for (int i = 0; i < os.size(); i++) {
				Function f = (Function) os.next();
				assertEquals("function " + i, f.getName());
			}
		}
		odb.close();
	}

	public void test() {
		String s = "olivier";
		String ss = s.substring(0, 1);
		assertEquals(1, ss.length());

		ss = s.substring(0, 2);
		assertEquals(2, ss.length());

		List<String> l = new ArrayList<String>();
		l.add("s1");
		l.add("s2");
		l.add("s3");
		l.add("s4");
		l.add("s5");
		assertEquals(1, l.subList(0, 1).size());
		assertEquals(2, l.subList(0, 2).size());
		assertEquals(3, l.subList(0, 3).size());

	}

	public void testGetLimitedResult() throws Exception {
		String baseName = getBaseName();
		int size = 1000;

		ODB odb = open(baseName);

		for (int i = 0; i < size; i++) {
			odb.store(new Function("function " + i));
		}
		odb.close();

		odb = open(baseName);
		Query q = odb.query(Function.class);
		q.getQueryParameters().setStartAndEndIndex(0, 10);
		q.orderByAsc("name");
		Objects os = q.objects();
		assertEquals(10, os.size());

		if (testNewFeature) {
			for (int i = 0; i < 10; i++) {
				Function f = (Function) os.next();
				assertEquals("function " + i, f.getName());
			}
		}

		odb.close();
	}

	public void testGetLimitedResult2() throws Exception {
		String baseName = getBaseName();
		int size = 1000;

		ODB odb = open(baseName);

		for (int i = 0; i < size; i++) {
			odb.store(new Function("function " + i));
		}
		odb.close();

		odb = open(baseName);
		Query q = odb.query(Function.class);
		Objects os = odb.getObjects(q, true, 10, 20);
		assertEquals(10, os.size());
		if (testNewFeature) {
			for (int i = 10; i < 20; i++) {
				Function f = (Function) os.next();
				assertEquals("function " + i, f.getName());
			}
		}
		odb.close();

	}

	public void testGetLimitedResult3() throws Exception {
		String baseName = getBaseName();
		int size = 1000;

		ODB odb = open(baseName);

		for (int i = 0; i < size; i++) {
			if (i < size / 2) {
				odb.store(new Function("function " + i));
			} else {
				odb.store(new Function("FUNCTION " + i));
			}

		}
		odb.close();

		odb = open(baseName);
		Query q = odb.query(Function.class, W.like("name", "FUNCTION%"));
		Objects os = odb.getObjects(q, true, 0, 10);
		assertEquals(10, os.size());

		if (testNewFeature) {
			for (int i = size / 2; i < size / 2 + 10; i++) {
				Function f = (Function) os.next();
				assertEquals("FUNCTION " + i, f.getName());
			}
		}
		odb.close();
	}

	public void testGetLimitedResult4() throws Exception {
		String baseName = getBaseName();
		int size = 1000;

		ODB odb = open(baseName);

		for (int i = 0; i < size; i++) {
			if (i < size / 2) {
				odb.store(new Function("function " + i));
			} else {
				odb.store(new Function("FUNCTION " + i));
			}

		}
		odb.close();

		odb = open(baseName);
		Query q = odb.query(Function.class, W.like("name", "FUNCTION%"));
		q.getQueryParameters().setStartAndEndIndex(10, 20);
		Objects os = q.objects();
		assertEquals(10, os.size());

		if (testNewFeature) {
			for (int i = size / 2 + 10; i < size / 2 + 20; i++) {
				Function f = (Function) os.next();
				assertEquals("FUNCTION " + i, f.getName());
			}
		}
		odb.close();
	}
}
