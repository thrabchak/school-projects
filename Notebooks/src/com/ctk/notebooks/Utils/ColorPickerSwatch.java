package com.ctk.notebooks.Utils;

import com.ctk.notebooks.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;

/**
 * Creates a circular swatch of a specified color.  Adds a checkmark if marked as checked.
 */
public class ColorPickerSwatch extends FrameLayout implements View.OnClickListener {
    private int mColor = 0xfab41d;
    private ImageView mSwatchImage;
    private ImageView mCheckmarkImage;
    private OnColorSelectedListener mOnColorSelectedListener = null;

    /**
     * Interface for a callback when a color square is selected.
     */
    public interface OnColorSelectedListener {
    	public void onColorSelected(int color);
    }

    public ColorPickerSwatch(Context context, int color, boolean checked,
            OnColorSelectedListener listener) {
        super(context);
        
        init();
        mColor = color;
        mOnColorSelectedListener = listener;
    }
    
    public ColorPickerSwatch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ColorPickerSwatch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ColorPickerSwatch(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflator.inflate(R.layout.color_swatch, this);
        mSwatchImage = (ImageView) layout.findViewById(R.id.color_picker_swatch);
        mCheckmarkImage = (ImageView) layout.findViewById(R.id.color_picker_checkmark);
        setColor(mColor);
        setChecked(false);
        setOnClickListener(this);
        
	}
	
	public ColorPickerSwatch setColor(int color) {
		Drawable[] colorDrawable = new Drawable[]
                {getContext().getResources().getDrawable(R.drawable.color_swatch)};
        mSwatchImage.setImageDrawable(new ColorStateDrawable(colorDrawable, color));
        mColor = color;
        return this;
    }
	
	public ColorPickerSwatch setOnColorSelectedListener(OnColorSelectedListener listener) {
		mOnColorSelectedListener = listener;
		return this;
	}

    public void setChecked(boolean checked) {
        if (checked) {
            mCheckmarkImage.setVisibility(View.VISIBLE);
        } else {
            mCheckmarkImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnColorSelectedListener != null) {
            mOnColorSelectedListener.onColorSelected(mColor);
        }
    }

    /**
     * A drawable which sets its color filter to a color specified by the user, and changes to a
     * slightly darker color when pressed or focused.
     */
    public class ColorStateDrawable extends LayerDrawable {

        private static final float PRESSED_STATE_MULTIPLIER = 0.70f;

        private int mColor;

        public ColorStateDrawable(Drawable[] layers, int color) {
            super(layers);
            mColor = color;
        }

        @Override
        protected boolean onStateChange(int[] states) {
            boolean pressedOrFocused = false;
            for (int state : states) {
                if (state == android.R.attr.state_pressed || state == android.R.attr.state_focused) {
                    pressedOrFocused = true;
                    break;
                }
            }

            if (pressedOrFocused) {
                super.setColorFilter(getPressedColor(mColor), PorterDuff.Mode.SRC_ATOP);
            } else {
                super.setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
            }

            return super.onStateChange(states);
        }

        /**
         * Given a particular color, adjusts its value by a multiplier.
         */
        private int getPressedColor(int color) {
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] = hsv[2] * PRESSED_STATE_MULTIPLIER;
            return Color.HSVToColor(hsv);
        }

        @Override
        public boolean isStateful() {
            return true;
        }
    }
}