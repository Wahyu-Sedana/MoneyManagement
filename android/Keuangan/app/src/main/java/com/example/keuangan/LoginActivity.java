package com.example.keuangan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidate()){
                    // jika form sudah diisi dengan benar
                    Toast.makeText(getApplicationContext(), "Berhasil Login", Toast.LENGTH_LONG).show();
                }else {
                    // jika form belum disii dengan benar
                    Toast.makeText(getApplicationContext(), "gagal Login", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.linkregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private boolean isValidate() {
        boolean valid = true;

        TextInputEditText tvEmail = binding.lEmail;
        TextInputEditText tvPassword = binding.lPassword;

        String inputEmail = tvEmail.getText().toString().trim();
        String inputPassword = tvPassword.getText().toString().trim();


        if (TextUtils.isEmpty(inputEmail)) {
            tvEmail.setError("Email harus diisi");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            tvEmail.setError("Email tidak valid");
            valid = false;
        } else if (TextUtils.isEmpty(inputPassword)) {
            tvPassword.setError("Password harus diisi");
            valid = false;
        } else if (inputPassword.length() < 6) {
            tvPassword.setError("Password harus memiliki setidaknya 6 karakter");
            valid = false;
        } else {

        }

        return valid;
    }
}