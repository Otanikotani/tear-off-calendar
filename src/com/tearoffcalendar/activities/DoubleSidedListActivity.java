package com.tearoffcalendar.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.lifecycle.R;
import com.tearoffcalendar.app.TearOffApp;
import com.tearoffcalendar.themes.BasicTheme;
import com.tearoffcalendar.themes.BasicThemeManager;
import com.tearoffcalendar.themes.ThemeException;

public class DoubleSidedListActivity extends FragmentActivity implements
		FaceDownCardFragment.OnFaceDownCardClickListener,
		FaceUpCardFragment.OnHeadlineSelectedListener {

	private static final String TAG = "DoubleSidedListActivity";
	private static final String CURRENT_CARD_KEY = "DoubleSidedListActivity.CURRENT_CARD_KEY";

	public static final int MAXIMUM_DAYS_TO_TEAR_OFF = 100;

	private String preferenceTornCardsCollectionKey;
	private String preferenceFileKey;
	private Card currentCard;

	private FaceDownCardFragment faceDownCardFragment;
	private FaceUpCardFragment faceUpCardFragment;

	private static final BasicThemeManager themeManager = TearOffApp
			.getInstance().getThemeManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.card_fragment_container);

		// Check whether the activity is using the layout version with
		// the fragment_container FrameLayout. If so, we must add the first
		// fragment
		if (findViewById(R.id.card_fragment_container) != null) {

			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			preferenceTornCardsCollectionKey = getString(R.string.preference_file_torn_cards_key);
			preferenceFileKey = getString(R.string.preference_file_key);
			SharedPreferences sharedPref = this.getSharedPreferences(
					preferenceFileKey, Context.MODE_PRIVATE);

			currentCard = new Card();
			currentCard.setDateFromString(sharedPref.getString(
					CURRENT_CARD_KEY, new String()));
			if (currentCard.toString().isEmpty()) {
				currentCard.setDate(new Date());
				saveCurrentCard(currentCard);
			}

			faceDownCardFragment = new FaceDownCardFragment();
			// Create an instance of ExampleFragment
			faceUpCardFragment = new FaceUpCardFragment();

			List<String> list = new ArrayList<String>();
			list.add(currentCard.toString());

			faceUpCardFragment.setCardNames(list);
			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			faceUpCardFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction()
					.add(R.id.card_fragment_container, faceUpCardFragment).commit();

		}
	}

	public void onFaceDownCardClick() {
		Log.v(TAG, "On face down card click");
		// Capture the card fragment from the activity layout
		// Create an instance of ExampleFragment
		List<String> list = new ArrayList<String>();
		list.add(currentCard.toString());
		faceUpCardFragment = new FaceUpCardFragment();
		faceUpCardFragment.setCardNames(list);
		switchToFaceUp();
	}
	
	public void onCardSelected(Date date) {
		Log.v(TAG, "On card selected!");
		FaceDownCardFragment cardFrag = (FaceDownCardFragment) getSupportFragmentManager()
				.findFragmentById(R.id.face_down_card_fragment);
		if (isLimit()) {
			Log.v(TAG, "LIMIT!");
			Intent intent = new Intent(this, LimitExceededDialogActivity.class);
			startActivity(intent);
		} else {
			saveAsTorn(currentCard);
			currentCard.increment();
			saveCurrentCard(currentCard);
			if (null != cardFrag) {
				// Call a method in the CardFragment to update its content
				Log.v(TAG, "Starting another view");
				String str = getWebViewTextByDate(currentCard.getDate());
				cardFrag.updateCardView(str);
			} else {
				// Call a method in the CardFragment to update its content
				// If the frag is not available, we're in the one-pane layout
				// and
				// must swap frags...
				// Create fragment and give it an argument for the selected
				// article
				faceDownCardFragment = new FaceDownCardFragment();
				Log.v(TAG, "Starting another view");
				Bundle args = new Bundle();
				String str = getWebViewTextByDate(currentCard.getDate());
				args.putString(FaceDownCardFragment.CARD_WEB_VIEW_KEY, str);
				faceDownCardFragment.setArguments(args);
				switchToFaceDown();
			}
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_double_sided_list, menu);
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
			currentCard.setDate(new Date());
			saveCurrentCard(currentCard);
			ArrayList<String> list = new ArrayList<String>();
			list.add(currentCard.toString());
			faceUpCardFragment.setCardNames(list);
			faceUpCardFragment.updateListView();
			Toast.makeText(getApplicationContext(),
					"Cards are reset", Toast.LENGTH_SHORT).show();			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showPopup(MenuItem v) {
	}	

	private String getWebViewTextByDate(Date date) {
		SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		String themeNameKey = getString(R.string.current_theme_key);
		String currentThemeName = sharedPref.getString(themeNameKey, "");
		if (currentThemeName.isEmpty()) {
			// Add errh
		} else {
			try {
				BasicTheme theme = themeManager
						.getThemeByName(currentThemeName);
				return theme.getTextCard(date);
			} catch (ThemeException te) {
				Log.e(TAG, te.getMessage());
				return "";
			}
		}
		return "";
	}

	private int getDayOfYear() {
		Calendar rightNow = Calendar.getInstance();
		int dayOfYear = rightNow.get(Calendar.DAY_OF_YEAR);
		return dayOfYear;
	}

	private boolean isLimit() {
		int today = getDayOfYear();
		int limit = MAXIMUM_DAYS_TO_TEAR_OFF;
		if ((today + limit) < currentCard.getDayOfYear()) {
			Log.v(TAG, "LIMIT!");
			return true;
		}
		return false;
	}

	private void saveCurrentCard(Card card) {
		preferenceFileKey = getString(R.string.preference_file_key);
		SharedPreferences sharedPref = this.getSharedPreferences(
				preferenceFileKey, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(CURRENT_CARD_KEY, card.toString());
		editor.commit();
	}

	private void saveAsTorn(Card card) {
		SharedPreferences sharedPref = this.getSharedPreferences(
				preferenceFileKey, Context.MODE_PRIVATE);
		Set<String> tornCards = sharedPref.getStringSet(
				preferenceTornCardsCollectionKey, new HashSet<String>());
		tornCards.add(card.toString());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putStringSet(preferenceTornCardsCollectionKey, tornCards);
		editor.commit();
	}

	private void switchToFaceDown() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack so the user can
		// navigate back
		transaction.replace(R.id.card_fragment_container, faceDownCardFragment);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}

	private void switchToFaceUp() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.card_fragment_container, faceUpCardFragment);
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}
}
