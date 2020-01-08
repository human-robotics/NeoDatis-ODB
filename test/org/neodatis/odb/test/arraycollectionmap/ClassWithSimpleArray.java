package org.neodatis.odb.test.arraycollectionmap;

public class ClassWithSimpleArray {
	String name;
	int [] ints;
	int v;
	public ClassWithSimpleArray(String name, int i1, int i2, int v) {
		super();
		this.name = name;
		this.ints = new int[]{i1,i2};
		this.v = v;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getInts() {
		return ints;
	}
	public void setInts(int[] ints) {
		this.ints = ints;
	}
	
	
	

}