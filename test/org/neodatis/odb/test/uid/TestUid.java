package org.neodatis.odb.test.uid;

import org.junit.Test;
import org.neodatis.odb.test.ODBTest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestUid extends ODBTest {

    @Test
	public void test1(){
		UUID uuid = UUID.randomUUID();
		Map<String, UUID> m = new HashMap<String, UUID>();
		int size = 10000;
		for(int i=0;i<size;i++){
			uuid = UUID.randomUUID();
			assertFalse(m.containsKey(uuid.toString()));
			m.put(uuid.toString(), uuid);
		}
	}
    @Test
	public void test2(){
		UUID uuid = UUID.randomUUID();
		UUID uuid2 = UUID.fromString(uuid.toString());
		
		assertEquals(uuid, uuid2);
	}
    @Test
	public void test3(){
		UUID uuid = UUID.randomUUID();
		long l = uuid.getLeastSignificantBits();
		long m = uuid.getMostSignificantBits();
		
		UUID uuid2 = new UUID(m, l);
		assertEquals(uuid, uuid2);
		assertEquals(uuid.toString(), uuid2.toString());
	}
}
