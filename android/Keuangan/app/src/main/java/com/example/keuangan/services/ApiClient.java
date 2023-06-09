package com.example.keuangan.services;

import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.models.LoginResponse;
import com.example.keuangan.models.ProfileResponse;
import com.example.keuangan.models.RegisterResponse;
import com.example.keuangan.models.TransaksiResponse;
import com.google.gson.JsonElement;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2/pencatatan/api_mobile/";
    private static final String BASE_URL_KOMPUTER = "http://ipadd/pencatatan/api_mobile/";
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

    public static void loadTransaksi(int userId, Callback<TransaksiResponse> callback) {
        ApiServices apiInterface = getApiClient().create(ApiServices.class);
        Call<TransaksiResponse> call = apiInterface.getData(userId);
        call.enqueue(callback);
    }

    public static void addTransaksi(int idKatergori, int jumlah, String catatan, String tanggal, Callback<TransaksiResponse> callback) {
        ApiServices apiInterface = getApiClient().create(ApiServices.class);
        Call<TransaksiResponse> call = apiInterface.addTransaksi(idKatergori, jumlah, catatan, tanggal);
        call.enqueue(callback);
    }

    public static void loadDataCard(int userId, Callback<TransaksiResponse> callback) {
        ApiServices apiInterface = getApiClient().create(ApiServices.class);
        Call<TransaksiResponse> call = apiInterface.getData(userId);
        call.enqueue(callback);
    }

    public static void updateTransaksi(String idTransaksi, int idKategori, int jumlah, String catatan, String tanggal, Callback<TransaksiResponse> callback) {
        ApiServices apiServices = getApiClient().create(ApiServices.class);
        Call<TransaksiResponse> call = apiServices.updateTransaksi(idTransaksi, idKategori, jumlah, catatan, tanggal);
        call.enqueue(callback);
    }

    public static void deleteTransaksi(String idTransaksi, Callback<TransaksiResponse> callback) {
        ApiServices apiServices = getApiClient().create(ApiServices.class);
        Call<TransaksiResponse> call = apiServices.deleteTransaksi(idTransaksi);
        call.enqueue(callback);
    }

    public static void loadDataKategori(int userId, Callback<KategoriResponse> callback) {
        ApiServices apiInterface = getApiClient().create(ApiServices.class);
        Call<KategoriResponse> call = apiInterface.getDataKategori(userId);
        call.enqueue(callback);
    }

    public static void loadListKategori(int userId, Callback<KategoriResponse> callback) {
        ApiServices apiServices = getApiClient().create(ApiServices.class);
        Call<KategoriResponse> call = apiServices.getDataKategori(userId);
        call.enqueue(callback);
    }

    public static void deleteItemKategori(int kategoriId, Callback<KategoriResponse> callback) {
        ApiServices apiServices = getApiClient().create(ApiServices.class);
        Call<KategoriResponse> call = apiServices.deleteDataKategori(kategoriId);
        call.enqueue(callback);
    }

    public static void addItemKategori(int userId, String namaKategori, int jenisId, Callback<KategoriResponse> callback) {
        ApiServices apiServices = getApiClient().create(ApiServices.class);
        Call<KategoriResponse> call = apiServices.addDataKategori(userId, namaKategori, jenisId);
        call.enqueue(callback);
    }

    public static void updateDataKategori(int userId, String namaKategori, int jenisId, int kategoriId, Callback<KategoriResponse> callback) {
        ApiServices apiServices = getApiClient().create(ApiServices.class);
        Call<KategoriResponse> call = apiServices.updateDataKategori(userId, namaKategori, jenisId, kategoriId);
        call.enqueue(callback);
    }

    public static void getUserData(int userId, Callback<JsonElement> callback) {
        ApiServices apiInterface = getApiClient().create(ApiServices.class);
        Call<JsonElement> call = apiInterface.getDataProfile(userId);
        call.enqueue(callback);
    }

    public static void updateProfile(int userId, String namaUsaha, String alamat, String email, String passLama, String passBaru, Callback<ProfileResponse> callback) {
        ApiServices apiInterface = getApiClient().create(ApiServices.class);
        Call<ProfileResponse> call = apiInterface.updateDataProfile(userId, namaUsaha, alamat, email, passLama, passBaru);
        call.enqueue(callback);
    }

    public static void getFilterTransaksi(int userId, String tglAwal, String tglAkhir, Callback<TransaksiResponse> callback) {
        ApiServices apiServices = getApiClient().create(ApiServices.class);
        Call<TransaksiResponse> call = apiServices.readDataFilter(userId, tglAwal, tglAkhir);
        call.enqueue(callback);
    }

    public static void getStatistik(int userId, Callback<TransaksiResponse> callback) {
        ApiServices apiServices = getApiClient().create(ApiServices.class);
        Call<TransaksiResponse> call = apiServices.statistkData(userId);
        call.enqueue(callback);
    }

    public static void downloadExcel(int userId, Callback<ResponseBody> callback) {
        ApiServices apiServices = getApiClient().create(ApiServices.class);
        Call<ResponseBody> call = apiServices.laporanExcel(userId);
        call.enqueue(callback);
    }
}
