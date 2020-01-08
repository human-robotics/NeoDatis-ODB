package org.neodatis.odb.test.arraycollectionmap;

import org.junit.Test;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Vector;

public class TestMapContainingCollection extends ODBTest {
	public TestMapContainingCollection() throws Exception {
		super();
		setUp();
	}

    @Test
	public void test1() throws Exception {
		ODB odb = null;

		odb = open(getBaseName());
		MyMapObject o = new MyMapObject("test");
		Collection c = new Vector();
		c.add("ola");
		o.getMap().put("c", c);
		odb.store(o);
		odb.close();

		odb = open(getBaseName());
		Objects os = odb.query(MyMapObject.class).objects();
		MyMapObject mmo = (MyMapObject) os.first();
		odb.close();
		assertEquals(o.getName(), mmo.getName());
		assertEquals(o.getMap().size(), mmo.getMap().size());
		assertEquals(o.getMap().get("c"), mmo.getMap().get("c"));
	}

    @Test
	public void test2() throws Exception {

		ODB odb = null;

		odb = open(getBaseName());
		MyMapObject o = new MyMapObject("test");
		Collection c = new Vector();
		c.add(o);
		o.getMap().put("c", c);
		odb.store(o);
		odb.close();

		odb = open(getBaseName());
		Objects os = odb.getObjects(MyMapObject.class);
		MyMapObject mmo = (MyMapObject) os.first();
		odb.close();
		assertEquals(o.getName(), mmo.getName());
		assertEquals(o.getMap().size(), mmo.getMap().size());
		Collection c1 = (Collection) o.getMap().get("c");
		Collection c2 = (Collection) mmo.getMap().get("c");

		assertEquals(c1.size(), c2.size());
		assertEquals(mmo, c2.iterator().next());
	}
    @Test
	public void test1Function() throws Exception {

		ODB odb = null;

		odb = open(getBaseName());
		odb.store(new Function("f1"));
		odb.close();
		odb = open(getBaseName());
		Objects<Function> ffs = odb.query(Function.class).objects();
		Function f = ffs.first();
		odb.close();
		assertEquals("f1", f.getName());

	}
    @Test
	public void test3() throws Exception {

		// LogUtil.objectReaderOn(true);

		ODB odb = null;
		String baseName = getBaseName();
		System.out.println(baseName);

		odb = open(baseName);
		MyMapObject o = new MyMapObject("test");
		Collection c = new Vector();
		c.add(o);
		Function f1 = new Function("function1");

		o.getMap().put("a", c);
		int size = 1;
		for (int i = 0; i < size; i++) {
			//o.getMap().put("A" + new Integer(i), new Function("function" + i));
			o.getMap().put("A" + new Integer(i), f1);
		}

		o.getMap().put("c", f1);

		println("RealMap" + o.getMap());

		odb.store(o);
		Objects<Function> ffs = odb.query(Function.class).objects();
		System.out.println("functions=" + ffs);
		odb.close();
		
		File f = new File(DIRECTORY+ baseName);
		System.out.println(f.length());
		RandomAccessFile raf = new RandomAccessFile(f,"rw");
		raf.seek(f.length()-1);
		raf.read();
		raf.getChannel().force(true);
		raf.close();
		//Thread.sleep(500);
		
		odb = open(baseName);
		Objects os = odb.query(MyMapObject.class).objects();
		MyMapObject mmo = (MyMapObject) os.first();
		ffs = odb.query(Function.class).objects();
		System.out.println(ffs);
		
		assertEquals(o.getName(), mmo.getName());

		assertEquals(size + 2, mmo.getMap().size());
		assertEquals(mmo, ((Collection) mmo.getMap().get("a")).iterator().next());
		Object oo = mmo.getMap().get("c");
		Function f2 = ffs.first();
		if(f2.getName()==null){
			System.out.println("\n\nFirst was null \n\n");
			//odb.close();
			//odb = open(baseName);
			ffs = odb.query(Function.class).objects();
			System.out.println(ffs);
			//odb.close();
			f2 = ffs.first();
			
			
		}else{
			System.out.println("f2 is not null! " + f2.getName());
		}
		odb.close();
		assertTrue(f2.toString().startsWith("function"));

	}
    @Test
	public void test4() throws Exception {

		// LogUtil.objectReaderOn(true);

		ODB odb = null;
		String baseName = getBaseName();
		System.out.println(baseName);

		odb = open(baseName);

		Function f1 = new Function("function1");
		Profile p1 = new Profile("profile 2", f1);
		p1.addFunction(f1);

		odb.store(p1);
		odb.close();
		odb = open(baseName);
		Objects<Profile> os = odb.query(Profile.class).objects();
		Profile p = os.first();
		odb.close();
		assertEquals(p.getName(), "profile 2");
		assertEquals(p.getFunctions().get(0).toString(), "function1");
	}

	public static void main(String[] args) throws Exception {
		int size = 1000;

		TestMapContainingCollection t = new TestMapContainingCollection();
		for (int i = 0; i < size; i++) {
			t.resetBaseName();
			t.test3();
			System.out.println("i=" + i);
		}
	}

}

class MyMapObject {
	private String name;
	private OdbHashMap<Object, Object> map;

	public MyMapObject(String name) {
		this.name = name;
		this.map = new OdbHashMap<Object, Object>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OdbHashMap<Object, Object> getMap() {
		return map;
	}

	public void setMap(OdbHashMap<Object, Object> map) {
		this.map = map;
	}

}