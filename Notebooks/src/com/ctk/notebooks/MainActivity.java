package com.ctk.notebooks;

import android.R.color;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {

	Button mOpenNote;
	Button mRandomNote;
	DatabaseHelper db = null;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mOpenNote = (Button) findViewById(R.id.btn_open);
        mOpenNote.setOnClickListener(this);
        db = new DatabaseHelper(this);
        Toast.makeText(this, "Number of notebooks = " + db.getNumNotebooks(), Toast.LENGTH_SHORT).show();
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
			startActivity(new Intent(this, NoteActivity.class));
			return true;
		case R.id.action_new_notebook:
			final NewNotebookDialog.Builder builder = (new NewNotebookDialog()).new Builder(this);
		    builder.setTitle("Create new notebook").setPositiveButton("Create", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					db.addNotebook(builder.getEntry(), 0x7D26CD);
					Toast.makeText(getApplicationContext(), "Name of new notebook = " + builder.getEntry(), Toast.LENGTH_SHORT).show();
				}
			});
			builder.show(getSupportFragmentManager(), "ckt");
			
			break;
		}
		
		return false;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_open:
			Intent openNote = new Intent(this, NoteActivity.class);
			openNote.putExtra("is_open_note", true)
					.putExtra("filename", "test1");
			startActivity(openNote);
			break;
		}
		
	}
	
	
}