package com.ctk.notebooks.Utils;

import java.util.Calendar;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ctk.notebooks.DatabaseHelper;
import com.ctk.notebooks.R;
import com.ctk.notebooks.Utils.NotebookGridAdapter.OnNotebookActionClickListener;

public class NotebookCard {
	
	Context							mContext;
	Notebook 						mNotebook;
	View							mLayout;
	OnNotebookActionClickListener 	mActionListener;
	TextView						mTvNotebookTitle, mTvNotebookModified, mTvNotebookPages;
	View							mDeleteButton, mAddNoteButton;
	
	public NotebookCard(Context context, Notebook notebook, View cardLayoutRoot, OnNotebookActionClickListener actionListener) {
		mContext = context;
		mNotebook = notebook;
		mLayout = cardLayoutRoot;
		mActionListener = actionListener;
		
		bindViews();
		setColoring();
	}
	
	public View getCardView() {
		return mLayout;
	}
	
	private void bindViews() {
		mTvNotebookTitle = (TextView) mLayout.findViewById(R.id.tv_card_notebook_name);
		mTvNotebookTitle.setText(mNotebook.name);
		
		mTvNotebookModified = (TextView) mLayout.findViewById(R.id.tv_card_notebook_last_modified);
		setLastModified();
		
		mTvNotebookPages = (TextView) mLayout.findViewById(R.id.tv_card_notebook_number_of_pages);
		mTvNotebookPages.setText(mNotebook.numPages + " pages");
		
		mDeleteButton = mLayout.findViewById(R.id.card_notebook_delete);
		mDeleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mActionListener.onNotebookDeleteClick(mNotebook);
			}
		});
		
		mAddNoteButton = mLayout.findViewById(R.id.card_notebook_add_note);
		mAddNoteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mActionListener.onNotebookAddNoteClick(mNotebook);
			}
		});
	}
	
	private void setLastModified() {
		long nowtime = System.currentTimeMillis() / 1000L;
		long oldtime = mNotebook.lastModifiedTimestamp;
		String modified = "Last modified:\n";

		Calendar olddate = Calendar.getInstance();
    	olddate.setTimeInMillis((long)oldtime * 1000);

    	Calendar nowdate = Calendar.getInstance();
    	nowdate.setTimeInMillis(System.currentTimeMillis());

		if (nowtime > oldtime + 604800) {
    		modified += olddate.get(Calendar.MONTH) + "/" + olddate.get(Calendar.DAY_OF_MONTH) + "/" + olddate.get(Calendar.YEAR);
		} else {
			if (nowtime > oldtime + 86400) {
				int dayDiff = nowdate.get(Calendar.DAY_OF_MONTH) - olddate.get(Calendar.DAY_OF_MONTH);
				modified += dayDiff + (dayDiff == 1 ? " day ago" : " days ago");
			} else {
				if (nowtime > oldtime + 3600) {
					int hourDiff = (int) ((nowtime - oldtime) / 3600L);
					modified += hourDiff + (hourDiff == 1 ? " hour ago" : " hours ago");
				} else {
					if (nowtime > oldtime + 60) {
						int minDiff = (int) ((nowtime - oldtime) / 60L);
						modified += minDiff + (minDiff == 1 ? " minute ago" : " minutes ago");
					} else {
						modified += "less than a minute ago";
					}
				}
			}
		}
		
		mTvNotebookModified.setText(modified);
	}
	
	@SuppressWarnings("deprecation")
	private void setColoring() {
		mTvNotebookTitle.setTextColor(mNotebook.color);
		mTvNotebookModified.setTextColor(adjustColorOpacity(mNotebook.color, 0.5f));
		mTvNotebookPages.setTextColor(adjustColorOpacity(mNotebook.color, 0.9f));
		
		StateListDrawable statesAdd = new StateListDrawable();
		statesAdd.addState(new int[] {android.R.attr.state_pressed}, new ColorDrawable(adjustColorOpacity(mNotebook.color, 0.20f)));
		statesAdd.addState(new int[] {}, new ColorDrawable(0x00ffffff));
		
		StateListDrawable statesDelete = new StateListDrawable();
		statesDelete.addState(new int[] {android.R.attr.state_pressed}, new ColorDrawable(adjustColorOpacity(mNotebook.color, 0.20f)));
		statesDelete.addState(new int[] {}, new ColorDrawable(0x00ffffff));

		mDeleteButton.setBackgroundDrawable(statesDelete);
		mAddNoteButton.setBackgroundDrawable(statesAdd);
	}
	
	private int adjustColorOpacity(int color, float percentOpacity) {
		if (percentOpacity > 1.0f) percentOpacity = 1.0f;
		if (percentOpacity < 0.0f) percentOpacity = 0.0f;
		
		int a = (int) ((color >>> 24) * percentOpacity);
		int r = (color >> 16) & 0xff;
		int g = (color >> 8) & 0xff;
		int b = color & 0xff;

		return (a << 24) | (r << 16) | (g << 8) | b;
	}
}
