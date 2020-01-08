package org.neodatis.odb.test.btree.odb;

import org.neodatis.OrderByConstants;
import org.neodatis.btree.BTreeIteratorMultipleValuesPerKey;
import org.neodatis.btree.IBTree;
import org.neodatis.btree.impl.multiplevalue.InMemoryBTreeMultipleValuesPerKey;
import org.neodatis.odb.test.ODBTest;

import java.util.Iterator;

public class MultipleKeyBTree extends ODBTest {

	public void test1() {
		int size = 100000;
		IBTree tree = new InMemoryBTreeMultipleValuesPerKey("test1", 50);
		for (int i = 0; i < size; i++) {
			if (i % 10000 == 0) {
				println(i);
			}
			tree.insert(new Integer(i + 1), "value " + (i + 1));
		}
		assertEquals(size, tree.getSize());
		Iterator iterator = new BTreeIteratorMultipleValuesPerKey(tree, OrderByConstants.ORDER_BY_ASC);
		int j = 0;
		while (iterator.hasNext()) {
			Object o = iterator.next();
			// println(o);
			j++;
			if (j == size) {
				assertEquals("value " + size, o);
			}
		}
	}

	public void test2SameKey() {
		int size = 1000;
		int size2 = 100;
		IBTree tree = new InMemoryBTreeMultipleValuesPerKey("test1", 50);
		for (int i = 0; i < size; i++) {
			if (i % 10000 == 0) {
				println(i);
			}
			tree.insert(new Integer(i + 1), "value " + (i + 1));
		}

		for (int i = 0; i < size2; i++) {
			tree.insert(new Integer(100), "value " + (i + 1));
		}
		assertEquals(size + size2, tree.getSize());

	}

}
