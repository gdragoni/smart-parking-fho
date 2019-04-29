
package com.google.android.gms.samples.vision.ocrreader.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SendParkingsRequest {

    @SerializedName("allParkings")
    public ArrayList<String> allParkings;

    public SendParkingsRequest(ArrayList<String> allParkings) {
        this.allParkings = allParkings;
    }


}
