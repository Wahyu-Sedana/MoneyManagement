package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.keuangan.databinding.ActivityHomeBinding;
import com.example.keuangan.uiFragment.DataFragment;
import com.example.keuangan.uiFragment.OverviewFragment;
import com.example.keuangan.uiFragment.StatisticFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new OverviewFragment()).commit();
                return true;
            case R.id.navigation_tambahData:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new DataFragment()).commit();
                return true;
            case R.id.navigation_statistic:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new StatisticFragment()).commit();
                return true;
        }
        return false;
    }
}