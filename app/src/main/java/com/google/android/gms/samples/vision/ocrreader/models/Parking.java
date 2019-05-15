package com.google.android.gms.samples.vision.ocrreader.models;

import com.google.gson.annotations.SerializedName;

public class Parking {
    @SerializedName("disp")
    public Integer disp;

    @SerializedName("pos")
    public String pos;

    public Parking(String pos) {
        this.pos = pos;
        this.disp = 1;
    }
}
