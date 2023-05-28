package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.keuangan.databinding.ActivityHomeBinding;
import com.example.keuangan.models.Kategori;
import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;
import com.example.keuangan.uiFragment.OverviewFragment;
import com.example.keuangan.uiFragment.StatisticFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TransaksiActivity.class));
            }
        });


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
            case R.id.navigation_statistic:
               replaceFragment(new StatisticFragment());
                break;
        }
        return true;
    }
}