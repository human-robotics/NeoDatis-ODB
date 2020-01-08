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
package org.neodatis.odb.core.layers.layer3.buffer;

import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.tool.DLogger;
import org.neodatis.tool.IOUtil;
import org.neodatis.tool.wrappers.OdbString;
import org.neodatis.tool.wrappers.OdbSystem;
import org.neodatis.tool.wrappers.OdbThread;
import org.neodatis.tool.wrappers.io.OdbFile;

import java.io.IOException;

/**
 * A buffer manager that can manage more than one buffer. Number of buffers can
 * be configured using Configuration.setNbBuffers().
 * 
 * @author osmadja
 * 
 */
public class MultiBufferedFileIO extends MultiBufferedIO {
	private static final String LOG_ID = "MultiBufferedFileIO";

	private IO fileWriter;

	static public int nbcalls = 0;

	static public int nbdiffcalls = 0;
	private String wholeFileName;
	private boolean debug;
	
	public MultiBufferedFileIO(int nbBuffers, String name, String fileName, boolean canWrite, int bufferSize, boolean debug) {
		super(nbBuffers, name, bufferSize, canWrite,debug);
		init(fileName, canWrite);
	}

	private void init(String fileName, boolean canWrite) {
		String dataDirectory = OdbSystem.getProperty("data.directory");
		if (dataDirectory != null) {
			wholeFileName = dataDirectory + "/" + fileName;
		} else {
			wholeFileName = fileName;
		}
		
		try {
			if (debug){
				DLogger.info("Opening datatbase file : "+ new OdbFile(wholeFileName).getFullPath());
			}
			fileWriter = buildFileWriter(canWrite);
			setIoDeviceLength(fileWriter.length());

			if (canWrite) {
				try {
					fileWriter.lockFile();
				} catch (Exception e) {
					// The file region is already locked
					throw new NeoDatisRuntimeException(NeoDatisError.ODB_FILE_IS_LOCKED_BY_CURRENT_VIRTUAL_MACHINE.addParameter(wholeFileName).addParameter(
							OdbThread.getCurrentThreadName()).addParameter(String.valueOf(false)),e);
				}
				if (!fileWriter.isLocked()) {
					throw new NeoDatisRuntimeException(NeoDatisError.ODB_FILE_IS_LOCKED_BY_EXTERNAL_PROGRAM.addParameter(wholeFileName).addParameter(
							OdbThread.getCurrentThreadName()).addParameter(String.valueOf(false)));
				}
			}

		} catch (Exception e) {
			//fixme
			throw new NeoDatisRuntimeException(NeoDatisError.INTERNAL_ERROR,e);
		}
		
	}
	
	protected IO buildFileWriter(boolean canWrite) throws IOException{
	
		try{
			// Gets the IO class configured in ODBConfiguration
			//Class ioclass = OdbConfiguration.getIOClass();
			// Creates an instance
			//IO io = (IO) ioclass.newInstance();
			IO io = new OdbFileIO();
			// initialize the instance
			io.init(wholeFileName, canWrite, null);
			return io;
		}catch(Exception e){
			throw new NeoDatisRuntimeException(NeoDatisError.INTERNAL_ERROR,e);
		}
	}

	public void goToPosition(long position) {
		try {
			if (position < 0) {
				System.out.println("debug: "+position);
				throw new NeoDatisRuntimeException(NeoDatisError.NEGATIVE_POSITION.addParameter(position));
			}
			fileWriter.seek(position);
		} catch (IOException e) {
			long l = -1;
			try {
				l = fileWriter.length();
			} catch (IOException e1) {
			}
			throw new NeoDatisRuntimeException(NeoDatisError.GO_TO_POSITION.addParameter(position).addParameter(l), e);
		}
	}

	public long getLength() {
		nbcalls++;
		return getIoDeviceLength();
		/*
		try {
			return fileWriter.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new NeoDatisRuntimeException(e);
		}*/
	}

	public void internalWrite(byte b) {
		try {
			fileWriter.write(b);
		} catch (IOException e) {
			throw new NeoDatisRuntimeException(e, "Error while writing a byte");
		}
	}

	public void internalWrite(byte[] bs, int size) {
		try {
			fileWriter.write(bs, 0, size);
		} catch (IOException e) {
			throw new NeoDatisRuntimeException(e, "Error while writing an array of byte");
		}
	}

	public byte internalRead() {
		int b;
		try {
			b = fileWriter.read();
			if (b == -1) {
				throw new IOException("Enf of file");
			}
			return (byte) b;
		} catch (IOException e) {
			throw new NeoDatisRuntimeException(e, "Error while reading a byte");
		}

	}

	public long internalRead(byte[] array, int size) {
		// FIXME raf.read only returns int not long
		try {
			return fileWriter.read(array, 0, size);
		} catch (IOException e) {
			throw new NeoDatisRuntimeException(e, "Error while reading an array of byte");
		}
	}

	public void closeIO() {

		try {
			if (debug) {
				DLogger.debug("Closing file with size " + fileWriter.length());
			}
			// Problem found by mayworm : necessary for MacOSX
			if (fileWriter.isLocked()) {
				fileWriter.unlockFile();
			}
			fileWriter.close();
		} catch (IOException e) {
			DLogger.error(OdbString.exceptionToString(e, true));
		}
		fileWriter = null;
		if (isForTransaction() && automaticDeleteIsEnabled()) {
			boolean b = IOUtil.deleteFile(wholeFileName);
			if (!b) {
				throw new NeoDatisRuntimeException(NeoDatisError.CAN_NOT_DELETE_FILE.addParameter(wholeFileName));
			}
		}
		// The file lock is automatically released closing the raf object
	}

	public void clear() {
		super.clear();

	}

	public boolean delete() {
		return IOUtil.deleteFile(wholeFileName);
	}

	public void flushIO() throws IOException {
		fileWriter.flushIO();
	}

}
