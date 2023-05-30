package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.keuangan.databinding.ActivityHomeBinding;
import com.example.keuangan.session.SessionManager;
import com.example.keuangan.uiFragment.OverviewFragment;
import com.example.keuangan.uiFragment.LaporanFragment;
import com.example.keuangan.uiFragment.StatisticFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityHomeBinding binding;
    private SessionManager sessionManager;

    private int userId;
    private String email;
    private String namaUsaha;

    private String alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn() == false) {
            // Jika pengguna belum login, arahkan ke halaman Login
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Ambil data pengguna dari SharedPrefsManager
        userId = sessionManager.getUserId();
        email = sessionManager.getEmail();
        namaUsaha = sessionManager.getNamaUsaha();


        replaceFragment(new OverviewFragment());

        binding.bottomNavigationView.setBackground(null);


        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                replaceFragment(new OverviewFragment());
                break;
            case R.id.navigation_laporan:
                replaceFragment(new LaporanFragment());
                break;
            case R.id.navigation_stastistik:
                replaceFragment(new StatisticFragment());
                break;
        }
        return true;
    }
}