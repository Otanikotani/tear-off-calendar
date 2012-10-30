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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.android.lifecycle.R;

public class CardFragment extends Fragment {
	final static String CARD_WEB_VIEW_KEY = "CardFragment.cardWebView";
	private String cardWebView;
	
	private static final String TAG = "CardFragment";

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
		return inflater.inflate(R.layout.history_card, container, false);
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

	public void updateCardView(String str) {
		WebView mMainView = (WebView) getActivity().findViewById(R.id.card);
		mMainView.loadDataWithBaseURL("file:///android_res/raw/", str,
				"text/html", "utf-8", null);
//		mMainView.setBackgroundColor(0);
//        TextView article = (TextView) getActivity().findViewById(R.id.card);
//        article.setText(str);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		Log.v(TAG, "onSaveInstanceState");

		// Save the current article selection in case we need to recreate the
		// fragment
		outState.putString(CARD_WEB_VIEW_KEY, cardWebView);
	}
}