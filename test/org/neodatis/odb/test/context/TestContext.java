/**
 * 
 */
package org.neodatis.odb.test.context;

import org.junit.Test;
import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisObject;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 * 
 */
public class TestContext extends ODBTest {

    @Test
	public void testAutomaticReconnect1() {

		NeoDatis.getGlobalConfig().setReconnectObjectsToSession(true);
		String baseName = getBaseName();

		ODB odb = open(baseName, NeoDatis.getConfig().setReconnectObjectsToSession(true));

		// Create a computer with some components
		Computer cpu1 = new Computer("MacBookPro", 1);
		cpu1.addComponent(new Component("HD1", "Harddrive 500GB 7200rpm"));
		cpu1.addComponent(new Component("HD2", "Harddrive 2TB 10000rpm"));
		cpu1.addComponent(new Component("GC1", "GraphicCard with internal Processor"));
		cpu1.addMonitor(new Monitor("1920*1200", "Samsung"));
		
		// Store the computer into NeoDatis
		odb.store(cpu1);
		odb.close();

		// re open database and check if NeoDatis will understand that the
		// object is the same
		odb = open(baseName, NeoDatis.getConfig().setReconnectObjectsToSession(true));
		
		// Use the computer without reloading it
		cpu1.setName("Mac_Book_Pro");
		odb.store(cpu1);
		odb.close();

		// Then load computers to check if the computer has been updateds
		odb = open(baseName);
		Objects<Computer> computers = odb.query(Computer.class).objects();
		Objects<Component> components = odb.query(Component.class).objects();
		Objects<Monitor> monitors = odb.query(Monitor.class).objects();
		odb.close();
		
		println("Number of computers : "+  computers.size());
		println(computers.first().getName());

		assertEquals(1, computers.size());
		assertEquals(3, components.size());
		assertEquals(1, monitors.size());
		
	}

    @Test
	public void testAutomaticReconnectForDelete() {

		String baseName = getBaseName();

		ODB odb = open(baseName, NeoDatis.getConfig().setReconnectObjectsToSession(true));

		Computer cpu1 = new Computer("MacBookPro", 1);
		cpu1.addComponent(new Component("HD1", "Harddrive 500GB 7200rpm"));
		cpu1.addComponent(new Component("HD2", "Harddrive 2TB 10000rpm"));
		cpu1.addComponent(new Component("GC1", "GraphicCard with internal Processor"));
		cpu1.addMonitor(new Monitor("1920*1200", "Samsung"));
		odb.store(cpu1);
		odb.close();

		// re open database and check if NeoDatis will understand that the
		// object is the same
		odb = open(baseName, NeoDatis.getConfig().setReconnectObjectsToSession(true));
		odb.delete(cpu1);
		odb.close();

		odb = open(baseName);
		Objects<Computer> computers = odb.query(Computer.class).objects();
		Objects<Component> components = odb.query(Component.class).objects();
		Objects<Monitor> monitors = odb.query(Monitor.class).objects();
		odb.close();

		assertEquals(0, computers.size());
		assertEquals(3, components.size());
		assertEquals(1, monitors.size());

	}

    @Test
	public void testAutomaticReconnectForDeleteCascadeManual() {

		String baseName = getBaseName();

		ODB odb = open(baseName, NeoDatis.getConfig().setReconnectObjectsToSession(true));

		Computer cpu1 = new Computer("MacBookPro", 1);
		cpu1.addComponent(new Component("HD1", "Harddrive 500GB 7200rpm"));
		cpu1.addComponent(new Component("HD2", "Harddrive 2TB 10000rpm"));
		cpu1.addComponent(new Component("GC1", "GraphicCard with internal Processor"));
		cpu1.addMonitor(new Monitor("1920*1200", "Samsung"));
		odb.store(cpu1);
		odb.close();
		
		cpu1.setName("test");
		assertTrue(cpu1.getNeoDatisContext().hasChanged());
		
		NeoDatisObject no = cpu1.getMonitors().get(0);
		assertNotNull(no.getNeoDatisContext().getOid());

		// re open database and check if NeoDatis will understand that the
		// object is the same
		odb = open(baseName, NeoDatis.getConfig().setReconnectObjectsToSession(true));
		odb.delete(cpu1);
		odb.delete(cpu1.getComponents().get(0));
		odb.delete(cpu1.getComponents().get(1));
		odb.delete(cpu1.getComponents().get(2));
		odb.delete(cpu1.getMonitors().get(0));
		odb.close();

		odb = open(baseName, NeoDatis.getConfig().setReconnectObjectsToSession(true));
		Objects<Computer> computers = odb.query(Computer.class).objects();
		Objects<Component> components = odb.query(Component.class).objects();
		Objects<Monitor> monitors = odb.query(Monitor.class).objects();
		odb.close();

		assertEquals(0, computers.size());
		assertEquals(0, components.size());
		assertEquals(0, monitors.size());

	}
}
