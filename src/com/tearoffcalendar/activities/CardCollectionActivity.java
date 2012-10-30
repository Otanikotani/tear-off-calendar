package com.tearoffcalendar.activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.lifecycle.R;

public class CardCollectionActivity extends ListActivity {

	static final String TAG = "CardCollectionActivity";
	static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
			"Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
			"Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };

	private String preferenceTornCardsCollectionKey;
	private String preferenceFileKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferenceTornCardsCollectionKey = getString(R.string.preference_file_torn_cards_key);
		preferenceFileKey = getString(R.string.preference_file_key);
		
		SharedPreferences sharedPref = this.getSharedPreferences(
				preferenceFileKey, Context.MODE_PRIVATE);
		Set<String> tornCards = sharedPref.getStringSet(
				preferenceTornCardsCollectionKey, new HashSet<String>());
		Log.v(TAG, tornCards.toString());
		List<String> list = new ArrayList<String>(tornCards);
		
		initListView(list);
	}
	
	private void initListView(List<String> list) {
		Log.v(TAG, list.toString());
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_torn_cards,
				list));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_card_collection, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(TAG, "onOptionsItemSelected");
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Log.v(TAG, "Resetting torn cards collection...");
			SharedPreferences sharedPref = this.getSharedPreferences(
					preferenceFileKey,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.remove(preferenceTornCardsCollectionKey);
			editor.commit();
			//Pass empty list to reset view
			initListView(new ArrayList<String>());
			Toast.makeText(getApplicationContext(),
					"Torn cards history is reset", Toast.LENGTH_SHORT).show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
