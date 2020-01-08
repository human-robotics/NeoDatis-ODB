package org.neodatis.tool.compressor;

import java.io.IOException;

public interface Compressor {

	public byte[] compress(byte[] bytes) throws IOException;

	public byte[] uncompress(byte[] bytes) throws IOException;

}
