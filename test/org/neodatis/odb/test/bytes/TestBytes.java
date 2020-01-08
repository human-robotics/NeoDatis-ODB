/**
 * 
 */
package org.neodatis.odb.test.bytes;

import org.junit.Test;
import org.neodatis.odb.core.layers.layer3.Bytes;
import org.neodatis.odb.core.layers.layer3.BytesFactory;
import org.neodatis.odb.core.layers.layer3.Layer3Converter;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 * 
 */
public class TestBytes extends ODBTest {
	int SIZE = 10000000;

    @Test
	public void test1() {

		Bytes bytes = BytesFactory.getBytes();
		for (int i = 0; i < 500; i++) {
			bytes.set(i, (byte) i);
		}
		assertEquals(500, bytes.getRealSize());
		assertEquals(1, bytes.getNbBlocks());
	}
    @Test
	public void testInt() {

		Bytes bytes = BytesFactory.getBytes();
		byte[] bb = {1,2,3,4};
		bytes.set(Layer3Converter.DEFAULT_BYTES_SIZE-2, bb);
		assertEquals(Layer3Converter.DEFAULT_BYTES_SIZE+2, bytes.getRealSize());
	}
    @Test
	public void testInt2() {

		Bytes bytes = BytesFactory.getBytes();
		byte[] bb = {1,2,3,4};
		bytes.set(Layer3Converter.DEFAULT_BYTES_SIZE-1, bb);
		assertEquals(Layer3Converter.DEFAULT_BYTES_SIZE+3, bytes.getRealSize());
	}
    @Test
	public void testInt3() {

		Bytes bytes = BytesFactory.getBytes();
		byte[] bb = {1,2,3,4};
		bytes.set(Layer3Converter.DEFAULT_BYTES_SIZE, bb);
		assertEquals(Layer3Converter.DEFAULT_BYTES_SIZE+4, bytes.getRealSize());
	}

    @Test
	public void test2() {
		int size = SIZE;
		Bytes bytes = BytesFactory.getBytes();
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < size; i++) {
			bytes.set(i, (byte) i);
		}
		long end1 = System.currentTimeMillis();
		
		assertEquals(size, bytes.getRealSize());
		
		for(int i=0;i<size;i++){
			assertEquals((byte)i, bytes.get(i));	
		}
		long end2 = System.currentTimeMillis();
		System.out.println(String.format("Bytes  write=%d  and read=%d", (end1-start),(end2-end1)));
	}


    @Test
	public void testExtract() {
		int size = 10000;
		Bytes bytes = BytesFactory.getBytes();
		
		for (int i = 0; i < size; i++) {
			bytes.set(i, (byte) i);
		}
		for (int i = 0; i < 10; i++) {
			bytes.set(1020+i, (byte)i);
		}
		
		assertEquals(size, bytes.getRealSize());
		
		byte[] byteArray = bytes.extract(1020,10);
		for(int i=0;i<10;i++){
			assertEquals(i, byteArray[i]);	
		}
	}
    @Test
	public void testExtract2() {
		int size = 10000;
		Bytes bytes = BytesFactory.getBytes();
		
		for (int i = 0; i < size; i++) {
			bytes.set(i, (byte) i);
		}
		for (int i = 0; i < 10; i++) {
			bytes.set(1020+i, (byte)i);
		}
		
		assertEquals(size, bytes.getRealSize());
		
		byte[] byteArray = bytes.extract(1020,4);
		for(int i=0;i<4;i++){
			assertEquals(i, byteArray[i]);	
		}
	}
    @Test
	public void testExtract3() {
		int size = 10000;
		Bytes bytes = BytesFactory.getBytes();
		
		for (int i = 0; i < size; i++) {
			bytes.set(i, (byte) i);
		}
		for (int i = 0; i < 10; i++) {
			bytes.set(1020+i, (byte)i);
		}
		
		assertEquals(size, bytes.getRealSize());
		
		byte[] byteArray = bytes.getByteArray();
		assertEquals(size, byteArray.length);
		
		Bytes bytes2 = BytesFactory.getBytes(byteArray);
		assertEquals(size, bytes2.getRealSize());

		for (int i = 0; i < 10; i++) {
			assertEquals(i, bytes2.get(1020+i));
		}

	}
    @Test
	public void testAppend(){
		int blockSize = Layer3Converter.DEFAULT_BYTES_SIZE;
		byte[] bb = new byte[blockSize];
		bb[blockSize-1] = -1;
		bb[blockSize-2] = -2;
		bb[blockSize-3] = -3;
		Bytes bytes = BytesFactory.getBytes(bb);
		
		assertEquals(blockSize, bytes.getRealSize());
		byte[] bb2 = new byte[blockSize];
		bb2[0] = 1;
		bb2[1] = 2;
		bb2[2] = 3;
		int size = bytes.append(bb2);
		assertEquals(2*blockSize, bytes.getRealSize());
		assertEquals(blockSize, size);
		
		assertEquals(-3, bytes.get(blockSize-3));
		assertEquals(-2, bytes.get(blockSize-2));
		assertEquals(-1, bytes.get(blockSize-1));
		assertEquals(1, bytes.get(blockSize));
		assertEquals(2, bytes.get(blockSize+1));
		assertEquals(3, bytes.get(blockSize+2));
	}
    @Test
	public void testAppend2(){
		int blockSize = Layer3Converter.DEFAULT_BYTES_SIZE;
		Bytes bytes = BytesFactory.getBytes();
		
		assertEquals(0, bytes.getRealSize());
		byte[] bb2 = new byte[blockSize];
		bb2[0] = 1;
		bb2[1] = 2;
		bb2[2] = 3;
		bytes.append(bb2);
		assertEquals(blockSize, bytes.getRealSize());
		
		assertEquals(1, bytes.get(0));
		assertEquals(2, bytes.get(1));
		assertEquals(3, bytes.get(2));
	}


    @Test
	public void perfTest3() {
		int size = SIZE;
		
		long start = System.currentTimeMillis();
		byte[] bytes = new byte[size];
		
		for (int i = 0; i < size; i++) {
			bytes[i] = (byte) i;
		}
		long end1 = System.currentTimeMillis();
		
		assertEquals(size, bytes.length);
				
		for(int i=0;i<size;i++){
			assertEquals((byte)i, bytes[i]);	
		}
		long end2 = System.currentTimeMillis();
		System.out.println(String.format("byte[] write=%d  and read=%d", (end1-start),(end2-end1)));
	}
	
	
	public static void main(String[] args) {
		TestBytes t = new TestBytes();
		t.test2();
		t.perfTest3();
	}

}
