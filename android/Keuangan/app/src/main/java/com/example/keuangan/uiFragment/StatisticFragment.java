package com.example.keuangan.uiFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.keuangan.adapters.TableAdapterPemasukan;
import com.example.keuangan.adapters.TableAdapterPengeluaran;
import com.example.keuangan.databinding.FragmentStatisticBinding;

import com.example.keuangan.models.Transaksi;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticFragment extends Fragment {

    private FragmentStatisticBinding binding;
    private SessionManager sessionManager;

    private TableAdapterPemasukan tableAdapterPemasukan;
    private TableAdapterPengeluaran tableAdapterPengeluaran;

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

        tableAdapterPemasukan = new TableAdapterPemasukan(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rvPemasukan.setLayoutManager(layoutManager);
        binding.rvPemasukan.setAdapter(tableAdapterPemasukan);

        tableAdapterPengeluaran = new TableAdapterPengeluaran(new ArrayList<>());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext());
        binding.rvPengeluaran.setLayoutManager(layoutManager2);
        binding.rvPengeluaran.setAdapter(tableAdapterPengeluaran);

        loadData();

        return binding.getRoot();
    }

    private void loadData() {
        ApiClient.getStatistik(userId, new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse statisticResponse = response.body();
                    if (statisticResponse != null && statisticResponse.isSuccess()) {
                        List<Transaksi> transaksiList = statisticResponse.getData();
                        List<Transaksi> filterTransaksiPemasukan = new ArrayList<>();
                        List<Transaksi> filterTransaksiPengeluaran = new ArrayList<>();

                        int totalPemasukan = statisticResponse.getTotal_pemasukan();
                        int totalPengeluaran = statisticResponse.getTotal_pengeluaran();
                        int saldo = statisticResponse.getSaldo();

                        binding.textTotalPemasukan.setText(formatIDR(Double.parseDouble(String.valueOf(totalPemasukan))));
                        binding.textTotalPengeluaran.setText(formatIDR(Double.parseDouble(String.valueOf(totalPengeluaran))));
                        binding.textSaldo.setText(formatIDR(Double.parseDouble(String.valueOf(saldo))));

                        for (Transaksi dataItem : transaksiList) {
                            if (dataItem.getId_jenis() == 1) {
                                filterTransaksiPemasukan.add(dataItem);
                            } else if (dataItem.getId_jenis() == 2) {
                                filterTransaksiPengeluaran.add(dataItem);
                            }
                        }

                        tableAdapterPemasukan.setDataPemasukanList(filterTransaksiPemasukan);
                        tableAdapterPengeluaran.serDataPengeluaranList(filterTransaksiPengeluaran);
                        tableAdapterPemasukan.notifyDataSetChanged();
                        tableAdapterPengeluaran.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
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