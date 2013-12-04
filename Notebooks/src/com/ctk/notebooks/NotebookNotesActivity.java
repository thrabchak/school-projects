package com.ctk.notebooks;

import java.util.ArrayList;

import com.ctk.notebooks.Utils.Note;
import com.ctk.notebooks.Utils.NotebookNotesGridAdapter;
import com.ctk.notebooks.Utils.NotebookNotesGridAdapter.OnNoteActionClickListener;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class NotebookNotesActivity extends FragmentActivity {

	private ActionBar 					mActionBar;
	private String	 					mNotebookName;
	private int							mNotebookId;
	private GridView					mNotebookNotesGrid;
	private NotebookNotesGridAdapter	mNotebookNotesGridAdapter;
	private DatabaseHelper				mDatabase = null;
	private Context 					mContext;
	private TextView 					mTvNoNotes;
	private ArrayList<Note>				mNotesArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_notebook_notes);
		mNotebookNotesGrid = (GridView) findViewById(R.id.notebook_notes_grid);
		mTvNoNotes = (TextView) findViewById(R.id.tv_no_notes);
		
		mDatabase = new DatabaseHelper(this);
		mContext = this;
		
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		if (getIntent().hasExtra("notebook_name") && getIntent().hasExtra("notebook_id")) {
			mNotebookName = getIntent().getExtras().getString("notebook_name", "NULL");
			mNotebookId = getIntent().getExtras().getInt("notebook_id", -1);
			mActionBar.setTitle("Pages in " + mNotebookName);
		}
		
		mNotebookNotesGridAdapter = new NotebookNotesGridAdapter(this);
		mNotesArray = mDatabase.getNotes(mNotebookId);
		if (mNotesArray.size() != 0)
			mTvNoNotes.setVisibility(View.GONE);
		
		OnNoteActionClickListener listener = new OnNoteActionClickListener() {
			
			@Override
			public void onOverflowClicked(Note note) {
				Toast.makeText(mContext, "overflow clicked", Toast.LENGTH_SHORT).show();
			}
		};
		
		for (Note note : mNotesArray) {
			mNotebookNotesGridAdapter.addNote(note, listener);
		}
		
		mNotebookNotesGrid.setAdapter(mNotebookNotesGridAdapter);
	}
}
