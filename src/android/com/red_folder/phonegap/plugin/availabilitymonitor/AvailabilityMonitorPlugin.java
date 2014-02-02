package com.red_folder.phonegap.plugin.availabilitymonitor;

import java.util.Date;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;

import com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteDAL;
import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;
import com.red_folder.phonegap.plugin.availabilitymonitor.interfaces.IMonitorModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.models.HTTPMonitorModel;

public class AvailabilityMonitorPlugin extends CordovaPlugin {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private static final String TAG = AvailabilityMonitorPlugin.class.getSimpleName();

	/*
	 ************************************************************************************************
	 * Keys 
	 ************************************************************************************************
	 */
	public static final String ACTION_GET_MONITORS = "getMonitors";
	public static final String ACTION_SAVE_MONITOR = "saveMonitor";

	/*
	 ************************************************************************************************
	 * Fields 
	 ************************************************************************************************
	 */

	/*
	 ************************************************************************************************
	 * Overriden Methods 
	 ************************************************************************************************
	 */
	// Part fix for https://github.com/Red-Folder/Cordova-Plugin-BackgroundService/issues/19
	//public boolean execute(String action, JSONArray data, CallbackContext callback) {
	@Override
	public boolean execute(final String action, final JSONArray data, final CallbackContext callback) {
		boolean result = false;

		Log.d(TAG, "start of Execute");
		
		try {

			Log.d(TAG, "Action: " + action);

			if (ACTION_GET_MONITORS.equals(action) ||
				ACTION_SAVE_MONITOR.equals(action)) { 
			
				Log.d(TAG, "Setting up the runnable thread");
				
				final Context context = this.cordova.getActivity();
				cordova.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						PluginResult pluginResult = null;
						if (ACTION_GET_MONITORS.equals(action))
							pluginResult = getMonitors(context);
					
						if (ACTION_SAVE_MONITOR.equals(action))
							pluginResult = saveMonitor(context, data);

						if (pluginResult != null) {
							Log.d(TAG, "pluginResult = " +  pluginResult.toString());
							Log.d(TAG, "pluginResult.getMessage() = " +  pluginResult.getMessage());
						}
						
						callback.sendPluginResult(pluginResult);
					}
				});

