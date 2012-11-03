package com.tearoffcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.lifecycle.R;

public class DisplayMenuActivity extends Activity {

	private static final String TAG = "DisplayMenuActivity";

	public boolean onCreateOptionsMenu(Menu menu) {
		Log.v(TAG, "onCreateOptionsMenu");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(TAG, "onOptionsItemSelected");

		switch (item.getItemId()) {
		case R.id.menu_collection:
			Log.v(TAG, "Menu collection!");
			Intent intent = new Intent(this, CardHistoryActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_settings:
			Log.v(TAG, "Menu settings!");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showPopup(MenuItem v) {
	}
}
