package com.red_folder.phonegap.plugin.availabilitymonitor.db;

import java.util.ArrayList;
import java.util.List;

import com.red_folder.phonegap.plugin.availabilitymonitor.db.models.IModel;
import com.red_folder.phonegap.plugin.availabilitymonitor.utils.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public abstract class BaseProvider {
	
	private static String TAG = BaseProvider.class.getSimpleName();
	
	protected String TABLE_NAME = "NOTSET";
	protected static String COLUMN_ID = "_id";
	
	protected String[] ALL_COLUMNS = null;
	
	// Database fields
	protected SQLiteDatabase mDB;
	private SQLiteHelper mHelper;
	protected Context mContext;
	
	private String mOrderBy = null;
	
	protected BaseProvider(Context context) {
		this.mContext = context;
		this.mHelper = new SQLiteHelper(context);
		
		TABLE_NAME = getTableName();
		ALL_COLUMNS = getColumns();
	}
	
	public String getOrderBy() {
		return this.mOrderBy;
	}
	
	public void setOrderBy(String orderBy) {
		this.mOrderBy = orderBy;
	}
	
	private void open() throws SQLException {
		this.mDB = this.mHelper.getWritableDatabase();
	}

	private void close() {
		try {
			this.mHelper.close();
		} catch (Exception ex) {
			
		}
	}

	protected long add(IModel model) {
		long result = -1;
		
		try {
			ContentValues values = getContentValues(model);

			// Add the ID if we already know it
			
			open();
			if (model.getId() > 0) {
				values.put(COLUMN_ID, model.getId());
				this.mDB.insert(TABLE_NAME, null, values);
				result = model.getId();
			} else {
				result = this.mDB.insert(TABLE_NAME, null, values);
			}
	    	close();
	    	
	    	Log.d(TAG, "Created record in " + TABLE_NAME + "- new id = " + result);
	    	
		} catch (Exception ex) {
			close();
			Log.d(TAG, "Add failed to table " + TABLE_NAME, ex);
		}
	    
	    return result;
	}
	
	protected boolean delete(long id) {
		boolean result = false;
		
		try {
			open();
			int recordsDeleted = this.mDB.delete(TABLE_NAME, COLUMN_ID + " = " + id, null);
			close();
			
			if (recordsDeleted > 0)
				result = true;
			else 
				Log.d(TAG, "Delete failed, unable to find id " + id + " from " + TABLE_NAME);

		} catch (Exception ex) {
			close();
			Log.d(TAG, "Delete failed for id " + id + " from " + TABLE_NAME, ex);
		}
		
		return result;
	}

	protected boolean deleteBy(Expression[] expressions) {
		boolean result = false;
		String whereClause = null;
		
		try {
			for (int i = 0; i < expressions.length; i++) {
				if (whereClause == null)
					whereClause = expressions[i].toString();
				else
					whereClause += " and " + expressions[i].toString();
			}

			open();
			int recordsDeleted = this.mDB.delete(TABLE_NAME, whereClause, null);
			close();

			if (recordsDeleted > 0)
				result = true;
			else 
				Log.d(TAG, "Delete failed, unable to find ( " + whereClause + ") from " + TABLE_NAME);

		} catch (Exception ex) {
			close();
			Log.d(TAG, "Delete failed for (" + whereClause + ") in " + TABLE_NAME, ex);
		}

		return result;
	}

	protected boolean deleteAll() {
		boolean result = false;
		
		try {
			open();
			int recordsDeleted = this.mDB.delete(TABLE_NAME, null, null);
			close();
			
			if (recordsDeleted > 0) {
				result = true;
			}
	        
		} catch (Exception ex) {
			close();
			Log.d(TAG, "Delete failed for all from " + TABLE_NAME, ex);
		}
		
		return result;
	}
	
	protected boolean deleteBy(String columnName, long value) {
		boolean result = false;
		
		try {
			open();
			int recordsDeleted = this.mDB.delete(TABLE_NAME, columnName + " = " + value, null);
			close();
			
			if (recordsDeleted > 0)
				result = true;
			else 
				Log.d(TAG, "Delete failed, unable to find " + columnName + " " + value + " from " + TABLE_NAME);

	        
		} catch (Exception ex) {
			close();
			Log.d(TAG, "Delete failed for " + columnName + " " + value + " from " + TABLE_NAME, ex);
		}
		
		return result;
	}

	
	protected boolean update(IModel model) {
		boolean result = false;
		
		try {
			ContentValues values = getContentValues(model);
			
			open();
			int recordsDeleted = this.mDB.update(TABLE_NAME, values, COLUMN_ID + " = " + model.getId(), null);
			close();
			
			if (recordsDeleted > 0) {
				result = true;
			}
	        
		} catch (Exception ex) {
			close();
			Log.d(TAG, "Update failed for id " + model.getId() + " in " + TABLE_NAME, ex);

		}
		
		return result;
		
	}


	protected IModel get(long id) {
		IModel[] list = null;
		
		try {
			open();
			Cursor cursor = this.mDB.query(TABLE_NAME, ALL_COLUMNS, COLUMN_ID + " = " + id, null, null, null, this.mOrderBy);
		
			list = cursorToArray(cursor);
			close();
		} catch (Exception ex) {
			close();
			Log.d(TAG, "Get failed for id " + id + " in " + TABLE_NAME, ex);
		}
		
		if (list == null || list.length == 0) {
	    	Log.d(TAG, "Unable to find record " + id + " in " + TABLE_NAME);
			return null;
		} else {
			// Should only be 1, but we return first just to be sure
	    	Log.d(TAG, "Found record " + id + " in " + TABLE_NAME);
			return list[0];
		}
	}

	protected IModel[] getBy(Expression[] expressions) {
		return getBy(expressions, -1, -1);
	}
	
	protected IModel[] getBy(Expression[] expressions, int skip, int take) {
		IModel[] list = null;
		String whereClause = null;
		
		try {
			for (int i = 0; i < expressions.length; i++) {
				if (whereClause == null)
					whereClause = expressions[i].toString();
				else
					whereClause += " and " + expressions[i].toString();
			}

			String limit = null;
			if (skip > -1 || take > -1) {
				limit = skip + ", " + take;
			}

			open();
			Cursor cursor = this.mDB.query(TABLE_NAME, ALL_COLUMNS, whereClause, null, null, null, this.mOrderBy, limit);
		
			list = cursorToArray(cursor);
			close();
		} catch (Exception ex) {
			close();
			Log.d(TAG, "Get failed for (" + whereClause + ") in " + TABLE_NAME, ex);
		}
		
		if (list == null || list.length == 0) {
	    	Log.d(TAG, "Unable to find record (" + whereClause + ") in " + TABLE_NAME);
			return null;
		} else {
			// Should only be 1, but we return first just to be sure
	    	Log.d(TAG, "Found " + list.length + " record(s) (" + whereClause + ") in " + TABLE_NAME);
			return list;
		}
	}

	protected IModel[] getAll() {
		IModel[] list = null;
		
		try {
			open();
			list = cursorToArray(this.mDB.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, this.mOrderBy));
			close();
		} catch (Exception ex) {
			close();
			Log.d(TAG, "Get failed all for " + TABLE_NAME, ex);
		}
		
		return list;
	}

	protected IModel[] cursorToArray(Cursor cursor) {
	    List<IModel> list = new ArrayList<IModel>();

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	IModel rec = cursorToModel(cursor);
	        list.add(rec);
	        cursor.moveToNext();
	    }
	    
	    // Make sure to close the cursor
	    cursor.close();
	    return list.toArray(new IModel[list.size()]);
	}

	protected static void UpgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion) throws Exception {
		throw new Exception("Not implemented");
	}
	
	protected abstract String getTableName();
	protected abstract String[] getColumns();
	protected abstract ContentValues getContentValues(IModel model);
	protected abstract IModel cursorToModel(Cursor cursor);

}
