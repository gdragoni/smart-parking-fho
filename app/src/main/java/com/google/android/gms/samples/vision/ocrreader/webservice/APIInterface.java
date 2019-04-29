
package com.google.android.gms.samples.vision.ocrreader.webservice;

import com.google.android.gms.samples.vision.ocrreader.models.SendParkingsRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("/api/sendParkings")
    Call<Void> sendParkings(@Body SendParkingsRequest request);
}
