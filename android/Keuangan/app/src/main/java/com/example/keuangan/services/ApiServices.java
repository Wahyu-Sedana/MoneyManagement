package com.example.keuangan.services;

import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.models.LoginResponse;
import com.example.keuangan.models.ProfileResponse;
import com.example.keuangan.models.RegisterResponse;
import com.example.keuangan.models.TransaksiResponse;
import com.google.gson.JsonElement;

import okhttp3.ResponseBody;
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
    );

    @FormUrlEncoded
    @POST("transaksi/add.php")
    Call<TransaksiResponse> addTransaksi(
            @Field("id_kategori") int idKategori,
            @Field("jumlah") int jumlah,
            @Field("catatan") String catatan,
            @Field("tanggal") String tanggal
    );

    @GET("transaksi/read.php")
    Call<TransaksiResponse> readDataFilter(
            @Query("id_user") int id_user,
            @Query("tgl_dari") String tglAwal,
            @Query("tgl_sampai") String tglAkhir
    );

    @FormUrlEncoded
    @POST("transaksi/update.php")
    Call<TransaksiResponse> updateTransaksi(
            @Field("id_transaksi") String idTransaksi,
            @Field("id_kategori") int kategori,
            @Field("jumlah") int jumlah,
            @Field("catatan") String catatan,
            @Field("tanggal") String tanggal
    );

    @FormUrlEncoded
    @POST("transaksi/delete.php")
    Call<TransaksiResponse> deleteTransaksi(
            @Field("id_transaksi") String idTransaksi
    );

    @GET("kategori/read.php")
    Call<KategoriResponse> getDataKategori(
            @Query("id_user") int userId
    );

    @FormUrlEncoded
    @POST("kategori/update.php")
    Call<KategoriResponse> updateDataKategori(
            @Field("id_user") int userId,
            @Field("kategori") String namaKategori,
            @Field("id_jenis") int jenisId,
            @Field("id_kategori") int kategoriId
    );

    @FormUrlEncoded
    @POST("kategori/delete.php")
    Call<KategoriResponse> deleteDataKategori(
            @Field("id_kategori") int kategoriId
    );

    @FormUrlEncoded
    @POST("kategori/add.php")
    Call<KategoriResponse> addDataKategori(
            @Field("id_user") int userId,
            @Field("kategori") String namaKategori,
            @Field("id_jenis") int jenisId
    );

    @GET("profile/read.php")
    Call<JsonElement> getDataProfile(
            @Query("id_user") int userId
    );

    @FormUrlEncoded
    @POST("profile/update.php")
    Call<ProfileResponse> updateDataProfile(
            @Field("id_user") int userId,
            @Field("nama_usaha") String namaUsaha,
            @Field("alamat") String alamat,
            @Field("email") String email,
            @Field("password_lama") String passLama,
            @Field("password_baru") String passBaru
    );

    @GET("transaksi/read.php")
    Call<TransaksiResponse> statistkData(
            @Query("id_user") int userId
    );

    @GET("laporan_excel.php")
    Call<ResponseBody> laporanExcel(
            @Query("id_user") int userId
    );

}
