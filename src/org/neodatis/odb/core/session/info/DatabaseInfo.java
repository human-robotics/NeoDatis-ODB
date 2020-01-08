package org.neodatis.odb.core.session.info;

import org.neodatis.odb.DatabaseId;

public class DatabaseInfo {
	protected DatabaseId databaseId;
	protected boolean isEncrypted;
	protected String cryptographer;
	protected int version;
	protected String databaseCharacterEncoding;
	protected String oidGeneratorClassName;
	
	
	
	public DatabaseInfo(DatabaseId databaseId, boolean isEncrypted, String cryptographer, int version, String databaseCharacterEncoding,
			String oidGeneratorClassName) {
		super();
		this.databaseId = databaseId;
		this.isEncrypted = isEncrypted;
		this.cryptographer = cryptographer;
		this.version = version;
		this.databaseCharacterEncoding = databaseCharacterEncoding;
		this.oidGeneratorClassName = oidGeneratorClassName;
	}
	public DatabaseId getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(DatabaseId databaseId) {
		this.databaseId = databaseId;
	}
	public boolean isEncrypted() {
		return isEncrypted;
	}
	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}
	public String getCryptographer() {
		return cryptographer;
	}
	public void setCryptographer(String cryptographer) {
		this.cryptographer = cryptographer;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getDatabaseCharacterEncoding() {
		return databaseCharacterEncoding;
	}
	public void setDatabaseCharacterEncoding(String databaseCharacterEncoding) {
		this.databaseCharacterEncoding = databaseCharacterEncoding;
	}
	public String getOidGeneratorClassName() {
		return oidGeneratorClassName;
	}
	public void setOidGeneratorClassName(String oidGeneratorClassName) {
		this.oidGeneratorClassName = oidGeneratorClassName;
	}

	public String toString(String separator) {
		StringBuffer b = new StringBuffer();
		
		b.append("Is encrypted =").append(isEncrypted).append(separator);
		b.append("Cryptographer =").append(cryptographer).append(separator);
		b.append("File Formart version =").append(version).append(separator);
		b.append("Database Id =").append(databaseId).append(separator);
		b.append("OidGeneratorClassName =").append(oidGeneratorClassName).append(separator);
		
		return b.toString();
	}
	
}
