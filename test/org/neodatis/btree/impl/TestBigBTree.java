/**
 * 
 */
package org.neodatis.btree.impl;

import org.neodatis.btree.impl.multiplevalue.InMemoryBTreeMultipleValuesPerKey;
import org.neodatis.btree.impl.multiplevalue.ThreadSafeBTree;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.tool.MemoryMonitor;

import java.util.List;

/**
 * @author olivier
 *
 */
public class TestBigBTree extends ODBTest {
	public void testBig1() {
		int size = 10000;
		InMemoryBTreeMultipleValuesPerKey tree = new InMemoryBTreeMultipleValuesPerKey("test", 45);
		long t0 = System.currentTimeMillis();
		long t = System.currentTimeMillis();
		for(int i=0;i<size;i++){
			tree.insert(new Integer(i), "Key "+i+" value");
			if(i%100000==0){
				long tt = t;
				t = System.currentTimeMillis();
				MemoryMonitor.displayCurrentMemory(i + " t="+ (t-tt), false);
			}
		}
		long t1 = System.currentTimeMillis();
		List l = tree.search(new Integer(size/2));
		long t2 = System.currentTimeMillis();
		assertEquals(1, l.size());
		assertEquals(size, tree.getSize());
		
		println("Time to insert = " + (t1-t0) + " insert/s = " + (size/(t1-t0)*1000));
		println("Time to get    = " + (t2-t1));
	}
	
	public void testConcurrentAccess() throws InterruptedException {
		int size = 100;
		ThreadSafeBTree tree = new ThreadSafeBTree("test", 45);
		
		BTreeThread t1 = new BTreeThread(tree, "test1", size);
		BTreeThread t2 = new BTreeThread(tree, "test2", size);
		long tt0 = System.currentTimeMillis();
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		long tt1 = System.currentTimeMillis();
		println(tt1-tt0);
		
		List l = tree.search(new Integer(size/2));
		assertEquals(2, l.size());
		assertEquals(size*2, tree.getSize());
		println(tree.getSize());
		
	}	
}
