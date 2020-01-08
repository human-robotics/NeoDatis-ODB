/**
 * 
 */
package org.neodatis.odb.core.server.message.serialization;

import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.layers.layer3.Bytes;
import org.neodatis.odb.core.layers.layer3.BytesFactory;
import org.neodatis.odb.core.layers.layer3.BytesHelper;
import org.neodatis.odb.core.layers.layer3.ReadSize;
import org.neodatis.odb.core.server.message.GetObjectHeaderFromIdMessage;
import org.neodatis.odb.core.server.message.Message;

/**
 * @author olivier
 *
 */
public class GetObjectHeaderFromOidMessageSerializer extends SerializerAdapter{

	
	public GetObjectHeaderFromOidMessageSerializer(NeoDatisConfig config){
		super(config);
	}

	public Message fromBytes(BytesHelper bytes) throws Exception {
		GetObjectHeaderFromIdMessage message = new GetObjectHeaderFromIdMessage();
		ReadSize readSize = new ReadSize();
		HeaderSerializer.fill(message, bytes, readSize);
		
		ObjectOid oid = bytes.readObjectOid(readSize.get(), readSize,"oid");
		boolean useCache = bytes.readBoolean(readSize.get(), readSize, "usecache");
		message.setOid(oid);
		message.setUseCache(useCache);
		return message;
	}

	public Bytes toBytes(Message message) {
		GetObjectHeaderFromIdMessage m = (GetObjectHeaderFromIdMessage) message;

		BytesHelper bytes = new BytesHelper(BytesFactory.getBytes(),getConfig());
		int position = HeaderSerializer.toBytes(bytes, message);
		position+= bytes.writeObjectOid(m.getOid(), position, "oid");
		position+= bytes.writeBoolean(m.useCache(), position, "usecache");
		
		return bytes.getBytes();
	}

}
