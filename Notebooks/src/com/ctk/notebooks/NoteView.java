package com.ctk.notebooks;

import com.ctk.notebooks.Utils.LinedDrawable;
import com.ctk.notebooks.Utils.LockableScrollView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Class extending from View that contains a Canvas on which the user can draw.
 * 
 */
public class NoteView extends View {

	private LayerDrawable 	mLayers;
	private Drawable		myImage;
	private Canvas			mCanvas;
	private float			mX, mY;
	private Path			mPath;
	private Bitmap			mBitmap;
	private Paint			mBitmapPaint;
	private Paint			mPaint;
	private int				mPaintColor			= 0xFF000000;
	private float			mPaintWidth			= 0;
	private final String	fileName			= "test1";
	private boolean			mIsDrawingLocked	= false;
	private boolean 		mIsLinedPaper		= true;

	public NoteView(Context context) {
		super(context);
		init();
	}

	public NoteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NoteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Initializes member variables. Called in every constructor above.
	 */
	private void init() {
		
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPath = new Path();
		if(mIsLinedPaper){
			createLinedBackground();
		}
		// Create new Paint object, here we will be able
		// to change the draw color and width.
		mPaint = new Paint();
		mPaint.setColor(mPaintColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(mPaintWidth);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
	}
	
	public void createBlankBackground(){
		Toast.makeText(getContext(), "blank", Toast.LENGTH_SHORT).show();
		mBitmap = Bitmap.createScaledBitmap(mBitmap, mCanvas.getWidth(),mCanvas.getHeight(), false);
		mCanvas = new Canvas(mBitmap);
		mLayers=null;
		Drawable drawable =new BitmapDrawable (getResources(),mBitmap);
		drawable.setBounds(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
		drawable.draw(mCanvas);
	}
	
	public void createLinedBackground(){
		LinedDrawable d = new LinedDrawable();
		Canvas c = new Canvas();
		d.draw(c);
		Drawable q = (Drawable) d;
		Resources res = getContext().getResources();
		myImage = res.getDrawable(R.drawable.linedbackground);
		Drawable[] layers = new Drawable[2];
		layers[1]=myImage;
		mBitmap = Bitmap.createBitmap(44,75, Bitmap.Config.ARGB_8888);
		
		layers[0]=new BitmapDrawable (getResources(),mBitmap);
		
		mLayers = new LayerDrawable(layers);
		
		Drawable drawable = mLayers.mutate();
		mBitmap = Bitmap.createBitmap(mLayers.getIntrinsicWidth(),mLayers.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		drawable.setBounds(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
		drawable.draw(mCanvas);
		mLayers.draw(mCanvas);
		
	}

	/**
	 * Sets the color of the paintbrush.
	 * 
	 * @param color
	 *            An integer in the form 0xaarrggbb, where <code>aa</code> is
	 *            the hex value for the color transparency, and <code>rr</code>,
	 *            <code>gg</code>, and <code>bb</code> are the hex values
	 *            respectively for red, green, and blue.
	 * @return The current NoteView, to allow for method chaining.
	 */
	public NoteView setPaintColor(int color) {
		mPaintColor = color;
		mPaint.setColor(mPaintColor);
		return this;
	}

	/**
	 * Sets the width of the paintbrush.
	 * 
	 * @param width
	 *            A float for the width of the paintbrush (set to 10 by
	 *            default).
	 * @return The current NoteView, to allow for method chaining.
	 */
	public NoteView setPaintWidth(float width) {
		mPaintWidth = width;
		mPaint.setStrokeWidth(mPaintWidth);
		return this;
	}

	/**
	 * Sets whether drawing is currently enabled on the <code>NoteView</code>.
	 * 
	 * @param isLocked
	 *            <code>true</code> if the <code>NoteView</code> should be
	 *            locked, <code>false</code> if the <code>NoteView</code> should
	 *            be drawn to.
	 */
	public void setDrawingLocked(boolean isLocked) {
		mIsDrawingLocked = isLocked;
	}

	/**
	 * Called by the OS when the Canvas size is either first set or changed.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mBitmap == null) {
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
			mCanvas = new Canvas(mBitmap);
		} else {
			mBitmap = Bitmap.createScaledBitmap(mBitmap, w, h, false);
			mCanvas = new Canvas(mBitmap);
		}
	}

	/**
	 * Called by the OS to draw this View to the screen. It is here where any
	 * drawing must be done. This method can be triggered by calling
	 * <code>invalidate()</code>.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0xFFFFFFFF);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

		if (mBitmap == null)
			Toast.makeText(getContext(), "no", Toast.LENGTH_SHORT).show();

		canvas.drawPath(mPath, mPaint);
	}

	public void setmIsLinedPaper() {
		boolean b = !(mIsLinedPaper);
		this.mIsLinedPaper = b;
		if (mIsLinedPaper){
			createLinedBackground();
		}
		else{
			createBlankBackground();
		}
	}

	/**
	 * Called by the OS when a touch is registered. A MotionEvent is passed by
	 * the OS, from which it can be determined the type of action [ACTION_DOWN,
	 * ACTION_MOVE, ACTION_UP, among others].
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		if (!mIsDrawingLocked) {
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
		}
		return true;
	}

	/**
	 * @return The underlying <code>Bitmap</code> image of the NoteView's
	 *         <code>Canvas</code>.
	 */
	public Bitmap getBitmap() {
		return mBitmap;
	}

	/**
	 * @return The filename given to the current sheet of notes.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the <code>Bitmap</code> image of <code>Canvas</code> of the
	 * <code>NoteView</code>.
	 * 
	 * @param bitmap
	 *            The <code>Bitmap</code> that will become the background of the
	 *            <code>NoteView</code>
	 */
	public void setBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
	}
}