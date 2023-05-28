package com.example.keuangan.uiFragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.keuangan.databinding.FragmentStatisticBinding;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticFragment extends Fragment {

    private FragmentStatisticBinding binding;
    private SessionManager sessionManager;
    private int userId;
    private String namaUsaha;



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

        binding.tglAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.tglAwal);
            }
        });

        binding.tglAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.tglAkhir);
            }
        });

        return binding.getRoot();
    }

    private void loadDataCard() {

        String tglAwal2 = binding.tglAwal.getText().toString();
        String tglAkhir2 = binding.tglAkhir.getText().toString();
        ApiClient.getFilterTransaksi(userId, tglAwal2, tglAkhir2, new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse transaksiCard = response.body();
                    if (transaksiCard != null && transaksiCard.isSuccess()) {
                        // Ambil data dari TransaksiCard dan tampilkan di card di fragment
                        int totalPemasukan = transaksiCard.getTotal_pemasukan();
                        int totalPengeluaran = transaksiCard.getTotal_pengeluaran();

                        // Update tampilan card dengan data yang diambil dari respons JSON
                        binding.totalPemasukan.setText(formatIDR(Double.valueOf(totalPemasukan)));
                        binding.totalPengeluaran.setText(formatIDR(Double.valueOf(totalPengeluaran)));
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

    private void showDatePickerDialog(final EditText inputTanggal) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String formattedDate =  year + "/" + (month + 1) + "/" + dayOfMonth;
                inputTanggal.setText(formattedDate);
            }
        }, year, month, dayOfMonth);

        // Tampilkan dialog pemilih tanggal
        datePickerDialog.show();
    }
    private String formatIDR(double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        return numberFormat.format(number);
    }

}