package com.ctk.notebooks;

import com.ctk.notebooks.Utils.LockableScrollView;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class NotebookNotesActivity extends FragmentActivity {

	private ActionBar 	mActionBar;
	private String	 	mNotebookName;
	private int			mNotebookId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_note);
		
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		if (getIntent().hasExtra("notebook_name") && getIntent().hasExtra("notebook_id")) {
			mNotebookName = getIntent().getExtras().getString("notebook_name", "NULL");
			mNotebookId = getIntent().getExtras().getInt("notebook_id", -1);
			mActionBar.setTitle("Pages in " + mNotebookName);
		}
		
	}
}
