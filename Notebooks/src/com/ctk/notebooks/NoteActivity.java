package com.ctk.notebooks;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class NoteActivity extends Activity {

	private ActionBar mActionBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new NoteView(this));
		
		mActionBar = getActionBar();
		mActionBar.setTitle("New Note");
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	
	public class NoteView extends View {

		private Paint	mPaint;
		private Canvas	mCanvas;
		private float 	mX, mY;
		private Path	mPath;
		private Bitmap 	mBitmap;
		
		public NoteView(Context context) {
			super(context);
			mBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			
			mPaint = new Paint();
			mPaint.setColor(0xFF000000);
			mPaint.setStrokeWidth(0.5f);
			
			mPath = new Path();
			setBackgroundColor(0xFFfab41d);
			
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mX = event.getX();
				mY = event.getY();
				
				mPath.reset();
				mPath.moveTo(mX, mY);
				mCanvas.drawPath(mPath, mPaint);
				
				return true;
			case MotionEvent.ACTION_MOVE:
				mX = event.getX();
				mY = event.getY();
				
				mPath.moveTo(mX, mY);
				mCanvas.drawPath(mPath, mPaint);
			case MotionEvent.ACTION_UP:
				mX = event.getX();
				mY = event.getY();
				
				mPath.moveTo(mX, mY);
				mCanvas.drawPath(mPath, mPaint);
				mPath.close();
			}
			
			return false;
		}
		
	}
	
}
