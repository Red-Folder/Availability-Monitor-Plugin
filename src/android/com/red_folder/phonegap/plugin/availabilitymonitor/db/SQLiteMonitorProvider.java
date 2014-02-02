package com.red_folder.phonegap.plugin.availabilitymonitor.db;

import java.util.Arrays;
import java.util.Date;

import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.Expression.ExpressionType;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.IModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.MonitorModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteMonitorProvider extends BaseProvider {
	
	private static String COLUMN_NAME = "Name";
	private static String COLUMN_FREQUENCY = "Frequency";
	private static String COLUMN_TYPE = "Type";
	private static String COLUMN_NOTIFY_WHEN_DOWN = "NotifyWhenDown";
	private static String COLUMN_NOTIFY_WHEN_UP = "NotifyWhenUp";
    private static String COLUMN_CREATED = "Created";
    private static String COLUMN_AMENDED = "Amended";
    private static String COLUMN_UNAVAILABLE_THRESHOLD = "UnavailableThreshold";
    private static String COLUMN_UNAVAILABLE_COUNT = "UnavailableCount";

    private static String VERSION1_1 = 	"create table Monitors (" +
											"_id integer primary key autoincrement, " + 
											"Name text, " +
											"Frequency text, " +
											"Type integer, " + 
											"NotifyWhenDown integer, " +
											"NotifyWhenUp integer, " +
											"Created long, " +
											"Amended long, " +
											"UnavailableThreshold integer, " + 
											"UnavailableCount integer " + 
										");";
    
	public SQLiteMonitorProvider(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return "Monitors";
	}
	
	@Override
	protected String[] getColumns() {
		return new String[] { 	COLUMN_ID,
								COLUMN_NAME,
								COLUMN_FREQUENCY,
								COLUMN_TYPE,
								COLUMN_NOTIFY_WHEN_DOWN,
								COLUMN_NOTIFY_WHEN_UP,
								COLUMN_CREATED,
								COLUMN_AMENDED,
								COLUMN_UNAVAILABLE_THRESHOLD,
								COLUMN_UNAVAILABLE_COUNT } ;
	}

	@Override
	protected ContentValues getContentValues(IModel model) {
		MonitorModel monitor = (MonitorModel)model;
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, monitor.getName());
		values.put(COLUMN_FREQUENCY, monitor.getFrequency());
		values.put(COLUMN_TYPE, monitor.getType().toInt()); 
		values.put(COLUMN_NOTIFY_WHEN_DOWN, monitor.getNotifyWhenDown() ? 1 : 0 );
		values.put(COLUMN_NOTIFY_WHEN_UP, monitor.getNotifyWhenUp() ? 1 : 0);
		values.put(COLUMN_CREATED, monitor.getCreated().getTime());
		values.put(COLUMN_AMENDED, monitor.getAmended().getTime());
		values.put(COLUMN_UNAVAILABLE_THRESHOLD, monitor.getUnavailableThreshold());
		values.put(COLUMN_UNAVAILABLE_COUNT, monitor.getUnavailableCount());
		
		return values;
	}

	@Override
	protected IModel cursorToModel(Cursor cursor) {
		MonitorModel monitor = new MonitorModel();
		
	    monitor.setId(cursor.getLong(0));
	    monitor.setName(cursor.getString(1));
	    monitor.setFrequency(cursor.getInt(2));
	    monitor.setType(MonitorType.fromInt(cursor.getInt(3)));
	    monitor.setNotifyWhenDown(cursor.getShort(4) == 1 ? true : false);
	    monitor.setNotifyWhenUp(cursor.getShort(5) == 1 ? true : false);
	    monitor.setCreated(new Date(cursor.getLong(6)));
	    monitor.setAmended(new Date(cursor.getLong(7)));
	    monitor.setUnavailableThreshold(cursor.getInt(8));
	    monitor.setUnavailableCount(cursor.getInt(9));
	    
	    return (IModel) monitor;
	}

	protected static void UpgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 1)
			db.execSQL(VERSION1_1);
					
	}

	public long add(MonitorModel model) {
		return super.add(model);
	}

	public MonitorModel get(long id) {
		return (MonitorModel)super.get(id);
	}

	public MonitorModel[] getByName(String name) {
		Expression[] expressions = new Expression[1];
		expressions[0] = new Expression(COLUMN_NAME, ExpressionType.Equals, name);
		IModel[] tmpArray = super.getBy(expressions);

		if (tmpArray == null)
			return null;
		else
			return Arrays.copyOf(tmpArray, tmpArray.length, MonitorModel[].class);
	}

	public MonitorModel[] getAll() {
		IModel[] tmpArray = super.getAll();
		
		if (tmpArray == null)
			return null;
		else
			return Arrays.copyOf(tmpArray, tmpArray.length, MonitorModel[].class);
	}

	public boolean delete(long id) {
		return super.delete(id);
	}

	public boolean deleteAll() {
		return super.deleteAll();
	}

	public boolean update(MonitorModel model) {
		return super.update(model);
	}

}
