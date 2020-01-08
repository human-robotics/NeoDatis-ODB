package org.neodatis.odb.test.fromusers.wonman;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.test.ODBTest;

public class TestWonMan extends ODBTest {

	public void test1() {
		String baseName = getBaseName();

		ODB odb = null;

		try {
			odb = open(baseName);

			User u1 = new User("user1", new Customer(1));
			odb.store(u1);
			User u2 = new User("user2", new Customer(2));
			odb.store(u2);
			odb.close();
			
			odb = open(baseName);
			Query query = odb.query(User.class, W.equal("organization.id", 1));
            Objects<User> users = query.objects();
            assertEquals(1, users.size());

			query = odb.query(User.class, W.equal("organization.id", 2));
			users = query.objects();
            assertEquals(1, users.size());
			
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}
}
