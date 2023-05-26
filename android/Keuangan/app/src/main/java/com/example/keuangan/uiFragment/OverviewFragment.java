package com.example.keuangan.uiFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.keuangan.ProfileActivity;
import com.example.keuangan.R;
import com.example.keuangan.RegisterActivity;
import com.example.keuangan.adapters.TransaksiAdapter;
import com.example.keuangan.databinding.FormDialogBinding;
import com.example.keuangan.databinding.FragmentOverviewBinding;
import com.example.keuangan.models.RegisterResponse;
import com.example.keuangan.models.Transaksi;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.services.ApiServices;
import com.example.keuangan.session.SessionManager;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OverviewFragment extends Fragment {

    private FragmentOverviewBinding binding;
    private FormDialogBinding formDialogBinding;

    private TransaksiAdapter transaksiAdapter;

    private SessionManager sessionManager;
    private int userId;
    private String namaUsaha;
    private String email;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);

        sessionManager = new SessionManager(requireActivity());
        userId = sessionManager.getUserId();
        namaUsaha = sessionManager.getNamaUsaha();

        binding.namaUserLogin.setText(namaUsaha);

        transaksiAdapter = new TransaksiAdapter(new ArrayList<>());

        binding.listItemHistory.setAdapter(transaksiAdapter);
        binding.listItemHistory.setLayoutManager(new LinearLayoutManager(requireContext()));

//        Date currentDate = new Date();
//        performDateFilter(currentDate, currentDate);

        loadTransaksi();
        // Lakukan manipulasi view di sini menggunakan binding
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFilter();
            }
        });

        loadDataCard();

        return binding.getRoot();
    }

    private void addFilter() {
        formDialogBinding = FormDialogBinding.inflate(getLayoutInflater());



    }

    private void loadDataCard() {
        ApiClient.loadDataCard(userId, new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse transaksiCard = response.body();
                    if (transaksiCard != null && transaksiCard.isSuccess()) {
                        // Ambil data dari TransaksiCard dan tampilkan di card di fragment
                        int saldo = transaksiCard.getSaldo();
                        int totalPemasukan = transaksiCard.getTotal_pemasukan();
                        int totalPengeluaran = transaksiCard.getTotal_pengeluaran();

                        // Update tampilan card dengan data yang diambil dari respons JSON
                        binding.totalSaldo.setText(formatIDR(Double.valueOf(saldo)));
                        binding.pemasukan.setText(formatIDR(Double.valueOf(totalPemasukan)));
                        binding.pengeluaran.setText(formatIDR(Double.valueOf(totalPengeluaran)));
                    } else {
                        // Tampilkan pesan error jika tidak berhasil
                        String message = transaksiCard != null ? transaksiCard.getMessage() : "Error";
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Tampilkan pesan error jika respons tidak berhasil
                    Toast.makeText(requireContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
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


    private void loadTransaksi() {
        ApiClient.loadTransaksi(userId, new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        List<Transaksi> transaksiList = apiResponse.getData();
                        // Update daftar transaksi pada adapter
                        transaksiAdapter.setTransaksiList(transaksiList);
                        transaksiAdapter.notifyDataSetChanged();
                    }
                } else {
                    // Tangani kesalahan dalam respons API
                    // ...
                }
            }

            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {

            }
        });
    }
}