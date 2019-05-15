
package com.google.android.gms.samples.vision.ocrreader.webservice;

import com.google.android.gms.samples.vision.ocrreader.models.Parking;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("vagas")
    Call<Void> sendParkings(@Body ArrayList<Parking> request);
}
