/**
 * 
 */
package org.neodatis.odb.test.buffer;

import org.junit.Test;
import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.core.layers.layer3.*;
import org.neodatis.odb.test.ODBTest;

import java.nio.ByteBuffer;

/**
 * @author olivier to test performance of java.nio.ByteBuffer against byte[]
 * 
 */
public class ByteBufferTest extends ODBTest {
    @Test
	public void test1() {
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		byte b1 = 1;
		int i1 = 10;
		buffer.put(b1);
		buffer.putInt(i1);

		buffer.rewind();

		assertEquals(b1, buffer.get());
		assertEquals(i1, buffer.getInt());
	}
    @Test
	public void test2Perf() {
		int size = 1000000;
		DataConverter byteArrayConverter = new DataConverterImpl(false, "UTF-8",NeoDatis.getConfig());

		long startBuffer = System.currentTimeMillis();
		ByteBuffer buffer = ByteBuffer.allocate(size * 8);
		for (int i = 0; i < size; i++) {
			long l = i;
			buffer.putLong(l);
		}
		buffer.rewind();
		for (int i = 0; i < size; i++) {
			long l = i;
			assertEquals(l, buffer.getLong());
		}
		long endBuffer = System.currentTimeMillis();

		long startArray = System.currentTimeMillis();
		Bytes bytes = BytesFactory.getBytes();
		for (int i = 0; i < size; i++) {
			long l = i;
			byteArrayConverter.longToByteArray(l,bytes,i*8,"test");
		}
		ReadSize rs = new ReadSize();
		for (int i = 0; i < size; i++) {
			long l = i;
			long l2 = byteArrayConverter.byteArrayToLong(bytes, i * 8,rs,"test");

			assertEquals(l, l2);
		}
		long endArray = System.currentTimeMillis();

		println("time with ByteBuffer=" + (endBuffer - startBuffer));
		println("time with byte array=" + (endArray - startArray));

	}

}
