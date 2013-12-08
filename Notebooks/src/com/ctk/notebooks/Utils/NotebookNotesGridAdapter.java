package com.ctk.notebooks.Utils;

import java.util.ArrayList;

import com.ctk.notebooks.Utils.Note;
import com.ctk.notebooks.DatabaseHelper;
import com.ctk.notebooks.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NotebookNotesGridAdapter extends BaseAdapter {

	private final static String BBINDERDIRECTORY = Environment.getExternalStorageDirectory() + "/bBinder";
	
	Context 								mContext;
	ArrayList<Note> 						mNotesArray;
	LayoutInflater 							mLayoutInflater;
	ArrayList<OnNoteActionClickListener>	mNoteActionClickListenerArray;
	int										mNotebookId;
	DatabaseHelper							mDatabase;
	TextView								mNotePageNumber;
	TextView								mNoteName;
	View									mNoteActionButton;
	ImageView								mNoteThumbnail;
	LoadThumbnail							mThumbnailLoader;
	
	public NotebookNotesGridAdapter(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mNotesArray = new ArrayList<Note>();
		mNoteActionClickListenerArray = new ArrayList<OnNoteActionClickListener>();
		mThumbnailLoader = new LoadThumbnail();
	}
	
	public void addNote(Note note, OnNoteActionClickListener actionListener) {
		mNotesArray.add(note);
		mNoteActionClickListenerArray.add(actionListener);
		notifyDataSetChanged();
	}
	
	public void removeNote(Note note) {
		int index = 0;
		
		for (Note n : mNotesArray) {
			if (n.notebookId == note.notebookId && n.pageNumber == note.pageNumber)
				break;
			index++;
		}
		
		mNotesArray.remove(index);
		mNoteActionClickListenerArray.remove(index);
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
		ViewHolder viewHolder = null;
		if(convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.grid_item_note, null);
		
		viewHolder = new ViewHolder();
		
		mNotePageNumber = (TextView) convertView.findViewById(R.id.note_page_number);
		mNoteName = (TextView) convertView.findViewById(R.id.note_title);
		mNoteActionButton = convertView.findViewById(R.id.note_action_overflow);
		mNoteThumbnail = (ImageView) convertView.findViewById(R.id.note_thumbnail);
		viewHolder.thumbnail = mNoteThumbnail;
		viewHolder.noteLoading = (ProgressBar) convertView.findViewById(R.id.note_loading);
            
        final Note note = mNotesArray.get(position);
        
        mNotePageNumber.setText("Page " + note.pageNumber);
        if (note.name != "") mNoteName.setText(note.name);
        mNoteActionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(mContext, v);  
	            popup.getMenuInflater().inflate(R.menu.note_popup_menu, popup.getMenu());  
	           
	            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
		            public boolean onMenuItemClick(MenuItem item) {  
		            	switch (item.getItemId()) {
		            	case R.id.note_popup_menu_send:
		            		mNoteActionClickListenerArray.get(position).onSendClicked(note);
		            		break;
		            	case R.id.note_popup_menu_add_tag:
		            		mNoteActionClickListenerArray.get(position).onAddTagClicked(note);
		            		break;
		            	case R.id.note_popup_menu_delete:
		            		mNoteActionClickListenerArray.get(position).onDeleteClicked(note);
		            		break;
		            	}
		            	
		            	return true;
		            }  
	            });  
	  
	            popup.show();  
			}
		});
        
        mNoteThumbnail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mNoteActionClickListenerArray.get(position).onNoteClicked(note);
			}
		});
        
        convertView.setTag(viewHolder);
        viewHolder.filepath = note.filepath;
        new LoadThumbnail().execute(viewHolder);
        return convertView;
	}

	public interface OnNoteActionClickListener {
		public void onNoteClicked(Note note);
		public void onDeleteClicked(Note note);
		public void onSendClicked(Note note);
		public void onAddTagClicked(Note note);
	}
	
	static class ViewHolder {
		String filepath;
		Bitmap image;
		ImageView thumbnail;
		ProgressBar noteLoading;
	}
	
	public class LoadThumbnail extends AsyncTask<ViewHolder, Void, ViewHolder> {

		@Override
		protected ViewHolder doInBackground(ViewHolder... params) {
			ViewHolder v = params[0];
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inMutable = true;

			v.image = BitmapFactory.decodeFile(BBINDERDIRECTORY + "/" + v.filepath + ".png", opts);
			return v; 
		}

		@Override
		protected void onPostExecute(ViewHolder result) {
			if (result.image != null) {
				result.noteLoading.setVisibility(View.GONE);
				result.thumbnail.setImageBitmap(result.image);
				result.thumbnail.setScaleType(ScaleType.CENTER_CROP);
			}
		}
	}
}
