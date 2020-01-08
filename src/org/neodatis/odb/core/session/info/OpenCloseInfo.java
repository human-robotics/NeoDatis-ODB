package org.neodatis.odb.core.session.info;

public class OpenCloseInfo {
	protected long openDateTime;
	protected long closeDateTime;
	
	protected String ipWhereDatabaseWasOpen;

	public OpenCloseInfo(long openDateTime, long closeDateTime, String ipWhereDatabaseWasOpen) {
		super();
		this.openDateTime = openDateTime;
		this.closeDateTime = closeDateTime;
		this.ipWhereDatabaseWasOpen = ipWhereDatabaseWasOpen;
	}

	public long getOpenDateTime() {
		return openDateTime;
	}

	public void setOpenDateTime(long openDateTime) {
		this.openDateTime = openDateTime;
	}

	public long getCloseDateTime() {
		return closeDateTime;
	}

	public void setCloseDateTime(long closeDateTime) {
		this.closeDateTime = closeDateTime;
	}

	public String getIpWhereDatabaseWasOpen() {
		return ipWhereDatabaseWasOpen;
	}

	public void setIpWhereDatabaseWasOpen(String ipWhereDatabaseWasOpen) {
		this.ipWhereDatabaseWasOpen = ipWhereDatabaseWasOpen;
	}
	
	

}
