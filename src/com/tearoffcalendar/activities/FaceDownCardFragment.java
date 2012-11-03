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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.android.lifecycle.R;
import com.tearoffcalendar.activities.FaceUpCardFragment.OnHeadlineSelectedListener;

public class FaceDownCardFragment extends Fragment implements OnTouchListener {
	final static String CARD_WEB_VIEW_KEY = "FaceDownCardFragment.cardWebView";
	private String cardWebView;

	private static final String TAG = "FaceDownCardFragment";

	private float deltaY;
	private float clickStartedOnY;
	private static final float DELTA_Y_MAX = 100;

	// The container Activity must implement this interface so the frag can
	// deliver messages
	public interface OnFaceDownCardClickListener {
		public void onFaceDownCardClick();
	}

	private OnFaceDownCardClickListener onFaceDownCardClickListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null) {
			cardWebView = savedInstanceState.getString(CARD_WEB_VIEW_KEY);
		} else {
			cardWebView = new String();
		}
		Log.v(TAG, "OnCreateView");

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.face_down_card_fragment, container,
				false);
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.v(TAG, "OnStart");

		// During startup, check if there are arguments passed to the fragment.
		// onStart is a good place to do this because the layout has already
		// been
		// applied to the fragment at this point so we can safely call the
		// method
		// below that sets the article text.
		Bundle args = getArguments();
		if (args != null) {
			// Set article based on argument passed in
			updateCardView(args.getString(CARD_WEB_VIEW_KEY));
		} else if (cardWebView.isEmpty()) {
			// Set article based on saved instance state defined during
			// onCreateView
			updateCardView(cardWebView);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			onFaceDownCardClickListener = (OnFaceDownCardClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFaceDownCardClickListener");
		}
	}

	public void updateCardView(String str) {
		WebView mMainView = (WebView) getActivity().findViewById(
				R.id.face_down_card_fragment);
		mMainView.loadDataWithBaseURL("file:///android_res/raw/", str,
				"text/html", "utf-8", null);
		mMainView.setOnTouchListener(this);
		// mMainView.setBackgroundColor(0);
		// TextView article = (TextView) getActivity().findViewById(R.id.card);
		// article.setText(str);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Log.v(TAG, "onSaveInstanceState");
		// Save the current article selection in case we need to recreate the
		// fragment
		outState.putString(CARD_WEB_VIEW_KEY, cardWebView);
	}

	public boolean onTouch(View view, MotionEvent event) {
		// We need to register click here
		final float y = event.getRawY();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			deltaY = 0;
			clickStartedOnY = y;
			Log.v(TAG, "Click started on:" + String.valueOf(clickStartedOnY));
			break;
		case MotionEvent.ACTION_UP:
			Log.v(TAG, "Resulted deltaY:" + String.valueOf(deltaY));
			// This one is for when finger is up, not a rude gesture bro!
			if (deltaY >= DELTA_Y_MAX) {
				Log.v(TAG, "Greater than max of delta y - not a click!");
			} else {
				Log.v(TAG, "Smaller than max of delta y - a click!");
				if (null != onFaceDownCardClickListener) {
					Log.v(TAG, "Calling on face down card click listener");
					onFaceDownCardClickListener.onFaceDownCardClick();
				} else {
					Log.e(TAG, "On face down card click listener is missing!");
				}
			}
			deltaY = clickStartedOnY = 0;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			break;
		case MotionEvent.ACTION_POINTER_UP:
			break;
		case MotionEvent.ACTION_MOVE:
			deltaY += Math.abs(y - clickStartedOnY);
			Log.v(TAG, "Current delta: " + String.valueOf(deltaY));
			break;
		}
		return false;
	}
}