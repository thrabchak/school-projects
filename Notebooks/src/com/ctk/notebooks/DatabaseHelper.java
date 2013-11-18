package com.ctk.notebooks;

import java.util.ArrayList;

import com.ctk.notebooks.Utils.Notebook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_NOTEBOOKS 	= "notebooks";
	public static final String COLUMN_ID 		= "_id";
	public static final String COLUMN_TITLE 	= "notebook_title";
	public static final String COLUMN_CREATED 	= "date_created";
	public static final String COLUMN_MODIFIED 	= "date_modified";
	public static final String COLUMN_COLOR 	= "notebook_color";

	private static final String DATABASE_NAME = "bbinder.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String CREATE_TABLE_BINDER = "create table " + TABLE_NOTEBOOKS + "(" +
				COLUMN_ID + " integer primary key autoincrement, " +
				COLUMN_TITLE + " text not null, " +
				COLUMN_CREATED + " integer, " +
				COLUMN_MODIFIED + " integer, " +
				COLUMN_COLOR + " integer);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_BINDER);
	}
	  
	public boolean addNotebook(String name, int colorHex) {
		long timestamp = System.currentTimeMillis() / 1000L;
	  
		String sql = "insert into " + TABLE_NOTEBOOKS + 
				" (" + COLUMN_TITLE + "," + COLUMN_CREATED + "," + COLUMN_MODIFIED + "," + COLUMN_COLOR + ")" + 
				" values ('" + name + "', " + timestamp + ", " + timestamp + ", " + colorHex + ");";
		
		try {
			getWritableDatabase().execSQL(sql);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public boolean deleteNotebook(int notebookId) {
		String sql = "delete from " + TABLE_NOTEBOOKS + " where " + COLUMN_ID + "=" + notebookId;
		
		try {
			getWritableDatabase().execSQL(sql);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public ArrayList<Notebook> getNotebooks() {
		ArrayList<Notebook> notebooks = new ArrayList<Notebook>();
		
		Cursor result = getReadableDatabase().query(TABLE_NOTEBOOKS, null, null, null, null, null, null);
		
		if (result.moveToFirst()) {
			do {
				notebooks.add(new Notebook(result.getString(result.getColumnIndex(COLUMN_TITLE)), 
										   result.getInt(result.getColumnIndex(COLUMN_ID)), 
										   result.getInt(result.getColumnIndex(COLUMN_CREATED)), 
										   result.getInt(result.getColumnIndex(COLUMN_MODIFIED)), 
										   result.getInt(result.getColumnIndex(COLUMN_COLOR))));
			} while (result.moveToNext());
		}
		
		return notebooks;
	}
	
	public int getPagesInNotebook(int notebookId) {
		//getReadableDatabase().rawQuery("select sum(, selectionArgs)
		return 0;
	}
	  
	public boolean addNote(String name, String notebookName) {
		return true;
	}
  
	public int getNumNotebooks() {
		Cursor result = null;
		int count = -1;
		
		try {
			result = getReadableDatabase().rawQuery("select count(*) from " + TABLE_NOTEBOOKS, null);
			count = result.getCount();
		} finally {
			result.close();
		}
		
		return count;
	}
 
	public void closeDB() {
		SQLiteDatabase db = getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), 
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTEBOOKS);
		onCreate(db);
	}
}