package com.github.display.retrofitservice;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private Api myApi;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().retryOnConnectionFailure(true);
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);  // set your desired log level

    private RetrofitClient(String baseURL) {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            httpClient.connectTimeout(30, TimeUnit.MINUTES);
            httpClient.readTimeout(30, TimeUnit.MINUTES);
        }
        Retrofit retrofit = new Retrofit.Builder().client(httpClient.build()).baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }

    public static synchronized RetrofitClient getInstance(String baseURL) {
        instance = new RetrofitClient(baseURL);

        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
}