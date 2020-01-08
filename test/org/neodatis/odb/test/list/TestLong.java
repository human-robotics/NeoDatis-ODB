package org.neodatis.odb.test.list;

import junit.framework.TestCase;
import org.neodatis.odb.tool.MemoryMonitor;
import org.neodatis.tool.wrappers.OdbTime;

import java.util.*;

public class TestLong extends TestCase {

	/** Just check ordering of LinkedHashMap */
	public void testOrderedMap() {
		Map m = new LinkedHashMap();

		for (int i = 0; i < 10; i++) {
			m.put("key" + i, "value" + i);
		}

		Iterator iterator = m.keySet().iterator();
		int j = 0;
		while (iterator.hasNext()) {
			assertEquals("key" + j, iterator.next());
			j++;
		}
	}

	public static void main(String[] args) {
		MemoryMonitor.displayCurrentMemory("start", true);
		int size = 3400000;
		long start = OdbTime.getCurrentTimeInMs();
		List l = new ArrayList();
		for (int i = 0; i < size; i++) {
			l.add(new MyInt(i));
		}
		long end = OdbTime.getCurrentTimeInMs();
		MemoryMonitor.displayCurrentMemory("end " + (end - start) + "ms", true);

	}

}
