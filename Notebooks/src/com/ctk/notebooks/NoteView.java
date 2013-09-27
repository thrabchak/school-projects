package com.ctk.notebooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Class extending from View that contains a Canvas on which
 * the user can draw.
 *
 */
public class NoteView extends View {

	private Canvas	mCanvas;
	private float 	mX, mY;
	private Path	mPath;
	private Bitmap 	mBitmap;
	private Paint   mBitmapPaint;
	private Paint	mPaint;
	
	private int		mPaintColor = 0xFF000000;
	private float	mPaintWidth	= 10;
	private String 	fileName="test1";
	

	public NoteView(Context context) {
		super(context);
		
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPath = new Path();
		
		// Create new Paint object, here we will be able
		// to change the draw color and width.
		mPaint = new Paint();
		mPaint.setColor(mPaintColor);
		mPaint.setStrokeWidth(mPaintWidth);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
	}
	
	/**
	 * Sets the color of the paintbrush.
	 * @param color	An integer in the form 0xaarrggbb, where <code>aa</code>
	 * 				is the hex value for the color transparency, and
	 * 				<code>rr</code>, <code>gg</code>, and <code>bb</code> are
	 * 				the hex values respectively for red, green, and blue.
	 * @return The current NoteView, to allow for method chaining.
	 */
	public NoteView setPaintColor(int color) {
		mPaintColor = color;
		mPaint.setColor(mPaintColor);
		return this;
	}
	
	/**
	 * Sets the width of the paintbrush.
	 * @param width	A float for the width of the paintbrush (set to 10 by default).
	 * @return The current NoteView, to allow for method chaining.
	 */
	public NoteView setPaintWidth(float width) {
		mPaintWidth = width;
		mPaint.setStrokeWidth(mPaintWidth);
		return this;
	}

	/**
	 * Called by the OS when the Canvas size is either
	 * first set or changed.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}
	
	/**
	 * Called by the OS to draw this View to the screen. It is here
	 * where any drawing must be done. This method can be triggered
	 * by calling <code>invalidate()</code>.
	 */
	@Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFFFFFFF);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

	/**
	 * Called by the OS when a touch is registered. A MotionEvent is
	 * passed by the OS, from which it can be determined the type of
	 * action [ACTION_DOWN, ACTION_MOVE, ACTION_UP, among others].
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
			invalidate();
			return true;
		case MotionEvent.ACTION_MOVE:
			float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= 4 || dy >= 4) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
            invalidate();
            return true;
		case MotionEvent.ACTION_UP:
			mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
            invalidate();
            return true;
		}
		
		return false;
	}
	
	/**
	 * @return	The underlying <code>Bitmap</code> image of the NoteView's <code>Canvas</code>.
	 */
	public Bitmap getBitmap(){
		return mBitmap;
	}
	
	/**
	 * @return	The filename given to the current sheet of notes.
	 */
	public String getFileName() {
		return fileName;
	}
}