package org.yuttadhammo.tipitaka;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import com.actionbarsherlock.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import android.widget.CheckBox;

import android.graphics.Typeface;

public class SearchDialog extends SherlockActivity {

	private boolean b1;
	private boolean b2;
	private boolean b3;
	private boolean b4;
	private boolean b5;
	private boolean b6;
	private boolean b7;

	private EditText searchText;
	private EditText codeText;
	private EditText numberText;
	private InputMethodManager imm;
	private ListView historyList;
	private Context context;
	private String lang = "thai";
	private SearchHistoryDBAdapter searchHistoryDBAdapter;
	private SearchResultsDBAdapter searchResultsDBAdapter;
	private ResultsCursorAdapter historyAdapter;
	private Dialog historyItemDialog;
	private Cursor savedCursor;
	private String sortKey;
	private boolean isDesc = false;
	private SharedPreferences prefs;	
	private View searchView;
	private Typeface font;
	private MainTipitakaDBAdapter db;
	private ActionBar actionBar;
	private CheckBox cbm;
	private CheckBox cba;
	private CheckBox cbt;
		   	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        searchView =  View.inflate(this, R.layout.search_dialog, null);
        setContentView(searchView);

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

		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
      
        context = this;
		
		prefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sortKey = prefs.getString("SORT_KEY", SearchHistoryDBAdapter.KEY_KEYWORDS);
		isDesc = prefs.getBoolean("IS_DESC", false);
		searchView.setBackgroundColor(prefs.getInt("background_color", getResources().getColor(R.color.text_color_back)));

		font = Typeface.createFromAsset(getAssets(), "verajjan.ttf");
		
		cbm = (CheckBox) searchView.findViewById(R.id.cb_mul);
		cba = (CheckBox) searchView.findViewById(R.id.cb_att);
		cbt = (CheckBox) searchView.findViewById(R.id.cb_tik);
		
		cbm.setTypeface(font);
		cba.setTypeface(font);
		cbt.setTypeface(font);
		
		searchHistoryDBAdapter = new SearchHistoryDBAdapter(this);
    	searchResultsDBAdapter = new SearchResultsDBAdapter(this);

