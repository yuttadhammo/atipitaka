package org.yuttadhammo.tipitaka;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ListView;


public class SelectBookActivity extends Activity {
	private int selectedSet = 0;
	private View main;
	public String lang = "pali";
	public String thisTitle;
    private Gallery setGallery; //= (Gallery) findViewById(R.id.gallery_cate);
    private Gallery heirGallery;
	private ListView volumeList;

	private SharedPreferences prefs;  
    private SearchDialog searchDialog = null;
    
    private int hierC = 0;
    
	private SelectBookActivity context;
	private String[] volumeNumbers;
    private int scriptIndex;


    @SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.context = this;

        main =  View.inflate(this, R.layout.main, null);
        setContentView(main);
        
        final Context context = getApplicationContext();
        prefs =  PreferenceManager.getDefaultSharedPreferences(context);
        
		int api = Build.VERSION.SDK_INT;
		
		if (api >= 14) {
			this.getActionBar().setHomeButtonEnabled(true);
		}
        
        setGallery = (Gallery) main.findViewById(R.id.gallery_cate);
        heirGallery = (Gallery) main.findViewById(R.id.gallery_hier);
        volumeList = (ListView) main.findViewById(R.id.vol_list);
        
        //TextView cautionText = (TextView) findViewById(R.id.caution);
        //cautionText.setText(Html.fromHtml(getString(R.string.caution)));
        
        //TextView limitationText = (TextView) findViewById(R.id.limitation);
        //limitationText.setText(Html.fromHtml(getString(R.string.limitation)));
        final Resources res = getResources();

		final int[] ncate0 = res.getIntArray(R.array.lengths_0);
		final int[] ncate1 = res.getIntArray(R.array.lengths_1);
		final int[] ncate2 = res.getIntArray(R.array.lengths_2);
		final int[] ncate3 = res.getIntArray(R.array.lengths_3);
        
        setGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectedSet = arg2+1;
				
				int[] ncate = ncate0;
				switch(arg2) {
					case 0:
						ncate = ncate0;
						break;
					case 1:
						ncate = ncate1;
						break;
					case 2:
						ncate = ncate2;
						break;
					case 3:
						ncate = ncate3;
						break;
				}

				String [] t_ncate = new String [ncate[hierC]];				
				
				//Log.i("Tipitaka","Number of books: "+ncate[hierC]);		
				
