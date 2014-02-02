package com.red_folder.phonegap.plugin.availabilitymonitor.db;

import java.util.Date;

import android.content.Context;

import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.HTTPMonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.HTTPResultModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.IModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.MonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.ResultModel;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;

import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IDAL;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IMonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IResultModel;

public class SQLiteDAL implements IDAL {
	
	SQLiteMonitorProvider mMonitorProvider;
	SQLiteHTTPMonitorProvider mHTTPMonitorProvider;

	SQLiteResultProvider mResultProvider;
	SQLiteHTTPResultProvider mHTTPResultProvider;

	public SQLiteDAL(Context context) {
		this.mMonitorProvider = new SQLiteMonitorProvider(context);
		this.mHTTPMonitorProvider = new SQLiteHTTPMonitorProvider(context);

		this.mResultProvider = new SQLiteResultProvider(context);
		this.mHTTPResultProvider = new SQLiteHTTPResultProvider(context);
		
		// Set the order of the ResultProvider
		this.mResultProvider.setOrderBy("Timestamp desc");
	}
	
	public long addMonitor(IMonitorModel monitor) {
		MonitorModel dbMonitor = this.toDBMonitorModel(monitor);
		long id = this.mMonitorProvider.add((IModel)dbMonitor);
		
		if (monitor.getType() == MonitorType.HTTP) {
			HTTPMonitorModel dbHTTPModel = this.toDBHTTPMonitorModel((com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPMonitorModel) monitor);
			dbHTTPModel.setMonitorId(id);
			this.mHTTPMonitorProvider.add(dbHTTPModel);
		}
		
		return id;
	}

	public boolean deleteMonitor(IMonitorModel monitor) {
		boolean result = false;
		
		long id = monitor.getId();
		
		MonitorModel storedModel = this.mMonitorProvider.get(id);
		
		if (storedModel != null) {
			if (storedModel.getType() == MonitorType.HTTP)
				result = this.mHTTPMonitorProvider.deleteByMonitorId(id);

			if (result)
				result = this.mMonitorProvider.delete(id);
		}
		
		return result;
	}

	public boolean deleteMonitor(IMonitorModel monitor, boolean deleteAllResults) {
		boolean result = true;
		
		if (deleteAllResults)
			result = this.deleteResultsByMonitorId(monitor.getId());
		
		result = deleteMonitor(monitor);
		
		return result;
	}

	public boolean updateMonitor(IMonitorModel monitor) {
		boolean result = false;
		
		MonitorModel storedModel = this.mMonitorProvider.get(monitor.getId());
		
		if (storedModel != null) {
			MonitorModel dbMonitor = this.toDBMonitorModel(monitor);
			result = this.mMonitorProvider.update((IModel)dbMonitor);
		
			if (monitor.getType() != storedModel.getType()) {
				if (storedModel.getType() == MonitorType.HTTP)
					this.mHTTPMonitorProvider.delete(storedModel.getId());
			}
			
			if (monitor.getType() == MonitorType.HTTP) {
				HTTPMonitorModel dbHTTPModel = this.toDBHTTPMonitorModel((com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPMonitorModel) monitor);
				result = this.mHTTPMonitorProvider.update(dbHTTPModel);
			}
		}
		
		return result;
	}

	public IMonitorModel getMonitor(long id) {
		IMonitorModel result = null;
		
		MonitorModel storedMonitor = this.mMonitorProvider.get(id);
		
		if (storedMonitor != null) {
			if (storedMonitor.getType() == MonitorType.HTTP) {
				HTTPMonitorModel storedHTTPMonitor = this.mHTTPMonitorProvider.getByMonitorId(id)[0];
				if (storedHTTPMonitor != null)
					result = this.fromDBMonitorModel(storedMonitor, storedHTTPMonitor);
			}
		}

		return result;
	}

	public IMonitorModel[] getMonitors() {
		IMonitorModel[] results = null;
		
		MonitorModel[] storedMonitors = this.mMonitorProvider.getAll();
		
		if (storedMonitors != null) {
			results = new IMonitorModel[storedMonitors.length];
			
			for (int i = 0; i < storedMonitors.length; i++) {
				MonitorModel storedMonitor = storedMonitors[i];
				if (storedMonitor.getType() == MonitorType.HTTP) {
					HTTPMonitorModel storedHTTPMonitor = this.mHTTPMonitorProvider.getByMonitorId(storedMonitor.getId())[0];
					if (storedHTTPMonitor != null)
						results[i] = this.fromDBMonitorModel(storedMonitor, storedHTTPMonitor);
				}
			}
		}

		return results;
	}

