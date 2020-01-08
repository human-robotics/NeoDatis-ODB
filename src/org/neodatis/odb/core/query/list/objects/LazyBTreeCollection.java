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
package org.neodatis.odb.core.query.list.objects;

import org.neodatis.OrderByConstants;
import org.neodatis.btree.IBTree;
import org.neodatis.btree.impl.multiplevalue.InMemoryBTreeMultipleValuesPerKey;
import org.neodatis.odb.core.btree.LazyODBBTreeIteratorMultiple;
import org.neodatis.odb.core.session.SessionEngine;

import java.util.Iterator;

/**
 * A collection using a BTtree as a back-end component. Lazy because it only keeps the oids of the objects. When asked for an object, loads
 * it on demand and returns  it
 * @author osmadja
 *
 */
public class LazyBTreeCollection<T> extends AbstractBTreeCollection<T> {

	private SessionEngine sessionEngine;
    private boolean returnObjects;
    
    public LazyBTreeCollection(int size, SessionEngine engine, boolean returnObjects, int btreeDegree) {
        super(size, OrderByConstants.ORDER_BY_ASC,btreeDegree);
        this.sessionEngine = engine;
        this.returnObjects = returnObjects;
    }

    public LazyBTreeCollection(int size, OrderByConstants orderByType,int btreeDegree) {
        super(size, orderByType,btreeDegree);
    }

    public IBTree buildTree(int degree) {
        return new InMemoryBTreeMultipleValuesPerKey("default",degree);
    }

	public Iterator<T> iterator(OrderByConstants orderByType) {
		return (Iterator<T>) new LazyODBBTreeIteratorMultiple( getTree(), orderByType, sessionEngine, returnObjects);
	}

}
