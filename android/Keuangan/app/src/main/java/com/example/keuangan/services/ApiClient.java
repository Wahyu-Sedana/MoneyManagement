package com.example.keuangan.services;

import com.example.keuangan.models.LoginResponse;
import com.example.keuangan.models.RegisterResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2/pencatatan/api_mobile/";
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void registerUser(String nama, String alamat, String email, String password, Callback<RegisterResponse> callback) {
        ApiServices apiInterface = getApiClient().create(ApiServices.class);
        Call<RegisterResponse> call = apiInterface.registerUser(nama, alamat, email, password);
        call.enqueue(callback);
    }

    public static void loginUser(String email, String password, Callback<LoginResponse> callback) {
        ApiServices apiInterface = getApiClient().create(ApiServices.class);
        Call<LoginResponse> call = apiInterface.loginResponse(email, password);
        call.enqueue(callback);
    }
}
