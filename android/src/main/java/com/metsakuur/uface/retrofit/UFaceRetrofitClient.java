package com.metsakuur.uface.retrofit;

import com.metsakuur.uface.UFaceConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UFaceRetrofitClient {
    public static volatile UFaceRetrofitClient instance;

    //region Singleton
    public static UFaceRetrofitClient getInstance() {
        if (instance == null)
            instance = new UFaceRetrofitClient();
        return instance;
    }
    private UFaceRetrofitInterface uFaceRetrofitInterface = null;
    private Retrofit retrofit = null;
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).connectTimeout(5, TimeUnit.SECONDS);

    public Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(UFaceConfig.getInstance().BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public UFaceRetrofitInterface getRetrofitInterface() {
        if (uFaceRetrofitInterface == null) {
            uFaceRetrofitInterface = getClient().create(UFaceRetrofitInterface.class);
        }
        return uFaceRetrofitInterface;
    }
}
