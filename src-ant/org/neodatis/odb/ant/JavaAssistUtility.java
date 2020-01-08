package org.neodatis.odb.ant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

import org.neodatis.odb.NeoDatisContext;
import org.neodatis.odb.NeoDatisObject;
import org.neodatis.odb.core.context.NeoDatisContextImpl;
import org.neodatis.tool.wrappers.OdbClassUtil;

public class JavaAssistUtility {

	public void makeNeoDatisClass(String className, String directoryToWrite) throws Exception {
		try {
			System.out.println("Enhancing class " + className);
			Class c = Class.forName(className);
			System.out.println("C from java " + c.getName());
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = pool.get(className);
			cc.stopPruning(true);
			cc.defrost();

			String fieldNameContext = "__neoDatisContext__";

			// adds the context field
			String code = String.format("protected %s %s = new %s();", NeoDatisContext.class.getName(), fieldNameContext, NeoDatisContextImpl.class.getName());
			System.out.println(code);
			CtField context = CtField.make(code, cc);
			cc.addField(context);

			// retrieve all setters
			CtMethod[] methods = cc.getMethods();
			for (CtMethod method : methods) {
				if (method.getName().startsWith("set")) {
					method.insertAfter(String.format("{%s.markAsChanged();}", fieldNameContext));
					System.out.println(method.toString());
					// method.insertAfter("{System.out.println();};");
				}
			}

			// add the interface
			cc.addInterface(pool.getCtClass(NeoDatisObject.class.getName()));
			// add the get/set Methods for context
			CtMethod getterContext = CtNewMethod.make(
					String.format("public %s getNeoDatisContext() { return %s; }", NeoDatisContext.class.getName(), fieldNameContext), cc);
			CtMethod setterContext = CtNewMethod.make(
					String.format("public void setNeoDatisContext(%s context) { %s = context; }", NeoDatisContext.class.getName(), fieldNameContext), cc);
			cc.addMethod(getterContext);
			cc.addMethod(setterContext);

			String fullName = directoryToWrite + "/" + cc.getName().replace(".", "/") + ".class";
			cc.writeFile(directoryToWrite);
			// Class c = cc.toClass();
			System.out.println("Writing instrumented class " + cc.getSimpleName() + " to " + fullName);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			throw e;
		}
	}

	public void makeNeoDatisMetaClass(String className, String directoryToWrite, String metaPackage) throws Exception {
		try {
			System.out.println("Build Meta class for class " + className);
			Class c = Class.forName(className);

			Field[] fields = c.getDeclaredFields();
			buildMetaClass(className, fields, directoryToWrite, metaPackage);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
			throw e;
		}
	}

	private void buildMetaClass(String className, Field[] ffs, String directoryToWrite, String metaDirectory) throws IOException {

		String cpackage = OdbClassUtil.getPackageName(className) + "." + metaDirectory;
		String cclassname = OdbClassUtil.getClassName(className) + "Meta";

		StringBuilder builder = new StringBuilder();
		builder.append("package ").append(cpackage).append(";");
		builder.append("\n\n/**\n\nAttribute information of class ").append(className);
		builder.append("\n\n generated by NeoDatis on  ").append(new Date());

		builder.append("\n\n*/");
		builder.append("\npublic class ").append(cclassname).append("{");
		boolean hasFields = false;
		for (Field f : ffs) {
			if (!Modifier.isStatic(f.getModifiers())) {
				builder.append("\n\tpublic final static String ").append(format(f.getName())).append(" = \"").append(f.getName()).append("\";");
				hasFields = true;
			}
		}
		builder.append("\n}");
		if (hasFields) {

			String fileName = directoryToWrite + "/" + cpackage.replace('.', '/') + "/" + cclassname + ".java";
			File javaFile = new File(directoryToWrite + "/" + className.replace('.', '/') + ".java");
			File javaMetaFile = new File(fileName);

			if (!javaMetaFile.exists() || javaFile.lastModified() > javaMetaFile.lastModified()) {
				new File(fileName).getParentFile().mkdirs();
				FileWriter w = new FileWriter(fileName);
				w.write(builder.toString());
				w.close();
				System.out.println("Meta File Created : " + fileName);
			}
		}
	}

	private String format(String s) {
		int l = s.length();
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < l; i++) {
			char c = s.charAt(i);
			if (Character.isUpperCase(c)) {
				b.append("_");
			}
			b.append(Character.toUpperCase(c));
		}
		return b.toString();
	}

	public static String getClassDescription(String className) throws ClassNotFoundException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Class name=").append(className).append("\n");

		Class aClass = Class.forName(className);
		for (int i = 0; i < aClass.getDeclaredFields().length; i++) {
			buffer.append(i + 1).append(":").append(aClass.getDeclaredFields()[i].getName() + " : " + aClass.getDeclaredFields()[i].getType().getName())
					.append("\n");
		}
		for (int i = 0; i < aClass.getDeclaredMethods().length; i++) {
			buffer.append(i + 1).append(":").append(aClass.getDeclaredMethods()[i].getName()).append("\n");
		}

		return buffer.toString();
	}

	public static void main(String[] args) throws Exception {
		JavaAssistUtility jau = new JavaAssistUtility();
		String className = User.class.getName();

		// jau.makeNeoDatisClass(className, "tmp");
		// System.out.println(getClassDescription(User.class.getName()+"2"));
		jau.makeNeoDatisMetaClass(className, "tmp", "meta");
	}

}
