package org.neodatis.odb.core.layers.layer4.plugin.jdbmv3;

import org.neodatis.odb.ClassOid;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.OID;
import org.neodatis.odb.core.layers.layer3.Bytes;
import org.neodatis.odb.core.layers.layer3.BytesImpl2;
import org.neodatis.odb.core.layers.layer3.OidAndBytes;
import org.neodatis.odb.core.layers.layer4.ClassOidIterator;
import org.neodatis.odb.core.layers.layer4.ObjectOidIterator;
import org.neodatis.odb.core.layers.layer4.OidGenerator;
import org.neodatis.odb.core.layers.layer4.StorageEngineAdapter;
import org.neodatis.tool.DLogger;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;

public class JDBM3Plugin extends StorageEngineAdapter{
    protected boolean debug;
    protected JDBM3Wrapper jdbm;
    protected NeoDatisConfig config;
    public void open(String baseName, NeoDatisConfig config) {
        this.config = config;
        try {
            jdbm = new JDBM3Wrapper(baseName);
        } catch (IOException e) {
            throw new NeoDatisRuntimeException("Unable to open JDBM3 database "+ baseName);
        }
    }

    public void open(String host, int port, String baseName, NeoDatisConfig config) {
        throw new UnsupportedOperationException("JDBM plugin does not support remote access");
    }

    public void commit() {
        jdbm.commit();
    }

    public void rollback() {
        jdbm.rollback();
    }

    public void close() {
        jdbm.close();
    }

    public OidAndBytes read(OID oid, boolean useCache) {
        try {
            Object o = jdbm.get(oid);
            byte[] data = (byte[]) o;
            //Bytes data = (Bytes) pHashMap.get(oid);
            if (data == null) {
                if (debug) {
                    DLogger.info("Reading OID " + oid.oidToString() + " = null");
                }
                return null;
            }
            OidAndBytes oab = new OidAndBytes(oid, new BytesImpl2(data));
            if (debug) {
                DLogger.info("Reading OID " + oid.oidToString() + " | bytes = " + oab.bytes);
            }
            return oab;
        } catch (Exception e) {
            throw new NeoDatisRuntimeException(e, "Error while reading data for oid " + oid.oidToString());
        }
    }

    public boolean existOid(OID oid) {
        return jdbm.existOid(oid);
    }

    public void write(OidAndBytes oidAndBytes) {
        jdbm.put(oidAndBytes.oid,oidAndBytes.bytes.getByteArray());
    }

    public ObjectOidIterator getObjectOidIterator(ClassOid classOid, ObjectOidIterator.Way way) {
        return new Jdbm3ObjectOidIterator((SortedMap<String, byte[]>) jdbm.getMap(classOid.oidToString()),getOidGenerator());
    }

    public ClassOidIterator getClassOidIterator() {
        return new Jdbm3ClassOidIterator(jdbm.getClassOidBtree(),getOidGenerator());
    }

    public void deleteObjectWithOid(OID oid) {
        jdbm.remove(oid);
    }

    public String getEngineDirectoryForBaseName(String theBaseName) {

        return new File(theBaseName).getParentFile().getAbsolutePath();
    }

    public String getStorageEngineName() {
        return "jdbmv3";
    }

    public boolean useDirectory() {
        return true;
    }
}
