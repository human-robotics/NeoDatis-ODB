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
package org.neodatis.odb.core.query.values;

import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.query.IQueryFieldAction;
import org.neodatis.odb.core.query.ValuesQuery;
import org.neodatis.odb.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.core.query.criteria.CriteriaQueryImpl;
import org.neodatis.odb.core.query.criteria.Criterion;
import org.neodatis.tool.wrappers.OdbString;
import org.neodatis.tool.wrappers.list.IOdbList;
import org.neodatis.tool.wrappers.list.OdbArrayList;

import java.util.HashSet;
import java.util.Iterator;

/**
 * A values Criteria quwry is a query to retrieve object values instead of
 * objects. Values Criteria Query allows one to retrieve one field value of an
 * object: - A field values - The sum of a specific numeric field - The Max
 * value of a specific numeric field - The Min value of a specific numeric field
 * - The Average value of a specific numeric value
 * 
 * @author osmadja
 * 
 */
public class ValuesCriteriaQuery extends CriteriaQueryImpl implements ValuesQuery {

	private IOdbList<IQueryFieldAction> objectActions;
	private String[] groupByFieldList;
	private boolean hasGroupBy;
	/**
	 * To specify if the result must build instance of object meta
	 * representation
	 */
	private boolean returnInstance;

	public ValuesCriteriaQuery(Class aClass, ObjectOid oid) {
		super(aClass.getName());
		setOidOfObjectToQuery(oid);
		init();
	}

	public ValuesCriteriaQuery(Class aClass, Criterion criteria) {
		super(aClass.getName(), criteria);
		init();
	}

	public ValuesCriteriaQuery(Class aClass) {
		super(aClass.getName());
		init();
	}

	public ValuesCriteriaQuery(String aFullClassName) {
		super(aFullClassName);
		init();
	}

	public ValuesCriteriaQuery(String aFullClassName, Criterion criteria) {
		super(aFullClassName, criteria);
		init();

	}

	public ValuesCriteriaQuery(CriteriaQuery query) {
		this(query.getFullClassName(), query.getCriteria());

	}

	private void init() {
		objectActions = new OdbArrayList<IQueryFieldAction>();
		returnInstance = true;
	}

	public ValuesQuery count(String alias) {
		objectActions.add(new CountAction(alias));
		return this;

	}

	public ValuesQuery sum(String attributeName) {
		return sum(attributeName, attributeName);
	}

	public ValuesQuery sum(String attributeName, String alias) {
		objectActions.add(new SumAction(attributeName, alias));
		return this;
	}

	public ValuesQuery sublist(String attributeName, int fromIndex, int size, boolean throwException) {
		return sublist(attributeName, attributeName, fromIndex, size, throwException);
	}

	public ValuesQuery sublist(String attributeName, String alias, int fromIndex, int size, boolean throwException) {
		objectActions.add(new SublistAction(attributeName, alias, fromIndex, size, throwException));
		return this;
	}

	public ValuesQuery sublist(String attributeName, int fromIndex, int toIndex) {
		return sublist(attributeName, attributeName, fromIndex, toIndex);
	}

	public ValuesQuery sublist(String attributeName, String alias, int fromIndex, int toIndex) {
		objectActions.add(new SublistAction(attributeName, alias, fromIndex, toIndex));
		return this;
	}

	public ValuesQuery size(String attributeName) {
		return size(attributeName, attributeName);
	}

	public ValuesQuery size(String attributeName, String alias) {
		objectActions.add(new SizeAction(attributeName, alias));
		return this;
	}

	public ValuesQuery avg(String attributeName) {
		return avg(attributeName, attributeName);
	}

	public ValuesQuery avg(String attributeName, String alias) {
		objectActions.add(new AverageValueAction(attributeName, alias, getSessionEngine().getSession().getConfig().getScaleForAverageDivision(), getSessionEngine().getSession().getConfig().getRoundTypeForAverageDivision()));
		return this;
	}

	public ValuesQuery max(String attributeName) {
		return max(attributeName, attributeName);
	}

