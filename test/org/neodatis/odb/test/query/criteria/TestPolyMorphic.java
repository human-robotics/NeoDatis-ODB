package org.neodatis.odb.test.query.criteria;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.ValuesQuery;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.human.Animal;
import org.neodatis.odb.test.vo.human.Human;
import org.neodatis.odb.test.vo.human.Man;
import org.neodatis.odb.test.vo.human.Woman;

import java.math.BigInteger;

public class TestPolyMorphic extends ODBTest {

	public void test1() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		odb.store(new Animal("dog", "M", "my dog"));
		odb.store(new Animal("cat", "F", "my cat"));

		odb.store(new Man("Joe"));
		odb.store(new Woman("Karine"));

		odb.close();

		odb = open(baseName);
		Query q = odb.query(Object.class);
		q.setPolymorphic(true);
		Objects os = odb.getObjects(q);
		println(os);
		odb.close();
		assertEquals(4, os.size());
	}

	public void test2() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		odb.store(new Animal("dog", "M", "my dog"));
		odb.store(new Animal("cat", "F", "my cat"));

		odb.store(new Man("Joe"));
		odb.store(new Woman("Karine"));

		odb.close();

		odb = open(baseName);
		Query q = odb.query(Human.class);
		q.setPolymorphic(true);
		Objects os = odb.getObjects(q);
		println(os);
		odb.close();
		assertEquals(2, os.size());
	}

	public void test3() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		odb.store(new Animal("dog", "M", "my dog"));
		odb.store(new Animal("cat", "F", "my cat"));

		odb.store(new Man("Joe"));
		odb.store(new Woman("Karine"));

		odb.close();

		odb = open(baseName);
		ValuesQuery q = odb.queryValues(Object.class).field("specie");
		q.setPolymorphic(true);
		Values os = q.values();
		println(os);
		odb.close();
		assertEquals(4, os.size());
	}

	public void test4() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		odb.store(new Animal("dog", "M", "my dog"));
		odb.store(new Animal("cat", "F", "my cat"));

		odb.store(new Man("Joe"));
		odb.store(new Woman("Karine"));

		odb.close();

		odb = open(baseName);
		ValuesQuery q = odb.queryValues(Human.class).field("specie");
		q.setPolymorphic(true);
		Values os = q.values();
		println(os);
		odb.close();
		assertEquals(2, os.size());
	}

	public void test5() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		odb.store(new Animal("dog", "M", "my dog"));
		odb.store(new Animal("cat", "F", "my cat"));

		odb.store(new Man("Joe"));
		odb.store(new Woman("Karine"));

		odb.close();

		odb = open(baseName);
		ValuesQuery q = odb.queryValues(Man.class).field("specie");
		q.setPolymorphic(true);
		Values os = q.values();
		println(os);
		odb.close();
		assertEquals(1, os.size());
	}

	public void test6() throws Exception {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		odb.store(new Animal("dog", "M", "my dog"));
		odb.store(new Animal("cat", "F", "my cat"));

		odb.store(new Man("Joe"));
		odb.store(new Woman("Karine"));

		odb.close();

		odb = open(baseName);
		Query q = odb.query(Object.class);
		q.setPolymorphic(true);
		BigInteger nb = q.count();
		println(nb);
		odb.close();
		assertEquals(new BigInteger("4"), nb);
	}

	public void test7() throws Exception {
		int size = isLocal ? 3000 : 300;
		String baseName = getBaseName();
		ODB odb = open(baseName);
		for (int i = 0; i < size; i++) {
			odb.store(new Animal("dog", "M", "my dog"));
			odb.store(new Animal("cat", "F", "my cat"));

			odb.store(new Man("Joe" + i));
			odb.store(new Woman("Karine" + i));
		}

		odb.close();

		odb = open(baseName);
		Query q = odb.query(Object.class);
		q.setPolymorphic(true);
		BigInteger nb = q.count();
		println(nb);
		odb.close();
		assertEquals(new BigInteger("" + 4 * size), nb);
		
	}

	public void test8() throws Exception {
		String baseName = getBaseName();
		int size = isLocal ? 300 : 30;
		ODB odb = open(baseName);
		for (int i = 0; i < size; i++) {
			odb.store(new Animal("dog" + i, "M", "my dog" + i));
			odb.store(new Animal("cat" + i, "F", "my cat" + i));

			odb.store(new Man("Joe" + i));
			odb.store(new Woman("Karine" + i));
		}

		odb.close();

		odb = open(baseName);
		Query q = odb.query(Object.class, W.equal("specie", "man"));
		q.setPolymorphic(true);
		BigInteger nb = q.count();
		println(nb);
		odb.close();
		assertEquals(new BigInteger("" + 1 * size), nb);
		
	}

}
