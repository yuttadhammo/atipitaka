
package org.yuttadhammo.tipitaka;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.database.DatabaseUtils;
import android.content.ContentValues;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;


public class DictionaryActivity extends SherlockActivity {
	//private DBHelper dbh;
	private MainTipitakaDBAdapter db;

	private static final String	HTML_KEY	= "html";
	
	static final String LOOKUP_TEXT_IS_FOCUSED_KEY = "lookup_textisFocused";

	private static final String	LOOKUP_TEXT_KEY	= "lookup_text";

	private static final String	WORD_KEY	= "word";
	
	private static final String	DICT_KEY	= "dict";  // 0 = CPED, 1 = DPPN, 2 = PED
	
	private static final String[] DICT_ARRAY = {"PED","CPED","CEPD","DPPN","MULTI"};  // 0 = CPED, 1 = DPPN, 2 = PED
	private static final String[] TABLE_ARRAY = {"ped","cped","cepd","dppn"};  // 0 = CPED, 1 = DPPN, 2 = PED
	
	private int dict = 0;  // 0 = CPED, 1 = DPPN, 2 = PED

	private String html, word;
	private EditText lookupText;
	private Button lookupButton;
	
	private SharedPreferences prefs;
	private WebView wv;

	private String TAG = "DictionaryActivity";

	private ActionBar actionBar;

	@SuppressLint("NewApi")
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
        db = new MainTipitakaDBAdapter(this);

        try {
        	db.open();
        	if(db.isOpened()) {
        		db.close();
        	} else {
            	Downloader dl = new Downloader(this);
            	dl.startDownloader("http://static.sirimangalo.org/pali/ATPK/ATPK.zip", "ATPK.zip");
        		return;
        	}
        } catch (SQLiteException e) {
			Log.e ("Tipitaka","error:", e);
        	Downloader dl = new Downloader(this);
        	dl.startDownloader("http://static.sirimangalo.org/pali/ATPK/ATPK.zip", "ATPK.zip");
        	return;
        }
        
		setContentView (R.layout.cped);
		prefs = getPreferences (MODE_PRIVATE);
		
		wv            = (WebView) findViewById (R.id.webview);
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setSupportZoom(true);

		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		lookupText   = (EditText) findViewById (R.id.lookup_text);
		lookupButton = (Button) findViewById (R.id.lookup_button);
		
