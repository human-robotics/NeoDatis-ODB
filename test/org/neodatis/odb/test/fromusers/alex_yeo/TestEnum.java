package org.neodatis.odb.test.fromusers.alex_yeo;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

public class TestEnum extends ODBTest {

	
	public void testClassWithEnumInInterface() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		NeoDatis.getGlobalConfig().setCheckMetaModelCompatibility(false);
		String baseName = getBaseName();
		ODB odb = open(baseName);

		MyClass myclass = new MyClass("name", new McUsbDaqDevice());
		odb.store(myclass);
		odb.close();
		

		odb = open(baseName);
		Objects<MyClass> cc = odb.getObjects(MyClass.class);
		odb.close();
		MyClass my = cc.first();
		assertEquals("name", cc.first().getName());
		assertEquals(myclass.getDevice().getDescriptor(), cc.first().getDevice().getDescriptor());
	}
	
	public void testClassWithEnumInInterface2() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		McUsbDaqDevice d = new McUsbDaqDevice();
		d.setInput(McUsbDaqInput.DIO0);
		d.setRangePolarity(VoltageRangePolarity.BIPOLAR10);
		MyClass myclass = new MyClass("name", d);
		odb.store(myclass);
		odb.close();
		

		odb = open(baseName);
		Objects<MyClass> cc = odb.getObjects(MyClass.class);
		odb.close();
		MyClass my = cc.first();
		assertEquals("name", cc.first().getName());
		assertEquals(myclass.getDevice().getDescriptor(), cc.first().getDevice().getDescriptor());
		
		McUsbDaqDevice d2 = (McUsbDaqDevice) cc.first().getDevice();
		assertEquals(McUsbDaqInput.DIO0, d2.getInput());
		assertEquals(VoltageRangePolarity.BIPOLAR10, d2.getRangePolarity());
		
		
	}

}
