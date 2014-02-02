package com.red_folder.phonegap.plugin.availabilitymonitor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "AvailabilityMonitor.db";
	private static final int DATABASE_VERSION = 1;

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		onUpgrade(db, 0, 1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		SQLiteMonitorProvider.UpgradeDatabase(db, oldVersion, newVersion);
		SQLiteHTTPMonitorProvider.UpgradeDatabase(db, oldVersion, newVersion);

		SQLiteResultProvider.UpgradeDatabase(db, oldVersion, newVersion);
		SQLiteHTTPResultProvider.UpgradeDatabase(db, oldVersion, newVersion);
	}
	
}
