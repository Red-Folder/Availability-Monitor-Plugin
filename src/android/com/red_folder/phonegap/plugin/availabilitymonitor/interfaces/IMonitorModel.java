package com.red_folder.phonegap.plugin.availabilitymonitor.interfaces;

import java.util.Date;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;


public interface IMonitorModel extends IModel {

	public String getName();
	public void setName(String name);
	
	public int getFrequency();
	public void setFrequency(int frequency);
	
	public MonitorType getType();
	public void setType(MonitorType type);
	
	public boolean getNotifyWhenDown();
	public void setNotifyWhenDown(boolean flag);

	public boolean getNotifyWhenUp();
	public void setNotifyWhenUp(boolean flag);

	public Date getCreated();
	public void setCreated(Date created);
	
	public Date getAmended();
	public void setAmended(Date amended);
	
	public int getUnavailableThreshold();
	public void setUnavailableThreshold(int threshold);
	
	public int getUnavailableCount();
	public void setUnavailableCount(int count);
}
