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

package org.neodatis.odb.test.serialization;

import org.neodatis.odb.*;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.MetaModel;
import org.neodatis.odb.core.layers.layer2.meta.MetaModelImpl;
import org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo;
import org.neodatis.odb.core.layers.layer3.Bytes;
import org.neodatis.odb.core.layers.layer3.BytesFactory;
import org.neodatis.odb.core.layers.layer3.OidAndBytes;
import org.neodatis.odb.core.layers.layer4.IOFileParameter;
import org.neodatis.odb.core.layers.layer4.OidGenerator;
import org.neodatis.odb.core.layers.layer4.plugin.memory.InMemoryStorageEngine;
import org.neodatis.odb.core.oid.uuid.UniqueOidGeneratorImpl;
import org.neodatis.odb.core.query.GenericExecutionPlanImpl;
import org.neodatis.odb.core.query.IQueryExecutionPlan;
import org.neodatis.odb.core.query.InternalQuery;
import org.neodatis.odb.core.query.QueryFactory;
import org.neodatis.odb.core.query.criteria.W;
import org.neodatis.odb.core.query.list.objects.SimpleList;
import org.neodatis.odb.core.server.ReturnValue;
import org.neodatis.odb.core.server.message.*;
import org.neodatis.odb.core.server.message.process.*;
import org.neodatis.odb.core.server.message.serialization.AllSerializers;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.core.session.SessionImpl;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.oid.OIDFactory;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;
import org.neodatis.odb.test.vo.login.User;
import org.neodatis.tool.wrappers.list.IOdbList;
import org.neodatis.tool.wrappers.list.OdbArrayList;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestSerialization extends ODBTest implements Serializable{
	
	protected OidGenerator oidGenerator;
	
	@Override
	public void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		
		oidGenerator = new UniqueOidGeneratorImpl();
	}

	public void testConnectMessage() throws Exception{
		int size = 10000;
		long start = System.currentTimeMillis();
		for(int i=0;i<size;i++){
			ConnectMessage m = new ConnectMessage("baseId", "sessionId","ip", "user", "password", i%2==0);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig());
			
			Bytes bytes = serializer.toBytes(m);
			ConnectMessage m2 = (ConnectMessage) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			assertEquals(m.getIp(), m2.getIp());
			assertEquals(m.getUser(), m2.getUser());
			assertEquals(m.getPassword(), m2.getPassword());
			assertEquals(m.isTransactional(), m2.isTransactional());
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	public void testConnectMessage2() throws Exception{
		int size = 1000;
		long start = System.currentTimeMillis();
		for(int i=0;i<size;i++){
			ConnectMessage m = new ConnectMessage("baseId", "sessionId","ip", "user", "password",i%2==0);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(m);
			byte[] bytes = baos.toByteArray();
			
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			
			ConnectMessage m2 = (ConnectMessage) new ObjectInputStream(bais).readObject();
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			assertEquals(m.getIp(), m2.getIp());
			assertEquals(m.getUser(), m2.getUser());
			assertEquals(m.getPassword(), m2.getPassword());
			assertEquals(m.isTransactional(), m2.isTransactional());
		}
		long end = System.currentTimeMillis();
		println("Time with serialization " + (end-start));
	}
	public void testConnectMessageResponse() throws Exception{
		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);

		int size = 1000;
		long start = System.currentTimeMillis();
		MetaModel metaModel = new MetaModelImpl(config);
		ClassInfo ci = new ClassInfo("class test");
		ci.setOid(OIDFactory.buildClassOID());
		metaModel.addClass(ci, false);
		
		
		Session session = new SessionImpl(new IOFileParameter("test",true,config));
		SessionEngine engine = session.getEngine();
		IOdbList<OidAndBytes> oabs = new OdbArrayList<OidAndBytes>();
		oabs.add(engine.classInfoToBytes(ci));
		try{
			for(int i=0;i<size;i++){
				
				ConnectMessageResponse m = new ConnectMessageResponse("baseId", "sessionId" , oabs, "OidGeneratorName", 10, "DatabaseId");
				
				AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class));
				
				Bytes bytes = serializer.toBytes(m);
				ConnectMessageResponse m2 = (ConnectMessageResponse) serializer.fromBytes(bytes);
				
				assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
				assertEquals(m.getSessionId(), m2.getSessionId());
				assertEquals(m.getDateTime(), m2.getDateTime());
				assertEquals(m.getOabsOfMetaModel().size(), m2.getOabsOfMetaModel().size());
				assertEquals(m.getOabsOfMetaModel().get(0).oid, m2.getOabsOfMetaModel().get(0).oid);
			}
			
		}finally{
			
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	
	public void testStoreMessage() throws Exception{
		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class).setOidGeneratorClass(UniqueOidGeneratorImpl.class);
		try{
			int size = 1000;
			long start = System.currentTimeMillis();
			
			Session session = new SessionImpl(new IOFileParameter("test",true,config));
			SessionEngine engine = session.getEngine();
			Function function = new Function("function");

			NonNativeObjectInfo nnoi = engine.layer1ToLayer2(function);
			IOdbList<OidAndBytes> oabs = engine.layer2ToLayer3( nnoi);
			
			
			
			ClassOid cid = OIDFactory.buildClassOID();
			ObjectOid[] clientIds = { OIDFactory.buildObjectOID(cid),OIDFactory.buildObjectOID(cid),OIDFactory.buildObjectOID( cid)};
			oabs.get(0).oid = clientIds[0];
			
			for(int i=0;i<size;i++){
				StoreObjectMessage m = new StoreObjectMessage("baseId", "sessionId",oabs, clientIds);
				
				AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class));
				
				Bytes bytes = serializer.toBytes(m);
				StoreObjectMessage m2 = (StoreObjectMessage) serializer.fromBytes(bytes);
			
				assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
				assertEquals(m.getSessionId(), m2.getSessionId());
				assertEquals(m.getDateTime(), m2.getDateTime());
				assertEquals(m.getClientIds().length, m2.getClientIds().length);
				assertEquals(m.getClientIds()[0], m2.getClientIds()[0]);
				assertEquals(m.getClientIds()[1], m2.getClientIds()[1]);
				assertEquals(m.getClientIds()[2], m2.getClientIds()[2]);
				assertEquals(m.getOabs().size(), m2.getOabs().size());
				assertEquals(m.getOabs().get(0).oid, m2.getOabs().get(0).oid);
			}
			long end = System.currentTimeMillis();
			println("Time " + (end-start));
			
		}finally{
		}
	}
	
	public void testStoreMessageResponse() throws Exception{
		int size = 1000;
		long start = System.currentTimeMillis();
		
		ClassOid cid = OIDFactory.buildClassOID();
		ObjectOid oid = OIDFactory.buildObjectOID( cid);
		ObjectOid[] clientIds = { OIDFactory.buildObjectOID( cid),OIDFactory.buildObjectOID(cid),OIDFactory.buildObjectOID( cid)};
		ObjectOid[] serverIds = { OIDFactory.buildObjectOID( cid),OIDFactory.buildObjectOID( cid),OIDFactory.buildObjectOID( cid)};
		
		List<ReturnValue> rvs = new ArrayList<ReturnValue>();
		ReturnValue rv1 = new MyReturnValue();
		ReturnValue rv2 = new MyReturnValue();
		
		rvs.add(rv1);
		rvs.add(rv2);
		
		
		for(int i=0;i<size;i++){
			StoreObjectMessageResponse m = new StoreObjectMessageResponse("baseId", "sessionId",oid, true, serverIds,rvs);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class));
			
			Bytes bytes = serializer.toBytes(m);
			StoreObjectMessageResponse m2 = (StoreObjectMessageResponse) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			assertEquals(m.getServerIds()[0], m2.getServerIds()[0]);
			assertEquals(m.getServerIds()[1], m2.getServerIds()[1]);
			assertEquals(m.getServerIds()[2], m2.getServerIds()[2]);
			assertEquals(m.getOid(), m2.getOid());
			assertEquals(2, m2.getReturnValues().size());
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	
	public void testGetMessage() throws Exception{
		int size = 10000;
		long start = System.currentTimeMillis();
		
		Query q = QueryFactory.query(User.class, W.equal("name", "name1").and(W.equal("email", "email")).or(W.gt("nnn", 15)));
		
		for(int i=0;i<size;i++){
			GetObjectsMessage m = new GetObjectsMessage("baseId", "sessionId" , (InternalQuery) q);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig());
			
			Bytes bytes = serializer.toBytes(m);
			GetObjectsMessage m2 = (GetObjectsMessage) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			
			assertEquals(q.toString(), m2.getQuery().toString());
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	
	public void testGetMessageResponse() throws Exception{
		int size = 1;
		int nbOids = 1000;
		long start = System.currentTimeMillis();
		
		Objects<ObjectOid> oids = new SimpleList<ObjectOid>();
		ClassOid coid = OIDFactory.buildClassOID();
		for(int i=0;i<nbOids;i++){
			oids.add(OIDFactory.buildObjectOID( coid));
		}
		
		for(int i=0;i<size;i++){
			
			IQueryExecutionPlan plan = new GenericExecutionPlanImpl(true, i, "this is the details"+i);

			GetObjectsMessageResponse m = new GetObjectsMessageResponse("baseId"+i, "sessionId"+i,oids,plan);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class));
			
			Bytes bytes = serializer.toBytes(m);
			GetObjectsMessageResponse m2 = (GetObjectsMessageResponse) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			
			assertEquals(m.getPlan().getDetails(), m2.getPlan().getDetails());
			assertEquals(m.isOnlyOids(), m2.isOnlyOids());
			assertEquals(m.getObjectOids().size(), m2.getObjectOids().size());
			assertEquals(m.getObjectOids().iterator().next(), m2.getObjectOids().iterator().next());
			
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	
	public void testGetMessageResponse2() throws Exception{
		int size = 1;
		int nbOids = 100;
		long start = System.currentTimeMillis();
		
		Collection<IOdbList<OidAndBytes>> c= new ArrayList<IOdbList<OidAndBytes>>();
		
		ClassOid coid = OIDFactory.buildClassOID();
		for(int i=0;i<nbOids;i++){
			IOdbList<OidAndBytes> oabs = new OdbArrayList<OidAndBytes>();
			for(int j=0;j<3;j++){
				oabs.add(new OidAndBytes(OIDFactory.buildObjectOID( coid), BytesFactory.getBytes(("olá chico"+(i*j)).getBytes())));
			}
			c.add(oabs);
		}
		
		for(int i=0;i<size;i++){
			
			IQueryExecutionPlan plan = new GenericExecutionPlanImpl(true, i, "this is the details"+i);

			GetObjectsMessageResponse m = new GetObjectsMessageResponse("baseId"+i, "sessionId"+i,c,plan,false);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class));
			
			Bytes bytes = serializer.toBytes(m);
			GetObjectsMessageResponse m2 = (GetObjectsMessageResponse) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			
			assertEquals(m.getPlan().getDetails(), m2.getPlan().getDetails());
			assertEquals(m.isOnlyOids(), m2.isOnlyOids());
			assertEquals(m.getListOfOabs().size(), m2.getListOfOabs().size());
			assertEquals(m.getListOfOabs().iterator().next().get(0).oid, m2.getListOfOabs().iterator().next().get(0).oid);
			assertEquals(m.getListOfOabs().iterator().next().get(0).bytes.getRealSize(), m2.getListOfOabs().iterator().next().get(0).bytes.getRealSize());
			
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}

	public void testGetObjectFromOidMessageResponse() throws Exception{
		NeoDatisConfig config = NeoDatis.getConfig().setStorageEngineClass(InMemoryStorageEngine.class);
		try{
			int size = 10000;
			long start = System.currentTimeMillis();
			
			Session session = new SessionImpl(new IOFileParameter("test",true,config));
			SessionEngine engine = session.getEngine();
			User user = new User("name" , "mail"  , new Profile("profile" , new Function("function")));

			NonNativeObjectInfo nnoi = engine.layer1ToLayer2(user);
			IOdbList<OidAndBytes> oabs = engine.layer2ToLayer3(nnoi);
			
			ClassOid cid = OIDFactory.buildClassOID();

			for(int i=0;i<size;i++){
				GetObjectFromIdMessageResponse m = new GetObjectFromIdMessageResponse("baseId", "sessionId",oabs);
				
				AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig());
				
				Bytes bytes = serializer.toBytes(m);
				GetObjectFromIdMessageResponse m2 = (GetObjectFromIdMessageResponse) serializer.fromBytes(bytes);
				
				assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
				assertEquals(m.getSessionId(), m2.getSessionId());
				assertEquals(m.getDateTime(), m2.getDateTime());
				assertEquals(m.getOabs().get(0).oid, m2.getOabs().get(0).oid);
			}
			long end = System.currentTimeMillis();
			println("Time " + (end-start));
			
		}finally{
		}
	}
	
	public void testDeleteMessage() throws Exception{
		int size = 10000;
		long start = System.currentTimeMillis();
		
		ClassOid cid = OIDFactory.buildClassOID();
		ObjectOid oid = OIDFactory.buildObjectOID(cid);
		for(int i=0;i<size;i++){
			DeleteObjectMessage m = new DeleteObjectMessage("baseId", "sessionId",oid,i%3==0);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class));
			
			Bytes bytes = serializer.toBytes(m);
			DeleteObjectMessage m2 = (DeleteObjectMessage) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			assertEquals(m.getOid(), m2.getOid());
			assertEquals(m.isCascade(), m2.isCascade());
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	public void testDeleteMessageResponse() throws Exception{
		int size = 10000;
		long start = System.currentTimeMillis();
		
		ClassOid cid = OIDFactory.buildClassOID();
		ObjectOid oid = OIDFactory.buildObjectOID( cid);
		for(int i=0;i<size;i++){
			DeleteObjectMessageResponse m = new DeleteObjectMessageResponse("baseId", "sessionId",oid);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig().setOidGeneratorClass(UniqueOidGeneratorImpl.class));
			
			Bytes bytes = serializer.toBytes(m);
			DeleteObjectMessageResponse m2 = (DeleteObjectMessageResponse) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			assertEquals(m.getOid(), m2.getOid());
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	
	public void testSendFileMessage() throws Exception{
		int size = 100;
		long start = System.currentTimeMillis();
		String s = "This is a test string!";
		String baseName = getBaseName();
		FileOutputStream fos = new FileOutputStream(baseName);
		fos.write(s.getBytes());
		fos.close();
		
		for(int i=0;i<size;i++){
			SendFileMessage m = new SendFileMessage("baseId", "sessionId",baseName,"file"+i+".txt", true);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig());
			
			Bytes bytes = serializer.toBytes(m);
			SendFileMessage m2 = (SendFileMessage) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			assertEquals(m.getLocalFileName(), m2.getLocalFileName());
			assertEquals(m.getRemoteFileName(), m2.getRemoteFileName());
			assertEquals(m.isPutFileInServerInbox(), m2.isPutFileInServerInbox());
			
			File f = new File(NeoDatisGlobalConfig.get().getInboxDirectory()+"/"+m.getRemoteFileName());
			assertEquals(true, f.exists());
			f.delete();
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	
	public void testGetFileMessage() throws Exception{
		int size = 100;
		long start = System.currentTimeMillis();
		String s = "This is a test string!";
		String baseName = getBaseName();
		FileOutputStream fos = new FileOutputStream(baseName);
		fos.write(s.getBytes());
		fos.close();
		
		for(int i=0;i<size;i++){
			GetFileMessage m = new GetFileMessage("baseId", "sessionId",false, baseName,true, "file"+i+".txt");
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig());
			
			Bytes bytes = serializer.toBytes(m);
			GetFileMessage m2 = (GetFileMessage) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			assertEquals(m.getLocalFileName(), m2.getLocalFileName());
			assertEquals(m.getRemoteFileName(), m2.getRemoteFileName());
			assertEquals(m.isPutFileInClientInbox(), m2.isPutFileInClientInbox());
			assertEquals(m.isGetFileInServerInbox(), m2.isGetFileInServerInbox());
			
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	
	public void testGetFileMessageResponse() throws Exception{
		int size = 100;
		long start = System.currentTimeMillis();
		String s = "This is a test string!";
		String baseName = getBaseName();
		FileOutputStream fos = new FileOutputStream(baseName);
		fos.write(s.getBytes());
		fos.close();
		
		for(int i=0;i<size;i++){
			GetFileMessageResponse m = new GetFileMessageResponse("baseId", "sessionId",false, baseName,true, "file"+i+".txt");
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig());
			
			Bytes bytes = serializer.toBytes(m);
			GetFileMessageResponse m2 = (GetFileMessageResponse) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			assertEquals(m.getLocalFileName(), m2.getLocalFileName());
			assertEquals(m.getRemoteFileName(), m2.getRemoteFileName());
			assertEquals(m.isPutFileInClientInbox(), m2.isPutFileInClientInbox());
			assertEquals(m.isGetFileInServerInbox(), m2.isGetFileInServerInbox());
			
			File f = new File(NeoDatisGlobalConfig.get().getInboxDirectory()+"/"+m.getLocalFileName());
			assertEquals(true, f.exists());
			f.delete();
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	public void testRemoteProcessMessage() throws Exception{
		int size = 1000;
		long start = System.currentTimeMillis();
		RemoteProcess process = new MyRemoteProcess();
		for(int i=0;i<size;i++){
			RemoteProcessMessage m = new RemoteProcessMessage("baseId", "sessionId",process, true);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig());
			
			Bytes bytes = serializer.toBytes(m);
			RemoteProcessMessage m2 = (RemoteProcessMessage) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			assertEquals(m.isSynchronous(), m2.isSynchronous());
			assertEquals(m.getProcess().getClass(), m2.getProcess().getClass());
			
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	
	public void testRemoteProcessMessageResponse() throws Exception{
		int size = 1000;
		long start = System.currentTimeMillis();
		RemoteProcessReturn processReturn = new DefaultProcessReturn("r1","ok1");
		for(int i=0;i<size;i++){
			RemoteProcessMessageResponse m = new RemoteProcessMessageResponse("baseId", "sessionId",processReturn);
			
			AllSerializers serializer = AllSerializers.getInstance(NeoDatis.getConfig());
			
			Bytes bytes = serializer.toBytes(m);
			RemoteProcessMessageResponse m2 = (RemoteProcessMessageResponse) serializer.fromBytes(bytes);
			
			assertEquals(m.getBaseIdentifier(), m2.getBaseIdentifier());
			assertEquals(m.getSessionId(), m2.getSessionId());
			assertEquals(m.getDateTime(), m2.getDateTime());
			
		}
		long end = System.currentTimeMillis();
		println("Time " + (end-start));
	}
	
}
