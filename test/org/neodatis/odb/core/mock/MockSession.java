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
package org.neodatis.odb.core.mock;

import org.neodatis.odb.*;
import org.neodatis.odb.core.event.EventManager;
import org.neodatis.odb.core.event.NeoDatisEventListener;
import org.neodatis.odb.core.layers.layer2.meta.*;
import org.neodatis.odb.core.layers.layer4.BaseIdentification;
import org.neodatis.odb.core.layers.layer4.IOFileParameter;
import org.neodatis.odb.core.layers.layer4.OidGenerator;
import org.neodatis.odb.core.oid.DatabaseIdImpl;
import org.neodatis.odb.core.session.Cache;
import org.neodatis.odb.core.session.CacheImpl;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.core.session.info.DatabaseInfo;
import org.neodatis.odb.core.session.info.OpenCloseInfo;
import org.neodatis.odb.core.trigger.CloseListener;
import org.neodatis.odb.core.trigger.CommitListener;
import org.neodatis.odb.core.trigger.TriggerManager;

import java.util.Observable;


/**
 * A fake session used for tests
 * 
 * @author olivier s
 * 
 */
public class MockSession implements Session {
	protected MetaModel metaModel;
	protected Cache cache;
	protected SessionEngine engine;
	public MockSession(String baseIdentification) {
		this.metaModel = new MetaModelImpl(NeoDatis.getConfig());
		this.cache = new CacheImpl();
		this.engine = new MockSessionEngine(this);
	}


	public ClassInfoList addClasses(ClassInfoList ciList) {
		metaModel.addClasses(ciList);
		return ciList;
	}

	public void addObjectToCache(OID oidCrossSession, Object o, ObjectInfoHeader oih) {
		// TODO Auto-generated method stub
		
	}

	public BaseIdentification getBaseIdentification() {
		return new IOFileParameter("mock id",true,NeoDatis.getConfig());
	}

	public Cache getCache() {
		return cache;
	}

	public String getId() {
		return "mock";
	}

	public ObjectInfoHeader getObjectInfoHeaderFromOid(OID oidCrossSession, boolean b) {
		return null;
	}

	public TriggerManager getTriggerManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isClosed() {
		return false;
	}

	public boolean isRollbacked() {
		return false;
	}


	public MetaModel getMetaModel() {
		// TODO Auto-generated method stub
		return metaModel;
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#isLocal()
	 */
	public boolean isLocal() {
		return false;
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#close()
	 */
	public void close() {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#commit()
	 */
	public void commit() {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#getClassInfo(java.lang.String)
	 */
	public ClassInfo getClassInfo(String fullClassName) {
		return metaModel.getClassInfo(fullClassName, true);
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#getCurrentTransactionId()
	 */
	public TransactionId getCurrentTransactionId() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#getDatabaseId()
	 */
	public DatabaseId getDatabaseId() {
		// TODO Auto-generated method stub
		return new DatabaseIdImpl("test");
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#getEngine()
	 */
	public SessionEngine getEngine() {
		return engine;
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#rollback()
	 */
	public void rollback() {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#clearCache()
	 */
	public void clearCache() {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#setMetaModel(org.neodatis.odb.core.layers.layer2.meta.MetaModel)
	 */
	public void setMetaModel(MetaModel metaModel) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#transactionIsPending()
	 */
	public boolean transactionIsPending() {
		// TODO Auto-generated method stub
		return false;
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#setId(java.lang.String)
	 */
	public void setId(String sessionId) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#unlockObjectsAndClasses()
	 */
	public void unlockObjectsAndClasses() {
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#unlockClass(java.lang.String, org.neodatis.odb.core.session.Session)
	 */
	public synchronized void unlockClass(String fullClassName, Session session) throws InterruptedException {
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#unlockOidForSession(org.neodatis.odb.OID, org.neodatis.odb.core.session.Session)
	 */
	public synchronized void unlockOidForSession(OID oid, Session session) throws InterruptedException {
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#lockClassForSession(java.lang.String, org.neodatis.odb.core.session.Session, long)
	 */
	public synchronized void lockClassForSession(String fullClassName, Session session, long timeout) throws InterruptedException {
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#lockOidForSession(org.neodatis.odb.OID, org.neodatis.odb.core.session.Session, long)
	 */
	public synchronized void lockOidForSession(OID oid, Session session, long timeout) throws InterruptedException {
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#getEventManager()
	 */
	public EventManager getEventManager() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#lockClassForSession(java.lang.String, long)
	 */
	public void lockClassForSession(String fullClassName, long timeout) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#lockOidForSession(org.neodatis.odb.OID, long)
	 */
	public void lockOidForSession(OID oid, long timeout) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#registerEventListenerFor(org.neodatis.odb.NeoDatisEventType, org.neodatis.odb.core.event.NeoDatisEventListener)
	 */
	public void registerEventListenerFor(NeoDatisEventType neoDatisEventType, NeoDatisEventListener eventListener) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#unlockClass(java.lang.String)
	 */
	public void unlockClass(String fullClassName) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#unlockOidForSession(org.neodatis.odb.OID)
	 */
	public void unlockOidForSession(OID oid) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#updateMetaModel()
	 */
	public void updateMetaModel() {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.Session#getConfig()
	 */
	public NeoDatisConfig getConfig() {
		return NeoDatis.getConfig();
	}


	public OidGenerator getOidGenerator() {
		// TODO Auto-generated method stub
		return null;
	}


	public int getExecutionType() {
		// TODO Auto-generated method stub
		return 0;
	}


	public void addCloseListener(CloseListener closeListener) {
		// TODO Auto-generated method stub
		
	}


	public void addCommitListener(CommitListener commitListener) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Object getUserParameter(String name, boolean remove) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setUserParameter(String name, Object object) {
		// TODO Auto-generated method stub
		
	}


	public DatabaseInfo getDatabaseInfo() {
		return null;
	}


	public OpenCloseInfo getOpenCloseInfo() {
		return null;
	}

}
