package com.ctk.notebooks;

import java.util.Calendar;

import android.R.color;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_BINDER 	= "binder";
	public static final String COLUMN_ID 		= "_id";
	public static final String COLUMN_TITLE 	= "notebook_title";
	public static final String COLUMN_CREATED 	= "date_created";
	public static final String COLUMN_MODIFIED 	= "date_modified";
	public static final String COLUMN_COLOR 	= "notebook_color";

	  private static final String DATABASE_NAME = "bbinder.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table " + TABLE_BINDER + "(" +
			  COLUMN_ID + " integer primary key autoincrement, " +
			  COLUMN_TITLE + " text not null, " +
			  COLUMN_CREATED + " integer, " +
			  COLUMN_MODIFIED + " integer, " +
			  COLUMN_COLOR + " integer);";

	  public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }
	  
	  public boolean addNotebook(String name, int colorHex) {
		  
		  int timestamp = Calendar.getInstance().get(Calendar.MILLISECOND);
		  
		  String sql = "insert into " + TABLE_BINDER + 
				  " (" + COLUMN_TITLE + "," + COLUMN_CREATED + "," + COLUMN_MODIFIED + "," + COLUMN_COLOR + ")" + 
				  " values ('" + name + "', " + timestamp + ", " + timestamp + ", " + colorHex + ");";
		  
		  getWritableDatabase().execSQL(sql);
		  
		  
		  return true;
	  }
	  
	  public boolean addNote(String name, String NotebookName){
	  
		  
		  return true;
	  }
	  
	 
	  @Override
	  public void onCreate(SQLiteDatabase database) {
		  database.execSQL(DATABASE_CREATE);		  
		  addNotebook("Random notes", color.background_dark);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
		    "Upgrading database from version " + oldVersion + " to "
		        + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BINDER);
		onCreate(db);
	  }
	  
	  
}
