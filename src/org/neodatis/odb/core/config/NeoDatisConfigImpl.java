/**
 * 
 */
package org.neodatis.odb.core.config;

import org.neodatis.odb.DatabaseStartupManager;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.NeoDatisGlobalConfig;
import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.core.CoreProvider;
import org.neodatis.odb.core.CoreProviderImpl;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.event.FileHasBeenReceived;
import org.neodatis.odb.core.layers.layer3.DataConverterImpl;
import org.neodatis.odb.core.layers.layer4.plugin.jdbmv3.JDBM3Plugin;
import org.neodatis.odb.core.oid.uuid.UniqueOidGeneratorImpl;
import org.neodatis.odb.core.query.IQueryExecutorCallback;
import org.neodatis.odb.core.server.MessageStreamerImpl;
import org.neodatis.tool.wrappers.ConstantWrapper;
import org.neodatis.tool.wrappers.NeoDatisClassLoader;
import org.neodatis.tool.wrappers.map.OdbHashMap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author olivier
 * 
 */
public class NeoDatisConfigImpl implements NeoDatisConfig {
	protected boolean coreProviderInit = false;
	protected boolean debugEnabled = false;
	protected boolean logAll = false;
	protected int debugLevel = 100;
	protected Map<String, String> logIds = null;
	protected boolean infoEnabled = false;
	protected long maxNumberOfObjectInCache = 3000000;
	protected boolean automaticCloseFileOnExit = false;

	protected String defaultDatabaseCharacterEncoding = "ISO8859-1";
	protected String databaseCharacterEncoding = defaultDatabaseCharacterEncoding;

	protected boolean throwExceptionWhenInconsistencyFound = true;
	protected boolean checkMetaModelCompatibility = true;
	protected boolean logSchemaEvolutionAnalysis = false;
	protected boolean monitorMemory = false;
	protected boolean debugLayers = false;
	protected boolean debugMessageStreamer = false;
	protected boolean debugStorageEngine = false;
	protected long timeoutToAcquireMutex = 1000;

	protected int socketTimeoutForClientServer = 5 * 1000;
	protected int dataBlockSize = 2048;
	

	/** The database name */
	protected String baseName;

	/**
	 * A boolean value to indicate if ODB can create empty constructor when not
	 * available
	 */
	protected boolean enableEmptyConstructorCreation = true;

	// For multi thread
	/**
	 * a boolean value to specify if ODBFactory waits a little to re-open a file
	 * when a file is locked
	 */
	protected boolean retryIfFileIsLocked = true;
	/** How many times ODBFactory tries to open the file when it is locked */
	protected int numberOfRetryToOpenFile = 5;
	/** How much time (in ms) ODBFactory waits between each retry */
	protected long retryTimeout = 100;

	/** Automatically increase cache size when it is full */
	protected boolean automaticallyIncreaseCacheSize = false;

	protected boolean useCache = true;

	protected boolean logServerStartupAndShutdown = true;

	protected boolean logServerConnections = false;

	/** The default btree size for index btrees */
	protected int defaultIndexBTreeDegree = 20;

	/** The default btree size for collection btrees */
	protected int defaultCollectionBTreeDegree = 20;

	/**
	 * The type of cache. If true, the cache use weak references that allows
	 * very big inserts,selects like a million of objects. But it is a little
	 * bit slower than setting to false
	 */
	protected boolean useLazyCache = false;

	/** To indicate if warning must be displayed */
	protected boolean displayWarnings = true;

	protected IQueryExecutorCallback queryExecutorCallback = null;

	/** Scale used for average action * */
	protected int scaleForAverageDivision = 2;

	/** Round Type used for the average division */
	protected int roundTypeForAverageDivision = ConstantWrapper.ROUND_TYPE_FOR_AVERAGE_DIVISION;

	/** for IO atomic : password for encryption */
	protected String encryptionPassword;

	/** The core provider is the provider of core object implementation for ODB */
	protected CoreProvider coreProvider;

