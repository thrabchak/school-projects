package com.ctk.notebooks.Utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NotebookNotesGridAdapter extends BaseAdapter {

	Context 								mContext;
	ArrayList<Note> 						mNotesArray;
	LayoutInflater 							mLayoutInflater;
	ArrayList<OnNoteActionClickListener>	mNotebctionClickListenerArray;
	
	public NotebookNotesGridAdapter(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mNotesArray = new ArrayList<Note>();
		mNotebctionClickListenerArray = new ArrayList<OnNoteActionClickListener>();
	}
	
	@Override
	public int getCount() {
		return mNotesArray.size();
	}

	@Override
	public Note getItem(int position) {
		return mNotesArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	public interface OnNoteActionClickListener {
		public void onDeleteClicked(Note note);
	}
}
