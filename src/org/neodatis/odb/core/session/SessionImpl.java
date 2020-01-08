/**
 * 
 */
package org.neodatis.odb.core.session;

import org.neodatis.odb.*;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.event.EventManager;
import org.neodatis.odb.core.event.EventManagerImpl;
import org.neodatis.odb.core.event.NeoDatisEventListener;
import org.neodatis.odb.core.layers.layer2.meta.*;
import org.neodatis.odb.core.layers.layer3.FileFormatVersion;
import org.neodatis.odb.core.layers.layer4.BaseIdentification;
import org.neodatis.odb.core.layers.layer4.OidGenerator;
import org.neodatis.odb.core.oid.DatabaseIdImpl;
import org.neodatis.odb.core.oid.TransactionIdImpl;
import org.neodatis.odb.core.session.info.DatabaseInfo;
import org.neodatis.odb.core.session.info.OpenCloseInfo;
import org.neodatis.odb.core.trigger.CloseListener;
import org.neodatis.odb.core.trigger.CommitListener;
import org.neodatis.tool.DLogger;
import org.neodatis.tool.mutex.Mutex;
import org.neodatis.tool.mutex.MutexFactory;
import org.neodatis.tool.wrappers.OdbThread;
import org.neodatis.tool.wrappers.OdbTime;
import org.neodatis.tool.wrappers.list.IOdbList;
import org.neodatis.tool.wrappers.list.OdbArrayList;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.util.*;

/**
 * @author olivier
 * 
 */
public class SessionImpl implements Session {
	public static final String LOG_ID = "SessionImpl";

	protected BaseIdentification identification;
	protected SessionEngine engine;
	protected MetaModel metaModel;
	protected Cache cache;
	protected String id;
	protected TransactionId transactionId;
	protected boolean isClosed;
	protected boolean isRollbacked;
	protected IOdbList<CommitListener> commitListeners;
	protected IOdbList<CloseListener> closeListeners;
	protected boolean isCommitted;

	protected Map<OID, Mutex> lockedOids;
	protected Map<String, Mutex> lockedClasses;

	protected EventManager eventManager;
	protected NeoDatisConfig config;
	protected OidGenerator oidGenerator;

	protected Map<String, Object> userParameters;

	protected DatabaseInfo databaseInfo;
	protected OpenCloseInfo openCloseInfo;

	/**
	 * @param fileParameter
	 */
	public SessionImpl(BaseIdentification parameter) {
		init(parameter, null);
	}

	/**
	 * @param parameter
	 * @param sessionId
	 */
	public SessionImpl(BaseIdentification parameter, String sessionId) {
		init(parameter, sessionId);
	}

	protected void init(BaseIdentification baseIdentification, String sessionId) {
		this.config = baseIdentification.getConfig();
		this.identification = baseIdentification;

		this.eventManager = new EventManagerImpl();
		lockedOids = new OdbHashMap<OID, Mutex>();
		lockedClasses = new OdbHashMap<String, Mutex>();

		if (sessionId != null) {
			this.id = sessionId;
		} else {
			this.id = new StringBuffer(baseIdentification.getBaseId()).append(System.currentTimeMillis()).toString();
		}

		this.cache = new CacheImpl();
		this.engine = buildSessionEngine();
		this.transactionId = new TransactionIdImpl(this.id);
		this.isClosed = false;
		this.isRollbacked = false;
		this.isCommitted = false;
		commitListeners = new OdbArrayList<CommitListener>();
		closeListeners = new OdbArrayList<CloseListener>();
		oidGenerator = config.getCoreProvider().getOidGenerator();
		initDatabase();

	}

	protected SessionEngine buildSessionEngine() {
		return new SessionEngineImpl(this);
	}

	/**
	 * @return
	 */
	protected void initDatabase() {
		if (engine.isNewDatabase()) {
			writeNewDatabaseHeader();
		} else {
			readDatabaseHeader();

			// check open close
			checkOpenClose();
		}
		initMetaModel();
	}

