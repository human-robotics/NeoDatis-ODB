/**
 * 
 */
package org.neodatis.odb.test.fromusers.francisco;

import org.neodatis.odb.*;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;

import java.util.*;

/**
 * @author olivier
 * 
 */
public class TestReconnect extends ODBTest {
	
	public void test0() {
		//NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);

		String baseName = getBaseName();
		ODB odb = open(baseName);

		Module module1 = buildModule("a1", "description 1111", "www.neodatis.org");
		Module module2 = buildModule("a2", "description 2222", "www.oracle.com");

		odb.store(module1);
		odb.store(module2);

		module1.setAuthor("new author of module 1");
		odb.store(module1);
		odb.close();

		 odb = open(baseName);
		// Check number of objects
		Objects modules = odb.query(Module.class).objects();
		Objects tags = odb.query(Tag.class).objects();
		Objects versions = odb.query(Version.class).objects();

		odb.close();

		assertEquals(2, modules.size());
		assertEquals(2, tags.size());
		assertEquals(2, versions.size());

	}
	/** check if calling twice the store duplicate the object
	 * 
	 */
	public void test01() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);

		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10009,config);
		server.startServer(true);
		ODB odb = server.openClient(baseName,config);
		
		Function f= new Function("function");

		odb.store(f);

		f.setName("new function");
		odb.store(f);
		odb.close();

		 odb = server.openClient(baseName,config);
		// Check number of objects
		Objects<Function> functions = odb.query(Function.class).objects();

		odb.close();
		server.close();

		assertEquals(1, functions.size());

	}
	
	
	/**
	 * this junits fails because of byte code instrumentation in
	 * ServerObjectWriter.java line 72 in public OID
	 * updateNonNativeObjectInfo(NonNativeObjectInfo nnoi, boolean forceUpdate)
	 */
	public void test1() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);

		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10008,config);
		server.startServer(true);
		ODB odb = server.openClient(baseName,config);

		Module module1 = buildModule("a1", "description 1111", "www.neodatis.org");
		Module module2 = buildModule("a2", "description 2222", "www.oracle.com");

		ObjectOid oid = odb.store(module1);
		println(oid);
		odb.store(module2);

		module1.setAuthor("new author of module 1");
		odb.store(module1);
		odb.close();

		// Check number of objects
		odb = server.openClient(baseName);
		Objects modules = odb.query(Module.class).objects();
		Objects tags = odb.query(Tag.class).objects();
		Objects versions = odb.query(Version.class).objects();

		odb.close();
		server.close();

		assertEquals(2, modules.size());
		assertEquals(2, tags.size());
		assertEquals(2, versions.size());

	}

	public void test2() throws Exception {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10009,config);
		server.startServer(true);
		
		ODB odb = server.openClient(baseName,config);

		Module module1 = buildModule("a1", "description 1111", "www.neodatis.org");
		Module module2 = buildModule("a2", "description 2222", "www.oracle.com");

		odb.store(module1);
		odb.store(module2);

		module1.setAuthor("new author of module 1");
		odb.store(module1);
		odb.commit();
		/*
		 * module1.setAuthor("new author of module 2"); odb.store(module1);
		 * odb.commit();
		 * 
		 * module1.setAuthor("new author of module 3"); odb.store(module1);
		 * odb.commit();
		 * 
		 * module1.setAuthor("new author of module 4"); odb.store(module1);
		 * odb.commit();
		 */

		Objects modules = odb.query(Module.class).objects();
		Objects tags = odb.query(Tag.class).objects();
		Objects versions = odb.query(Version.class).objects();

		odb.close();
		server.close();
		deleteBase(baseName);

		assertEquals(2, modules.size());
		assertEquals(2, tags.size());
		assertEquals(2, versions.size());

	}

	public void test3() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10010);
		server.startServer(true);
		ODB odb = server.openClient(baseName);

		Module module1 = buildModule("a1", "description 1111", "www.neodatis.org");
		Module module2 = buildModule("a2", "description 2222", "www.oracle.com");

		odb.store(module1);
		odb.store(module2);

		module1.setAuthor("new author of module 1");
		module1.addTopic(new Tag("new topic 1"));
		module1.addTopic(new Tag("new topic 2"));

		odb.store(module1);
		odb.commit();

		module1.setAuthor("new author of module 2");
		module1.addTopic(new Tag("new topic 3"));
		module1.addTopic(new Tag("new topic 4"));
		odb.store(module1);
		odb.commit();

		module1.setAuthor("new author of module 3");
		module1.addTopic(new Tag("new topic 5"));
		module1.addTopic(new Tag("new topic 6"));

		odb.store(module1);
		odb.commit();

		module1.setAuthor("new author of module 4");
		odb.store(module1);
		module1.addTopic(new Tag("new topic 7"));
		module1.addTopic(new Tag("new topic 8"));

		odb.commit();

		Objects modules = odb.query(Module.class).objects();
		Objects tags = odb.query(Tag.class).objects();
		Objects versions = odb.query(Version.class).objects();

		odb.close();
		server.close();
		deleteBase(baseName);

		assertEquals(2, modules.size());
		assertEquals(8, tags.size());
		assertEquals(2, versions.size());

	}

	public void test2Function() {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		
		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10011,config);
		server.startServer(true);
		
		ODB odb = server.openClient(baseName,config);
		Function f1 = new Function("f1");
		Function f2 = new Function("f2");
		odb.store(f1);
		odb.store(f2);

		odb.store(f1);
		odb.commit();

		Objects functions = odb.query(Function.class).objects();

		odb.close();
		server.close();
		deleteBase(baseName);

		assertEquals(2, functions.size());

	}

	public void test2FunctionLocal() {

		String baseName = getBaseName();
		ODB odb = NeoDatis.open(baseName);
		Function f1 = new Function("f1");
		Function f2 = new Function("f2");
		odb.store(f1);
		odb.store(f2);

		odb.store(f1);
		odb.commit();

		Objects functions = odb.query(Function.class).objects();

		odb.close();
		deleteBase(baseName);

		assertEquals(2, functions.size());

	}

	public ODB getClientServerOdb(ODBServer server, String baseName, boolean sameVm) {
		if (sameVm) {
			return server.openClient(baseName);
		}
		return NeoDatis.openClient("localhost", 10007, baseName);
	}

	public void test2SameVm() {
		internalTest(true);
	}

	public void test2ClientServer() {
		internalTest(false);
	}

	public void test2SameVm2() {
		internalTest2(true);
	}

	public void test2ClientServer2() {
		internalTest2(false);
	}

	private void internalTest(boolean sameVm) {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10007,config);
		if (!useSameVmOptimization || !sameVm) {
			server.startServer(true);
		}
		ODB odb = getClientServerOdb(server, baseName, sameVm);
		println(odb.getName());

		Module module1 = buildModule("a1", "description 1111", "www.neodatis.org");
		Module module2 = buildModule("a2", "description 2222", "www.oracle.com");

		odb.store(module1);
		odb.store(module2);

		odb.close();
		odb = server.openClient(baseName,config);
		println(odb.getName());

		module1.setAuthor("new author of module 1");
		odb.store(module1);
		odb.close();

		// Check number of objects
		odb = server.openClient(baseName);
		Objects modules = odb.query(Module.class).objects();
		Objects tags = odb.query(Tag.class).objects();
		Objects versions = odb.query(Version.class).objects();

		odb.close();
		server.close();
		deleteBase(baseName);

		assertEquals(2, modules.size());
		assertEquals(2, tags.size());
		assertEquals(2, versions.size());

	}

	private void internalTest2(boolean sameVm) {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10007,config);
		if (!useSameVmOptimization || !sameVm) {
			server.startServer(true);
		}
		ODB odb = getClientServerOdb(server, baseName, sameVm);
		int size = 100;
		List modules = new ArrayList();
		for (int i = 0; i < size; i++) {
			Module module = buildModule("author " + i, "description " + i, "www.neodatis.org" + i);
			odb.store(module);
			modules.add(module);
		}

		odb.close();
		odb = server.openClient(baseName);
		for (int i = 0; i < modules.size(); i++) {
			Module m = (Module) modules.get(i);
			m.setAuthor(m.getAuthor() + " updated - updated");
			odb.store(m);
		}
		odb.close();

		// Check number of objects
		odb = server.openClient(baseName);
		Objects storedModules = odb.query(Module.class).objects();
		Objects tags = odb.query(Tag.class).objects();
		Objects versions = odb.query(Version.class).objects();

		odb.close();
		server.close();
		deleteBase(baseName);
		println(storedModules.size() + " stored modules");
		assertEquals(size, storedModules.size());
		assertEquals(size, tags.size());
		assertEquals(size, versions.size());

	}

	public void test2SameVmWithList() {
		internTestWithList(true);
	}

	public void test2ClientServerWithList() {
		internTestWithList(false);
	}

	public void test2SameVmWithListAndUpdate() {
		internTestWithListAndUpdate(true);
	}

	public void test2ClientServerWithListAndUpdate() {
		internTestWithListAndUpdate(false);
	}

	public void test2SameVmWithListAndUpdateCommit() {
		internTestWithListAndUpdateCommit(true);
	}

	public void test2ClientServerWithListAndUpdateCommit() {
		internTestWithListAndUpdateCommit(false);
	}

	private void internTestWithList(boolean sameVm) {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		
		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10007,config);
		if (!useSameVmOptimization || !sameVm) {
			server.startServer(true);
		}
		ODB odb = getClientServerOdb(server, baseName, sameVm);

		ModuleWithList module1 = buildModuleWithList("a1", "description 1111", "www.neodatis.org");
		ModuleWithList module2 = buildModuleWithList("a2", "description 2222", "www.oracle.com");

		odb.store(module1);
		odb.store(module2);

		odb.close();
		odb = server.openClient(baseName);

		module1.setAuthor("new author of module 1");
		odb.store(module1);
		odb.close();

		// Check number of objects
		odb = server.openClient(baseName);
		Objects modules = odb.query(ModuleWithList.class).objects();
		Objects tags = odb.query(Tag.class).objects();
		Objects versions = odb.query(Version.class).objects();

		odb.close();
		server.close();
		deleteBase(baseName);

		assertEquals(2, modules.size());
		assertEquals(2, tags.size());
		assertEquals(2, versions.size());

	}

	private void internTestWithListAndUpdate(boolean sameVm) {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		
		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10007,config);
		if (!useSameVmOptimization || !sameVm) {
			server.startServer(true);
		}
		ODB odb = getClientServerOdb(server, baseName, sameVm);

		ModuleWithList module1 = buildModuleWithList("a1", "description 1111", "www.neodatis.org");
		ModuleWithList module2 = buildModuleWithList("a2", "description 2222", "www.oracle.com");

		odb.store(module1);
		odb.store(module2);

		odb.close();
		odb = server.openClient(baseName);

		module1.setAuthor("new author of module 1");

		module1.addTopic(new Tag("new topic one"));
		module1.addTopic(new Tag("new topic two"));

		odb.store(module1);

		odb.close();

		// Check number of objects
		odb = server.openClient(baseName);
		Objects modules = odb.query(ModuleWithList.class).objects();
		Objects tags = odb.query(Tag.class).objects();
		Objects versions = odb.query(Version.class).objects();
		Objects tags2 = odb.query(Tag.class, W.like("name", "new topic%")).objects();

		odb.close();
		server.close();
		deleteBase(baseName);

		assertEquals(2, modules.size());
		assertEquals(4, tags.size());
		assertEquals(2, tags2.size());
		assertEquals(2, versions.size());

	}

	private void internTestWithListAndUpdateCommit(boolean sameVm) {
		NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);
		
		String baseName = getBaseName();
		ODBServer server = NeoDatis.openServer(10007,config);
		if (!useSameVmOptimization ||  !sameVm) {
			server.startServer(true);
		}
		ODB odb = getClientServerOdb(server, baseName, sameVm);

		ModuleWithList module1 = buildModuleWithList("a1", "description 1111", "www.neodatis.org");
		ModuleWithList module2 = buildModuleWithList("a2", "description 2222", "www.oracle.com");

		odb.store(module1);
		odb.store(module2);

		odb.commit();

		module1.setAuthor("new author of module 1");

		module1.addTopic(new Tag("new topic one"));
		module1.addTopic(new Tag("new topic two"));

		odb.store(module1);

		odb.close();

		// Check number of objects
		odb = server.openClient(baseName);
		Objects modules = odb.query(ModuleWithList.class).objects();
		Objects tags = odb.query(Tag.class).objects();
		Objects versions = odb.query(Version.class).objects();
		Objects tags2 = odb.query(Tag.class, W.like("name", "new topic%")).objects();

		odb.close();
		server.close();
		deleteBase(baseName);

		assertEquals(2, modules.size());
		assertEquals(4, tags.size());
		assertEquals(2, tags2.size());
		assertEquals(2, versions.size());

	}

	private Module buildModule(String author, String description, String homePage) {
		Module module = new Module();

		module.setAuthor(author);
		module.setDateTime(new Date());
		module.setDescription(description);
		module.setExamplesUrl("exampleUrl");
		module.setHomePageUrl(homePage);
		module.setMavenArtifactUrl("maven url");
		module.setSourceCodeExamplesUrl("src");
		module.setSourceCodeUrl("src2");
		module.setTitle("My Title");

		Tag t1 = new Tag("tag1-" + author);
		Set set1 = new HashSet<Tag>();
		set1.add(t1);
		module.setTopics(set1);

		Version v1 = new Version("v1-" + author);
		Set set2 = new HashSet<Version>();
		set2.add(v1);
		module.setVersions(set2);

		return module;

	}

	private ModuleWithList buildModuleWithList(String author, String description, String homePage) {
		ModuleWithList module = new ModuleWithList();

		module.setAuthor(author);
		module.setDateTime(new Date());
		module.setDescription(description);
		module.setExamplesUrl("exampleUrl");
		module.setHomePageUrl(homePage);
		module.setMavenArtifactUrl("maven url");
		module.setSourceCodeExamplesUrl("src");
		module.setSourceCodeUrl("src2");
		module.setTitle("My Title");

		Tag t1 = new Tag("tag1-" + author);
		List<Tag> set1 = new ArrayList<Tag>();
		set1.add(t1);
		module.setTopics(set1);

		Version v1 = new Version("v1-" + author);
		List set2 = new ArrayList<Version>();
		set2.add(v1);
		module.setVersions(set2);

		return module;

	}
}
