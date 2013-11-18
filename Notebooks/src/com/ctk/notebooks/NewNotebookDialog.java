package com.ctk.notebooks;

import com.ctk.notebooks.Utils.ColorPickerSwatch;
import com.ctk.notebooks.Utils.ColorPickerSwatch.OnColorSelectedListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class NewNotebookDialog extends DialogFragment {
		
	final int NUM_SWATCHES = 12;

	Context				mContext;
	LayoutInflater 		mInflator;
	String 				mDialogTitle;
	OnClickListener		mPosClick, mNegClick;
	String 				mPosTitle, mNegTitle;
	View 				mLayout;
	TextView 			mTvNotebookName;
	ColorPickerSwatch 	mSwatches[] = new ColorPickerSwatch[NUM_SWATCHES];
	int					mNotebookColor;
	
	public NewNotebookDialog() {}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mLayout)
        	   .setTitle(mDialogTitle)
               .setPositiveButton(mPosTitle, mPosClick)
               .setNegativeButton(mNegTitle, mNegClick);
        return builder.create();
    }
	
	public class Builder {
		
		NewNotebookDialog dialog;
		
		private int mSwatchColors[] = { 0xff33b5e5, 0xffaa66cc, 0xffff4444, 0xffffbb33, 0xff99cc00, 
									    0xff0099cc, 0xff9933cc, 0xffcc0000, 0xffff8800, 0xff669900,
									    0xffdddddd, 0xff888888 };
		
		private int mSwatchIds[] 	= { R.id.swatch_1, R.id.swatch_2, R.id.swatch_3, R.id.swatch_4, R.id.swatch_5,
										R.id.swatch_6, R.id.swatch_7, R.id.swatch_8, R.id.swatch_9, R.id.swatch_10,
										R.id.swatch_11, R.id.swatch_12 };
		
		public Builder(Context context) {
			dialog = new NewNotebookDialog();
			dialog.mContext = context;	
			dialog.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog.mLayout = dialog.mInflator.inflate(R.layout.dialog_new_notebook, null);
			
			initSwatches();
	        dialog.mTvNotebookName = (TextView) dialog.mLayout.findViewById(R.id.new_notebook_name);
		}
		
		private void initSwatches() {
			for (int i = 0; i < NUM_SWATCHES; i++) {
				final int temp = i;
				dialog.mSwatches[i] = (ColorPickerSwatch) dialog.mLayout.findViewById(mSwatchIds[i]);
				dialog.mSwatches[i].setColor(mSwatchColors[i]);
				dialog.mSwatches[i].setOnColorSelectedListener(new OnColorSelectedListener() {
					
					@Override
					public void onColorSelected(int color) {
						mNotebookColor = color;
						
						dialog.mSwatches[temp].setChecked(true);
						for (int j = 0; j < NUM_SWATCHES; j++) {
							if (j != temp)
								dialog.mSwatches[j].setChecked(false);
						}
					}
				});
			}
		}
		
		public Builder setColorSwatches(int[] colors) {
			mSwatchColors = colors;
			return this;
		}
		
		public Builder setTitle(String new_title) {
			dialog.mDialogTitle = new_title;
			return this;
		}
		
		public Builder setPositiveButton(String name, DialogInterface.OnClickListener clickListener) {
			dialog.mPosClick = clickListener;
			dialog.mPosTitle = name;
			return this;
		}
		
		public Builder setNegativeButton(String name, DialogInterface.OnClickListener clickListener) {
			dialog.mNegClick = clickListener;
			dialog.mNegTitle = name;
			return this;
		}
		
		public void show(FragmentManager manager, String tag) {
			dialog.show(manager, tag);
		}
		
		public String getNotebookName() {
			return dialog.mTvNotebookName.getText().toString();
		}
		
		public int getNotebookColor() {
			return mNotebookColor;
		}
	}
}