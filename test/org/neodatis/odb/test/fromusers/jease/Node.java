/**
 * 
 */
package org.neodatis.odb.test.fromusers.jease;

/**
 * @author olivier
 * 
 */
public class Node {
	String name;
	Node parent;
	Node[] children;

	public Node(String name){
		this.name = name;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("node ").append(name).append("[");
		
		for(Node n:children){
			buffer.append(n.name).append(",");
		}
		buffer.append("]");
		return buffer.toString();
	}
}
