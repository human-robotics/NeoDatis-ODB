package org.neodatis.odb.test.refactoring.manual;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;

public class TestRefactoring1 extends ODBTest {

	public void test1() throws Exception {
		ODB odb = open("refac");
		odb.close();
		Item item = new Item("oli");
		//item.date = new Date();
		//item.s1 = "Olivier";
		item.s2 = "Pierre";
		//deleteBase("refac");
		odb = open("refac");
		Objects<Item> items = odb.query(Item.class).objects();
		System.out.println(items.size());

		odb.store(item);

		odb.close();
		System.out.println("dOne");
	}
	
	public void t1estCSMode() throws Exception {
		String baseName = "manual-refactoring.neodatis";
		int port = 10000;
		ODBServer server = NeoDatis.openServer(port);
		server.startServer(true);
		server.addBase(baseName, DIRECTORY+baseName);
		ODB odb = NeoDatis.openClient("localhost", port, baseName);
		odb.close();
		Item item = new Item("oli");
		//item.date = new Date();
		//item.s1 = "Olivier";
		item.s2 = "Pierre";
		//deleteBase("refac");
		odb = NeoDatis.openClient("localhost", port, baseName);
		Objects<Item> items = odb.query(Item.class).objects();
		System.out.println(items.size());

		odb.store(item);

		odb.close();
		System.out.println("dOne");
		server.close();
	}
	public static void main(String[] args) throws Exception {
		new TestRefactoring1().t1estCSMode();
	}
}
