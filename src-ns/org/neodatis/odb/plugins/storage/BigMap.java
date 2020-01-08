package org.neodatis.odb.plugins.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class BigMap {

	public static void main(String[] args) {
		String file = "test.dat";
		System.out.println("Data file deleted ? " + new File(file).delete());
		Map<UUID, Long> m = new HashMap<UUID, Long>();

		int size = 1000000;

		long t0 = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			m.put(UUID.randomUUID(), new Long(i));
		}
		System.out.println(".");
		long t1 = System.currentTimeMillis();
		storeMap(file, m);
		long t2 = System.currentTimeMillis();
		Map<UUID, Long> m2 = readMap(file);
		long t3= System.currentTimeMillis();
		System.out.println(".");
		long tCreateMap = t1-t0;
		long tSaveFile = t2-t1;
		long tLoadFile = t3-t2;
		
		System.out.println(" create map=" + tCreateMap + "    StoreFile="+tSaveFile + "     ReadMap="+tLoadFile);
		
		

	}
	
	public static void main2(String[] args) {
		Map<UUID, Long> m = new HashMap<UUID, Long>();

		int size = 100000;

		long t0 = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			m.put(UUID.randomUUID(), new Long(i));
		}
		System.out.println(".");
		long t1 = System.currentTimeMillis();
		storeMap("test.dat", m);
		long t2 = System.currentTimeMillis();
		Map<UUID, Long> m2 = (Map) retrieve("test.dat");
		long t3= System.currentTimeMillis();
		System.out.println(".");
		long tCreateMap = t1-t0;
		long tSaveFile = t2-t1;
		long tLoadFile = t3-t2;
		
		System.out.println(" create map=" + tCreateMap + "    StoreFile="+tSaveFile + "     ReadMap="+tLoadFile);
		
		

	}

	public static void store(String fileName, Object o) {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(o);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void storeMap(String fileName, Map<UUID, Long> m) {
		try {
			int step = 10;
			RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
			Iterator<UUID> keys = m.keySet().iterator();
			int totalSize = m.size() * 3 * 8;
			ByteBuffer buffer = ByteBuffer.allocate(totalSize);
			System.out.println("Writing map with " + m.size() + " elements");
			while(keys.hasNext()){
				UUID key = keys.next();
				Long value = m.get(key);
				buffer.putLong(key.getLeastSignificantBits());
				buffer.putLong(key.getMostSignificantBits());
				buffer.putLong(value.longValue());
			}
			
			raf.write(buffer.array());
			
			System.out.println("Final file size = " + raf.length() + "   , should be " + m.size() * 3 * 8);
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object retrieve(String fileName) {
		try {
			FileInputStream fin = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Map<UUID, Long> readMap(String fileName) {
		Map<UUID, Long> map = new HashMap<UUID, Long>();
		try {
			
			
			RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
			
			byte[] bytes = new byte[(int) raf.length()];
			raf.read(bytes);
			int nbObjects = (int) (raf.length() / (8*3));
			
			System.out.println("Reading map with " + nbObjects + " elements, file size=" + raf.length());
			
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			for(int i=0;i<nbObjects;i++){
				Long l1 = buffer.getLong();
				Long l2 = buffer.getLong();
				Long value = buffer.getLong();
				
				UUID uuid = new UUID(l2,l1);
				map.put(uuid,value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
