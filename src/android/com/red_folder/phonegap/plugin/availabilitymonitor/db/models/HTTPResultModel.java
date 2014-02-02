package com.red_folder.phonegap.plugin.availabilitymonitor.db.models;

public class HTTPResultModel implements IModel {

	private long mId;
	private long mResultId;
	private int mResponseCode;
	
	public long getId() {
		return this.mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public long getResultId() {
		return this.mResultId;
	}

	public void setResultId(long id) {
		this.mResultId = id;
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
        		&& (this.getResultId() == other.getResultId())
        		&& (this.getResultId() == other.getResultId());
    }
	
}
