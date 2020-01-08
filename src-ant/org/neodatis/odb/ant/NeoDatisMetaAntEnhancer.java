/**
 * 
 */
package org.neodatis.odb.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Reference;

/**
 * Generate meta class for value objects to use query fields as java constants
 * instead of string
 * 
 * @author olivier
 * 
 */
public class NeoDatisMetaAntEnhancer extends Java {

	private String msg;
	private List<FileSet> sets;
	private String location;
	private String meta;

	public NeoDatisMetaAntEnhancer() {
		super();
		sets = new ArrayList<FileSet>();
	}

	public void addFileset(FileSet fileset) {
		sets.add(fileset);
		System.out.println("adding normal " + fileset);
	}

	public void addConfiguredFileset(FileSet fileset) {
		sets.add(fileset);
		System.out.println("adding configured " + fileset.getDir() + " / " + fileset.getDirectoryScanner().getIncludedFiles());
		System.out.println(System.identityHashCode(sets));
		System.out.println("Configured on " + sets);
		System.out.println("Configured on " + sets.size());
	}

	public void setLocation(String location) {
		this.location = location;
	}

	// The method executing the task
	public void execute() throws BuildException {
		JavaAssistUtility jau = new JavaAssistUtility();

		System.out.println("Executing on " + sets.size());
		System.out.println(System.identityHashCode(sets));
		for (FileSet fs : sets) {
			System.out.println(" Executing on file set " + fs.getDir());
			String[] files = fs.getDirectoryScanner().getIncludedFiles();
			System.out.println(" Nb Included files " + files.length);
			for (String s : files) {
				String className = s.replace("/", ".").replace(".class", "");
				try {
					jau.makeNeoDatisMetaClass(className, location, "meta");
					System.out.println(className);
				} catch (Exception e) {
					throw new BuildException(e);
				}
			}
		}
	}

	// The setter for the "message" attribute
	public void setMessage(String msg) {
		this.msg = msg;
	}

	public void setMetaPackage(String meta) {
		this.meta = meta;
	}

	public void createFileset(FileSet fileset) {
		sets.add(fileset);
	}

	public void setClasspathRef(Reference r) {
		super.setClasspathRef(r);
		System.out.println("Setting classpath ref to " + r);
	}

	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("Need 2 arguments:\n\t <directory where to look for files> <meta package>");
			return;
		}
		String directory = args[0];
		String metaPackage = args[1];
		
		System.out.println("NeoDatis: Generating meta classes for directory "+ directory);
		
		JavaAssistUtility jau = new JavaAssistUtility();
		
		List<String> classNames = getAllJavaClasses(directory);
		for(String c:classNames){
			jau.makeNeoDatisMetaClass(c, directory, metaPackage);
		};
	}

	private static List<String> getAllJavaClasses(String directory) {
		File [] fs = new File(directory).listFiles();
		List<String> classes = new ArrayList<String>();
		for(File f:fs){
			if(f.isDirectory()){
				classes.addAll(getAllJavaClasses(f.getAbsolutePath()));
			}else{
				if(f.getAbsolutePath().endsWith(".java")){
					String className = f.getAbsolutePath().replace(".java", "");
					className.replace(directory,"");
					classes.add(className);
				}
			}
		}
		return null;
	}
}
