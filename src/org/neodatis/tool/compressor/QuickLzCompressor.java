package org.neodatis.tool.compressor;

import java.io.IOException;

public class QuickLzCompressor implements Compressor{

	public byte[] compress(byte[] bytes) throws IOException {
		return QuickLZ.compress(bytes);
	}

	public byte[] uncompress(byte[] bytes) throws IOException {
		return QuickLZ.decompress(bytes);
	}

}
