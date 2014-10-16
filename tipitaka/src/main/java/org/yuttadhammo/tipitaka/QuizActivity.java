package org.yuttadhammo.tipitaka;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class QuizActivity extends Activity {


	private SharedPreferences prefs;
	private Typeface font;
	private View layout;
	private MainTipitakaDBAdapter db;
	private QuizActivity context;
	private GridView grid;
	private int CPED_LENGTH = 20971;
	private int rtotal;
	private int wtotal;
	private float textSize;
	public float largeSize;
	private TextView wrongText;
	private TextView rightText;
	private TextView resultText;
	private TextView questionText;
	private QuizAdapter adapter;

	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.context = this;
        prefs =  PreferenceManager.getDefaultSharedPreferences(this);
		font = Typeface.createFromAsset(getAssets(), "verajjan.ttf");
        
        layout =  View.inflate(this, R.layout.quiz, null);
        setContentView(layout);

        wrongText = (TextView)layout.findViewById(R.id.wrong_text);
        rightText = (TextView)layout.findViewById(R.id.right_text);
        resultText = (TextView)layout.findViewById(R.id.result_text);
        questionText = (TextView)layout.findViewById(R.id.question_text);
        
        questionText.setTypeface(font);

		@SuppressWarnings("deprecation")
		int api = Integer.parseInt(Build.VERSION.SDK);
		
		if (api >= 14) {
			this.getActionBar().setHomeButtonEnabled(true);
		}
        
        db = new MainTipitakaDBAdapter(this);
        
        try {
        	db.open();
        	if(db.isOpened()) {
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
        grid = (GridView) findViewById(R.id.buttons);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        	grid.setNumColumns(2);
        
        adapter = new QuizAdapter();
        adapter.initQuiz();
        grid.setAdapter(adapter);
        
	}


	@Override
	protected void onResume(){
		super.onResume();
        layout.setBackgroundColor(prefs.getInt("text_color_back", getResources().getColor(R.color.text_color_back)));
        textSize = Float.parseFloat(prefs.getString("base_text_size", "16"));
        largeSize = Float.parseFloat(Double.toString(textSize*1.5));
		wrongText.setTextSize(largeSize);
        rightText.setTextSize(largeSize);
        resultText.setTextSize(largeSize);
        questionText.setTextSize(textSize);
        questionText.setTypeface(font);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		QuizButton[] oldButtons = adapter.buttons;
		adapter = new QuizAdapter(oldButtons);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        	grid.setNumColumns(2);
        else
        	grid.setNumColumns(1);
        grid.setAdapter(adapter);		
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.quiz_menu, menu);
	    return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		super.onOptionsItemSelected(item);
		
		//SharedPreferences.Editor editor = prefs.edit();
		Intent intent;
		switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            intent = new Intent(this, SelectBookActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
			case (int)R.id.help_menu_item:
				intent = new Intent(this, HelpActivity.class);
				startActivity(intent);
				break;
			case (int)R.id.prefs_menu_item:
				intent = new Intent(this, SettingsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case (int)R.id.read_dict_menu_item:
				intent = new Intent(this, DictionaryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case (int)R.id.english_menu_item:
				intent = new Intent(this, EnglishActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			default:
				return false;
	    }
		return true;
	}	
	
	
	
	public class QuizAdapter extends BaseAdapter {
		
		private int rpos;
		private String[] rtext;
		private ArrayList<String[]> values;
		public QuizButton[] buttons;
		
	    public QuizAdapter(QuizButton[] oldButtons) {
			this.buttons = oldButtons;
		}

		public QuizAdapter() {
			
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
	    	
	        Button i = (Button)View.inflate(context, R.layout.quiz_button, null);
	        
	        int pixels = grid.getHeight()/4;
	        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
	        	pixels = grid.getHeight()/2;
	        
	        i.setHeight(pixels);
	        i.setText(buttons[position].getText()[1]);
	        i.setTextSize(textSize);
	        i.setTypeface(font);
	        
        	i.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					answerQuiz(buttons[position].isRight());
				}
        	});
	        
	        return i;
	    }
	
		public final int getCount() {
	        return 4;
	    }
	

	    public final long getItemId(int position) {
	        return position;
	    }

		@Override
		public Object getItem(int position) {
			return null;
		}

		private void initQuiz() {
			
			// right
			
			int rint = (int) Math.round(Math.random()*CPED_LENGTH);
			Cursor cursor = db.dictQuizQuery("cped",rint);
			cursor.moveToFirst();
			
			rtext = new String[]{cursor.getString(0),cursor.getString(1)};

			questionText.setText(String.format(context.getString(R.string.question), toUni(rtext[0])));

			rpos = (int) Math.round(Math.random()*3);
			
			// make buttons

			buttons = new QuizButton[4]; 
			
			int[] positions = new int[4];
			values = new ArrayList<String[]>();
			
			for(int i = 0; i < 4; i++) {
				
				if(i == rpos) {
					values.add(rtext);
					buttons[i] = new QuizButton(true, rtext, i);
					Log.i("Tipitaka","Button "+i+", right, "+rtext[1]);
					continue;
				}
				
				while (true) {
					int rand = (int) Math.round(Math.random()*CPED_LENGTH);
					if(rand == rint)
						continue;
					for(int j = 0; j < i; j++) {
						if(rand == positions[j])
							continue;
					}
					positions[i] = rand;
					break;
				}
				cursor = db.dictQuizQuery("cped",positions[i]);
				cursor.moveToFirst();
				values.add(new String[]{cursor.getString(0),cursor.getString(1)}); 

				Log.i("Tipitaka","Button "+i+", wrong, "+values.get(i)[1]);
				buttons[i] = new QuizButton(false, values.get(i), i);
			}
			cursor.close();
		}
	}
	
    protected void answerQuiz(boolean right) {
    	if(right) {
    		rtotal++;
        	resultText.setText(R.string.correct);
        	resultText.setTextColor(0xFF00CC00);
            adapter = new QuizAdapter();
            adapter.initQuiz();
            grid.setAdapter(adapter);
    	}
    	else {
    		wtotal++;
    		resultText.setText(R.string.incorrect);
    		resultText.setTextColor(0xFFCC0000);
    	}
    	
    	wrongText.setText(Integer.toString(wtotal));
    	rightText.setText(Integer.toString(rtotal));
		
    	
	}
	private String toUni(String string) {
		string = string.replace("aa", "ā").replace("ii", "ī").replace("uu", "ū").replace(".t", "ṭ").replace(".d", "ḍ").replace("\"n", "ṅ").replace(".n", "ṇ").replace(".m", "ṃ").replace("~n", "ñ").replace(".l", "ḷ");
		return string;
	}
}
