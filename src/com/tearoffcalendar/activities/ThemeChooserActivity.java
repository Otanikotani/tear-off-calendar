package com.tearoffcalendar.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android.lifecycle.R;
import com.tearoffcalendar.app.TearOffApp;
import com.tearoffcalendar.themes.BasicTheme;
import com.tearoffcalendar.themes.BasicThemeManager;

public class ThemeChooserActivity extends Activity implements
		RadioGroup.OnCheckedChangeListener, View.OnClickListener {
	
	private RadioGroup themeRadioGroup;
	ArrayList<BasicTheme> availableThemes;
	
	private static final String TAG = "ThemeChooserActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_theme_chooser);
		themeRadioGroup = (RadioGroup) findViewById(R.id.theme_chooser_radio_group);
		themeRadioGroup.setOnCheckedChangeListener(this);
		
		availableThemes = new ArrayList<BasicTheme>();
		
		// Get all themes
		BasicThemeManager manager = TearOffApp.getInstance().getThemeManager();
		int i = 0;
		for (BasicTheme theme: manager.getAvailableThemes()) {
			Log.v(TAG, "Theme name:" + theme.getName());
			RadioButton newRadioButton = new RadioButton(this);
			newRadioButton.setText(theme.getName());
			newRadioButton.setId(i);
			newRadioButton.setOnClickListener(this);
			LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
					RadioGroup.LayoutParams.WRAP_CONTENT,
					RadioGroup.LayoutParams.WRAP_CONTENT);
			themeRadioGroup.addView(newRadioButton, 0, layoutParams);
			availableThemes.add(theme);
			i++;
		}	

	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Log.v(TAG, "Use theme: " + availableThemes.get(checkedId).getName());
		SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		String themeNameKey = getString(R.string.current_theme_key);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(themeNameKey, availableThemes.get(checkedId).getName());
		editor.commit();
		Intent intent = new Intent(this,
				MainActivity.class);
		startActivity(intent);
	}

	public void onClick(View v) {
		// Do we actually need to do something here...
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_theme_chooser, menu);
		return true;
	}
}
