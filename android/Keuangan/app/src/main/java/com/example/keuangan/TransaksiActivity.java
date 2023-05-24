package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityTransaksiBinding;
import com.example.keuangan.models.Kategori;
import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaksiActivity extends AppCompatActivity {

    private ActivityTransaksiBinding binding;

    private SessionManager sessionManager;
    private DatePickerDialog datePickerDialog;

    private List<Kategori> kategoriList;

    private int userId;
    private String email;
    private String namaUsaha;

    private String alamat;
    private String password_lama;
    private String password_baru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTransaksiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);

        // Ambil data pengguna dari SharedPrefsManager
        userId = sessionManager.getUserId();
        email = sessionManager.getEmail();
        namaUsaha = sessionManager.getNamaUsaha();

        binding.tTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        binding.semuaKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransaksiActivity.this, SemuaKategoriActivity.class));
            }
        });

        setUpSpinner();

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    private void addData() {

        int selectedPosition = binding.spinner.getSelectedItemPosition();
        Kategori selectedKategori =  kategoriList.get(selectedPosition);
        int idKategori = selectedKategori.getId_kategori();
        int jumlah = Integer.parseInt(binding.addJumlah.getText().toString());
        String catatan = binding.addCatatan.getText().toString();
        Date tanggal = new Date();

        // Format tanggal menjadi String menggunakan SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String tanggalString = dateFormat.format(tanggal);

        ApiClient.addTransaksi(idKategori, jumlah, catatan, tanggalString, new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse addDataResponse = response.body();
                    if(addDataResponse.isSuccess() && addDataResponse != null){
                        new AlertDialog.Builder(TransaksiActivity.this)
                                .setTitle("Success")
                                .setMessage("Berhasil menambahkan data")
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
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {

            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(TransaksiActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Tangkap tanggal yang dipilih
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                        // Tampilkan tanggal yang dipilih di EditText
                        binding.tTanggal.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Tampilkan DatePickerDialog
        datePickerDialog.show();
    }

    private void setUpSpinner() {
        ApiClient.loadDataKategori(userId, new Callback<KategoriResponse>() {

            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                if (response.isSuccessful()) {
                    KategoriResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        kategoriList = apiResponse.getData();

                        // Membuat adapter untuk Spinner dengan data kategori
                        ArrayAdapter<Kategori> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, kategoriList);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // Mengatur adapter pada Spinner
                        binding.spinner.setAdapter(spinnerAdapter);
                    } else {
                        // Gagal memuat data kategori
                        Toast.makeText(getApplicationContext(), "Gagal memuat data kategori", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Tangani kesalahan dalam respons API
                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat mengirim permintaan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // handle the back button click
            startActivity(new Intent(TransaksiActivity.this, HomeActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}