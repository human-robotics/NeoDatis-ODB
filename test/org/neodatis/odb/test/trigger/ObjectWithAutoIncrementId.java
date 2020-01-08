package org.neodatis.odb.test.trigger;

public class ObjectWithAutoIncrementId {
	private String name;
	private long id;
	private ObjectWithAutoIncrementId o;

	public ObjectWithAutoIncrementId(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ObjectWithAutoIncrementId getO() {
		return o;
	}

	public void setO(ObjectWithAutoIncrementId o) {
		this.o = o;
	}

}