		Button queryBtn = (Button) findViewById(R.id.query_btn);
		queryBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchText();
			}
		});
		
		codeText = (EditText)findViewById(R.id.code_text);
		codeText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String text = searchText.getText().toString();
				updateHistoryList(lang, text);
			}
		});
		
		numberText = (EditText)findViewById(R.id.number_text);
		numberText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String text = searchText.getText().toString();
				updateHistoryList(lang, text);
			}
		});
		
		searchText = (EditText) findViewById(R.id.search_text);
    	searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		        	searchText();
		            return true;
		        }
		        return false;
		    }
		});
		searchText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String text = searchText.getText().toString();
				updateHistoryList(lang, text);
			}
		});
		
		//show keyboard
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(0, 0);		
		
        historyList = (ListView) this.findViewById(R.id.search_history_listview);
        
        updateHistoryList(lang);
        

        historyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//searchHistoryDBAdapter.open();
				//Cursor cursor = searchHistoryDBAdapter.getEntries(lang);
				//cursor.moveToPosition(arg2);
				//String keywords = cursor.getString(SearchHistoryDBAdapter.KEYWORDS_COL);
				
				String keywords = ((TextView)arg1.findViewById(R.id.hline1)).getText().toString();
				//String keywords = line.substring(0, line.lastIndexOf('(')).trim();
				searchText.setText("");
				searchText.append(keywords);

				String hiers = ((TextView)arg1.findViewById(R.id.hline2)).getText().toString();
				if(hiers.contains("M"))
					((CheckBox) searchView.findViewById(R.id.cb_mul)).setChecked(true);
				else
					((CheckBox) searchView.findViewById(R.id.cb_mul)).setChecked(false);
				if(hiers.contains("A"))
					((CheckBox) searchView.findViewById(R.id.cb_att)).setChecked(true);
				else
					((CheckBox) searchView.findViewById(R.id.cb_att)).setChecked(false);
				if(hiers.contains("T"))
					((CheckBox) searchView.findViewById(R.id.cb_tik)).setChecked(true);
				else
					((CheckBox) searchView.findViewById(R.id.cb_tik)).setChecked(false);
				
				String sets = ((TextView)arg1.findViewById(R.id.hline3)).getText().toString();
				
				if(sets.contains(getString(R.string.ss_vinai)))
					((CheckBox) searchView.findViewById(R.id.cb_vin)).setChecked(true);
				else
					((CheckBox) searchView.findViewById(R.id.cb_vin)).setChecked(false);
				if(sets.contains(getString(R.string.ss_suttan)))
					((CheckBox) searchView.findViewById(R.id.cb_sut)).setChecked(true);
				else
					((CheckBox) SearchDialog.this.findViewById(R.id.cb_sut)).setChecked(false);
				if(sets.contains(getString(R.string.ss_abhi)))
					((CheckBox) searchView.findViewById(R.id.cb_abhi)).setChecked(true);
				else
					((CheckBox) searchView.findViewById(R.id.cb_abhi)).setChecked(false);
				if(sets.contains(getString(R.string.ss_etc)))
					((CheckBox) searchView.findViewById(R.id.cb_etc)).setChecked(true);
				else
					((CheckBox) searchView.findViewById(R.id.cb_etc)).setChecked(false);


				
				//cursor.close();
				//searchHistoryDBAdapter.close();
		        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.toggleSoftInput(0, 0);				
			}
		});
        
        historyList.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
	            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(searchText.getApplicationWindowToken(), 0);
	            //if(historyAdapter.getMarkedPosition() != -1) {
	            //	historyAdapter.setMarkedPosition(-1);
	            //}
				return false;
			}
		});                

		if(prefs.getString("SORT_KEY", SearchHistoryDBAdapter.KEY_KEYWORDS).equals(SearchHistoryDBAdapter.KEY_CODE)) {
			showCodePanel();
			setupFullPopupMenu();
		} else {
			hideCodePanel();
			setupNormalPopupMenu();
		}
        
		super.onCreate(savedInstanceState);
		
		
	}
	
	protected void searchText() {
		String query = searchText.getText().toString();
		b1 = ((CheckBox) searchView.findViewById(R.id.cb_vin)).isChecked();
		b2 = ((CheckBox) searchView.findViewById(R.id.cb_sut)).isChecked();
		b3 = ((CheckBox) searchView.findViewById(R.id.cb_abhi)).isChecked();
		b4 = ((CheckBox) searchView.findViewById(R.id.cb_etc)).isChecked();
		
		b5 = cbm.isChecked();
		b6 = cba.isChecked();
		b7 = cbt.isChecked();
		
		if(query.trim().length() > 0) {
    		query = query.replace("aa", "ā").replace("ii", "ī").replace("uu", "ū").replace(".t", "ṭ").replace(".d", "ḍ").replace("\"n", "ṅ").replace(".n", "ṇ").replace(".m", "ṃ").replace("~n", "ñ").replace(".l", "ḷ");
    		Intent intent = new Intent(context, SearchActivity.class);
    		Bundle dataBundle = new Bundle();
    		dataBundle.putString("LANG", "pali");
    		dataBundle.putString("QUERY", query);
    		dataBundle.putBoolean("b1", b1);
    		dataBundle.putBoolean("b2", b2);
    		dataBundle.putBoolean("b3", b3);
    		dataBundle.putBoolean("b4", b4);
    		dataBundle.putBoolean("b5", b5);
    		dataBundle.putBoolean("b6", b6);
    		dataBundle.putBoolean("b7", b7);
    		intent.putExtras(dataBundle);
    		startActivity(intent);
		}		
	}

	private class ResultsCursorAdapter extends SimpleCursorAdapter {

		private int markedPosition = -1;
		
		public ResultsCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
			super(context, layout, c, from, to);
			// TODO Auto-generated constructor stub
		}
		
		public void setMarkedPosition(int position) {
			markedPosition = position;
			this.notifyDataSetChanged();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			
			String skey = prefs.getString("SORT_KEY", SearchHistoryDBAdapter.KEY_KEYWORDS);
			
			TextView codeLabel = (TextView)view.findViewById(R.id.priority_code);
			TextView numberLabel = (TextView)view.findViewById(R.id.priority_number);
			
			if(position > 0 && skey.equals(SearchHistoryDBAdapter.KEY_CODE)) {
				savedCursor.moveToPosition(position-1);
				savedCursor.moveToPosition(position);
				
				savedCursor.moveToPosition(position-1);
				savedCursor.moveToPosition(position);
				
				codeLabel.setVisibility(View.VISIBLE);
				numberLabel.setVisibility(View.VISIBLE);
				
			} else if(!skey.equals(SearchHistoryDBAdapter.KEY_CODE)) {
				codeLabel.setVisibility(View.GONE);
				numberLabel.setVisibility(View.GONE);
			}

			TextView line2 = (TextView)view.findViewById(R.id.hline2);
			String text = line2.getText().toString();
			text = text.replaceAll("([MAT]\b)","<font color='#45FF45'>$1</font>");
			
			TextView line3 = (TextView)view.findViewById(R.id.hline3);
			text = line3.getText().toString();
			String [] tokens = text.split("\\s+");
			String output = "";
			for(String s: tokens) {
				if(s.startsWith(context.getString(R.string.ss_vinai))) {
					if(s.endsWith(context.getString(R.string.zero_zero))) {
						output += s + "  ";
					} else {
						output += String.format("<font color='#1E90FF'>%s</font>  ", s);
					}
				} else if(s.startsWith(context.getString(R.string.ss_suttan))) {
					if(s.endsWith(context.getString(R.string.zero_zero))) {
						output += s + "  ";
					} else {
						output += String.format("<font color='#FF4500'>%s</font>  ", s);
					}
				} else if(s.startsWith(context.getString(R.string.ss_abhi))) {
					if(s.endsWith(context.getString(R.string.zero_zero))) {
						output += s + "  ";
					} else {
						output += String.format("<font color='#A020F0'>%s</font>  ", s);
					}
				} else if(s.startsWith(context.getString(R.string.ss_etc))) {
					if(s.endsWith(context.getString(R.string.zero_zero))) {
						output += s + "  ";
					} else {
						output += String.format("<font color='#00AA00'>%s</font>  ", s);
					}
				}
			}
			line3.setText(Html.fromHtml(output.trim()));
			
			line3.setTypeface(font);
			if(markedPosition == position) {
				view.setBackgroundColor(Color.LTGRAY);
			}
			
			return view;
		}
	}
	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if(searchText != null && searchText.getText().toString().trim().length() > 0 || 
	    		codeText.getText().toString().trim().length() > 0 ||
	    		numberText.getText().toString().trim().length() > 0) {
	    		searchText.setText("");
	    		codeText.setText("");
	    		numberText.setText("");
	    		return true;
	    	}  
	    } else if(keyCode == KeyEvent.KEYCODE_MENU) {
	    	// hide keyboard
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchText.getApplicationWindowToken(), 0);
            final Dialog sortDialog = new Dialog(context);
            sortDialog.setContentView(R.layout.sort_dialog);
            sortDialog.setTitle(R.string.sorting_prefs);
            
            sortKey = prefs.getString("SORT_KEY", SearchHistoryDBAdapter.KEY_KEYWORDS);
            isDesc = prefs.getBoolean("IS_DESC", false);
            if(sortKey.equals(SearchHistoryDBAdapter.KEY_KEYWORDS)) {
            	((RadioButton)sortDialog.findViewById(R.id.sort_by_keywords)).setChecked(true);
            } else if (sortKey.equals(SearchHistoryDBAdapter.KEY_ID)) {
            	((RadioButton)sortDialog.findViewById(R.id.sort_by_id)).setChecked(true);
            } else if (sortKey.equals(SearchHistoryDBAdapter.KEY_N_SUT)) {
            	((RadioButton)sortDialog.findViewById(R.id.sort_by_suts)).setChecked(true);
            } else if (sortKey.equals(SearchHistoryDBAdapter.KEY_FREQ)) {
            	((RadioButton)sortDialog.findViewById(R.id.sort_by_freq)).setChecked(true);
            } else if (sortKey.equals(SearchHistoryDBAdapter.KEY_CODE)) {
            	((RadioButton)sortDialog.findViewById(R.id.sort_by_priority)).setChecked(true);
            } 
            
            if(isDesc) {
            	((RadioButton)sortDialog.findViewById(R.id.desc_sort)).setChecked(true);
            } else {
            	((RadioButton)sortDialog.findViewById(R.id.asc_sort)).setChecked(true);
            }
            
            ((Button)sortDialog.findViewById(R.id.sort_ok)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences.Editor editor = prefs.edit();
					if(((RadioButton)sortDialog.findViewById(R.id.desc_sort)).isChecked()) {
						isDesc = true;
						
					} else if(((RadioButton)sortDialog.findViewById(R.id.asc_sort)).isChecked()) {
						isDesc = false;
						
					}
					
					if(((RadioButton)sortDialog.findViewById(R.id.sort_by_id)).isChecked()) {
						sortKey = SearchHistoryDBAdapter.KEY_ID;
						hideCodePanel();
						setupNormalPopupMenu();
					} else if(((RadioButton)sortDialog.findViewById(R.id.sort_by_keywords)).isChecked()) {
						sortKey = SearchHistoryDBAdapter.KEY_KEYWORDS;
						hideCodePanel();
						setupNormalPopupMenu();
					} else if(((RadioButton)sortDialog.findViewById(R.id.sort_by_suts)).isChecked()) {
						sortKey = SearchHistoryDBAdapter.KEY_N_SUT;
						hideCodePanel();
						setupNormalPopupMenu();
					} else if(((RadioButton)sortDialog.findViewById(R.id.sort_by_freq)).isChecked()) {
						sortKey = SearchHistoryDBAdapter.KEY_FREQ;
						hideCodePanel();
						setupNormalPopupMenu();
					} else if(((RadioButton)sortDialog.findViewById(R.id.sort_by_priority)).isChecked()) {
						sortKey = SearchHistoryDBAdapter.KEY_CODE;
						showCodePanel();
						setupFullPopupMenu();
					}
					
					editor.putBoolean("IS_DESC", isDesc);
					editor.putString("SORT_KEY", sortKey);
					
					editor.commit();
					sortDialog.dismiss();
					updateHistoryList(lang, searchText.getText().toString().trim());
				}
			});
            sortDialog.show();
	    }
	    return super.onKeyUp(keyCode, event);
	}	
	

	private void setupFullPopupMenu() {
        historyList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {				
				final int position = arg2;
				savedCursor.moveToPosition(position);
				int rowId = savedCursor.getInt(SearchHistoryDBAdapter.ID_COL);
				searchHistoryDBAdapter.open();
				SearchHistoryItem item = searchHistoryDBAdapter.getEntry(rowId);
				searchHistoryDBAdapter.close();
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				
				final CharSequence[] items = {
								String.format(context.getString(R.string.recall_data) + " " + context.getString(R.string.freq_format), 
										Utils.arabic2thai(item.getFrequency()+"", context.getResources())),
								context.getString(R.string.delete),
								context.getString(R.string.assign_priority),
								context.getString(R.string.move_up),
								context.getString(R.string.move_down),
								context.getString(R.string.recompute_priority)};

				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which) {
							case 0: // recall data
								recallItemAt(position);
								break;
							case 1: // delete
								deleteItemAt(position);
								break;
							case 2: // assign
								assignPriorityAt(position);
								break;
							case 3: // move up
								moveItemUp(position);
								break;
							case 4: // move down
								moveItemDown(position);
								break;
							case 5: // recompute
								recomputePriority();
								break;
						}
						
						historyItemDialog.dismiss();
					}
				});				
				historyItemDialog = builder.create();
				historyItemDialog.show();
				return false;
			}
		}); 		
	}
	
	private void setupNormalPopupMenu() {
        historyList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {				
				final int position = arg2;
				savedCursor.moveToPosition(position);
				int rowId = savedCursor.getInt(SearchHistoryDBAdapter.ID_COL);
				searchHistoryDBAdapter.open();
				SearchHistoryItem item = searchHistoryDBAdapter.getEntry(rowId);
				searchHistoryDBAdapter.close();
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				
				final CharSequence[] items = {
								String.format(context.getString(R.string.recall_data) + " " + context.getString(R.string.freq_format), 
										Utils.arabic2thai(item.getFrequency()+"", context.getResources())),
								context.getString(R.string.delete)};
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which) {
							case 0: // recall data
								recallItemAt(position);
								break;
							case 1: // delete
								deleteItemAt(position);
								break;
						}
						
						historyItemDialog.dismiss();
					}
				});				
				historyItemDialog = builder.create();
				historyItemDialog.show();
				return false;
			}
		}); 		
	}
	
	private void hideCodePanel() {
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.filter_layout);
		layout.setVisibility(View.GONE);
	}
	
	private void showCodePanel() {
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.filter_layout);
		layout.setVisibility(View.VISIBLE);
	}
	
	private void recomputePriority() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);   
		builder.setTitle(context.getString(R.string.recompute_priority));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(context.getString(R.string.confirm_command));
		
		builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				searchHistoryDBAdapter.open();
				Cursor cursor = searchHistoryDBAdapter.getEntries(lang, SearchHistoryDBAdapter.KEY_PRIORITY, false);
				if(cursor.moveToFirst()) {
					int count = 0;
					while(!cursor.isAfterLast()) {
						count++;
						int rowId = cursor.getInt(SearchHistoryDBAdapter.ID_COL);
						SearchHistoryItem item = searchHistoryDBAdapter.getEntry(rowId);
						item.setPriority(String.format("%04d",count));
						searchHistoryDBAdapter.updateEntry(rowId, item);
						cursor.moveToNext();
					}
				}
				cursor.close();
				searchHistoryDBAdapter.close();
				updateHistoryList();
				return;
			}
		});
		
		builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		
		
		builder.show();
		
	}
	
	private void moveItemUp(int position) {
		if(savedCursor.moveToPosition(position)) {
			int selectedRowId = savedCursor.getInt(SearchHistoryDBAdapter.ID_COL);
			
			if(savedCursor.moveToPosition(position-1)) {
				int prevRowId = savedCursor.getInt(SearchHistoryDBAdapter.ID_COL);
				searchHistoryDBAdapter.open();		
				SearchHistoryItem selectedItem = searchHistoryDBAdapter.getEntry(selectedRowId);
				SearchHistoryItem prevItem = searchHistoryDBAdapter.getEntry(prevRowId);
				String oldPriority = selectedItem.getPriority(); 
				String newPriority = prevItem.getPriority();
				String oldCode = selectedItem.getCode();
				String newCode = prevItem.getCode();
				
				selectedItem.setPriority(newPriority);
				selectedItem.setCode(newCode);
				prevItem.setPriority(oldPriority);
				prevItem.setCode(oldCode);
				
				searchHistoryDBAdapter.updateEntry(selectedRowId, selectedItem);
				searchHistoryDBAdapter.updateEntry(prevRowId, prevItem);
				searchHistoryDBAdapter.close();
				//updateHistoryList(lang, searchText.getText().toString().trim(), position-1);
				historyAdapter.setMarkedPosition(position-1);
			}
		}		
	}
	
	private void moveItemDown(int position) {
		if(savedCursor.moveToPosition(position)) {
			int selectedRowId = savedCursor.getInt(SearchHistoryDBAdapter.ID_COL);
			
			if(savedCursor.moveToPosition(position+1)) {
				int nextRowId = savedCursor.getInt(SearchHistoryDBAdapter.ID_COL);
				searchHistoryDBAdapter.open();		
				SearchHistoryItem selectedItem = searchHistoryDBAdapter.getEntry(selectedRowId);
				SearchHistoryItem nextItem = searchHistoryDBAdapter.getEntry(nextRowId);
				String oldPriority = selectedItem.getPriority(); 
				String newPriority = nextItem.getPriority();
				String oldCode = selectedItem.getCode();
				String newCode = nextItem.getCode();
				
				selectedItem.setPriority(newPriority);
				selectedItem.setCode(newCode);
				nextItem.setPriority(oldPriority);
				nextItem.setCode(oldCode);
				
				searchHistoryDBAdapter.updateEntry(selectedRowId, selectedItem);
				searchHistoryDBAdapter.updateEntry(nextRowId, nextItem);
				searchHistoryDBAdapter.close();
				//updateHistoryList(lang, searchText.getText().toString().trim(), position+1);
				historyAdapter.setMarkedPosition(position+1);
			}
		}		
	}
	
	private void assignPriorityAt(int _position) {
		final int position = _position;
		//move cursor to selected item
		savedCursor.moveToPosition(position);
		final int selectedRowId = savedCursor.getInt(SearchHistoryDBAdapter.ID_COL);
						
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.assign_dialog);
		
		searchHistoryDBAdapter.open();		
		SearchHistoryItem item = searchHistoryDBAdapter.getEntry(selectedRowId);
		TextView infoText = (TextView)dialog.findViewById(R.id.assign_info);
		infoText.setText(item.getKeywords() + " (" + item.getCode() + ":" + item.getPriority() + ")");
		searchHistoryDBAdapter.close();				
			
		dialog.setTitle(R.string.assign_priority);
		final EditText priorityEditText = (EditText)dialog.findViewById(R.id.assign_text);
		final EditText codeEditText = (EditText)dialog.findViewById(R.id.code_text);
		
		((Button)dialog.findViewById(R.id.assign_btn)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String priorityText = priorityEditText.getText().toString();
				String codeText = codeEditText.getText().toString();
				if(priorityText.length() > 0 || codeText.length() > 0) {
					searchHistoryDBAdapter.open();								
					savedCursor.moveToPosition(position);
					SearchHistoryItem item = searchHistoryDBAdapter.getEntry(selectedRowId);
					if(priorityText.length() > 0)
						item.setPriority(String.format("%04d", Integer.parseInt(priorityText)));
					if(codeText.length() > 0)
						item.setCode(codeText);
					searchHistoryDBAdapter.updateEntry(selectedRowId, item);
					searchHistoryDBAdapter.close();
					updateHistoryList(lang, searchText.getText().toString().trim(), position);					
					dialog.dismiss();
				}
			}
		});
		dialog.show();
	}
	
	private void recallItemAt(int position) {
		searchHistoryDBAdapter.open();
				
		// prepare necessary information for retrieving the result from DB
		String lang = savedCursor.getString(SearchHistoryDBAdapter.LANG_COL);
		String keywords = savedCursor.getString(SearchHistoryDBAdapter.KEYWORDS_COL);
		String sCate = savedCursor.getString(SearchHistoryDBAdapter.SEL_CATE_COL);
		searchResultsDBAdapter.open();
		Cursor cursor = searchResultsDBAdapter.getEntries(lang, keywords, sCate);
		String content = "";
		String pClicked = "";
		String sClicked = "";
		String saved = "";
		String marked = "";
		
		// retrieve the result from DB
		if(cursor.getCount() > 0 && cursor.moveToFirst()) {
			content = cursor.getString(SearchResultsDBAdapter.CONTENT_COL);
			pClicked = cursor.getString(SearchResultsDBAdapter.PRIMARY_CLIKCED_COL);
			sClicked = cursor.getString(SearchResultsDBAdapter.SECONDARY_CLIKCED_COL);
			saved = cursor.getString(SearchResultsDBAdapter.SAVED_COL);
			marked = cursor.getString(SearchResultsDBAdapter.MARKED_COL);
		}
		
		// send the result to SearchActivity 
		if(content.length() > 0) {			
			// increase frequency of history item
			savedCursor.moveToPosition(position);
			long rowId = savedCursor.getInt(SearchHistoryDBAdapter.ID_COL);
			SearchHistoryItem item = searchHistoryDBAdapter.getEntry(rowId);
			item.setFrequency(item.getFrequency()+1);
			searchHistoryDBAdapter.updateEntry(rowId, item);
			
    		Intent intent = new Intent(context, SearchActivity.class);
    		Bundle dataBundle = new Bundle();
    		dataBundle.putString("LANG", lang);
    		dataBundle.putString("CONTENT", content);
    		dataBundle.putString("QUERY", keywords);
    		dataBundle.putString("PCLICKED", pClicked);
    		dataBundle.putString("SCLICKED", sClicked);
    		dataBundle.putString("SAVED", saved);
    		dataBundle.putString("MARKED", marked);
    		dataBundle.putString("SCATE", sCate);
    		
    		intent.putExtras(dataBundle);
    		context.startActivity(intent);
    		
			// update list
			updateHistoryList(lang, searchText.getText().toString().trim());    		
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getString(R.string.data_not_found));
			builder.setMessage(context.getString(R.string.please_search_again));
			builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			
			builder.show();
		}
		
		cursor.close();
		searchResultsDBAdapter.close();				
		searchHistoryDBAdapter.close();
		
	}
	
	private void deleteItemAt(int _position) {
		final int position = _position;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);   
		builder.setTitle(context.getString(R.string.delete_item));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(context.getString(R.string.confirm_delete_item));
		
		builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// remove record from DB
				searchHistoryDBAdapter.open();								
				savedCursor.moveToPosition(position);
				int rowId = savedCursor.getInt(SearchHistoryDBAdapter.ID_COL);
				searchHistoryDBAdapter.removeEntry(rowId);								
				searchHistoryDBAdapter.close();
				
				// prepare data for updating the corresponding record in another DB
				String lang = savedCursor.getString(SearchHistoryDBAdapter.LANG_COL);
				String keywords = savedCursor.getString(SearchHistoryDBAdapter.KEYWORDS_COL);
				String sCate = savedCursor.getString(SearchHistoryDBAdapter.SEL_CATE_COL);
				int nSutsHistory = savedCursor.getInt(SearchHistoryDBAdapter.N_SUT_COL);
				
				// update the corresponding record
				searchResultsDBAdapter.open();
				Cursor cursor = searchResultsDBAdapter.getEntries(lang, keywords, sCate);
				if(cursor.getCount() > 0 && cursor.moveToFirst()) {
					int i;
					int nSutsResults;
					String suts;
					String [] tokens;
					while(!cursor.isAfterLast()) {
						i = cursor.getInt(SearchResultsDBAdapter.ID_COL);
						suts = cursor.getString(SearchResultsDBAdapter.SUTS_COL);
						tokens = suts.split(":");
						nSutsResults = Integer.parseInt(tokens[0]) + Integer.parseInt(tokens[1]) + Integer.parseInt(tokens[2]);
						if(nSutsResults == nSutsHistory) {
							searchResultsDBAdapter.removeEntry(i);
						}
						cursor.moveToNext();
					}
				}
				cursor.close();
				searchResultsDBAdapter.close();
				updateHistoryList(lang, searchText.getText().toString().trim(),position);
				
				BookmarkDBAdapter bookmarkDBAdapter = new BookmarkDBAdapter(context);
				bookmarkDBAdapter.open();
				cursor = bookmarkDBAdapter.getEntries(lang, keywords);
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					bookmarkDBAdapter.removeEntry(cursor.getInt(0));
					cursor.moveToNext();
				}
				bookmarkDBAdapter.close();
				
				return;
				
			}
		});
		builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
				
			}
		});		
		builder.show();
	
	}
	

	private void updateHistoryList(String _lang, String text, int position) {
		String code = codeText.getText().toString();
		String number = numberText.getText().toString();
		searchHistoryDBAdapter.open();
		if(savedCursor != null && !savedCursor.isClosed()) {
			savedCursor.close();
		}
		savedCursor = searchHistoryDBAdapter.getEntries(_lang, text, sortKey, isDesc, code, number);
        historyAdapter = new ResultsCursorAdapter(this, R.layout.history_item, savedCursor, 
        		new String[] {  SearchHistoryDBAdapter.KEY_KEYWORDS,
        						SearchHistoryDBAdapter.KEY_LINE1, 
        						SearchHistoryDBAdapter.KEY_LINE2, 
        						SearchHistoryDBAdapter.KEY_PRIORITY, SearchHistoryDBAdapter.KEY_CODE}, 
        		new int[] {R.id.hline1, R.id.hline2, R.id.hline3, R.id.priority_number, R.id.priority_code});
        historyList.setAdapter(historyAdapter);				
		searchHistoryDBAdapter.close();
		if(position > 0) {
			historyList.setSelected(true);
			historyList.setSelection(position-1);
		}
	}
	
	private void updateHistoryList(String _lang, String text) {
		String code = codeText.getText().toString();
		String number = numberText.getText().toString();		
		searchHistoryDBAdapter.open();
		if(savedCursor != null && !savedCursor.isClosed()) {
			savedCursor.close();
		}
		savedCursor = searchHistoryDBAdapter.getEntries(_lang, text, sortKey, isDesc, code, number);
        historyAdapter = new ResultsCursorAdapter(context, R.layout.history_item, savedCursor, 
        		new String[] {  SearchHistoryDBAdapter.KEY_KEYWORDS,
        						SearchHistoryDBAdapter.KEY_LINE1, 
        						SearchHistoryDBAdapter.KEY_LINE2, 
        						SearchHistoryDBAdapter.KEY_PRIORITY, SearchHistoryDBAdapter.KEY_CODE}, 
        		new int[] {R.id.hline1, R.id.hline2, R.id.hline3, R.id.priority_number, R.id.priority_code});
        historyList.setAdapter(historyAdapter);				
		searchHistoryDBAdapter.close();
	}

	private void updateHistoryList(String _lang) {
		searchHistoryDBAdapter.open();
		if(savedCursor != null && !savedCursor.isClosed()) {
			savedCursor.close();
		}
		savedCursor = searchHistoryDBAdapter.getEntries(_lang, sortKey, isDesc);
        historyAdapter = new ResultsCursorAdapter(this, R.layout.history_item, savedCursor, 
        		new String[] {  SearchHistoryDBAdapter.KEY_KEYWORDS,
        						SearchHistoryDBAdapter.KEY_LINE1, 
        						SearchHistoryDBAdapter.KEY_LINE2, 
        						SearchHistoryDBAdapter.KEY_PRIORITY, SearchHistoryDBAdapter.KEY_CODE}, 
        		new int[] {R.id.hline1, R.id.hline2, R.id.hline3, R.id.priority_number, R.id.priority_code});
        historyList.setAdapter(historyAdapter);				
		searchHistoryDBAdapter.close();
		Log.i("Tipitaka","saved cursor length: "+savedCursor.getCount());
	}
	
	public void updateHistoryList() {
		updateHistoryList(lang,searchText.getText().toString());
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
		}
		return false;
	}
}