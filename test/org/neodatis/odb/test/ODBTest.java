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
package org.neodatis.odb.test;


import junit.framework.TestCase;
import org.neodatis.odb.*;
import org.neodatis.odb.core.session.cross.CrossSessionCache;
import org.neodatis.odb.test.plugins.jdbm.JDBMTestWrapper;
import org.neodatis.tool.IOUtil;
import org.neodatis.tool.wrappers.io.OdbFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @sharpen.ignore
 * @author olivier
 * 
 */
public class ODBTest extends TestCase {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");

	public static boolean loadPropertiesFromFile = false;
	public static boolean isLocal = true;
	public static boolean useSameVmOptimization = false;
	public static boolean startServerAutomatically = false;

	//public static String HOST = "neodatis.4java.ca";
	//public static String HOST = "192.168.0.160";
	public static String HOST = "localhost";
	public static int PORT = 13000;
	public static String DIRECTORY = "unit-test-data/";

	public static boolean runAll = false;
	public static boolean print = true;
	public static Class testWrapperClass = null;

	/**
	 * as we use junit to test new feature and they may not be implemented yet,
	 * there is a way to disable testing them
	 */
	public static boolean testNewFeature = false;
	/**
	 * as we use junit for performance test and they depend on the computer on
	 * which they are run, there is a way to disable testing them
	 */
	public static boolean testPerformance = false;
	/**
	 * Some bugs are know but have a low priority because we can live with them
	 * so they may not be resolved now, so we can use with property to avoid
	 * executing theses tests
	 */
	public static boolean testKnownProblems = false;

	public static boolean cryptoOn = false;
	public static String cryptoPassword = "blabla";

	private static ODBServer server = null;

	private String name;
	protected static NeoDatisConfig testConfig = NeoDatis.getConfig().setBaseDirectory(DIRECTORY).setTransactional(true); 
	private UnitTestWrapper testWrapper;
	private String baseName;

	static {
		testConfig.setHostAndPort("localhost",PORT);
		if (loadPropertiesFromFile) {
			Properties properties = new Properties();
			String propertyFileName = null;
			try {
				propertyFileName = System.getProperty("test.property.file", "/test.properties");
				InputStream is = String.class.getResourceAsStream(propertyFileName);
				if(is==null){
					System.out.println("Java class path : "+ System.getProperty("java.class.path"));
				}
				
				properties.load(is);
				String testSuiteInitializationClassName = properties.getProperty("test-suite-initialization-class");
				String testWrapperClassName = properties.getProperty("test-wrapper-class");
				
				String mode = properties.getProperty("mode");

				try{
					Class clazz = Class.forName(testSuiteInitializationClassName);
					SuiteInitialization startup = (SuiteInitialization) clazz.newInstance();
					startup.init();
				}catch (Exception e) {
					System.err.println("Error while executing test suite initialization class " + testSuiteInitializationClassName);
					e.printStackTrace();
				}
				
				try{
					testWrapperClass = Class.forName(testWrapperClassName);	
				}catch (Exception e) {
					System.err.println("Error while executing test wrapper class " + testSuiteInitializationClassName);
					e.printStackTrace();
				}
				
				
				if (mode.equals("local")) {
					isLocal = true;
				}
				if (mode.equals("same-vm-cs")) {
					isLocal = false;
					startServerAutomatically = true;
					useSameVmOptimization = true;
				}
				if (mode.equals("cs")) {
					isLocal = false;
					startServerAutomatically = false;
					useSameVmOptimization = false;
				}

				System.out.println(String.format(" NeoDatis Test Mode = %s, property file = %s, engine=%s", mode, propertyFileName,testWrapperClassName));
			} catch (Exception e) {
				System.err.println("Error while loading test properties from " + propertyFileName);
				e.printStackTrace();
			}
			ODBTest.testConfig.setStorageEngineClass(NeoDatisGlobalConfig.get().getStorageEngineClass());

		}else{
            testWrapperClass = JDBMTestWrapper.class;
        }
	}

	public ODBTest() {
		super();
	}
	
	/**
	 * @param name2
	 */
	public ODBTest(String name2) {
		super(name2);
	}

