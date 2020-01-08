/**
 * 
 */
package org.neodatis.odb.core.mock;

import org.neodatis.odb.*;
import org.neodatis.odb.core.layers.layer1.ObjectIntrospector;
import org.neodatis.odb.core.layers.layer2.instance.InstanceBuilderContext;
import org.neodatis.odb.core.layers.layer2.meta.*;
import org.neodatis.odb.core.layers.layer3.OidAndBytes;
import org.neodatis.odb.core.layers.layer4.ClassOidIterator;
import org.neodatis.odb.core.layers.layer4.ObjectOidIterator;
import org.neodatis.odb.core.layers.layer4.ObjectOidIterator.Way;
import org.neodatis.odb.core.layers.layer4.StorageEngine;
import org.neodatis.odb.core.query.InternalQuery;
import org.neodatis.odb.core.query.ValuesQuery;
import org.neodatis.odb.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.core.query.criteria.Criterion;
import org.neodatis.odb.core.query.values.ValuesCriteriaQuery;
import org.neodatis.odb.core.refactor.CheckMetaModelResult;
import org.neodatis.odb.core.refactor.RefactorManager;
import org.neodatis.odb.core.server.message.Message;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.core.session.info.DatabaseInfo;
import org.neodatis.odb.core.session.info.OpenCloseInfo;
import org.neodatis.odb.core.trigger.*;
import org.neodatis.tool.wrappers.list.IOdbList;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;

/**
 * @author olivier
 *
 */
public class MockSessionEngine implements SessionEngine{

