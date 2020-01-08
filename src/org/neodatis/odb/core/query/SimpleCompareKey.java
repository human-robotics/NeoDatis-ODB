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
package org.neodatis.odb.core.query;


/**
 * A simple compare key : an object that contains various values used for indexing query result
 * <p>
 * 
 * </p>
 *
 */
public class SimpleCompareKey extends CompareKey{
    private Comparable  key;
    
    public SimpleCompareKey(Comparable key){
        this.key = key;
    }

    public int compareTo(Object o) {
        if(o==null || o.getClass()!=SimpleCompareKey.class){
            return -1;
        }
        SimpleCompareKey ckey = (SimpleCompareKey) o;
        return key.compareTo(ckey.key);
    }
    
    public String toString(){
    	return key.toString();
    }
    
    public boolean equals(Object o) {
    	if(o==null || !(o instanceof SimpleCompareKey)){
    		return false;
    	}
    	SimpleCompareKey c = (SimpleCompareKey) o;
    	return key.equals(c.key);
    }

    public Comparable getKey(){
    	return key;
    }
	public int hashCode() {
		return key.hashCode();
	}
    

}
