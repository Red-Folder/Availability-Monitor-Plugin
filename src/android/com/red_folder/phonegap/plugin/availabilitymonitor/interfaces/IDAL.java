package com.red_folder.phonegap.plugin.availabilitymonitor.interfaces;

import java.util.Date;

public interface IDAL {

	public long addMonitor(IMonitorModel monitor);
	public boolean deleteMonitor(IMonitorModel monitor);
	public boolean deleteMonitor(IMonitorModel monitor, boolean deleteAllResults);
	public boolean updateMonitor(IMonitorModel monitor);
	public IMonitorModel getMonitor(long id);
	public IMonitorModel[] getMonitors();
	public IMonitorModel[] getMonitors(String name);
	
	public long addResult(IResultModel result);
	public boolean deleteResult(IResultModel result);
	public boolean deleteResultsByMonitorId(long monitorId);
	public boolean deleteResultsByMonitorId(long monitorId, Date olderThan);
	public boolean updateResult(IResultModel result);
	public IResultModel getResult(long id);
	public IResultModel[] getResults(long monitorId);
	public IResultModel[] getResults(long monitorId, Date from, Date to);
	public IResultModel[] getResults(long monitorId, int skip, int take);
	public IResultModel[] getResults(long monitorId, Date from, Date to, int skip, int take);
	
}
