package com.ctk.notebooks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.ctk.notebooks.Utils.LockableScrollView;

public class NoteActivity extends Activity {

	private final static String BBINDERDIRECTORY = Environment.getExternalStorageDirectory() + "/bBinder";
	
	private ActionBar 			mActionBar;
	private NoteView 			mNoteView;
	private LockableScrollView	mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_note);
		mNoteView = (NoteView) findViewById(R.id.note_view);
		mScrollView = (LockableScrollView) findViewById(R.id.note_scroll_view);
		
		if (getIntent().hasExtra("is_open_note") && getIntent().getExtras().getBoolean("is_open_note", false)) {
			String filename = getIntent().getExtras().getString("filename");
			mNoteView.setBitmap(openFile(filename));
		} 
		
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		mNoteView.setPaintColor(0xFFfab41d);
		mNoteView.setPaintWidth(16);
	}
	
	/**
	 * Called by the OS when a touch is registered. A MotionEvent is
	 * passed by the OS, from which it can be determined the type of
	 * action [ACTION_DOWN, ACTION_MOVE, ACTION_UP, among others].
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (event.getPointerCount() == 1) {
			mScrollView.setScrollingLocked(true);
			mScrollView.requestDisallowInterceptTouchEvent(true);
			mNoteView.setDrawingLocked(false);
			mNoteView.onTouchEvent(event);
		} else if (event.getPointerCount() == 2) {
			mNoteView.setDrawingLocked(true);
			mScrollView.setScrollingLocked(false);
			mScrollView.requestDisallowInterceptTouchEvent(false);
			mScrollView.onTouchEvent(event);
		}
		
		return true;
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
	    	if (!saveFile(mNoteView.getFileName())){
	    		Toast.makeText(getApplicationContext(), "nope", Toast.LENGTH_SHORT).show();
	    	}
	    	
	    	// On the ActionBar Up button pressed, allow the OS
	    	// to return us to this Activity's parent.
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	        
	    case 1234567:
	    	if (mScrollView.isScrollLocked()) {
	    		mScrollView.setScrollingLocked(true);
	    		mNoteView.setDrawingLocked(false);
	    	}
	    	else {
	    		mScrollView.setScrollingLocked(false);
	    		mNoteView.setDrawingLocked(true);
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
	public boolean saveFile(String fileName) {
		try {
			File bBinderDirectory = new File(BBINDERDIRECTORY);
			bBinderDirectory.mkdir();
			
			FileOutputStream stream = new FileOutputStream(new File(bBinderDirectory, "/" + fileName + ".png"));
			mNoteView.getBitmap().compress(CompressFormat.PNG, 80, stream);
			stream.close();
			
			return true;
		} catch(IOException e) {
        	Log.e("ckt", "Save file error");
            e.printStackTrace();
            return false;
		}
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
}