	/**
	 * @param mockSession
	 */
	public MockSessionEngine(MockSession mockSession) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#addDeleteTriggerFor(java.lang.String, org.neodatis.odb.core.trigger.DeleteTrigger)
	 */
	public void addDeleteTriggerFor(String name, DeleteTrigger trigger) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#addIndexOn(java.lang.String, java.lang.String, java.lang.String[], boolean, boolean)
	 */
	public void addIndexOn(String fullClassName, String name, String[] indexFields, boolean verbose, boolean b) {
		// TODO Auto-generated metho	d stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#addInsertTriggerFor(java.lang.String, org.neodatis.odb.core.trigger.InsertTrigger)
	 */
	public void addInsertTriggerFor(String name, InsertTrigger trigger) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#addSelectTriggerFor(java.lang.String, org.neodatis.odb.core.trigger.SelectTrigger)
	 */
	public void addSelectTriggerFor(String name, SelectTrigger trigger) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#addUpdateTriggerFor(java.lang.String, org.neodatis.odb.core.trigger.UpdateTrigger)
	 */
	public void addUpdateTriggerFor(String name, UpdateTrigger trigger) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#close()
	 */
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#criteriaQuery(java.lang.Class, org.neodatis.odb.core.query.criteria.ICriterion)
	 */
	public CriteriaQuery criteriaQuery(Class clazz, Criterion criterion) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#criteriaQuery(java.lang.Class)
	 */
	public CriteriaQuery criteriaQuery(Class clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#defragmentTo(java.lang.String)
	 */
	public void defragmentTo(String newFileName) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#delete(java.lang.Object)
	 */
	public ObjectOid delete(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#deleteIndex(java.lang.String, java.lang.String, boolean)
	 */
	public void deleteIndex(String fullClassName, String indexName, boolean verbose) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#deleteObjectWithOid(org.neodatis.odb.OID)
	 */
	public void deleteObjectWithOid(OID oid) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#disconnect(java.lang.Object)
	 */
	public void disconnect(Object object) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#existOid(org.neodatis.odb.OID)
	 */
	public boolean existOid(OID oid) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getClassOidIterator()
	 */
	public ClassOidIterator getClassOidIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getFieldValuesFromOid(org.neodatis.odb.OID, java.util.HashSet, boolean)
	 */
	public AttributeValuesMap getFieldValuesFromOid(OID oid, HashSet<String> fields, boolean optimizeObjectCompararison) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getLayer4()
	 */
	public StorageEngine getStorageEngine() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getMetaHeaderFromOid(org.neodatis.odb.ObjectOid, boolean)
	 */
	public ObjectInfoHeader getMetaHeaderFromOid(ObjectOid oid, boolean throwExceptionIfNotFound, boolean useCache) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getMetaObjectFromOid(org.neodatis.odb.ObjectOid)
	 */
	public NonNativeObjectInfo getMetaObjectFromOid(ObjectOid oid) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getObjectFromOid(org.neodatis.odb.ObjectOid)
	 */
	public Object getObjectFromOid(ObjectOid oid) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getObjectInfoHeaderFromOid(org.neodatis.odb.ObjectOid, boolean)
	 */
	public ObjectInfoHeader getObjectInfoHeaderFromOid(ObjectOid oid, boolean useCache) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getObjectIntrospector()
	 */
	public ObjectIntrospector getObjectIntrospector() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getObjectOid(java.lang.Object, boolean)
	 */
	public ObjectOid getObjectOid(Object object, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getObjectOidIterator(org.neodatis.odb.ClassOid, org.neodatis.odb.core.layers.layer4.ObjectOidIterator.Way)
	 */
	public ObjectOidIterator getObjectOidIterator(ClassOid classOid, Way way) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getObjects(org.neodatis.odb.core.query.IQuery, boolean, int, int)
	 */
	public <T> Objects<T> execute(Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getObjects(java.lang.Class, boolean, int, int)
	 */
	public <T> Objects<T> getObjects(Class clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getRefactorManager()
	 */
	public RefactorManager getRefactorManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getSession()
	 */
	public Session getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getValues(org.neodatis.odb.core.query.IValuesQuery, int, int)
	 */
	public Values getValues(ValuesQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#introspectClass(java.lang.String, boolean)
	 */
	public ClassInfoList introspectClass(String fullClassName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer1ToLayer2(java.lang.Object)
	 */
	public NonNativeObjectInfo layer1ToLayer2(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer2ToLayer1(org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo)
	 */
	public Object layer2ToLayer1(NonNativeObjectInfo nnoi) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer2ToLayer3(org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo)
	 */
	public IOdbList<OidAndBytes> layer2ToLayer3(NonNativeObjectInfo nnoi) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer3ToLayer2(org.neodatis.odb.core.layers.layer3.OidAndBytes, boolean)
	 */
	public NonNativeObjectInfo layer3ToLayer2(OidAndBytes oidAndBytes, boolean full) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer3ToLayer4(org.neodatis.tool.wrappers.list.IOdbList)
	 */
	public ObjectOid layer3ToLayer4(IOdbList<OidAndBytes> oidsAndBytes) {
		return null;
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer4ToLayer3(org.neodatis.odb.OID)
	 */
	public OidAndBytes layer4ToLayer3(OID oid) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#loadMetaModel()
	 */
	public MetaModel loadMetaModel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#rebuildIndex(java.lang.String, java.lang.String, boolean)
	 */
	public void rebuildIndex(String fullClassName, String indexName, boolean verbose) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#reconnect(java.lang.Object)
	 */
	public void reconnect(Object object) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#store(java.lang.Object)
	 */
	public ObjectOid store(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#store(org.neodatis.odb.ObjectOid, java.lang.Object)
	 */
	public ObjectOid store(ObjectOid oid, Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#storeClassInfos(org.neodatis.odb.core.layers.layer2.meta.ClassInfoList)
	 */
	public void storeClassInfos(ClassInfoList ciList) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getMetaObjectFromOid(org.neodatis.odb.ObjectOid, boolean)
	 */
	public NonNativeObjectInfo getMetaObjectFromOid(ObjectOid oid, boolean throwExceptionIfNotFound) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getMetaObjects(org.neodatis.odb.core.query.IQuery, boolean, int, int)
	 */
	public <T> Objects<T> getMetaObjects(Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getObjectFromOid(org.neodatis.odb.ObjectOid, boolean)
	 */
	public Object getObjectFromOid(ObjectOid oid, boolean throwExceptionIfNotFound) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#storeClassInfo(org.neodatis.odb.core.layers.layer2.meta.ClassInfo)
	 */
	public void storeClassInfo(ClassInfo ci) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#deleteObjectWithOid(org.neodatis.odb.ObjectOid)
	 */
	public void deleteObjectWithOid(ObjectOid oid) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getFileFormatVersion()
	 */
	public String getFileFormatVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getTriggerManager()
	 */
	public TriggerManager getTriggerManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#loadMetaModel(org.neodatis.odb.core.layers.layer2.meta.MetaModel)
	 */
	public MetaModel loadMetaModel(MetaModel metaModel) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#commit()
	 */
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#rollback()
	 */
	public void rollback() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#storeMeta(org.neodatis.odb.ObjectOid, org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo)
	 */
	public ObjectOid storeMeta(ObjectOid oid, NonNativeObjectInfo nnoi) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#callLayer3ToLayer4OnServer(org.neodatis.tool.wrappers.list.IOdbList)
	 */
	public void callLayer3ToLayer4OnServer(IOdbList<OidAndBytes> oidsAndBytes) {
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer3ToLayer2(org.neodatis.tool.wrappers.list.IOdbList, boolean, java.util.Map)
	 */
	public NonNativeObjectInfo layer3ToLayer2(IOdbList<OidAndBytes> oabs, boolean full, Map<OID, OID> oidsToReplace) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#classInfoFromBytes(org.neodatis.odb.core.layers.layer3.OidAndBytes)
	 */
	public ClassInfo classInfoFromBytes(OidAndBytes oab) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getBytesOfObjectFromOid(org.neodatis.odb.ObjectOid, boolean)
	 */
	public OidAndBytes getBytesOfObjectFromOid(ObjectOid oid, boolean throwExceptionIfNotExist) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#checkMetaModelCompatibility(java.util.Map)
	 */
	public CheckMetaModelResult checkMetaModelCompatibility(Map<String, ClassInfo> currentCIs) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#classInfoToBytes(org.neodatis.odb.core.layers.layer2.meta.ClassInfo)
	 */
	public OidAndBytes classInfoToBytes(ClassInfo ci) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#count(org.neodatis.odb.core.query.criteria.CriteriaQuery)
	 */
	public BigInteger count(CriteriaQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#delete(java.lang.Object, boolean)
	 */
	public ObjectOid delete(Object o, boolean cascade) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#deleteObjectWithOid(org.neodatis.odb.ObjectOid, boolean)
	 */
	public void deleteObjectWithOid(ObjectOid oid, boolean cascade) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#classInfoFromBytes(org.neodatis.odb.core.layers.layer3.OidAndBytes, boolean)
	 */
	public ClassInfo classInfoFromBytes(OidAndBytes oab, boolean full) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#setTriggerManager(org.neodatis.odb.core.trigger.TriggerManager)
	 */
	public void setTriggerManager(TriggerManager triggerManagerImpl) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getFieldValuesFromOid(org.neodatis.odb.OID, java.util.HashSet, boolean, int)
	 */
	public AttributeValuesMap getFieldValuesFromOid(OID oid, HashSet<String> fields, boolean optimizeObjectCompararison, int depth) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getMetaObjectFromOid(org.neodatis.odb.ObjectOid, boolean, org.neodatis.odb.core.layers.layer2.instance.InstanceBuilderContext)
	 */
	public NonNativeObjectInfo getMetaObjectFromOid(ObjectOid oid, boolean throwExceptionIfNotFound, InstanceBuilderContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getObjectFromOid(org.neodatis.odb.ObjectOid, boolean, org.neodatis.odb.core.layers.layer2.instance.InstanceBuilderContext)
	 */
	public Object getObjectFromOid(ObjectOid oid, boolean throwExceptionIfNotFound, InstanceBuilderContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer2ToLayer1(org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo, org.neodatis.odb.core.layers.layer2.instance.InstanceBuilderContext)
	 */
	public Object layer2ToLayer1(NonNativeObjectInfo nnoi, InstanceBuilderContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer3ToLayer2(org.neodatis.odb.core.layers.layer3.OidAndBytes, boolean, int)
	 */
	public NonNativeObjectInfo layer3ToLayer2(OidAndBytes oidAndBytes, boolean full, int depth) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#layer3ToLayer2(org.neodatis.tool.wrappers.list.IOdbList, boolean, java.util.Map, int)
	 */
	public NonNativeObjectInfo layer3ToLayer2(IOdbList<OidAndBytes> oabs, boolean full, Map<OID, OID> oidsToReplace, int depth) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#refresh(java.lang.Object, int)
	 */
	public void refresh(Object o, int depth) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#execute(org.neodatis.odb.core.query.InternalQuery)
	 */
	public <T> Objects<T> execute(InternalQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#getMetaObjects(org.neodatis.odb.core.query.InternalQuery)
	 */
	public <T> Objects<T> getMetaObjects(InternalQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#criteriaQuery(java.lang.String, org.neodatis.odb.core.query.criteria.Criterion)
	 */
	public CriteriaQuery criteriaQuery(String className, Criterion criterion) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#criteriaQuery(java.lang.String)
	 */
	public CriteriaQuery criteriaQuery(String className) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#count(org.neodatis.odb.Query)
	 */
	public BigInteger count(Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#queryValues(java.lang.Class, org.neodatis.odb.core.query.criteria.Criterion)
	 */
	public ValuesQuery queryValues(Class clazz, Criterion criterion) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#queryValues(java.lang.String, org.neodatis.odb.core.query.criteria.Criterion)
	 */
	public ValuesQuery queryValues(String className, Criterion criterion) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#queryValues(java.lang.String)
	 */
	public ValuesQuery queryValues(String className) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#queryValues(java.lang.Class)
	 */
	public ValuesQuery queryValues(Class clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#addOidTriggerFor(java.lang.String, org.neodatis.odb.core.trigger.OIDTrigger)
	 */
	public void addOidTriggerFor(String className, OIDTrigger trigger) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.neodatis.odb.core.session.SessionEngine#sendMessage(org.neodatis.odb.core.server.message.Message)
	 */
	public Message sendMessage(Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	public ValuesCriteriaQuery queryValues(Class clazz, ObjectOid oid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNewDatabase() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeOidTrigger(String className, OIDTrigger trigger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeDatabaseHeader(DatabaseInfo di) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DatabaseInfo readDatabaseHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OpenCloseInfo readOpenCloseInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
