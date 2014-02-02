package com.red_folder.phonegap.plugin.availabilitymonitor.db;

import java.util.Arrays;

import com.red_folder.phonegap.plugin.availabilitymonitor.utils.Log;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.Expression.ExpressionType;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.IModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.HTTPMonitorModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteHTTPMonitorProvider extends BaseProvider {

	private static String TAG = SQLiteHTTPMonitorProvider.class.getSimpleName();
	
	private static String COLUMN_MONITOR_ID = "MonitorId";
	private static String COLUMN_URL = "Url";

    private static String VERSION1_1 = 	"create table HttpMonitors (" +
    										"_id integer primary key autoincrement, " +
											"MonitorId integer, " + 
											"Url text " + 
										");";
    
	public SQLiteHTTPMonitorProvider(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return "HttpMonitors";
	}

	@Override
	protected String[] getColumns() {
		return new String[] { 	COLUMN_ID,
								COLUMN_MONITOR_ID,
								COLUMN_URL } ;
	}

	@Override
	protected ContentValues getContentValues(IModel model) {
		HTTPMonitorModel monitor = (HTTPMonitorModel)model;
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_MONITOR_ID, monitor.getMonitorId());
		values.put(COLUMN_URL, monitor.getURL());
		
		return values;
	}

	@Override
	protected IModel cursorToModel(Cursor cursor) {
		// Because we may have different Monitor Models, we need to use the MonitorType createModel,
		// to produce the relevant model
		HTTPMonitorModel monitor = new HTTPMonitorModel();

	    monitor.setId(cursor.getLong(0));
	    monitor.setMonitorId(cursor.getLong(1));
		monitor.setURL(cursor.getString(2));
	    
	    return (IModel) monitor;
	}

	protected static void UpgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 1)
			db.execSQL(VERSION1_1);
					
	}

	public long add(HTTPMonitorModel model) {
		return super.add(model);
	}

	public HTTPMonitorModel get(long id) {
		return (HTTPMonitorModel)super.get(id);
	}

	public HTTPMonitorModel[] getAll() {
		IModel[] tmpArray = super.getAll();
		return Arrays.copyOf(tmpArray, tmpArray.length, HTTPMonitorModel[].class);
	}

	public HTTPMonitorModel[] getByMonitorId(long monitorId) {
		Expression[] expressions = new Expression[1];
		expressions[0] = new Expression(COLUMN_MONITOR_ID, ExpressionType.Equals, monitorId);
		IModel[] tmpArray = super.getBy(expressions);

		return Arrays.copyOf(tmpArray, tmpArray.length, HTTPMonitorModel[].class);
	}

	public boolean delete(long id) {
		return super.delete(id);
	}

	public boolean deleteAll() {
		return super.deleteAll();
	}

	public boolean deleteByMonitorId(long monitorId) {
		return super.deleteBy(COLUMN_MONITOR_ID, monitorId);
	}

	public boolean update(HTTPMonitorModel model) {
		return super.update(model);
	}
}
