package org.neodatis.odb.test.inmemory;

import org.neodatis.odb.*;
import org.neodatis.odb.core.layers.layer4.plugin.memory.InMemoryStorageEngine;
import org.neodatis.odb.core.layers.layer4.plugin.memory.InMemoryStorageEngineFast;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

public class TestInMemory extends ODBTest {
	private Class defaultLayer4Class;

	
	public void testObjects1() {

		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);
		
		
		String baseName = getBaseName();
		ODB odb = open(baseName,config);
		Function function = new Function("function");
		ObjectOid oid = odb.store(function);

		Function f2 = (Function) odb.getObjectFromId(oid);

		assertEquals(function.getName(), f2.getName());

	}


	public void testObjects2() {

		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);
		
		String baseName = getBaseName();
		ODB odb = open(baseName,config);
		int size = 1000;
		for(int i=0;i<size;i++){
			odb.store(new Function("function "+i));
		}

		Objects<Function> functions = odb.query(Function.class, W.equal("name", "function 550")).objects(); 

		assertEquals(1, functions.size());
		assertEquals(size, odb.query(Function.class).objects().size());

	}
	
	public void testObjects10000() {

		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngineFast.class);
		long start = System.currentTimeMillis();
		String baseName = getBaseName();
		ODB odb = open(baseName,config);
		int size = 10000;
		for(int i=0;i<size;i++){
			odb.store(new Function("function "+i));
		}

		Objects<Function> functions = odb.query(Function.class, W.equal("name", "function 550")).objects(); 
		long end = System.currentTimeMillis();
		assertEquals(1, functions.size());
		assertEquals(size, odb.query(Function.class).objects().size());
		println("time is "+ (end-start));

	}

}
