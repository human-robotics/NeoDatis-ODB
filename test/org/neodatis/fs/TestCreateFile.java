package org.neodatis.fs;

import org.neodatis.fs.transaction.NdfsTransaction;
import org.neodatis.odb.core.layers.layer3.Bytes;
import org.neodatis.odb.core.layers.layer3.BytesFactory;
import org.neodatis.odb.test.ODBTest;

import java.util.Collection;

public class TestCreateFile extends ODBTest{
	
	/** creates a simple file system with one file
	 * 
	 */
	public void test1(){
		String fileName = "file1.txt"; 
		String fsName = getBaseName();
		
		NDFS fs = NDFSFactory.open(NDFSFactory.getConfig(fsName).setDebug(true));
		
		NdfsFile f1 = fs.getFile(".", fileName);
		f1.append(BytesFactory.getBytes("test1".getBytes()));
		f1.close();
		
		fs.close();
		
		fs = NDFSFactory.open(NDFSFactory.getConfig(fsName));

		Collection<String> files = fs.getFileNames();
		fs.close();
		System.out.println(files);
		
		assertFalse(files.isEmpty());
		assertTrue(files.contains(fileName));
		
		
		
	}

	/** creates a simple file system with one file
	 * 
	 */
	public void test1WithTransaction(){
		String fileName = "file1.txt"; 
		String fsName = getBaseName();
		
		NDFS fs = NDFSFactory.open(NDFSFactory.getConfig(fsName).setDebug(true));
		NdfsTransaction t = fs.startTransaction();
		
		NdfsFile f1 = t.getFile(".", fileName);
		f1.append(BytesFactory.getBytes("test1".getBytes()));
		f1.close();
		
		t.commit();
		fs.close();
		
		fs = NDFSFactory.open(NDFSFactory.getConfig(fsName));

		Collection<String> files = fs.getFileNames();
		fs.close();
		System.out.println(files);
		
		assertFalse(files.isEmpty());
		assertTrue(files.contains(fileName));
		
	}
	
	/** creates a simple file system with one file, puts the text 'text1' and save, Then re-open the file, add 'text2' and rollback the transaction, then check that text2 has not been saved
	 * 
	 */
	public void testRollback(){
		String fileName = "file1.txt"; 
		String fsName = getBaseName();
		
		NDFS fs = NDFSFactory.open(NDFSFactory.getConfig(fsName).setDebug(true));
		NdfsTransaction transaction1 = fs.startTransaction();
		
		Bytes bytes1 = BytesFactory.getBytes("test1".getBytes()); 
		Bytes bytes2 = BytesFactory.getBytes("test12345".getBytes());
		
		NdfsFile f1 = transaction1.getFile(".", fileName);
		f1.append(bytes1);
		f1.close();
		transaction1.commit();
		
		NdfsTransaction transaction2 = fs.startTransaction();

		NdfsFile f2 = transaction2.getFile(".", fileName);
		
		// check size
		assertEquals(bytes1.getRealSize(), f2.getLength());
		
		f2.append(bytes2);
		f1.close();
		transaction2.rollback();
		
		fs.close();
		
		fs = NDFSFactory.open(NDFSFactory.getConfig(fsName));
		
		NdfsFile f3 = fs.getFile(".", fileName);

		assertEquals(bytes1.getRealSize(), f3.getLength());
		
		Bytes bytes3 = f3.readAll();
		String s = new String(bytes3.getByteArray());
		
		assertEquals("test1", s);
		
	}

}
