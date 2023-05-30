package com.example.keuangan.uiFragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.example.keuangan.databinding.FragmentStatisticBinding;
import com.example.keuangan.models.Transaksi;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticFragment extends Fragment {

    private FragmentStatisticBinding binding;
    private SessionManager sessionManager;
    private int userId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatisticBinding.inflate(inflater, container, false);

        sessionManager = new SessionManager(requireActivity());
        userId = sessionManager.getUserId();

        loadData();

        binding.viewTanggalAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.viewTanggalAwal);
            }
        });

        binding.viewTglAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.viewTglAkhir);
            }
        });

        binding.btnFilterStatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData();
            }
        });

        return binding.getRoot();
    }

    private void showDatePickerDialog(final EditText inputTanggal) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String formattedDate =  year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                inputTanggal.setText(formattedDate);
            }
        }, year, month, dayOfMonth);

        // Tampilkan dialog pemilih tanggal
        datePickerDialog.show();
    }

    private void filterData() {
        String tglAwal = binding.viewTanggalAwal.getText().toString();
        String tglAkhir = binding.viewTglAkhir.getText().toString();

        binding.progressBar.setVisibility(View.VISIBLE);

        ApiClient.getFilterTransaksi(userId, tglAwal, tglAkhir,  new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Sembunyikan ProgressBar setelah penundaan
                                binding.progressBar.setVisibility(View.GONE);

                                int totalPemasukan = apiResponse.getTotal_pemasukan();
                                int totalPengeluaran = apiResponse.getTotal_pengeluaran();

                                List<PieEntry> entries = new ArrayList<>();
                                entries.add(new PieEntry((float) apiResponse.getTotal_pemasukan(), "Pemasukan"));
                                entries.add(new PieEntry((float) apiResponse.getTotal_pengeluaran(), "Pengeluaran"));

                                // Buat dataset untuk Pie Chart
                                PieDataSet dataSet = new PieDataSet(entries, "Total");
                                dataSet.setColors(new int[] {Color.parseColor("#00FF00"), Color.parseColor("#FF0000")});
                                dataSet.setValueTextColor(Color.WHITE);
                                dataSet.setValueTextSize(12f);

                                // Buat konfigurasi Pie Chart
                                PieData data = new PieData(dataSet);
                                data.setDrawValues(true);
                                data.setValueFormatter(new DefaultValueFormatter(0)); // Format angka tanpa desimal

                                // Set data ke Pie Chart
                                binding.pieChart.setData(data);
                                binding.pieChart.getDescription().setEnabled(false);
                                binding.pieChart.setEntryLabelColor(Color.WHITE);
                                binding.pieChart.setEntryLabelTextSize(12f);
                                binding.pieChart.animateY(500);
                                binding.pieChart.invalidate();


                                // Update tampilan card dengan data yang diambil dari respons JSON
                                binding.viewPengeluaran.setText(formatIDR(Double.valueOf(totalPemasukan)));
                                binding.viewPengeluaran.setText(formatIDR(Double.valueOf(totalPengeluaran)));
                            }
                        }, 1000);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.progressBar.setVisibility(View.GONE);

                                int totalPemasukan = apiResponse.getTotal_pemasukan();
                                int totalPengeluaran = apiResponse.getTotal_pengeluaran();

                                // Update tampilan card dengan data yang diambil dari respons JSON
                                binding.viewPengeluaran.setText(formatIDR(Double.valueOf(totalPemasukan)));
                                binding.viewPengeluaran.setText(formatIDR(Double.valueOf(totalPengeluaran)));
                            }
                        }, 1000);
                    }
                } else {
                    Toast.makeText(requireContext(), "Terjadi kesalahan saat mengirim permintaan", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadData() {
        ApiClient.loadTransaksi(userId, new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse responseData = response.body();
                    if (responseData != null && responseData.isSuccess()) {

                        // Display total pemasukan and total pengeluaran
                        binding.viewPemasukan.setText(formatIDR(responseData.getTotal_pemasukan()));
                        binding.viewPengeluaran.setText(formatIDR(responseData.getTotal_pengeluaran()));

                        List<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry((float) responseData.getTotal_pemasukan(), "Pemasukan"));
                        entries.add(new PieEntry((float) responseData.getTotal_pengeluaran(), "Pengeluaran"));

                        // Buat dataset untuk Pie Chart
                        PieDataSet dataSet = new PieDataSet(entries, "Total");
                        dataSet.setColors(new int[] {Color.parseColor("#00FF00"), Color.parseColor("#FF0000")});
                        dataSet.setValueTextColor(Color.WHITE);
                        dataSet.setValueTextSize(12f);

                        // Buat konfigurasi Pie Chart
                        PieData data = new PieData(dataSet);
                        data.setDrawValues(true);
                        data.setValueFormatter(new DefaultValueFormatter(0)); // Format angka tanpa desimal

                        // Set data ke Pie Chart
                        binding.pieChart.setData(data);
                        binding.pieChart.getDescription().setEnabled(false);
                        binding.pieChart.setEntryLabelColor(Color.WHITE);
                        binding.pieChart.setEntryLabelTextSize(12f);
                        binding.pieChart.animateY(500);
                        binding.pieChart.invalidate();
                    }
                }
            }

            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {

            }
        });
    }

    private String formatIDR(double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        return numberFormat.format(number);
    }
}