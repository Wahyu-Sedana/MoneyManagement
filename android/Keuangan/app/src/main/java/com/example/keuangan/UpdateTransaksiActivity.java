package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityUpdateTransaksiBinding;
import com.example.keuangan.models.Kategori;
import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTransaksiActivity extends AppCompatActivity {

    private ActivityUpdateTransaksiBinding binding;

    private SessionManager sessionManager;
    private int userId;

    private DatePickerDialog datePickerDialog;

    private List<Kategori> kategoriList;

    private String kategori;
    private String idTransaksi;
    private String catatan;
    private int total;
    private String tgl;
    private int jenisId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateTransaksiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Update Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        Intent terima = getIntent();
        catatan = terima.getStringExtra("catatan");
        total = terima.getIntExtra("total", 0);
        tgl = terima.getStringExtra("tgl");
        kategori = terima.getStringExtra("kategori");
        idTransaksi = terima.getStringExtra("id_transaksi");

        binding.updateCatatan.setText(catatan);
        binding.updateJumlah.setText(String.valueOf(total));
        binding.updateTanggal.setText(tgl);

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        binding.updateTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        setUpSpinner();
    }

    private void updateData() {
        catatan = binding.updateCatatan.getText().toString();
        total = Integer.parseInt(binding.updateJumlah.getText().toString());
        tgl = binding.updateTanggal.getText().toString().trim();

        Kategori selectedKategori = (Kategori) binding.spinner.getSelectedItem();
        int idKategori = selectedKategori.getId_kategori();

        ApiClient.updateTransaksi(idTransaksi, idKategori, total, catatan, tgl, new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if(response.isSuccessful()){
                    TransaksiResponse transaksiResponse = response.body();
                    if(transaksiResponse.isSuccess() && transaksiResponse != null){
                        new AlertDialog.Builder(UpdateTransaksiActivity.this)
                                .setTitle("Success")
                                .setMessage("Berhasil update transaksi")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), "Terimakasih!", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                    } else {
                        // Tangani kesalahan respons
                    }
                }
            }

            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {

            }
        });
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

                        if (kategori != null) {
                            int kategoriPosition = getKategoriPosition(kategori);
                            if (kategoriPosition != -1) {
                                binding.spinner.setSelection(kategoriPosition);
                            }
                        }
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
                Toast.makeText(UpdateTransaksiActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                Log.e("error", t.getMessage(), t);
            }
        });
    }

    private int getKategoriPosition(String kategori) {
        for (int i = 0; i < kategoriList.size(); i++) {
            if (kategoriList.get(i).getKategori().equals(kategori)) {
                return i;
            }
        }
        return -1;
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(UpdateTransaksiActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Tangkap tanggal yang dipilih
                        String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;

                        // Tampilkan tanggal yang dipilih di EditText
                        binding.updateTanggal.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Tampilkan DatePickerDialog
        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // handle the back button click
            startActivity(new Intent(UpdateTransaksiActivity.this, HomeActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}