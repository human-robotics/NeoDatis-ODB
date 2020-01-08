package org.neodatis.odb.test.performance;

import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.tool.MemoryMonitor;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.lang.ref.WeakReference;
import java.util.*;

public class TestWeakReference extends ODBTest {

	public void test1() {
		int size = 20000;
		Map map = new WeakHashMap();
		List l = new ArrayList();
		for (int i = 0; i < size; i++) {
			Object o = getSimpleObjectInstance(i);
			l.add(o);
			if (i % 5000 == 0) {
				println("i=" + i);
			}
			map.put(o, new WeakReference(o));
		}
		println("Test 1 ok");
		println("Map size " + map.size());
	}

	public void test1WithoutWeak() {
		int size = 4000;
		Map map = new OdbHashMap();
		for (int i = 0; i < size; i++) {
			Object o = getSimpleObjectInstance(i);
			if (i % 500 == 0) {
				MemoryMonitor.displayCurrentMemory("" + i, true);
			}
			map.put(o, o);
		}
		println("Test 1 ok");
		println("Map size " + map.size());
	}

	public void test2() {
		int size = 2000;
		Map map = new WeakHashMap();
		for (int i = 0; i < size; i++) {
			Object o = getSimpleObjectInstance(i);
			if (i % 500 == 0) {
				println("i=" + i);
			}
			map.put(new Long(i), new WeakReference(o));
		}
		println("Test 2 ok");
		println("Map size " + map.size());
	}

	private SimpleObject getSimpleObjectInstance(int i) {
		SimpleObject so = new SimpleObject();
		so.setDate(new Date());
		so.setDuration(i);
		so.setName("Bonjour, comment allez vous?" + i);
		return so;
	}

	public static void main(String[] args) {
		TestWeakReference t = new TestWeakReference();
		t.test1();
	}

}
