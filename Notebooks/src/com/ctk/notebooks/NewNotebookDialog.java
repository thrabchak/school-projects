package com.ctk.notebooks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class NewNotebookDialog extends DialogFragment{
		
		LayoutInflater inflator;
		String title;
		DialogInterface.OnClickListener posClick, negClick;
		String posTitle, negTitle;
		View layout;
		TextView notebookName;
		
		public NewNotebookDialog() {
			
		}
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        layout = inflator.inflate(R.layout.dialog_new_notebook, null);
	        notebookName = (TextView)layout.findViewById(R.id.notebookName);
	        builder.setView(layout);
	        builder.setTitle(title)
	               .setPositiveButton(posTitle, posClick)
	               .setNegativeButton(negTitle, negClick);
	         //Create the AlertDialog object and return it
	        return builder.create();
	    }
		
		public class Builder{
			
			public Builder(Context context){
				inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
			}
			
			public Builder setTitle(String new_title){
				title = new_title;
				return this;
			}
			
			public Builder setPositiveButton(String name, DialogInterface.OnClickListener clickListener){
				posClick = clickListener;
				posTitle = name;
				return this;
			}
			
			public Builder setNegativeButton(String name, DialogInterface.OnClickListener clickListener){
				negClick = clickListener;
				negTitle = name;
				return this;
			}
			
			public void show(){
				
			}
			
			public String getEntry(){
				return notebookName.getText().toString();
			}
			
		}
	
	}