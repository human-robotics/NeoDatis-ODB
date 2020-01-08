package org.neodatis.odb.test.fromusers.wonman.real2;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

public class TestWonMan extends ODBTest {

	/**
	 * @param args
	 * @throws ArgumentException
	 * @throws ArgumentException
	 */
	public void test1() throws ArgumentException {
		String baseName = getBaseName();

		ODB odb = NeoDatis.open(baseName);

		Barloworld barloworld = new Barloworld();

		odb.store(barloworld);

		User user = new User(barloworld, "user1");

		odb.store(user);

		odb.close();

		odb = NeoDatis.open(baseName);
		Objects<User> uus = odb.query(User.class).objects();
		odb.close();

		assertEquals(1, uus.size());
		User u = uus.first();
		assertEquals(u.getOrganization().getId(), user.getOrganization().getId());

		odb = NeoDatis.open(baseName);
		uus = odb.query(User.class, W.equal("organization.id", 2)).objects();
		odb.close();
		
		assertEquals(1, uus.size());	
	}

}