				for(int i=0; i<ncate[hierC]; i++) {
					t_ncate[i] = Integer.toString(i+1);
				}
				//Log.i("Tipitaka","item selected: "+arg2);		
		        setVolumeList();
		        		        
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				return;	
			}
        });
       
        heirGallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				hierC = arg2;
				
				int[] ncate = ncate0;
				switch(selectedSet) {
					case 1:
						ncate = ncate0;
						break;
					case 2:
						ncate = ncate1;
						break;
					case 3:
						ncate = ncate2;
						break;
					case 4:
						ncate = ncate3;
						break;
				}

				String [] t_ncate = new String [ncate[hierC]];				
				
				
				for(int i=0; i<ncate[hierC]; i++) {
					t_ncate[i] = Integer.toString(i+1);
				}
				        
		        setVolumeList();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
					
			}
        });
        
        volumeList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int position = Integer.parseInt(volumeNumbers[arg2]);
				SharedPreferences.Editor editor = prefs.edit();
				int pos1 = setGallery.getSelectedItemPosition();
				editor.putInt("Position1", pos1);				
				switch(pos1) {
					case 0:
						editor.putInt("VPosition", position);						
						break;
					case 1:
						editor.putInt("SPosition", position);						
						break;
					case 2:
						editor.putInt("APosition", position);						
						break;
				}				
				editor.apply();
        		Intent intent = new Intent(context, ReadBookActivity.class);
        		Bundle dataBundle = new Bundle();
        		dataBundle.putInt("VOL", position);
        		dataBundle.putInt("PAGE", 0);
        		dataBundle.putString("LANG", lang);
        		dataBundle.putString("TITLE", thisTitle);
        		dataBundle.putString("FIRSTPAGE", "TRUE");
        		intent.putExtras(dataBundle);
        		startActivity(intent);						
			}
        	
        });
 
    }


	@Override
    public boolean onSearchRequested() {
		Intent intent = new Intent(this, SearchDialog.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
    	return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		//super.onOptionsItemSelected(item);	
		//Log.i("Tipitaka","Menu clicked ID: " + item.getItemId() + " vs. "+ R.id.preferences_menu_item);
		Intent intent;
		switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;	    	
			case (int)R.id.bookmark_menu_item:
				intent = new Intent(this, BookmarkPaliActivity.class);
				Bundle dataBundle = new Bundle();
				dataBundle.putString("LANG", lang);
				intent.putExtras(dataBundle);
				startActivity(intent);	
				break;
			case (int)R.id.preferences_menu_item:
				intent = new Intent(this, SettingsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case (int)R.id.search_menu_item:
				intent = new Intent(this, SearchDialog.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case (int)R.id.help_menu_item:
				intent = new Intent(this, HelpActivity.class);
				startActivity(intent);
				break;
			case (int)R.id.dict_menu_item:
				intent = new Intent(this, DictionaryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case (int)R.id.english_menu_item:
				intent = new Intent(this, EnglishActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case (int)R.id.quiz_menu_item:
				intent = new Intent(this, QuizActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			default:
				return false;
	    }
    	return true;
	}		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.select_menu, menu);
	    /*
	    if (lang.equals("thai")) {
	    	menu.getItem(0).setTitle(getString(R.string.select_lang) + getString(R.string.pl_lang));
	    } else if(lang.equals("pali")) {
	    	menu.getItem(0).setTitle(getString(R.string.select_lang) + getString(R.string.th_lang));
	    }*/
	    
	    return true;
	}		
	

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_SEARCH) {
			/*
			Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
			if(lang.equals("thai")) {
				toast = Toast.makeText(this, getString(R.string.find_thai), Toast.LENGTH_LONG);
			}
			else if(lang.equals("pali")) {
				toast = Toast.makeText(this, getString(R.string.find_pali), Toast.LENGTH_LONG);
			}
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 70);
			toast.show();
			*/
			return false;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}
	
	
	//~ private void changeHeader() {
		//~ String header = getString(R.string.th_tipitaka_book).trim() + " " + Utils.arabic2thai(Integer.toString(selectedBook), getResources());
		//~ textHeader.setText(header);
		//~ if(lang.equals("thai")) {
			//~ textHeaderLang.setText(getString(R.string.th_lang));
		//~ }
		//~ else if(lang.equals("pali")) {
			//~ textHeaderLang.setText(getString(R.string.pl_lang));
		//~ }
			//~ 
	//~ }
	

	@Override
	protected void onRestart() {
		super.onRestart();
		//changeHeader();
		if(searchDialog != null) {
			searchDialog.updateHistoryList();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        scriptIndex = Integer.parseInt(prefs.getString("text_script","0"));

        Resources res = getResources();

        final String [] cnames = res.getStringArray(R.array.category);
        for(int idx = 0; idx < cnames.length; idx++) {
            cnames[idx] = PaliUtils.translit(cnames[idx],scriptIndex);
        }
        final String [] hnames = res.getStringArray(R.array.hnames);
        for(int idx = 0; idx < hnames.length; idx++) {
            hnames[idx] = PaliUtils.translit(hnames[idx],scriptIndex);
        }

        ArrayAdapter<String> adapter0 = new TipitakaGalleryAdapter(this, R.layout.my_gallery_item_0, hnames);
        heirGallery.setAdapter(adapter0);

        ArrayAdapter<String> adapter1 = new TipitakaGalleryAdapter(this, R.layout.my_gallery_item_1, cnames);
        setGallery.setAdapter(adapter1);

        setVolumeList();

        main.setBackgroundColor(prefs.getInt("background_color", getResources().getColor(R.color.text_color_back)));
	}
    
    protected void setVolumeList() {
    	Resources res = getResources();
    	volumeNumbers = res.getStringArray(R.array.vin_m_list);
		switch(selectedSet) {
			case 1:
				switch(hierC) {
					case 0:
						break;
					case 1:
						volumeNumbers = res.getStringArray(R.array.vin_a_list);
						break;
					case 2:
						volumeNumbers = res.getStringArray(R.array.vin_t_list);
						break;
				}
				break;
			case 2:
				switch(hierC) {
					case 0:
						volumeNumbers = res.getStringArray(R.array.sut_m_list);
						break;
					case 1:
						volumeNumbers = res.getStringArray(R.array.sut_a_list);
						break;
					case 2:
						volumeNumbers = res.getStringArray(R.array.sut_t_list);
						break;
				}
				break;
			case 3:
				switch(hierC) {
					case 0:
						volumeNumbers = res.getStringArray(R.array.abhi_m_list);
						break;
					case 1:
						volumeNumbers = res.getStringArray(R.array.abhi_a_list);
						break;
					case 2:
						volumeNumbers = res.getStringArray(R.array.abhi_t_list);
						break;
				}
				break;
			case 4:
				switch(hierC) {
					case 0:
						volumeNumbers = res.getStringArray(R.array.etc_m_list);
						break;
					case 1:
						volumeNumbers = res.getStringArray(R.array.etc_a_list);
						break;
					case 2:
						volumeNumbers = res.getStringArray(R.array.etc_t_list);
						break;
				}
				break;
			default:
				break;
		}

		ArrayAdapter<String> adapter = new VolumeItemAdapter(context, R.layout.volume_item, R.id.volume, volumeNumbers);
		volumeList.setAdapter(adapter);
    }
}
