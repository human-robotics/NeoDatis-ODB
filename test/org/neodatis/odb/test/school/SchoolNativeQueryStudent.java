/* 
 * $RCSfile: SchoolNativeQueryStudent.java,v $
 * Tag : $Name:  $
 * $Revision: 1.2 $
 * $Author: olivier_smadja $
 * $Date: 2010/02/07 18:43:31 $
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

import org.neodatis.odb.core.query.nq.NativeQuery;
import org.neodatis.odb.test.vo.school.Student;

public class SchoolNativeQueryStudent extends NativeQuery<Student> {
	private String name;
	private int age;

	public SchoolNativeQueryStudent(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public boolean match(Student object) {
		Student s = (Student) object;
		return s.getName().equals(name);
	}

}
