package com.red_folder.phonegap.plugin.availabilitymonitor.db.models;

import java.util.Date;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;

public class ResultModel implements IModel {

	private long mId;
	private long mMonitorId;
	private MonitorType mType;
	private Date mTimestamp;
	
	public long getId() {
		return this.mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public long getMonitorId() {
		return this.mMonitorId;
	}

	public void setMonitorId(long monitorId) {
		this.mMonitorId = monitorId;
	}

	public MonitorType getType() {
		return this.mType;
	}

	public void setType(MonitorType type) {
		this.mType = type;
	}

	public Date getTimestamp() {
		return this.mTimestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.mTimestamp = timestamp;
	}

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        ResultModel other = (ResultModel)obj;
        return (this.getId() == other.getId())
        		&& (this.getMonitorId() == other.getMonitorId())
        		&& (this.getType() == other.getType() || (this.getType() != null && this.getType().toInt() == other.getType().toInt()))
        		&& (this.getTimestamp() == other.getTimestamp() || (this.getTimestamp() != null && this.getTimestamp().getTime() == other.getTimestamp().getTime()));
    }

}
