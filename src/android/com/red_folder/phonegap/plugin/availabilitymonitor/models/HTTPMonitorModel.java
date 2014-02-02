package com.red_folder.phonegap.plugin.availabilitymonitor.models;

import java.util.Date;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IMonitorModel;

public class HTTPMonitorModel implements IMonitorModel {

	private long mId;
	private String mName;
	private int mFrequency;
	private MonitorType mType;
	private boolean mNotifyWhenDown;
	private boolean mNotifyWhenUp;
	private Date mCreated;
	private Date mAmended;
	private int mUnavailableThreshold;
	private int mUnavailableCount;
	
	private String mUrl;
	
	@Override
	public long getId() {
		return this.mId;
	}

	@Override
	public void setId(long id) {
		this.mId = id;
	}

	@Override
	public String getName() {
		return this.mName;
	}

	@Override
	public void setName(String name) {
		this.mName = name;
	}

	@Override
	public int getFrequency() {
		return this.mFrequency;
	}

	@Override
	public void setFrequency(int frequency) {
		this.mFrequency = frequency;
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
	public boolean getNotifyWhenDown() {
		return this.mNotifyWhenDown;
	}

	@Override
	public void setNotifyWhenDown(boolean flag) {
		this.mNotifyWhenDown = flag;
	}

	@Override
	public Date getCreated() {
		return this.mCreated;
	}

	@Override
	public void setCreated(Date created) {
		this.mCreated = created ;
	}

	@Override
	public Date getAmended() {
		return this.mAmended;
	}

	@Override
	public void setAmended(Date amended) {
		this.mAmended = amended;
	}

	@Override
	public boolean getNotifyWhenUp() {
		return this.mNotifyWhenUp;
	}

	@Override
	public void setNotifyWhenUp(boolean flag) {
		this.mNotifyWhenUp = flag;
	}

	@Override
	public int getUnavailableThreshold() {
		return this.mUnavailableThreshold;
	}

	@Override
	public void setUnavailableThreshold(int threshold) {
		this.mUnavailableThreshold = threshold;
	}

	@Override
	public int getUnavailableCount() {
		return this.mUnavailableCount;
	}

	@Override
	public void setUnavailableCount(int count) {
		this.mUnavailableCount = count;
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
        		&& (this.getName() == other.getName() || (this.getName() != null && this.getName().equals(other.getName())))
        		&& (this.getFrequency() == other.getFrequency())
        		&& (this.getType() == other.getType() || (this.getType() != null && this.getType().toInt() == other.getType().toInt()))
        		&& (this.getNotifyWhenDown() == other.getNotifyWhenDown())
        		&& (this.getNotifyWhenUp() == other.getNotifyWhenUp())
        		&& (this.getCreated() == other.getCreated() || (this.getCreated() != null && this.getCreated().getTime() == other.getCreated().getTime()))
        		&& (this.getAmended() == other.getAmended() || (this.getAmended() != null && this.getAmended().getTime() == other.getAmended().getTime()))
        		&& (this.getUnavailableThreshold() == other.getUnavailableThreshold())
        		&& (this.getUnavailableCount() == other.getUnavailableCount())
        		&& (this.getURL() == other.getURL() || (this.getURL() != null && this.getURL().equals(other.getURL())));
    }
    
}
