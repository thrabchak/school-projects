package com.ctk.notebooks.Utils;

import java.util.ArrayList;

import com.ctk.notebooks.Utils.Note;
import com.ctk.notebooks.Utils.NotebookGridAdapter.OnNotebookActionClickListener;
import com.ctk.notebooks.DatabaseHelper;
import com.ctk.notebooks.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotebookNotesGridAdapter extends BaseAdapter {

	Context 								mContext;
	ArrayList<Note> 						mNotesArray;
	LayoutInflater 							mLayoutInflater;
	ArrayList<OnNoteActionClickListener>	mNoteActionClickListenerArray;
	int										mNotebookId;
	DatabaseHelper							mDatabase;
	TextView								mNotePageNumber;
	TextView								mNoteName;
	View									mNoteActionButton;
	
	public NotebookNotesGridAdapter(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mNotesArray = new ArrayList<Note>();
		mNoteActionClickListenerArray = new ArrayList<OnNoteActionClickListener>();
	}
	
	public void addNote(Note note, OnNoteActionClickListener actionListener) {
		mNotesArray.add(note);
		mNoteActionClickListenerArray.add(actionListener);
		notifyDataSetChanged();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.grid_item_note, null);
		
		mNotePageNumber = (TextView) convertView.findViewById(R.id.note_page_number);
		mNoteName = (TextView) convertView.findViewById(R.id.note_title);
		mNoteActionButton = convertView.findViewById(R.id.note_action_overflow);
            
        final Note note = mNotesArray.get(position);
        
        mNotePageNumber.setText("Page " + note.pageNumber);
        if (note.name != "") mNoteName.setText(note.name);
        mNoteActionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mNoteActionClickListenerArray.get(position).onOverflowClicked(note);
			}
		});

        return convertView;
	}

	public interface OnNoteActionClickListener {
		public void onOverflowClicked(Note note);
	}
}
