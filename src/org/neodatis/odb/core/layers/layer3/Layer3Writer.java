/**
 * 
 */
package org.neodatis.odb.core.layers.layer3;

import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo;
import org.neodatis.odb.core.session.info.DatabaseInfo;
import org.neodatis.tool.wrappers.list.IOdbList;

import java.net.UnknownHostException;

/**
 * @author olivier
 *
 */
public interface Layer3Writer {
	IOdbList<OidAndBytes>  metaToBytes(NonNativeObjectInfo nnoi);
	IOdbList<OidAndBytes>  classInfoToBytes(ClassInfo ci);
	Bytes buildDatabaseHeaderBytes(DatabaseInfo di);
	/** Writes the id of the opening and the datatime
	 * 
	 * @return
	 */
	public Bytes buildDatabaseLastCloseBytes(long openId);
	/** Writes the id of the opening and the datatime
	 * 
	 * @return
	 * @throws UnknownHostException 
	 */
	public Bytes buildDatabaseLastOpenBytes(long openId);
}
