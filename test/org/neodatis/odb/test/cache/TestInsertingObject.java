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
package org.neodatis.odb.test.cache;

import org.neodatis.odb.test.ODBTest;

public class TestInsertingObject extends ODBTest {
/*
	public void test1() throws IOException {

		ICache cache = CacheFactory.getLocalCache(null, "test");
		String s1 = "ola1";
		String s2 = "ola2";
		String s3 = "ola3";
		cache.startInsertingObjectWithOid(s1, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,1), null);
		cache.startInsertingObjectWithOid(s2, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,2), null);
		cache.startInsertingObjectWithOid(s3, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,3), null);

		assertTrue(cache.idOfInsertingObject(s1) != StorageEngineConstant.NULL_OBJECT_ID);
		assertTrue(cache.idOfInsertingObject(s2) != StorageEngineConstant.NULL_OBJECT_ID);
		assertTrue(cache.idOfInsertingObject(s3) != StorageEngineConstant.NULL_OBJECT_ID);

		cache.endInsertingObject(s3);
		cache.endInsertingObject(s2);
		cache.endInsertingObject(s1);

		assertTrue(cache.idOfInsertingObject(s1) == StorageEngineConstant.NULL_OBJECT_ID);
		assertTrue(cache.idOfInsertingObject(s2) == StorageEngineConstant.NULL_OBJECT_ID);
		assertTrue(cache.idOfInsertingObject(s3) == StorageEngineConstant.NULL_OBJECT_ID);
	}

	public void test2() throws IOException {

		ICache cache = CacheFactory.getLocalCache(null, "temp");
		String s1 = "ola1";
		String s2 = "ola2";
		String s3 = "ola3";

		for (int i = 0; i < 1000 * 3; i += 3) {
			cache.startInsertingObjectWithOid(s1, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,i+1), null);
			cache.startInsertingObjectWithOid(s2, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,i+2), null);
			cache.startInsertingObjectWithOid(s3, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,i+3), null);
		}
		assertEquals(1000, cache.insertingLevelOf(s1));
		assertEquals(1000, cache.insertingLevelOf(s2));
		assertEquals(1000, cache.insertingLevelOf(s3));

		for (int i = 0; i < 1000; i++) {
			cache.endInsertingObject(s1);
			cache.endInsertingObject(s2);
			cache.endInsertingObject(s3);
		}
		assertEquals(0, cache.insertingLevelOf(s1));
		assertEquals(0, cache.insertingLevelOf(s2));
		assertEquals(0, cache.insertingLevelOf(s3));

		cache.startInsertingObjectWithOid(s1, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,1), null);
		cache.startInsertingObjectWithOid(s1, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,1), null);
		cache.startInsertingObjectWithOid(s1, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,1), null);
		cache.startInsertingObjectWithOid(s2, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,2), null);
		cache.startInsertingObjectWithOid(s3, OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,3), null);

		assertTrue(cache.idOfInsertingObject(s1) != StorageEngineConstant.NULL_OBJECT_ID);
		assertTrue(cache.idOfInsertingObject(s2) != StorageEngineConstant.NULL_OBJECT_ID);
		assertTrue(cache.idOfInsertingObject(s3) != StorageEngineConstant.NULL_OBJECT_ID);

		cache.endInsertingObject(s3);
		cache.endInsertingObject(s2);
		cache.endInsertingObject(s1);

		assertTrue(cache.idOfInsertingObject(s1) != StorageEngineConstant.NULL_OBJECT_ID);
		assertTrue(cache.idOfInsertingObject(s2) == StorageEngineConstant.NULL_OBJECT_ID);
		assertTrue(cache.idOfInsertingObject(s3) == StorageEngineConstant.NULL_OBJECT_ID);
	}

	public void test3() throws IOException {

		ICache cache = CacheFactory.getLocalCache(null, "temp");
		ClassInfo ci = new ClassInfo(this.getClass().getName());
		ci.setPosition(1);
		ObjectInfoHeader oih1 = new ObjectInfoHeader();
		ObjectInfoHeader oih2 = new ObjectInfoHeader();
		ObjectInfoHeader oih3 = new ObjectInfoHeader();
		oih1.setOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,1));
		oih2.setOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,10));
		oih3.setOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,100));
		NonNativeObjectInfo nnoi1 = new NonNativeObjectInfo(oih1, ci);
		NonNativeObjectInfo nnoi2 = new NonNativeObjectInfo(oih2, ci);
		NonNativeObjectInfo nnoi3 = new NonNativeObjectInfo(oih3, ci);

		cache.startReadingObjectInfoWithOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,1), nnoi1);
		cache.startReadingObjectInfoWithOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,10), nnoi2);
		cache.startReadingObjectInfoWithOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,100), nnoi3);

		assertTrue(cache.isReadingObjectInfoWithOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,1)));
		assertTrue(cache.isReadingObjectInfoWithOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,10)));
		assertTrue(cache.isReadingObjectInfoWithOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,100)));

		cache.endReadingObjectInfo(nnoi1.getOid());
		cache.endReadingObjectInfo(nnoi2.getOid());
		cache.endReadingObjectInfo(nnoi3.getOid());

		assertFalse(cache.isReadingObjectInfoWithOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,1)));
		assertFalse(cache.isReadingObjectInfoWithOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,10)));
		assertFalse(cache.isReadingObjectInfoWithOid(OIDFactory.buildOID(OIDTypes.TYPE_OBJECT_OID, 1,100)));
	}
*/
}
