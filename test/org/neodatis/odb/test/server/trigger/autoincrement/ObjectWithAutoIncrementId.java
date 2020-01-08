package org.neodatis.odb.test.server.trigger.autoincrement;

public class ObjectWithAutoIncrementId {
	private String name;
	private long id;

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

}
