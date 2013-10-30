package com.ctk.notebooks;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	Button mOpenNote;
	Button mRandomNote;
	DatabaseHelper db = null;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mOpenNote = (Button) findViewById(R.id.btn_open);
        mOpenNote.setOnClickListener(this);
        mRandomNote=(Button) findViewById(R.id.btn_randNotebook);
        mRandomNote.setOnClickListener(this);
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
		case R.id.btn_randNotebook:
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		    builder.setTitle("Pick a Note");
//		           .setAdapter(), new DialogInterface.OnClickListener() {
//		               public void onClick(DialogInterface dialog, int which) {
//		               // The 'which' argument contains the index position
//		               // of the selected item
//		           }
//		    });
		}
		
	}
}