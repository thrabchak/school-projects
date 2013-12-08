package com.ctk.notebooks;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.ctk.notebooks.Utils.Note;
import com.ctk.notebooks.Utils.NotebookNotesGridAdapter;
import com.ctk.notebooks.Utils.NotebookNotesGridAdapter.OnNoteActionClickListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class NotebookNotesActivity extends FragmentActivity {

	private final static String BBINDERDIRECTORY = Environment.getExternalStorageDirectory() + "/bBinder";
	
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
			public void onNoteClicked(Note note) {
				Intent i = new Intent(mContext, NoteActivity.class);
				i.putExtra("is_open_note", true)
				 .putExtra("filename", note.filepath)
				 .putExtra("notebook_id", note.notebookId)
				 .putExtra("notebook_name", mNotebookName)
				 .putExtra("note_page_number", note.pageNumber);
				startActivity(i);
			}

			@Override
			public void onDeleteClicked(Note note) {
				showConfirmDeleteDialog(note);
			}

			@Override
			public void onSendClicked(Note note) {
				// TODO: Get send to work
				Toast.makeText(mContext, note.name + " send", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAddTagClicked(Note note) {
				// TODO: (maybe) Get tags working. Shouldn't be toooooo difficult. 
				Toast.makeText(mContext, note.name + " add tag", Toast.LENGTH_SHORT).show();
			}
		};
		
		for (Note note : mNotesArray) {
			mNotebookNotesGridAdapter.addNote(note, listener);
		}
		
		mNotebookNotesGrid.setAdapter(mNotebookNotesGridAdapter);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notebook_notes_activity, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_new_note:
			Intent i = new Intent(mContext, NoteActivity.class);
			i.putExtra("notebook_id", mNotebookId)
			 .putExtra("notebook_name", mNotebookName)
			 .putExtra("note_page_number", mNotebookNotesGrid.getCount() + 1);
			startActivity(i);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void showConfirmDeleteDialog(final Note note) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Delete " + note.name + " from " + mNotebookName + "?")
			   .setMessage("This action will delete this page of notes from " + mNotebookName + ". You"
			   		+ "cannot undo this action. Are you sure you want to delete?")
			   .setPositiveButton("Cancel", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			   })
			   
			   .setNegativeButton("Delete", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDatabase.deleteNote(note.notebookId, note.pageNumber);
					mNotebookNotesGridAdapter.removeNote(note);
					if (mNotebookNotesGridAdapter.getCount() == 0)
						mTvNoNotes.setVisibility(View.VISIBLE);
				}
			});
		builder.show();
	}
	
	public class SendNote extends AsyncTask<String, Void, Void> {
		String name;
		ProgressDialog	pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(mContext);
			pd.setTitle("Converting to PDF ...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			Uri uri = Uri.fromFile(new File(BBINDERDIRECTORY + "/" + name
					+ ".pdf"));
			emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, name);
			if (pd != null) {
				pd.dismiss();
			}
			startActivity(Intent.createChooser(emailIntent,
					"Send with: "));
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(String... params) {
			name = params[0];
			
//			try {
//				File bBinderDirectory = new File(BBINDERDIRECTORY);
//				bBinderDirectory.mkdir();
//
//				FileOutputStream stream = new FileOutputStream(new File(
//						bBinderDirectory, "/" + params[0] + ".png"));
//				//mNoteView.getBitmap().compress(CompressFormat.PNG, 80, stream);
//				stream.close();
//			} catch (IOException e) {
//				Log.e("ckt", "Save file error");
//				e.printStackTrace();
//			}
			
			
			return null;
		}
		
		public Document convertToPDF(String filename) {
			Document d = new Document();
			try {
				PdfWriter.getInstance(d, new FileOutputStream(BBINDERDIRECTORY
						+ "/" + filename + ".pdf"));
				d.open();

				String impath = BBINDERDIRECTORY + "/" + filename + ".png";
				Image im = Image.getInstance(impath);
				im.scaleAbsolute(550f, 800f);
				d.add(im);
				d.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return d;

		}
	}
}
