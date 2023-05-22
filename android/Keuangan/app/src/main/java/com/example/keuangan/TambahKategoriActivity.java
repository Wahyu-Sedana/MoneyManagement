package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityTambahKategoriBinding;
import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKategoriActivity extends AppCompatActivity {

    private ActivityTambahKategoriBinding binding;

    private SessionManager sessionManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTambahKategoriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        getSupportActionBar().setTitle("Tambah Kategori");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        binding.btnAddKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    private void addData() {
        String namaKategori = binding.inputKategori.getText().toString();

        // Mendapatkan jenis ID yang dipilih dari radio button
        int jenisId = -1; // Inisialisasi jenis ID
        int selectedRadioButtonId = binding.radioGroupJenis.getCheckedRadioButtonId();
        switch (selectedRadioButtonId) {
            case R.id.radioButtonPemasukan:
                jenisId = 1; // Jenis ID untuk pemasukan
                break;
            case R.id.radioButtonPengeluaran:
                jenisId = 2; // Jenis ID untuk pengeluaran
                break;
        }

        // Lakukan pemanggilan API tambah data
        if (jenisId != -1) {
            // Lakukan pemanggilan API tambah data menggunakan Retrofit
            ApiClient.addItemKategori(userId, namaKategori, jenisId, new Callback<KategoriResponse>() {
                @Override
                public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                    if (response.isSuccessful()) {
                        KategoriResponse addDataResponse = response.body();
                        if(addDataResponse.isSuccess() && addDataResponse != null){
                            new AlertDialog.Builder(TambahKategoriActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Berhasil menambahkan item kategori")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getApplicationContext(), "Terimakasih!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                        }
                    } else {
                        // Tangani kesalahan respons
                    }
                }

                @Override
                public void onFailure(Call<KategoriResponse> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // handle the back button click
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}