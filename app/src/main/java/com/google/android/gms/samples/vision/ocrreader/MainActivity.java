/*
 * Copyright (C) The Android Open Source Project
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

package com.google.android.gms.samples.vision.ocrreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.ocrreader.models.SendParkingsRequest;
import com.google.android.gms.samples.vision.ocrreader.webservice.APIClient;
import com.google.android.gms.samples.vision.ocrreader.webservice.APIInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * recognizes text.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // Use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;
    private Button button;

    private String regex = "@\\d+";
    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";
    Timer timer;
    Integer timeToRunCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.status_message);
        textValue = (TextView)findViewById(R.id.text_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        button = findViewById(R.id.read_text);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(timer == null) {
            openCamera();
        } else {
            cancelTimer();
        }
    }

    void openCamera() {
        Intent intent = new Intent(this, OcrCaptureActivity.class);
        intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
        intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

        startActivityForResult(intent, RC_OCR_CAPTURE);
    }

    class CameraTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    statusMessage.setText("Run in " + timeToRunCamera);
                }
            });

            if(timeToRunCamera == 0) {
                cancelTimer();
                openCamera();
            }
            timeToRunCamera--;
        }
    };

    void cancelTimer() {
        timer.cancel();
        timer.purge();
        timer = null;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                button.setText("DETECT TEXT");
                statusMessage.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    ArrayList<String> allText = data.getStringArrayListExtra(OcrCaptureActivity.TextBlockObject);
                    ArrayList<String> allTextFiltered = new ArrayList<>();
                    for(String entry : allText) {
                        if(entry.matches(regex) && !allTextFiltered.contains(entry)) {
                            allTextFiltered.add(entry);
                        }
                    }
                    this.textValue.setText(TextUtils.join(",", allTextFiltered));
//                    HashSet<String> hashSet = new HashSet<String>();
//                    hashSet.addAll(allText);
//                    allText.clear();
//                    allText.addAll(hashSet);
//                    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
//
//                    Call<Void> call = apiInterface.sendParkings(new SendParkingsRequest(allText));
//
//                    call.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            call.cancel();
//                        }
//                    });
                    timeToRunCamera = 5;
                    timer = new Timer();
                    timer.schedule(new CameraTimerTask(), 0, 1000);
                    button.setText("STOP TIMER");
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}