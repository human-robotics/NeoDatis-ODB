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
import org.neodatis.odb.core.server.message.SendFileMessageResponse;

/**
 * @author olivier
 *
 */
public class SendFileMessageResponseSerializer extends SerializerAdapter{

	public SendFileMessageResponseSerializer(NeoDatisConfig config) {
		super(config);
	}

	public Message fromBytes(BytesHelper bytes) throws Exception {
		SendFileMessageResponse message = new SendFileMessageResponse();
		ReadSize readSize = new ReadSize();
		HeaderSerializer.fill(message, bytes, readSize);

		boolean fileExist = bytes.readBoolean(readSize.get(), readSize, "file exist");
		long fileSize = bytes.readLong(readSize.get(), readSize, "file size");

		message.setFileExist(fileExist);
		message.setFileSize(fileSize);

		return message;
	}

	public Bytes toBytes(Message message) {
		SendFileMessageResponse m = (SendFileMessageResponse) message;

		BytesHelper bytes = new BytesHelper(BytesFactory.getBytes(), getConfig());
		int position = HeaderSerializer.toBytes(bytes, message);

		position += bytes.writeBoolean(m.fileExist(), position, "file exist");
		position += bytes.writeLong(m.getFileSize(), position, "file size");

		return bytes.getBytes();
	}
	

}
