package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityUpdateTransaksiBinding;
import com.example.keuangan.models.Kategori;
import com.example.keuangan.models.KategoriResponse;
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
    private String catatan;
    private String total;
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
        total = terima.getStringExtra("total");
        tgl = terima.getStringExtra("tgl");
        kategori = terima.getStringExtra("kategori");

        binding.updateCatatan.setText(catatan);
        binding.updateJumlah.setText(total);
        binding.updateTanggal.setText(tgl);


        catatan = binding.updateCatatan.getText().toString();
        total = binding.updateJumlah.getText().toString().trim();
        tgl = binding.updateTanggal.getText().toString().trim();

        setUpSpinner();
        showDatePicker();
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

            }
        });
    }

    private int getKategoriPosition(String kategori) {
        if (kategoriList != null && !kategoriList.isEmpty()) {
            for (int i = 0; i < kategoriList.size(); i++) {
                if (kategoriList.get(i).getKategori().equals(kategori)) {
                    return i;
                }
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
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

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