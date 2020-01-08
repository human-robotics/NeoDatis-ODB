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

import org.neodatis.btree.IBTree;
import org.neodatis.btree.IBTreeNode;
import org.neodatis.btree.IBTreeNodeOneValuePerKey;
import org.neodatis.btree.IKeyAndValue;
import org.neodatis.btree.impl.AbstractBTree;
import org.neodatis.btree.impl.AbstractBTreeNode;
import org.neodatis.btree.impl.KeyAndValue;
import org.neodatis.btree.impl.singlevalue.BTreeNodeSingleValuePerKey;
import org.neodatis.btree.impl.singlevalue.BTreeSingleValuePerKey;
import org.neodatis.odb.ClassOid;
import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.OID;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.btree.ODBBTreeMultiple;
import org.neodatis.odb.core.btree.ODBBTreeNodeMultiple;
import org.neodatis.odb.core.btree.ODBBTreeNodeSingle;
import org.neodatis.odb.core.btree.ODBBTreeSingle;
import org.neodatis.odb.core.layers.layer2.instance.ClassPool;
import org.neodatis.odb.core.layers.layer2.instance.FullInstantiationHelper;
import org.neodatis.odb.core.layers.layer2.instance.InstantiationHelper;
import org.neodatis.odb.core.layers.layer2.instance.ParameterHelper;
import org.neodatis.odb.core.layers.layer2.meta.*;
import org.neodatis.odb.core.layers.layer4.OidGenerator;
import org.neodatis.odb.core.oid.uuid.OIDImpl;
import org.neodatis.odb.core.query.SimpleCompareKey;
import org.neodatis.odb.core.session.Session;
import org.neodatis.tool.DLogger;
import org.neodatis.tool.DisplayUtility;
import org.neodatis.tool.wrappers.OdbClassUtil;
import org.neodatis.tool.wrappers.list.IOdbList;
import org.neodatis.tool.wrappers.list.OdbArrayList;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The ClassIntrospector is used to introspect classes. It uses Reflection to
 * extract class information. It transforms a native Class into a ClassInfo (a
 * meta representation of the class) that contains all informations about the
 * class.
 * 
 * @sharpen.ignore
 * @author osmadja
 * 
 */
public abstract class ClassIntrospectorImpl implements ClassIntrospector {
	private static final String LOG_ID = "ClassIntrospector";
	private Map<String, IOdbList<Field>> fieldsByClassName = new OdbHashMap<String, IOdbList<Field>>();
	private Map<String, Field> fieldsByClassNameAndName = new OdbHashMap<String, Field>();
	private Map<String, Class> systemClasses = new OdbHashMap<String, Class>();
	private Map<String, FullInstantiationHelper> fullInstantiationHelpers = new OdbHashMap<String, FullInstantiationHelper>();
	private Map<String, InstantiationHelper> instantiationHelpers = new OdbHashMap<String, InstantiationHelper>();
	private Map<String, ParameterHelper> parameterHelpers = new OdbHashMap<String, ParameterHelper>();
	private ClassPool classPool;
	protected OidGenerator oidGenerator;
	protected Session session;

	public ClassIntrospectorImpl(Session session, OidGenerator oidGenerator) {
		this.session = session;
		this.oidGenerator = oidGenerator;
		this.classPool = session.getConfig().getCoreProvider().getClassPool();
	}

	protected OidGenerator getoidGenerator() {
		return oidGenerator;
	}

