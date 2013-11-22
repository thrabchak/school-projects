package com.ctk.notebooks;

import java.util.ArrayList;

import com.ctk.notebooks.Utils.Notebook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_NOTEBOOKS 				= "notebooks";
	public static final String NOTEBOOKS_COLUMN_ID 			= "_id";
	public static final String NOTEBOOKS_COLUMN_TITLE 		= "notebook_title";
	public static final String NOTEBOOKS_COLUMN_CREATED 	= "date_created";
	public static final String NOTEBOOKS_COLUMN_MODIFIED 	= "date_modified";
	public static final String NOTEBOOKS_COLUMN_COLOR 		= "notebook_color";
	public static final String NOTEBOOKS_COLUMN_NUM_PAGES	= "notebook_num_pages";
	
	public static final String TABLE_NOTES 				= "notes";
	public static final String NOTES_COLUMN_NOTEBOOK_ID = "notebook_id";
	public static final String NOTES_COLUMN_TITLE 		= "note_title";
	public static final String NOTES_COLUMN_CREATED 	= "date_created";
	public static final String NOTES_COLUMN_MODIFIED 	= "date_modified";
	public static final String NOTES_COLUMN_FILEPATH 	= "filepath";
	public static final String NOTES_COLUMN_PAGE_NUMBER	= "page_number";

	private static final String DATABASE_NAME = "bbinder.db";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_TABLE_NOTEBOOKS = "create table " + TABLE_NOTEBOOKS + "(" +
				NOTEBOOKS_COLUMN_ID + " integer primary key autoincrement, " +
				NOTEBOOKS_COLUMN_TITLE + " text not null, " +
				NOTEBOOKS_COLUMN_CREATED + " integer, " +
				NOTEBOOKS_COLUMN_MODIFIED + " integer, " +
				NOTEBOOKS_COLUMN_COLOR + " integer, " + 
				NOTEBOOKS_COLUMN_NUM_PAGES + " integer);";
	
	private static final String CREATE_TABLE_NOTES = "create table " + TABLE_NOTES + "(" +
			NOTES_COLUMN_NOTEBOOK_ID + " integer, " +
			NOTES_COLUMN_TITLE + " text, " +
			NOTES_COLUMN_CREATED + " integer, " +
			NOTES_COLUMN_MODIFIED + " integer, " +
			NOTES_COLUMN_FILEPATH + " text not null, " + 
			NOTES_COLUMN_PAGE_NUMBER + " integer, " +
			"primary key(" + NOTES_COLUMN_NOTEBOOK_ID + ", " + NOTES_COLUMN_PAGE_NUMBER + "));";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_NOTEBOOKS);
		database.execSQL(CREATE_TABLE_NOTES);
	}
	  
	public boolean addNotebook(String name, int colorHex) {
		long timestamp = System.currentTimeMillis() / 1000L;
	  
		String sql = "insert into " + TABLE_NOTEBOOKS + 
				" (" + NOTEBOOKS_COLUMN_TITLE + "," + 
					   NOTEBOOKS_COLUMN_CREATED + "," + 
					   NOTEBOOKS_COLUMN_MODIFIED + "," +
					   NOTEBOOKS_COLUMN_COLOR + "," +
					   NOTEBOOKS_COLUMN_NUM_PAGES + ")" + 
				" values ('" + name + "', " + timestamp + ", " + timestamp + ", " + colorHex + ", " + 0 + ");";
		
		try {
			getWritableDatabase().execSQL(sql);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public boolean deleteNotebook(int notebookId) {
		String sqlDeleteNotebook = "delete from " + TABLE_NOTEBOOKS + " where " + NOTEBOOKS_COLUMN_ID + "=" + notebookId;
		
		String sqlSelectNotes = "select " + NOTES_COLUMN_FILEPATH + " from " + TABLE_NOTES + " where " + NOTES_COLUMN_NOTEBOOK_ID + "=?";
		Cursor notesToDelete = getReadableDatabase().rawQuery(sqlSelectNotes, new String[]{""+notebookId});
		String sqlDeleteNotes = "delete from " + TABLE_NOTES + " where " + NOTES_COLUMN_NOTEBOOK_ID + "=" + notebookId;
		
		// TODO: Use notesToDelete to iterate over all filenames to delete from filesystem.
		
		try {
			getWritableDatabase().execSQL(sqlDeleteNotebook);
			getWritableDatabase().execSQL(sqlDeleteNotes);
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
				notebooks.add(new Notebook(result.getString(result.getColumnIndex(NOTEBOOKS_COLUMN_TITLE)), 
										   result.getInt(result.getColumnIndex(NOTEBOOKS_COLUMN_ID)), 
										   result.getInt(result.getColumnIndex(NOTEBOOKS_COLUMN_CREATED)), 
										   result.getInt(result.getColumnIndex(NOTEBOOKS_COLUMN_MODIFIED)), 
										   result.getInt(result.getColumnIndex(NOTEBOOKS_COLUMN_COLOR)),
										   result.getInt(result.getColumnIndex(NOTEBOOKS_COLUMN_NUM_PAGES))));
			} while (result.moveToNext());
		}
		
		return notebooks;
	}
	
	public boolean addNote(String name, String filepath, int notebookId) {
		long timestamp = System.currentTimeMillis() / 1000L;
		
		String sql = "select " + NOTEBOOKS_COLUMN_NUM_PAGES + " from " + TABLE_NOTEBOOKS + " where " + NOTEBOOKS_COLUMN_ID + "=?";
		Cursor result = getReadableDatabase().rawQuery(sql, new String[]{""+notebookId});
		result.moveToFirst();
		int numPages = result.getInt(result.getColumnIndex(NOTEBOOKS_COLUMN_NUM_PAGES));
		
		String sqlInsertNote = "insert into " + TABLE_NOTES + 
				" (" + NOTES_COLUMN_NOTEBOOK_ID + "," + 
					   NOTES_COLUMN_TITLE + "," + 
					   NOTES_COLUMN_CREATED + "," +
					   NOTES_COLUMN_MODIFIED + "," +
					   NOTES_COLUMN_FILEPATH + "," +
					   NOTES_COLUMN_PAGE_NUMBER + ")" + 
				" values (" + notebookId + ", '" + name + "', " + timestamp + ", " + timestamp + ", '" + filepath + "', " + (numPages + 1) + ");";
		
		try {
			getWritableDatabase().execSQL(sqlInsertNote);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	  
	public boolean addNote(String filepath, int notebookId) {
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