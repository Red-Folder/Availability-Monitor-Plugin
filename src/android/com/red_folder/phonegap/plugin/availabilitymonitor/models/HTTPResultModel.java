package com.red_folder.phonegap.plugin.availabilitymonitor.models;

import java.util.Date;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IResultModel;

public class HTTPResultModel implements IResultModel {

	private long mId;
	private long mMonitorId;
	private MonitorType mType;
	private Date mTimestamp;
	private int mResponseCode;
	
	@Override
	public boolean isAvailable() {
		return (this.mResponseCode == 200);
	}

	@Override
	public long getId() {
		return this.mId;
	}

	@Override
	public void setId(long id) {
		this.mId = id;
		
	}

	@Override
	public long getMonitorId() {
		return this.mMonitorId;
	}

	@Override
	public void setMonitorId(long monitorId) {
		this.mMonitorId = monitorId;
	}
	
	@Override
	public MonitorType getType() {
		return this.mType;
	}

	@Override
	public void setType(MonitorType type) {
		this.mType = type;
	}

	@Override
	public Date getTimestamp() {
		return this.mTimestamp;
	}

	@Override
	public void setTimestamp(Date timestamp) {
		this.mTimestamp = timestamp;
		
	}
	
	public int getResponseCode() {
		return this.mResponseCode;
	}
	
	public void setResponseCode(int responseCode) {
		this.mResponseCode = responseCode;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        HTTPResultModel other = (HTTPResultModel)obj;
        return (this.getId() == other.getId())
        		&& (this.getMonitorId() == other.getMonitorId())
        		&& (this.getType() == other.getType() || (this.getType() != null && this.getType().toInt() == other.getType().toInt()))
        		&& (this.getTimestamp() == other.getTimestamp() || (this.getTimestamp() != null && this.getTimestamp().getTime() == other.getTimestamp().getTime()))
        		&& (this.getResponseCode() == other.getResponseCode());
    }
}
