/* 
 * $RCSfile: TestQuerySchool.java,v $
 * Tag : $Name:  $
 * $Revision: 1.2 $
 * $Author: olivier_smadja $
 * $Date: 2009/11/14 03:45:20 $
 * 
 * 
 */

/*
 NeoDatis ODB : Native Object Database (odb.info@neodatis.org)
 Copyright (C) 2007 NeoDatis Inc. http://www.neodatis.org

 "This file is part of the NeoDatis ODB open source object database".

 NeoDatis ODB is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 NeoDatis ODB is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.neodatis.odb.test.school;

import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.MetaModel;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.school.*;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class TestQuerySchool extends ODBTest {

	// possiveis consultas
	// Listar todos os alunos de determinado professor
	// Listar alunos com nota abaixo de x
	// Listar disciplinas que um professor ministrou no semestre

	public void test1() throws Exception {
		if (!isLocal) {
			return;
		}
		ODB odb = open(getBaseName());

		// List students by name
		SchoolNativeQueryStudent natQuery = new SchoolNativeQueryStudent("Brenna", 23);
		Objects students = odb.query(natQuery).objects();

		SchoolSimpleNativeQueryStudent sNatQuery = new SchoolSimpleNativeQueryStudent("Brenna");
		students = odb.query(sNatQuery).objects();

		// list disciplines of one teacher by semester

		SchoolNativeQueryTeacher natQuery2 = new SchoolNativeQueryTeacher("Jeremias");
		Objects historys = odb.query(natQuery2).objects();
		HashMap listDiscipline = new OdbHashMap();
		for (Iterator iter = historys.iterator(); iter.hasNext();) {
			History h = (History) iter.next();
			listDiscipline.put(h.getDiscipline().getName(), h.getDiscipline());
		}

		odb.close();

	}

	public void test12() throws Exception {
		ODB odb = null;

		try {
			odb = open(getBaseName());
			MetaModel metaModel = Dummy.getEngine(odb).getSession().getMetaModel();
			ClassInfo ci = metaModel.getClassInfo(Student.class.getName(), true);
			assertFalse(metaModel.hasCyclicReference(ci));
		} finally {
			if (odb != null) {
				odb.close();
			}
		}
	}

	public void setUp() throws Exception {
		super.setUp();
		ODB odb = open(getBaseName());

		Objects students = odb.query(Student.class).objects();
		int numStudents = students.size();

		Course computerScience = new Course("Computer Science");
		Teacher teacher = new Teacher("Jeremias", "Java");
		Discipline dw1 = new Discipline("Des. Web 1", 3);
		Discipline is = new Discipline("Intranet/Segurança", 4);

		Student std1 = new Student(20, computerScience, new Date(), "1cs", "Brenna");

		History h1 = new History(new Date(), dw1, 0, teacher);
		History h2 = new History(new Date(), is, 0, teacher);

		std1.addHistory(h1);
		std1.addHistory(h2);

		odb.store(std1);

		odb.commit();
		odb.close();

		odb = open(getBaseName());
		students = odb.query(Student.class).objects();
		odb.close();
		assertEquals(numStudents + 1, students.size());
	}


}
