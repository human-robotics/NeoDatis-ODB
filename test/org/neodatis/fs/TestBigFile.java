package org.neodatis.fs;

import org.neodatis.odb.core.layers.layer3.BytesFactory;

public class TestBigFile {
	public void testCreateBigFile(){
		String fileName = "file1.txt"; 
		String fsName = "/Volumes/Work/ndfs";
		
		NDFS fs = NDFSFactory.open(NDFSFactory.getConfig(fsName).setDebug(false));
		
		NdfsFile f1 = fs.getFile(".", fileName);
		
		long start = System.currentTimeMillis();
		int size = 1024 * 1024*1024;
		for(int i=0;i<size;i++){
			f1.append(BytesFactory.getBytes("test1".getBytes()));
			
			if(i%(1024*1024)==0){
				System.out.println(i + "   = time = " + (System.currentTimeMillis() - start));
				start = System.currentTimeMillis();
			}
		}
		
		
		f1.close();
		
		fs.close();
		
	}
	
	public static void main(String[] args) {
		new TestBigFile().testCreateBigFile();
	}

}
