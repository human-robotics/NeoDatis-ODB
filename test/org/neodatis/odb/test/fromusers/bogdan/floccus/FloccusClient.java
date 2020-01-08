/*
 * Floccus client
 */

package org.neodatis.odb.test.fromusers.bogdan.floccus;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;

/**
 *
 * @author bo
 */
public class FloccusClient {
    private ODB odb;
    private NeoDatisConfig dbProps;
    private String dbName;



    public FloccusClient(String dbName, String host, int port) {
        this.dbProps = NeoDatis.getConfig().setHostAndPort(host, port);
        this.dbName = dbName;
    }


    public FloccusClient(String dbName) {
        this(dbName, FloccusConstants.DEFAULT_HOST, FloccusConstants.DEFAULT_PORT);
    }


    public void open() {
        if (this.odb == null) {
            this.odb = NeoDatis.openClient(this.dbProps.getHost(), this.dbProps.getPort(), this.dbName);
        }
    }

    public void close() {
        if (this.odb != null) {
            this.odb.close();
            this.odb = null;
        }
    }


    /**
     * Store object.
     * 
     * @param obj
     */
    public synchronized void store(Object obj) {
        this.open();
        this.odb.store(obj);
    }


    /**
     * Get objects by a class.
     * 
     * @param klass
     * @return
     */
    public Objects getObjects(Class klass) {
        this.open();
        return this.odb.getObjects(klass);
    }
}
