package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import com.example.keuangan.adapters.KategoriAdapter;
import com.example.keuangan.databinding.ActivitySemuaKategoriBinding;
import com.example.keuangan.models.Kategori;
import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SemuaKategoriActivity extends AppCompatActivity {

    private ActivitySemuaKategoriBinding binding;
    private KategoriAdapter adapter;
    private SessionManager sessionManager;

    private int userId;
    private int idJenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySemuaKategoriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kategori");


        adapter = new KategoriAdapter(this, new ArrayList<>());

        binding.listKategori.setAdapter(adapter);
        binding.listKategori.setLayoutManager(new LinearLayoutManager(this));
        loadKategori();

        binding.lAddKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SemuaKategoriActivity.this, TambahKategoriActivity.class));
            }
        });


    }


    private void loadKategori() {
        ApiClient.loadListKategori(userId, new Callback<KategoriResponse>() {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                if(response.isSuccessful());
                KategoriResponse kategoriResponse = response.body();
                if(kategoriResponse.isSuccess() && kategoriResponse != null){
                    List<Kategori> kategoriList = kategoriResponse.getData();
                    adapter.setKategoriList(kategoriList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {
                Log.e("error", t.getMessage(), t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // handle the back button click
            startActivity(new Intent(SemuaKategoriActivity.this, HomeActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}