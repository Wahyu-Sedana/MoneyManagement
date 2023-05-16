package com.example.keuangan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.keuangan.databinding.ActivityProfileBinding;

public class AddDataActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}