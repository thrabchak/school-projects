package com.ctk.notebooks.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Toast;

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

    float x;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

    	if( event.getPointerCount() > 1){
        	Toast.makeText(getContext(), "scroll loc = " + getScrollY(), Toast.LENGTH_SHORT).show();
    		return true;
    	} else if (!mIsScrollLocked) 
        	return super.onInterceptTouchEvent(event);
        else 
        	return false;
		
    }
}
