package com.red_folder.phonegap.plugin.availabilitymonitor.db;

import java.util.Arrays;

import com.red_folder.phonegap.plugin.availabilitymonitor.utils.Log;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.Expression.ExpressionType;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.HTTPResultModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.IModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteHTTPResultProvider extends BaseProvider {

	private static String TAG = SQLiteHTTPResultProvider.class.getSimpleName();
	
	private static String COLUMN_RESULT_ID = "ResultId";
	private static String COLUMN_RESPONSE_CODE = "ResponseCode";

    private static String VERSION1_1 = 	"create table HttpResults (" +
    										"_id integer primary key autoincrement, " +
											"ResultId integer, " + 
											"ResponseCode integer " + 
										");";
    
	public SQLiteHTTPResultProvider(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return "HttpResults";
	}

	@Override
	protected String[] getColumns() {
		return new String[] { 	COLUMN_ID,
								COLUMN_RESULT_ID,
								COLUMN_RESPONSE_CODE } ;
	}

	@Override
	protected ContentValues getContentValues(IModel model) {
		HTTPResultModel monitor = (HTTPResultModel)model;
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_RESULT_ID, monitor.getResultId());
		values.put(COLUMN_RESPONSE_CODE, monitor.getResponseCode());
		
		return values;
	}

	@Override
	protected IModel cursorToModel(Cursor cursor) {
		HTTPResultModel monitor = new HTTPResultModel();

	    monitor.setId(cursor.getLong(0));
	    monitor.setResultId(cursor.getLong(1));
		monitor.setResponseCode(cursor.getInt(2));
	    
	    return (IModel) monitor;
	}

	protected static void UpgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 1)
			db.execSQL(VERSION1_1);
					
	}

	public long add(HTTPResultModel model) {
		return super.add(model);
	}

	public HTTPResultModel get(long id) {
		return (HTTPResultModel)super.get(id);
	}

	public HTTPResultModel[] getAll() {
		IModel[] tmpArray = super.getAll();
		return Arrays.copyOf(tmpArray, tmpArray.length, HTTPResultModel[].class);
	}

	public HTTPResultModel[] getByResultId(long resultId) {
		Expression[] expressions = new Expression[1];
		expressions[0] = new Expression(COLUMN_RESULT_ID, ExpressionType.Equals, resultId);
		IModel[] tmpArray = super.getBy(expressions);

		return Arrays.copyOf(tmpArray, tmpArray.length, HTTPResultModel[].class);
	}

	public boolean delete(long id) {
		return super.delete(id);
	}

	public boolean deleteAll() {
		return super.deleteAll();
	}

	public boolean deleteByResultId(long resultId) {
		return super.deleteBy(COLUMN_RESULT_ID, resultId);
	}

	public boolean update(HTTPResultModel model) {
		return super.update(model);
	}
}
