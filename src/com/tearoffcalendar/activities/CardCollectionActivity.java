package com.tearoffcalendar.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.android.lifecycle.R;

public class CardCollectionActivity extends Activity {
	
	private static final String TAG = "CardCollectionActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_collection);
        
        //Show every logged card.
        Log.v(TAG, "Show every logged card!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_card_collection, menu);
        return true;
    }
}
