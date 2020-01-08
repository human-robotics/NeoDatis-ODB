/**
 * 
 */
package org.neodatis.odb.test.query.criteria;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * @author olivier
 * 
 */
public class TestQueryOrderBy extends ODBTest {

	public void test1() {
		String baseName = getBaseName();

		ODB odb = open(baseName);

		odb.store(new Class1("c1"));
		odb.store(new Class1("c1"));
		odb.store(new Class1("c2"));
		odb.store(new Class1("c2"));
		odb.store(new Class1("c3"));
		odb.store(new Class1("c4"));

		odb.close();

		odb = open(baseName);

		Query q = odb.query(Class1.class);
		q.orderByAsc("name");
		Objects objects = odb.getObjects(q);
		assertEquals(6, objects.size());
		while (objects.hasNext()) {
			System.out.println(objects.next());
		}

		// println(objects);

		odb.close();

	}

	public void test2() {
		String baseName = getBaseName();

		ODB odb = open(baseName);

		odb.store(new Class1("c1"));
		odb.store(new Class1("c1"));
		odb.store(new Class1("c2"));
		odb.store(new Class1("c2"));
		odb.store(new Class1("c3"));
		odb.store(new Class1("c4"));

		odb.close();

		odb = open(baseName);

		Query q = odb.query(Class1.class);
		// q.orderByAsc("name");
		Objects objects = odb.getObjects(q);
		assertEquals(6, objects.size());

		println(objects);

		odb.close();

	}

	public void test3() {
		String baseName = getBaseName();

		ODB odb = open(baseName);

		int size = 500;
		for (int i = 0; i < size; i++) {
			odb.store(new Class1("c1"));
		}
		for (int i = 0; i < size; i++) {
			odb.store(new Class1("c2"));
		}
		odb.close();

		odb = open(baseName);

		Query q = odb.query(Class1.class).orderByAsc("name" );
		// q.orderByAsc("name");
		Objects<Class1> objects = q.objects();
		assertEquals(size * 2, objects.size());

		for (int i = 0; i < size; i++) {
			Class1 c1 = (Class1) objects.next();
			assertEquals("c1", c1.getName());
		}
		for (int i = 0; i < size; i++) {
			Class1 c1 = (Class1) objects.next();
			assertEquals("c2", c1.getName());
		}

		odb.close();

	}

	public void test4() {
		String baseName = getBaseName();

		ODB odb = open(baseName);

		int size = 5;
		for (int i = 0; i < size; i++) {
			odb.store(new Function("f" + (i + 1)));
		}
		odb.close();

		odb = open(baseName);

		Query q = odb.query(Function.class);
		// q.orderByAsc("name");
		Objects objects = odb.getObjects(q, true, 0, 2);
		List l = new ArrayList<Function>(objects);
		assertEquals(2, l.size());

		odb.close();

		odb = open(baseName);

		q = odb.query(Function.class);
		q.orderByAsc("name");
		objects = odb.getObjects(q, true, 0, 2);
		l = new ArrayList<Function>(objects);
		assertEquals(2, l.size());

		odb.close();

		odb = open(baseName);
		q = odb.query(Function.class);
		q.orderByDesc("name");
		objects = odb.getObjects(q, true, 0, 2);
		l = new ArrayList<Function>(objects);
		assertEquals(2, l.size());

		odb.close();

	}

	public void test51() {
		String baseName = getBaseName();
		ODB odb = open(baseName);
		odb.store(new Function("Not Null"));
		odb.store(new Function(null));
		odb.close();

		odb = open(baseName);
		Query q = odb.query(Function.class, W.isNotNull("name"));
		// q.orderByAsc("name");
		Objects objects = odb.getObjects(q, true, 0, 10);
		List l = new ArrayList<Function>(objects);
		odb.close();
		assertEquals(1, l.size());

	}

	public void test5() {
		String baseName = getBaseName();

		ODB odb = open(baseName);

		int size = 5;
		for (int i = 0; i < size; i++) {
			odb.store(new Function("f1"));
		}
		odb.store(new Function(null));
		odb.close();

		odb = open(baseName);

		Query q = odb.query(Function.class, W.isNotNull("name"));
		// q.orderByAsc("name");
		Objects objects = odb.getObjects(q, true, 0, 10);
		List l = new ArrayList<Function>(objects);
		assertEquals(size, l.size());

		odb.close();

		odb = open(baseName);

		q = odb.query(Function.class, W.isNotNull("name"));
		q.orderByAsc("name");
		objects = odb.getObjects(q, true, 0, 10);
		l = new ArrayList<Function>(objects);
		assertEquals(5, l.size());

		odb.close();

		odb = open(baseName);

		q = odb.query(Function.class, W.isNotNull("name"));
		q.orderByDesc("name");
		objects = odb.getObjects(q, true, 0, 10);
		l = new ArrayList<Function>(objects);
		assertEquals(5, l.size());

		odb.close();

	}

	public void test6() {
		String baseName = getBaseName();

		ODB odb = open(baseName);

		int size = 5;
		for (int i = 0; i < size; i++) {
			odb.store(new Function("f1"));
		}
		odb.store(new Function(null));
		odb.close();

		odb = open(baseName);

		Query q = odb.query(Function.class, W.isNull("name"));
		// q.orderByAsc("name");
		Objects objects = odb.getObjects(q, true, 0, 10);
		List l = new ArrayList<Function>(objects);
		assertEquals(1, l.size());

		odb.close();

		odb = open(baseName);

		q = odb.query(Function.class, W.isNull("name"));
		q.orderByAsc("name");
		objects = odb.getObjects(q, true, 0, 10);
		l = new ArrayList<Function>(objects);
		assertEquals(1, l.size());

		odb.close();

		odb = open(baseName);

		q = odb.query(Function.class, W.isNull("name"));
		q.orderByDesc("name");
		objects = odb.getObjects(q, true, 0, 10);
		l = new ArrayList<Function>(objects);
		assertEquals(1, l.size());

		odb.close();

	}

}
