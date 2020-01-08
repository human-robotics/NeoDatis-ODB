/**
 * 
 */
package org.neodatis.odb.test.server.message;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.core.server.message.SendFileMessage;
import org.neodatis.odb.core.server.message.SendFileMessageResponse;
import org.neodatis.odb.test.ODBTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author olivier
 *
 */
public class TestSendFile extends ODBTest{
	public void test1() throws IOException{
		
		// gets the base name
		String baseName = getBaseName();
		int port = 15000;

		// Builds a config instance for the server
		NeoDatisConfig config = NeoDatis.getConfig().setInboxDirectory("unit-test-data/inbox").setBaseDirectory("unit-test-data");
		
		// Creates and starts the server
		ODBServer server = NeoDatis.openServer(port,config);
		server.setAutomaticallyCreateDatabase(true);
		server.startServer(true);
		
		try{
			// Opens the client
			ODB odb = NeoDatis.openClient("localhost", port, baseName);

			// creates a simple file
			String s = "This is a test string!";
			String localFileName = DIRECTORY+ baseName+".txt";
			FileOutputStream fos = new FileOutputStream(localFileName);
			fos.write(s.getBytes());
			fos.close();
			// keep the size
			long size = new File(localFileName).length();
			
			// sends the file
			SendFileMessageResponse fmr = odb.ext().sendFile(localFileName, "f1.txt" , true);
			odb.close();
			
			// check file size
			assertTrue(fmr.fileExist());
			assertEquals(size, fmr.getFileSize());
			
		}finally{
			server.close();
		}
		
	}
	
	public void testBigFile() throws IOException{
		String baseName = getBaseName();
		int port = 15000;
		NeoDatisConfig config = NeoDatis.getConfig().setInboxDirectory("unit-test-data/inbox").setBaseDirectory("unit-test-data");
		ODBServer server = NeoDatis.openServer(port,config);
		server.setAutomaticallyCreateDatabase(true);
		server.startServer(true);

		try{
			ODB odb = NeoDatis.openClient("localhost", port, baseName);
			// creates a simple file
			String s = "This is a test string!";
			String localFileName = DIRECTORY+baseName+".txt";
			FileOutputStream fos = new FileOutputStream(localFileName);
			int n = 100000;
			for(int i=0;i<n;i++){
				fos.write(s.getBytes());
			}
			fos.close();
			long size = new File(localFileName).length();
			println("file size is " + size);
			SendFileMessageResponse fmr = (SendFileMessageResponse) odb.ext().sendMessage(new SendFileMessage(localFileName,"f1.txt",true));
			
			assertTrue(fmr.fileExist());
			assertEquals(size, fmr.getFileSize());
			odb.close();
		}finally{
			server.close();
		}
		
	}

}
