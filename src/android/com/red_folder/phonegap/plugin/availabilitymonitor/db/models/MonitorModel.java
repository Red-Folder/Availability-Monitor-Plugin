package com.red_folder.phonegap.plugin.availabilitymonitor.db.models;

import java.util.Date;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;

public class MonitorModel implements IModel {

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
	
	public long getId() {
		return this.mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public String getName() {
		return this.mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public int getFrequency() {
		return this.mFrequency;
	}

	public void setFrequency(int frequency) {
		this.mFrequency = frequency;
	}

	public MonitorType getType() {
		return this.mType;
	}

	public void setType(MonitorType type) {
		this.mType = type;
	}

	public boolean getNotifyWhenDown() {
		return this.mNotifyWhenDown;
	}

	public void setNotifyWhenDown(boolean flag) {
		this.mNotifyWhenDown = flag;
	}

	public Date getCreated() {
		return this.mCreated;
	}

	public void setCreated(Date created) {
		this.mCreated = created ;
	}

	public Date getAmended() {
		return this.mAmended;
	}

	public void setAmended(Date amended) {
		this.mAmended = amended;
	}

	public boolean getNotifyWhenUp() {
		return this.mNotifyWhenUp;
	}

	public void setNotifyWhenUp(boolean flag) {
		this.mNotifyWhenUp = flag;
	}

	public int getUnavailableThreshold() {
		return this.mUnavailableThreshold;
	}

	public void setUnavailableThreshold(int threshold) {
		this.mUnavailableThreshold = threshold;
	}

	public int getUnavailableCount() {
		return this.mUnavailableCount;
	}

	public void setUnavailableCount(int count) {
		this.mUnavailableCount = count;
	}
	
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        MonitorModel other = (MonitorModel)obj;
        return (this.getId() == other.getId())
        		&& (this.getName() == other.getName() || (this.getName() != null && this.getName().equals(other.getName())))
        		&& (this.getFrequency() == other.getFrequency())
        		&& (this.getType() == other.getType() || (this.getType() != null && this.getType().toInt() == other.getType().toInt()))
        		&& (this.getNotifyWhenDown() == other.getNotifyWhenDown())
        		&& (this.getNotifyWhenUp() == other.getNotifyWhenUp())
        		&& (this.getCreated() == other.getCreated() || (this.getCreated() != null && this.getCreated().getTime() == other.getCreated().getTime()))
        		&& (this.getAmended() == other.getAmended() || (this.getAmended() != null && this.getAmended().getTime() == other.getAmended().getTime()))
        		&& (this.getUnavailableThreshold() == other.getUnavailableThreshold())
        		&& (this.getUnavailableCount() == other.getUnavailableCount());
    }
    
}
