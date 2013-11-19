package com.ctk.notebooks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ctk.notebooks.Utils.LockableScrollView;

public class NoteActivity extends Activity {

	private final static String BBINDERDIRECTORY = Environment.getExternalStorageDirectory() + "/bBinder";
	
	private ActionBar 			mActionBar;
	private NoteView 			mNoteView;
	private LockableScrollView	mScrollView;
	private boolean				mIsInNotebook = false;
	private String				mNotebookName;
	private int					mNotebookId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_note);
		mNoteView = (NoteView) findViewById(R.id.note_view);
		mScrollView = (LockableScrollView) findViewById(R.id.note_scroll_view);
		
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		if (getIntent().hasExtra("is_open_note") && getIntent().getExtras().getBoolean("is_open_note", false)) {
			String filename = getIntent().getExtras().getString("filename");
			mNoteView.setBitmap(openFile(filename));
		}
		
		if (getIntent().hasExtra("notebook_name") && getIntent().hasExtra("notebook_id")) {
			mIsInNotebook = true;
			mNotebookName = getIntent().getExtras().getString("notebook_name", "NULL");
			mNotebookId = getIntent().getExtras().getInt("notebook_id", -1);
			mActionBar.setTitle("New note");
			mActionBar.setSubtitle("in " + mNotebookName);
		}
		
		mNoteView.setPaintColor(0xFFfab41d);
		mNoteView.setPaintWidth(16);
		
		mScrollView.setScrollingLocked(true);
		mNoteView.setDrawingLocked(false);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1234567, Menu.NONE, "Scroll");
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	saveFile(mNoteView.getFileName());
	    	
	    	// On the ActionBar Up button pressed, allow the OS
	    	// to return us to this Activity's parent.
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	        
	    case 1234567:
	    	if (mScrollView.isScrollLocked()) {
	    		mScrollView.setScrollingLocked(false);
	    		mNoteView.setDrawingLocked(true);
	    	}
	    	else {
	    		mScrollView.setScrollingLocked(true);
	    		mNoteView.setDrawingLocked(false);
	    	}
	    	return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	/**
	 * Save current Note as a png to the SD card.
	 * @param fileName	The name for which the file will be stored.
	 * @return	<code>true</code> if the file was saved without an error,
	 * 			<code>false</code> otherwise.
	 */
	public void saveFile(String fileName) {
		new SaveNote().execute(fileName);
	}
	
	/**
	 * Reads a saved Note as a png from the SD card.
	 * @param filename	The name of the file to read.
	 * @return	A <code>Bitmap</code> of the png.
	 */
	public Bitmap openFile(String filename) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inMutable = true;
		
		return BitmapFactory.decodeFile(BBINDERDIRECTORY + "/" + filename + ".png", opts);
	}
	
	@Override
	protected void onStop() {
		saveFile(mNoteView.getFileName());
		super.onStop();
	}
	
	public class SaveNote extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... strings) {
			try {
				File bBinderDirectory = new File(BBINDERDIRECTORY);
				bBinderDirectory.mkdir();
				
				FileOutputStream stream = new FileOutputStream(new File(bBinderDirectory, "/" + strings[0] + ".png"));
				mNoteView.getBitmap().compress(CompressFormat.PNG, 80, stream);
				stream.close();
			} catch(IOException e) {
	        	Log.e("ckt", "Save file error");
	            e.printStackTrace();
			}
			return null;
		}
		
	}
}