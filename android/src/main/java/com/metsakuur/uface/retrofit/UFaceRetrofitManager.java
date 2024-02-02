package com.metsakuur.uface.retrofit;


import com.metsakuur.uface.UFaceConfig;
import com.metsakuur.ufacedetectormango.util.UFaceLogger;

import kotlin.jvm.JvmOverloads;
import retrofit2.Call;
import retrofit2.Callback;


public class UFaceRetrofitManager {

    public static volatile UFaceRetrofitManager instance;

    //region Singleton
    public static UFaceRetrofitManager getInstance() {
        if (instance == null) {
            instance = new UFaceRetrofitManager();
            return instance;
        }
        return instance;
    }

    @JvmOverloads
    public void requestApiRetrofit(
            String apiName,
            UFaceRequestData faceRequestData,
            Callback<UFaceAPIResult> callback
    ) {

        UFaceAPIRequest apiRequest = new UFaceAPIRequest();
        apiRequest.setApiName(apiName);
        apiRequest.setService(UFaceConfig.getInstance().service_name);
        apiRequest.setParam(faceRequestData);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Call<UFaceAPIResult> call = UFaceRetrofitClient.getInstance().getRetrofitInterface().requestApi(UFaceConfig.getInstance().TOKEN, apiRequest);
                call.enqueue(callback);
            }
        });
        thread.start();

    }
}

