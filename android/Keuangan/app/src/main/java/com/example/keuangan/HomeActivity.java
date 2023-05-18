package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;

import com.example.keuangan.databinding.ActivityHomeBinding;
import com.example.keuangan.uiFragment.OverviewFragment;
import com.example.keuangan.uiFragment.StatisticFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityHomeBinding binding;
    private Animation slideUpAnimation;
    private DatePickerDialog datePickerDialog;
    private boolean isSlideUpVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        replaceFragment(new OverviewFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.tTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        binding.btnPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActive(binding.btnPemasukan);
                setButtonInactive(binding.btnPengeluaran);
            }
        });

        binding.semuaKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SemuaKategori.class));
            }
        });

        binding.btnPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActive(binding.btnPengeluaran);
                setButtonInactive(binding.btnPemasukan);
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSlideUpVisible){
                    hideSlideUpLayout();
                }else {
                    showSlideUpLayout();
                }
            }
        });

        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slideup_animation);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);

        setUpSpinner();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(HomeActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Tangkap tanggal yang dipilih
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                        // Tampilkan tanggal yang dipilih di EditText
                        binding.tTanggal.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Tampilkan DatePickerDialog
        datePickerDialog.show();
    }

    private void setUpSpinner() {
        List<String> dataList = new ArrayList<>();
        dataList.add("Data 1");
        dataList.add("Data 2");
        dataList.add("Data 3");
        dataList.add("Data 4");
        dataList.add("Data 5");
        dataList.add("Data 6");
        dataList.add("Data 7");
        dataList.add("Data 8");
        dataList.add("Data 9");
        dataList.add("Data 10");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
    }

    private void setButtonActive(Button button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.white));
        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue_dark));
    }

    private void setButtonInactive(Button button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.gray));
        button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_inactive_tint));
    }

    private void showSlideUpLayout() {
        binding.fab.setVisibility(View.GONE);
        // Menampilkan view latar belakang gelap
        binding.darkBackground.setVisibility(View.VISIBLE);

        // Mengatur tinggi slide up layout menjadi 0 secara awal
        binding.layoutSlideUp.setVisibility(View.VISIBLE);
        binding.layoutSlideUp.setTranslationY(binding.layoutSlideUp.getHeight());

        // Menghitung tinggi slide up layout yang sebenarnya
        binding.layoutSlideUp.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                binding.layoutSlideUp.getViewTreeObserver().removeOnPreDrawListener(this);

                // Animasi slide up
                binding.layoutSlideUp.animate()
                        .translationY(0)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(300)
                        .setListener(null);

                binding.darkBackground.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Mendapatkan koordinat klik
                        float x = event.getRawX();
                        float y = event.getRawY();

                        // Mendapatkan posisi layout slide up
                        int[] location = new int[2];
                        binding.layoutSlideUp.getLocationOnScreen(location);
                        int slideUpTop = location[1];
                        int slideUpBottom = slideUpTop + binding.layoutSlideUp.getHeight();

                        // Menutup slide up hanya jika klik diluar area layout slide up
                        if (y < slideUpTop || y > slideUpBottom) {
                            hideSlideUpLayout();
                        }
                        return false;
                    }
                });

                return false;
            }
        });

        isSlideUpVisible = true;
    }

    private void hideSlideUpLayout() {
        // Menyembunyikan view latar belakang gelap
        binding.darkBackground.setVisibility(View.GONE);

        // Animasi slide down
        binding.layoutSlideUp.animate()
                .translationY(binding.layoutSlideUp.getHeight())
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        binding.layoutSlideUp.setVisibility(View.GONE);
                        binding.fab.setVisibility(View.VISIBLE);
                    }
                });

        isSlideUpVisible = false;
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