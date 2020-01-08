package org.neodatis.odb.test.refactoring;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;

public class RefactoringTest3 extends ByteCodeTest {
	public void start() throws Exception {
		resetDb();
	}

	public void step1() throws Exception {
		JavaAssistUtility jau = new JavaAssistUtility();
		String className = "Test5";
		int nbFields = 20;
		String[] fieldNames = new String[nbFields];
		Class[] fieldTypes = new Class[nbFields];

		for (int i = 0; i < nbFields; i++) {
			fieldNames[i] = "field" + i;
			fieldTypes[i] = String.class;
		}
		Class c = jau.createClass(className, fieldNames, fieldTypes);

		ODB odb = null;
		try {
			odb = open();
			for (int j = 0; j < 100; j++) {
				Object o = c.newInstance();
				for (int i = 0; i < nbFields; i++) {
					setFieldValue(o, "field" + i, "step17:another string value of " + i);
				}
				odb.store(o);
			}
			odb.close();
			closeServer();
			testOk("step1");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			odb.close();
			closeServer();
			System.exit(1);
		}
	}

	public void step2() throws Exception {

		JavaAssistUtility jau = new JavaAssistUtility();
		String className = "Test5";
		int nbFields = 20;
		String[] fieldNames = new String[nbFields];
		Class[] fieldTypes = new Class[nbFields];

		for (int i = 0; i < nbFields; i++) {
			fieldNames[i] = "field" + i;
			fieldTypes[i] = String.class;
		}
		Class c = jau.createClass(className, fieldNames, fieldTypes);

		ODB odb = null;
		try {
			odb = open();
			Objects objects = odb.query(c).objects();
			odb.close();
			closeServer();
			assertEquals(100, objects.size());
			testOk("step2");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			odb.close();
			closeServer();
			System.exit(1);
		}
	}

	/** adds a new field of type class 6 to class 5
	 * 
	 * @throws Exception
	 */
	public void step3() throws Exception {

		JavaAssistUtility jau = new JavaAssistUtility();
		String className = "Test6";
		String[] fieldNames = new String[1];
		Class[] fieldTypes = new Class[1];

		fieldNames[0] = "field0";
		fieldTypes[0] = String.class;

		Class c6 = jau.createClass(className, fieldNames, fieldTypes);
		
		int nbFields = 20;
		String[] fieldNames2 = new String[nbFields+1];
		Class[] fieldTypes2 = new Class[nbFields+1];

		for (int i = 0; i < nbFields; i++) {
			fieldNames2[i] = "field" + i;
			fieldTypes2[i] = String.class;
		}
		fieldNames2[20] = "fieldClass6";
		fieldTypes2[20] = Class.forName("Test6");

		className = "Test5";

		Class c5 = jau.createClass(className, fieldNames, fieldTypes);
		

		ODB odb = null;
		try {
			odb = open();
			Objects objects = odb.query(c5).objects();
			odb.store(c5.newInstance());
			odb.close();
			closeServer();
			assertEquals(100, objects.size());
			testOk("step3");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			odb.close();
			closeServer();
			System.exit(1);
		}
	}
	public static void main(String[] args) throws Exception {
		RefactoringTest3 tf = new RefactoringTest3();
		tf.execute(args);
	}

}
