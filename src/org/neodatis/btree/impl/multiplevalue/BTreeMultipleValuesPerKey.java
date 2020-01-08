/*
 NeoDatis ODB : Native Object Database (odb.info@neodatis.org)
 Copyright (C) 2007 NeoDatis Inc. http://www.neodatis.org

 "This file is part of the NeoDatis ODB open source object database".

 NeoDatis ODB is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 NeoDatis ODB is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.neodatis.btree.impl.multiplevalue;

import org.neodatis.OrderByConstants;
import org.neodatis.btree.BTreeIteratorMultipleValuesPerKey;
import org.neodatis.btree.IBTreeMultipleValuesPerKey;
import org.neodatis.btree.IBTreeNodeMultipleValuesPerKey;
import org.neodatis.btree.IBTreePersister;
import org.neodatis.btree.impl.AbstractBTree;

import java.util.Iterator;
import java.util.List;

public abstract class BTreeMultipleValuesPerKey extends AbstractBTree implements IBTreeMultipleValuesPerKey{

	public BTreeMultipleValuesPerKey() {
		super();
	}

	public BTreeMultipleValuesPerKey(String name, int degree, IBTreePersister persister) {
		super(name, degree, persister);
	}

	public List search(Comparable key) {
		IBTreeNodeMultipleValuesPerKey theRoot = (IBTreeNodeMultipleValuesPerKey) getRoot();
		return theRoot.search(key);
	}	
	
	public Iterator iterator(OrderByConstants orderBy) {
		return new BTreeIteratorMultipleValuesPerKey(this,orderBy);
	}
}
