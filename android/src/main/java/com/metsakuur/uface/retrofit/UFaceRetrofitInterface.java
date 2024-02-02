package com.metsakuur.uface.retrofit;

import com.metsakuur.uface.UFaceConfig;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UFaceRetrofitInterface {
    @POST("/api/invoke")
    @Headers({"Accept:application/json"})
    Call<UFaceAPIResult> requestApi(@Header("Authorization") String authorization, @Body @NotNull UFaceAPIRequest param);
}
