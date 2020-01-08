package org.neodatis.tool.wrappers.net;

import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.core.NeoDatisError;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * A simple wrapper to get ipo address
 * @author olivier
 * @sharpen.ignore
 *
 */
public class NeoDatisIpAddress {
		public static String get(String hostName){
			try {
				String ip = InetAddress.getByName(hostName).getHostAddress();
				return ip;
			} catch (UnknownHostException e) {
				throw new NeoDatisRuntimeException(NeoDatisError.ERROR_WHILE_GETTING_IP_ADDRESS.addParameter(hostName),e);
			}
		}

}