	public ValuesQuery max(String attributeName, String alias) {
		objectActions.add(new MaxValueAction(attributeName, alias));
		return this;
	}

	public ValuesQuery min(String attributeName) {
		return min(attributeName, attributeName);
	}

	public ValuesQuery min(String attributeName, String alias) {
		objectActions.add(new MinValueAction(attributeName, alias));
		return this;
	}

	public ValuesQuery field(String attributeName) {
		return field(attributeName, attributeName);
	}

	public ValuesQuery field(String attributeName, String alias) {
		objectActions.add(new FieldValueAction(attributeName, alias));
		return this;
	}

	public ValuesQuery custom(String attributeName, ICustomQueryFieldAction action) {
		return custom(attributeName, attributeName, action);
	}

	public ValuesQuery custom(String attributeName, String alias, ICustomQueryFieldAction action) {
		action.setAttributeName(attributeName);
		action.setAlias(alias);
		objectActions.add(action);
		return this;
	}

	public IOdbList<IQueryFieldAction> getObjectActions() {
		return objectActions;
	}

	/**
	 * Returns the list of involved fields for this query. List of String
	 * 
	 * <pre>
	 * If query must return sum(&quot;value&quot;) and field(&quot;name&quot;), involvedField will contain &quot;value&quot;,&quot;name&quot;
	 * </pre>
	 */
	public HashSet<String> getAllInvolvedFields() {
		HashSet<String> l = new HashSet<String>();

		l.addAll(super.getAllInvolvedFields());

		Iterator<IQueryFieldAction> iterator = objectActions.iterator();
		IQueryFieldAction oa = null;
		String name = null;
		while (iterator.hasNext()) {
			oa = iterator.next();
			if (oa.getClass() != CountAction.class) {
				name = oa.getAttributeName();
				l.add(name);
			}
		}
		if (hasGroupBy) {
			for (int i = 0; i < groupByFieldList.length; i++) {
				name = groupByFieldList[i];
				l.add(name);
			}
		}
		if (hasOrderBy()) {
			for (int i = 0; i < orderByFields.length; i++) {
				name = orderByFields[i];
				l.add(name);
			}
		}
		return l;
	}

	public boolean isMultiRow() {
		boolean isMultiRow = true;
		IQueryFieldAction oa = null;

		// Group by protection
		// When a group by with one field exist in the query, FieldObjectAction
		// with this field must be set to SingleRow
		boolean groupBy = hasGroupBy && groupByFieldList.length == 1;
		String oneGroupByField = null;
		Iterator<IQueryFieldAction> iterator = objectActions.iterator();
		if (groupBy) {
			oneGroupByField = groupByFieldList[0];
			while (iterator.hasNext()) {
				oa = iterator.next();
				if (oa instanceof FieldValueAction && oa.getAttributeName().equals(oneGroupByField)) {
					oa.setMultiRow(false);
				}
			}
		}

		iterator = objectActions.iterator();
		if (iterator.hasNext()) {
			oa = iterator.next();
			isMultiRow = oa.isMultiRow();
		}

		while (iterator.hasNext()) {
			oa = iterator.next();
			if (isMultiRow != oa.isMultiRow()) {
				throw new NeoDatisRuntimeException(NeoDatisError.VALUES_QUERY_NOT_CONSISTENT.addParameter(this));
			}
		}
		return isMultiRow;
	}

	public ValuesQuery groupBy(String fieldList) {
		groupByFieldList = OdbString.split(fieldList, ",");
		hasGroupBy = true;
		return this;
	}

	public boolean hasGroupBy() {
		return hasGroupBy;
	}

	public String[] getGroupByFieldList() {
		return groupByFieldList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.query.IValuesQuery#returnInstance()
	 */
	public boolean returnInstance() {
		return returnInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.query.IValuesQuery#setReturnInstance(boolean)
	 */
	public void setReturnInstance(boolean returnInstance) {
		this.returnInstance = returnInstance;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.query.ValuesQuery#values(org.neodatis.odb.core.query.ValuesQuery)
	 */
	public Values values() {
		return getSessionEngine().getValues(this);
	}
}
