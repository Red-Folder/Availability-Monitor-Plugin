package com.red_folder.phonegap.plugin.availabilitymonitor.db;

import java.util.Arrays;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.red_folder.phonegap.plugin.availabilitymonitor.db.Expression.ExpressionType;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.IModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.ResultModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.enums.MonitorType;

public class SQLiteResultProvider  extends BaseProvider {
	
	private static String COLUMN_MONITOR_ID = "MonitorId";
	private static String COLUMN_TYPE = "Type";
	private static String COLUMN_TIMESTAMP = "Timestamp";

    private static String VERSION1_1 = 	"create table Results (" +
											"_id integer primary key autoincrement, " + 
											"MonitorId long, " +
											"Type integer, " + 
											"Timestamp long " +
										");";
    
	public SQLiteResultProvider(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return "Results";
	}
	
	@Override
	protected String[] getColumns() {
		return new String[] { 	COLUMN_ID,
								COLUMN_MONITOR_ID,
								COLUMN_TYPE,
								COLUMN_TIMESTAMP } ;
	}

	@Override
	protected ContentValues getContentValues(IModel model) {
		ResultModel result = (ResultModel)model;
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_MONITOR_ID, result.getMonitorId());
		values.put(COLUMN_TYPE, result.getType().toInt()); 
		values.put(COLUMN_TIMESTAMP, result.getTimestamp().getTime());
		
		return values;
	}

	@Override
	protected IModel cursorToModel(Cursor cursor) {
		ResultModel result = new ResultModel();
		
	    result.setId(cursor.getLong(0));
	    result.setMonitorId(cursor.getLong(1));
	    result.setType(MonitorType.fromInt(cursor.getInt(2)));
	    result.setTimestamp(new Date(cursor.getLong(3)));
	    
	    return (IModel) result;
	}

	protected static void UpgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 1)
			db.execSQL(VERSION1_1);
	}

	public long add(ResultModel model) {
		return super.add(model);
	}

	public ResultModel get(long id) {
		return (ResultModel)super.get(id);
	}

	public ResultModel[] getByMonitorId(long monitorId) {
		Expression[] expressions = new Expression[1];
		expressions[0] = new Expression(COLUMN_MONITOR_ID, ExpressionType.Equals, monitorId);
		IModel[] tmpArray = super.getBy(expressions);

		if (tmpArray == null)
			return null;
		else
		    return Arrays.copyOf(tmpArray, tmpArray.length, ResultModel[].class);
	}

	public ResultModel[] getByMonitorId(long monitorId, int skip, int take) {
		Expression[] expressions = new Expression[1];
		expressions[0] = new Expression(COLUMN_MONITOR_ID, ExpressionType.Equals, monitorId);
		IModel[] tmpArray = super.getBy(expressions, skip, take);

		if (tmpArray == null)
			return null;
		else
			return Arrays.copyOf(tmpArray, tmpArray.length, ResultModel[].class);
	}

	public ResultModel[] getByMonitorId(long monitorId, Date from, Date to) {
		int columns = from != null && to != null ? 3 : (from != null || to != null ? 2 : 1);
		Expression[] expressions = new Expression[columns];
		expressions[0] = new Expression(COLUMN_MONITOR_ID, ExpressionType.Equals, monitorId);
		if (from != null)
			expressions[1] = new Expression(COLUMN_TIMESTAMP, ExpressionType.GreaterThanOrEquals, from);
		if (to != null)
			expressions[columns-1] = new Expression(COLUMN_TIMESTAMP, ExpressionType.LessThanOrEquals, to);
		
		
		IModel[] tmpArray = super.getBy(expressions);

		if (tmpArray == null)
			return null;
		else
			return Arrays.copyOf(tmpArray, tmpArray.length, ResultModel[].class);
	}

	public ResultModel[] getByMonitorId(long monitorId, Date from, Date to, int skip, int take) {
		int columns = from != null && to != null ? 3 : (from != null || to != null ? 2 : 1);
		Expression[] expressions = new Expression[columns];
		expressions[0] = new Expression(COLUMN_MONITOR_ID, ExpressionType.Equals, monitorId);
		if (from != null)
			expressions[1] = new Expression(COLUMN_TIMESTAMP, ExpressionType.GreaterThanOrEquals, from);
		if (to != null)
			expressions[columns-1] = new Expression(COLUMN_TIMESTAMP, ExpressionType.LessThanOrEquals, to);
		
		IModel[] tmpArray = super.getBy(expressions, skip, take);

		if (tmpArray == null)
			return null;
		else
			return Arrays.copyOf(tmpArray, tmpArray.length, ResultModel[].class);
	}

	public ResultModel[] getAll() {
		IModel[] tmpArray = super.getAll();
		
		if (tmpArray == null)
			return null;
		else
			return Arrays.copyOf(tmpArray, tmpArray.length, ResultModel[].class);
	}

	public boolean delete(long id) {
		return super.delete(id);
	}

	public boolean deleteAll() {
		return super.deleteAll();
	}

	public boolean update(ResultModel model) {
		return super.update(model);
	}

}