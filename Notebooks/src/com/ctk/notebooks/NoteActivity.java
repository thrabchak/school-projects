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
import android.widget.Spinner;
import android.widget.Toast;

import com.ctk.notebooks.Utils.ColorPickerSwatch;
import com.ctk.notebooks.Utils.ColorPickerSwatch.OnColorSelectedListener;
import com.ctk.notebooks.Utils.LockableScrollView;
import com.ctk.notebooks.Utils.RandomStringGenerator;
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
	private Spinner					mStrokeWidthSpinner;
	private int						mDefaultStrokeSize;

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

		Toast.makeText(this, "" + mNotePageNumber, Toast.LENGTH_LONG).show();

		mNoteView.setPaintColor(0xFF000000);
		mNoteView.setPaintWidth(16);

		mScrollView.setScrollingLocked(true);

		mSelectedColor = 0;
		mDefaultStrokeSize = 5;
		initSwatches();
		initStrokeWidthSpinner();
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
							mNoteView.setEraser(false);

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
				mNoteView.setEraser(true);

				mSwatches[6].setChecked(true);
				for (int j = 0; j < NUM_SWATCHES; j++) {
					if (j != 6)
						mSwatches[j].setChecked(false);
				}
			}
		});

		mSwatches[mSelectedColor].setChecked(true);
	}

	private void initStrokeWidthSpinner() {
		mStrokeWidthSpinner = (Spinner) findViewById(R.id.pen_size_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.toolbar_sizes_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStrokeWidthSpinner.setAdapter(adapter);
		mStrokeWidthSpinner.setSelection(mDefaultStrokeSize, false);
		mStrokeWidthSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						mNoteView.setPaintWidth(pos);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Nothing - needed by default
					}
				});
		mNoteView.setPaintWidth(mDefaultStrokeSize);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 2468101, Menu.NONE, "Email PDF");
		menu.add(Menu.NONE, 1234568, Menu.NONE, "Save");
		menu.add(Menu.NONE, 1234, Menu.NONE, "Toolbar");
		menu.add(Menu.NONE, 4, Menu.NONE, "Lined Paper");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			saveFile();

			// On the ActionBar Up button pressed, allow the OS
			// to return us to this Activity's parent.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case 2468101:
			// TODO email useful file name
			email("test");
			return true;
		case 1234568:
			saveFile();
			return true;
		case 1234:
			mDrawerLayout.openDrawer(Gravity.END);
			return true;
		case 4:

			mNoteView.setmIsLinedPaper();
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
	public void saveFile() {
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
	protected void onStop() {
		// saveFile(mNoteView.getFileName());
		super.onStop();
	}

	public void email(String filename) {
		new EmailNote().execute(filename);
	}

	public class EmailNote extends AsyncTask<String, Void, Void> {
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
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			// can we get rid of this?-- Document doc = convertToPDF(name);
			emailIntent.setType("plain/text");
			Uri uri = Uri.fromFile(new File(BBINDERDIRECTORY + "/" + name
					+ ".pdf"));
			emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, name);
			if (pd != null) {
				pd.dismiss();
			}
			startActivity(Intent.createChooser(emailIntent,
					"Send your email in: "));
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
			Document d =convertToPDF(params[0]);
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

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (mDatabase.doesNoteExist(mNotebookId, mNotePageNumber)) {
				mDatabase.updateNote(mNotebookId, mNotePageNumber);
				Toast.makeText(mContext, "updated", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(mContext, "added", Toast.LENGTH_LONG).show();
				if (mNoteName == null)
					mDatabase.addNote(filepath, mNotebookId, mNotePageNumber);
				else
					mDatabase.addNote(mNoteName, filepath, mNotebookId,
							mNotePageNumber);
			}
		}
	}
}
