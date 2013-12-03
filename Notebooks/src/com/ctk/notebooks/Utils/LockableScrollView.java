package com.ctk.notebooks.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView {

	private boolean	mIsScrollLocked	= false;

	public LockableScrollView(Context context) {
		super(context);
	}

	public LockableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LockableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Toggle whether the <code>LockableScrollView</code> can be scrolled.
	 * 
	 * @param isScrollLocked
	 *            Pass <code>true</code> if scrolling should be locked,
	 *            <code>false</code> otherwise.
	 */
	public void setScrollingLocked(boolean isScrollLocked) {
		mIsScrollLocked = isScrollLocked;
	}

	/**
	 * @return <code>true</code> if the <code>LockableScrollView</code> is
	 *         scrollable, <code>false</code> otherwise.
	 */
	public boolean isScrollLocked() {
		return mIsScrollLocked;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mY == -1)
			mY = event.getAxisValue(MotionEvent.AXIS_Y);

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_MOVE:
			float new_y = event.getAxisValue(MotionEvent.AXIS_Y);
			// Log.d("ckt", "old: " + mY + " new: " + new_y + " scrollBy: "
			// + (int) (new_y - mY));
			smoothScrollBy(0, (int) (mY - new_y));
			mY = event.getAxisValue(MotionEvent.AXIS_Y);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			twoFingersDown = false;
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_POINTER_DOWN:
			twoFingersDown = true;
			return true;
		case MotionEvent.ACTION_MOVE:
			if (twoFingersDown)
				return true;
			else
				return false;
		case MotionEvent.ACTION_POINTER_UP:
			twoFingersDown = false;
			mY = -1;
			return true;
		default:
			return false;
		}
	}

	private float	mY				= -1;
	private boolean	twoFingersDown	= false;
}
