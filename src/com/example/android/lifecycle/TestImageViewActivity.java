package com.example.android.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.lifecycle.util.StatusTracker;

public class TestImageViewActivity extends Activity implements OnTouchListener {

	private StatusTracker mStatusTracker = StatusTracker.getInstance();
	private ImageView imageToMove;

	int mX, mY;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_image_view);

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
    	
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        
        StringBuilder coordinatesToString = new StringBuilder();
        coordinatesToString.append(x);
        coordinatesToString.append(", ");
        coordinatesToString.append(y);
        String xy = coordinatesToString.toString();
        Log.w("Coordinates:", xy);
        
        StringBuilder imageCoordinatesToString = new StringBuilder();
        imageCoordinatesToString.append(imageToMove.getX());
        imageCoordinatesToString.append(", ");
        imageCoordinatesToString.append(imageToMove.getY());
        String imageXY = imageCoordinatesToString.toString();
        Log.w("Image view coordinates: ", imageXY);
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
            	Log.w("Touch:", "Action pointer down!");
                break;
            case MotionEvent.ACTION_POINTER_UP:
            	Log.w("Touch:", "Action pointer up!");
                break;
            case MotionEvent.ACTION_MOVE:
            	float deltaY = y - mY;
            	imageToMove.setY(deltaY);
            	imageToMove.invalidate();
            	Log.w("Touch:", "Action move!");
            	StringBuilder yDifferenceToString = new StringBuilder();
            	yDifferenceToString.append(deltaY);
            	Log.w("Touch:", yDifferenceToString.toString());
                break;
        }    	
    	return true;
    }
}
