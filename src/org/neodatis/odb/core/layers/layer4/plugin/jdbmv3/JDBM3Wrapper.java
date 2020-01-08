package org.neodatis.odb.core.layers.layer4.plugin.jdbmv3;

import org.apache.jdbm.DB;
import org.apache.jdbm.DBMaker;
import org.neodatis.odb.ClassOid;
import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.core.oid.StringOid;
import org.neodatis.tool.DLogger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;


public class JDBM3Wrapper {
    protected DB db;
    protected String directory;
    protected String file;

    public String CLASS_OID = "class-oid";
    public String STRING_OID = "string-oid";
    public String STRING = "string";
    protected boolean debug = false;

    public JDBM3Wrapper(String directoryName) throws IOException {

        File f = new File(directoryName);

        if (!f.exists() ) {
            f.mkdirs();
            //f.createNewFile();
        }

        if(debug){
            DLogger.info("JDBM3Wrapper -> Opening " + new File(directoryName).getAbsolutePath());
        }
        file = "neodatis";

        db = DBMaker.openFile(directoryName+"/"+file)
                //.deleteFilesAfterClose()
                //.enableEncryption("password", false)
                .make();
    }

    public Map getMap(String name) {
        Map map = db.getTreeMap(name);
        if (map == null) {
            map = db.createTreeMap(name);
        }
        return map;
    }

    public SortedMap<String, byte[]> getClassOidBtree(){
        return (SortedMap<String, byte[]>) getMap(CLASS_OID);
    }

    public void put(OID key, byte[] value) {
        if (key instanceof ObjectOid) {
            ObjectOid oid = (ObjectOid) key;
            SortedMap<String, byte[]> btree = (SortedMap<String, byte[]>) getMap(oid.getClassOid().oidToString());
            if (debug) {
                DLogger.info("inserting object data of oid " + key.oidToString() + " in store " + oid.getClassOid().oidToString());
            }
            btree.put(oid.oidToString(), (byte[]) value);
            return;
        }
        if (key instanceof StringOid) {
            StringOid oid = (StringOid) key;
            SortedMap<String, byte[]> btree = (SortedMap<String, byte[]>) getMap(STRING_OID);
            if (debug) {
                DLogger.info("inserting object data of oid " + key.oidToString() + " in store " + STRING_OID);
            }
            btree.put(oid.oidToString(), value);
            return;
        }
        if (key instanceof ClassOid) {
            ClassOid coid = (ClassOid) key;
            SortedMap<String, byte[]> btree = (SortedMap<String, byte[]>) getMap(CLASS_OID);

            if (debug) {
                DLogger.info("inserting class info data of oid " + key.oidToString() + " in store " + CLASS_OID);
            }

            btree.put(coid.oidToString(), (byte[]) value);
            return;
        }
        throw new RuntimeException("put:Unmanaged OID of type " + key.getClass().getName());
    }

    public boolean containsKey(OID key){
        return get(key) != null;
    }

    public Object get(OID key) {
        if (key instanceof ObjectOid) {
            ObjectOid oid = (ObjectOid) key;
            SortedMap<String, byte[]> btree = (SortedMap<String, byte[]>) getMap(oid.getClassOid().oidToString());
            if (debug) {
                DLogger.info("reading object data of oid " + key.oidToString() + " in store " + oid.getClassOid().oidToString());
            }

            return btree.get(oid.oidToString());
        }
        if (key instanceof StringOid) {
            StringOid oid = (StringOid) key;
            SortedMap<String, String> btree = (SortedMap<String, String>) getMap(STRING_OID);
            if (debug) {
                DLogger.info("reading object data of oid " + key.oidToString() + " in store " + STRING_OID);
            }

            return btree.get(oid.oidToString());
        }
        if (key instanceof ClassOid) {
            ClassOid coid = (ClassOid) key;
            SortedMap<String, byte[]> btree = (SortedMap<String, byte[]>) getMap(CLASS_OID);
            if (debug) {
                DLogger.info("reading class info data of oid " + key.oidToString() + " in store " + CLASS_OID);
            }
            return btree.get(coid.oidToString());
        }
        throw new RuntimeException("get:Unmanaged OID of type " + key.getClass().getName());
    }

    public Object remove(OID key) {
        if (key instanceof ObjectOid) {
            ObjectOid oid = (ObjectOid) key;
            SortedMap<String, byte[]> btree = (SortedMap<String, byte[]>) getMap(oid.getClassOid().oidToString());
            return btree.remove(oid.oidToString());
        }
        if (key instanceof ClassOid) {
            ClassOid coid = (ClassOid) key;
            SortedMap<String, byte[]> btree = (SortedMap<String, byte[]>) getMap(CLASS_OID);
            return btree.remove(key.oidToString());
        }
        return null;
    }

    public boolean existOid(OID key) {
        if (key instanceof ObjectOid) {
            ObjectOid oid = (ObjectOid) key;
            SortedMap<String, byte[]> btree = (SortedMap<String, byte[]>) getMap(oid.getClassOid().oidToString());
            return btree.containsKey(key.oidToString());
        }
        if (key instanceof ClassOid) {
            ClassOid coid = (ClassOid) key;
            SortedMap<String, byte[]> btree = (SortedMap<String, byte[]>) getMap(CLASS_OID);
            return btree.containsKey(key.oidToString());
        }
        // TODO Check if we must check STRING_OID too
        return false;
    }

    public void commitAndClose() {
        db.commit();
        db.close();
    }

    public void commit() {
        db.commit();
    }

    public void rollback() {
        db.rollback();
    }

    public void close() {
        db.close();
    }

    public String getDirectory() {
        return directory;
    }

    public String getFile() {
        return file;
    }


}
