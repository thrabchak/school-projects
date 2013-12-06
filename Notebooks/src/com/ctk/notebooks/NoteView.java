package com.ctk.notebooks;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Class extending from View that contains a Canvas on which the user can draw.
 * 
 */
public class NoteView extends View {

	private LayerDrawable		mLayers;
	private Drawable			myImage;
	private Canvas				mCanvas;
	private float				mX, mY;
	private Path				mPath;
	private Bitmap				mBitmap;
	private Paint				mBitmapPaint;
	private Paint				mPaint;
	private int					mPaintColor			= 0xFF000000;
	private float				mPaintWidth			= 0;
	private final String		fileName			= "test1";
	private boolean				mIsLinedPaper		= false;
	private final Drawable[]	layers				= new Drawable[2];
	private Drawable			drawable			= null;
	private boolean				isEraserSelected	= false;

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
		mBitmap = null;
		if (mIsLinedPaper) {
			createLinedBackground();
		} else {
			createBlankBackground();
		}
		mPaint = new Paint();
		mPaint.setColor(mPaintColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(mPaintWidth);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
	}

	public void createBlankBackground() {
		Resources res = getContext().getResources();
		myImage = res.getDrawable(R.drawable.blank);
		layers[0] = myImage;
		if (mBitmap == null) {
			mBitmap = Bitmap.createBitmap(44, 55, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			layers[1] = new BitmapDrawable(getResources(), mBitmap);
			mLayers = new LayerDrawable(layers);
			drawable = mLayers.mutate();
			mBitmap = Bitmap.createBitmap(mLayers.getIntrinsicWidth(),
					mLayers.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
			drawable.setBounds(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
			drawable.draw(mCanvas);
		} else {
			layers[1] = new BitmapDrawable(getResources(), mBitmap);
			mLayers = new LayerDrawable(layers);
			layers[1].draw(mCanvas);
			drawable = mLayers.mutate();
			drawable.setBounds(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
			drawable.draw(mCanvas);

		}
	}

	public void createLinedBackground() {
		Resources res = getContext().getResources();
		myImage = res.getDrawable(R.drawable.lineback);
		layers[0] = myImage;
		if (mBitmap == null) {
			mBitmap = Bitmap.createBitmap(44, 55, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			layers[1] = new BitmapDrawable(getResources(), mBitmap);
			mLayers = new LayerDrawable(layers);
			drawable = mLayers.mutate();
			mBitmap = Bitmap.createBitmap(mLayers.getIntrinsicWidth(),
					mLayers.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
			drawable.setBounds(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
			drawable.draw(mCanvas);
		} else {
			layers[1] = new BitmapDrawable(getResources(), mBitmap);
			layers[1].setAlpha(0);
			mLayers = new LayerDrawable(layers);
			layers[0].draw(mCanvas);
			drawable = mLayers.mutate();
			drawable.setBounds(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
			drawable.draw(mCanvas);
		}

	}

	public void setEraser(boolean isSelected) {
		this.isEraserSelected = isSelected;
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
		if (mIsLinedPaper) {
			createLinedBackground();
		} else {
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
		float x, y;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			y = event.getY();
			mCanvas.drawPath(mPath, mPaint);
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
			invalidate();
			return true;
		case MotionEvent.ACTION_MOVE:
			x = event.getX();
			y = event.getY();
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
