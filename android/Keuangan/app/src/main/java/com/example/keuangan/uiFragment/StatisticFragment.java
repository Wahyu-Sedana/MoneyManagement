package com.example.keuangan.uiFragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keuangan.databinding.FragmentStatisticBinding;
import com.example.keuangan.models.StatistikResponse;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

import java.text.NumberFormat;
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

        return binding.getRoot();
    }

    private void loadData() {
        ApiClient.getStatistik(userId, new Callback<StatistikResponse>() {
            @Override
            public void onResponse(Call<StatistikResponse> call, Response<StatistikResponse> response) {
                if (response.isSuccessful()) {
                    StatistikResponse statisticResponse = response.body();
                    if (statisticResponse != null && statisticResponse.isSuccess()) {
                        List<StatistikResponse.DataPemasukan> dataPemasukanList = statisticResponse.getDataPemasukan();
                        List<StatistikResponse.DataPengeluaran> dataPengeluaranList = statisticResponse.getDataPengeluaran();
                        int totalPemasukan = statisticResponse.getTotal_pemasukan();
                        int totalPengeluaran = statisticResponse.getTotal_pengeluaran();
                        int saldo = statisticResponse.getSaldo();

                        // Tampilkan data pemasukan dalam tabel
                        displayDataPemasukan(dataPemasukanList);

                        // Tampilkan data pengeluaran dalam tabel
                        displayDataPengeluaran(dataPengeluaranList);

                        // Tampilkan total pemasukan, pengeluaran, dan saldo
                        binding.textTotalPemasukan.setText(formatIDR(Double.parseDouble(String.valueOf(totalPemasukan))));
                        binding.textTotalPengeluaran.setText(formatIDR(Double.parseDouble(String.valueOf(totalPengeluaran))));
                        binding.textSaldo.setText(formatIDR(Double.parseDouble(String.valueOf(saldo))));
                    }
                }
            }

            @Override
            public void onFailure(Call<StatistikResponse> call, Throwable t) {

            }
        });
    }

    private String formatIDR(double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        return numberFormat.format(number);
    }

    private void displayDataPemasukan(List<StatistikResponse.DataPemasukan> dataPemasukanList) {
        if (dataPemasukanList != null) {
            for (StatistikResponse.DataPemasukan dataPemasukan : dataPemasukanList) {
                TableRow tableRow = new TableRow(requireContext());

                TextView tvJenis = new TextView(requireContext());
                tvJenis.setText("Pemasukan");
                tvJenis.setPadding(8, 8, 8, 8);
                tableRow.addView(tvJenis);

                TextView tvJumlah = new TextView(requireContext());
                tvJumlah.setText(dataPemasukan.getJumlah());
                tvJumlah.setPadding(8, 8, 8, 8);
                tableRow.addView(tvJumlah);

                TextView tvCatatan = new TextView(requireContext());
                tvCatatan.setText(dataPemasukan.getCatatan());
                tvCatatan.setPadding(8, 8, 8, 8);
                tableRow.addView(tvCatatan);

                TextView tvTanggal = new TextView(requireContext());
                tvTanggal.setText(dataPemasukan.getTanggal());
                tvTanggal.setPadding(8, 8, 8, 8);
                tableRow.addView(tvTanggal);

                binding.tableLayout.addView(tableRow);
            }
        }
    }

    private void displayDataPengeluaran(List<StatistikResponse.DataPengeluaran> dataPengeluaranList) {
        if (dataPengeluaranList != null) {
            for (StatistikResponse.DataPengeluaran dataPengeluaran : dataPengeluaranList) {
                TableRow tableRow = new TableRow(requireContext());

                TextView tvJenis = new TextView(requireContext());
                tvJenis.setText("Pengeluaran");
                tvJenis.setPadding(8, 8, 8, 8);
                tableRow.addView(tvJenis);

                TextView tvJumlah = new TextView(requireContext());
                tvJumlah.setText(dataPengeluaran.getJumlah());
                tvJumlah.setPadding(8, 8, 8, 8);
                tableRow.addView(tvJumlah);

                TextView tvCatatan = new TextView(requireContext());
                tvCatatan.setText(dataPengeluaran.getCatatan());
                tvCatatan.setPadding(8, 8, 8, 8);
                tableRow.addView(tvCatatan);

                TextView tvTanggal = new TextView(requireContext());
                tvTanggal.setText(dataPengeluaran.getTanggal());
                tvTanggal.setPadding(8, 8, 8, 8);
                tableRow.addView(tvTanggal);

                binding.tableLayout.addView(tableRow);
            }
        }
    }

}