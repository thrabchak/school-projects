package com.ctk.notebooks;

import java.util.ArrayList;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ctk.notebooks.Utils.Notebook;

public class NewNoteDialog extends DialogFragment {
	
	Context				mContext;
	LayoutInflater 		mInflator;
	String 				mDialogTitle;
	OnClickListener		mPosClick, mNegClick;
	String 				mPosTitle, mNegTitle;
	View 				mLayout;
	ListView			mNotebookList;
	ArrayList<Notebook>	mNotebooksArray;
	DatabaseHelper		mDatabase;
	
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
		
		NewNoteDialog dialog;
		
		public Builder(Context context) {
			dialog = new NewNoteDialog();
			dialog.mContext = context;	
			dialog.mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog.mLayout = dialog.mInflator.inflate(R.layout.dialog_new_note, null);
			dialog.mNotebookList = (ListView) dialog.mLayout.findViewById(R.id.dialog_new_note_notebook_list);
			
			mDatabase = new DatabaseHelper(context);
			mNotebooksArray = mDatabase.getNotebooks();
			
			String notebookNames[] = new String[mNotebooksArray.size()];
			for (int i = 0; i < mNotebooksArray.size(); i++)
				notebookNames[i] = mNotebooksArray.get(i).name;
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, notebookNames);
			dialog.mNotebookList.setAdapter(adapter);
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
	}
}
