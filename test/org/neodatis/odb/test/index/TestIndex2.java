/*
 NeoDatis ODB : Native Object Dataodb (odb.info@neodatis.org)
 Copyright (C) 2007 NeoDatis Inc. http://www.neodatis.org

 "This file is part of the NeoDatis ODB open source object dataodb".

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
package org.neodatis.odb.test.index;

import org.neodatis.odb.ClassRepresentation;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfoIndex;
import org.neodatis.odb.core.layers.layer2.meta.MetaModel;
import org.neodatis.odb.core.layers.layer4.engine.Dummy;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.test.ODBTest;

import java.util.Date;

public class TestIndex2 extends ODBTest {

	public void testIndexFail() throws Exception {
		String odbName = getBaseName();
		ODB odb = open(odbName);

		String indexName = "index1";
		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject3.class);
		String[] indexFields1 = { "i1", "i2", "i3" };
		clazz.addUniqueIndexOn(indexName, indexFields1, true);

		odb.close();

		odb = open(odbName);
		IndexedObject3 io = new IndexedObject3(1, 2, 3, "1", "2", "3", new Date(), new Date(), new Date());
		odb.store(io);

		try {
			IndexedObject3 io2 = new IndexedObject3(1, 2, 3, "1", "2", "3", new Date(), new Date(), new Date());
			odb.store(io2);
		} catch (Exception e) {
			assertTrue(e.getMessage().indexOf(indexName) != -1);
			// println(e.getMessage());
		}

		odb.close();
		odb = open(odbName);
		Objects<IndexedObject3> oo3 = odb.query(IndexedObject3.class).objects();
		odb.close();

		assertEquals(0, oo3.size());
		
	}

	public void testSaveIndex() throws Exception {
		String odbName = getBaseName();
		
		ODB odb = open(odbName);

		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject3.class);
		String[] indexFields1 = { "i1", "i2", "i3" };
		clazz.addUniqueIndexOn("index1", indexFields1, true);

		String[] indexFields2 = { "s1", "s2", "s3" };
		clazz.addUniqueIndexOn("index2", indexFields2, true);

		String[] indexFields3 = { "dt1", "dt2", "dt3" };
		clazz.addUniqueIndexOn("index3", indexFields3, true);

		String[] indexFields4 = { "i1", "i2", "i3", "s1", "s2", "s3", "dt1", "dt2", "dt3" };
		clazz.addUniqueIndexOn("index4", indexFields4, true);

		odb.close();

		odb = open(odbName);

		Session session = Dummy.getEngine(odb).getSession();
		MetaModel metaModel = session.getMetaModel();
		ClassInfo ci = metaModel.getClassInfo(IndexedObject3.class.getName(), true);
		assertEquals(4, ci.getNumberOfIndexes());

		assertEquals(ci.getIndex(0).getName(), "index1");
		assertEquals(3, ci.getIndex(0).getAttributeIds().length);
		assertEquals(ClassInfoIndex.ENABLED, ci.getIndex(0).getStatus());

		assertEquals(ci.getIndex(1).getName(), "index2");
		assertEquals(3, ci.getIndex(1).getAttributeIds().length);
		assertEquals(ClassInfoIndex.ENABLED, ci.getIndex(1).getStatus());

		assertEquals(ci.getIndex(2).getName(), "index3");
		assertEquals(3, ci.getIndex(2).getAttributeIds().length);
		assertEquals(ClassInfoIndex.ENABLED, ci.getIndex(2).getStatus());

		assertEquals(ci.getIndex(3).getName(), "index4");
		assertEquals(9, ci.getIndex(3).getAttributeIds().length);
		assertEquals(ClassInfoIndex.ENABLED, ci.getIndex(3).getStatus());
		odb.close();

		odb = open(odbName);

		for (int i = 0; i < 10; i++) {
			IndexedObject3 io = new IndexedObject3(1 + i, 2, 3, "1" + i, "2", "3", new Date(2009, i, 1), new Date(), new Date());
			odb.store(io);
		}

		odb.close();

		
	}

	/**
	 * Test index creation without commit
	 * 
	 * @throws Exception
	 */
	public void testCreateIndexWithoutCommit() throws Exception {
		String odbName = getBaseName();
		
		ODB odb = open(odbName);

		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject3.class);
		String[] indexFields1 = { "i1" };
		clazz.addUniqueIndexOn("index1", indexFields1, true);

		for (int i = 0; i < 10; i++) {
			IndexedObject3 io = new IndexedObject3(1 + i, 2, 3, "1" + i, "2", "3", new Date(2009, i, 1), new Date(), new Date());
			odb.store(io);
		}

		odb.close();

		odb = open(odbName);
		Query q = odb.query(IndexedObject3.class, W.equal("i1", 1));
		Objects<IndexedObject3> iis = q.objects();
		odb.close();
		assertEquals(1, iis.size());
		assertTrue(q.getExecutionPlan().useIndex());

		
	}

	/**
	 * Opens a connection C1, then create the index in another connection C2 and
	 * then stores the object in connection C1
	 * 
	 * @throws Exception
	 */
	public void testCreateIndexInOtherConnection() throws Exception {
		if (isLocal || !testNewFeature) {
			return;
		}
		String odbName = getBaseName();
		
		ODB odb1 = open(odbName);
		ODB odb2 = open(odbName);

		ClassRepresentation clazz = odb2.getClassRepresentation(IndexedObject3.class);
		String[] indexFields1 = { "i1" };
		clazz.addUniqueIndexOn("index1", indexFields1, true);
		odb2.close();

		for (int i = 0; i < 10; i++) {
			IndexedObject3 io = new IndexedObject3(1 + i, 2, 3, "1" + i, "2", "3", new Date(2009, i, 1), new Date(), new Date());
			odb1.store(io);
		}

		odb1.close();

		ODB odb = open(odbName);
		Query q = odb.query(IndexedObject3.class, W.equal("i1", 1));
		Objects<IndexedObject3> iis = q.objects();
		odb.close();
		assertEquals(1, iis.size());
		assertTrue(q.getExecutionPlan().useIndex());

		
	}

	/**
	 * Opens a connection C1, then create the index in another connection C2 and
	 * then stores the object in connection C1
	 * 
	 * @throws Exception
	 */
	public void testCreateIndexInOtherConnectionNoClose() throws Exception {
		if (isLocal || !testNewFeature) {
			return;
		}

		String odbName = getBaseName();
		
		ODB odb1 = open(odbName);
		ODB odb2 = open(odbName);

		ClassRepresentation clazz = odb2.getClassRepresentation(IndexedObject3.class);
		String[] indexFields1 = { "i1" };
		clazz.addUniqueIndexOn("index1", indexFields1, true);
		odb2.commit();

		for (int i = 0; i < 10; i++) {
			IndexedObject3 io = new IndexedObject3(1 + i, 2, 3, "1" + i, "2", "3", new Date(2009, i, 1), new Date(), new Date());
			odb1.store(io);
		}

		odb1.close();

		ODB odb = open(odbName);
		Query q = odb.query(IndexedObject3.class, W.equal("i1", 1));
		Objects<IndexedObject3> iis = odb.query(q).objects();
		odb.close();
		odb2.close();
		assertEquals(1, iis.size());
		assertTrue(q.getExecutionPlan().useIndex());

		
	}

	/**
	 * Opens a connection C1, then create the index in another connection C2 and
	 * then stores the object in connection C1
	 * 
	 * @throws Exception
	 */
	public void testCreateIndexInOtherConnectionNoCommit1() throws Exception {
		if (isLocal || !testNewFeature) {
			return;
		}

		String odbName = getBaseName();
		
		ODB odb1 = open(odbName);
		ODB odb2 = open(odbName);

		ClassRepresentation clazz = odb2.getClassRepresentation(IndexedObject3.class);
		String[] indexFields1 = { "i1" };
		clazz.addUniqueIndexOn("index1", indexFields1, true);

		for (int i = 0; i < 10; i++) {
			IndexedObject3 io = new IndexedObject3(1 + i, 2, 3, "1" + i, "2", "3", new Date(2009, i, 1), new Date(), new Date());
			odb1.store(io);
		}

		odb2.close();
		odb1.close();

		ODB odb = open(odbName);
		Query q = odb.query(IndexedObject3.class, W.equal("i1", 1));
		Objects<IndexedObject3> iis = odb.query(q).objects();
		odb.close();
		assertEquals(1, iis.size());
		assertTrue(q.getExecutionPlan().useIndex());

		
	}

	/**
	 * Opens a connection C1, then create the index in another connection C2 and
	 * then stores the object in connection C1
	 * 
	 * @throws Exception
	 */
	public void testCreateIndexInOtherConnectionNoCommit2() throws Exception {
		if (isLocal || !testNewFeature) {
			return;
		}

		String odbName = getBaseName();
		
		ODB odb1 = open(odbName);
		ODB odb2 = open(odbName);

		ClassRepresentation clazz = odb2.getClassRepresentation(IndexedObject3.class);
		String[] indexFields1 = { "i1" };
		clazz.addUniqueIndexOn("index1", indexFields1, true);

		for (int i = 0; i < 10; i++) {
			IndexedObject3 io = new IndexedObject3(1 + i, 2, 3, "1" + i, "2", "3", new Date(2009, i, 1), new Date(), new Date());
			odb1.store(io);
		}

		odb1.close();
		odb2.close();

		ODB odb = open(odbName);
		Query q = odb.query(IndexedObject3.class, W.equal("i1", 1));
		Objects<IndexedObject3> iis = odb.query(q).objects();
		odb.close();
		assertEquals(1, iis.size());
		assertTrue(q.getExecutionPlan().useIndex());

		
	}

	/**
	 * Create objects, then create index, then execute a select with index, then
	 * rebuild index e execute
	 * 
	 * @throws Exception
	 */
	public void testRebuildIndex() throws Exception {
		String odbName = getBaseName();
		

		ODB odb = open(odbName);

		for (int i = 0; i < 2500; i++) {
			IndexedObject3 io = new IndexedObject3(1 + i, 2, 3, "1" + i, "2", "3", new Date(2009, i, 1), new Date(), new Date());
			odb.store(io);
		}

		odb.close();

		odb = open(odbName);

		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject3.class);
		String[] indexFields1 = { "i1", "i2", "i3" };
		clazz.addUniqueIndexOn("index1", indexFields1, true);

		odb.close();

		odb = open(odbName);
		Session session = Dummy.getEngine(odb).getSession();
		MetaModel metaModel = session.getMetaModel();
		ClassInfo ci = metaModel.getClassInfo(IndexedObject3.class.getName(), true);
		assertEquals(1, ci.getNumberOfIndexes());

		assertEquals(ci.getIndex(0).getName(), "index1");
		assertEquals(3, ci.getIndex(0).getAttributeIds().length);
		assertEquals(ClassInfoIndex.ENABLED, ci.getIndex(0).getStatus());

		Query q = odb.query(IndexedObject3.class, W.and().add(W.equal("i1", 10)).add(W.equal("i2", 2)).add(
				W.equal("i3", 3)));
		Objects<IndexedObject3> objects = odb.query(q).objects();
		assertEquals(true, q.getExecutionPlan().useIndex());

		odb.getClassRepresentation(IndexedObject3.class).rebuildIndex("index1", true);
		odb.close();

		odb = open(odbName);
		objects = odb.query(q).objects();
		assertEquals(true, q.getExecutionPlan().useIndex());
		odb.close();

		
	}

	/**
	 * Create objects, then create index, then execute a select with index, then
	 * rebuild index e execute
	 * 
	 * @throws Exception
	 */
	public void testDeleteIndex() throws Exception {
		String odbName = getBaseName();
		

		ODB odb = open(odbName);

		for (int i = 0; i < 250; i++) {
			IndexedObject3 io = new IndexedObject3(1 + i, 2, 3, "1" + i, "2", "3", new Date(2009, i, 1), new Date(), new Date());
			odb.store(io);
		}

		odb.close();

		odb = open(odbName);

		ClassRepresentation clazz = odb.getClassRepresentation(IndexedObject3.class);
		String[] indexFields1 = { "i1", "i2", "i3" };
		clazz.addUniqueIndexOn("index1", indexFields1, true);

		odb.close();

		odb = open(odbName);
		Session session = Dummy.getEngine(odb).getSession();
		MetaModel metaModel = session.getMetaModel();
		ClassInfo ci = metaModel.getClassInfo(IndexedObject3.class.getName(), true);
		assertEquals(1, ci.getNumberOfIndexes());

		assertEquals(ci.getIndex(0).getName(), "index1");
		assertEquals(3, ci.getIndex(0).getAttributeIds().length);
		assertEquals(ClassInfoIndex.ENABLED, ci.getIndex(0).getStatus());

		Query q = odb.query(IndexedObject3.class, W.and().add(W.equal("i1", 10)).add(W.equal("i2", 2)).add(
				W.equal("i3", 3)));
		Objects<IndexedObject3> objects = q.objects();
		assertEquals(true, q.getExecutionPlan().useIndex());

		odb.getClassRepresentation(IndexedObject3.class).deleteIndex("index1", true);
		odb.close();

		odb = open(odbName);
		objects = odb.query(q).objects();
		assertEquals(false, q.getExecutionPlan().useIndex());
		odb.close();

		
	}
}
