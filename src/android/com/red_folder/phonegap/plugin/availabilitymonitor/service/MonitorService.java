package com.red_folder.phonegap.plugin.availabilitymonitor.service;

import com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL;
import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IMonitor;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IMonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IResultModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.utils.Log;
import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;
import org.json.JSONObject;

public class MonitorService extends BackgroundService {

	private static String TAG = MonitorService.class.getSimpleName();
	
	@Override
	protected JSONObject initialiseLatestResult() {
		// TODO Auto-generated method stub
		return new JSONObject();
	}

	@Override
	protected JSONObject doWork() {
		SQLiteDAL dal = new SQLiteDAL(this);
		
		IMonitorModel[] monitors = dal.getMonitors();
		
		Log.d(TAG, "Checking for monitors");
		if (monitors != null) {
			Log.d(TAG, "Monitors not null.  There are " + monitors.length + " monitors.");
			if (monitors.length > 0) {
				for (int i = 0; i < monitors.length; i++) {
					Log.d(TAG, "Processing monitor[" + i + "]");
					if (monitors[i].getType() == MonitorType.HTTP) {
						Log.d(TAG, "Monitor is HTTP, running logic");
						IMonitorModel monitor = monitors[i];
						IMonitor logic = new com.red_folder.phonegap.plugin.availabilitymonitor.monitors.HTTPMonitor();
						IResultModel result = logic.run(monitor);
						
						if (dal.addResult(result) > 0) {
							Log.d(TAG, "Saved result");
							boolean changed = false;
							if (result.isAvailable()) {
								Log.d(TAG, "Monitored target is available");
								if (monitor.getUnavailableCount() > 0) {
									monitor.setUnavailableCount(0);
									changed = true;

									Log.d(TAG, "Monitored target back Up");

									// TODO 
									// Add NotifyWhenUp logic
								}
							} else {
								Log.d(TAG, "Monitored target is unavailable");
								monitor.setUnavailableCount( monitor.getUnavailableCount() + 1);
								changed = true;

								Log.d(TAG, "Monitored target back Up.  Failed " + monitor.getUnavailableCount() + " times");

								// TODO 
								// Add NotifyWhenDown logic
							}
							
							if (changed)
								if (dal.updateMonitor(monitor))
									Log.d(TAG, "Monitor saved");
								else
									Log.d(TAG, "Monitor couldn't be saved");
							
						} else {
							Log.d(TAG, "Failed to save result");
						}
					}
				}
			}
		}
		
		return new JSONObject();
	}

	@Override
	protected JSONObject getConfig() {
		// TODO Auto-generated method stub
		return new JSONObject();
	}

	@Override
	protected void setConfig(JSONObject config) {
		// TODO Auto-generated method stub
		
	}

}