	public IMonitorModel[] getMonitors(String name) {
		IMonitorModel[] results = null;
		
		MonitorModel[] storedMonitors = this.mMonitorProvider.getByName(name);
		
		if (storedMonitors != null) {
			results = new IMonitorModel[storedMonitors.length];
			
			for (int i = 0; i < storedMonitors.length; i++) {
				MonitorModel storedMonitor = storedMonitors[i];
				if (storedMonitor.getType() == MonitorType.HTTP) {
					HTTPMonitorModel storedHTTPMonitor = this.mHTTPMonitorProvider.getByMonitorId(storedMonitor.getId())[0];
					if (storedHTTPMonitor != null)
						results[i] = this.fromDBMonitorModel(storedMonitor, storedHTTPMonitor);
				}
			}
		}

		return results;
	}
	
	public long addResult(IResultModel result) {
		ResultModel dbMonitor = this.toDBResultModel(result);
		long id = this.mResultProvider.add((IModel)dbMonitor);
		
		if (result.getType() == MonitorType.HTTP) {
			HTTPResultModel dbHTTPModel = this.toDBHTTPResultModel((com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPResultModel) result);
			dbHTTPModel.setResultId(id);
			this.mHTTPResultProvider.add(dbHTTPModel);
		}
		
		return id;
	}

	public boolean deleteResult(IResultModel model) {
		boolean result = false;
		
		long id = model.getId();
		
		ResultModel storedModel = this.mResultProvider.get(id);
		
		if (storedModel != null) {
			if (model.getType() == MonitorType.HTTP)
				result = this.mHTTPResultProvider.deleteByResultId(id);

			if (result)
				result = this.mResultProvider.delete(id);
		}
		
		return result;
	}

	public boolean deleteResultsByMonitorId(long monitorId) {
		boolean result = true;
		
		IResultModel[] results = this.getResults(monitorId);
		if (results != null)
			for (int i = 0; i < results.length; i++) {
				if (result)
					result = deleteResult(results[i]); 
			}
		
		return result;
	}

	public boolean deleteResultsByMonitorId(long monitorId, Date olderThan) {
		boolean result = true;
		
		IResultModel[] results = this.getResults(monitorId, null, olderThan);
		if (results != null)
			for (int i = 0; i < results.length; i++) {
				if (result)
					result = deleteResult(results[i]); 
			}
		
		return result;
	}

	public boolean updateResult(IResultModel model) {
		boolean result = false;
		
		ResultModel storedModel = this.mResultProvider.get(model.getId());
		
		if (storedModel != null) {
			ResultModel dbResult = this.toDBResultModel(model);
			result = this.mResultProvider.update((IModel)dbResult);
		
			if (model.getType() == MonitorType.HTTP) {
				HTTPResultModel dbHTTPModel = this.toDBHTTPResultModel((com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPResultModel) model);
				result = this.mHTTPResultProvider.update(dbHTTPModel);
			}
		}
		
		return result;
	}

	public IResultModel getResult(long id) {
		IResultModel result = null;
		
		ResultModel storedResult = this.mResultProvider.get(id);
		
		if (storedResult != null) {
		    if (storedResult.getType() == MonitorType.HTTP) {
			   HTTPResultModel storedHTTPResult = this.mHTTPResultProvider.getByResultId(id)[0];
			   if (storedHTTPResult != null)
			 	  result = this.fromDBResultModel(storedResult, storedHTTPResult);
		    }
		
		}

		return result;
	}
	
	private IResultModel[] convertDbResultArray(ResultModel[] storedResults) {
		IResultModel[] results = null;

		if (storedResults != null) {
			results = new IResultModel[storedResults.length];
			
			for (int i = 0; i < storedResults.length; i++) {
				ResultModel storedResult = storedResults[i];
				if (storedResult.getType() == MonitorType.HTTP) {
					HTTPResultModel storedHTTPResult = this.mHTTPResultProvider.getByResultId(storedResult.getId())[0];
					if (storedHTTPResult != null)
						results[i] = this.fromDBResultModel(storedResult, storedHTTPResult);
				}
			}
		}
		
		return results;
	}

	public IResultModel[] getResults() {
		return convertDbResultArray(this.mResultProvider.getAll());
	}

	public IResultModel[] getResults(long monitorId) {
		return convertDbResultArray(this.mResultProvider.getByMonitorId(monitorId));
	}

