/**
 * 
 */
package org.neodatis.odb.test.btree.odb.nativ;

import org.neodatis.btree.IBTreePersister;
import org.neodatis.btree.IBTreeSingleValuePerKey;
import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.core.btree.nativ.NativeBTree;
import org.neodatis.odb.core.btree.nativ.NativeBTreePersisterWithNeoDatisFS;
import org.neodatis.odb.core.btree.nativ.Position;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 * 
 */
public class SingleValueBTree extends ODBTest {
	
	public IBTreePersister getPersister(String fileName, int degree){
		//return new NativeBTreePersisterWithSplit(fileName,1024*1024*10,degree);
		return new NativeBTreePersisterWithNeoDatisFS(fileName,1024*1024*10,degree,false, "UTF-8",NeoDatis.getConfig());
		//return new InMemoryPersister();
	}
	public IBTreeSingleValuePerKey getBTree(int degree, IBTreePersister persister){
		return new NativeBTree(new Long(1), degree, persister, new Long(1));
		//return new InMemoryBTreeSingleValuePerKey("name",degree,persister);
	}
	
	
	public void test1() throws Exception {

		long tt1 = System.currentTimeMillis();
		int size = 10000;
		String fileName = getBaseName();
		int degree = 30;
		IBTreePersister persister = getPersister(fileName, degree);
		IBTreeSingleValuePerKey tree = getBTree(degree, persister);
		long t0 = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			if (i % 10000 == 0) {
				long t = System.currentTimeMillis();
				println(i + " - " + (t-t0)+"ms");
				t0 = t;
			}
			tree.insert(new Long(i + 1), new Position(i, i , i));
		}
		long tt11 = System.currentTimeMillis();
		println("Time for insert " + (tt11-tt1));
		tree = (IBTreeSingleValuePerKey) persister.loadBTree(new Long(1));
		//System.out.println(new BTreeDisplay().build(tree, true).toString());

		persister.close();
		println("Time for commit " + (System.currentTimeMillis()-tt11));
		
		persister = getPersister(fileName, degree);
		tree = (IBTreeSingleValuePerKey) persister.loadBTree(new Long(1));
		//System.out.println(new BTreeDisplay().build(tree, true).toString());
		assertEquals(size, tree.getSize());
		long ts1 = System.currentTimeMillis();
		Object o = tree.search(new Long(2));
		long ts2 = System.currentTimeMillis();
		System.out.println("Found object  is " +o);
		persister.close();
		long tt2 = System.currentTimeMillis();
		System.out.println("Search time is " + (ts2-ts1));
		System.out.println("Total time is " + (tt2-tt1));
		
		
	}
}
