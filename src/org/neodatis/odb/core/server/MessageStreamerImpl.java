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
package org.neodatis.odb.core.server;

import org.apache.tools.ant.util.StringUtils;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.layers.layer3.*;
import org.neodatis.odb.core.server.message.Message;
import org.neodatis.odb.core.server.message.serialization.AllSerializers;
import org.neodatis.tool.DLogger;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author olivier
 */
public class MessageStreamerImpl implements MessageStreamer {
	private static final int RE_READ_TIMEOUT = 100000;
	protected AllSerializers serializer;
	protected DataConverter converter;
	protected Bytes bytesForDataSize;
	protected ReadSize readSize;
	protected NeoDatisConfig neoDatisConfig;
	protected int timeout;
	private OutputStream out;
	private InputStream in;
	private BufferedOutputStream bos;
	private BufferedInputStream bis;
	private Socket socket;
	private String host;
	private int port;
	private String name;
	private boolean isClosed;
	private boolean debug;

	public MessageStreamerImpl() {
	}

	public MessageStreamerImpl(Socket socket, NeoDatisConfig config)
			throws IOException {
		this.neoDatisConfig = config;
		this.socket = socket;
		
		out = socket.getOutputStream();
		in = socket.getInputStream();
		this.socket.setSoTimeout(timeout);
		bos = new BufferedOutputStream(out);
		bis = new BufferedInputStream(in);
		this.serializer = AllSerializers.getInstance(neoDatisConfig);
		converter = new DataConverterImpl(neoDatisConfig.debugLayers(),
				neoDatisConfig.getDatabaseCharacterEncoding(), neoDatisConfig);
		bytesForDataSize = BytesFactory.getBytes();
		readSize = new ReadSize();
		this.debug = config.isInfoEnabled(LOG_ID);
	}

	public void init(String host, int port, String name, NeoDatisConfig config) {
		this.host = host;
		this.port = port;
		this.name = name;
		this.neoDatisConfig = config;
		this.serializer = AllSerializers.getInstance(neoDatisConfig);
		converter = new DataConverterImpl(neoDatisConfig.debugLayers(),
				neoDatisConfig.getDatabaseCharacterEncoding(), neoDatisConfig);
		bytesForDataSize = BytesFactory.getBytes();
		readSize = new ReadSize();
		timeout = neoDatisConfig.getSocketTimeoutForClientSever();
		this.debug = config.isInfoEnabled(LOG_ID);
		initSocket();
	}

	private void initSocket() {
		if (socket == null) {
			try {
				socket = createSocket();
				socket.setKeepAlive(true);
				socket.setTcpNoDelay(true);
				out = socket.getOutputStream();
				socket.setSoTimeout(timeout);
				in = socket.getInputStream();
				bos = new BufferedOutputStream(out);
				bis = new BufferedInputStream(in);

			} catch (Exception e) {
				throw new NeoDatisRuntimeException(
						NeoDatisError.CLIENT_NET_ERROR, e);
			}
		}
		if (isClosed) {
			throw new NeoDatisRuntimeException(
					NeoDatisError.ODB_IS_CLOSED.addParameter(name));
		}
	}

