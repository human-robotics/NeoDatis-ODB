package org.neodatis.odb.test.explorer;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.test.vo.attribute.AllAttributeClass;
import org.neodatis.tool.IOUtil;
import org.neodatis.tool.wrappers.OdbTime;

import java.math.BigDecimal;
import java.util.Date;

public class CreateDataToTestOdbExplorer {

	/**
	 * bug found by Julio Jimenez Borreguero When there exist an index on a
	 * numeric field, the criteria query is constructed with a value of type
	 * String instead of numeric
	 */
	public void test1() {
		IOUtil.deleteFile("base1.neodatis");
		ODB odb = NeoDatis.open("base1.neodatis");
		String[] fields = { "int1" };
		odb.getClassRepresentation(AllAttributeClass.class).addUniqueIndexOn("index1", fields, true);
		long start = OdbTime.getCurrentTimeInMs();
		int size = 50;
		for (int i = 0; i < size; i++) {
			AllAttributeClass testClass = new AllAttributeClass();
			testClass.setBigDecimal1(new BigDecimal(i));
			testClass.setBoolean1(i % 3 == 0);
			testClass.setChar1((char) (i % 5));
			testClass.setDate1(new Date(start + i));
			testClass.setDouble1(new Double(((double) (i % 10)) / size));
			testClass.setInt1(size - i);
			testClass.setString1("test class " + i);

			odb.store(testClass);
			// println(testClass.getDouble1() + " | " + testClass.getString1() +
			// " | " + testClass.getInt1());
		}
		odb.close();
	}

	public static void main(String[] args) throws Exception {
		new CreateDataToTestOdbExplorer().test1();
	}
}