		lookupText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		        	lookupWord();
		            return true;
		        }
		        return false;
		    }
		});
		
		dict = prefs.getInt (DICT_KEY, 0); // default to PED

		word = prefs.getString (WORD_KEY, "");
		
		if (prefs.getBoolean (LOOKUP_TEXT_IS_FOCUSED_KEY, true)) {
			lookupText.requestFocus ();
		} else {
			wv.requestFocus ();
		}
		
		lookupButton.setOnClickListener (new OnClickListener() {
			@Override
			public void onClick (View v) {
				lookupWord ();
			}
		});
		


		displayWebViewHtml (
			prefs.getString (HTML_KEY, loadResToString (R.raw.index))
		);
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) {
			String received = null;
			if(extras.containsKey("word"))
				received = extras.getString("word");
			else if (extras.containsKey(Intent.EXTRA_TEXT))
				received = extras.getCharSequence(Intent.EXTRA_TEXT).toString();
			if(received != null) {
				if(extras.containsKey("dict")) {
					dict = extras.getInt("dict");
					SharedPreferences.Editor ed = prefs.edit ();
					ed.putInt (DICT_KEY, extras.getInt("dict"));
					ed.commit();
				}
				else 
					dict = 4;
				
				setTitleWithMessage (received);
				lookupText.setText(PaliUtils.toVel(received));
				wv.setSelected (true);
				lookupWord ();
			}
		}
		else
			setTitleWithMessage (word);
		int api = Integer.parseInt(Build.VERSION.SDK);
		
		if (api >= 14) {
			this.getActionBar().setHomeButtonEnabled(true);
		}
	}


	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.cped_menu, menu);
	    
	    Menu sub = menu.findItem(R.id.menu_dict).getSubMenu();
	    sub.setGroupCheckable(R.id.group_dict, true, true);
		sub.getItem(dict).setChecked(true);
	    
	    return true;
	}



	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		if(item.isCheckable()) {
			item.setChecked(true);
			SharedPreferences.Editor ed = prefs.edit ();
			switch(item.getItemId()) {
		        case R.id.menu_PED:
					dict = 0;
					break;
				case R.id.menu_CPED:
					dict = 1;
					break;
				case R.id.menu_CEPD:
					dict = 2;
					break;
				case R.id.menu_DPPN:
					dict = 3;
					break;
				case R.id.menu_MULTI:
					dict = 4;
					break;
			}
			this.word = null;
			ed.putInt (DICT_KEY, dict);
			ed.commit ();
			lookupWord();
		}
		else {
			Intent intent;
			switch (item.getItemId()) {
		        case android.R.id.home:
		            // app icon in action bar clicked; go home
		        	finish();
		            return true;
				case R.id.menu_english:
					intent = new Intent(this, EnglishActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					break;
	
				case R.id.menu_top:
					if(wv != null)
						wv.scrollTo(0,0);
					break;
/*				case R.id.menu_plus:
					wv.zoomIn();
					break;
				case R.id.menu_minus:
					wv.zoomOut();
					break;*/
			}
		}
		
		return super.onOptionsItemSelected (item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		this.word = null;
		return true;
	}
	
	@Override
	protected void onPause () {
		super.onPause ();
		if(prefs == null)
			return;
		SharedPreferences.Editor ed = prefs.edit();
		ed.putString (WORD_KEY, word);
		ed.putString (HTML_KEY, html);
		ed.putString (LOOKUP_TEXT_KEY, lookupText.getText ().toString ());
		ed.putBoolean (LOOKUP_TEXT_IS_FOCUSED_KEY, lookupButton.isFocused ());
		ed.commit ();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}

	}

	
	
	private void displayLoadingPage () {
		displayWebViewHtml (loadResToString (R.raw.searching));
		setTitleWithMessage (null);
	}
	
	private void displayResult (String word, String raw ) {
		
		String htmlout = "<html><head><style> @font-face { font-family: verajjan; src: url('file:///android_asset/verajjan.ttf'); } @font-face { font-family: verajjab; src: url('file:///android_asset/verajjab.ttf'); } body{font-family:verajjan; background-color:white;} .title{font-family:verajjab; color:#5A5;font-weight:bold} div.title{font-size:150%; margin-top:24px;}</style></head><body><p style=\"font-family:verajjan\">"+raw+"</p></body></html>";
		
		displayWebViewHtml (htmlout);
		//lookup_text.setText (word);
		setTitleWithMessage (word);
		wv.requestFocus ();
	}
	
	private void displayWebViewHtml (String html) {
		this.html = html;
    	wv.loadDataWithBaseURL ("", html, null, "utf-8", null);
	}


	private String loadResToString (int resId) {

		try {
			int read;
			byte[] buffer = new byte[4096];
			ByteArrayOutputStream baos = new ByteArrayOutputStream ();
			InputStream is = DictionaryActivity.this.getResources ().openRawResource (resId);

			while (0 < (read = is.read (buffer))) {
				baos.write (buffer, 0, read);
			}

			baos.close ();
			is.close ();

			String data = baos.toString ();

			//Log.i (Global.TAG, "ResourceUtils loaded resource to string: " + resId);

			return data;
		} catch (Exception e) {
			//Log.e (Global.TAG, "ResourceUtils failed to load resource to string", e);
			return null;
		}
	}

	
	private void lookupWord () {
		lookupWord (lookupText.getText ().toString ());
	}

	private void lookupWord (String query) {
		query = query.replaceAll("^ +","").replaceAll(" +$","").replace("*","%").replaceAll("%$","");
		if ((this.word != null && this.word.equals (query)) || query == "") {
			return;
		}
		query = PaliUtils.toVel(query);
		
		query = query.toLowerCase();
		
		this.word = query;
		
		displayLoadingPage ();
		
		String[] tables;

		// multi
		
		if(dict == 4) {
			tables = new String[]{"ped","cped","dppn"};
		}
		else {
			tables = new String[]{TABLE_ARRAY[dict]};
		}

		String html = "";
		int count = 0;
		db.open();
		for(String table:tables) {
			if(count == 2)
				count = 3;
			Cursor c = db.dictQuery(table,query);		
			html += parse(c, query, dict == 4?count++:dict);
		}
		db.close();
		displayResult (query, html);
	}

	public String parse (Cursor c, String query, int table){
		Log.i ("Tipitaka", "parsing results for dict "+DICT_ARRAY[table]);
    	if(c == null || c.getCount() == 0) {
    		Log.i ("Tipitaka", "No results for dict "+DICT_ARRAY[table]+"... parsing endings");
    		return parseEndings(query, table);
    	}
		try {
			String raw = "";
			ContentValues cvs = new ContentValues();
			int idx = 0;
			int count = c.getCount();
			String[] entries = new String[count];
			String[] texts = new String[count]; 
			
			c.moveToFirst();
			do {
				DatabaseUtils.cursorRowToContentValues(c, cvs);	    
				
				entries[idx] = PaliUtils.toUni(cvs.getAsString("entry"));
				texts[idx] = cvs.getAsString("text");
				idx++;
				
			}
			while(c.moveToNext());
			c.close();
			
			raw +="<div style=\"font-weight:bold; font-size:125%; margin-bottom:24px; font-family:verajjab\">"+count+" "+(count == 1?"entry":"entries")+" for "+PaliUtils.toUni(this.word)+" in "+DICT_ARRAY[table]+":</div><hr/>";
			
			if(table == 0 || table == 3) {
				raw += "<table width=\"100%\"><tr><td valign=\"top\"><table>";
				
				idx=0;
				while(idx<entries.length) {
					raw+= "<tr><td><a href=\"#"+table+entries[idx]+"\" style=\"text-decoration:none; font-weight:bold; font-size:125%; margin:10px 0; color:#5A5;font-family:verajjab\">"+entries[idx++].replace("^"," ")+"</b></td></tr>";
				}

				raw += "</table></td></tr></table><hr/>";
			}
			
			idx=0;
			while(idx<entries.length) {
				String thisText = texts[idx];
				if(table == 3) { // fudge for DPPN colors
					thisText = thisText.replaceAll("^([^<]*<[^>]*)>","$1 style='color:#5A5;font-family:verajjab'>");
				}
				raw+= ((table == 1 || table == 2)?"<b style=\"color:#5A5;font-family:verajjab\">"+entries[idx]+"</b>: ":"<a name=\""+table+entries[idx]+"\">")+thisText+((table == 1 || table == 2)?"<br/>":"<hr/>");
				idx++;
			}
			
			return raw + ((table == 1 || table == 2)?"<hr/>":"");
		}
		catch(Exception e) {
			Log.e ("cped", "failed to load entry: " + e);
			return "<div style=\"font-weight:bold; font-size:125%; margin-bottom:24px; font-family:verajjab\">No results for "+this.word+" in "+DICT_ARRAY[table]+".</div><hr/>";
		}
    }

	private String parseEndings(String query, int table) {
		String[] declensions = getResources().getStringArray(R.array.declensions);
		ArrayList<String> endings = new ArrayList<String>(); 
		for(String declension : declensions) {
			String[] decArray = TextUtils.split(declension, ",");
			String ending = decArray[0];
			int offset = Integer.parseInt(decArray[1]);
			int min = Integer.parseInt(decArray[2]);
			String add = decArray[3];
			//Log.d ("cped", "checking ending: " + ending);
			if(query.length() > min && query.endsWith(ending)) {
				endings.add(TextUtils.substring(query, 0, query.length()-ending.length()+offset)+add);
				//Log.d ("cped", "adding ending: " + endings.get(endings.size()-1));
			}
		}
		if(endings.isEmpty())
			return "<div style=\"font-weight:bold; font-size:125%; margin-bottom:24px; font-family:verajjab\">No results for "+this.word+" in "+DICT_ARRAY[table]+".</div><hr/>";;

		String endstring = "'"+TextUtils.join("','", endings)+"'";
		Log.d ("cped", "endings: " + endstring);

		Cursor c = db.dictQueryEndings(TABLE_ARRAY[table],endstring);	
    	if(c == null || c.getCount() == 0)
			return "<div style=\"font-weight:bold; font-size:125%; margin-bottom:24px; font-family:verajjab\">No results for "+this.word+" in "+DICT_ARRAY[table]+".</div><hr/>";
    	else
    		return parse(c, query, table);
	}


	private void setTitleWithMessage (String m) {
		if (m == null || m.equals (""))
			setTitle (getResources ().getText (R.string.app_name));
		else
			setTitle (getResources ().getText (R.string.app_name) + " - " + m + " in " + DICT_ARRAY[dict]);
	}
	
	public String[] convertStreamToString(int[] tno) throws IOException {
		String[] texts = new String[tno.length];
		try {
			InputStream is = this.getAssets().open(TABLE_ARRAY[dict]);

			BufferedReader r = new BufferedReader(new InputStreamReader(is));

			String line;

			Log.i ("cped", tno.length+" entries found");

			int readerCtr = 0;
			int lineCtr = 0;
			int idx = 0;
			while (readerCtr < tno.length)   {
				line = r.readLine();
				if (lineCtr == tno[readerCtr]) {
					Log.i ("cped", tno[readerCtr]+" "+lineCtr);
					readerCtr++;
					texts[idx++] = line;
				}
				lineCtr++;
			}
			Log.i ("cped", "lookup completed");
			return texts;

		} catch (java.io.IOException e) {
			return texts;
		}
	}
	
}
