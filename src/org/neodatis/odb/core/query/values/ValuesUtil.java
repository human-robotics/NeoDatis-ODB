package org.neodatis.odb.core.query.values;

import org.neodatis.tool.wrappers.NeoDatisNumber;

import java.math.BigDecimal;

public class ValuesUtil {

	public static BigDecimal convert(Number number){
		BigDecimal bd = NeoDatisNumber.createDecimalFromString(number.toString());
		return bd;
	}	
}
