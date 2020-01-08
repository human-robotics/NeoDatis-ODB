package org.neodatis.odb.test.query.nq;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.nq.NativeQuery;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.human.Animal;
import org.neodatis.odb.test.vo.human.Human;
import org.neodatis.odb.test.vo.human.Man;
import org.neodatis.odb.test.vo.human.Woman;

public class TestPolyMorphic extends ODBTest {

	public void test1() throws Exception {
		if (!isLocal) {
			return;
		}
		ODB odb = open(getBaseName());

		odb.store(new Animal("dog", "M", "my dog"));
		odb.store(new Animal("cat", "F", "my cat"));

		odb.store(new Man("Joe"));
		odb.store(new Woman("Karine"));

		odb.close();

		odb = open(getBaseName());
		NativeQuery q = new NativeQuery<Animal>() {
			public boolean match(Animal animal) {
				return true;
			}
		};
		Objects os = odb.query(q).setPolymorphic(true).objects();
		println(os);
		odb.close();
		assertEquals(4, os.size());
	}

	public void test2() throws Exception {
		if (!isLocal) {
			return;
		}
		
		ODB odb = open(getBaseName());

		odb.store(new Animal("dog", "M", "my dog"));
		odb.store(new Animal("cat", "F", "my cat"));

		odb.store(new Man("Joe"));
		odb.store(new Woman("Karine"));

		odb.close();

		odb = open(getBaseName());
		NativeQuery q = new NativeQuery<Human>() {
			public boolean match(Human human) {
				return true;
			}
		};
		q.setPolymorphic(true);
		Objects os = odb.query(q).setPolymorphic(true).objects();
		println(os);
		odb.close();
		assertEquals(2, os.size());
		
	}

	public void test8() throws Exception {
		if (!isLocal) {
			return;
		}
		int size = isLocal ? 300 : 30;
		
		ODB odb = open(getBaseName());
		for (int i = 0; i < size; i++) {
			odb.store(new Animal("dog", "M", "my dog" + i));
			odb.store(new Animal("cat", "F", "my cat" + i));

			odb.store(new Man("Joe" + i));
			odb.store(new Woman("my Karine" + i));
		}

		odb.close();

		odb = open(getBaseName());
		Query q = new NativeQuery<Animal>() {
			public boolean match(Animal object) {
				return object.getName().startsWith("my ");
			}
		};
		//no polymorpihc
		
		Objects objects = odb.query(q).objects();
		odb.close();
		
		assertEquals(size * 2, objects.size());

	}
	public void test9 () throws Exception {
		if (!isLocal) {
			return;
		}
		int size = isLocal ? 300 : 30;
		
		ODB odb = open(getBaseName());
		for (int i = 0; i < size; i++) {
			odb.store(new Animal("dog", "M", "my dog" + i));
			odb.store(new Animal("cat", "F", "my cat" + i));

			odb.store(new Man("Joe" + i));
			odb.store(new Woman("my Karine" + i));
		}

		odb.close();

		odb = open(getBaseName());
		Query q = new NativeQuery<Animal>() {
			public boolean match(Animal object) {
				return object.getName().startsWith("my ");
			}
		}.setPolymorphic(true);
		
		Objects objects = odb.query(q).objects();
		odb.close();
		
		assertEquals(size * 3, objects.size());

	}


}
