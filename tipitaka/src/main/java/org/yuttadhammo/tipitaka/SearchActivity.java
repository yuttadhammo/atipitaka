package org.yuttadhammo.tipitaka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Typeface;


public class SearchActivity extends SherlockActivity {
    /** Called when the activity is first created. */
	private MainTipitakaDBAdapter mainTipitakaDBAdapter = null;
	private Handler handler = new Handler();
	//private Button btSearch;
	//private TextView output;
	//private EditText text;
	private ArrayList<String> resultList = new ArrayList<String>();
	private ProgressDialog pdialog;
	//private long start_time;
	private TextView statusText;
	private TextView searchText;
	private ListView resultView;
	private View divider1;
	private View divider2;
	private String savedQuery;
	private String selCate;
	private MatrixCursor savedCursor;
	private SpecialCursorAdapter adapter;
	private TableLayout table;
	private int pVinai = 0;
	private int pSuttan = 0;
	private int pAbhi = 0;
	private int pEtc = 0;
	private int suVinai = 0;
	private int suSuttan = 0;
	private int suAbhi = 0;
	private int suEtc = 0;
	private int firstPosVinai = Integer.MAX_VALUE;
	private int firstPosSuttan = Integer.MAX_VALUE;
	private int firstPosAbhi = Integer.MAX_VALUE;
	private int firstPosEtc = Integer.MAX_VALUE;
	private Intent intent;
	private float line1Size = 12f;
	private float line2Size = 12f;
	private SharedPreferences prefs;	

	private SearchHistoryDBAdapter searchHistoryDBAdapter;
	private SearchResultsDBAdapter searchResultsDBAdapter;
	private BookmarkDBAdapter bookmarkDBAdapter;
	
	private SearchResultsItem savedResultsItem;
	private long savedResultsItemPosition;
	
	public String lang = "pali";
	
	private static final int SHOW_READBOOK_ACTIVITY = 1;
	private static final int SHOW_BOOKMARK_ACTIVITY = 2;
	
	private static final int V_BOOKS_M = 6;
	private static final int S_BOOKS_M = 43;
	private static final int A_BOOKS_M = 14;
	private static final int E_BOOKS_M = 9;
	
	private static final int V_BOOKS_A = 6;
	private static final int S_BOOKS_A = 36;
	private static final int A_BOOKS_A = 7;
	private static final int E_BOOKS_A = 2;
	
	private static final int V_BOOKS_T = 7;
	private static final int S_BOOKS_T = 22;
	private static final int A_BOOKS_T = 7;
	private static final int E_BOOKS_T = 0;

	private boolean b1;
	private boolean b2;
	private boolean b3;
	private boolean b4;
	private boolean b5;
	private boolean b6;
	private boolean b7;
	
	private String [] readPages = null;

	private Typeface font;
	private ActionBar actionBar;
    private View main;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main =  View.inflate(this, R.layout.results_list, null);


        searchHistoryDBAdapter = new SearchHistoryDBAdapter(this);
    	searchResultsDBAdapter = new SearchResultsDBAdapter(this);
    	bookmarkDBAdapter = new BookmarkDBAdapter(this);
    	searchHistoryDBAdapter.open();        
        
        setContentView(main);

		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
      
        Context context = getApplicationContext();
        prefs =  PreferenceManager.getDefaultSharedPreferences(context);

		font = Typeface.createFromAsset(getAssets(), "verajjan.ttf");
        
        line1Size = prefs.getFloat("Line1Size", 12f);        
        line2Size = prefs.getFloat("Line2Size", 12f);        
        
        statusText = (TextView) this.findViewById(R.id.result_status);

