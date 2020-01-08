/**
 * 
 */
package org.neodatis.odb.ant;

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
 * @author olivier
 * 
 */
public class NeoDatisAntEnhancer extends Java {

	private String msg;
	private List<FileSet> filesets;
	private String location;

	public NeoDatisAntEnhancer(){
		super();
		filesets = new ArrayList<FileSet>();
	}
	public void addFileset(FileSet fileset) {
		filesets.add(fileset);
		System.out.println("adding normal " + fileset);
	}

	public void addConfiguredFileset(FileSet fileset) {
		filesets.add(fileset);
		System.out.println("adding configured " + fileset.getDir()+ " / " + fileset.getDirectoryScanner().getIncludedFiles());
	}

	public void setLocation(String location) {
        this.location = location;
    }
	// The method executing the task
	public void execute() throws BuildException {
		NeoDatisByteCodeInstrumentor instrumentor = new NeoDatisByteCodeInstrumentor();
		try {
			CtClass c = ClassPool.getDefault().get(instrumentor.getClass().getName());
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for(FileSet fs : filesets){
			String[] files = fs.getDirectoryScanner().getIncludedFiles();
			for(String s:files){
				String className = s.replace("/", ".").replace(".class", "");
				try {
					//instrumentor.execute(className);
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
	
	public void createFileset(FileSet fileset){
		filesets.add(fileset);
	}
	public void setClasspathRef(Reference r) {
		super.setClasspathRef(r);
		System.out.println("Setting classpath ref to "+r );
	}

}
