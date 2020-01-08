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
package org.neodatis.odb.core.layers.layer1;

import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.layers.layer4.OidGenerator;
import org.neodatis.odb.core.session.Session;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;

/**
 * The ClassIntrospector is used to introspect classes. It uses Reflection to
 * extract class information. It transforms a native Class into a ClassInfo (a
 * meta representation of the class) that contains all informations about the
 * class.
 * @sharpen.ignore
 * @author osmadja
 * 
 */
public class DefaultClassIntrospector extends ClassIntrospectorImpl {
	
	public DefaultClassIntrospector(Session session, OidGenerator oidGenerator) {
		super(session, oidGenerator);
	}

	/**
	 * Tries to create a default constructor (with no identification) for the class
	 * and stores it the constructor cache.
	 * 
	 * @param clazz
	 * @return
	 */
	protected boolean tryToCreateAnEmptyConstructor(Class clazz) {
		if (!session.getConfig().enableEmptyConstructorCreation()) {
			return false;
		}
		// TODO Check java version, must be >= 1.4
		Constructor javaLangObjectConstructor;
		try {
			javaLangObjectConstructor = Object.class
					.getDeclaredConstructor(new Class[0]);
		} catch (Exception e) {
			throw new NeoDatisRuntimeException(
					NeoDatisError.ERROR_WHILE_GETTING_CONSTRCTORS_OF_CLASS
							.addParameter(clazz.getName()), e);
		}
		
		Constructor customConstructor = ReflectionFactory
				.getReflectionFactory().newConstructorForSerialization(clazz,
						javaLangObjectConstructor);
		customConstructor.setAccessible(true);
		addConstructor(clazz.getName(), customConstructor);
		return true;
	}

}
