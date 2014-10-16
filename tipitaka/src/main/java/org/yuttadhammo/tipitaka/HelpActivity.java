package org.yuttadhammo.tipitaka;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class HelpActivity extends Activity {
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View main =  View.inflate(this, R.layout.help_dialog, null);
        setContentView(main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        main.setBackgroundColor(prefs.getInt("text_color_back", getResources().getColor(R.color.text_color_back)));

	}
}
