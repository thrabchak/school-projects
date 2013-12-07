package com.ctk.notebooks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.ctk.notebooks.Utils.ColorPickerSwatch;
import com.ctk.notebooks.Utils.ColorPickerSwatch.OnColorSelectedListener;
import com.ctk.notebooks.Utils.LockableScrollView;
import com.ctk.notebooks.Utils.RandomStringGenerator;
import com.ctk.notebooks.Utils.VerticalSeekBar;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class NoteActivity extends Activity {

	private final static String		BBINDERDIRECTORY	= Environment
																.getExternalStorageDirectory()
																+ "/bBinder";

	private ActionBar				mActionBar;
	private NoteView				mNoteView;
	private LockableScrollView		mScrollView;
	private boolean					mSaveNote			= true;
	private String					mNotebookName;
	private int						mNotebookId;
	private int						mNotePageNumber;
	private DrawerLayout			mDrawerLayout;
	private DatabaseHelper			mDatabase;
	private final String			mNoteName			= null;
	private String					mFileName;
	private RandomStringGenerator	mRandomStringGenerator;
	private Context					mContext;
	private final int				NUM_SWATCHES		= 7;
	private int						mSelectedColor;
	private final int				mSwatchColors[]		= { 0xff000000,
			0xff33b5e5, 0xffaa66cc, 0xffff4444, 0xffffbb33, 0xff99cc00,
			0xffffffff									};
	ColorPickerSwatch				mSwatches[]			= new ColorPickerSwatch[NUM_SWATCHES];
	private final int				mSwatchIds[]		= { R.id.swatch_1,
			R.id.swatch_2, R.id.swatch_3, R.id.swatch_4, R.id.swatch_5,
			R.id.swatch_6, R.id.swatch_7				};
	private int						mStrokeSize;
	private VerticalSeekBar	mVerticalSeekBar;
	private final int[] strokeSizes = {2, 5, 8, 12, 20, 50};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_note);

		mContext = this;

		mNoteView = (NoteView) findViewById(R.id.note_view);
		mScrollView = (LockableScrollView) findViewById(R.id.note_scroll_view);
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.toolbar_drawer);

		mDatabase = new DatabaseHelper(this);
		mRandomStringGenerator = new RandomStringGenerator(16);

		if (getIntent().hasExtra("is_open_note")
				&& getIntent().getExtras().getBoolean("is_open_note", false)) {
			mFileName = getIntent().getExtras().getString("filename");
			mNoteView.setBitmap(openFile(mFileName));
		} else {
			mFileName = mRandomStringGenerator.nextString();
		}

		if (getIntent().hasExtra("notebook_name")
				&& getIntent().hasExtra("notebook_id")
				&& getIntent().hasExtra("note_page_number")) {
			mNotebookName = getIntent().getExtras().getString("notebook_name",
					"NULL");
			mNotebookId = getIntent().getExtras().getInt("notebook_id", -1);
			mNotePageNumber = getIntent().getExtras().getInt(
					"note_page_number", -1);
			mActionBar.setTitle("Page " + mNotePageNumber);
			mActionBar.setSubtitle("in " + mNotebookName);
		}
		
		mVerticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
		mVerticalSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mNoteView.setPaintWidth(strokeSizes[progress]);
			}
		});

		mNoteView.setPaintColor(0xFF000000);
		mNoteView.setPaintWidth(5);

		mScrollView.setScrollingLocked(true);

		mSelectedColor = 0;
		mStrokeSize = 5;
		initSwatches();
	}

	private void initSwatches() {
		for (int i = 0; i < NUM_SWATCHES; i++) {
			final int temp = i;
			mSwatches[i] = (ColorPickerSwatch) mDrawerLayout
					.findViewById(mSwatchIds[i]);
			mSwatches[i].setColor(mSwatchColors[i]);
			mSwatches[i]
					.setOnColorSelectedListener(new OnColorSelectedListener() {

						@Override
						public void onColorSelected(int color) {
							mSelectedColor = color;
							mNoteView.setPaintColor(color);
							mNoteView.setPaintWidth(mStrokeSize);

							mSwatches[temp].setChecked(true);
							for (int j = 0; j < NUM_SWATCHES; j++) {
								if (j != temp)
									mSwatches[j].setChecked(false);
							}
						}
					});
		}

		// Handle the special case of the eraser
		mSwatches[6].setAsEraser();
		mSwatches[6].setOnColorSelectedListener(new OnColorSelectedListener() {
			@Override
			public void onColorSelected(int color) {
				mSelectedColor = color;
				mNoteView.setPaintColor(color);
				mNoteView.setPaintWidth(mStrokeSize * 10);

				mSwatches[6].setChecked(true);
				for (int j = 0; j < NUM_SWATCHES; j++) {
					if (j != 6)
						mSwatches[j].setChecked(false);
				}
			}
		});

		mSwatches[mSelectedColor].setChecked(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.note_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_send_note:
			sendNote();
			return true;
		case R.id.action_show_toolbar:
			if (mDrawerLayout.isDrawerOpen(Gravity.END))
				mDrawerLayout.closeDrawer(Gravity.END);
			else
				mDrawerLayout.openDrawer(Gravity.END);
			return true;
		case R.id.action_discard_note:
			mSaveNote = false;
			NavUtils.navigateUpFromSameTask(this);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Save current Note as a png to the SD card.
	 * 
	 * @param fileName
	 *            The name for which the file will be stored.
	 * @return <code>true</code> if the file was saved without an error,
	 *         <code>false</code> otherwise.
	 */
	public void saveNote() {
		new SaveNote().execute(mFileName);
	}

	/**
	 * Reads a saved Note as a png from the SD card.
	 * 
	 * @param filename
	 *            The name of the file to read.
	 * @return A <code>Bitmap</code> of the png.
	 */
	public Bitmap openFile(String filename) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inMutable = true;

		return BitmapFactory.decodeFile(BBINDERDIRECTORY + "/" + filename
				+ ".png", opts);
	}

	@Override
	protected void onPause() {
		if (mSaveNote)
			saveNote();
		super.onPause();
	}

	public void sendNote() {
		new SendNote().execute(mFileName);
	}

	public class SendNote extends AsyncTask<String, Void, Void> {
		String			name;
		ProgressDialog	pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(NoteActivity.this);
			pd.setTitle("Converting to PDF ...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			super.onPreExecute();

			// new SaveNote().execute(name);
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
			startActivity(Intent.createChooser(emailIntent, "Send with: "));
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(String... params) {
			name = params[0];

			try {
				File bBinderDirectory = new File(BBINDERDIRECTORY);
				bBinderDirectory.mkdir();

				FileOutputStream stream = new FileOutputStream(new File(
						bBinderDirectory, "/" + params[0] + ".png"));
				mNoteView.getBitmap().compress(CompressFormat.PNG, 80, stream);
				stream.close();
			} catch (IOException e) {
				Log.e("ckt", "Save file error");
				e.printStackTrace();
			}

			Document d = convertToPDF(name);
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

	public class SaveNote extends AsyncTask<String, Void, Void> {

		String	filepath;

		@Override
		protected Void doInBackground(String... strings) {
			filepath = strings[0];
			if (mDatabase.doesNoteExist(mNotebookId, mNotePageNumber)) {
				mDatabase.updateNote(mNotebookId, mNotePageNumber);
			} else {
				if (mNoteName == null)
					mDatabase.addNote(filepath, mNotebookId, mNotePageNumber);
				else
					mDatabase.addNote(mNoteName, filepath, mNotebookId,
							mNotePageNumber);
			}

			try {
				File bBinderDirectory = new File(BBINDERDIRECTORY);
				bBinderDirectory.mkdir();

				FileOutputStream stream = new FileOutputStream(new File(
						bBinderDirectory, "/" + filepath + ".png"));

				mNoteView.getBitmap().compress(CompressFormat.PNG, 80, stream);
				stream.close();
			} catch (IOException e) {
				Log.e("ckt", "Save file error");
				e.printStackTrace();
			}
			return null;
		}

	}
}
