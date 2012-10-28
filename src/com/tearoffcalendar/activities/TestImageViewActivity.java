package com.tearoffcalendar.activities;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.lifecycle.R;
import com.example.android.lifecycle.util.StatusTracker;
import com.example.android.lifecycle.util.Utils;

public class TestImageViewActivity extends Activity implements OnTouchListener {

	private StatusTracker mStatusTracker = StatusTracker.getInstance();
	private ImageView imageToMove;

	private float bottomLimitToMove;
	float mX, mY, initialX, initialY;
	private boolean neverShifted = true;

	private static final String TAG = "TestImageViewActivity";
	private static final String IMAGE_KEY = "TestImageViewActivity.ImageKey";
	private static final String TEARING_OFF_LIMIT_DAY_KEY = "TestImageViewActivity.TEARING_OFF_LIMIT_DAY_KEY";
	private static final int MAXIMUM_DAYS_TO_TEAR_OFF = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		neverShifted = true;

		// Need to set image not by id, but getting it from saved instance state
		SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
		int currentImageId = sharedPref.getInt(IMAGE_KEY, -1);
		if (-1 == currentImageId) {
			// Default's -1 which must be okay in most of occasions. Ids are
			// like days from 1 to 365.
			Log.v(TAG, "First run! Set view according to current date...");
			currentImageId = getDayOfYear();

			SharedPreferences.Editor editor = sharedPref.edit();
			// +1 day after tearing off
			editor.putInt(IMAGE_KEY, currentImageId);
			editor.commit();
		} else {
			Log.v(TAG,
					"Not a first run! Current id is: "
							+ String.valueOf(currentImageId));
		}

		// Check if not set
		int currentDay = getDayOfYear();
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(TEARING_OFF_LIMIT_DAY_KEY, currentDay
				+ MAXIMUM_DAYS_TO_TEAR_OFF);
		editor.commit();
		Log.v(TAG,
				"Limit: "
						+ String.valueOf(currentDay + MAXIMUM_DAYS_TO_TEAR_OFF));

//		 clearPreferences();
		setContentView(R.layout.activity_test_image_view);

		// Get screen height to determine when the limit
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		bottomLimitToMove = (float) (size.y - size.y * 0.50);
		Log.v(TAG, "Screen height: " + String.valueOf(bottomLimitToMove));

		imageToMove = (ImageView) findViewById(R.id.imageView1);
		imageToMove.setOnTouchListener(this);
		initialX = imageToMove.getX();
		initialY = imageToMove.getY();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, currentImageId);
		TextView text = (TextView) findViewById(R.id.textView1);
		text.setText("Today is: " + calendar.getTime());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_test_image_view, menu);
		return true;
	}

	public boolean onTouch(View view, MotionEvent event) {
		mStatusTracker.setStatus("TestImageViewActivity", "On touch!");

		final float x = event.getRawX();
		final float y = event.getRawY();

		// printCoordinates(x, y);
		// printImageCoordinates();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			Log.v(TAG, "Touch: Action down!");
			mX = x;
			mY = y;
			break;
		case MotionEvent.ACTION_UP:
			Log.v(TAG, "Touch: Action up!");
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.v(TAG, "Touch: Action pointer down!");
			break;
		case MotionEvent.ACTION_POINTER_UP:
			Log.v(TAG, "Touch: Action pointer up!");
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaY = y - mY;
			imageToMove.setY(deltaY);
			imageToMove.invalidate();
			StringBuilder yDifferenceToString = new StringBuilder();
			yDifferenceToString.append(deltaY);

			// printImageCoordinates();
			Log.v(TAG, "Image y: " + String.valueOf(imageToMove.getY()));
			// Do not run this code more than once, thus bool flag
			if (imageToMove.getY() >= bottomLimitToMove && neverShifted) {
				neverShifted = false;

				// We shall check whether limit of cards to tear of is exceeded.
				// Checking it by taking currentImageId, i.e
				SharedPreferences sharedPref = this
						.getPreferences(MODE_PRIVATE);
				int limit = sharedPref.getInt(TEARING_OFF_LIMIT_DAY_KEY, -1);

				// Setting next image id
				int currentImageId = sharedPref.getInt(IMAGE_KEY, -1);

				if (limit != -1 && currentImageId != -1) {
					if (limit <= (currentImageId + 1)) {
						Log.v(TAG, "Limit is exceeded!");
						
						//Go to dialog activity to notify user
						Intent intent = new Intent(TestImageViewActivity.this,
								LimitExceededDialogActivity.class);
						startActivity(intent);
						
					} else {
						Log.v(TAG,
								"Limit: " + String.valueOf(limit)
										+ " Current id: "
										+ String.valueOf(currentImageId));
						SharedPreferences.Editor editor = sharedPref.edit();
						// +1 day after tearing off
						editor.putInt(IMAGE_KEY, currentImageId + 1);
						editor.commit();

						Log.v(TAG, "Activity: Starting TextCardActivity!");
						Intent intent = new Intent(TestImageViewActivity.this,
								TextCardActivity.class);
						startActivity(intent);
					}
				} else {
					Log.v(TAG, "Too bad!");
					// Throw something bad
				}
			}
			break;
		}
		return true;
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        imageToMove.setX(initialX);
        imageToMove.setY(initialY);
        imageToMove.invalidate();
        neverShifted = true;
        //Probably reset image back to what it was at the beginning
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

	private int getDayOfYear() {
		Calendar rightNow = Calendar.getInstance();
		int dayOfYear = rightNow.get(Calendar.DAY_OF_YEAR);
		return dayOfYear;
	}

	private void clearPreferences() {
		SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
	}

	// private void printCoordinates(float x, float y) {
	// StringBuilder coordinatesToString = new StringBuilder();
	// coordinatesToString.append(x);
	// coordinatesToString.append(", ");
	// coordinatesToString.append(y);
	// Log.v(TAG, "Coordinates: " + coordinatesToString.toString());
	// }
	//
	// private void printImageCoordinates() {
	// printCoordinates(imageToMove.getX(), imageToMove.getY());
	// }
}
