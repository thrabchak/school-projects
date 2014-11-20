package com.ctk.notebooks.Utils;

import java.util.ArrayList;

import com.ctk.notebooks.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NotebookGridAdapter extends BaseAdapter {

	Context 									mContext;
	ArrayList<Notebook> 						mNotebookArray;
	ArrayList<OnNotebookActionClickListener> 	mNotebookActionClickListenerArray;
	LayoutInflater								mLayoutInflater;
	
	public NotebookGridAdapter(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mNotebookArray = new ArrayList<Notebook>();
		mNotebookActionClickListenerArray = new ArrayList<OnNotebookActionClickListener>();
	}
	
	public void addNotebook(Notebook notebook, OnNotebookActionClickListener actionListener) {
		mNotebookArray.add(notebook);
		mNotebookActionClickListenerArray.add(actionListener);
		notifyDataSetChanged();
	}
	
	public void removeNotebook(int id) {
		int index = 0;
		
		for (Notebook notebook : mNotebookArray) {
			if (notebook.id == id)
				break;
			index++;
		}
		
		mNotebookArray.remove(index);
		mNotebookActionClickListenerArray.remove(index);
		notifyDataSetChanged();
	}
	
	public void empty() {
		mNotebookArray.clear();
		mNotebookActionClickListenerArray.clear();
	}
	
	public void setNotebookArray(ArrayList<Notebook> notebooks) {
		mNotebookArray = notebooks;
	}
	
	@Override
	public int getCount() {
		return mNotebookArray.size();
	}

	@Override
	public Notebook getItem(int index) {
		return mNotebookArray.get(index);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.grid_item_notebook_card, null);

        NotebookCard notebookCard = new NotebookCard(mContext, mNotebookArray.get(position), convertView, 
        							mNotebookActionClickListenerArray.get(position));

        return notebookCard.getCardView();
	}
	
	public interface OnNotebookActionClickListener {
		public void onNotebookDeleteClick(Notebook notebook);
		public void onNotebookAddNoteClick(Notebook notebook);
		public void onNotebookClicked(Notebook notebook);
	}
}
