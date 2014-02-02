package com.red_folder.phonegap.plugin.availabilitymonitor.interfaces;

import java.util.Date;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;

public interface IResultModel extends IModel {

	public long getMonitorId();
	public void setMonitorId(long monitorId);
	
	public MonitorType getType();
	public void setType(MonitorType type);
	
	public Date getTimestamp();
	public void setTimestamp(Date timestamp);
	
	public boolean isAvailable();
	
}
