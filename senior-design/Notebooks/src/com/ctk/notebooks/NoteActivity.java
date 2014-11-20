package com.ctk.notebooks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

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
	private boolean					mSaveNote			= true;
	private LockableScrollView		mScrollView;
	private boolean					mIsNoteLined		= false;
	private String					mNotebookName;
	private int						mNotebookId;
	private int						mNotePageNumber;
	private DrawerLayout			mDrawerLayout;
	private DatabaseHelper			mDatabase;
	private final String			mNoteName			= null;
	private String					mFileName;
	private RandomStringGenerator	mRandomStringGenerator;
	private final int				NUM_SWATCHES		= 6;
	private int						mSelectedColor;
	private LinearLayout			mSecondarySwatchGroup;
	private final int				mSwatchColors[]		= { 0xff000000,
			0xff33b5e5, 0xffaa66cc, 0xffff4444, 0xffffbb33, 0xff99cc00, };
	ColorPickerSwatch				mSwatches[]			= new ColorPickerSwatch[NUM_SWATCHES];
	private final int				mSwatchIds[]		= { R.id.swatch_1,
			R.id.swatch_2, R.id.swatch_3, R.id.swatch_4, R.id.swatch_5,
			R.id.swatch_6,								};
	private ColorPickerSwatch		mSwatchMain;
	private int						mStrokeSize;
	private VerticalSeekBar			mVerticalSeekBar;
	private final int[]				strokeSizes			= { 2, 5, 8, 12, 20, 50 };
	private ImageView mToolPen;
	private ImageView mToolErase;
	private LinearLayout mSwatchMainGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_note);
		mNoteView = (NoteView) findViewById(R.id.note_view);
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

		if (getIntent().hasExtra("note_page_background_lined")) {
			mIsNoteLined = getIntent().getExtras().getBoolean(
					"note_page_background_lined", false);
		}
		
		initSwatches();
		mSwatchMainGroup = (LinearLayout)findViewById(R.id.swatch_main_group);
		
		mNoteView.setmIsLinedPaper(mIsNoteLined);		

		mNoteView.setPaintColor(0xFF000000);
		mNoteView.setPaintWidth(16);
		mNoteView.setDrawingLocked(false);

		mVerticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
		mVerticalSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						mStrokeSize = strokeSizes[progress];
						mNoteView.setPaintWidth(strokeSizes[progress]);
					}
				});

		mNoteView.setPaintColor(0xFF000000);
		mStrokeSize = 5;
		mNoteView.setPaintWidth(5);
		mSelectedColor = 0;
		
		mToolPen = (ImageView) findViewById(R.id.tool_pen);
		mToolPen.setActivated(true);
		mToolPen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mToolPen.setActivated(true);
				mToolErase.setActivated(false);
				mSwatchMainGroup.setVisibility(View.VISIBLE);
				mSecondarySwatchGroup.setVisibility(View.GONE);
			}
		});
		mToolErase = (ImageView) findViewById(R.id.tool_eraser);
		mToolErase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mToolPen.setActivated(false);
				mToolErase.setActivated(true);
				mSwatchMainGroup.setVisibility(View.GONE);
				mSecondarySwatchGroup.setVisibility(View.GONE);
			}
		});
	}

	private void initSwatches() {
		mSecondarySwatchGroup = (LinearLayout) mDrawerLayout
				.findViewById(R.id.swatch_secondary_group);
		mSecondarySwatchGroup.setVisibility(View.GONE);
		mSwatchMain = (ColorPickerSwatch) mDrawerLayout
				.findViewById(R.id.swatch_main);
		mSwatchMain.setColor(0xfffffff); // starting color black
		mSwatchMain.setOnColorSelectedListener(new OnColorSelectedListener() {

			@Override
			public void onColorSelected(int color) {
				mSwatchMain.setVisibility(View.GONE);
				mSecondarySwatchGroup.setVisibility(View.VISIBLE);
			}
		});
		for (int i = 0; i < NUM_SWATCHES; i++) {
			final int temp = i;
			mSwatches[i] = (ColorPickerSwatch) mDrawerLayout
					.findViewById(mSwatchIds[i]);
			mSwatches[i].setColor(mSwatchColors[i]);
			mSwatches[i]
					.setOnColorSelectedListener(new OnColorSelectedListener() {

						@Override
						public void onColorSelected(int color) {
							mSwatches[temp].setChecked(true);
							mSelectedColor = color;
							mNoteView.setPaintColor(color);
							mNoteView.setPaintWidth(mStrokeSize);

							for (int j = 0; j < NUM_SWATCHES; j++) {
								if (j != temp)
									mSwatches[j].setChecked(false);
							}
							mSwatchMain.setColor(color);
							mSecondarySwatchGroup.setVisibility(View.GONE);
							mSwatchMain.setVisibility(View.VISIBLE);
						}
					});
		}
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
			Time t = new Time();
			t.setToNow();
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, name + "\n"
					+ t.format3339(false));
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
				Bitmap  b = mNoteView.getBitmap();

				FileOutputStream stream = new FileOutputStream(new File(
						bBinderDirectory, "/" + filepath + ".png"));
				b.compress(CompressFormat.PNG, 80, stream);
				
				b = getResizedBitmap(b, 624, (int) (624.0/11*8.5));
				FileOutputStream stream2 = new FileOutputStream(new File(
						bBinderDirectory, "/" + filepath + "_t.png"));
				b.compress(CompressFormat.PNG, 80, stream2);
				
				stream.close();
				stream2.close();
			} catch (IOException e) {
				Log.e("ckt", "Save file error");
				e.printStackTrace();
			}
			return null;
		}
		
		public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		    int width = bm.getWidth();
		    int height = bm.getHeight();
		    float scaleWidth = ((float) newWidth) / width;
		    float scaleHeight = ((float) newHeight) / height;
		    // CREATE A MATRIX FOR THE MANIPULATION
		    Matrix matrix = new Matrix();
		    // RESIZE THE BIT MAP
		    matrix.postScale(scaleWidth, scaleHeight);

		    // "RECREATE" THE NEW BITMAP
		    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		    return resizedBitmap;
		}

	}
}
