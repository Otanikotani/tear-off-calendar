package com.tearoffcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.lifecycle.R;

public class DisplayMenuActivity extends Activity {

	private static final String TAG = "DisplayMenuActivity";

	public boolean onCreateOptionsMenu(Menu menu) {
		Log.v(TAG, "onCreateOptionsMenu");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(TAG, "DisplayMenuActivity: Item selected: " + item.toString());

		switch (item.getItemId()) {
		case R.id.card_history_menu:
			Log.v(TAG, "Card history!");
			Intent intent = new Intent(this, CardHistoryActivity.class);
			startActivity(intent);
			return true;
		case R.id.theme_selection_menu:
			Log.v(TAG, "Theme selection menu!");
			Toast.makeText(getApplicationContext(), "Theme selection menu! Under construction",
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showPopup(MenuItem v) {
	}
}
