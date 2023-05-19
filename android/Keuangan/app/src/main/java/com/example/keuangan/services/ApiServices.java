package com.example.keuangan.services;

import com.example.keuangan.models.LoginResponse;
import com.example.keuangan.models.RegisterResponse;
import com.example.keuangan.models.TransaksiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServices {

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> registerUser(
            @Field("nama_usaha") String nama,
            @Field("alamat") String alamat,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> loginResponse(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("transaksi/")
    Call<TransaksiResponse> getData(
            @Field("id_user") String userId,
            @Field("tgl_dari") String startDate,
            @Field("tgl_sampai") String endDate
    );
}
