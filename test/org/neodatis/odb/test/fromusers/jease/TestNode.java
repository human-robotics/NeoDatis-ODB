/**
 * 
 */
package org.neodatis.odb.test.fromusers.jease;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

/**
 * @author olivier
 * 
 */
public class TestNode extends ODBTest {

	public void test0() {
		String baseName = getBaseName();
		
		try{
			NeoDatisConfig config = NeoDatis.getConfig().setReconnectObjectsToSession(true);

			ODB odb = NeoDatis.open(baseName,config);

			Node root = new Node("root");
			Node child1 = new Node("c1");
			Node child2 = new Node("c2");

			child1.parent = root;
			child2.parent = root;
			root.children = new Node[] { child1, child2 };
			odb.store(root);
			odb.store(child1);
			odb.store(child2);
			odb.commit();

			root.children = new Node[] { child2, child1 };
			odb.store(root);
			odb.commit();

			odb.close();
			
			odb = NeoDatis.open(baseName);
			Objects<Node> nodes = odb.query(Node.class, W.equal("name", "root")).objects();
			assertEquals(1, nodes.size());
			Node n = nodes.first();
			assertEquals(2, n.children.length);

			nodes = odb.query(Node.class).objects();
			assertEquals(3, nodes.size());
			odb.close();
			
		}finally{
		}
		
		
	}
	public void test1() {
		String baseName = getBaseName();

		ODB odb = NeoDatis.open(baseName);

		Node root = new Node("root");
		Node child1 = new Node("c1");
		Node child2 = new Node("c2");

		child1.parent = root;
		child2.parent = root;
		root.children = new Node[] { child1, child2 };
		odb.store(root);
		odb.store(child1);
		odb.store(child2);
		odb.commit();

		root.children = new Node[] { child2, child1 };
		odb.store(root);
		odb.store(child1);
		odb.store(child2);
		odb.commit();

		odb.close();
		
		odb = NeoDatis.open(baseName);
		Objects<Node> nodes = odb.query(Node.class, W.equal("name", "root")).objects();
		assertEquals(1, nodes.size());
		Node n = nodes.first();
		assertEquals(2, n.children.length);

		nodes = odb.query(Node.class).objects();
		assertEquals(3, nodes.size());
		odb.close();
		
		
	}
	
	public void test12() {
		String baseName = getBaseName();

		ODB odb = NeoDatis.open(baseName);

		Node root = new Node("root");
		Node child1 = new Node("c1");
		Node child2 = new Node("c2");

		child1.parent = root;
		child2.parent = root;
		root.children = new Node[] { child1, child2 };
		odb.store(root);
		odb.commit();

		root.children = new Node[] { child2, child1 };
		odb.store(root);
		odb.commit();

		odb.close();
		
		odb = NeoDatis.open(baseName);
		Objects<Node> nodes = odb.query(Node.class, W.equal("name", "root")).objects();
		odb.close();
		
		assertEquals(1, nodes.size());
		
		Node n = nodes.first();
		assertEquals(2, n.children.length);
		
		assertEquals("c2", n.children[0].name);
		assertEquals("c1", n.children[1].name);
		
		
	}
	public void test2() {
		String baseName = getBaseName();

		ODB odb = NeoDatis.open(baseName);

		Node root = new Node("root");
		Node child1 = new Node("c1");
		Node child2 = new Node("c2");

		child1.parent = root;
		child2.parent = root;
		root.children = new Node[] { child1, child2 };
		odb.store(root);
		odb.store(child1);
		odb.store(child2);
		odb.close();

		odb = NeoDatis.open(baseName);
		root.children = new Node[] { child2, child1 };
		odb.store(root);
		odb.store(child1);
		odb.store(child2);

		odb.close();
		
		odb = NeoDatis.open(baseName);
		
		Objects<Node> nodes = odb.query(Node.class).objects();
		odb.close();
		assertEquals(6, nodes.size());
		
		
	}


}