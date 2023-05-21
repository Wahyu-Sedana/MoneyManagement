package com.example.keuangan.services;

import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.models.LoginResponse;
import com.example.keuangan.models.RegisterResponse;
import com.example.keuangan.models.TransaksiResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("transaksi/read.php")
    Call<TransaksiResponse> getData(
            @Query("id_user") int userId
//            @Field("tgl_dari") String startDate,
//            @Field("tgl_sampai") String endDate
    );

    @GET("transaksi/read.php")
    Call<TransaksiResponse> getDataFilter(
            @Query("id_user") int userId,
            @Field("tgl_dari") String startDate,
            @Field("tgl_sampai") String endDate
    );

    @FormUrlEncoded
    @POST("transaksi/add.php")
    Call<TransaksiResponse> addTransaksi(
            @Field("id_kategori") int idKategori,
            @Field("jumlah") int jumlah,
            @Field("catatan") String catatan,
            @Field("tanggal") String tanggal
    );

    @GET("kategori/read.php")
    Call<KategoriResponse> getDataKategori(
            @Query("id_user") int userId
    );

    @FormUrlEncoded
    @POST("kategori/delete.php")
    Call<KategoriResponse> deleteDataKategori(
            @Field("id_kategori") int kategoriId
    );
}