	public void setUp() throws Exception {
		CrossSessionCache.clearAll();

		resetBaseName();
		try {
			testWrapper = (UnitTestWrapper) testWrapperClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		testWrapper.start(baseName, testConfig);
	}
	protected void resetBaseName() {
		baseName = DIRECTORY+ "ztest_"+ getClass().getSimpleName()+"."+getName() + "." + System.currentTimeMillis() + ".neodatis";
		name = getClass().getName();
	}

	protected void tearDown() throws Exception {
		if(testWrapper==null){
			System.err.println("********************  test wrapper is null in " + name + " test");
		}
		testWrapper.end();
	}

	public ODB open(String fileName, NeoDatisConfig config) {

		config.setBaseDirectory(testConfig.getBaseDirectory());
		if (cryptoOn) {
			// OdbConfiguration.setIOClass(AesMd5Cypher.class, cryptoPassword);
		} else {
			// OdbConfiguration.setIOClass(OdbFileIO.class, null);
		}

		if (isLocal) {
			return NeoDatis.open(fileName, config);
		}
		return openClient(fileName, config);
	}

	public ODB open(String fileName) {
		return open(fileName, testConfig);
	}
	public ODB openLocal(String fileName) {
		return open(DIRECTORY + fileName, testConfig);
	}

	public ODBServer openServer(int port) {
		ODBServer s = NeoDatis.openServer(port);
		return s;
	}

	public ODB openClient(String baseIdentifier, NeoDatisConfig config) {
		if (startServerAutomatically) {
			startServer();
		}
		if (useSameVmOptimization) {
			return server.openClient(DIRECTORY + baseIdentifier);
		}

		
		return NeoDatis.openClient(DIRECTORY + baseIdentifier, config);
	}

	public ODB openClient(String host, int port, String baseIdentifier, String user, String password) {
		testConfig.setHostAndPort(host, port).setUser(user).setPassword(password);
		return openClient(baseIdentifier, testConfig);
		
	}

	public void failCS() {
		assertTrue(true);
		// fail("Native query not supported in Client/ServerMode");
	}

	protected void failNotImplemented(String string) {
		// fail(string + " not implemented in Client/ServerMode");
		assertTrue(true);
	}

	/**
	 * The deleteBase even in SameVmClientServerMode user socket to reach the
	 * server
	 * 
	 * @param baseName
	 */
	public boolean deleteBase(String baseName) {
		if (isLocal) {
			String s = DIRECTORY + baseName;
			return !(new OdbFile(s).exists()) || IOUtil.deleteDirectory(s);
		} else {
			if (startServerAutomatically) {
				startServer();
			}
			/*
			 * ServerAdmin sa = new ServerAdmin(HOST, PORT); DeleteBaseMessage
			 * message = new DeleteBaseMessage(DIRECTORY + baseName);
			 * DeleteBaseMessageResponse rmessage = (DeleteBaseMessageResponse)
			 * sa.sendMessage(message); if (rmessage.hasError()) { throw new
			 * ODBRuntimeException
			 * (NeoDatisError.SERVER_ERROR.addParameter(rmessage.getError())); }
			 */
			// if(startServerAutomatically){
			// server.close();
			// server = null;
			// }
			return true;
		}
	}

	public void t1estzzzz() {

	}

	public void print(Object o) {
		if (print) {
			System.out.print(o);
		}
	}

	public void println(Object o) {
		if (print) {
			System.out.println(o);
		}
	}

	public void println(long l) {
		if (print) {

			System.out.println(l);
		}
	}

	public void println(int i) {
		if (print) {

			System.out.println(i);
		}
	}

	public void println(float i) {
		if (print) {
			System.out.println(i);
		}
	}

	public void println(double i) {
		if (print) {
			System.out.println(i);
		}
	}

	public void startServer() {
		if (server == null) {
			println("+++++++ ODB TEST : Starting server on port " + PORT);
			server = openServer(ODBTest.PORT);
			server.setAutomaticallyCreateDatabase(true);
			// LogUtil.allOn(true);
			server.startServer(true);

		}
	}

	public String getBaseName() {
		return baseName;
	}

	public static String getHOST() {
		return HOST;
	}

	public static void setHOST(String host) {
		HOST = host;
	}

	public static String getDIRECTORY() {
		return DIRECTORY;
	}

	public static void setDIRECTORY(String directory) {
		DIRECTORY = directory;
	}

	public static int getPORT() {
		return PORT;
	}

	public static void setPORT(int port) {
		PORT = port;
	}


	/**
	 * 
	 */
	public void closeServer() {
		if (server != null) {
			println("Closing server on port " + PORT);
			server.close();
			server = null;
		} else {
			println("NOT Closing server on port " + PORT + " because it is null");
		}

	}
}
