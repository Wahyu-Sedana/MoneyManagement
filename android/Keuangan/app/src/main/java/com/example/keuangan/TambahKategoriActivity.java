package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityTambahKategoriBinding;
import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKategoriActivity extends AppCompatActivity {

    private ActivityTambahKategoriBinding binding;

    private SessionManager sessionManager;
    private int userId;

    private int kategoriId;
    private String namaKategori;
    private boolean pemasukan;
    private boolean pengeluaran;
    private int jenisId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            binding = ActivityTambahKategoriBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            sessionManager = new SessionManager(this);
            userId = sessionManager.getUserId();

            Intent terima = getIntent();
            kategoriId = terima.getIntExtra("kategoriId", 0);
            namaKategori = terima.getStringExtra("namaKategori");
            jenisId = terima.getIntExtra("jenisId", 0);

            binding.inputKategori.setText(namaKategori);

            namaKategori = binding.inputKategori.getText().toString().trim();
            pemasukan = binding.radioButtonPemasukan.isChecked();
            pengeluaran = binding.radioButtonPengeluaran.isChecked();
            boolean isFormEmpty = namaKategori.isEmpty() && !pemasukan && !pengeluaran;

            if(jenisId == 1){
                binding.radioButtonPemasukan.setChecked(true);
            }else if (jenisId == 2){
                binding.radioButtonPengeluaran.setChecked(true);
            }


            getSupportActionBar().setTitle("Tambah Kategori");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            binding.btnAddKategori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(isFormEmpty){
                       addData();
                   }else {
                       updateData();
                   }
                }
            });
        }

        private void updateData() {
            String updatedNamaKategori = binding.inputKategori.getText().toString().trim();
            int updatedJenisId = binding.radioButtonPemasukan.isChecked() ? 1 : 2;

            ApiClient.updateDataKategori(userId, updatedNamaKategori, updatedJenisId, kategoriId, new Callback<KategoriResponse>() {
                @Override
                public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        KategoriResponse kategoriResponse = response.body();
                        if(kategoriResponse.isSuccess() && kategoriResponse != null){
                            new AlertDialog.Builder(TambahKategoriActivity.this)
                                    .setTitle("Success")
                                    .setMessage("Berhasil update item kategori")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getApplicationContext(), "Terimakasih!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(TambahKategoriActivity.this, SemuaKategoriActivity.class));
                                        }
                                    }).show();
                        }
                    } else {
                        // Tampilkan pesan error jika response tidak sukses
                        Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<KategoriResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "please try again!", Toast.LENGTH_SHORT).show();
                    Log.e("error", t.getMessage(), t);
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
                                            startActivity(new Intent(TambahKategoriActivity.this, SemuaKategoriActivity.class));
                                        }
                                    }).show();
                        }
                    } else {
                        // Tangani kesalahan respons
                        Toast.makeText(getApplicationContext(), "please try again!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<KategoriResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "please try again!", Toast.LENGTH_SHORT).show();
                    Log.e("error", t.getMessage(), t);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // handle the back button click
            startActivity(new Intent(TambahKategoriActivity.this, SemuaKategoriActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}