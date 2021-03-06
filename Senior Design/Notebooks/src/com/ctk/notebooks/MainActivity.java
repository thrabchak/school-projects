package com.ctk.notebooks;

import com.ctk.notebooks.NewNoteDialog.OnCreateNewNotebookListener;
import com.ctk.notebooks.Utils.Notebook;
import com.ctk.notebooks.Utils.NotebookGridAdapter;
import com.ctk.notebooks.Utils.NotebookGridAdapter.OnNotebookActionClickListener;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	Context							mContext = this;
	DatabaseHelper 					mDatabase = null;
	TextView						mTvNoNotebooks;
	GridView						mNotebookGrid;
	NotebookGridAdapter 			mNotebookGridAdapter;
	OnNotebookActionClickListener	mStandardListener;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTvNoNotebooks = (TextView) findViewById(R.id.tv_no_notebooks);
        mNotebookGridAdapter = new NotebookGridAdapter(this);
        mNotebookGrid = (GridView) findViewById(R.id.notebook_grid_list);
        mDatabase = new DatabaseHelper(this);
        
        mStandardListener = new OnNotebookActionClickListener() {
			
			@Override
			public void onNotebookDeleteClick(Notebook notebook) {
				showConfirmDeleteDialog(notebook);
			}
			
			@Override
			public void onNotebookAddNoteClick(Notebook notebook) {
				Intent i = new Intent(mContext, NoteActivity.class);
				i.putExtra("notebook_id", notebook.id)
				 .putExtra("notebook_name", notebook.name)
				 .putExtra("note_page_number", notebook.numPages + 1);
				startActivity(i);
			}

			@Override
			public void onNotebookClicked(Notebook notebook) {
				Intent i = new Intent(mContext, NotebookNotesActivity.class);
				i.putExtra("notebook_id", notebook.id)
				 .putExtra("notebook_name", notebook.name);
				startActivity(i);
			}
		};

		loadNotebooks();
		mNotebookGrid.setAdapter(mNotebookGridAdapter);
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		loadNotebooks();
	}

	private void loadNotebooks() {
    	mNotebookGridAdapter.empty();
    	for (Notebook notebook : mDatabase.getNotebooks())
			mNotebookGridAdapter.addNotebook(notebook, mStandardListener);
    	
    	if (mNotebookGridAdapter.getCount() != 0)
    		mTvNoNotebooks.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new_note:
			final NewNoteDialog.Builder noteBuilder = (new NewNoteDialog()).new Builder(this);
		    noteBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
				
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(mContext, NoteActivity.class);
							i.putExtra("notebook_id", noteBuilder.getSelectedNotebookId())
							 .putExtra("notebook_name", noteBuilder.getSelectedNotebookName())
							 .putExtra("note_page_number", noteBuilder.getSelectedNotebookPageCount() + 1)
							 .putExtra("note_page_background_lined", noteBuilder.isNoteLined());
							startActivity(i);
						}
					})
			           .setCreateNewNotebookListener(new OnCreateNewNotebookListener() {
						
						@Override
						public void onClickCreateNewNotebook() {
							showNewNotebookDialog();
						}
					})
					.setTitle("New note")
			        .show(getSupportFragmentManager(), "ckt");
			
			break;
		case R.id.action_new_notebook:
			showNewNotebookDialog();
			break;
		}
		
		return false;
	}
	
	private void showNewNotebookDialog() {
		final NewNotebookDialog.Builder notebookBuilder = (new NewNotebookDialog()).new Builder(this);
	    notebookBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
			
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mDatabase.addNotebook(notebookBuilder.getNotebookName(), notebookBuilder.getNotebookColor());
							loadNotebooks();
						}
					})
		               .setTitle("Create new notebook")
		               .show(getSupportFragmentManager(), "ckt");
	}

	private void showConfirmDeleteDialog(final Notebook notebook) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Delete notebook " + notebook.name + "?")
			   .setMessage("This action will delete this notebook, including all of the notes "
			   			   + "you have created within it. You cannot undo this action. Are you sure "
			   			   + "you want to delete this notebook?")
			   .setPositiveButton("Cancel", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			   })
			   
			   .setNegativeButton("Delete", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDatabase.deleteNotebook(notebook.id);
					mNotebookGridAdapter.removeNotebook(notebook.id);
					if (mNotebookGridAdapter.getCount() == 0) mTvNoNotebooks.setVisibility(View.VISIBLE);
				}
			});
		builder.show();
	}
}