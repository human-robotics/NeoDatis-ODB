/**
 * 
 */
package org.neodatis.odb.core.server.message.serialization;

import org.neodatis.odb.ClassOid;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.layers.layer2.meta.ObjectInfoHeader;
import org.neodatis.odb.core.layers.layer3.Bytes;
import org.neodatis.odb.core.layers.layer3.BytesFactory;
import org.neodatis.odb.core.layers.layer3.BytesHelper;
import org.neodatis.odb.core.layers.layer3.ReadSize;
import org.neodatis.odb.core.server.message.GetObjectHeaderFromIdMessageResponse;
import org.neodatis.odb.core.server.message.Message;

/**
 * @author olivier
 *
 */
public class GetObjectHeaderFromOidMessageResponseSerializer extends SerializerAdapter{

	
	public GetObjectHeaderFromOidMessageResponseSerializer(NeoDatisConfig config){
		super(config);
	}

	public Message fromBytes(BytesHelper bytes) throws Exception {
		GetObjectHeaderFromIdMessageResponse message = new GetObjectHeaderFromIdMessageResponse();
		ReadSize readSize = new ReadSize();
		HeaderSerializer.fill(message, bytes, readSize);
		
		ObjectOid oid = bytes.readObjectOid(readSize.get(), readSize,"oid");
		ClassOid coid = bytes.readClassOid(readSize.get(), readSize,"coid");
		
		long creation = bytes.readLong(readSize.get(), readSize,"creation");
		long update = bytes.readLong(readSize.get(), readSize,"update");
		long objectVersion = bytes.readLong(readSize.get(), readSize,"objectversion");
		
		ObjectInfoHeader oih = new ObjectInfoHeader();
		oih.setOid(oid);
		oih.setClassInfoId(coid);
		oih.setCreationDate(creation);
		oih.setUpdateDate(update);
		oih.setObjectVersion(objectVersion);
		
		message.setOih(oih);
		return message;
	}

	public Bytes toBytes(Message message) {
		GetObjectHeaderFromIdMessageResponse m = (GetObjectHeaderFromIdMessageResponse) message;

		BytesHelper bytes = new BytesHelper(BytesFactory.getBytes(),getConfig());
		int position = HeaderSerializer.toBytes(bytes, message);
		ObjectInfoHeader oih = m.getOih();
		
		
		position+= bytes.writeObjectOid(oih.getOid(), position, "oid");
		position+= bytes.writeClassOid(oih.getClassInfoId(), position, "coid");
		
		position+= bytes.writeLong(oih.getCreationDate(), position, "creation");
		position+= bytes.writeLong(oih.getUpdateDate(), position, "update");
		position+= bytes.writeLong(oih.getObjectVersion(), position, "objectversion");
		
		return bytes.getBytes();
	}

}