	protected void checkOpenClose() {
		openCloseInfo = engine.readOpenCloseInfo();

		if (config.isDebugEnabled()) {
			DLogger.debug("Last open of database was on " + openCloseInfo.getIpWhereDatabaseWasOpen() + " at " + new Date(openCloseInfo.getOpenDateTime()));
			DLogger.debug("Last close at " + new Date(openCloseInfo.getCloseDateTime()));
		}
	}

	private void readDatabaseHeader() {
		databaseInfo = engine.readDatabaseHeader();
	}

	private void writeNewDatabaseHeader() {
		DatabaseId databaseId = DatabaseIdImpl.fromString(UUID.randomUUID().toString());
		databaseInfo = new DatabaseInfo(databaseId, false, "none", FileFormatVersion.CURRENT, getConfig().getDatabaseCharacterEncoding(),
				getOidGenerator().getClass().getName());
		engine.writeDatabaseHeader(databaseInfo);
	}

	/**
	 * @return
	 */
	protected void initMetaModel() {

		metaModel = new MetaModelImpl(this.getConfig());
		engine.loadMetaModel(metaModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.session.Session#addClasses(org.neodatis.odb.core
	 * .layers.layer2.meta.ClassInfoList)
	 */
	public ClassInfoList addClasses(ClassInfoList ciList) {
		metaModel.addClasses(ciList);
		engine.storeClassInfos(ciList);
		return ciList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.session.Session#addObjectToCache(org.neodatis.odb
	 * .OID, java.lang.Object,
	 * org.neodatis.odb.core.layers.layer2.meta.ObjectInfoHeader)
	 */
	public void addObjectToCache(OID oidCrossSession, Object o, ObjectInfoHeader oih) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#close()
	 */
	public void close() {
		if (config.isSessionAutoCommit()) {
			if (!isRollbacked) {
				commit();
			}
		} else {
			if (!isCommitted) {
				rollback();
			}
		}

		engine.close();
		isClosed = true;

		manageCloseListenersAfter();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#commit()
	 */
	public void commit() {
		if (isRollbacked) {
			return;
		}
		if (isCommitted) {
			return;
		}
		manageCommitListenersBefore();
		engine.commit();
		manageCommitListenersAfter();
		isCommitted = true;
		unlockObjectsAndClasses();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#getBaseIdentification()
	 */
	public BaseIdentification getBaseIdentification() {
		return identification;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#getCache()
	 */
	public Cache getCache() {
		return cache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#getClassInfo(java.lang.String)
	 */
	public ClassInfo getClassInfo(String fullClassName) {
		if (ODBType.getFromName(fullClassName).isNative()) {
			return null;
		}
		MetaModel metaModel = getMetaModel();

		if (metaModel.existClass(fullClassName)) {
			return metaModel.getClassInfo(fullClassName, true);
		}
		ClassInfo ci = null;
		ClassInfoList ciList = null;
		ciList = engine.introspectClass(fullClassName);
		// to enable junit tests
		addClasses(ciList);
		// old:For client Server : reset meta model
		// if (!storageEngine.isLocal()) {
		// metaModel = session.getMetaModel();
		// }
		// /old
		ci = metaModel.getClassInfo(fullClassName, true);
		return ci;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#getId()
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#getMetaModel()
	 */
	public MetaModel getMetaModel() {
		return metaModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.session.Session#getObjectInfoHeaderFromOid(org.
	 * neodatis.odb.OID, boolean)
	 */
	public ObjectInfoHeader getObjectInfoHeaderFromOid(OID oidCrossSession, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public int getExecutionType() {
		return ExecutionType.LOCAL_CLIENT;
	}

	public boolean isLocal() {
		return true;
	}

	public boolean isRollbacked() {
		return isRollbacked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#rollback()
	 */
	public void rollback() {
		engine.rollback();
		isRollbacked = true;
		unlockObjectsAndClasses();
	}

	public SessionEngine getEngine() {
		return engine;
	}

	public TransactionId getCurrentTransactionId() {
		return transactionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#clearCache()
	 */
	public void clearCache() {
		cache.clear();
	}

	public void update(Observable o, Object arg) {
		throw new NeoDatisRuntimeException(NeoDatisError.NOT_YET_IMPLEMENTED);

	}

	public void addCommitListener(CommitListener commitListener) {
		this.commitListeners.add(commitListener);
	}

	public IOdbList<CommitListener> getCommitListeners() {
		return commitListeners;
	}

	public IOdbList<CloseListener> getCloseListeners() {
		return closeListeners;
	}

	private void manageCommitListenersAfter() {
		if (commitListeners == null || commitListeners.isEmpty()) {
			return;
		}
		Iterator<CommitListener> iterator = commitListeners.iterator();
		CommitListener commitListener = null;
		while (iterator.hasNext()) {
			commitListener = iterator.next();
			commitListener.afterCommit();
		}
	}

	private void manageCloseListenersAfter() {
		if (closeListeners == null || closeListeners.isEmpty()) {
			return;
		}
		Iterator<CloseListener> iterator = closeListeners.iterator();
		CloseListener closeListener = null;
		while (iterator.hasNext()) {
			closeListener = iterator.next();
			closeListener.afterClose();
		}
	}

	private void manageCommitListenersBefore() {
		if (commitListeners == null || commitListeners.isEmpty()) {
			return;
		}
		Iterator<CommitListener> iterator = commitListeners.iterator();
		CommitListener commitListener = null;
		while (iterator.hasNext()) {
			commitListener = iterator.next();
			commitListener.beforeCommit();
		}
	}

	public void setMetaModel(MetaModel metaModel) {
		this.metaModel = metaModel;

		// persist classes
		for (ClassInfo ci : metaModel.getAllClasses()) {
			engine.storeClassInfo(ci);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#transactionIsPending()
	 */
	public boolean transactionIsPending() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#endCurrentAction()
	 */
	public void endCurrentAction() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#setCurrentAction(int)
	 */
	public void setCurrentAction(int action) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neodatis.odb.core.session.Session#setId(java.lang.String)
	 */
	public void setId(String sessionId) {
		this.id = sessionId;
	}

	public synchronized void lockOidForSession(OID oid, long timeout) throws InterruptedException {
		boolean locked = false;
		long start = OdbTime.getCurrentTimeInMs();

		if (config.isDebugEnabled(LOG_ID)) {
			start = OdbTime.getCurrentTimeInMs();
			DLogger.debug("Trying to lock object with oid " + oid + " - session id=" + getId() + " - Thread = " + OdbThread.getCurrentThreadName());
		}
		try {
			Mutex mutex = lockedOids.get(oid);
			if (mutex == null) {
				mutex = MutexFactory.get(getBaseIdentification().getBaseId() + oid.oidToString());
				locked = mutex.attempt(config.getTimeoutToAcquireMutex());
				if (!locked) {
					throw new LockTimeOutException("Object with oid " + oid.oidToString() + " - session id " + getId() + " - Thread = "
							+ OdbThread.getCurrentThreadName());
				}
				lockedOids.put(oid, mutex);

				return;
			}
		} finally {
			if (locked) {
				if (config.isDebugEnabled(LOG_ID)) {
					DLogger.debug("Object with oid " + oid + " locked (" + (OdbTime.getCurrentTimeInMs() - start) + "ms) - " + getId() + " - Thread = "
							+ OdbThread.getCurrentThreadName());
				}
			}
		}
	}

	public synchronized void lockClassForSession(String fullClassName, long timeout) throws InterruptedException {
		long start = OdbTime.getCurrentTimeInMs();
		boolean locked = false;
		if (config.isDebugEnabled(LOG_ID)) {
			start = OdbTime.getCurrentTimeInMs();
			DLogger.debug(String.format("CM:Trying to lock class %s - id=%s", fullClassName, getId()));
		}
		try {
			Mutex mutex = lockedClasses.get(fullClassName);
			if (mutex != null) {
				mutex = MutexFactory.get(getBaseIdentification().getBaseId() + fullClassName);
				locked = mutex.attempt(timeout);
				if (!locked) {
					throw new LockTimeOutException("Class with name " + fullClassName);
				}

				lockedClasses.put(fullClassName, mutex);
			}
			return;
		} finally {
			if (config.isDebugEnabled(LOG_ID)) {
				DLogger.debug(String.format("Class %s locked (%dms) - %s", fullClassName, (OdbTime.getCurrentTimeInMs() - start), getId()));
			}
		}
	}

	public synchronized void unlockOidForSession(OID oid) throws InterruptedException {
		long start = OdbTime.getCurrentTimeInMs();

		if (config.isDebugEnabled(LOG_ID)) {
			start = OdbTime.getCurrentTimeInMs();
			DLogger.debug("Trying to unlock lock object with oid " + oid + " - id=" + getId());
		}

		try {
			Mutex mutex = lockedOids.get(oid);
			if (mutex != null) {
				mutex.release(getId());
				lockedOids.remove(oid);
			}
		} finally {
			if (config.isDebugEnabled(LOG_ID)) {
				DLogger.debug("Object with oid " + oid + " unlocked (" + (OdbTime.getCurrentTimeInMs() - start) + "ms) - " + getId());
			}
		}
	}

	public synchronized void unlockClass(String fullClassName) throws InterruptedException {
		long start = OdbTime.getCurrentTimeInMs();

		if (config.isDebugEnabled(LOG_ID)) {
			start = OdbTime.getCurrentTimeInMs();
			DLogger.debug("Trying to unlock class " + fullClassName + " - id=" + getId());
		}

		try {
			Mutex mutex = lockedClasses.get(fullClassName);
			if (mutex != null) {
				mutex.release(getId());
				lockedClasses.remove(fullClassName);
			}
		} finally {
			if (config.isDebugEnabled(LOG_ID)) {
				DLogger.debug("Class  " + fullClassName + " unlocked (" + (OdbTime.getCurrentTimeInMs() - start) + "ms) - " + getId());
			}
		}
	}

	/**
	 * Release all objects and classes lock by this session
	 * 
	 */
	public void unlockObjectsAndClasses() {
		Iterator<Mutex> objectMutexes = lockedOids.values().iterator();
		while (objectMutexes.hasNext()) {
			objectMutexes.next().release(getId());
		}
		Iterator<Mutex> classMutexes = lockedClasses.values().iterator();
		while (classMutexes.hasNext()) {
			classMutexes.next().release(getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neodatis.odb.core.session.Session#registerEventListenerFor(org.neodatis
	 * .odb.NeoDatisEventType, org.neodatis.odb.EventListener)
	 */
	public void registerEventListenerFor(NeoDatisEventType neoDatisEventType, NeoDatisEventListener eventListener) {
		eventManager.addEventListener(neoDatisEventType, eventListener);
	}

	public void updateMetaModel() {
		MetaModel metaModel = getMetaModel();
		DLogger.info("Automatic refactoring : updating meta model");

		// User classes :
		List<ClassInfo> userClasses = new ArrayList<ClassInfo>(metaModel.getUserClasses());
		Iterator<ClassInfo> iterator = userClasses.iterator();
		// Iterator iterator = metaModel.getUserClasses().iterator();

		while (iterator.hasNext()) {
			engine.storeClassInfo(iterator.next());
		}
		// System classes
		iterator = metaModel.getSystemClasses().iterator();
		while (iterator.hasNext()) {
			engine.storeClassInfo(iterator.next());
		}

	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public NeoDatisConfig getConfig() {
		return config;
	}

	public OidGenerator getOidGenerator() {
		return oidGenerator;
	}

	public void addCloseListener(CloseListener closeListener) {
		this.closeListeners.add(closeListener);
	}

	public Object getUserParameter(String name, boolean remove) {
		if (userParameters == null) {
			return null;
		}
		Object o = userParameters.get(name);

		if (o != null && remove) {
			userParameters.remove(o);
		}
		return o;

	}

	public synchronized void setUserParameter(String name, Object object) {
		if (userParameters == null) {
			userParameters = new HashMap<String, Object>();
		}
		userParameters.put(name, object);

	}

	public DatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}

	public OpenCloseInfo getOpenCloseInfo() {
		return openCloseInfo;
	}
}
