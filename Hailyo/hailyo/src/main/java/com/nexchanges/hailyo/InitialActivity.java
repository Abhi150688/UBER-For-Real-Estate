package com.nexchanges.hailyo;

import android.os.Bundle;

/**
 * Copyright (C) 2014 Twitter Inc and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.Intent;

import com.nexchanges.hailyo.model.SessionRecorder;
import com.nexchanges.hailyo.model.SharedPrefs;
import com.twitter.sdk.android.Twitter;

import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.Session;


public class InitialActivity extends Activity {

    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Session activeSession = SessionRecorder.recordInitialSessionState(
                Twitter.getSessionManager().getActiveSession(),
                Digits.getSessionManager().getActiveSession()
        );

        role = SharedPrefs.getString(this, SharedPrefs.MY_ROLE_KEY);



        if (activeSession != null && role.equalsIgnoreCase("customer")) {
            startCustomerActivity();
        }
        else if (activeSession != null && role.equalsIgnoreCase("broker")) {
            startBrokerActivity();
        }
        else {
            startLoginActivity();
        }

    }

    private void startCustomerActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity1.class));
    }

    private void startBrokerActivity() {
        startActivity(new Intent(this, MainBrokerActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
       }
}
