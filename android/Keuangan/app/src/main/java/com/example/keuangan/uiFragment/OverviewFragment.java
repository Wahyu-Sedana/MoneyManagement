package com.example.keuangan.uiFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.keuangan.HomeActivity;
import com.example.keuangan.ProfileActivity;
import com.example.keuangan.R;
import com.example.keuangan.RegisterActivity;
import com.example.keuangan.TransaksiActivity;
import com.example.keuangan.adapters.TransaksiAdapter;
import com.example.keuangan.databinding.FormDialogBinding;
import com.example.keuangan.databinding.FragmentOverviewBinding;
import com.example.keuangan.models.ProfileResponse;
import com.example.keuangan.models.RegisterResponse;
import com.example.keuangan.models.Transaksi;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.services.ApiServices;
import com.example.keuangan.session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private DatePickerDialog datePickerDialog;


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

        transaksiAdapter = new TransaksiAdapter(new ArrayList<>());

        binding.listItemHistory.setAdapter(transaksiAdapter);
        binding.listItemHistory.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadTransaksi();
        // Lakukan manipulasi view di sini menggunakan binding
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), TransaksiActivity.class));
            }
        });

        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFilter();
            }
        });

        loadDataProfile();

        return binding.getRoot();
    }

    private void loadDataProfile() {
        ApiClient.getUserData(userId, new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    if (jsonElement != null) {
                        String jsonResponse = jsonElement.toString();

                        Gson gson = new Gson();
                        ProfileResponse userResponse = gson.fromJson(jsonResponse, new TypeToken<ProfileResponse>(){}.getType());

                        if (userResponse.isSuccess() && userResponse.getData() != null && !userResponse.getData().isEmpty()) {
                            ProfileResponse.User user = userResponse.getData().get(0);
                            String namaUsaha = user.getNama_usaha();

                            // Tampilkan data pada TextView
                            binding.namaUserLogin.setText(namaUsaha);
                        } else {
                            // Tampilkan pesan error jika tidak berhasil
                            String message = userResponse != null ? userResponse.getMessage() : "Error";
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                // Tangani kegagalan permintaan
                Toast.makeText(requireContext(), "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        formDialogBinding = FormDialogBinding.inflate(getLayoutInflater());

        formDialogBinding.tglAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(formDialogBinding.tglAwal);
            }
        });

        formDialogBinding.tglAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(formDialogBinding.tglAkhir);
            }
        });

        builder.setView(formDialogBinding.getRoot())
                .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        loadTransaksiFilter();
                    }
                })
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadTransaksiFilter() {

        String tglAwal = formDialogBinding.tglAwal.getText().toString();
        String tglAkhir = formDialogBinding.tglAkhir.getText().toString();

        binding.progressBar.setVisibility(View.VISIBLE);
        ApiClient.getFilterTransaksi(userId, tglAwal, tglAkhir,  new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        List<Transaksi> transaksiList = apiResponse.getData();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Sembunyikan ProgressBar setelah penundaan
                                binding.progressBar.setVisibility(View.GONE);
                                binding.textBelumAdaData.setVisibility(View.GONE);
                                binding.listItemHistory.setVisibility(View.VISIBLE);
                                // Update daftar transaksi pada adapter
                                transaksiAdapter.setTransaksiList(transaksiList);
                                transaksiAdapter.notifyDataSetChanged();

                                int saldo = apiResponse.getSaldo();
                                int totalPemasukan = apiResponse.getTotal_pemasukan();
                                int totalPengeluaran = apiResponse.getTotal_pengeluaran();

                                if (saldo < 0) {
                                    saldo = 0;
                                }

                                // Update tampilan card dengan data yang diambil dari respons JSON
                                binding.totalSaldo.setText(formatIDR(Double.valueOf(saldo)));
                                binding.pemasukan.setText(formatIDR(Double.valueOf(totalPemasukan)));
                                binding.pengeluaran.setText(formatIDR(Double.valueOf(totalPengeluaran)));
                            }
                        }, 1000);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.listItemHistory.setVisibility(View.GONE);
                                binding.textBelumAdaData.setText("Belum ada data dari tanggal " + tglAwal + " hingga tanggal " + tglAkhir);
                                binding.textBelumAdaData.setVisibility(View.VISIBLE);

                                int saldo = apiResponse.getSaldo();
                                int totalPemasukan = apiResponse.getTotal_pemasukan();
                                int totalPengeluaran = apiResponse.getTotal_pengeluaran();

                                if (saldo < 0) {
                                    saldo = 0;
                                }

                                // Update tampilan card dengan data yang diambil dari respons JSON
                                binding.totalSaldo.setText(formatIDR(Double.valueOf(saldo)));
                                binding.pemasukan.setText(formatIDR(Double.valueOf(totalPemasukan)));
                                binding.pengeluaran.setText(formatIDR(Double.valueOf(totalPengeluaran)));
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

    private void showDatePickerDialog(final TextInputEditText inputTanggal) {
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

    private String formatIDR(double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        return numberFormat.format(number);
    }


    private void loadTransaksi() {

        binding.progressBar.setVisibility(View.VISIBLE);
        ApiClient.loadTransaksi(userId, new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {

                if (response.isSuccessful()) {
                    TransaksiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        List<Transaksi> transaksiList = apiResponse.getData();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Sembunyikan ProgressBar setelah penundaan
                                binding.progressBar.setVisibility(View.GONE);
                                // Update daftar transaksi pada adapter
                                transaksiAdapter.setTransaksiList(transaksiList);
                                transaksiAdapter.notifyDataSetChanged();

                                int saldo = apiResponse.getSaldo();
                                int totalPemasukan = apiResponse.getTotal_pemasukan();
                                int totalPengeluaran = apiResponse.getTotal_pengeluaran();

                                if (saldo < 0) {
                                    saldo = 0;
                                }

                                // Update tampilan card dengan data yang diambil dari respons JSON
                                binding.totalSaldo.setText(formatIDR(Double.valueOf(saldo)));
                                binding.pemasukan.setText(formatIDR(Double.valueOf(totalPemasukan)));
                                binding.pengeluaran.setText(formatIDR(Double.valueOf(totalPengeluaran)));
                            }
                        }, 1000);
                    }else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.textBelumAdaData.setVisibility(View.VISIBLE);
                        binding.textBelumAdaData.setText("Belum ada data");
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
}