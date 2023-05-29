package com.example.keuangan.uiFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keuangan.adapters.TableAdapterPemasukan;
import com.example.keuangan.databinding.FragmentStatisticBinding;
import com.example.keuangan.models.DataPemasukan;
import com.example.keuangan.models.DataPengeluaran;
import com.example.keuangan.models.StatistikResponse;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

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

    private TableAdapterPemasukan tableAdapterPemasukan;

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

        return binding.getRoot();
    }

    private void loadData() {

        tableAdapterPemasukan = new TableAdapterPemasukan(new ArrayList<>());
        binding.rvPemasukan.setAdapter(tableAdapterPemasukan);
        binding.rvPemasukan.setLayoutManager(new LinearLayoutManager(requireContext()));
        ApiClient.getStatistik(userId, new Callback<StatistikResponse>() {
            @Override
            public void onResponse(Call<StatistikResponse> call, Response<StatistikResponse> response) {
                if (response.isSuccessful()) {
                    StatistikResponse statisticResponse = response.body();
                    if (statisticResponse != null && statisticResponse.isSuccess()) {
                        List<DataPemasukan> dataPemasukanList = statisticResponse.getDataPemasukan();
                        List<DataPengeluaran> dataPengeluaranList = statisticResponse.getDataPengeluaran();
                        int totalPemasukan = statisticResponse.getTotal_pemasukan();
                        int totalPengeluaran = statisticResponse.getTotal_pengeluaran();
                        int saldo = statisticResponse.getSaldo();

                        // Tampilkan data pemasukan dalam tabel
                        tableAdapterPemasukan.setDataPemasukanList(dataPemasukanList);
                        tableAdapterPemasukan.notifyDataSetChanged();


                        // Tampilkan total pemasukan, pengeluaran, dan saldo
                        binding.textTotalPemasukan.setText(formatIDR(Double.parseDouble(String.valueOf(totalPemasukan))));
                        binding.textTotalPengeluaran.setText(formatIDR(Double.parseDouble(String.valueOf(totalPengeluaran))));
                        binding.textSaldo.setText(formatIDR(Double.parseDouble(String.valueOf(saldo))));
                    }
                }
            }

            @Override
            public void onFailure(Call<StatistikResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatIDR(double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        return numberFormat.format(number);
    }
}