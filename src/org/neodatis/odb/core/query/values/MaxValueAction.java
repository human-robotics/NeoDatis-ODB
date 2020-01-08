package org.neodatis.odb.core.query.values;

import org.neodatis.odb.OID;
import org.neodatis.odb.core.layers.layer2.meta.AttributeValuesMap;
import org.neodatis.odb.core.query.IQueryFieldAction;

import java.math.BigDecimal;

/**
 * An action to compute the max value of a field
 * @author osmadja
 *
 */
public class MaxValueAction extends AbstractQueryFieldAction {

	private BigDecimal maxValue;
	private OID oidOfMaxValues;
	
	public MaxValueAction(String attributeName, String alias) {
		super(attributeName,alias,false);
		this.maxValue = new BigDecimal(Long.MIN_VALUE);
		this.oidOfMaxValues = null;
	}


	public void execute(OID oid, AttributeValuesMap values) {
		Number n = (Number) values.get(attributeName);
		BigDecimal bd = ValuesUtil.convert(n);
		if(bd.compareTo(maxValue)>0){
			oidOfMaxValues  =oid;
			maxValue = bd;
		}
	}


	public Object getValue() {
		return maxValue;
	}


	public void end() {
		// nothing to do
	}

	public void start() {
		// Nothing to do
	}

	public OID getOidOfMaxValues() {
		return oidOfMaxValues;
	}
	
	public IQueryFieldAction copy() {
		return new MaxValueAction(attributeName,alias);
	}	

}
