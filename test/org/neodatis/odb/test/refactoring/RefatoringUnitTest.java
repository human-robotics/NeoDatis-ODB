/**
 * 
 */
package org.neodatis.odb.test.refactoring;

import junit.framework.TestCase;
import org.neodatis.odb.test.ODBTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author olivier
 * 
 */
public class RefatoringUnitTest extends TestCase {

	public static final String LINUX_CLASSPATH = " -cp bin:conf:target/neodatis-odb.jar:lib/others/javassist.jar:lib/junit-4.7.jar:lib/ext/je-3.3.82.jar";
	public static final String WINDOWS_CLASSPATH = " -cp bin;conf;target/neodatis-odb.jar;lib/others/javassist.jar;lib/junit-4.7.jar;lib/ext/je-3.3.82.jar";

	public Process exec(String command, String args) throws IOException, InterruptedException {
		String c = null;

		String os = System.getProperty("os.name");
		//System.out.println(os);
		if (os.indexOf("Windows") == -1) {
			c = command + LINUX_CLASSPATH +" "+ args;
		} else {
			c = command + WINDOWS_CLASSPATH+" "+ args;
		}
		System.out.println(c);
		Process p = Runtime.getRuntime().exec(c);

		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}

		StringBuffer error = new StringBuffer();

		in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		while ((line = in.readLine()) != null) {
			error.append(line).append("\n");
		}
		if (error.length() != 0) {
			System.out.println(error.toString());
			// there is an error
			throw new RuntimeException(error.toString());
		}
		p.waitFor();
		return p;
	}

	public void extTestStart() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.Result start");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring1_start() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest1 start");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring1_1() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest1 step1");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring1_2() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest1 step2");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring1_3() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest1 step3");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring1_4() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest1 step4");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring2_start() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest2 start");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring2_1() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest2 step1");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring2_2() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest2 step2");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring2_3() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest2 step3");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring2_4() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest2 step4");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring2_5() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest2 step5");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring2_6() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest2 step6");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring3_start() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest3 start");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring3_1() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest3 step1");
		assertEquals(0, p.exitValue());
	}

	public void extTestRefactoring3_2() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest3 step2");
		assertEquals(0, p.exitValue());
	}
	public void extTestRefactoring3_3() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.RefactoringTest3 step3");
		assertEquals(0, p.exitValue());
	}

	public void extTestEnd() throws Exception {
		Process p = exec("java", "org.neodatis.odb.test.refactoring.Result end");
		assertEquals(0, p.exitValue());
	}

	public void test1() throws Exception {
		int port = ODBTest.PORT;
		try{
			extTestStart();
			extTestRefactoring1_start();
			extTestRefactoring1_1();
			extTestRefactoring1_2();
			extTestRefactoring1_3();
			extTestRefactoring1_4();
			extTestRefactoring2_start();
			extTestRefactoring2_1();
			extTestRefactoring2_2();
			extTestRefactoring2_3();
			extTestRefactoring2_4();
			extTestRefactoring2_5();
			extTestRefactoring2_6();

			extTestRefactoring3_start();
			extTestRefactoring3_1();
			extTestRefactoring3_2();
			extTestRefactoring3_3();
			assertEquals(0, Result.getNbBadTests());
		}finally{
			ODBTest.PORT = port;
		}

	}

	public static void main(String[] args) throws Exception {
		RefatoringUnitTest tr1 = new RefatoringUnitTest();
		// tr1.start();

		tr1.extTestRefactoring3_start();

	}
}
