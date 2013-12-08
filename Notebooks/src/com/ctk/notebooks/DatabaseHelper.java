package com.ctk.notebooks;

import java.io.File;
import java.util.ArrayList;

import com.ctk.notebooks.Utils.Note;
import com.ctk.notebooks.Utils.Notebook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

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
	
	private final static String BBINDERDIRECTORY = Environment.getExternalStorageDirectory() + "/bBinder";

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

	public boolean deleteNotebook(int notebookId) {
		String sqlDeleteNotebook = "delete from " + TABLE_NOTEBOOKS + " where " + NOTEBOOKS_COLUMN_ID + "=" + notebookId;
		
		String sqlSelectNotes = "select " + NOTES_COLUMN_FILEPATH + " from " + TABLE_NOTES + " where " + NOTES_COLUMN_NOTEBOOK_ID + "=?";
		Cursor notesToDelete = getReadableDatabase().rawQuery(sqlSelectNotes, new String[]{""+notebookId});
		String sqlDeleteNotes = "delete from " + TABLE_NOTES + " where " + NOTES_COLUMN_NOTEBOOK_ID + "=" + notebookId;
		
		File fileToDelete = null;
		String filename;
		if (notesToDelete.moveToFirst()) {
			do {
				filename = notesToDelete.getString(notesToDelete.getColumnIndex(NOTES_COLUMN_FILEPATH));
				fileToDelete = new File(BBINDERDIRECTORY + "/" + filename + ".png"); 
				if (!fileToDelete.delete())
					Log.d("onDeleteNote", "Note " + filename + ".png failed to delete");
			} while (notesToDelete.moveToNext());
		}
		
		try {
			getWritableDatabase().execSQL(sqlDeleteNotebook);
			getWritableDatabase().execSQL(sqlDeleteNotes);
		} catch (Exception e) {
			return false;
		}
		
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

	public boolean addNote(String name, String filepath, int notebookId, int pageNumber) {
		long timestamp = System.currentTimeMillis() / 1000L;
		
		String sqlInsertNote = "insert into " + TABLE_NOTES + 
				" (" + NOTES_COLUMN_NOTEBOOK_ID + "," + 
					   NOTES_COLUMN_TITLE + "," + 
					   NOTES_COLUMN_CREATED + "," +
					   NOTES_COLUMN_MODIFIED + "," +
					   NOTES_COLUMN_FILEPATH + "," +
					   NOTES_COLUMN_PAGE_NUMBER + ")" + 
				" values (" + notebookId + ", '" + name + "', " + timestamp + ", " + timestamp + ", '" + filepath + "', " + pageNumber + ");";
		
		String sqlUpdateNotebook = "update " + TABLE_NOTEBOOKS + 
								   " set " + NOTEBOOKS_COLUMN_NUM_PAGES + "=" + pageNumber + ", " + NOTEBOOKS_COLUMN_MODIFIED + "=" + timestamp +
								   " where " + NOTEBOOKS_COLUMN_ID + "=" + notebookId;
		
		try {
			getWritableDatabase().execSQL(sqlInsertNote);
			getWritableDatabase().execSQL(sqlUpdateNotebook);
		} catch (Exception e) {
			Log.e("bBinder", e.toString());
			return false;
		}
		
		return true;
	}
	  
	public boolean addNote(String filepath, int notebookId, int pageNumber) {
		addNote("Page " + pageNumber, filepath, notebookId, pageNumber);
		return true;
	}
	
	public boolean doesNoteExist(int notebookId, int pageNumber) {
		String sql = "select count(*) from " + TABLE_NOTES + " where " + NOTES_COLUMN_PAGE_NUMBER + "=? and " +NOTES_COLUMN_NOTEBOOK_ID + "=?";
		Cursor result = getReadableDatabase().rawQuery(sql, new String[]{"" + pageNumber, "" + notebookId});
		result.moveToFirst();
		
		return result.getInt(0) == 1;
	}
	
	public boolean updateNote(int notebookId, int pageNumber) {
		long timestamp = System.currentTimeMillis() / 1000L;
		
		String sqlUpdateNote = "update " + TABLE_NOTES +
							   " set " + NOTES_COLUMN_MODIFIED + "=" + timestamp +
							   " where " + NOTES_COLUMN_NOTEBOOK_ID + "=" + notebookId + " and " + NOTES_COLUMN_PAGE_NUMBER + "=" + pageNumber;
		
		String sqlUpdateNotebook = "update " + TABLE_NOTEBOOKS + 
				   " set " + NOTEBOOKS_COLUMN_MODIFIED + "=" + timestamp +
				   " where " + NOTEBOOKS_COLUMN_ID + "=" + notebookId;

		try {
			getWritableDatabase().execSQL(sqlUpdateNote);
			getWritableDatabase().execSQL(sqlUpdateNotebook);
		} catch (Exception e) {
			Log.e("bBinder", e.toString());
			return false;
		}
		
		return true;
	}
	
	public boolean deleteNote(int notebookId, int pageNumber) {
		long timestamp = System.currentTimeMillis() / 1000L;
		String sqlSelectNotes = "select " + NOTES_COLUMN_FILEPATH + " from " + TABLE_NOTES + " where " + NOTES_COLUMN_NOTEBOOK_ID + "=? and " + NOTES_COLUMN_PAGE_NUMBER + "=?";
		Cursor notesToDelete = getReadableDatabase().rawQuery(sqlSelectNotes, new String[]{""+notebookId, ""+pageNumber});
		
		File fileToDelete = null;
		String filename;
		if (notesToDelete.moveToFirst()) {
			filename = notesToDelete.getString(notesToDelete.getColumnIndex(NOTES_COLUMN_FILEPATH));
			fileToDelete = new File(BBINDERDIRECTORY + "/" + filename + ".png"); 
			if (!fileToDelete.delete()) {
				Log.d("onDeleteNote", "Note " + filename + ".png failed to delete");
				return false;
			}
		}
		
		String sqlDeleteNote = "delete from " + TABLE_NOTES + " where " + NOTES_COLUMN_NOTEBOOK_ID + "=" + notebookId + " and " + NOTES_COLUMN_PAGE_NUMBER + "=" + pageNumber;
		String sqlUpdateNotebookPageCount = "update " + TABLE_NOTEBOOKS +
										    " set " + NOTEBOOKS_COLUMN_MODIFIED + "=" + timestamp + ", " + NOTEBOOKS_COLUMN_NUM_PAGES + "=" + NOTEBOOKS_COLUMN_NUM_PAGES + "- 1" + 
										    " where " + NOTEBOOKS_COLUMN_ID + "=" + notebookId;
		String sqlUpdatePageNumbers = "update " + TABLE_NOTES +
									  " set " + NOTES_COLUMN_PAGE_NUMBER + "=" + NOTES_COLUMN_PAGE_NUMBER + "- 1 " +
									  " where " + NOTES_COLUMN_NOTEBOOK_ID + "=" + notebookId + " and " + NOTES_COLUMN_PAGE_NUMBER + ">" + pageNumber;
	
		try {
			getWritableDatabase().execSQL(sqlDeleteNote);
			getWritableDatabase().execSQL(sqlUpdateNotebookPageCount);
			getWritableDatabase().execSQL(sqlUpdatePageNumbers);
		} catch (Exception e) {
			Log.e("bBinder", e.toString());
			return false;
		}
		
		return true;
	}
	
	public ArrayList<Note> getNotes(int notebookId) {
		ArrayList<Note> notes = new ArrayList<Note>();
		
		String sqlGetNotes = "select * from " + TABLE_NOTES + " where " + NOTES_COLUMN_NOTEBOOK_ID + "=?";
		Cursor result = getReadableDatabase().rawQuery(sqlGetNotes, new String[]{""+notebookId});
		
		if (result.moveToFirst()) {
			do {
				notes.add(new Note(result.getString(result.getColumnIndex(NOTES_COLUMN_TITLE)), 
								   result.getInt(result.getColumnIndex(NOTES_COLUMN_NOTEBOOK_ID)), 
								   result.getInt(result.getColumnIndex(NOTES_COLUMN_PAGE_NUMBER)), 
								   result.getLong(result.getColumnIndex(NOTES_COLUMN_CREATED)), 
								   result.getLong(result.getColumnIndex(NOTES_COLUMN_MODIFIED)), 
								   result.getString(result.getColumnIndex(NOTES_COLUMN_FILEPATH))));
			} while (result.moveToNext());
		}
		
		return notes;
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