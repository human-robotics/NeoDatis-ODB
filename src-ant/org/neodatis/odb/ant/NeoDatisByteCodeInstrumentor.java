/**
 * 
 */
package org.neodatis.odb.ant;


/**
 * @author olivier
 *
 */
public class NeoDatisByteCodeInstrumentor {
	protected JavaAssistUtility jau;
	
	public NeoDatisByteCodeInstrumentor(){
		this.jau = new JavaAssistUtility();
	}
	public void execute(String className) throws Exception{
		jau.makeNeoDatisClass(className,null);
	}

}
