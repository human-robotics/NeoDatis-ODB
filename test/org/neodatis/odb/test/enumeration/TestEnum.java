package org.neodatis.odb.test.enumeration;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

import java.util.ArrayList;
import java.util.List;

public class TestEnum extends ODBTest {

	public void testStoreWithEnum() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		User user = new User(UserRole.ADMINISTRATOR, "admin",1);
		odb.store(user);
		odb.close();
		
		System.out.println(baseName);

		odb = open(baseName);
		Objects<User> users = odb.query(User.class).objects();
		odb.close();
		assertEquals(1, users.size());
	}

	public void testStoreWithNullEnum() {
		String baseName = getBaseName();
		System.out.println(baseName);
		ODB odb = open(baseName);

		User user = new User(null, "admin",1);
		odb.store(user);
		odb.close();

		odb = open(baseName);
		Objects<User> users = odb.query(User.class).objects();
		odb.close();
		assertEquals(1, users.size());
	}

	public void testStoreWithEnumAndCheckDuplicationOfEnum() {
		String baseName = getBaseName();
		System.out.println(baseName);
		ODB odb = open(baseName);

		User user = new User(UserRole.ADMINISTRATOR, "admin",1);
		odb.store(user);

		user = new User(UserRole.ADMINISTRATOR, "oper",2);
		odb.store(user);
		odb.close();

		odb = open(baseName);
		user = new User(UserRole.ADMINISTRATOR, "oper",2);
		odb.store(user);
		odb.close();

		odb = open(baseName);
		Objects<User> users = odb.query(User.class).orderByAsc("index").objects();
		odb.close();
		assertEquals(3, users.size());
		User user1 = users.first();
		assertEquals("admin", user1.getName());
		assertEquals(UserRole.ADMINISTRATOR, user1.getRole());

	}

	public void testStoreWithEnum2() {
		String baseName = getBaseName();
		System.out.println(baseName);
		ODB odb = open(baseName);
		int size = 100;
		for (int i = 0; i < size; i++) {
			User user = new User(UserRole.SUPERVISOR, "supervisor" + i,i*3);
			odb.store(user);

			user = new User(UserRole.ADMINISTRATOR, "admin" + i,i*3+1);
			odb.store(user);

			user = new User(UserRole.OPERATOR, "operator" + i,i*3+2);
			odb.store(user);
		}
		odb.close();

		odb = open(baseName);
		Objects<User> users = odb.query(User.class).orderByAsc("index").objects();
		int i = 0;
		while (users.hasNext()) {
			User u = users.next();
			assertEquals("supervisor" + i, u.getName());
			assertEquals(UserRole.SUPERVISOR, u.getRole());

			u = users.next();
			assertEquals("admin" + i, u.getName());
			assertEquals(UserRole.ADMINISTRATOR, u.getRole());

			u = users.next();
			assertEquals("operator" + i, u.getName());
			assertEquals(UserRole.OPERATOR, u.getRole());
			i++;
		}
		odb.close();
		assertEquals(3 * size, users.size());

	}

	public void testEnumUpdate() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		User user = new User(UserRole.SUPERVISOR, "supervisor 1",1);
		odb.store(user);
		odb.close();

		odb = open(baseName);
		Objects<User> users = odb.query(User.class).objects();
		User u1 = users.first();

		assertEquals(UserRole.SUPERVISOR, u1.getRole());
		assertEquals("supervisor 1", u1.getName());

		// Then update it
		u1.setRole(UserRole.ADMINISTRATOR);
		odb.store(u1);
		odb.close();

		odb = open(baseName);
		users = odb.query(User.class).objects();
		u1 = users.first();
		odb.close();

		assertEquals(UserRole.ADMINISTRATOR, u1.getRole());
		assertEquals("supervisor 1", u1.getName());
	}

	public void testQueryWithEnum() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		User user = new User(UserRole.SUPERVISOR, "supervisor 1",1);
		odb.store(user);
		odb.close();

		odb = open(baseName);
		Objects<User> users = odb.query(User.class, W.equal("role", UserRole.SUPERVISOR)).objects();
		odb.close();
		assertEquals(1, users.size());
		User u1 = users.first();

		assertEquals(UserRole.SUPERVISOR, u1.getRole());
		assertEquals("supervisor 1", u1.getName());

	}

	public void testQueryWithEnumWithAnd() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		User user = new User(UserRole.SUPERVISOR, "supervisor 1",1);
		odb.store(user);
		odb.close();

		odb = open(baseName);
		Objects<User> users = odb.query(User.class, W.equal("role", UserRole.SUPERVISOR).and(W.equal("name", "supervisor 1"))).objects();
		odb.close();
		assertEquals(1, users.size());
		User u1 = users.first();

		assertEquals(UserRole.SUPERVISOR, u1.getRole());
		assertEquals("supervisor 1", u1.getName());

	}
	
	public void testQueryWithEnum2() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ClassWithEnum cwe = new ClassWithEnum("class 1",MyEnum.ATIVO);
		odb.store(cwe);
		odb.close();

		odb = open(baseName);
		Objects<ClassWithEnum> cwes = odb.query(ClassWithEnum.class, W.equal("myEnum", MyEnum.ATIVO)).objects();
		odb.close();
		assertEquals(1, cwes.size());
		ClassWithEnum c = cwes.first();

		assertEquals(MyEnum.ATIVO, c.getMyEnum());
		assertEquals("class 1", c.getName());

	}

	public void testQueryWithEnumWithAnd2() {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		ClassWithEnum cwe = new ClassWithEnum("class 1",MyEnum.ATIVO);
		odb.store(cwe);
		odb.close();

		odb = open(baseName);
		Objects<ClassWithEnum> cwes = odb.query(ClassWithEnum.class, W.equal("myEnum", MyEnum.ATIVO).and(W.equal("name", "class 1"))).objects();
		odb.close();
		assertEquals(1, cwes.size());
		ClassWithEnum u1 = cwes.first();

		assertEquals(MyEnum.ATIVO, u1.getMyEnum());
		assertEquals("class 1", u1.getName());

	}
	
	

	public void testEnumReflection() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class clazz = UserRole.ADMINISTRATOR.getClass();
		System.out.println(clazz.getName());
		System.out.println(clazz.isEnum());
		UserRole[] roles = UserRole.values();
		UserRole ur = roles[0];
		System.out.println(ur);
		UserRole ur2 = UserRole.valueOf("ADMINISTRATOR");
		System.out.println(ur2);

		String s = "Test";
		assertFalse(s.getClass().isEnum());

		Class enumClass = Class.forName(UserRole.class.getName());
		assertTrue(enumClass.isEnum());

		Object ooo = Enum.valueOf(enumClass, "ADMINISTRATOR");
		System.out.println(ooo);
		assertEquals(UserRole.ADMINISTRATOR, ooo);

	}

	public void testClassWithEnumInInterface() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String baseName = getBaseName();
		ODB odb = open(baseName);

		User user = new User(UserRole.SUPERVISOR, "supervisor 1",1);
		List<IUser> users = new ArrayList<IUser>();
		users.add(user);
		ClassWithInterfaceWithEnum c = new ClassWithInterfaceWithEnum("my name", users);
		odb.store(c);
		odb.close();

		odb = open(baseName);
		Objects<ClassWithInterfaceWithEnum> cc = odb.getObjects(ClassWithInterfaceWithEnum.class);
		ClassWithInterfaceWithEnum ci = cc.first();

		odb.close();

	}

}
