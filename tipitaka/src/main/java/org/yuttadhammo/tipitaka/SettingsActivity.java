package org.yuttadhammo.tipitaka;

import java.io.File;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Environment;

import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;


public class SettingsActivity extends PreferenceActivity {
	
	private Context context;
	private Activity activity;
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		context = this;
		activity = this;
		addPreferencesFromResource(R.xml.preferences);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		
		final EditTextPreference sizePref = (EditTextPreference)findPreference("base_text_size");
		sizePref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		if(sizePref.getText() == null || sizePref.getText().equals(""))
			sizePref.setText("16");
		sizePref.setSummary(sizePref.getText());
		
		sizePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				sizePref.setSummary((String)newValue);
				return true;
			}
			
		});
		
		final EditTextPreference dirPref = (EditTextPreference)findPreference("data_dir");
		if(dirPref.getText() == null || dirPref.getText().equals(""))
			dirPref.setText(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ATPK");
		dirPref.setSummary(dirPref.getText());
		
		dirPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				dirPref.setSummary((String)newValue);
				return true;
			}
			
		});

		final EditTextPreference atiPref = (EditTextPreference)findPreference("ati_dir");
		if(atiPref.getText() == null || atiPref.getText().equals(""))
			atiPref.setText(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ati_website");
		atiPref.setSummary(atiPref.getText());
		
		atiPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				atiPref.setSummary((String)newValue);
				return true;
			}
			
		});

		final Preference textColorPref = findPreference("text_color");
		final Preference backColorPref = findPreference("text_color_back");
		final Preference titleColorPref = findPreference("text_color_title");
		final Preference varColorPref = findPreference("text_color_var");
		final Preference searchColorPref = findPreference("text_color_search");
		final Preference findColorPref = findPreference("text_color_find");

		textColorPref.setSummary(PaliUtils.colorToHexString(prefs.getInt("text_color", getResources().getColor(R.color.text_color))));
		backColorPref.setSummary(PaliUtils.colorToHexString(prefs.getInt("text_color_back", getResources().getColor(R.color.text_color_back))));
		titleColorPref.setSummary(PaliUtils.colorToHexString(prefs.getInt("text_color_title", getResources().getColor(R.color.text_color_title))));
		varColorPref.setSummary(PaliUtils.colorToHexString(prefs.getInt("text_color_var", getResources().getColor(R.color.text_color_var))));
		searchColorPref.setSummary(PaliUtils.colorToHexString(prefs.getInt("text_color_search", getResources().getColor(R.color.text_color_search))));
		findColorPref.setSummary(PaliUtils.colorToHexString(prefs.getInt("text_color_find", getResources().getColor(R.color.text_color_find))));

		
		textColorPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				onClickColorPickerDialog("text_color", getResources().getColor(R.color.text_color), textColorPref);
				return false;
			}
			
		});
		backColorPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				onClickColorPickerDialog("text_color_back", getResources().getColor(R.color.text_color_back), backColorPref);
				return false;
			}
			
		});
		titleColorPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				onClickColorPickerDialog("text_color_title", getResources().getColor(R.color.text_color_title), titleColorPref);
				return false;
			}
			
		});
		varColorPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				onClickColorPickerDialog("text_color_var", getResources().getColor(R.color.text_color_var), varColorPref);
				return false;
			}
			
		});		
		searchColorPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				onClickColorPickerDialog("text_color_search", getResources().getColor(R.color.text_color_search), searchColorPref);
				return false;
			}
			
		});
		findColorPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				onClickColorPickerDialog("text_color_find", getResources().getColor(R.color.text_color_find), findColorPref);
				return false;
			}
			
		});

        final Preference updatePref = findPreference("update_pref");
        updatePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(context, PlayDownloaderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return false;
            }
        });

        final ListPreference textScripts = (ListPreference)findPreference("text_script");

        final String[] entryValues = {"0","1","2","3","4"};
        final String [] entries= getResources().getStringArray(R.array.text_scripts);

        textScripts.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {
                int scriptEntry = Integer.parseInt((String)newValue);
                textScripts.setSummary(entries[scriptEntry]);
                return true;
            }

        });

        //Default value
        if(textScripts.getValue() == null) textScripts.setValue("0");
        textScripts.setDefaultValue("0");

        int scriptEntry = Integer.parseInt(textScripts.getValue());
        textScripts.setSummary(entries[scriptEntry]);

        textScripts.setEntries(entries);
        textScripts.setEntryValues(entryValues);


    }
	
	public void onClickColorPickerDialog(final String key, int def, final Preference apref) {
		//The color picker menu item as been clicked. Show 
		//a dialog using the custom ColorPickerDialog class.
		
		int initialValue = prefs.getInt(key, def);
		
		Log.d("mColorPicker", "initial value:" + initialValue);
				
		final ColorPickerDialog colorDialog = new ColorPickerDialog(this, initialValue);
		
		colorDialog.setAlphaSliderVisible(true);
		colorDialog.setTitle("Pick a Color!");

		colorDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			

			@Override
			public void onClick(DialogInterface dialog, int which) {
							
				//Save the value in our preferences.
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(key, colorDialog.getColor());
				editor.commit();
			}
		});
		
		colorDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Nothing to do here.
			}
		});
		colorDialog.setOnDismissListener(new OnDismissListener(){

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				apref.setSummary(PaliUtils.colorToHexString(colorDialog.getColor()));
			}
			
		});
		colorDialog.show();
	}

}
