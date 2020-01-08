package org.neodatis.tool.compressor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class DefaultCompressor implements Compressor {

	public byte[] compress(byte[] bytes) throws IOException {
		Deflater compressor = new Deflater();
		compressor.setLevel(Deflater.BEST_SPEED);

		// Give the compressor the data to compress
		compressor.setInput(bytes);
		compressor.finish();

		// Create an expandable byte array to hold the compressed data.
		// It is not necessary that the compressed data will be smaller than
		// the uncompressed data.
		ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);

		// Compress the data
		byte[] buf = new byte[1024];
		while (!compressor.finished()) {
			int count = compressor.deflate(buf);
			bos.write(buf, 0, count);
		}
		try {
			bos.close();
		} catch (IOException e) {
			throw e;
		}

		// Get the compressed data
		byte[] compressedData = bos.toByteArray();

		return compressedData;

	}

	public byte[] uncompress(byte[] bytes) throws IOException {
		// Create the decompressor and give it the data to compress
		Inflater decompressor = new Inflater();
		decompressor.setInput(bytes);

		// Create an expandable byte array to hold the decompressed data
		ByteArrayOutputStream bos = new ByteArrayOutputStream(
				bytes.length);

		// Decompress the data
		byte[] buf = new byte[1024];
		while (!decompressor.finished()) {
			try {
				int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			} catch (DataFormatException e) {
				throw new IOException(e.getMessage());
			}
		}
		try {
			bos.close();
		} catch (IOException e) {
			throw e;
		}

		// Get the decompressed data
		byte[] decompressedData = bos.toByteArray();

		return decompressedData;
	}

}
