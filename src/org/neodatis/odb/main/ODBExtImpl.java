package org.neodatis.odb.main;

import org.neodatis.odb.*;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.layers.layer2.meta.ObjectInfoHeader;
import org.neodatis.odb.core.oid.ExternalObjectOIDImpl;
import org.neodatis.odb.core.refactor.RefactorManager;
import org.neodatis.odb.core.server.message.*;
import org.neodatis.odb.core.server.message.process.RemoteProcess;
import org.neodatis.odb.core.server.message.process.RemoteProcessMessage;
import org.neodatis.odb.core.server.message.process.RemoteProcessMessageResponse;
import org.neodatis.odb.core.server.message.process.RemoteProcessReturn;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.core.trigger.CloseListener;

import java.io.File;
import java.util.List;

public class ODBExtImpl implements ODBExt {
	protected SessionEngine engine;
	protected Session session;

	public ODBExtImpl(SessionEngine sessionEngine) {
		this.engine = sessionEngine;
		this.session = sessionEngine.getSession();
	}

	public ExternalOID convertToExternalOID(ObjectOid oid) {
		return session.getOidGenerator().toExternalOid(oid, session.getDatabaseInfo().getDatabaseId());
	}

	public TransactionId getCurrentTransactionId() {
		return session.getCurrentTransactionId();
	}

	public DatabaseId getDatabaseId() {
		return session.getDatabaseInfo().getDatabaseId();
	}

	public ExternalObjectOid getObjectExternalOID(Object object) {
		return new ExternalObjectOIDImpl(engine.getObjectOid(object, true), getDatabaseId());
	}

	public long getObjectVersion(ObjectOid oid, boolean useCache) {
		ObjectInfoHeader oih = engine.getMetaHeaderFromOid(oid, true, useCache);
		if (oih == null) {
			throw new NeoDatisRuntimeException(NeoDatisError.OBJECT_WITH_OID_DOES_NOT_EXIST_IN_CACHE.addParameter(oid));
		}
		return oih.getObjectVersion();
	}

	public long getObjectCreationDate(ObjectOid oid) {
		ObjectInfoHeader oih = engine.getMetaHeaderFromOid(oid, true, true);
		if (oih == null) {
			throw new NeoDatisRuntimeException(NeoDatisError.OBJECT_WITH_OID_DOES_NOT_EXIST_IN_CACHE.addParameter(oid));
		}
		return oih.getCreationDate();
	}

	public long getObjectUpdateDate(ObjectOid oid, boolean useCache) {
		ObjectInfoHeader oih = engine.getMetaHeaderFromOid(oid, true, useCache);
		if (oih == null) {
			throw new NeoDatisRuntimeException(NeoDatisError.OBJECT_WITH_OID_DOES_NOT_EXIST_IN_CACHE.addParameter(oid));
		}
		return oih.getUpdateDate();
	}

	public RefactorManager getRefactorManager() {
		return engine.getRefactorManager();
	}

	public ObjectOid store(ObjectOid oid, Object object) {
		return engine.store(oid, object);
	}

	public Message sendMessage(Message message) {
		try {
			return engine.sendMessage(message);
		} catch (NeoDatisRuntimeException e) {
			if (engine.getSession().getConfig().logExceptionsToFile()) {
				e.writeToFile();
			}
			throw e;
		}
	}

	/**
	 * 
	 */
	public SendFileMessageResponse sendFile(String localFileName,String remoteDirectory, String remoteFileName, boolean putFileInServerInbox, boolean saveFileToFileSystem) {
        try {
            return (SendFileMessageResponse) engine.sendMessage(new SendFileMessage(engine.getSession().getBaseIdentification().getBaseId(), engine
                    .getSession().getId(), localFileName, remoteDirectory, remoteFileName, putFileInServerInbox,saveFileToFileSystem));
        } catch (NeoDatisRuntimeException e) {
            if (engine.getSession().getConfig().logExceptionsToFile()) {
                e.writeToFile();
            }
            throw e;
        }
    }

	public SendFileMessageResponse sendFile(String localFileName, String remoteFileName, boolean putFileInServerInbox) {
		try {
			return (SendFileMessageResponse) engine.sendMessage(new SendFileMessage(engine.getSession().getBaseIdentification().getBaseId(), engine
					.getSession().getId(), localFileName, ".", remoteFileName, putFileInServerInbox,true));
		} catch (NeoDatisRuntimeException e) {
			if (engine.getSession().getConfig().logExceptionsToFile()) {
				e.writeToFile();
			}
			throw e;
		}
	}

	public SendFileMessageResponse sendFile(String localFileName) {
		try {
			String remoteFileName = new File(localFileName).getName();
			return (SendFileMessageResponse) engine.sendMessage(new SendFileMessage(engine.getSession().getBaseIdentification().getBaseId(), engine
					.getSession().getId(), localFileName, ".", remoteFileName, true,true));
		} catch (NeoDatisRuntimeException e) {
			if (engine.getSession().getConfig().logExceptionsToFile()) {
				e.writeToFile();
			}
			throw e;
		}
	}

	public GetFileMessageResponse getFile(String remoteFileName) {
		try {
			return (GetFileMessageResponse) engine.sendMessage(new GetFileMessage(engine.getSession().getBaseIdentification().getBaseId(), engine.getSession()
					.getId(), true, remoteFileName, true, remoteFileName));
		} catch (NeoDatisRuntimeException e) {
			if (engine.getSession().getConfig().logExceptionsToFile()) {
				e.writeToFile();
			}
			throw e;
		}
	}

	public GetFileMessageResponse getFile(boolean remoteFileInbox, String remoteFileName, boolean localFileInbox, String localFileName) {
		try {
			return (GetFileMessageResponse) engine.sendMessage(new GetFileMessage(engine.getSession().getBaseIdentification().getBaseId(), engine.getSession()
					.getId(), remoteFileInbox, remoteFileName, localFileInbox, localFileName));
		} catch (NeoDatisRuntimeException e) {
			if (engine.getSession().getConfig().logExceptionsToFile()) {
				e.writeToFile();
			}
			throw e;
		}
	}

	public RemoteProcessReturn executeRemoteProcess(RemoteProcess process, boolean synchronous) {
		try {
			RemoteProcessMessageResponse r = (RemoteProcessMessageResponse) engine.sendMessage(new RemoteProcessMessage(engine.getSession()
					.getBaseIdentification().getBaseId(), engine.getSession().getId(), process, synchronous));
			return r.getRemoteProcessReturn();
		} catch (NeoDatisRuntimeException e) {
			if (engine.getSession().getConfig().logExceptionsToFile()) {
				e.writeToFile();
			}
			throw e;
		}
	}

	public void dontCallTriggersForClasses(List<Class> classes) {
		engine.getTriggerManager().addClassesNotToCallTriggersOn(classes);
	}

	public ObjectOid objectOidFromString(String id) {
		return engine.getSession().getOidGenerator().objectOidFromString(id);
	}

	public void disableTriggers() {
		engine.getTriggerManager().disableTriggers();

	}

	public void enableTriggers() {
		engine.getTriggerManager().disableTriggers();
	}

	public void addCloseListener(CloseListener l) {
		session.addCloseListener(l);
	}
}
