/**
 * 
 */
package org.neodatis.odb.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neodatis.odb.ant.JavaAssistUtility;
import org.neodatis.tool.IOUtil;
import org.neodatis.tool.wrappers.OdbClassUtil;

/**
 * Generate meta class for value objects to use query fields as java constants
 * instead of string
 * 
 * @author olivier
 * 
 */
public class NeoDatisMetaClassesGenerator {

	public NeoDatisMetaClassesGenerator() {
		super();
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("Need 3 arguments:\n\t <base directory> <list of packages separated by , with no space> <meta package>");
			return;
		}
		String directory = args[0];
		String packages = args[1];
		String metaPackage = args[2];
		File f = new File(directory);

		if (!f.exists()) {
			System.err.println("Directory " + f.getAbsolutePath() + " does not exist");
			return;
		}
		System.out.println("NeoDatis: Generating meta classes for basedir " + f.getAbsolutePath() + "  and packages " + packages);

		JavaAssistUtility jau = new JavaAssistUtility();

		List<String> classNames = getAllJavaClasses(directory, packages);
		System.out.println("Building/Checking meta class for classes : "+ classNames);
		for (String c : classNames) {
			jau.makeNeoDatisMetaClass(c, directory, metaPackage);
		}
		
	}

	private static List<String> getAllJavaClasses(String directory, String packages) {
		File d = new File(directory);

		File[] fs = new File(directory).listFiles();
		List<String> classes = new ArrayList<String>();

		String[] okDirectories = packages.replace('.', '/').split(",");
		
		Map<String, String> packagesOk = new HashMap<String, String>();
		for(String s:packages.split(",")){
			packagesOk.put(s,s);
		}
		for (File f : fs) {
			System.out.println(f.getAbsolutePath());
			if (f.isDirectory()) {
				classes.addAll(getAllJavaClasses(f.getAbsolutePath(), packages));
			} else {
				if (f.getAbsolutePath().endsWith(".java")) {
					if (okDirectories.length == 0 || packageOk(f, okDirectories)) {
						String className = getClassName(f);
						String packageName = OdbClassUtil.getPackageName(className);
						if (packagesOk.containsKey(packageName)) {
							classes.add(className);
						}
					}
				}
			}
		}
		return classes;
	}

	private static boolean packageOk(File f, String[] okDirectories) {
		String compare = f.getAbsolutePath().replace("\\", "/"); 
		for (String p : okDirectories) {
			if (compare.indexOf(p) != -1) {
				return true;
			}
		}
		return false;
	}

	private static String getClassName(File f) {
		String s = IOUtil.getStringFromFile(f.getAbsolutePath());

		int i1 = s.indexOf("package ");
		int i2 = s.indexOf(";");
		if(i1<0 || i2<0){
			System.err.println("error while analysing java class "+ f.getAbsolutePath());
			return "error";
		}
		String packageName = s.substring(i1 + 8, i2);
		String className = f.getName().replace(".java", "");
		return packageName.replace('/', ',').replace('\\', '.') + "." + className;
	}
}
