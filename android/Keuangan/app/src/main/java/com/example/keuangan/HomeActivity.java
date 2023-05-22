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
    private Animation slideUpAnimation;
    private DatePickerDialog datePickerDialog;
    private SessionManager sessionManager;
    private boolean isSlideUpVisible = false;

    private List<Kategori> kategoriList;

    private int userId;
    private String email;
    private String namaUsaha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        sessionManager = new SessionManager(this);

        // Ambil data pengguna dari SharedPrefsManager
        userId = sessionManager.getUserId();
        email = sessionManager.getEmail();
        namaUsaha = sessionManager.getNamaUsaha();


        replaceFragment(new OverviewFragment());

        binding.bottomNavigationView.setBackground(null);

        binding.tTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        binding.semuaKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SemuaKategoriActivity.class));
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

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    private void addData() {

        int selectedPosition = binding.spinner.getSelectedItemPosition();
        Kategori selectedKategori =  kategoriList.get(selectedPosition);
        int idKategori = selectedKategori.getId_kategori();
        int jumlah = Integer.parseInt(binding.addJumlah.getText().toString());
        String catatan = binding.addCatatan.getText().toString();
        Date tanggal = new Date();

        // Format tanggal menjadi String menggunakan SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String tanggalString = dateFormat.format(tanggal);

        ApiClient.addTransaksi(idKategori, jumlah, catatan, tanggalString, new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse addDataResponse = response.body();
                    if(addDataResponse.isSuccess() && addDataResponse != null){
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("Success")
                                .setMessage("Berhasil menambahkan data")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), "Terimakasih!", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                    }
                } else {
                    // Tangani kesalahan respons
                }
            }

            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {

            }
        });
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
        ApiClient.loadDataKategori(userId, new Callback<KategoriResponse>() {

            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                if (response.isSuccessful()) {
                    KategoriResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        kategoriList = apiResponse.getData();

                        // Membuat adapter untuk Spinner dengan data kategori
                        ArrayAdapter<Kategori> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, kategoriList);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // Mengatur adapter pada Spinner
                        binding.spinner.setAdapter(spinnerAdapter);
                    } else {
                        // Gagal memuat data kategori
                        Toast.makeText(getApplicationContext(), "Gagal memuat data kategori", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Tangani kesalahan dalam respons API
                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat mengirim permintaan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {

            }
        });
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