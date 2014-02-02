package com.red_folder.phonegap.plugin.availabilitymonitor.db.models;

public class HTTPMonitorModel implements IModel {

	private long mId;
	private long mMonitorId;
	private String mUrl;
	
	public long getId() {
		return this.mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public long getMonitorId() {
		return this.mMonitorId;
	}

	public void setMonitorId(long id) {
		this.mMonitorId = id;
	}

	public String getURL() {
		return this.mUrl;
	}
	
	public void setURL(String url) {
		this.mUrl = url;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        HTTPMonitorModel other = (HTTPMonitorModel)obj;
        return (this.getId() == other.getId())
        		&& (this.getMonitorId() == other.getMonitorId())
        		&& (this.getURL() == other.getURL() || (this.getURL() != null && this.getURL().equals(other.getURL())));
    }
    
}
								