        TextView vinaiLabel = (TextView) SearchActivity.this.findViewById(R.id.vinai_label);
        vinaiLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosVinai != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosVinai);
				}
			}
		});
        
        TextView vinaiLabel2 = (TextView) findViewById(R.id.npage1);
        vinaiLabel2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosVinai != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosVinai);
				}
			}
		});

        TextView vinaiLabel3 = (TextView) findViewById(R.id.nsutt1);
        vinaiLabel3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosVinai != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosVinai);
				}
			}
		});
        
        
        TextView suttanLabel = (TextView) findViewById(R.id.suttan_label);
        suttanLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosSuttan != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosSuttan);
				}
			}
		});
        
        TextView suttanLabel2 = (TextView) findViewById(R.id.npage2);
        suttanLabel2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosSuttan != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosSuttan);
				}
			}
		});

        TextView suttanLabel3 = (TextView) findViewById(R.id.nsutt2);
        suttanLabel3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosSuttan != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosSuttan);
				}
			}
		});
        
        
        TextView abhiLabel = (TextView) findViewById(R.id.abhi_label);
        abhiLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosAbhi != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosAbhi);
				}
			}
		});

        TextView abhiLabel2 = (TextView) findViewById(R.id.npage3);
        abhiLabel2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosAbhi != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosAbhi);
				}
			}
		});

        TextView abhiLabel3 = (TextView) findViewById(R.id.nsutt3);
        abhiLabel3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosAbhi != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosAbhi);
				}
			}
		});        
        
        
        
        TextView etcLabel = (TextView) findViewById(R.id.etc_label);
        etcLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosEtc != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosEtc);
				}
			}
		});

        TextView etcLabel2 = (TextView) findViewById(R.id.npage4);
        etcLabel2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosEtc != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosEtc);
				}
			}
		});

        TextView etcLabel3 = (TextView) findViewById(R.id.nsutt4);
        etcLabel3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(firstPosEtc != Integer.MAX_VALUE) {
					resultView.setSelected(true);
					resultView.setSelection(firstPosEtc);
				}
			}
		});        
        
        searchText = (TextView) findViewById(R.id.search_word);
        resultView = (ListView) findViewById(R.id.result_list);
        table = (TableLayout) findViewById(R.id.table_layout);
        divider1 = findViewById(R.id.result_divider_1);
        divider2 = findViewById(R.id.result_divider_2);


        // long click action
        resultView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
				
				String starLabel;
				final Integer position = new Integer(arg2);
				if (adapter.isMarked(position)) {
					starLabel = getString(R.string.unmarked);
				} else {
					starLabel = getString(R.string.marked);
				}
				
				final CharSequence[] items = {
						getString(R.string.unread), 
						getString(R.string.unread_all), 
						getString(R.string.memo),
						getString(R.string.delete_memo),
						starLabel};
				
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String [] tokens = resultList.get(position).split(":");
						final int volume = Integer.parseInt(tokens[1]);
						final int page = Integer.parseInt(tokens[2]);
						final int item = Integer.parseInt(tokens[3].split("\\s+")[0]);
						AlertDialog.Builder builder;
						switch(which) {
							case 0: // unread
								adapter.clearClickedPosition(position);
								updateClickedStatusData(savedResultsItemPosition, savedResultsItem);
								break;
							case 1: // unread all
								builder = new AlertDialog.Builder(SearchActivity.this);   
								builder.setTitle(getString(R.string.unread_all_items));
								builder.setIcon(android.R.drawable.ic_dialog_alert);
								builder.setMessage(getString(R.string.confirm_unread_all_items));
								builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										adapter.clearClickedPosition();
										updateClickedStatusData(savedResultsItemPosition, savedResultsItem);
									}
								});
								builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										return;
									}
								});
								builder.show();
								break;
							case 2: // memo
								//Toast.makeText(SearchActivity.this, resultList.get(position), Toast.LENGTH_SHORT).show();
								memoAt(volume, item, page ,lang, savedQuery, position);
								break;
							case 3: // delete memo
								bookmarkDBAdapter.open();
								int memoCount = bookmarkDBAdapter.getEntries(lang, volume, page, savedQuery).getCount();
								bookmarkDBAdapter.close();
								if (memoCount > 0) {
									builder = new AlertDialog.Builder(SearchActivity.this);   
									builder.setTitle(getString(R.string.delete_memo));
									builder.setIcon(android.R.drawable.ic_dialog_alert);
									builder.setMessage(getString(R.string.confirm_delete_memo));
									builder.setPositiveButton(getString(R.string.yes), 
											new DialogInterface.OnClickListener() {									
										@Override
										public void onClick(DialogInterface dialog, int which) {
											deleteMemoAt(lang, volume, page, savedQuery, position);
											Toast.makeText(SearchActivity.this, R.string.deleted_memo, 
													Toast.LENGTH_SHORT).show();
										}
									});
									builder.setNegativeButton(getString(R.string.no), 
											new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											return;
										}
									});
									builder.show();
									
								} else {
									adapter.clearSavedPosition(position);
									updateClickedStatusData(savedResultsItemPosition, savedResultsItem);										
									Toast.makeText(SearchActivity.this, 
											R.string.memo_not_found, Toast.LENGTH_SHORT).show();
								}
								break;
							case 4:
								toggleStar(position);
								break;
						}
					}
				});	
				builder.show();
				return false;
			}
		});
        
        // click action
        resultView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String [] tokens = ((String) resultList.get(arg2)).split(":");
				int volume = Integer.parseInt(tokens[1]);
				int page = Integer.parseInt(tokens[2]);
				Log.i("Tipitaka","search result clicked: "+volume+" "+page);
				
				//Toast.makeText(SearchPage.this, Integer.toString(arg2) + ":" + Long.toString(arg3), Toast.LENGTH_SHORT).show();
				
				adapter.addClickedPosition(arg2);				
				try {
					String pClicked = Utils.toStringBase64(adapter.getPrimaryClicked());
					savedResultsItem.setPrimaryClicked(pClicked);
					searchResultsDBAdapter.open();
					searchResultsDBAdapter.updateEntry(savedResultsItemPosition, savedResultsItem);
					searchResultsDBAdapter.close();
				} catch(IOException e) {
					e.printStackTrace();
				}				
				
        		Intent intent = new Intent(SearchActivity.this, ReadBookActivity.class);
        		Bundle dataBundle = new Bundle();
        		dataBundle.putInt("VOL", volume);
        		dataBundle.putInt("PAGE", page);
        		dataBundle.putString("LANG", lang);
        		dataBundle.putString("QUERY", savedQuery);
        		
        		intent.putExtras(dataBundle);
        		startActivityForResult(intent,SHOW_READBOOK_ACTIVITY);								
				
			}
        	
        });
        
		intent = getIntent();
		Bundle dataBundle = intent.getExtras();
		if(dataBundle != null && dataBundle.containsKey("QUERY") && dataBundle.containsKey("LANG") && !dataBundle.containsKey("CONTENT")) {

			final String query = dataBundle.getString("QUERY");
			final String lang = dataBundle.getString("LANG");

			b1 = dataBundle.getBoolean("b1");
			b2 = dataBundle.getBoolean("b2");
			b3 = dataBundle.getBoolean("b3");
			b4 = dataBundle.getBoolean("b4");
			b5 = dataBundle.getBoolean("b5");
			b6 = dataBundle.getBoolean("b6");
			b7 = dataBundle.getBoolean("b7");
			
			if((b1 | b2 | b3 | b4) && (b5 | b6 | b7)) {
				doSearch(query, lang);						
			}

		} else if (dataBundle != null && dataBundle.containsKey("LANG") && dataBundle.containsKey("QUERY")  && dataBundle.containsKey("CONTENT")) {
			String content = dataBundle.getString("CONTENT");
			String pClicked = dataBundle.getString("PCLICKED");
			String sClicked = dataBundle.getString("SCLICKED");
			String saved = dataBundle.getString("SAVED");
			String marked = dataBundle.getString("MARKED");
			String sCate = dataBundle.getString("SCATE");
						
			savedQuery = dataBundle.getString("QUERY");
			lang = dataBundle.getString("LANG");
			resultList.clear();
			
			// save result item and its position
			searchResultsDBAdapter.open();
			Cursor cursor = searchResultsDBAdapter.getEntries(lang, savedQuery, sCate);
			if(cursor.getCount() > 0 && cursor.moveToFirst()) {
				savedResultsItemPosition = cursor.getInt(SearchResultsDBAdapter.ID_COL);
				savedResultsItem = searchResultsDBAdapter.getEntry(savedResultsItemPosition);
			}
			cursor.close();
			searchResultsDBAdapter.close();
			
	        try {
	        	resultList = (ArrayList<String>) Utils.fromStringBase64(content);
	        	ArrayList<Integer> pClickedList = (ArrayList<Integer>)Utils.fromStringBase64(pClicked);
	        	ArrayList<Integer> sClickedList = (ArrayList<Integer>)Utils.fromStringBase64(sClicked);
	        	ArrayList<Integer> savedList = (ArrayList<Integer>)Utils.fromStringBase64(saved);
	        	ArrayList<Integer> markedList = (ArrayList<Integer>)Utils.fromStringBase64(marked);
				showResults(resultList, false, savedQuery, pClickedList, sClickedList, savedList, markedList);
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	        	e.printStackTrace();
	        }
		}
    }
	
	// for highlighting selected items
	private class SpecialCursorAdapter extends SimpleCursorAdapter {
		private ArrayList<Integer> checkClicked = new ArrayList<Integer>();
		private ArrayList<Integer> checkSecondaryClicked = new ArrayList<Integer>();
		private ArrayList<Integer> checkSaved = new ArrayList<Integer>();
		private ArrayList<Integer> checkMarked = new ArrayList<Integer>();
		private int posVinai = Integer.MAX_VALUE;
		private int posSuttan = Integer.MAX_VALUE;
		private int posAbhi = Integer.MAX_VALUE;
		private int posEtc = Integer.MAX_VALUE;
		public SpecialCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			//Toast.makeText(SearchPage.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
			
			TextView line1 = (TextView)view.findViewById(R.id.line1);
			float textSize = Float.parseFloat(prefs.getString("base_text_size", "16"));
			line1.setTextSize(textSize);
			TextView line2 = (TextView)view.findViewById(R.id.line2);
			line2.setTextSize(textSize);

			line1.setTypeface(font);				
			line2.setTypeface(font);
			
			line2.setText(line2.getText().toString().replace("<br/>", " "));

					
			if(checkClicked.contains(position)) {
				line1.setBackgroundColor(Color.LTGRAY);
				line2.setBackgroundColor(Color.LTGRAY);
			} else if(checkSecondaryClicked.contains(position)) {
				line1.setBackgroundColor(Color.LTGRAY);
				line2.setBackgroundColor(Color.LTGRAY);
			}
			else {
				line1.setBackgroundColor(Color.TRANSPARENT);
				line2.setBackgroundColor(Color.TRANSPARENT);
			}

			if(checkSaved.contains(position)) {
				line1.setBackgroundColor(Color.rgb(25, 25, 90));
				line2.setBackgroundColor(Color.rgb(25, 25, 90));
			}
			
			if(position >= posVinai && position < posSuttan && position < posAbhi) {				
				line1.setTextColor(Color.argb(255, 30, 144, 255));
			} else if(position >= posSuttan && position < posAbhi) {
				line1.setTextColor(Color.argb(255, 255, 69, 0));
			} else if(position >= posAbhi && position < posEtc) {
				line1.setTextColor(Color.argb(255, 160, 32, 240));
			} else if(position >= posEtc) {
				line1.setTextColor(Color.argb(255, 00, 150, 00));
			}

			return view;			
		}

		public void addMarkedPosition(Integer position) {
			if(!checkMarked.contains(position)) {
				checkMarked.add(position);
				this.notifyDataSetChanged();
			}
		}
		
		public void addClickedPosition(Integer position) {
			if (!checkClicked.contains(position)) {				
				checkClicked.add(position);
				this.notifyDataSetChanged();
			}	
		}
		
		public void addSecondaryClickedPosition(Integer position) {
			if (!checkSecondaryClicked.contains(position)) {				
				checkSecondaryClicked.add(position);
				this.notifyDataSetChanged();
			}				
		}
		
		public void addSavedPosition(Integer position) {
			if(!checkSaved.contains(position)) {
				checkSaved.add(position);
				this.notifyDataSetChanged();
			}
		}
		
		public ArrayList<Integer> getPrimaryClicked() {
			return checkClicked;
		}
		
		public ArrayList<Integer> getSecondaryClicked() {
			return checkSecondaryClicked;
		}
		
		public ArrayList<Integer> getSaved() {
			return checkSaved;
		}
		
		public ArrayList<Integer> getMarked() {
			return checkMarked;
		}
		
		public void setPrimaryClicked(ArrayList<Integer> al) {
			checkClicked = al;
			this.notifyDataSetChanged();
		}
		
		public void setSecondaryClicked(ArrayList<Integer> al) {
			checkSecondaryClicked = al;
			this.notifyDataSetChanged();
		}
		
		public void setSaved(ArrayList<Integer> al) {
			checkSaved = al;
			this.notifyDataSetChanged();
		}
		
		public void setMarked(ArrayList<Integer> al) {
			checkMarked = al;
			this.notifyDataSetChanged();
		}
		
		public boolean isMarked(Integer position) {
			return checkMarked.contains(position);
		}
		
		public void clearClickedPosition(Integer position) {
			checkClicked.remove(position);
			checkSecondaryClicked.remove(position);
			this.notifyDataSetChanged();
		}
		
		public void clearMarkedPosition(Integer position) {
			checkMarked.remove(position);
			this.notifyDataSetChanged();
		}
		
		public void clearClickedPosition() {
			checkClicked.clear();
			checkSecondaryClicked.clear();
			this.notifyDataSetChanged();
		}
		
		public void clearSavedPosition(Integer position) {
			checkSaved.remove(position);
			this.notifyDataSetChanged();
		}
		
		public void setVinaiPosition(int position) {
			posVinai = position;
		}
		
		public void setSuttanPosition(int position) {
			posSuttan = position;
		}		

		public void setAbhiPosition(int position) {
			posAbhi = position;
		}
		public void setEtcPosition(int position) {
			posEtc = position;
		}
		
	}

	
	private void showResults(ArrayList<String> _resultList, boolean isSaved, String keywords, ArrayList<Integer> pList, ArrayList<Integer> sList, ArrayList<Integer> savedList, ArrayList<Integer> markedList) {
		Log.i("Tipitaka","showing search results");
		
        savedCursor = convertToCursor(_resultList,keywords);
        adapter = new SpecialCursorAdapter(SearchActivity.this, R.layout.result_item, savedCursor,
        		new String[] {"line1", "line2"},
        		new int[] {R.id.line1, R.id.line2});
        if(pList != null) {
        	adapter.setPrimaryClicked(pList);
        }
        
        if(sList != null) {
        	adapter.setSecondaryClicked(sList);
        }
   
        if(savedList != null) {
        	adapter.setSaved(savedList);
        }
        
        if(markedList != null) {
        	adapter.setMarked(markedList);
        }
        
        adapter.setVinaiPosition(firstPosVinai);
        adapter.setSuttanPosition(firstPosSuttan);
        adapter.setAbhiPosition(firstPosAbhi);
        adapter.setEtcPosition(firstPosEtc);
        
        TextView p1 = (TextView) findViewById(R.id.npage1);
        TextView p2 = (TextView) findViewById(R.id.npage2);
        TextView p3 = (TextView) findViewById(R.id.npage3);
        TextView p4 = (TextView) findViewById(R.id.npage4);
        
        p1.setText(Integer.toString(pVinai));
        p2.setText(Integer.toString(pSuttan));
        p3.setText(Integer.toString(pAbhi));
        p4.setText(Integer.toString(pEtc));
        
        TextView s1 = (TextView) findViewById(R.id.nsutt1);
        TextView s2 = (TextView) findViewById(R.id.nsutt2);
        TextView s3 = (TextView) findViewById(R.id.nsutt3);
        TextView s4 = (TextView) findViewById(R.id.nsutt4);
        
        s1.setText(Integer.toString(suVinai));
        s2.setText(Integer.toString(suSuttan));
        s3.setText(Integer.toString(suAbhi));
        s4.setText(Integer.toString(suEtc));

        if(isSaved) {

            // save search history

        	Log.i("Tipitaka","saving search history");
    		
            String tmp = ""+(pVinai+pSuttan+pAbhi+pEtc) + " " + getString(R.string.sections);
            tmp += " " + ""+(suVinai+suSuttan+suAbhi+suEtc) + " " + getString(R.string.volumes);
            String line1 = String.format("(%s)", tmp);

            if(b5) {
            	line1 += " M ";
            }
            if(b6) {
            	line1 += " A ";
            }
            if(b7) {
            	line1 += " T ";
            }
            
            String line2 = "";
            if(b1) {
            	line2 += String.format("%s(%s/%s)  ", 
            			getString(R.string.ss_vinai), 
            			pVinai+"", 
            			suVinai+"");
            			
            }
            if(b2) {
            	line2 += String.format("%s(%s/%s)  ", 
            			getString(R.string.ss_suttan), 
            			pSuttan+"",
            			suSuttan+"");
            }
            if(b3) {
            	line2 += String.format("%s(%s/%s) ", 
            			getString(R.string.ss_abhi), 
            			pAbhi+"", 
            			suAbhi+"");
            }
            if(b4) {
            	line2 += String.format("%s(%s/%s) ", 
            			getString(R.string.ss_etc), 
            			pEtc+"", 
            			suEtc+"");
            }            
            
            searchHistoryDBAdapter.open();
    	    SearchHistoryItem item1 = new SearchHistoryItem(lang, keywords, pVinai+pSuttan+pAbhi+pEtc, suVinai+suSuttan+suAbhi+suEtc, selCate, line1, line2);
    	    if(!searchHistoryDBAdapter.isDuplicated(item1)) {
    	    	searchHistoryDBAdapter.insertEntry(item1);
    	    }
            searchHistoryDBAdapter.close();


            // save search results
	        searchResultsDBAdapter.open();
	        	
	        SearchResultsItem item2 = new SearchResultsItem(lang, keywords, 
	        		pVinai+":"+pSuttan+":"+pAbhi+":"+pEtc,
	        		suVinai+":"+suSuttan+":"+suAbhi+":"+suEtc,selCate);
	        if(!searchResultsDBAdapter.isDuplicated(item2)) {
	        	savedResultsItemPosition = searchResultsDBAdapter.insertEntry(item2);
	        	//Toast.makeText(this, "SAVE"+":"+savedResultsItemPosition, Toast.LENGTH_SHORT).show();
	        } else {
	        	Cursor cursor = searchResultsDBAdapter.getEntries(lang, keywords, selCate);
	        	if(cursor.getCount() > 0 && cursor.moveToFirst()) {
	        		savedResultsItemPosition = cursor.getInt(SearchResultsDBAdapter.ID_COL);

	        	}
	        	cursor.close();
	        }
	        savedResultsItem = item2;

	        searchResultsDBAdapter.close();    	        
        }
        
        searchText.setText("\"" + keywords + "\"");
		
		if(_resultList.size() > 0) {
			statusText.setText(getString(R.string.th_found) + 
					" " + Integer.toString(_resultList.size()) + 
					" " + getString(R.string.sections) + 
					" " + getString(R.string.in) + 
					" " + Integer.toString(suVinai+suSuttan+suAbhi+suEtc) +
					" " + getString(R.string.volumes));
		} else {
			statusText.setText(getString(R.string.not_found));
		}
            	        
        table.setVisibility(View.VISIBLE);
        divider1.setVisibility(View.VISIBLE);
        divider2.setVisibility(View.VISIBLE);
        
        resultView.setAdapter(adapter);
		
	}
		
	private Runnable doUpdateGUI = new Runnable() {
    	public void run() {
    		pdialog.incrementProgressBy(1);  
    		pdialog.setMessage(getString(R.string.th_found)+" "+Integer.toString(resultList.size())+" "+getString(R.string.sections));
    		if(pdialog.getProgress() == pdialog.getMax()) {
    			pdialog.dismiss();
    			showResults(resultList, true, savedQuery, null, null, null, null);
    		}
    	}
    };

	public class QueryAllThread implements Runnable {
		private String query;
		private ArrayList<String> resultList;
		private boolean vinai = true;
		private boolean suttan = true;
		private boolean abhidham = true;
		private boolean etc = true;
		
		private boolean mul = true;
		private boolean att = true;
		private boolean tik = true;
		
		public QueryAllThread(String query, ArrayList<String> resultList) {
			this.query = query;
			this.resultList = resultList;
		}

		public QueryAllThread(String query, ArrayList<String> resultList, boolean vinai, boolean suttan, boolean abhidham, boolean etc, boolean mul, boolean att, boolean tik) {
			this.query = query;
			this.resultList = resultList;
			this.vinai = vinai;
			this.suttan = suttan;
			this.abhidham = abhidham;
			this.etc = etc;
			
			this.mul = mul;
			this.att = att;
			this.tik = tik;
		}
		
		private void search(String vols) {
			int vol = Integer.parseInt(vols);
			mainTipitakaDBAdapter.open();
    		Cursor cursor = mainTipitakaDBAdapter.search(vol, this.query, lang);    		
    		cursor.moveToFirst();
    		while(cursor.isAfterLast() == false) {
    			this.resultList.add(cursor.getString(0)+":"+cursor.getString(1)+":"+cursor.getString(2)+":"+cursor.getString(3));
    			cursor.moveToNext();
    		}
    		cursor.close();
    		mainTipitakaDBAdapter.close();
    		handler.post(doUpdateGUI);							
		}
		
		@Override
		public void run() {
			final Resources res = getResources();

			String [] volumes;

			if(vinai) {
				if(mul) {
					volumes = res.getStringArray(R.array.vin_m_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
				if(att) {
					volumes = res.getStringArray(R.array.vin_a_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
				if(tik) {
					volumes = res.getStringArray(R.array.vin_t_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
			}
			if(suttan) {
				if(mul) {
					volumes = res.getStringArray(R.array.sut_m_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
				if(att) {
					volumes = res.getStringArray(R.array.sut_a_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
				if(tik) {
					volumes = res.getStringArray(R.array.sut_t_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
			}
			if(abhidham) {
				if(mul) {
					volumes = res.getStringArray(R.array.abhi_m_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
				if(att) {
					volumes = res.getStringArray(R.array.abhi_a_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
				if(tik) {
					volumes = res.getStringArray(R.array.abhi_t_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
			}
			if(etc) {
				if(mul) {
					volumes = res.getStringArray(R.array.etc_m_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
				if(att) {
					volumes = res.getStringArray(R.array.etc_a_list);
					for(int i=0; i<volumes.length; i++) {
						search(volumes[i]);
					} 
				}
				if(tik) {

				}
			}
    	}
	}
	
	private MatrixCursor filterCursor(MatrixCursor cursor, boolean vFlag, boolean sFlag, boolean aFlag) {
		final String [] matrix = { "_id", "line1", "line2" };
		MatrixCursor newCursor = new MatrixCursor(matrix);

		int rowId = 0;
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			String line2 = cursor.getString(1);
			if(vFlag && line2.startsWith(getString(R.string.vinai_full))) {
				newCursor.addRow(new Object[] { rowId++, cursor.getString(0), cursor.getString(1)});
			} else if(sFlag && line2.startsWith(getString(R.string.suttan_full))) {
				newCursor.addRow(new Object[] { rowId++, cursor.getString(0), cursor.getString(1)});
			} else if(aFlag && line2.startsWith(getString(R.string.abhi_full))) {
				newCursor.addRow(new Object[] { rowId++, cursor.getString(0), cursor.getString(1)});
			}
			cursor.moveToNext();
		}
		
		return newCursor;
	}
	
	private MatrixCursor convertToCursor(ArrayList<String> results, String query) {
		final String [] matrix = { "_id", "line1", "line2" };
		MatrixCursor cursor = new MatrixCursor(matrix);
		final Resources res = this.getResources();
		pVinai = 0;
		pSuttan = 0;
		pAbhi = 0;
		pEtc = 0;
		suVinai = 0;
		suSuttan = 0;
		suAbhi = 0;
		suEtc = 0;
		firstPosVinai = Integer.MAX_VALUE;
		firstPosSuttan = Integer.MAX_VALUE;
		firstPosAbhi = Integer.MAX_VALUE;
		firstPosEtc = Integer.MAX_VALUE;
		
		ArrayList<String> al_tmp = new ArrayList<String>();
		
		String [] bnames = res.getStringArray(R.array.volume_names);
		
		int key = 0;
		
		boolean vFound = false;
		boolean sFound = false;
		boolean aFound = false;
		boolean eFound = false;
					
		Log.i("Tipitaka","Parsing search results");
		
		for (Iterator<String> it = results.iterator(); it.hasNext();) {
			String item = it.next();
			String [] tokens = item.split(":");
			String vol = tokens[1];
			int voli = Integer.parseInt(tokens[1]);
			
			
			if(Arrays.asList(res.getStringArray(R.array.vin_m_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.vin_a_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.vin_t_list)).contains(vol)) {
				pVinai++;
				if(!vFound) {
					vFound = true;
					firstPosVinai = key;				
				}
			}
			else if(Arrays.asList(res.getStringArray(R.array.sut_m_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.sut_a_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.sut_t_list)).contains(vol)) {
				pSuttan++;
				if(!sFound) {
					sFound = true;
					firstPosSuttan = key;				
				}
			}
			else if(Arrays.asList(res.getStringArray(R.array.abhi_m_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.abhi_a_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.abhi_t_list)).contains(vol)) {
				pAbhi++;
				if(!aFound) {
					aFound = true;
					firstPosAbhi = key;				
				}
			}
			else if(Arrays.asList(res.getStringArray(R.array.etc_m_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.etc_a_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.etc_t_list)).contains(vol)) {
				pEtc++;
				if(!eFound) {
					eFound = true;
					firstPosEtc = key;				
				}
			}
			
			String sVol = Integer.toString(voli+1);
			for(String sut : tokens[1].split("\\s+")) {
				if(! al_tmp.contains(sVol+":"+sut)) {
					al_tmp.add(sVol+":"+sut);
					if(Arrays.asList(res.getStringArray(R.array.vin_m_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.vin_a_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.vin_t_list)).contains(vol)) {
						suVinai++;
					}
					else if(Arrays.asList(res.getStringArray(R.array.sut_m_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.sut_a_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.sut_t_list)).contains(vol)) {
						suSuttan++;
					}
					else if(Arrays.asList(res.getStringArray(R.array.abhi_m_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.abhi_a_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.abhi_t_list)).contains(vol)) {
						suAbhi++;
					}
					else if(Arrays.asList(res.getStringArray(R.array.etc_m_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.etc_a_list)).contains(vol) || Arrays.asList(res.getStringArray(R.array.etc_t_list)).contains(vol)) {
						suEtc++;
					}
					// count only one time 
				}
				break;
			}
			
			int summaryBuffer = 20; 
			String summary = "";
			String content = tokens[3].replaceAll("^\\[[0-9]+\\]","").replaceAll("\\^a\\^[^^]*\\^ea\\^","").replaceAll("\\^b\\^","").replaceAll("\\^eb\\^","");
			int startQuery = content.indexOf(query);
			if(startQuery > -1) {
				if(startQuery < summaryBuffer)
					startQuery = summaryBuffer;
				int endQuery = startQuery+query.length()+summaryBuffer;
				if(content.length() < endQuery)
					endQuery = content.length();
				summary = content.substring(startQuery-summaryBuffer,endQuery);
			}
			
			String line2 =  summary;
			
			String [] ts = tokens[2].split("\\s+");

			String l2p = Integer.toString((Integer.parseInt(ts[ts.length-1])+1));

			String t_items;
			if(ts.length > 1) {
				t_items = ts[0] + "-" + l2p;
			} else {
				t_items = l2p;
			}
			
			String tmp = "";
			int count = 0;
			
			
			for(String t: bnames[voli].trim().split("\\s+")) {
				if(count==4)
					break;
				tmp = tmp + t + " ";
				count++;
			}
			String line1 = Integer.toString(key+1) + ". " + tmp + " " + getString(R.string.th_page_label) + " " + t_items;
			cursor.addRow(new Object[] { key++, line1, line2});
		}				
		return cursor;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//if(dbhelper != null && dbhelper.isOpened()) {
		//	dbhelper.close();
		//}
		
		//if(searchHistoryDBAdapter != null) {
		//	searchHistoryDBAdapter.close();
		//}
	}
	
	private void doSearch(String _query, String _lang) {
		divider1.setVisibility(View.INVISIBLE);
		divider2.setVisibility(View.INVISIBLE);

		savedQuery = _query;
		lang = _lang;
    	
        //SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, 
        //		ATPKSearchSuggestionProvider.AUTHORITY, ATPKSearchSuggestionProvider.MODE);
        //suggestions.saveRecentQuery(savedQuery, null);
    	
        //dbhelper = new DataBaseHelper(SearchActivity.this);
		mainTipitakaDBAdapter = new MainTipitakaDBAdapter(SearchActivity.this);
        //dbhelper.openDataBase();
        resultList.clear();
        //start_time = System.currentTimeMillis();

        pdialog = new ProgressDialog(SearchActivity.this);
		pdialog.setCancelable(false);
		pdialog.setMessage(getString(R.string.th_searching));
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdialog.setProgress(0);
		
		// convert selected categories into string
		selCate = "";
		if(b1) {
			selCate += "1";
		} else {
			selCate += "0";
		}

		if(b2) {
			selCate += "1";
		} else {
			selCate += "0";
		}
		
		if(b3) {
			selCate += "1";
		} else {
			selCate += "0";
		}
		
		if(b4) {
			selCate += "1";
		} else {
			selCate += "0";
		}

		
		Thread searchThread = new Thread(new QueryAllThread(savedQuery, resultList, b1 ,b2, b3, b4,b5, b6, b7));
		searchThread.start();
		
		int maxSearch = 0;

		if(b1) {
			if(b5)
				maxSearch += V_BOOKS_M;
			if(b6)
				maxSearch += V_BOOKS_A;
			if(b7)
				maxSearch += V_BOOKS_T;
		}
		if(b2) {
			if(b5)
				maxSearch += S_BOOKS_M;
			if(b6)
				maxSearch += S_BOOKS_A;
			if(b7)
				maxSearch += S_BOOKS_T;
		}
		if(b3) {
			if(b5)
				maxSearch += A_BOOKS_M;
			if(b6)
				maxSearch += A_BOOKS_A;
			if(b7)
				maxSearch += A_BOOKS_T;
		}
		if(b4) {
			if(b5)
				maxSearch += E_BOOKS_M;
			if(b6)
				maxSearch += E_BOOKS_A;
			if(b7)
				maxSearch += E_BOOKS_T;
		}
		Log.i("Tipitaka", "maxSearch: "+maxSearch);
		pdialog.setMax(maxSearch);
		if(maxSearch > 0) {
			pdialog.show();
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle dataBundle = data.getExtras();
		if(requestCode == SHOW_READBOOK_ACTIVITY && dataBundle != null) {
			readPages = dataBundle.getStringArray("READ_PAGES");
			if(readPages != null) {
				for(String s : readPages) {
					String [] tokens = s.split(":");
					int volume = Integer.parseInt(tokens[0]);
					int page = Integer.parseInt(tokens[1]);
					int position = 0;
					for(String result : resultList) {
						String [] items = result.split(":");
						if(volume == Integer.parseInt(items[1]) && page == Integer.parseInt(items[2])) {
							adapter.addSecondaryClickedPosition(position);
							try {
								String sClicked = Utils.toStringBase64(adapter.getSecondaryClicked());
								savedResultsItem.setSecondaryClicked(sClicked);
								searchResultsDBAdapter.open();
								searchResultsDBAdapter.updateEntry(savedResultsItemPosition, savedResultsItem);
								searchResultsDBAdapter.close();
							} catch(IOException e) {
								e.printStackTrace();
							}
						}
						position++;
					}
				}
			}
		} else if(requestCode == SHOW_BOOKMARK_ACTIVITY && dataBundle != null) {
			String [] removedItems = dataBundle.getStringArray("REMOVED_ITEMS");
			ArrayList<Integer> newSaved = adapter.getSaved();
			int position = 0;
			//Toast.makeText(SearchActivity.this, removedItems.toString(), Toast.LENGTH_SHORT).show();
			for(String result: resultList) {
				String [] tokens = result.split(":");
				String key = String.format("%d:%d", Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])); // volume : page
				Log.i("KEY", key);
				for(String rItem: removedItems) {
					Log.i("ITEM", rItem);
					if(rItem.equals(key)) {
						newSaved.remove(new Integer(position));
						break;
					}
				}
				position++;
			}
			adapter.setSaved(newSaved);
			try {
				savedResultsItem.setSaved(Utils.toStringBase64(newSaved));
				searchResultsDBAdapter.open();
				searchResultsDBAdapter.updateEntry(savedResultsItemPosition, savedResultsItem);
				searchResultsDBAdapter.close();			
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        main.setBackgroundColor(prefs.getInt("text_color_back", getResources().getColor(R.color.text_color_back)));

        line1Size = prefs.getFloat("Line1Size", 12f);        
        line2Size = prefs.getFloat("Line2Size", 12f);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
        line1Size = prefs.getFloat("Line1Size", 12f);        
        line2Size = prefs.getFloat("Line2Size", 12f);
	}
	
	
	private void updateClickedStatusData(long position, SearchResultsItem item) {
		try {
			String pClicked = Utils.toStringBase64(adapter.getPrimaryClicked());
			String sClicked = Utils.toStringBase64(adapter.getPrimaryClicked());
			String mClicked = Utils.toStringBase64(adapter.getSaved());
			item.setPrimaryClicked(pClicked);
			item.setSecondaryClicked(sClicked);
			item.setSaved(mClicked);

			searchResultsDBAdapter.open();
			searchResultsDBAdapter.updateEntry(position, item);
			searchResultsDBAdapter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}
	

	public void toggleStar(int _position) {
		if(adapter.isMarked(_position)) {
			adapter.clearMarkedPosition(_position);
		} else {
			adapter.addMarkedPosition(_position);
		}
		
		ArrayList<Integer> newMarked = adapter.getMarked();		
		
		try {
			savedResultsItem.setMarked(Utils.toStringBase64(newMarked));
			searchResultsDBAdapter.open();
			searchResultsDBAdapter.updateEntry(savedResultsItemPosition, savedResultsItem);
			searchResultsDBAdapter.close();			
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void deleteMemoAt(String _language, int _volume, int _page, String _keywords, int _position) {
		bookmarkDBAdapter.open();
		Cursor c = bookmarkDBAdapter.getEntries(lang, _volume, _page, _keywords);
		c.moveToFirst();
		while(!c.isAfterLast()) {
			bookmarkDBAdapter.removeEntry(c.getInt(0));
			c.moveToNext();
		}
		bookmarkDBAdapter.close();
		adapter.clearSavedPosition(_position);
		updateClickedStatusData(savedResultsItemPosition, savedResultsItem);		
	}
	
	private void memoAt(int _volume, int _item, int _page, String _language, String _keywords, int _position) {
		final Dialog memoDialog = new Dialog(SearchActivity.this);
		memoDialog.setContentView(R.layout.memo_dialog);
		
		final Button memoBtn = (Button)memoDialog.findViewById(R.id.memo_btn);
		final EditText memoText = (EditText)memoDialog.findViewById(R.id.memo_text);
		
		final int volume = _volume;
		final int item = _item;
		final int page = _page;
		final String language = _language;
		final String keywords = _keywords;
		final int position = _position;
		
		memoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bookmarkDBAdapter.open();
				BookmarkItem bookmarkItem = new BookmarkItem(language, volume, page, item, memoText.getText().toString(), keywords);
				
				if(!bookmarkDBAdapter.isDuplicated(bookmarkItem)) {
					bookmarkDBAdapter.insertEntry(bookmarkItem);
					// update list status
					adapter.addSavedPosition(position);
					try {
						String saved = Utils.toStringBase64(adapter.getSaved());
						savedResultsItem.setSaved(saved);
						searchResultsDBAdapter.open();
						searchResultsDBAdapter.updateEntry(savedResultsItemPosition, savedResultsItem);
						searchResultsDBAdapter.close();
					} catch(IOException e) {
						e.printStackTrace();
					}														
					Toast.makeText(SearchActivity.this, getString(R.string.memo), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SearchActivity.this, getString(R.string.duplicated_item), Toast.LENGTH_SHORT).show();
				}
				bookmarkDBAdapter.close();
				memoDialog.dismiss();
			}
		});
		
		memoDialog.setCancelable(true);
		String title1 = getString(R.string.th_tipitaka_label) + " " + getString(R.string.pl_lang);
		

		memoDialog.setTitle(title1);
		memoDialog.show();		
		
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		super.onOptionsItemSelected(item);
		SharedPreferences.Editor editor = prefs.edit();
		
		switch (item.getItemId()) {
	        case android.R.id.home:
	            finish();
	            return true;

			case (int)R.id.zoom_in_result:
				line1Size=prefs.getFloat("Line1Size", 12f)+1;
				line2Size=prefs.getFloat("Line2Size", 12f)+1;
				editor.putFloat("Line1Size", line1Size);
				editor.putFloat("Line2Size", line2Size);
				editor.commit();
				adapter.notifyDataSetChanged();
				return true;
			case (int)R.id.zoom_out_result:
				line1Size=prefs.getFloat("Line1Size", 12f)-1;
				line2Size=prefs.getFloat("Line2Size", 12f)-1;
				editor.putFloat("Line1Size", line1Size);
				editor.putFloat("Line2Size", line2Size);
				editor.commit();
				adapter.notifyDataSetChanged();
				return true;
			case (int)R.id.jump_to_result_item:
				final Dialog dialog = new Dialog(SearchActivity.this);
				dialog.setContentView(R.layout.goto_position_dialog);
				dialog.setTitle(R.string.goto_result_position);
				((Button)dialog.findViewById(R.id.goto_position_btn)).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String input = ((EditText)dialog.findViewById(R.id.goto_position_edittext)).getText().toString();
						resultView.setSelected(true);
						resultView.setSelection(Integer.parseInt(input)-1);						
						dialog.dismiss();
					}
				});
				dialog.show();
				return true;
			case (int)R.id.results_bookmark:
				Intent intent = new Intent(SearchActivity.this, BookmarkPaliActivity.class);
				Bundle dataBundle = new Bundle();
				dataBundle.putString("LANG", lang);
				intent.putExtras(dataBundle);
				startActivity(intent);
				return true;
			default:
				return false;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.search_menu, menu);
	    
	    return true;
	}		
	    
    
    /*
    private void hideKeyboard() {
    	InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }*/
	
}