package org.yuttadhammo.tipitaka;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import android.util.Log;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import android.webkit.WebView;
import android.webkit.WebViewClient;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class EnglishActivity extends SherlockActivity {
	
	private String TAG = "EnglishActivity";
	
	private View english;
	
	private SharedPreferences zoomPref;
	private float zoom;
	
	private boolean firstPage = true;

	private static byte[] sBuffer = new byte[512];
	
	public String lang = "pali";
    private eBookmarkDBAdapter ebookmarkDBAdapter;
    private ProgressDialog downloadProgressDialog;
    private Handler handler = new Handler();
    public WebView ewv;

	private String ATI_PATH;

	private ActionBar actionBar;


	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);

		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
        zoomPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Log.d("Tipitaka", "No SDCARD");
			return;
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		ATI_PATH = prefs.getString("ati_dir", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ati_website");
		
		File file = new File(ATI_PATH, "start.html" );
		if (!file.exists()) {
			ATI_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ati_website";
			file = new File(Environment.getExternalStorageDirectory(), "ATI.zip" );
			if (file.exists()) {
				Downloader dl = new Downloader(this);
				dl.uncompressFile("ATI.zip");
			}
			else {
				startDownload(false);
			}
			return;
		}

		int api = Integer.parseInt(Build.VERSION.SDK);
		
		if (api >= 14) {
			this.getActionBar().setHomeButtonEnabled(true);
		}
		
		showActivity();

	}

	public void showActivity() {
        english =  View.inflate(this, R.layout.english, null);

        ewv  = (WebView) english.findViewById(R.id.ewv);

        ewv.getSettings().setJavaScriptEnabled(true); // enable javascript

        ewv.setWebViewClient(new MyWebViewClient());

		ewv.getSettings().setBuiltInZoomControls(true);
		ewv.getSettings().setSupportZoom(true);
		
		String url = "file://"+ATI_PATH+"/html/index.html";

		if(this.getIntent().getExtras() != null) {
			Bundle dataBundle = this.getIntent().getExtras();
			url = dataBundle.getString("url");
		}

        zoom = zoomPref.getFloat("english_zoom", 1f);
		
		//Log.d("Tipitaka", "Initial Zoom"+zoom);
        
        ewv.setInitialScale((int)(100*zoom));
		
		ewv.loadUrl(url);
		
		//~ ewv.loadDataWithBaseURL("", 
            //~ htmlContent, 
            //~ "text/html", 
            //~ "utf-8", 
            //~ null);
        setContentView(english);
		ebookmarkDBAdapter = new eBookmarkDBAdapter(this);
	}

    
    
    private boolean isInternetOn() {
	    ConnectivityManager cm =
		        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        return true;
		    }
		    return false;
    }


    private class ReadFile extends AsyncTask<String, Integer, String> {
        private String version;

		@Override
        protected String doInBackground(String... sUrl) {
            try {

            	Log.d(TAG, "reading remote ATI file...");
            	
            	
            	URL url = new URL(sUrl[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                connection.getContentLength();


                
                // download the file
                InputStream input = new BufferedInputStream(url.openStream());

	            ByteArrayOutputStream content = new ByteArrayOutputStream();

	            // Read response into a buffered stream
	            int readBytes = 0;
	            while ((readBytes = input.read(sBuffer)) != -1) {
	                content.write(sBuffer, 0, readBytes);
	            }
	            //rename to actual url
	            String contentString = content.toString();
	            
	            Pattern p = Pattern.compile("URL=([-/a-z0-9.]*\\.zip)");
	            Matcher m = p.matcher(contentString);

	            if (m.find())
	                version = m.group(1);

                input.close();
            } catch (Exception e) {
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloadProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
			if(downloadProgressDialog.isShowing()) {
				downloadProgressDialog.dismiss();
			}
    		Log.i("Tipitaka","File to download: "+version);

    		if(version == null) {
				Toast.makeText(EnglishActivity.this, getString(R.string.ati_error), Toast.LENGTH_SHORT).show();
    			return;
    		}
			String urlText = "http://www.accesstoinsight.org/tech/download/"+version;
    		
    		Log.i("Tipitaka","Downloading "+urlText);
        	Downloader dl = new Downloader(EnglishActivity.this);
        	dl.downloadFile(urlText, "ATI.zip");
        }

    }


    private void downloadFile() {
        downloadProgressDialog = new ProgressDialog(this);
        downloadProgressDialog.setCancelable(true);
        downloadProgressDialog.setMessage(getString(R.string.ati_version));
        downloadProgressDialog.setIndeterminate(true);

        ReadFile rf = new ReadFile();
        rf.execute("http://www.accesstoinsight.org/tech/download/zipfile.html");
    }
    
    
    @Override
    public boolean onSearchRequested() {
		Intent intent = new Intent(this, SearchDialog.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
    	return true;
    }

	private void startDownload(boolean close) {
    	
		Log.d(TAG, "starting ATI download...");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(getString(R.string.ati_not_found));
    	builder.setMessage(getString(R.string.confirm_download_ati));
    	builder.setCancelable(true);
    	
    	final boolean Close = close;
    	
    	builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(isInternetOn()) {
					downloadFile();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(EnglishActivity.this);
					builder.setTitle(getString(R.string.internet_not_connected));
					builder.setMessage(getString(R.string.check_your_connection));
					builder.setCancelable(false);
					builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
					builder.show();
				}
			}
		});
    	
    	builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(Close)
					finish();
			}
		});
    	
    	builder.show();
	}
	
	private void memoItem() {

		final Dialog memoDialog = new Dialog(EnglishActivity.this);
		memoDialog.setContentView(R.layout.memo_dialog);
		
		Button memoBtn = (Button)memoDialog.findViewById(R.id.memo_btn);
		final EditText memoText = (EditText)memoDialog.findViewById(R.id.memo_text);
		memoText.setText(lastTitle, TextView.BufferType.EDITABLE);
		
		memoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ebookmarkDBAdapter.open();
				eBookmarkItem bookmarkItem = new eBookmarkItem(memoText.getText().toString(), lastUrl);
				ebookmarkDBAdapter.insertEntry(bookmarkItem);
				ebookmarkDBAdapter.close();
				Toast.makeText(EnglishActivity.this, getString(R.string.memo_set), Toast.LENGTH_SHORT).show();
				memoDialog.dismiss();
			}
		});
		
		memoDialog.setCancelable(true);

		memoDialog.setTitle(getString(R.string.memoTitle));
		memoDialog.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		super.onOptionsItemSelected(item);	
		Intent intent;
		switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            finish();
	            return true;
			case (int)R.id.pali:
				intent = new Intent(this, SelectBookActivity.class);
				startActivity(intent);	
				break;
			case (int)R.id.memo:
				memoItem();
				break;
			case (int)R.id.forward:
				if (ewv != null && ewv.canGoForward()) {
					ewv.goForward();
				}
				break;
			case (int)R.id.home:
				if(ewv != null)
					ewv.loadUrl("file://"+ATI_PATH+"/html/index.html");
				break;
			case (int)R.id.update_archive:
				startDownload(false);
				break;
			case (int)R.id.dict_menu_item:
				intent = new Intent(this, DictionaryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case (int)R.id.read_bookmark:
				intent = new Intent(this, BookmarkEnglishActivity.class);
				Bundle dataBundle = new Bundle();
				dataBundle.putString("title", lastTitle);
				dataBundle.putString("url", lastUrl);
				intent.putExtras(dataBundle);
				startActivity(intent);	
				break;
			default:
				return false;
	    }
    	return true;
	}		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.english_menu, menu);
	    return true;

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if(ewv == null)
			return true;
		MenuItem forward = (MenuItem) menu.findItem(R.id.forward);
		if (!ewv.canGoForward())
			forward.setVisible(false);
		else 
			forward.setVisible(true);
	    return true;
	}

	private void replaceCSS() {
		String newFile = getTextContent("html/css/screen.css");

		if(newFile == null)
			return;
			
		newFile = newFile.replaceAll("width:680px;","");
		newFile = newFile.replaceAll("width:660px;","max-width: 660px");
		
		try{
			Log.i("Tipitaka","Modifying CSS");

			// backup file
			File src = new File(ATI_PATH, "html/css/screen.css" );
			File dest = new File(ATI_PATH, "html/css/screen.css.bkp" );
			src.renameTo(dest);
			
			src = new File(ATI_PATH, "html/css/screen.css" );

			FileOutputStream osr = new FileOutputStream(src);
			OutputStreamWriter osw = new OutputStreamWriter(osr); 

			// Write the string to the file
			osw.write(newFile);

			/* ensure that everything is
			* really written out and close */
			osw.flush();
			osw.close();
			Log.i("Tipitaka","CSS Modified");
		}
		catch(IOException ex) {
			Log.e("Tipitaka","Error modifying CSS: " + ex.toString());
		}


	}

    private String getTextContent(String fileName) {

		//Get the text file
		File file = new File(ATI_PATH,fileName);

		//Read text from file
		String text = "";

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text+=line+"\n";
			}
		}
		catch (IOException e) {
			Log.i("Tipitaka","get text error: " + e.toString());
			return null;
		}
		return text;
	}

	public String lastTitle;
	public String lastUrl;
	
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			float scale = view.getScale();
			if(firstPage)
				firstPage = false;
			else
				view.setInitialScale((int)(100*scale));
			
			if(zoom != scale) {
				zoom = scale;
				zoomPref.edit().putFloat("english_zoom", scale).commit();
			}
			Log.d("Tipitaka", "This Zoom"+zoom);

			EnglishActivity.this.lastTitle = view.getTitle();
			EnglishActivity.this.lastUrl = view.getUrl();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (ewv != null && keyCode == KeyEvent.KEYCODE_BACK && ewv.canGoBack()) {
			ewv.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onDestroy() {
		if(zoomPref != null && ewv != null)
			zoomPref.edit().putFloat("english_zoom", ewv.getScale()).commit();
		super.onDestroy();
	}
	@Override
	public void onPause(){
		if(zoomPref != null && ewv != null)
			zoomPref.edit().putFloat("english_zoom", ewv.getScale()).commit();
		super.onPause();
	}
	
}
