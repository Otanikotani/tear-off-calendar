package com.tearoffcalendar.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.android.lifecycle.R;

public class LimitExceededDialogActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_limit_exceeded_dialog);
    }

    public void finishDialog(View v) {
        LimitExceededDialogActivity.this.finish();
    }    
}
