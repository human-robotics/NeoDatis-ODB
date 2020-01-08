/**
 * 
 */
package org.neodatis.odb.test.buffer;

import org.junit.Test;
import org.neodatis.odb.core.layers.layer3.buffer.MultiBufferedFileIO;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier to test performance of java.nio.ByteBuffer against byte[]
 * 
 */
public class TestMultiBuffer extends ODBTest {
    @Test
	public void test1() {

		String baseName = getBaseName();

		MultiBufferedFileIO io = new MultiBufferedFileIO(5, "b1", baseName, true, 2048,false);
		byte[] b = null;// io.readBytes(9);
		int size = 9;
		for (int i = 0; i < size; i++) {
			io.writeBytes(String.valueOf("neodatis odb " + i).getBytes());
		}
		io.flushAllBuffers();
		io.close();
		io = new MultiBufferedFileIO(5, "b1", baseName, true, 2048,false);
		for (int i = 0; i < size; i++) {
			b = io.readBytes(14);
			System.out.println(new String(b));
			assertEquals("neodatis odb " + i, new String(b));
		}
		io.close();
		io.delete();

	}
    @Test
	public void test2() {

		String baseName = getBaseName();

		MultiBufferedFileIO io = new MultiBufferedFileIO(5, "b1", baseName, true, 2048,false);
		byte[] b = new byte[3000];
		for(int i=0;i<3000;i++){
			b[i] = (byte) i;
		}
		
		int size = 100;
		for (int i = 0; i < size; i++) {
			io.writeBytes(b);
		}
		io.flushAllBuffers();
		io.close();
		io = new MultiBufferedFileIO(5, "b1", baseName, true, 2048,false);
		for (int i = 0; i < size; i++) {
			b = io.readBytes(3000);
			System.out.println(new String(b));
			assertEquals("neodatis odb " + i, new String(b));
		}
		io.close();
		io.delete();

	}
}
