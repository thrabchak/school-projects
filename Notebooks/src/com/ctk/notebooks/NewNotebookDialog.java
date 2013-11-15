package com.ctk.notebooks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class NewNotebookDialog extends DialogFragment{
		
		public LayoutInflater inflator;
		public String title;
		public DialogInterface.OnClickListener posClick, negClick;
		public String posTitle, negTitle;
		public View layout;
		public TextView notebookName;
		
		public NewNotebookDialog() {
			
		}
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setView(layout);
	        builder.setTitle(title)
	               .setPositiveButton(posTitle, posClick)
	               .setNegativeButton(negTitle, negClick);
	         //Create the AlertDialog object and return it
	        return builder.create();
	    }
		
		public class Builder{
			
			NewNotebookDialog d;
			
			public Builder(Context context){
				d = new NewNotebookDialog();
				d.inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				d.layout = d.inflator.inflate(R.layout.dialog_new_notebook, null);
		        d.notebookName = (TextView)d.layout.findViewById(R.id.notebookName);
			}
			
			public Builder setTitle(String new_title){
				d.title = new_title;
				return this;
			}
			
			public Builder setPositiveButton(String name, DialogInterface.OnClickListener clickListener){
				d.posClick = clickListener;
				d.posTitle = name;
				return this;
			}
			
			public Builder setNegativeButton(String name, DialogInterface.OnClickListener clickListener){
				d.negClick = clickListener;
				d.negTitle = name;
				return this;
			}
			
			public void show(FragmentManager manager, String tag){
				
				d.show(manager, tag);
			}
			
			public String getEntry(){
				return d.notebookName.getText().toString();
			}
			
		}
	
	}