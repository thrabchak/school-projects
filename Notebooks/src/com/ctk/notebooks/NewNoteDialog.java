package com.ctk.notebooks;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ctk.notebooks.Utils.Notebook;

public class NewNoteDialog extends DialogFragment {
	
	Context								mContext;
	LayoutInflater 						mInflator;
	String 								mDialogTitle;
	OnClickListener						mPosClick, mNegClick;
	String 								mPosTitle, mNegTitle;
	View 								mLayout;
	ListView							mNotebookList;
	ArrayList<Notebook>					mNotebooksArray;
	OnCreateNewNotebookListener			mCreateNewNotebookListener;
	DatabaseHelper						mDatabase;
	TextView							mCreateNewNotebook;
	
	public NewNoteDialog() {}
	
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
		
		final static int NOTE_BACKGROUND_LINED = 0;
		final static int NOTE_BACKGROUND_BLANK = 1;
		
		NewNoteDialog 				dialog;
		private TextView			mLined, mBlank;
		int 						mNotebookSelected;
		int							mNoteBackground;
		
		public Builder(Context context) {
			dialog = new NewNoteDialog();
			dialog.mContext = context;	
			dialog.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog.mLayout = dialog.mInflator.inflate(R.layout.dialog_new_note, null);
			dialog.mNotebookList = (ListView) dialog.mLayout.findViewById(R.id.dialog_new_note_notebook_list);
			
			mDatabase = new DatabaseHelper(context);
			mNotebooksArray = mDatabase.getNotebooks();
			
			if (mNotebooksArray.size() == 0) 
				dialog.mNotebookList.setVisibility(View.GONE);
			else
				dialog.mNotebookList.setVisibility(View.VISIBLE);
			
			NotebookListAdapter adapter = new NotebookListAdapter(dialog.mContext, mNotebooksArray);
			dialog.mNotebookList.setAdapter(adapter);
			dialog.mNotebookList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
			dialog.mNotebookList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View row, int position, long arg3) {
					dialog.mNotebookList.setSelection(position);
					mNotebookSelected = position;
				}
			});
			
			dialog.mCreateNewNotebook = (TextView) dialog.mLayout.findViewById(R.id.tv_new_note_no_notebooks);
			if (mNotebooksArray.size() == 0)
				dialog.mCreateNewNotebook.setVisibility(View.VISIBLE);
			else
				dialog.mCreateNewNotebook.setVisibility(View.GONE);
			dialog.mCreateNewNotebook.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					dialog.mCreateNewNotebookListener.onClickCreateNewNotebook();
				}
			});
			
			mLined = (TextView) dialog.mLayout.findViewById(R.id.new_note_lined_background);
			mBlank = (TextView) dialog.mLayout.findViewById(R.id.new_note_blank_background);
			
			mLined.setTypeface(null, Typeface.NORMAL);
			mBlank.setTypeface(null, Typeface.BOLD);
			
			mLined.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mNoteBackground = NOTE_BACKGROUND_LINED;
					mLined.setTypeface(null, Typeface.BOLD);
					mBlank.setTypeface(null, Typeface.NORMAL);
				}
			});
			
			mBlank.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mNoteBackground = NOTE_BACKGROUND_BLANK;
					mBlank.setTypeface(null, Typeface.BOLD);
					mLined.setTypeface(null, Typeface.NORMAL);
				}
			});
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
		
		public Builder setCreateNewNotebookListener(OnCreateNewNotebookListener clickListener) {
			dialog.mCreateNewNotebookListener = clickListener;
			return this;
		}
		
		public void show(FragmentManager manager, String tag) {
			dialog.show(manager, tag);
		}
		
		public int getSelectedNotebookId() {
			return mNotebooksArray.get(mNotebookSelected).id;
		}
		
		public String getSelectedNotebookName() {
			return mNotebooksArray.get(mNotebookSelected).name;
		}
		
		public int getSelectedNotebookPageCount() {
			return mNotebooksArray.get(mNotebookSelected).numPages;
		}
		
		public boolean isNoteLined() {
			return mNoteBackground == NOTE_BACKGROUND_LINED;
		}
	}
	
	public interface OnCreateNewNotebookListener {
		public void onClickCreateNewNotebook();
	}
	
	public class NotebookListAdapter extends BaseAdapter {
		
		ArrayList<Notebook> mNotebookArray;
		LayoutInflater		mLayoutInflater;
		Context				mContext;
		TextView			mNotebookName;
		
		public NotebookListAdapter(Context context, ArrayList<Notebook> notebooks) {
			mNotebookArray = notebooks;
			mContext = context;
			mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return mNotebookArray.size();
		}

		@Override
		public Object getItem(int position) {
			return mNotebookArray.get(position).name;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
	            convertView = mLayoutInflater.inflate(R.layout.list_item_notebooks, null);
			
			mNotebookName = (TextView) convertView.findViewById(R.id.list_notebook_name);
			mNotebookName.setText(mNotebookArray.get(position).name);
			mNotebookName.setTextColor(mNotebookArray.get(position).color);
			
			return convertView;
		}
	}
}
