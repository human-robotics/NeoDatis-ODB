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
package org.neodatis.odb.test.refactoring;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.tool.IOUtil;
import org.neodatis.tool.wrappers.OdbString;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ByteCodeTest {

	/**
	 * we use the odbTest class here to simply be able to execute in local, same
	 * vm & normal client server mode as this class already has the
	 * functionality
	 */
	protected ODBTest test;

	public ByteCodeTest() {
		test = new ODBTest();
		test.PORT = test.PORT + 10;
	}

	public static final String RESULT_FILE_NAME = "result.neodatis";

	public static final String ODB_TEST_FILE_NAME = "refactoring.neodatis";

	/**
	 * 
	 */
	protected void closeServer() {
		if(!test.isLocal&&test.useSameVmOptimization){
			test.closeServer();
		}
	}

	public void execute(String[] args) throws Exception {
		String step = args[0];

		System.out.println("\nMethod = " + step);
		try {
			Method method = this.getClass().getDeclaredMethod(step, new Class[0]);
			method.invoke(this, new Object[0]);
			testOk(step);
		} catch (Exception e) {
			System.out.println("Error while calling " + step);
			testBad(step, e);
			e.printStackTrace();
		}
	}

	protected void setFieldValue(Object o, String fieldName, Object fieldValue) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = o.getClass().getDeclaredField(fieldName);
		f.setAccessible(true);
		f.set(o, fieldValue);
	}

	protected Object getFieldValue(Object o, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Field f = o.getClass().getDeclaredField(fieldName);
		f.setAccessible(true);
		return f.get(o);
	}

	protected ODB open() throws Exception {
		return test.open(ODB_TEST_FILE_NAME);
	}

	protected ODB openResultDb() throws Exception {
		return NeoDatis.open(ODBTest.DIRECTORY+RESULT_FILE_NAME);
	}

	protected void resetDb() throws Exception {
		closeServer();
		test.deleteBase(ODB_TEST_FILE_NAME);
		closeServer();
	}

	protected void resetResultDb() throws Exception {
		IOUtil.deleteFile(ODBTest.DIRECTORY+RESULT_FILE_NAME);
	}

	protected void testOk(String testName) throws Exception {
		ODB odb = null;

		try {
			odb = openResultDb();
			Objects objects = odb.getObjects(VoTestResult.class);
			if (objects.isEmpty()) {
				VoTestResult tr = new VoTestResult();
				tr.getTests().put(testName, "ok");
				tr.setNbGoodTests(1);
				odb.store(tr);
			} else {
				VoTestResult tr = (VoTestResult) objects.first();
				tr.incrementGood();
				tr.getTests().put(this.getClass().getName() + "." + testName, "ok");
				odb.store(tr);
			}
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	protected void testBad(String testName, Exception e) throws Exception {
		ODB odb = null;

		try {
			odb = openResultDb();
			Objects objects = odb.getObjects(VoTestResult.class);
			if (objects.isEmpty()) {
				VoTestResult tr = new VoTestResult();
				tr.getTests().put(testName, OdbString.exceptionToString(e, false));
				tr.setNbBadTests(1);
				odb.store(tr);
			} else {
				VoTestResult tr = (VoTestResult) objects.first();
				// tr.getTests().put(testName,OdbString.exceptionToString(e));
				// tr.getTests().put(this.getClass().getName()+"."+testName,e.getMessage());
				tr.incrementBad();
				odb.store(tr);
			}
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	protected void assertEquals(int i, int j) throws Exception {
		if (i != j) {
			throw new Exception(i + " != " + j);
		}
	}
}
