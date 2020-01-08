package org.neodatis.odb.test.compressor;

import org.junit.Test;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.tool.compressor.DefaultCompressor;

public class TestCompressor extends ODBTest{
    @Test
	public void test1() throws Exception{
		
		byte[] bytes = {'1','2','3'};
		DefaultCompressor compressor = new DefaultCompressor();
		byte[] bb = compressor.compress(bytes);
		
		byte[] r = compressor.uncompress(bb);
		
		assertEquals(3, r.length);
		
		for(int i=0;i<bytes.length;i++){
			assertEquals(bytes[i], r[i]);
		}
	}
    @Test
	public void test2() throws Exception{
		int size = 10000;
		byte[] bytes = new byte[size];
		for(int i=0;i<size;i++){
			bytes[i] = (byte) ((byte) Math.random()*256 - 128);
		}
		DefaultCompressor compressor = new DefaultCompressor();
		byte[] bb = compressor.compress(bytes);
		
		byte[] r = compressor.uncompress(bb);
		
		assertEquals(size, r.length);
		
		for(int i=0;i<bytes.length;i++){
			assertEquals(bytes[i], r[i]);
		}
		
	}

    @Test
	public void test3StoreWithFunction(){
		String baseName = getBaseName();

		ODB odb = null;

		try {
			odb = open(baseName);

			odb.store(new Function("f1"));
			odb.store(new Function("f2"));
			odb.store(new Function("f3"));
			odb.close();
			
			odb = open(baseName);
			Objects<Function> functions = odb.query(Function.class).orderByAsc("name").objects();
			assertEquals(3, functions.size());
			
			assertEquals("f1", functions.first().getName());
			
			
		} finally {
			if (odb != null&&!odb.isClosed()) {
				odb.close();
			}
		}


	}

}
