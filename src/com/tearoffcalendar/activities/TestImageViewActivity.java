package com.tearoffcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.example.android.lifecycle.R;
import com.example.android.lifecycle.util.StatusTracker;

public class TestImageViewActivity extends Activity implements OnTouchListener {

	private StatusTracker mStatusTracker = StatusTracker.getInstance();
	private ImageView imageToMove;

	private float bottomLimitToMove;
	float mX, mY;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_image_view);

		// Get screen height to determine when the limit
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		bottomLimitToMove = (float) (size.y - size.y * 0.50);
		Log.w("SCREEN HEIGHT:", String.valueOf(bottomLimitToMove));

		Log.w("Tag", "On create");
		imageToMove = (ImageView) findViewById(R.id.imageView1);
		imageToMove.setOnTouchListener(this);
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
			Log.w("Touch:", "Action down!");
			mX = x;
			mY = y;
			break;
		case MotionEvent.ACTION_UP:
			Log.w("Touch:", "Action up!");
			break;
		case MotionEvent.ACTION_POINTER_DOWN:

			break;
		case MotionEvent.ACTION_POINTER_UP:
			Log.w("Touch:", "Action pointer up!");
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaY = y - mY;
			imageToMove.setY(deltaY);
			imageToMove.invalidate();
			StringBuilder yDifferenceToString = new StringBuilder();
			yDifferenceToString.append(deltaY);

			// printImageCoordinates();
			Log.w("Image y:", String.valueOf(imageToMove.getY()));
			if (imageToMove.getY() >= bottomLimitToMove) {
				Log.w("Activity:", "Starting TextCardActivity!");
				Intent intent = new Intent(TestImageViewActivity.this,
						TextCardActivity.class);
				startActivity(intent);
			}
			break;
		}
		return true;
	}

	private void printCoordinates(float x, float y) {
		StringBuilder coordinatesToString = new StringBuilder();
		coordinatesToString.append(x);
		coordinatesToString.append(", ");
		coordinatesToString.append(y);
		Log.w("Coordinates: ", coordinatesToString.toString());
	}

	private void printImageCoordinates() {
		printCoordinates(imageToMove.getX(), imageToMove.getY());
	}
}
