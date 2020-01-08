package org.neodatis.odb.core.query;

import org.neodatis.odb.Query;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.session.SessionEngine;

public interface IMultiClassQueryExecutor extends IQueryExecutor{

	/**
	 * Used to indicate if the execute method must call start and end method of the queryResultAction. The default is yes.
	 * For MultiClass Query executor, it is set to false to avoid to reset the result
	 * @return true or false to indicate if start and end method of queryResultAction must be executed
	 */
	boolean executeStartAndEndOfQueryAction();
	void setExecuteStartAndEndOfQueryAction(boolean yes);
	SessionEngine getSessionEngine();
	Query getQuery();

	/** The class on which to execute the query*/
	void setClassInfo(ClassInfo ci);

}