package com.example.keuangan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.example.keuangan.databinding.ActivityHomeBinding;
import com.example.keuangan.uiFragment.OverviewFragment;
import com.example.keuangan.uiFragment.StatisticFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityHomeBinding binding;
    private Animation slideUpAnimation;
    private boolean isSlideUpVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.btnPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActive(binding.btnPemasukan);
                setButtonInactive(binding.btnPengeluaran);
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

                binding.darkBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSlideUpLayout();
                    }
                });

                return false;
            }
        });

        isSlideUpVisible = true;
    }

    private void hideSlideUpLayout() {
        binding.fab.setVisibility(View.VISIBLE);

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
                    }
                });

        isSlideUpVisible = false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new OverviewFragment()).commit();
                return true;
            case R.id.navigation_statistic:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new StatisticFragment()).commit();
                return true;
        }
        return false;
    }
}