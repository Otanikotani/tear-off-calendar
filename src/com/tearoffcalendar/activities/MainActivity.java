/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tearoffcalendar.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.lifecycle.DialogActivity;
import com.example.android.lifecycle.R;

/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */
public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		// This code is to move to future main activity!

		SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		Log.v(TAG, "First message!");
		String themeNameKey = getString(R.string.current_theme_key);
		String currentThemeName = sharedPref.getString(themeNameKey, "");
		if (!currentThemeName.isEmpty()) {
			Log.v(TAG, "Current theme: " + currentThemeName);
		} else {
			// First launch, make user to choose a theme
			// Create fragment and stuff later, but now on set it to something
			// we can check that it saves.
			Log.v(TAG, "First launch, choosing theme...");
			Intent intent = new Intent(this, ThemeChooserActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void startDialog(View v) {
		Intent intent = new Intent(MainActivity.this, DialogActivity.class);
		startActivity(intent);
	}

	public void startTextCardView(View v) {
		Intent intent = new Intent(MainActivity.this, TextCardActivity.class);
		startActivity(intent);
	}

	public void startTestImageView(View v) {
		Intent intent = new Intent(MainActivity.this,
				TestImageViewActivity.class);
		startActivity(intent);
	}

}
