package org.neodatis.odb.test.resistance;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

import java.math.BigInteger;

public class TestBigDatabase extends ODBTest {
	public void t1est8() throws Exception {
		int size1 = 10000;
		int size2 = 1000;
		String baseName = getBaseName();
		ODB odb = null;
		Objects os = null;
		ObjectOid oid = null;
		odb = NeoDatis.open(baseName);
		long t0 = System.currentTimeMillis();
		for (int i = 0; i < size1; i++) {
			odb = NeoDatis.open(baseName);
			for (int j = 0; j < size2; j++) {
				Function f = new Function("function " + j);
				oid = odb.store(f);
			}
			
			if (i % 100 == 0) {
				long t1  =System.currentTimeMillis();
				println(i + "/" + size1 + " - time=" + (t1-t0) + " - last Object Oid = " + oid);
				t0 = t1;
			}
		}
		odb.close();
		odb = NeoDatis.open(baseName);
		t0 = System.currentTimeMillis();
		BigInteger size = odb.query(Function.class).count();
		System.out.println("Real size  is " + size + " -  time to get size = " + (System.currentTimeMillis()-t0));
		odb.close();

		//deleteBase(baseName);
	}
	
	public void t1est9() throws Exception {
		int size1 = 10000;
		int size2 = 1000;
		String baseName = getBaseName();
		ODB odb = null;
		Objects os = null;
		ObjectOid oid = null;
		odb = NeoDatis.open(baseName);
		long t0 = System.currentTimeMillis();
		for (int i = 0; i < size1; i++) {
			odb = NeoDatis.open(baseName);
			for (int j = 0; j < size2; j++) {
				Function f = new Function("function " + j);
				oid = odb.store(f);
			}
			
			if (i % 100 == 0) {
				long t1  =System.currentTimeMillis();
				println(i + "/" + size1 + " - time=" + (t1-t0) + " - last Object Oid = " + oid);
				t0 = t1;
			}
		}
		odb.close();
		odb = NeoDatis.open(baseName);
		t0 = System.currentTimeMillis();
		BigInteger size = odb.query(Function.class).count();
		System.out.println("Real size  is " + size + " -  time to get size = " + (System.currentTimeMillis()-t0));
		odb.close();

		//deleteBase(baseName);
	}
}