	public void reset() {
		fieldsByClassName.clear();
		fieldsByClassNameAndName.clear();
		fullInstantiationHelpers.clear();
		instantiationHelpers.clear();
		parameterHelpers.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #addInstanciationHelper(java.lang.Class,
	 * org.neodatis.odb.core.layers.layer2.instance.InstantiationHelper)
	 */
	public void addInstanciationHelper(Class clazz, InstantiationHelper helper) {
		addInstantiationHelper(clazz.getName(), helper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #addParameterHelper(java.lang.Class,
	 * org.neodatis.odb.core.layers.layer2.instance.ParameterHelper)
	 */
	public void addParameterHelper(Class clazz, ParameterHelper helper) {
		addParameterHelper(clazz.getName(), helper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #addFullInstanciationHelper(java.lang.Class,
	 * org.neodatis.odb.core.layers.layer2.instance.FullInstantiationHelper)
	 */
	public void addFullInstanciationHelper(Class clazz, FullInstantiationHelper helper) {
		addFullInstantiationHelper(clazz.getName(), helper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #addInstantiationHelper(java.lang.String,
	 * org.neodatis.odb.core.layers.layer2.instance.InstantiationHelper)
	 */
	public void addInstantiationHelper(String clazz, InstantiationHelper helper) {
		instantiationHelpers.put(clazz, helper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #addParameterHelper(java.lang.String,
	 * org.neodatis.odb.core.layers.layer2.instance.ParameterHelper)
	 */
	public void addParameterHelper(String clazz, ParameterHelper helper) {
		parameterHelpers.put(clazz, helper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #addFullInstantiationHelper(java.lang.String,
	 * org.neodatis.odb.core.layers.layer2.instance.FullInstantiationHelper)
	 */
	public void addFullInstantiationHelper(String clazz, FullInstantiationHelper helper) {
		fullInstantiationHelpers.put(clazz, helper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #removeInstantiationHelper(java.lang.Class)
	 */
	public void removeInstantiationHelper(Class clazz) {
		removeInstantiationHelper(clazz.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #removeInstantiationHelper(java.lang.String)
	 */
	public void removeInstantiationHelper(String canonicalName) {
		instantiationHelpers.remove(canonicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #removeParameterHelper(java.lang.Class)
	 */
	public void removeParameterHelper(Class clazz) {
		removeParameterHelper(clazz.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #removeParameterHelper(java.lang.String)
	 */
	public void removeParameterHelper(String canonicalName) {
		parameterHelpers.remove(canonicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #removeFullInstantiationHelper(java.lang.Class)
	 */
	public void removeFullInstantiationHelper(Class clazz) {
		removeFullInstantiationHelper(clazz.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #removeFullInstantiationHelper(java.lang.String)
	 */
	public void removeFullInstantiationHelper(String canonicalName) {
		fullInstantiationHelpers.remove(canonicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #introspect(java.lang.Class, boolean)
	 */
	public ClassInfoList introspect(Class clazz) {
		return internalIntrospect(clazz, null);
	}

	/**
	 * introspect a list of classes
	 * 
	 * 
	 * This method return the current meta model based on the classes that
	 * currently exist in the execution classpath. The result will be used to
	 * check meta model compatiblity between the meta model that is currently
	 * persisted in the database and the meta model currently executing in JVM.
	 * This is used b the automatic meta model refactoring
	 * 
	 * @return
	 * 
	 * 
	 * @param classInfos
	 *            , the classinfo currently existng in the meta model
	 * @return A map where the key is the class name and the key is the
	 *         ClassInfo: the class meta representation
	 */
	public Map<String, ClassInfo> instrospect(IOdbList<ClassInfo> classInfos) {
		ClassInfo persistedCI = null;
		ClassInfo currentCI = null;

		Map<String, ClassInfo> cis = new OdbHashMap<String, ClassInfo>();
		// re introspect classes
		Iterator<ClassInfo> iterator = classInfos.iterator();
		while (iterator.hasNext()) {
			persistedCI = iterator.next();
			currentCI = getClassInfo(persistedCI.getFullClassName(), persistedCI);
			cis.put(currentCI.getFullClassName(), currentCI);
		}
		return cis;

	}

	/**
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @param recursive
	 *            If true, goes does the hierarchy to try to analyse all classes
	 * @param classInfoList
	 *            A map with classname that are being introspected, to avoid
	 *            recursive calls
	 * 
	 * @return
	 */
	private ClassInfoList internalIntrospect(Class clazz, ClassInfoList classInfoList) {

		if (classInfoList != null) {

			ClassInfo existingCi = classInfoList.getClassInfoWithName(clazz.getName());
			if (existingCi != null) {
				return classInfoList;
			}
		}
		if (classInfoList == null) {
			classInfoList = new ClassInfoList();
		}

		// If class already exists in meta model, we don't need to add it to the
		// list
		ClassInfo existingCi = session.getMetaModel().getClassInfo(clazz.getName(), false);
		if (existingCi != null) {
			classInfoList.addClassInfo(existingCi);
			return classInfoList;
		}

		Class superClass = clazz.getSuperclass();
		boolean hasSuperClass = superClass != null && superClass != Object.class;
		// Introspect super class (if not Object.class)
		if (hasSuperClass) {
			internalIntrospect(superClass, classInfoList);
		}

		ClassInfo classInfo = new ClassInfo(clazz.getName());
		if (hasSuperClass) {
			classInfo.setSuperClassOid(classInfoList.getClassInfoWithName(superClass.getName()).getOid());
		}
		classInfo.setOid(getoidGenerator().createClassOid());
		classInfo.setClassCategory(getClassCategory(clazz.getName()));
		if (classInfoList == null) {
			classInfoList = new ClassInfoList(classInfo);
		} else {
			classInfoList.addClassInfo(classInfo);
		}

		IOdbList<Field> fields = getAllFields(clazz.getName());
		IOdbList<ClassAttributeInfo> attributes = new OdbArrayList<ClassAttributeInfo>(fields.size());

		ClassInfo attributeCi = null;
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			if (!ODBType.getFromClass(field.getType()).isNative() || OdbClassUtil.isEnum(field.getType())) {
				classInfoList = internalIntrospect(field.getType(), classInfoList);
				attributeCi = classInfoList.getClassInfoWithName(field.getType().getName());
			} else {
				attributeCi = null;
			}
			ClassOid attributeClassInfoOid = null;
			if (attributeCi != null) {
				attributeClassInfoOid = attributeCi.getOid();
			}
			attributes.add(new ClassAttributeInfo((i + 1), field.getName(), field.getType(), field.getType().getName(), attributeClassInfoOid, classInfo
					.getOid()));
		}
		classInfo.setAttributes(attributes);
		classInfo.setMaxAttributeId(fields.size());
		return classInfoList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #getClassInfo(java.lang.String,
	 * org.neodatis.odb.core.layers.layer2.meta.ClassInfo)
	 */
	public ClassInfo getClassInfo(String fullClassName, ClassInfo existingClassInfo) {

		ClassInfo classInfo = new ClassInfo(fullClassName);
		classInfo.setClassCategory(getClassCategory(fullClassName));
		IOdbList<Field> fields = getAllFields(fullClassName);
		IOdbList<ClassAttributeInfo> attributes = new OdbArrayList<ClassAttributeInfo>(fields.size());
		int attributeId = -1;
		int maxAttributeId = existingClassInfo.getMaxAttributeId();
		ClassInfo ci = null;
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			// Gets the attribute id from the existing class info
			attributeId = existingClassInfo.getAttributeId(field.getName());
			if (attributeId == -1) {
				maxAttributeId++;
				// The attribute with field.getName() does not exist in existing
				// class info
				// create a new id
				attributeId = maxAttributeId;

			}
			// TODO is it ok to pass null for attributeCiOid?
			attributes.add(new ClassAttributeInfo(attributeId, field.getName(), field.getType(), field.getType().getName(), null, classInfo.getOid()));
		}
		classInfo.setAttributes(attributes);
		classInfo.setMaxAttributeId(maxAttributeId);
		return classInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #getSuperClasses(java.lang.String, boolean)
	 */
	public List<Class> getSuperClasses(String fullClassName, boolean includingThis) {
		List<Class> result = new ArrayList<Class>(10);

		Class clazz = classPool.getClass(fullClassName);

		if (clazz.isInterface()) {
			// throw new ODBRuntimeException(clazz.getName() + " is an
			// interface");
		}
		if (includingThis) {
			result.add(clazz);
		}

		Class superClass = clazz.getSuperclass();
		while (superClass != null && superClass != Object.class) {
			result.add(superClass);
			superClass = superClass.getSuperclass();
		}
		return result;
	}

	public Field getField(Class clazz, String fieldName) {
		Field f = fieldsByClassNameAndName.get(clazz.getName() + "." + fieldName);
		if (f == null) {
			throw new NeoDatisRuntimeException(NeoDatisError.REFLECTION_ERROR_WHILE_GETTING_FIELD.addParameter(fieldName).addParameter(clazz.getName()));
		}
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #getAllFields(java.lang.String)
	 */
	public synchronized IOdbList<Field> getAllFields(String fullClassName) {

		IOdbList<Field> result = fieldsByClassName.get(fullClassName);

		if (result != null) {
			return result;
		}

		// Map used to prevent from getting a duplicated name for attributes
		Map attributesNames = new OdbHashMap();

		result = new OdbArrayList(50);
		Field[] superClassfields = null;
		List<Class> classes = getSuperClasses(fullClassName, true);
		for (int i = 0; i < classes.size(); i++) {
			Class clazz1 = classes.get(i);

			superClassfields = clazz1.getDeclaredFields();
			for (int j = 0; j < superClassfields.length; j++) {
				// Only adds the attribute if it does not exist one with same
				// name
				if (attributesNames.get(superClassfields[j].getName()) == null) {
					superClassfields[j].setAccessible(true);
					result.add(superClassfields[j]);
					attributesNames.put(superClassfields[j].getName(), superClassfields[j].getName());
				}
			}
		}
		result = removeUnnecessaryFields(result);
		fieldsByClassName.put(fullClassName, result);

		for (Field f : result) {
			fieldsByClassNameAndName.put(fullClassName + "." + f.getName(), f);
		}

		attributesNames.clear();
		attributesNames = null;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #removeUnnecessaryFields(java.util.List)
	 */
	public IOdbList<Field> removeUnnecessaryFields(IOdbList<Field> fields) {
		IOdbList<Field> fieldsToRemove = new OdbArrayList<Field>(fields.size());

		// Remove fields
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);

			// Remove transient and fields
			if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
				fieldsToRemove.add(field);
			}
			// Remove inner class fields
			if (field.getName().startsWith("this$")) {
				fieldsToRemove.add(field);
			}
		}

		fields.removeAll(fieldsToRemove);
		return fields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #introspect(java.lang.String, boolean)
	 */
	public ClassInfoList introspect(String fullClassName) {
		return introspect(classPool.getClass(fullClassName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #getConstructorOf(java.lang.String)
	 */
	public Constructor getConstructorOf(String fullClassName) {
		Class clazz = classPool.getClass(fullClassName);
		try {
			// Checks if exist a default constructor - with no parameters
			Constructor constructor = clazz.getConstructor(new Class[0]);
			return constructor;
		} catch (NoSuchMethodException e) {
			// else take the constructor with the smaller number of parameters
			// and call it will null values
			// TODO Put this info in cache !
			if (session.getConfig().isDebugEnabled(LOG_ID)) {
				DLogger.debug(clazz + " does not have default constructor! using a 'with identification' constructor will null values");
			}
			Constructor[] constructors = clazz.getConstructors();
			int numberOfParameters = 1000;
			int bestConstructorIndex = 0;
			for (int i = 0; i < constructors.length; i++) {
				if (constructors[i].getParameterTypes().length < numberOfParameters) {
					bestConstructorIndex = i;
				}
			}
			Constructor constructor = constructors[bestConstructorIndex];
			return constructor;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #newFullInstanceOf(java.lang.Class,
	 * org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo)
	 */
	public Object newFullInstanceOf(Class clazz, NonNativeObjectInfo nnoi) {
		String className = clazz.getName();
		FullInstantiationHelper helper = (FullInstantiationHelper) fullInstantiationHelpers.get(className);
		if (helper != null) {
			Object o = helper.instantiate(nnoi);
			if (o != null) {
				return o;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #newInstanceOf(java.lang.Class)
	 */
	public Object newInstanceOf(Class clazz) {
		String className = clazz.getName();

		InstantiationHelper helper = (InstantiationHelper) instantiationHelpers.get(className);
		if (helper != null) {
			Object o = helper.instantiate();
			if (o != null) {
				return o;
			}
		}

		try {
			Constructor constructor = null;
			constructor = classPool.getConstructor(className);
			if (constructor == null) {
				// Checks if exist a default constructor - with no parameters
				constructor = clazz.getDeclaredConstructor(new Class[0]);
				constructor.setAccessible(true);
				classPool.addConstructor(className, constructor);
			}
			return constructor.newInstance(new Object[0]);
		} catch (Exception e) {
			// Any exception, creates a default empty constructor
			return manageNoDefaultConstructor(clazz);
		}
	}

	private Object manageNoDefaultConstructor(Class clazz) {
		String className = clazz.getName();
		// It does not exist default constructor, tries
		// to create one
		boolean ok = tryToCreateAnEmptyConstructor(clazz);
		if (ok) {
			Constructor createdConstructor = classPool.getConstructor(className);
			try {
				return createdConstructor.newInstance(new Object[0]);
			} catch (Exception e) {
				throw new NeoDatisRuntimeException(NeoDatisError.INSTANCIATION_ERROR.addParameter(className), e);
			}
		}

		// else take the constructor with the smaller number of parameters
		// and call it will null values
		// @TODO Put this info in cache !
		if (session.getConfig().isDebugEnabled(LOG_ID)) {
			DLogger.debug(clazz + " does not have default constructor! using a 'with identification' constructor will null values");
		}
		Constructor[] constructors = clazz.getDeclaredConstructors();

		if (clazz.isInterface()) {
			// @TODO This is not a good solution to manage interface
			return null;
		}

		if (constructors.length == 0) {
			throw new NeoDatisRuntimeException(NeoDatisError.CLASS_WITHOUT_CONSTRUCTOR.addParameter(clazz.getName()));
		}
		int numberOfParameters = 1000;
		int bestConstructorIndex = 0;
		for (int i = 0; i < constructors.length; i++) {
			if (constructors[i].getParameterTypes().length < numberOfParameters) {
				bestConstructorIndex = i;
			}
		}
		Constructor constructor = constructors[bestConstructorIndex];
		Object[] parameters;
		ParameterHelper paramHelper = (ParameterHelper) parameterHelpers.get(className);
		if (paramHelper != null) {
			parameters = paramHelper.parameters();
			if (parameters.length != constructor.getParameterTypes().length) {
				throw new NeoDatisRuntimeException(NeoDatisError.PARAM_HELPER_WRONG_NO_OF_PARAMS.addParameter(className).addParameter(constructor.toString()));
			}
		} else {
			parameters = new Object[constructor.getParameterTypes().length];
			for (int i = 0; i < parameters.length; i++) {
				if (constructor.getParameterTypes()[i] == Integer.TYPE) {
					parameters[i] = new Integer(0);
				} else if (constructor.getParameterTypes()[i] == Long.TYPE) {
					parameters[i] = new Long(0);
				} else if (constructor.getParameterTypes()[i] == Short.TYPE) {
					parameters[i] = new Short("0");
				} else if (constructor.getParameterTypes()[i] == Byte.TYPE) {
					parameters[i] = new Byte("0");
				} else if (constructor.getParameterTypes()[i] == Float.TYPE) {
					parameters[i] = new Float("0");
				} else if (constructor.getParameterTypes()[i] == Double.TYPE) {
					parameters[i] = new Double("0");
				} else {
					parameters[i] = null;
				}
			}
		}
		Object object = null;

		constructor.setAccessible(true);
		try {
			object = constructor.newInstance(parameters);
		} catch (Throwable e2) {
			// throw new
			// ODBRuntimeException(Error.NO_NULLABLE_CONSTRUCTOR.addParameter(
			// DisplayUtility
			// .ojbectArrayToString(constructor.getParameterTypes()
			// )).addParameter(clazz.getName()),
			// e2);
			throw new NeoDatisRuntimeException(NeoDatisError.NO_NULLABLE_CONSTRUCTOR.addParameter(
					"[" + DisplayUtility.objectArrayToString(constructor.getParameterTypes()) + "]").addParameter(clazz.getName()), e2);
		}
		return object;

	}

	/**
	 * Tries to create a default constructor (with no identification) for the
	 * class and stores it the constructor cache.
	 * 
	 * @param clazz
	 * @return
	 */
	protected abstract boolean tryToCreateAnEmptyConstructor(Class clazz);

	public boolean isSystemClass(String fullClassName) {
		return systemClasses.containsKey(fullClassName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.impl.layers.layer1.introspector.IClassIntrospector
	 * #getClassCategory(java.lang.String)
	 */
	public byte getClassCategory(String fullClassName) {
		if (systemClasses.isEmpty()) {
			fillSystemClasses();
		}
		if (systemClasses.get(fullClassName) != null) {
			return ClassInfo.CATEGORY_SYSTEM_CLASS;
		}
		return ClassInfo.CATEGORY_USER_CLASS;
	}

	// FIXME put the list of the classes elsewhere!
	private void fillSystemClasses() {
		systemClasses.put(ClassInfoIndex.class.getName(), ClassInfoIndex.class);
		systemClasses.put(OID.class.getName(), OID.class);
		systemClasses.put(OIDImpl.class.getName(), OIDImpl.class);
		systemClasses.put(ODBBTreeNodeSingle.class.getName(), ODBBTreeNodeSingle.class);
		systemClasses.put(ODBBTreeNodeMultiple.class.getName(), ODBBTreeNodeMultiple.class);

		systemClasses.put(ODBBTreeMultiple.class.getName(), ODBBTreeMultiple.class);
		systemClasses.put(ODBBTreeSingle.class.getName(), ODBBTreeSingle.class);
		systemClasses.put(ODBBTreeNodeMultiple.class.getName(), ODBBTreeNodeMultiple.class);

		systemClasses.put(IBTree.class.getName(), IBTree.class);
		systemClasses.put(IBTreeNodeOneValuePerKey.class.getName(), IBTreeNodeOneValuePerKey.class);
		systemClasses.put(IKeyAndValue.class.getName(), IKeyAndValue.class);
		systemClasses.put(KeyAndValue.class.getName(), KeyAndValue.class);
		systemClasses.put(SimpleCompareKey.class.getName(), SimpleCompareKey.class);
		systemClasses.put(Comparable.class.getName(), Comparable.class);
		systemClasses.put(IBTreeNode.class.getName(), IBTreeNode.class);
		systemClasses.put(Object.class.getName(), Object.class);
		systemClasses.put(BTreeSingleValuePerKey.class.getName(), BTreeSingleValuePerKey.class);
		systemClasses.put(BTreeNodeSingleValuePerKey.class.getName(), BTreeNodeSingleValuePerKey.class);
		systemClasses.put(AbstractBTreeNode.class.getName(), AbstractBTreeNode.class);
		systemClasses.put(AbstractBTree.class.getName(), AbstractBTree.class);
	}

	protected void addConstructor(String className, Constructor constructor) {
		classPool.addConstructor(className, constructor);
	}

	public ClassPool getClassPool() {
		return classPool;
	}

}
