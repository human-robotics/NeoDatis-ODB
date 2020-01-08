/**
 * 
 */
package org.neodatis.odb.core.server.message.serialization;

import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.core.layers.layer3.Bytes;
import org.neodatis.odb.core.layers.layer3.BytesFactory;
import org.neodatis.odb.core.layers.layer3.BytesHelper;
import org.neodatis.odb.core.layers.layer3.ReadSize;
import org.neodatis.odb.core.server.message.Message;
import org.neodatis.odb.core.server.message.process.RemoteProcessMessageResponse;
import org.neodatis.odb.core.server.message.process.RemoteProcessReturn;

import java.io.*;

/**
 * @author olivier
 * 
 */
public class RemoteProcessMessageResponseSerializer extends SerializerAdapter{

	public RemoteProcessMessageResponseSerializer(NeoDatisConfig config) {
		super(config);
	}

	public Message fromBytes(BytesHelper bytes) throws Exception {
		RemoteProcessMessageResponse message = new RemoteProcessMessageResponse();
		ReadSize readSize = new ReadSize();
		HeaderSerializer.fill(message, bytes, readSize);

		int size = bytes.readInt(readSize.get(), readSize, "size");
		
		byte[] bb = bytes.getBytes().extract(readSize.get(),size);
		ByteArrayInputStream bais = new ByteArrayInputStream(bb);
		ObjectInputStream ois = new ObjectInputStream(bais);
		RemoteProcessReturn processReturn = (RemoteProcessReturn) ois.readObject();
		message.setRemoteProcessReturn(processReturn);

		return message;
	}

	public Bytes toBytes(Message message) throws IOException {
		RemoteProcessMessageResponse m = (RemoteProcessMessageResponse) message;

		BytesHelper bytes = new BytesHelper(BytesFactory.getBytes(),getConfig());
		int position = HeaderSerializer.toBytes(bytes, message);

		// we use serialization
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(m.getRemoteProcessReturn());
		byte[] bb = baos.toByteArray();
		position += bytes.writeInt(bb.length, position, "size");
		bytes.getBytes().append(bb);
		
		return bytes.getBytes();

	}
}
