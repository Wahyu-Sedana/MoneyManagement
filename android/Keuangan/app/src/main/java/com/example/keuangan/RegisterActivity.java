package com.example.keuangan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityRegisterBinding;
import com.example.keuangan.models.RegisterResponse;
import com.example.keuangan.services.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        binding.linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


    }

    private void registerUser() {
        boolean valid = true;
        String nama, alamat, email, password, cPassword;

        nama = binding.tNama.getText().toString();
        alamat = binding.tAlamat.getText().toString();
        email = binding.tEmail.getText().toString().trim();
        password = binding.tPassword.getText().toString().trim();
        cPassword = binding.tCPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nama)) {
            binding.tNama.setError("Nama harus diisi");
            valid = false;
        }else if (TextUtils.isEmpty(alamat)) {
            binding.tAlamat.setError("Alamat harus diisi");
            valid = false;
        } else if (TextUtils.isEmpty(email)) {
            binding.tEmail.setError("Email harus diisi");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tEmail.setError("Email tidak valid");
            valid = false;
        } else if (TextUtils.isEmpty(password)) {
            binding.tPassword.setError("Password harus diisi");
            valid = false;
        } else if (password.length() < 4) {
            binding.tPassword.setError("Password harus memiliki setidaknya 4 karakter");
            valid = false;
        } else if (!password.equals(cPassword)) {
            binding.tPassword.setError("Password tidak cocok");
            binding.tCPassword.setError("Password tidak cocok");
            valid = false;
        } else {
            ApiClient.registerUser(nama, alamat, email, password, new Callback<RegisterResponse>() {

                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        RegisterResponse registerResponse = response.body();
                        if (registerResponse.isSuccess() == true) {
                            // Jika register berhasil, lakukan sesuatu
                            String message = registerResponse.getMessage();
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            // Jika register gagal, lakukan sesuatu
                            String message = registerResponse.getMessage();
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
//                    Toast.makeText(RegisterActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                    } else {
                        // Jika response gagal, lakukan sesuatu
                        Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                    Log.e("error", t.getMessage(), t);
                }
            });
        }
    }
}