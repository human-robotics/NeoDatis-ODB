package org.neodatis.odb.test.server.trigger.oid;

public class ObjectWithAutoIncrementId {
	private String name;
	private String id;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ObjectWithAutoIncrementId getO() {
		return o;
	}

	public void setO(ObjectWithAutoIncrementId o) {
		this.o = o;
	}

}
