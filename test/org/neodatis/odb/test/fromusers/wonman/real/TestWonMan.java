package org.neodatis.odb.test.fromusers.wonman.real;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.fromusers.wonman.real.NeoDatisDAO.UpdateHelper;

import java.util.List;

public class TestWonMan extends ODBTest{

	/**
	 * @param args
	 * @throws ArgumentException 
	 * @throws ArgumentException 
	 */
	public void test1() throws ArgumentException {
		String baseName = getBaseName();
		NeoDatisDAO dao = new NeoDatisDAO(baseName);		
		Barloworld barloworld = dao.load(Barloworld.class);
		if(barloworld == null) {
			
			barloworld = dao.save(null, "wonman", new UpdateHelper<Barloworld>() {

				@Override
				public Barloworld setVariables(ODB odb, Barloworld barloworld)
						throws ArgumentException {
					//
					barloworld.setName("Barloworld Handling");
					//
					return barloworld;
				}
			});
		}

		final Barloworld bw = dao.load(Barloworld.class);

		
		List<User> users = dao.list(User.class, "organization.id", barloworld.getId());
		
		if(users.size() == 0) {
			
			dao.save(null, "wonman", new UpdateHelper<User>() {

				@Override
				public User setVariables(ODB odb, User user)
						throws ArgumentException {
					
					Barloworld b = (Barloworld) load(odb, bw.getId());
					user.setOrganization(b);

					user.setLoginID("b1");

					return user;
				}
			});
		}
		
		ODB odb = NeoDatis.open(baseName);
		Objects<Barloworld> bb  = odb.query(Barloworld.class).objects();
		odb.close();

		odb = NeoDatis.open(baseName);
		Objects<User> uus  = odb.query(User.class).objects();
		odb.close();

		users = dao.list(User.class, "organization.id", barloworld.getId());
		assertEquals(1, users.size());
	}

}
