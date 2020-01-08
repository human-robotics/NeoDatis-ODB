package org.neodatis.odb.test.ext;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBServer;
import org.neodatis.odb.core.server.MessageStreamerImpl;
import org.neodatis.odb.core.server.message.SendFileMessageResponse;

public class SendFileToDeviceServerTest {
	public static void main(String[] args) {


		String host = "localhost";
		int port = 12001;
		
		NeoDatisConfig config = NeoDatis.getConfig().setHostAndPort(host,port);
		config.setClientServerSocketTimeout(10000);
		config.setDataBlockSize(1024);
		config.addLogId(MessageStreamerImpl.LOG_ID);

		final ODBServer server = NeoDatis.openServer(port,config);
		
		server.startServer(true);
		
		
		ODB odb = NeoDatis.openClient("base",config);
		System.out.println("Connected");
		
		//String localFileName,String remoteDirectory, String remoteFileName, boolean putFileInServerInbox, boolean saveFileToFileSystem
		//SendFileMessageResponse r = odb.ext().sendFile("files/test1.json", "pmv1", "test1.json", true, true);
		
		
//		SendFileMessageResponse r1 = odb.ext().sendFile("files/msg1.gif", "pmv/1/files", "msg1.gif", true, true);
//		SendFileMessageResponse r2 = odb.ext().sendFile("files/current.img.json", "pmv/1", "current.json", true, true);
		
		SendFileMessageResponse r1 = odb.ext().sendFile("files/video1.mp4", "pmv/1/files", "video1.mp4", true, true);
		//SendFileMessageResponse r2 = odb.ext().sendFile("files/current.video.json", "pmv/1", "current.json", true, true);

		odb.close();
		server.close();
		//System.out.println(r1.toString());
		//System.out.println(r2.toString());
	}
}