	public IResultModel[] getResults(long monitorId, Date from, Date to) {
		return convertDbResultArray(this.mResultProvider.getByMonitorId(monitorId, from, to));
	}

	public IResultModel[] getResults(long monitorId, int skip, int take) {
		return convertDbResultArray(this.mResultProvider.getByMonitorId(monitorId, skip, take));
	}

	public IResultModel[] getResults(long monitorId, Date from, Date to, int skip, int take) {
		return convertDbResultArray(this.mResultProvider.getByMonitorId(monitorId, from, to, skip, take));
	}
	
	private MonitorModel toDBMonitorModel(IMonitorModel monitor) {
		MonitorModel dbMonitor = new MonitorModel();

		dbMonitor.setId(monitor.getId());
		dbMonitor.setName(monitor.getName());
		dbMonitor.setFrequency(monitor.getFrequency());
		dbMonitor.setType(monitor.getType());
		dbMonitor.setNotifyWhenDown(monitor.getNotifyWhenDown());
		dbMonitor.setNotifyWhenUp(monitor.getNotifyWhenUp());
		dbMonitor.setUnavailableThreshold(monitor.getUnavailableThreshold());
		dbMonitor.setUnavailableCount(monitor.getUnavailableCount());
		dbMonitor.setCreated(monitor.getCreated());
		dbMonitor.setAmended(monitor.getAmended());
		
		return dbMonitor;
	}
	
	private HTTPMonitorModel toDBHTTPMonitorModel(com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPMonitorModel appMonitor) {
		HTTPMonitorModel dbHTTPMonitor = new HTTPMonitorModel();

		// Try and the ID from the database (assuming we have anything to work with)
		if (appMonitor.getId() > 0) {
			HTTPMonitorModel storedMonitor = this.mHTTPMonitorProvider.getByMonitorId(appMonitor.getId())[0];
			dbHTTPMonitor.setId(storedMonitor.getId());
		}
		
		dbHTTPMonitor.setMonitorId(appMonitor.getId());
		dbHTTPMonitor.setURL(appMonitor.getURL());
		
		return dbHTTPMonitor;
	}
	
	private com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPMonitorModel fromDBMonitorModel(MonitorModel dbMonitor, HTTPMonitorModel dbHTTPMonitor) {
		com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPMonitorModel appMonitor = new com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPMonitorModel();
		
		appMonitor.setId(dbMonitor.getId());
		appMonitor.setName(dbMonitor.getName());
		appMonitor.setFrequency(dbMonitor.getFrequency());
		appMonitor.setType(dbMonitor.getType());
		appMonitor.setNotifyWhenDown(dbMonitor.getNotifyWhenDown());
		appMonitor.setNotifyWhenUp(dbMonitor.getNotifyWhenUp());
		appMonitor.setUnavailableThreshold(dbMonitor.getUnavailableThreshold());
		appMonitor.setUnavailableCount(dbMonitor.getUnavailableCount());
		appMonitor.setCreated(dbMonitor.getCreated());
		appMonitor.setAmended(dbMonitor.getAmended());
		
		appMonitor.setURL(dbHTTPMonitor.getURL());
		
		return appMonitor;
	}

	private ResultModel toDBResultModel(IResultModel result) {
		ResultModel dbResult = new ResultModel();

		dbResult.setId(result.getId());
		dbResult.setMonitorId(result.getMonitorId());
		dbResult.setType(result.getType());
		dbResult.setTimestamp(result.getTimestamp());
		
		return dbResult;
	}
	
	private HTTPResultModel toDBHTTPResultModel(com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPResultModel appResult) {
		HTTPResultModel dbHTTPResult = new HTTPResultModel();

		// Try and the ID from the database (assuming we have anything to work with)
		if (appResult.getId() > 0) {
			HTTPResultModel storedResult = this.mHTTPResultProvider.getByResultId(appResult.getId())[0];
			dbHTTPResult.setId(storedResult.getId());
		}
		
		dbHTTPResult.setResultId(appResult.getId());
		dbHTTPResult.setResponseCode(appResult.getResponseCode());
		
		return dbHTTPResult;
	}
	
	private com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPResultModel fromDBResultModel(ResultModel dbResult, HTTPResultModel dbHTTPResult) {
		com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPResultModel appResult = new com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPResultModel();
		
		appResult.setId(dbResult.getId());
		appResult.setMonitorId(dbResult.getMonitorId());
		appResult.setType(dbResult.getType());
		appResult.setTimestamp(dbResult.getTimestamp());

		appResult.setResponseCode(dbHTTPResult.getResponseCode());
		
		return appResult;
	}

}
