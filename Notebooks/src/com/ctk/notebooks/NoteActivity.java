package com.ctk.notebooks;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class NoteActivity extends Activity {

	private ActionBar 	mActionBar;
	private Paint		mPaint;
	private NoteView 	mNoteView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNoteView = new NoteView(this);
		setContentView(mNoteView);
		
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		mNoteView.setPaintColor(0xFFfab41d);
		mNoteView.setPaintWidth(16);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	
	    	// On the ActionBar Up button pressed, allow the OS
	    	// to return us to this Activity's parent.
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	
	
}
