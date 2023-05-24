package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityProfileBinding;
import com.example.keuangan.models.ProfileResponse;
import com.example.keuangan.models.ProfileUser;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    private int userId;
    private String namaUsaha;
    private String email;
    private String alamat;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        loadDataProfile();
    }

    private void loadDataProfile() {
        ApiClient.getDataProfile(userId, new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.isSuccessful()){
                    ProfileResponse profileResponse = response.body();
                    if(profileResponse.isSuccess() && profileResponse.getUserProfile() != null){
                        ProfileUser profileUser = profileResponse.getUserProfile();
                        binding.viewNamaUsaha.setText(profileUser.getNamaUsaha());
                        binding.viewEmail.setText(profileUser.getEmail());
                        binding.viewAlamat.setText(profileUser.getAlamat());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("error", t.getMessage(), t);
            }
        });
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