	protected Socket createSocket() throws UnknownHostException, IOException {
		return new Socket(host, port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.impl.core.server.layers.layer3.engine.IMessageStreamer
	 * #close()
	 */
	public void close() {
		try {
			bos.flush();
			bos.close();
			bis.close();
			out.close();
			in.close();
			isClosed = true;
		} catch (Exception e) {
			throw new NeoDatisRuntimeException(NeoDatisError.CLIENT_NET_ERROR,
					e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.impl.core.server.layers.layer3.engine.IMessageStreamer
	 * #write(org.neodatis.odb.core.server.layers.layer3.engine.Message)
	 */
	public void write(Message message) {
		int packageSize = neoDatisConfig.getDataBlockSize();
		try {
			Bytes bytes = serializer.toBytes(message);
			// System.out.println("Writing bytes with size = " +
			// bytes.getRealSize());
			converter.intToByteArray(bytes.getRealSize(), bytesForDataSize, 0,
					"datasize");
			byte[] bb = bytesForDataSize.extract(0, 4);
			bos.write(bb);
			if(debug) {
				System.out.println("MessageStreamer:write:Writing " + bb.length + " bytes");
			}
			int totalSize = bytes.getByteArray().length;
			if (totalSize <= packageSize) {
				bos.write(bytes.getByteArray());
				bos.flush();
			} else {
				int n = 1;
				byte[] bytesToSend = bytes.getByteArray();
				// cut data in several packages
				int alreadySent = 0;
				while (alreadySent < totalSize) {
					int sizeToSendNow = packageSize;
					int leftToSend = totalSize - alreadySent;

					if (leftToSend < packageSize) {
						sizeToSendNow = leftToSend;
					}
					bos.write(bytesToSend, alreadySent, sizeToSendNow);
					alreadySent += sizeToSendNow;
					n++;
					if(debug) {
						DLogger.debug("MessageStreamer.write:"+ n + " : " + sizeToSendNow
							+ " bytes sent. Total is " + alreadySent + " | Total To Send="+totalSize);
					}
				}
				bos.flush();
			}
		} catch (Exception e) {
			throw new NeoDatisRuntimeException(
					NeoDatisError.NET_SERIALISATION_ERROR, e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.impl.core.server.layers.layer3.engine.IMessageStreamer
	 * #read()
	 */
	public Message read() throws Exception {
		byte[] bytes = new byte[neoDatisConfig.getDataBlockSize()];
		int size = 0;
		Bytes bb = null;

		byte[] bbb = new byte[4];
		int s = bis.read(bbb);
		bytesForDataSize.set(0, bbb);
		int dataSize = converter.byteArrayToInt(bytesForDataSize, 0, readSize,
				"datasize");

		if(debug) {
			DLogger.debug("MessageStreamer.read : Reading " + dataSize + " bytes");
		}

		size = bis.read(bytes);

		if (size != -1) {
			byte[] rbytes = new byte[size];
			System.arraycopy(bytes, 0, rbytes, 0, size);
			bb = BytesFactory.getBytes(rbytes);
		}

		int i = 0;
		long start = System.currentTimeMillis();
		while ((bis.available() != 0 && size != -1 && System
				.currentTimeMillis() < start + RE_READ_TIMEOUT)
				|| (bb != null && bb.getRealSize() != dataSize)) {
			size = bis.read(bytes);
			
			if(debug) {
				DLogger.debug("MessageStreamer.read:Reading " + i++ + " : size = " + size
						+ " | real size = " + bb.getRealSize()
						+ " | ToReceiveSize = " + dataSize + " | lwft="
						+ (dataSize - bb.getRealSize()));
			}

			if (size >0) {
				byte[] rbytes = new byte[size];
				try{
					System.arraycopy(bytes, 0, rbytes, 0, size);
				}catch(ArrayIndexOutOfBoundsException t) {
					DLogger.error("Exception "+ t.getMessage() +" | Source Size="+bytes.length+" | Dest Size = "+ rbytes.length+" | Size to copy="+ size);
					throw t;
				}
				bb.append(rbytes);
			} else {
				Thread.sleep(5);
				if(debug) {
					DLogger.debug("MessageStreamer.read:Sleeping 5ms because readSize is "+size);
				}
			}
		}
		if (bb == null) {
			throw new EOFException(
					"MessageFromLocalClient:Remote socket has been closed");
		}
		if (bb.getRealSize() != dataSize) {
			throw new IOException(
					"Error in transmission, sizes does not match : sent size = "
							+ dataSize + " , received size = "
							+ bb.getRealSize());
		}
		return serializer.fromBytes(bb);

	}

	public Message sendAndReceive(Message m) {
		long start = System.currentTimeMillis();
		Message rmsg;
		try {
			write(m);
			rmsg = read();
		} catch (SocketTimeoutException t) {
			DLogger.error("sendAndReceive to " + host + ":" + port
					+ " - Read Timeout after "
					+ (System.currentTimeMillis() - start)
					+ "ms waiting for response of a " + m.getClass().getName()
					+ " | Configured timeout is "
					+ neoDatisConfig.getSocketTimeoutForClientSever() + "ms");
			throw new NeoDatisRuntimeException(NeoDatisError.CLIENT_NET_ERROR,
					t);
		} catch (Exception e) {
			if (e instanceof NeoDatisRuntimeException) {
				throw (NeoDatisRuntimeException) e;
			}
			throw new NeoDatisRuntimeException(NeoDatisError.CLIENT_NET_ERROR,
					e);
		}

		return rmsg;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

}