	/** To indicate if NeoDatis must check the runtime version, defaults to yes */
	protected boolean checkRuntimeVersion = true;

	/**
	 * To specify if NeoDatis must automatically reconnect objects loaded in
	 * previous session. With with flag on, user does not need to manually
	 * reconnect an object. Default value = true
	 */
	protected boolean reconnectObjectsToSession = false;

	protected ClassLoader classLoader = NeoDatisClassLoader.getCurrent();
	protected Class messageStreamerClass = MessageStreamerImpl.class;

	/**
	 * To activate or desactivate the use of index
	 * 
	 */
	protected boolean useIndex = true;

	/**
	 * Used to let same vm mode use an odb connection on severals different
	 * threads
	 * 
	 */
	protected boolean shareSameVmConnectionMultiThread = true;
	protected boolean lockObjectsOnSelect = false;
	/**
	 * Used to specify a base directory for database creations, default is .
	 * (current directory)
	 */
	protected String baseDirectory = ".";

	/**
	 * used to indicate if lazy instantiation must be used in cs mode Default
	 * behavior is that where retrieving a list of objects, the server return
	 * the list of meta representation (NonNativeObjectInfo:NNOI) to the client.
	 * The server then uses a specific list LazySimpleListOfAOI that keeps the
	 * NNOIs and only instantiate the the real object (java object) when user
	 * requests it.
	 * */
	protected boolean useLazyInstantiationInServerMode = true;

	/** a DatabaseStartupManager is called on every database open */
	protected DatabaseStartupManager databaseStartupManager = null;

	/** to tell NeoDatis if session must be auto committed */
	protected boolean sessionAutoCommit = true;

	protected Class storageEngineClass;

	protected boolean transactional;
	protected String user;
	protected String password;
	protected String homeDirectory;
	protected String host;
	protected int port;
	protected boolean isLocal;

	protected boolean allowDirtyReads;
	protected boolean commitNoSync;
	/**
	 * to indicate if storage must use its native config or take from this
	 * properties object
	 * 
	 */
	protected boolean useNativeConfig;
	protected List<Server> servers;

	/**
	 * The default directory to receive files
	 */
	protected String inboxDirectory;

	/** To indicate if client server mode is using ssl */
	protected boolean ssl;

	protected Class oidGeneratorClass;

	/** To specify if the underlying storage engine can use the its cache or not */
	protected boolean useStorageEngineCache;

	/** To specify if the oid generator can use cache */
	protected boolean oidGeneratorUseCache;

	/** a field to send a user info (String) to the client */
	protected String userInfo;
	/** to tell neodatis if execptions thrown by NeoDatis engine must be logged to file */
	private boolean logExceptionToFile;
	
	/** a callback to be called when the file server receives a file
	 * 
	 */
	private FileHasBeenReceived fileHasBeenReceivedCallback;

	public NeoDatisConfigImpl(boolean override) {
		super();
		this.transactional = true;
		this.isLocal = true;
		servers = new ArrayList<Server>();
		this.coreProvider = new CoreProviderImpl(this);
		this.storageEngineClass = JDBM3Plugin.class;
		this.inboxDirectory = "inbox";
		this.oidGeneratorClass = UniqueOidGeneratorImpl.class;
		this.oidGeneratorUseCache = true;
		this.useStorageEngineCache = true;

		if (override) {
			// override values with global config
			this.checkMetaModelCompatibility = NeoDatisGlobalConfig.get().checkMetaModelCompatibility();
			this.scaleForAverageDivision = NeoDatisGlobalConfig.get().getScaleForAverageDivision();
			this.roundTypeForAverageDivision = NeoDatisGlobalConfig.get().getRoundTypeForAverageDivision();
			this.storageEngineClass = NeoDatisGlobalConfig.get().getStorageEngineClass();
			this.messageStreamerClass = NeoDatisGlobalConfig.get().getMessageStreamerClass();
			this.inboxDirectory = NeoDatisGlobalConfig.get().getInboxDirectory();
			this.debugLayers = NeoDatisGlobalConfig.get().debugLayers();

		}

		String sSocketTimeoutForClientServer = System.getProperty("socketTimeoutForClientServer");
		
		if(sSocketTimeoutForClientServer!=null) {
			this.socketTimeoutForClientServer = Integer.parseInt(sSocketTimeoutForClientServer);
		}
		
	}

