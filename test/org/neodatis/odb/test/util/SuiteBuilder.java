/**
 * 
 */
package org.neodatis.odb.test.util;

import junit.framework.TestCase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author olivier
 *
 */
public class SuiteBuilder {
	
	
	public void build(String testDirectory, String testSuitePackagename, String testSuiteClassName, String fullTestSuiteFileName) throws Exception{
		List<String> testCases = new ArrayList<String>();
		getAllTestCases(testDirectory, testDirectory, testCases);
		System.out.println(String.format("Building test suite with %d test cases",testCases.size()));
		buildTestSuite(testSuitePackagename, testSuiteClassName, fullTestSuiteFileName, testCases);
	}
	/**
	 * @param fullTestSuiteName
	 * @param testCases
	 */
	private void buildTestSuite(String testSuitePackagename, String testSuiteClassName, String fullTestSuiteName, List<String> testCases) throws Exception {
		InputStream is = SuiteBuilderUtil.class.getResourceAsStream("NeoDatisTestSuite.template");
		String stringTemplate = convertStreamToString(is);
		StringBuffer buffer = new StringBuffer();
		for(String s:testCases){
			buffer.append("\t\tsuite.addTest(new TestSuite(").append(s).append(".class));").append("\n");
		}
		stringTemplate = stringTemplate.replaceAll("@tests", buffer.toString());
		stringTemplate = stringTemplate.replaceAll("@package", testSuitePackagename);
		stringTemplate = stringTemplate.replaceAll("@class-name", testSuiteClassName);
		File f = new File(fullTestSuiteName);
		if(!f.exists()){
			f.getParentFile().mkdirs();
		}
		FileOutputStream stream = new FileOutputStream(fullTestSuiteName);
		stream.write(stringTemplate.getBytes());
		stream.close();
		System.out.println("Test Suite " + fullTestSuiteName+ " created");
	}
	public void getAllTestCases(String rootDirectory, String currentDirectory, List<String> testCases) throws Exception{
		File f = new File(currentDirectory);
		if(f.isDirectory()){
			File[] files = f.listFiles();
			for(int i=0;i<files.length;i++){
				if(files[i].isDirectory()){
					getAllTestCases(rootDirectory, files[i].getAbsolutePath(), testCases);
				}else{
					if(files[i].getAbsolutePath().endsWith(".java")){
						String className = files[i].getPath().replaceAll(".java", "");
						className = className.replaceAll(new File(rootDirectory).getAbsolutePath()+'/', "");
						className = className.replace('/', '.');
						// this is the class we are building now
						if(className.endsWith("NeoDatisTestSuite")){
							continue;
						}
						Class clazz = Class.forName(className);
						// Check if this is a testcase
						if( TestCase.class.isAssignableFrom(clazz)){
							testCases.add(className);
						}
					}
				}
			}
		}
	}
	
	private String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
		SuiteBuilder builder = new SuiteBuilder();
		String root = args[0];
		String packageName = args[1];
		String className = args[2];
		String fullName = args[3];
		
		//builder.build("src", "org.neodatis.odb.test", "NeoDatisTestSuite", "src/org/neodatis/odb/test/NeoDatisTestSuite.java");
		builder.build(root,packageName,className,fullName);
	}
}