				result = true;
			} else {
				result = false;
			}
			
		} catch (Exception ex) {
			Log.d(TAG, "Exception - " + ex.getMessage());
		}

		Log.d(TAG, "end of Execute");

		return result;
	}

	private PluginResult getMonitors(Context context) {
		SQLiteDAL dal = new SQLiteDAL(context);
		
		JSONArray json = new JSONArray();
		IMonitorModel[] tmpMonitors = dal.getMonitors();
		if (tmpMonitors != null) {
			//HTTPMonitorModel[] monitors = (HTTPMonitorModel[])tmpMonitors; 
			json = convertModelToJSON(tmpMonitors);
		}
	
		return new PluginResult(Status.OK, json);
	}
	
	private PluginResult saveMonitor(Context context, JSONArray data) {
		PluginResult result = null;
		
		SQLiteDAL dal = new SQLiteDAL(context);
		
		HTTPMonitorModel monitor = new HTTPMonitorModel();
		
		Log.d(TAG, "Contents of data: " + data.toString());
		
		Log.d(TAG, "Found " + data.length() + " rules");
		if (data.length() > 0) {

			JSONObject monitorJSON = data.optJSONObject(0);

			monitor.setId(monitorJSON.optLong("Id", 0));
			monitor.setName(monitorJSON.optString("Name", "Unknown"));
			monitor.setFrequency(monitorJSON.optInt("Frequency", 1));
			monitor.setType(MonitorType.parse(monitorJSON.optString("Type", "HTTP")));
			monitor.setNotifyWhenDown(monitorJSON.optBoolean("NotifyWhenDown"));
			monitor.setNotifyWhenUp(monitorJSON.optBoolean("NotifyWhenUp"));
			monitor.setUnavailableThreshold(monitorJSON.optInt("UnavailableThreshold", 5));
			monitor.setURL(monitorJSON.optString("URL", "http://www.google.com"));
			
			monitor.setCreated(new Date());
			monitor.setAmended(new Date());

			HTTPMonitorModel existing = null;
			if (monitor.getId() > 0) {
				existing = (HTTPMonitorModel) dal.getMonitor(monitor.getId());
				
				if (existing != null) {
					monitor.setCreated(existing.getCreated());
					monitor.setUnavailableCount(existing.getUnavailableCount());
				}
			}
			
			if (existing == null) {
				monitor.setId(0);
				long newId = dal.addMonitor(monitor);
				JSONObject json = new JSONObject();
				try { json.put("Success", true); } catch (JSONException ex) { Log.d(TAG, "Adding Success to JSONObject failed", ex); }
				try { json.put("NewId", newId); } catch (JSONException ex) { Log.d(TAG, "Adding NewId to JSONObject failed", ex); }
				result = new PluginResult(Status.OK, json);
			} else {
				boolean updated = dal.updateMonitor(monitor);
				JSONObject json = new JSONObject();
				try { json.put("Success", updated); } catch (JSONException ex) { Log.d(TAG, "Adding Success to JSONObject failed", ex); }
				result = new PluginResult(Status.OK, json);
			}
		} else {
			JSONObject json = new JSONObject();
			try { json.put("Success", false); } catch (JSONException ex) { Log.d(TAG, "Adding Success to JSONObject failed", ex); }
			try { json.put("ErrorMessage", "No data passed in"); } catch (JSONException ex) { Log.d(TAG, "Adding ErrorMessage to JSONObject failed", ex); }
			result = new PluginResult(Status.ERROR, json);
		}

		return result;
	}
	
	private JSONArray convertModelToJSON(IMonitorModel[] monitors) {
		JSONArray results = new JSONArray();
		
		for (int i = 0; i < monitors.length; i++) {
			JSONObject monitor = new JSONObject();
			
			try { monitor.put("Id", monitors[i].getId()); } catch (Exception ex) {Log.d(TAG, "Adding Id to JSONObject failed", ex);};
			try { monitor.put("Name", monitors[i].getName()); } catch (Exception ex) {Log.d(TAG, "Adding Name to JSONObject failed", ex);};
			try { monitor.put("Frequency", monitors[0].getFrequency()); } catch (Exception ex) {Log.d(TAG, "Adding Frequency to JSONObject failed", ex);};
			try { monitor.put("Type", monitors[0].getType()); } catch (Exception ex) {Log.d(TAG, "Adding Type to JSONObject failed", ex);};
			try { monitor.put("NotifyWhenDown", monitors[0].getNotifyWhenDown()); } catch (Exception ex) {Log.d(TAG, "Adding NotifyWhenDown to JSONObject failed", ex);};
			try { monitor.put("NotifyWhenUp", monitors[0].getNotifyWhenUp()); } catch (Exception ex) {Log.d(TAG, "Adding NotifyWhenUp to JSONObject failed", ex);};
			try { monitor.put("UnavailableThreshold", monitors[0].getUnavailableThreshold()); } catch (Exception ex) {Log.d(TAG, "Adding UnavailableThreshold to JSONObject failed", ex);};
			try { monitor.put("UnavailableCount", monitors[0].getUnavailableCount()); } catch (Exception ex) {Log.d(TAG, "Adding UnavailableCount to JSONObject failed", ex);};
			try { monitor.put("Created", monitors[0].getCreated()); } catch (Exception ex) {Log.d(TAG, "Adding Created to JSONObject failed", ex);};
			try { monitor.put("Amended", monitors[0].getAmended()); } catch (Exception ex) {Log.d(TAG, "Adding Amended to JSONObject failed", ex);};
			
			if (monitors[i].getType() == MonitorType.HTTP)
				try { monitor.put("URL", ((HTTPMonitorModel)monitors[0]).getURL()); } catch (Exception ex) {Log.d(TAG, "Adding URL to JSONObject failed", ex);};
			
			results.put(monitor);
		}
		
		return results;
	}

}
