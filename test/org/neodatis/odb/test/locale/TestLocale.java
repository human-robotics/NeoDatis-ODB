/**
 * 
 */
package org.neodatis.odb.test.locale;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.attribute.AllAttributeClass;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author olivier
 *
 */
public class TestLocale extends ODBTest {
	public void test1() throws UnsupportedEncodingException{
		
		ODB odb = null;
		String baseName = getBaseName();
		Locale defaultLocale = Locale.getDefault(); 
		
		try{
			NeoDatisConfig config = NeoDatis.getConfig().setLatinDatabaseCharacterEncoding();
			odb = open(baseName,config);
			 
			Locale brLocale = new Locale("pt","BR");
			Locale.setDefault(brLocale);
			AllAttributeClass tc = new AllAttributeClass();
			tc.setBigDecimal1(new BigDecimal(5.3));
			println(tc.getBigDecimal1().toString());
			
			odb.store(tc);
			odb.close();
			
			odb = open(baseName);
			Objects<AllAttributeClass> objects = odb.query(AllAttributeClass.class).objects();
			assertEquals(1, objects.size());
			
			assertEquals(new BigDecimal(5.3), objects.first().getBigDecimal1());
			
		}finally{
			if(odb!=null && !odb.isClosed()){
				odb.close();
			}
			Locale.setDefault(defaultLocale);
		}
	}
	
	public void test2() throws UnsupportedEncodingException{
		
		ODB odb = null;
		String baseName = getBaseName();
		Locale defaultLocale = Locale.getDefault(); 
		
		try{
			NeoDatisConfig config = NeoDatis.getConfig().setLatinDatabaseCharacterEncoding();
			odb = open(baseName,config);
			
			 
			Locale brLocale = new Locale("pt","BR");
			Locale.setDefault(brLocale);
			AllAttributeClass tc = new AllAttributeClass();
			tc.setBigDecimal1(new BigDecimal("5.3"));
			println(tc.getBigDecimal1().toString());
			
			odb.store(tc);
			odb.close();
			
			odb = open(baseName);
			Objects<AllAttributeClass> objects = odb.getObjects(AllAttributeClass.class);
			assertEquals(1, objects.size());
			
			assertEquals(new BigDecimal("5.3"), objects.first().getBigDecimal1());
			
		}finally{
			if(odb!=null && !odb.isClosed()){
				odb.close();
			}
			Locale.setDefault(defaultLocale);
		}
	}

}
