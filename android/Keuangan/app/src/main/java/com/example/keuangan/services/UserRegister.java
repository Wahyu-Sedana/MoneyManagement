package com.example.keuangan.services;

import com.example.keuangan.models.LoginResponse;
import com.example.keuangan.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserRegister {

    @FormUrlEncoded
    @POST("register")
    Call<RegisterResponse> registerUser(
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @GET("login")
    Call<LoginResponse> loginResponse(
            @Field("email") String email,
            @Field("password") String password
    );
}
