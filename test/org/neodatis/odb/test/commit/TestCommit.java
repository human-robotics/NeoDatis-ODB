package org.neodatis.odb.test.commit;

import org.junit.Before;
import org.junit.Test;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestCommit extends ODBTest {

    @Before
	public void setUp() throws Exception {
		super.setUp();
	}

    @Test
	public void testInsertWithCommitsSimpleObject() throws Exception {
		ODB odb = null;
		int size = isLocal ? 1000 : 300;
		int commitInterval = 1;
		try {
			odb = open(getBaseName());

			for (int i = 0; i < size; i++) {
				odb.store(new Function("function " + i));
				if (i % commitInterval == 0) {
					odb.commit();
					// println("commiting "+i);
				}
			}
		} finally {
			odb.close();
		}

		odb = open(getBaseName());
		Objects objects = odb.getObjects(Function.class);
		int nbObjects = objects.size();
		Map map = new OdbHashMap();
		Function function = null;
		int j = 0;
		while (objects.hasNext()) {
			function = (Function) objects.next();
			Integer ii = (Integer) map.get(function);
			if (ii != null) {
				println(j + ":" + function.getName() + " already exist at " + ii);
			} else {
				map.put(function, new Integer(j));
			}
			j++;
		}
		odb.close();
		println("Nb objects=" + nbObjects);
		assertEquals(size, nbObjects);

	}

    @Test
	public void testInsertWithCommitsComplexObject() throws Exception {
		ODB odb = null;
		int size = isLocal ? 530 : 50;
		int commitInterval = 100;
		try {
			odb = open(getBaseName());

			for (int i = 0; i < size; i++) {
				odb.store(getInstance(i));
				if (i % commitInterval == 0) {
					odb.commit();
					// println("commiting "+i);
				}
				if (i % 100 == 0 && !isLocal) {
					println(i);
				}
			}
		} finally {
			odb.close();
		}

		odb = open(getBaseName());
		Objects users = odb.getObjects(User.class);
		Objects profiles = odb.getObjects(Profile.class);
		Objects functions = odb.getObjects(Function.class);
		int nbUsers = users.size();
		int nbProfiles = profiles.size();
		int nbFunctions = functions.size();
		odb.close();
		println("Nb users=" + nbUsers);
		println("Nb profiles=" + nbProfiles);
		println("Nb functions=" + nbFunctions);

		assertEquals(size, nbUsers);
		assertEquals(size, nbProfiles);
		assertEquals(size * 2, nbFunctions);

	}

	private Object getInstance(int i) {
		Function login = new Function("login" + i);
		Function logout = new Function("logout" + i);
		List list = new ArrayList();
		list.add(login);
		list.add(logout);
		Profile profile = new Profile("operator" + i, list);
		User user = new User("olivier" + i, "olivier@neodatis.com", profile);
		return user;
	}

}
