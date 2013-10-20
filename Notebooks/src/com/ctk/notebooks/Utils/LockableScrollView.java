package com.ctk.notebooks.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView {
	
	private boolean mIsScrollLocked = false;

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
	 * @param isScrollLocked	Pass <code>true</code> if scrolling should be locked,
	 * 							<code>false</code> otherwise.
	 */
    public void setScrollingLocked(boolean isScrollLocked) {
        mIsScrollLocked = isScrollLocked;
    }

    /**
     * @return	<code>true</code> if the <code>LockableScrollView</code> is scrollable,
     * 			<code>false</code> otherwise.
     */
    public boolean isScrollLocked() {
        return mIsScrollLocked;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
		// Checks if scrolling is enabled before passing the touch event
		// up to the superclass; if scrolling is enabled, allow the superclass
		// to enact its scroll method, if not return false.
	    if (!mIsScrollLocked) 
	    	return super.onTouchEvent(event);
	    else
	    	return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mIsScrollLocked) 
        	return super.onInterceptTouchEvent(event);
        else 
        	return false;
    }
}
