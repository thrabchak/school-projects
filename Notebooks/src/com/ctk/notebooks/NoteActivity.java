package com.ctk.notebooks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class NoteActivity extends Activity {

	private final static String BBINDERDIRECTORY = Environment.getExternalStorageDirectory() + "/bBinder";
	
	private ActionBar 	mActionBar;
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
	    	if (!saveCanvas(mNoteView.getFileName())){
	    		Toast.makeText(getApplicationContext(), "nope", Toast.LENGTH_SHORT).show();
	    	}
	    	
	    	// On the ActionBar Up button pressed, allow the OS
	    	// to return us to this Activity's parent.
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	public boolean saveCanvas(String fileName) {
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
	
	@Override
	protected void onStop() {
		saveCanvas(mNoteView.getFileName());
		super.onStop();
	}
}