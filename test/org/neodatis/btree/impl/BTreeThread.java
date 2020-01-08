/**
 * 
 */
package org.neodatis.btree.impl;

import org.neodatis.btree.impl.multiplevalue.InMemoryBTreeMultipleValuesPerKey;

/**
 * @author olivier
 *
 */
public class BTreeThread extends Thread {
	protected String label;
	protected InMemoryBTreeMultipleValuesPerKey tree;
	protected int size;
	/**
	 * @param tree
	 * @param string
	 */
	public BTreeThread(InMemoryBTreeMultipleValuesPerKey tree, String string, int size) {
		this.label = string;
		this.tree = tree;
		this.size = size;
	}

	public void run() {
		for(int i=0;i<size;i++){
			tree.insert(new Integer(i), label + "-Key "+i+" value");
		}
	}
}
