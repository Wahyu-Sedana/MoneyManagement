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

import com.example.keuangan.databinding.ActivityProfileBinding;
import com.example.keuangan.models.ProfileResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private SessionManager sessionManager;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Log out")
                        .setMessage("Apakah anda ingin log out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sessionManager.removeData();
                                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                                finish();
                                Toast.makeText(getApplicationContext(), "Terimakasih!", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        loadDataProfile();
    }

    private void updateData() {
        String namaUsaha = binding.updateNamaUsaha.getText().toString();
        String alamat = binding.updateAlamat.getText().toString();
        String email = binding.updateEmail.getText().toString();
        String passLama = binding.updatePasswordLama.getText().toString();
        String passBaru = binding.updatePasswordBaru.getText().toString();

        ApiClient.updateProfile(userId, namaUsaha, alamat, email, passLama, passBaru, new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.isSuccessful()){
                    ProfileResponse profileResponse = response.body();
                    if(profileResponse.isSuccess() && profileResponse != null){
                        new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("Success")
                                .setMessage("Berhasil update profile")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), "Terimakasih!", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                    } else {
                        // Tangani kesalahan respons
                        Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat mengirim permintaan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                Log.e("error", t.getMessage(), t);
            }
        });
    }

    private void loadDataProfile() {
        ApiClient.getUserData(userId, new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    if (jsonElement != null) {
                        String jsonResponse = jsonElement.toString();

                        Gson gson = new Gson();
                        ProfileResponse userResponse = gson.fromJson(jsonResponse, new TypeToken<ProfileResponse>(){}.getType());

                        if (userResponse.isSuccess() && userResponse.getData() != null && !userResponse.getData().isEmpty()) {
                            ProfileResponse.User user = userResponse.getData().get(0);
                            String namaUsaha = user.getNama_usaha();
                            String alamat = user.getAlamat();
                            String email = user.getEmail();

                            // Tampilkan data pada TextView
                            binding.updateNamaUsaha.setText(namaUsaha);
                            binding.updateAlamat.setText(alamat);
                            binding.updateEmail.setText(email);
                        } else {
                            // Tampilkan pesan error jika tidak berhasil
                            String message = userResponse != null ? userResponse.getMessage() : "Error";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                // Tangani kegagalan permintaan
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // handle the back button click
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}