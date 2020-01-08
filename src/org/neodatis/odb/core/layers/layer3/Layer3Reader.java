/**
 * 
 */
package org.neodatis.odb.core.layers.layer3;

import org.neodatis.odb.OID;
import org.neodatis.odb.core.layers.layer2.meta.AttributeValuesMap;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo;
import org.neodatis.odb.core.session.info.DatabaseInfo;
import org.neodatis.odb.core.session.info.OpenCloseInfo;
import org.neodatis.tool.wrappers.list.IOdbList;

import java.util.HashSet;
import java.util.Map;

/**
 * @author olivier
 *
 */
public interface Layer3Reader {
	NonNativeObjectInfo metaFromBytes(OidAndBytes oidAndBytes, boolean full, int depth);
	ClassInfo classInfoFromBytes(OidAndBytes oidAndBytes, boolean full);

	AttributeValuesMap valuesFromBytes(OidAndBytes oab, HashSet<String> attributeNames, int depth);
	public NonNativeObjectInfo metaFromBytes(IOdbList<OidAndBytes> oabs, boolean full, Map<OID, OID> oidsToReplace, int depth);
	DatabaseInfo readDatabaseHeader(OidAndBytes oab);
	OpenCloseInfo readDatabaseOpenCloseInfo(OidAndBytes oabOpen, OidAndBytes oabClose);
}
