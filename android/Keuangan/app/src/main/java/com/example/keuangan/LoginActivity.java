package com.example.keuangan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityLoginBinding;
import com.example.keuangan.models.LoginResponse;
import com.example.keuangan.models.RegisterResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        sessionManager = new SessionManager(this);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loginUser();
            }
        });

        binding.lForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        binding.linkregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    private void loginUser() {
        boolean valid = true;
        String email, password;

        email = binding.lEmail.getText().toString().trim();
        password = binding.lPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.lEmail.setError("Email harus diisi");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.lEmail.setError("Email tidak valid");
            valid = false;
        } else if (TextUtils.isEmpty(password)) {
            binding.lPassword.setError("Password harus diisi");
            valid = false;
        } else if (password.length() < 4) {
            binding.lPassword.setError("Password harus memiliki setidaknya 4 karakter");
            valid = false;
        }  else {
            ApiClient.loginUser(email, password, new Callback<LoginResponse>() {


                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse.isSuccess() == true && loginResponse != null) {
                            // Jika register berhasil, lakukan sesuatu
                            String message = loginResponse.getMessage();
                            sessionManager.saveLoginDetails(email, password);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            // Jika register gagal, lakukan sesuatu
                            String message = loginResponse.getMessage();
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
//                    Toast.makeText(RegisterActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                    } else {
                        // Jika response gagal, lakukan sesuatu
                        Toast.makeText(LoginActivity.this, "Failed to logim", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                    Log.e("error", t.getMessage(), t);
                }
            });
        }
    }
}