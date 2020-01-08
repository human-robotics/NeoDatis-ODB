package org.neodatis.odb.core.query;

import org.neodatis.tool.wrappers.OdbComparable;

import java.io.Serializable;

public abstract class CompareKey implements OdbComparable, Serializable {

	public abstract int compareTo(Object o) ;

}
