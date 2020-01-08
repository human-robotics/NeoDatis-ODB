/*
 * FloccusServer for the appserver node.
 */

package org.neodatis.odb.test.fromusers.bogdan.floccus;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODBServer;

/**
 *
 * @author bo
 */
public class FloccusServer {
    private String dbName;
    private Integer dbPort;
    private NeoDatisConfig config;
    private ODBServer server;
    private boolean isStarted;


    public FloccusServer(String dbName) {
        this(dbName, FloccusConstants.DEFAULT_HOST, FloccusConstants.DEFAULT_PORT);
    }


    public FloccusServer(String dbName, String host, Integer port) {
        this.dbName = dbName;
        this.dbPort = port;
        this.server = NeoDatis.openServer(this.dbPort);
        this.server.addBase(this.dbName, this.dbName);
        this.isStarted = false;
    }


    /**
     * Stop storage server.
     */
    public void stop() {
        if (this.isStarted) {
            this.server.startServer(false);
            this.isStarted = false;
        }
    }

    /**
     * Start storage server.
     */
    public void start() {
        if (!this.isStarted) {
            System.err.println("Starting server...");
            this.server.startServer(true);
            this.isStarted = true;
            System.err.println("Started.");
        }
    }
}
