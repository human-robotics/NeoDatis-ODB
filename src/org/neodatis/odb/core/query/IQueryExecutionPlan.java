package org.neodatis.odb.core.query;

import org.neodatis.odb.core.layers.layer2.meta.ClassInfoIndex;

import java.io.Serializable;

public interface IQueryExecutionPlan extends Serializable{
	boolean useIndex();
	ClassInfoIndex getIndex();
	String getDetails();
	long getDuration();
	void start();
	void end();

}
