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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.lifecycle.R;

public class FaceUpCardFragment extends ListFragment {
    OnHeadlineSelectedListener mCallback;
    private List<String> cardNames;
    
    private static final String TAG = "FaceUpCardFragment";

	// The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onCardSelected(Date date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        updateListView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "On start");
        updateListView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
    	String string = cardNames.get(position);
    	Log.v(TAG, "onListItemClick Card name: " + string);
        SimpleDateFormat dateFormat = new SimpleDateFormat(Card.DATE_FORMAT); 
        try {
        	Date convertedDate = dateFormat.parse(string);
        	mCallback.onCardSelected(convertedDate);
        } catch (ParseException pe) {
        	Log.e(TAG, pe.getMessage());
        }
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
    
    public List<String> getCardNames() {
		return cardNames;
	}

	public void setCardNames(List<String> cardNames) {
		this.cardNames = cardNames;
	}
	
	public void resetCardNames() {
		this.cardNames = new ArrayList<String>();
		updateListView();
	}    
    
    public void updateListView() {
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        // Create an array adapter for the list view
        Log.v(TAG, cardNames.toString());
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, cardNames));
    }
    
}