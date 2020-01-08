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
import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.NeoDatisError;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * A collection that uses a BTree as an underlying system to provide ordered by Collections
 * <p>
 * 
 * </p>
 *@sharpen.ignore
 */
public abstract class AbstractBTreeCollection<E> implements Objects<E>, Serializable{

	private IBTree tree;
	 
	private int size;
    private transient Iterator<E> currentIterator;
    private OrderByConstants orderByType;
	
	public AbstractBTreeCollection(int size, OrderByConstants orderByType, int btreeDegree){
		// TODO compute degree best value for the size value
		tree = buildTree(btreeDegree);
        this.orderByType = orderByType;
	}
	public AbstractBTreeCollection(){
		this(50,OrderByConstants.ORDER_BY_NONE, 20);
	}
    
    public abstract IBTree buildTree(int degree);
    
	public E first() {
		return iterator(orderByType).next();
	}

	public boolean hasNext() {
		if(currentIterator==null){
            currentIterator = iterator(orderByType);
        }
        
        return currentIterator.hasNext();
	}

	public E next() {
        if(currentIterator==null){
            currentIterator = iterator(orderByType);
        }

        return currentIterator.next();
		
	}

	public boolean add(E o) {
		tree.insert(new Integer(size),o);
		size++;
		return true;
	}
    
    /**Adds the object in the btree with the specific key
     * 
     * @param key
     * @param o
     * @return
     */
    public boolean addWithKey(Comparable key, E o) {
        tree.insert(key,o);
        size++;
        return true;
    }
    /**Adds the object in the btree with the specific key
     * 
     * @param key
     * @param o
     * @return
     */
    public boolean addWithKey(int key, E o) {
        tree.insert(new Integer(key),o);
        size++;
        return true;
    }

	public boolean addAll(Collection<? extends E> collection) {
		Iterator<? extends E> iterator = collection.iterator();
		while(iterator.hasNext()){
			add(iterator.next());
		}
		return true;
	}

	public void clear() {
		tree.clear();

	}

	public boolean contains(Object o) {
		throw new NeoDatisRuntimeException(NeoDatisError.OPERATION_NOT_IMPLEMENTED.addParameter("contains"));
	}

	public boolean containsAll(Collection collection) {
		throw new NeoDatisRuntimeException(NeoDatisError.OPERATION_NOT_IMPLEMENTED.addParameter("containsAll"));
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Iterator<E> iterator() {
		return iterator(orderByType);
	}
	public Iterator<E> iterator(OrderByConstants newOrderByType) {
		return (Iterator<E>) tree.iterator(newOrderByType);
	}

	public boolean remove(Object o) {
		throw new NeoDatisRuntimeException(NeoDatisError.OPERATION_NOT_IMPLEMENTED.addParameter("remove"));
	}

	public boolean removeAll(Collection collection) {
		throw new NeoDatisRuntimeException(NeoDatisError.OPERATION_NOT_IMPLEMENTED.addParameter("removeAll"));
	}

	public boolean retainAll(Collection collection) {
		throw new NeoDatisRuntimeException(NeoDatisError.OPERATION_NOT_IMPLEMENTED.addParameter("retainAll"));
	}

	public int size() {
		return size;
	}

	public Object[] toArray() {
		return toArray(new Object[size]);
	}

	public Object[] toArray(Object[] objects) {
		Iterator iterator = iterator();
		int i=0;
		while(iterator.hasNext()){
			objects[i++]=iterator.next();
		}
		return objects;
	}

	public void reset(){
		currentIterator = iterator(orderByType);
	}
	
	protected OrderByConstants getOrderByType() {
		return orderByType;
	}
	protected IBTree getTree() {
		return tree;
	}
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("size=").append(size).append(" [");
		Iterator<E> iterator = iterator();
		while(iterator.hasNext()){
			s.append(iterator.next());
			if(iterator.hasNext()){
				s.append(" , ");
			}
		}
		s.append("]");
		return s.toString();
	}

}