	/**
	 * @throws ClassNotFoundException
	 * 
	 */
	public NeoDatisConfig updateFromFile(String fileName) throws ClassNotFoundException {
		Properties properties = new Properties();
		try {
			properties = ConfigFileReader.read(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String storageEngineClassName = properties.getProperty(NeoDatisConfigTokens.STORAGE_ENGINE);
		if (storageEngineClassName != null) {
			setStorageEngineClass(Class.forName(storageEngineClassName));
		}

		String autocommit = properties.getProperty(NeoDatisConfigTokens.SESSION_AUTO_COMMIT);

		if (autocommit != null) {
			setSessionAutoCommit(autocommit.equals("true"));
		} else {
			setSessionAutoCommit(false);
		}
		String serverInboxDirectory = properties.getProperty(NeoDatisConfigTokens.INBOX_DIRECTORY);

		if (serverInboxDirectory != null) {
			setInboxDirectory(serverInboxDirectory);
		}
		return this;
	}

	public boolean isSessionAutoCommit() {
		return sessionAutoCommit;
	}

	public NeoDatisConfig setSessionAutoCommit(boolean sessionAutoCommit) {
		this.sessionAutoCommit = sessionAutoCommit;
		return this;
	}

	public Class getStorageEngineClass() {
		return storageEngineClass;
	}

	public NeoDatisConfig setStorageEngineClass(Class storageEngineClass) {
		this.storageEngineClass = storageEngineClass;
		return this;
	}

	/**
	 * @return
	 */
	public boolean reconnectObjectsToSession() {
		return reconnectObjectsToSession;
	}

	public NeoDatisConfig setReconnectObjectsToSession(boolean reconnectObjectsToSession) {
		this.reconnectObjectsToSession = reconnectObjectsToSession;
		return this;
	}

	public NeoDatisConfig addLogId(String logId) {
		if (logIds == null) {
			logIds = new OdbHashMap<String, String>();
		}
		logIds.put(logId, logId);
		return this;
	}

	public NeoDatisConfig removeLogId(String logId) {
		if (logIds == null) {
			logIds = new OdbHashMap<String, String>();
		}
		logIds.remove(logId);
		return this;
	}

	public boolean isDebugEnabled(String logId) {
		if (!debugEnabled) {
			return false;
		}
		if (logAll) {
			return true;
		}

		if (logIds == null || logIds.size() == 0) {
			return false;
		}

		return logIds.containsKey(logId);
	}

	public NeoDatisConfig setDebugEnabled(int level, boolean debug) {
		debugEnabled = debug;
		debugLevel = level;
		return this;
	}

	public boolean isInfoEnabled() {
		return infoEnabled;
	}

	public boolean isInfoEnabled(String logId) {
		// return false;

		if (logAll) {
			return true;
		}

		if (logIds == null || logIds.size() == 0) {
			return false;
		}

		return logIds.containsKey(logId);

		// return false;
	}

	public NeoDatisConfig setInfoEnabled(boolean infoEnabled) {
		this.infoEnabled = infoEnabled;
		return this;
	}

	public long getMaxNumberOfObjectInCache() {
		return maxNumberOfObjectInCache;
	}

	public NeoDatisConfig setMaxNumberOfObjectInCache(long maxNumberOfObjectInCache) {
		this.maxNumberOfObjectInCache = maxNumberOfObjectInCache;
		return this;
	}

	public int getNumberOfRetryToOpenFile() {
		return numberOfRetryToOpenFile;
	}

	public NeoDatisConfig setNumberOfRetryToOpenFile(int numberOfRetryToOpenFile) {
		this.numberOfRetryToOpenFile = numberOfRetryToOpenFile;
		return this;
	}

	public long getRetryTimeout() {
		return retryTimeout;
	}

	public NeoDatisConfig setRetryTimeout(long retryTimeout) {
		this.retryTimeout = retryTimeout;
		return this;
	}

	public boolean retryIfFileIsLocked() {
		return retryIfFileIsLocked;
	}

	public NeoDatisConfig setRetryIfFileIsLocked(boolean retryIfFileIsLocked) {
		this.retryIfFileIsLocked = retryIfFileIsLocked;
		return this;
	}

	public boolean isMultiThread() {
		return retryIfFileIsLocked;
	}

	public NeoDatisConfig useMultiThread(boolean yes) {
		useMultiThread(yes, numberOfRetryToOpenFile);
		return this;
	}

	public NeoDatisConfig useMultiThread(boolean yes, int numberOfThreads) {
		setRetryIfFileIsLocked(yes);
		if (yes) {
			setNumberOfRetryToOpenFile(numberOfThreads * 10);
			setRetryTimeout(50);
		}
		return this;
	}

	public boolean throwExceptionWhenInconsistencyFound() {
		return throwExceptionWhenInconsistencyFound;
	}

	public NeoDatisConfig setThrowExceptionWhenInconsistencyFound(boolean throwExceptionWhenInconsistencyFound) {
		this.throwExceptionWhenInconsistencyFound = throwExceptionWhenInconsistencyFound;
		return this;
	}

	public boolean automaticallyIncreaseCacheSize() {
		return automaticallyIncreaseCacheSize;
	}

	public NeoDatisConfig setAutomaticallyIncreaseCacheSize(boolean automaticallyIncreaseCache) {
		automaticallyIncreaseCacheSize = automaticallyIncreaseCache;
		return this;
	}

	/**
	 * @return Returns the debugLevel.
	 */
	public int getDebugLevel() {
		return debugLevel;
	}

	/**
	 * @param debugLevel
	 *            The debugLevel to set.
	 */
	public NeoDatisConfig setDebugLevel(int debugLevel) {
		this.debugLevel = debugLevel;
		return this;
	}

	public boolean checkMetaModelCompatibility() {
		return checkMetaModelCompatibility;
	}

	public NeoDatisConfig setCheckMetaModelCompatibility(boolean checkModelCompatibility) {
		checkMetaModelCompatibility = checkModelCompatibility;
		return this;
	}

	public boolean automaticCloseFileOnExit() {
		return automaticCloseFileOnExit;
	}

	public NeoDatisConfig setAutomaticCloseFileOnExit(boolean automaticFileClose) {
		automaticCloseFileOnExit = automaticFileClose;
		return this;
	}

	public boolean isLogAll() {
		return logAll;
	}

	public NeoDatisConfig setLogAll(boolean logAll) {
		this.logAll = logAll;
		return this;
	}

	public boolean logServerConnections() {
		return logServerConnections;
	}

	public NeoDatisConfig setLogServerConnections(boolean logServerConnections) {
		this.logServerConnections = logServerConnections;
		return this;
	}

	public int getDefaultIndexBTreeDegree() {
		return defaultIndexBTreeDegree;
	}

	public NeoDatisConfig setDefaultIndexBTreeDegree(int defaultIndexBTreeSize) {
		defaultIndexBTreeDegree = defaultIndexBTreeSize;
		return this;
	}

	public int getDefaultCollectionBTreeDegree() {
		return defaultCollectionBTreeDegree;
	}

	public NeoDatisConfig setDefaultCollectionBTreeDegree(int defaultIndexBTreeSize) {
		defaultCollectionBTreeDegree = defaultIndexBTreeSize;
		return this;
	}

	public boolean useLazyCache() {
		return useLazyCache;
	}

	public NeoDatisConfig setUseLazyCache(boolean useLazyCache) {
		this.useLazyCache = useLazyCache;
		return this;
	}

	/**
	 * @return the queryExecutorCallback
	 */
	public IQueryExecutorCallback getQueryExecutorCallback() {
		return queryExecutorCallback;
	}

	/**
	 * @param queryExecutorCallback
	 *            the queryExecutorCallback to set
	 */
	public NeoDatisConfig setQueryExecutorCallback(IQueryExecutorCallback queryExecutorCallback) {
		this.queryExecutorCallback = queryExecutorCallback;
		return this;
	}

	/**
	 * @return the useCache
	 */
	public boolean useCache() {
		return useCache;
	}

	/**
	 * @param useCache
	 *            the useCache to set
	 */
	public NeoDatisConfig setUseCache(boolean useCache) {
		this.useCache = useCache;
		return this;
	}

	public boolean monitoringMemory() {
		return monitorMemory;
	}

	public NeoDatisConfig setMonitorMemory(boolean yes) {
		monitorMemory = yes;
		return this;
	}

	public boolean displayWarnings() {
		return displayWarnings;
	}

	public NeoDatisConfig setDisplayWarnings(boolean yesOrNo) {
		displayWarnings = yesOrNo;
		return this;
	}

	public int getScaleForAverageDivision() {
		return scaleForAverageDivision;
	}

	public NeoDatisConfig setScaleForAverageDivision(int scaleForAverageDivision) {
		this.scaleForAverageDivision = scaleForAverageDivision;
		return this;
	}

	public int getRoundTypeForAverageDivision() {
		return roundTypeForAverageDivision;
	}

	public NeoDatisConfig setRoundTypeForAverageDivision(int roundTypeForAverageDivision) {
		this.roundTypeForAverageDivision = roundTypeForAverageDivision;
		return this;
	}

	public boolean enableEmptyConstructorCreation() {
		return enableEmptyConstructorCreation;
	}

	public NeoDatisConfig setEnableEmptyConstructorCreation(boolean enableEmptyConstructorCreation) {
		this.enableEmptyConstructorCreation = enableEmptyConstructorCreation;
		return this;
	}

	public String getEncryptionPassword() {
		return encryptionPassword;
	}

	public CoreProvider getCoreProvider() {
		if (!coreProviderInit) {
			coreProviderInit = true;
			try {
				coreProvider.init2();
			} catch (Exception e) {
				throw new NeoDatisRuntimeException(NeoDatisError.ERROR_IN_CORE_PROVIDER_INITIALIZATION.addParameter("Core Provider"), e);
			}

		}
		return coreProvider;
	}

	public NeoDatisConfig setCoreProvider(CoreProvider coreProvider) {
		this.coreProvider = coreProvider;
		return this;
	}

	public String getDatabaseCharacterEncoding() {
		return databaseCharacterEncoding;
	}

	public NeoDatisConfig setDatabaseCharacterEncoding(String dbCharacterEncoding) throws UnsupportedEncodingException {
		if (dbCharacterEncoding != null) {
			// Checks if encoding is valid, using it in the String.getBytes
			// method
			new DataConverterImpl(false, dbCharacterEncoding, this).testEncoding(dbCharacterEncoding);
			databaseCharacterEncoding = dbCharacterEncoding;
		} else {
			databaseCharacterEncoding = null;
		}
		return this;
	}

	public NeoDatisConfig setLatinDatabaseCharacterEncoding() throws UnsupportedEncodingException {
		databaseCharacterEncoding = defaultDatabaseCharacterEncoding;
		return this;
	}

	public boolean hasEncoding() {
		return databaseCharacterEncoding != null;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public NeoDatisConfig setClassLoader(ClassLoader cl) {

		if (cl == null) {
			throw new NeoDatisRuntimeException(NeoDatisError.INTERNAL_ERROR.addParameter("Class loader is null!"));
		}

		classLoader = cl;

		// throw new ODBRuntimeException(NeoDatisError.NOT_YET_IMPLEMENTED);
		// getCoreProvider().getClassIntrospector().reset();
		// getCoreProvider().getClassPool().reset();
		return this;

	}

	public boolean checkRuntimeVersion() {
		return checkRuntimeVersion;
	}

	public NeoDatisConfig setCheckRuntimeVersion(boolean checkJavaRuntimeVersion) {
		checkRuntimeVersion = checkJavaRuntimeVersion;
		return this;
	}

	/**
	 * @return
	 */
	public Class getMessageStreamerClass() {
		// throw new ODBRuntimeException(NeoDatisError.NOT_YET_IMPLEMENTED);
		return messageStreamerClass;
	}

	public NeoDatisConfig setMessageStreamerClass(Class messageStreamerClass) {
		this.messageStreamerClass = messageStreamerClass;
		return this;
	}

	public boolean logServerStartupAndShutdown() {
		return logServerStartupAndShutdown;
	}

	public NeoDatisConfig setLogServerStartupAndShutdown(boolean logServerStartup) {
		logServerStartupAndShutdown = logServerStartup;
		return this;
	}

	public boolean useIndex() {
		return useIndex;
	}

	public NeoDatisConfig setUseIndex(boolean useIndex) {
		this.useIndex = useIndex;
		return this;
	}

	public boolean isDebugEnabled() {
		return debugEnabled;
	}

	public NeoDatisConfig setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
		return this;
	}

	public boolean shareSameVmConnectionMultiThread() {
		return shareSameVmConnectionMultiThread;
	}

	public NeoDatisConfig setShareSameVmConnectionMultiThread(boolean shareSameVmConnectionMultiThread) {
		this.shareSameVmConnectionMultiThread = shareSameVmConnectionMultiThread;
		return this;
	}

	/**
	 * 
	 */
	public NeoDatisConfig lockObjectsOnSelect(boolean yesNo) {
		lockObjectsOnSelect = yesNo;
		return this;
	}

	public boolean lockObjectsOnSelect() {
		return lockObjectsOnSelect;
	}

	/**
	 * @return
	 */
	public boolean debugLayers() {
		return debugLayers;
	}

	public NeoDatisConfig setDebugLayers(boolean yesNo) {
		debugLayers = yesNo;
		return this;
	}

	public boolean debugMessageStreamer() {
		return debugMessageStreamer;
	}

	public NeoDatisConfig setDebugMessageStreamer(boolean yesNo) {
		debugMessageStreamer = yesNo;
		return this;
	}

	public boolean debugStorageEngine() {
		return debugStorageEngine;
	}

	public NeoDatisConfig setDebugStorageEngine(boolean yesNo) {
		debugStorageEngine = yesNo;
		return this;
	}

	public String getBaseDirectory() {
		return baseDirectory;
	}

	public NeoDatisConfig setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
		return this;
	}

	public boolean useLazyInstantiationInServerMode() {
		return useLazyInstantiationInServerMode;
	}

	public NeoDatisConfig setUseLazyInstantiationInServerMode(boolean useLazyInstantiationInServerMode) {
		this.useLazyInstantiationInServerMode = useLazyInstantiationInServerMode;
		return this;
	}

	/**
	 * @return
	 */
	public long getTimeoutToAcquireMutex() {
		return timeoutToAcquireMutex;
	}

	public NeoDatisConfig registerDatabaseStartupManager(DatabaseStartupManager manager) {
		databaseStartupManager = manager;
		return this;
	}

	public NeoDatisConfig removeDatabaseStartupManager() {
		databaseStartupManager = null;
		return this;
	}

	public DatabaseStartupManager getDatabaseStartupManager() {
		return databaseStartupManager;
	}

	public boolean logSchemaEvolutionAnalysis() {
		return logSchemaEvolutionAnalysis;
	}

	public NeoDatisConfig setLogSchemaEvolutionAnalysis(boolean log) {
		logSchemaEvolutionAnalysis = log;
		return this;
	}

	public boolean isTransactional() {
		return transactional;
	}

	public NeoDatisConfig setTransactional(boolean transactional) {
		this.transactional = transactional;
		return this;
	}

	public String getUser() {
		return user;
	}

	public NeoDatisConfig setUser(String user) {
		this.user = user;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public NeoDatisConfig setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * @param homeDirectory
	 */
	public NeoDatisConfig setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
		return this;
	}

	public String getHomeDirectory() {
		return homeDirectory;
	}

	public String getHost() {
		return host;
	}

	public NeoDatisConfig setHostAndPort(String host, int port) {
		this.host = host;
		this.port = port;
		servers.add(new Server(host, port));
		return this;
	}

	public int getPort() {
		return port;
	}

	public NeoDatisConfig setPort(int port) {
		this.port = port;
		return this;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public NeoDatisConfig setIsLocal(boolean isLocal) {
		this.isLocal = isLocal;
		return this;
	}

	/**
	 * @return
	 */
	public NeoDatisConfig copy() {
		NeoDatisConfigImpl p = new NeoDatisConfigImpl(false);
		p.setHomeDirectory(getHomeDirectory());
		p.setHostAndPort(getHost(), getPort());
		p.setIsLocal(isLocal());
		p.setPassword(getPassword());
		p.setPort(getPort());
		p.setTransactional(isTransactional());
		p.setUser(getUser());
		p.servers.clear();
		p.servers.addAll(servers);
		return p;
	}

	public NeoDatisConfig addServer(String host, int port) {
		servers.add(new Server(host, port));
		return this;
	}

	public List<Server> getServers() {
		return servers;
	}

	public boolean useNativeConfig() {
		return useNativeConfig;
	}

	public NeoDatisConfig setUseNativeConfig(boolean useNativeConfig) {
		this.useNativeConfig = useNativeConfig;
		return this;
	}

	public int getNumberOfServers() {
		return servers.size();
	}

	public NeoDatisConfig setUserAndPassword(String user, String password) {
		setUser(user);
		setPassword(password);
		return this;
	}

	/**
	 * @return The Directory used for Server/client Inbox (used for file
	 *         transfer)
	 */
	public String getInboxDirectory() {
		return inboxDirectory;
	}

	public NeoDatisConfig setInboxDirectory(String dir) {
		this.inboxDirectory = dir;
		return this;
	}

	public boolean isSSL() {
		return ssl;
	}

	public NeoDatisConfig setSSL(boolean ssl) {
		this.ssl = ssl;
		return this;
	}

	public Class getOidGeneratorClass() {
		return oidGeneratorClass;
	}

	public NeoDatisConfig setOidGeneratorClass(Class oidGeneratorClass) {
		this.oidGeneratorClass = oidGeneratorClass;
		return this;
	}

	public NeoDatisConfig setUseStorageEngineCache(boolean yesOrNo) {
		this.useStorageEngineCache = yesOrNo;
		return this;
	}

	public boolean useStorageEngineCache() {
		return useStorageEngineCache;
	}

	/**
	 * indicates if the oid generator can use cache
	 * 
	 * @param yesOrNo
	 * @return
	 */
	public NeoDatisConfig setOidGeneratorUseCache(boolean yesOrNo) {
		oidGeneratorUseCache = yesOrNo;
		return this;
	}

	public boolean oidGeneratorUseCache() {
		return oidGeneratorUseCache;
	}

	public int getSocketTimeoutForClientSever() {
		return socketTimeoutForClientServer;
	}

	public NeoDatisConfig setClientServerSocketTimeout(int t) {
		this.socketTimeoutForClientServer = t;
		return this;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getBaseName() {
		return baseName;
	}

	public NeoDatisConfig setBaseName(String baseName) {
		this.baseName = baseName;
		return this;
	}

	public boolean logExceptionsToFile() {
		return logExceptionToFile;
	}

	public void setLogExceptionsToFile(boolean yes) {
		logExceptionToFile = yes;
	}

	public int getDataBlockSize() {
		return dataBlockSize;
	}

	public void setDataBlockSize(int dataBlockSize) {
		this.dataBlockSize = dataBlockSize;
	}

	public FileHasBeenReceived getReceivedFileCallback() {
		return fileHasBeenReceivedCallback;
	}

	public void setFileHasBeenReceivedCallback(FileHasBeenReceived fileHasBeenReceivedCallback) {
		this.fileHasBeenReceivedCallback = fileHasBeenReceivedCallback;
	}
	
}
