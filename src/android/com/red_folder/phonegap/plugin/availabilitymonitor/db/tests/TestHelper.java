package com.red_folder.phonegap.plugin.availabilitymonitor.db.tests;

import android.content.Context;

import com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPMonitorProvider;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteHTTPResultProvider;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteMonitorProvider;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.SQLiteResultProvider;

public class TestHelper {

	public static void emptyDB(Context context) {
		emptyMonitors(context);
		emptyHTTPMonitors(context);
		
		emptyResults(context);
		emptyHTTPResults(context);
	}

	public static void emptyMonitors(Context context) {
		SQLiteMonitorProvider monitorProvider = new SQLiteMonitorProvider(context);
		monitorProvider.deleteAll();
	}

	public static void emptyHTTPMonitors(Context context) {
		SQLiteHTTPMonitorProvider httpMonitorProvider = new SQLiteHTTPMonitorProvider(context);
		httpMonitorProvider.deleteAll();
	}
	
	public static void emptyResults(Context context) {
		SQLiteResultProvider resultProvider = new SQLiteResultProvider(context);
		resultProvider.deleteAll();
	}

	public static void emptyHTTPResults(Context context) {
		SQLiteHTTPResultProvider httpResultProvider = new SQLiteHTTPResultProvider(context);
		httpResultProvider.deleteAll();
	}

}
