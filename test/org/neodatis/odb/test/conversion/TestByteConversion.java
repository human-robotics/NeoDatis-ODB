/*
 NeoDatis ODB : Native Object Database (odb.info@neodatis.org)
 Copyright (C) 2007 NeoDatis Inc. http://www.neodatis.org

 "This file is part of the NeoDatis ODB open source object database".

 NeoDatis ODB is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 NeoDatis ODB is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.neodatis.odb.test.conversion;

import org.junit.Test;
import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.core.layers.layer3.*;
import org.neodatis.odb.test.ODBTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;

public class TestByteConversion extends ODBTest {
	static DataConverter byteArrayConverter = new DataConverterImpl(false, "UTF-8",NeoDatis.getConfig());
	public static final int SIZE = 1000;

	public static final int SIZE0 = 1000;

    @Test
	public void testPerfLong() {
		int size = 10000;
		long l = 474367843;

		long start1 = System.currentTimeMillis();
		for (int j = 0; j < size; j++) {
			byte b[] = new byte[8];
			int i, shift;
			for (i = 0, shift = 56; i < 8; i++, shift -= 8) {
				b[i] = (byte) (0xFF & (l >> shift));
			}

		}
		long end1 = System.currentTimeMillis();

		long start2 = System.currentTimeMillis();
		for (int j = 0; j < size; j++) {
			byte b[] = ByteBuffer.allocate(8).putLong(l).array();
		}
		long end2 = System.currentTimeMillis();

		println("Standard conversion = " + (end1 - start1));
		println("NIO conversion = " + (end2 - start2));
	}

    @Test
	public void testPerfLong2(){
		int size = 1000000;
		long start = System.currentTimeMillis();
		for(int i=0;i<size;i++){
			testInt();
		}
		long end = System.currentTimeMillis();
		println("time = " + (end-start));
	}

    @Test
	public void testLong() {
		Bytes bytes = BytesFactory.getBytes();
		int offset = 500;
		ReadSize readSize = new ReadSize();
		long l1 = 785412;
		byteArrayConverter.longToByteArray(l1, bytes, offset, "label");
		long l2 = byteArrayConverter.byteArrayToLong(bytes, offset, readSize, "label");
		assertEquals(l1, l2);

		l1 = Long.MAX_VALUE;
		byteArrayConverter.longToByteArray(l1, bytes, offset, "label");
		l2 = byteArrayConverter.byteArrayToLong(bytes, offset, readSize, "label");
		assertEquals(l1, l2);

		l1 = Long.MIN_VALUE;
		byteArrayConverter.longToByteArray(l1, bytes, offset, "label");
		l2 = byteArrayConverter.byteArrayToLong(bytes, offset, readSize, "label");
		assertEquals(l1, l2);
	}

    @Test
	public void testInt() {
		Bytes bytes = BytesFactory.getBytes();
		int offset = 500;
		ReadSize readSize = new ReadSize();
		int l1 = 785412;
		byteArrayConverter.intToByteArray(l1, bytes, offset, "label");
		int l2 = byteArrayConverter.byteArrayToInt(bytes, offset,readSize, "label");

		assertEquals(l1, l2);
	}

    @Test
	public void testFloat() {
		Bytes bytes = BytesFactory.getBytes();
		int offset = 500;
		ReadSize readSize = new ReadSize();
		float l1 = (float) 785412.4875;
		byteArrayConverter.floatToByteArray(l1, bytes, offset, "label");
		float l2 = byteArrayConverter.byteArrayToFloat(bytes, offset,readSize, "label");
		assertEquals(l1, l2, 0);
	}

    @Test
	public void testDouble() {
		Bytes bytes = BytesFactory.getBytes();
		int offset = 500;
		ReadSize readSize = new ReadSize();
		double l1 = 785412.4875;
		byteArrayConverter.doubleToByteArray(l1, bytes, offset, "label");
		double l2 = byteArrayConverter.byteArrayToDouble(bytes, offset,readSize, "label");
		assertEquals(l1, l2, 0);
	}

    @Test
	public void testBoolean() throws IOException {
		Bytes bytes = BytesFactory.getBytes();
		int offset = 500;
		ReadSize readSize = new ReadSize();
		boolean b1 = true;
		byteArrayConverter.booleanToByteArray(b1, bytes, offset, "label");
		boolean b3 = byteArrayConverter.byteArrayToBoolean(bytes, offset, readSize,"label");
		assertEquals(b1, b3);
		b1 = false;
		byteArrayConverter.booleanToByteArray(b1, bytes, offset, "label");
		b3 = byteArrayConverter.byteArrayToBoolean(bytes, offset, readSize,"label");
		assertEquals(b1, b3);
	}

    @Test
	public void testChar() throws IOException {
		Bytes bytes = BytesFactory.getBytes();
		int offset = 500;
		ReadSize readSize = new ReadSize();
		char c = '\u00E1';
		byteArrayConverter.charToByteArray(c, bytes, offset, "label");
		char c1 = byteArrayConverter.byteArrayToChar(bytes, offset,readSize, "label");
		assertEquals(c, c1);
	}

    @Test
	public void testShort() throws IOException {
		Bytes bytes = BytesFactory.getBytes();
		int offset = 500;
		ReadSize readSize = new ReadSize();
		short s = 4598;
		byteArrayConverter.shortToByteArray(s, bytes, offset, "label");
		short s2 = byteArrayConverter.byteArrayToShort(bytes, offset,readSize, "label");
		// assertEquals(s,s2);

		s = 10000;
		byteArrayConverter.shortToByteArray(s, bytes, offset, "label");
		s2 = byteArrayConverter.byteArrayToShort(bytes, offset, readSize, "label");
		assertEquals(s, s2);

		s = Short.MAX_VALUE;
		byteArrayConverter.shortToByteArray(s, bytes, offset, "label");
		s2 = byteArrayConverter.byteArrayToShort(bytes, offset,readSize, "label");
		assertEquals(s, s2);

		s = Short.MIN_VALUE;
		byteArrayConverter.shortToByteArray(s, bytes, offset, "label");
		s2 = byteArrayConverter.byteArrayToShort(bytes, offset,readSize, "label");
		assertEquals(s, s2);
	}

    @Test
	public void testString() throws IOException {
		ReadSize readSize = new ReadSize();
		String s = "test1";
		Bytes bytes = BytesFactory.getBytes();
		int size = byteArrayConverter.stringToByteArray(s, true, bytes, 100,"label");
		String s2 = byteArrayConverter.byteArrayToString(bytes, true, 100,readSize,"label");
		assertEquals(s, s2);
	}

    @Test
	public void testBigDecimal1() throws IOException {

		BigDecimal bd1 = new BigDecimal(10);
		Bytes bytes = BytesFactory.getBytes();
		ReadSize readSize = new ReadSize();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 100,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 100,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal2() throws IOException {

		BigDecimal bd1 = new BigDecimal(10.123456789123456789);
		Bytes bytes = BytesFactory.getBytes();
		ReadSize readSize = new ReadSize();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal3() throws IOException {

		BigDecimal bd1 = new BigDecimal(0);
		Bytes bytes = BytesFactory.getBytes();
		ReadSize readSize = new ReadSize();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal4() throws IOException {

		BigDecimal bd1 = new BigDecimal(10);
		Bytes bytes = BytesFactory.getBytes();
		ReadSize readSize = new ReadSize();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal5() throws IOException {
		ReadSize readSize = new ReadSize();
		BigDecimal bd1 = new BigDecimal(0.000);
		Bytes bytes = BytesFactory.getBytes();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal6() throws IOException {
		ReadSize readSize = new ReadSize();
		BigDecimal bd1 = new BigDecimal(0.000000000000000123456789);
		Bytes bytes = BytesFactory.getBytes();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal7() throws IOException {
		ReadSize readSize = new ReadSize();
		BigDecimal bd1 = new BigDecimal(-1);
		Bytes bytes = BytesFactory.getBytes();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal8() throws IOException {
		ReadSize readSize = new ReadSize();
		BigDecimal bd1 = new BigDecimal(-123456789);
		Bytes bytes = BytesFactory.getBytes();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal9() throws IOException {
		ReadSize readSize = new ReadSize();
		BigDecimal bd1 = new BigDecimal(-0.000000000000000000000000000000123456789);
		Bytes bytes = BytesFactory.getBytes();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal10() throws IOException {
		ReadSize readSize = new ReadSize();
		BigDecimal bd1 = new BigDecimal(123456789123456789123456789.123456789123456789);
		Bytes bytes = BytesFactory.getBytes();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}
    @Test
	public void testBigDecimal11() throws IOException {
		ReadSize readSize = new ReadSize();
		BigDecimal bd1 = new BigDecimal(-0.00000);
		Bytes bytes = BytesFactory.getBytes();
		int size = byteArrayConverter.bigDecimalToByteArray(bd1, bytes, 500,"label");
		BigDecimal bd2 = byteArrayConverter.byteArrayToBigDecimal(bytes, 500,readSize,"label");
		assertEquals(bd1, bd2);
	}

}
