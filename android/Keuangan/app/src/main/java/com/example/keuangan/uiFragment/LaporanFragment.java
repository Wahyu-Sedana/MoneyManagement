package com.example.keuangan.uiFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.keuangan.adapters.TableAdapterPemasukan;
import com.example.keuangan.adapters.TableAdapterPengeluaran;

import com.example.keuangan.databinding.FormDialogBinding;
import com.example.keuangan.databinding.FragmentLaporanBinding;
import com.example.keuangan.helper.FileUtils;
import com.example.keuangan.models.Transaksi;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanFragment extends Fragment {

    private FragmentLaporanBinding binding;
    private FormDialogBinding formDialogBinding;
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
        binding = FragmentLaporanBinding.inflate(inflater, container, false);

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

        binding.fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFilter();
            }
        });

        binding.fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Success")
                        .setMessage("Apakah anda ingin mengunduh data ini?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ApiClient.downloadExcel(userId, new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            ResponseBody responseBody = response.body();
                                            if (responseBody != null) {
                                                try {
                                                    // Simpan file Excel ke penyimpanan eksternal
                                                    String fileName = "laporan_labarugi.xlsx"; // Ganti dengan nama file yang diinginkan
                                                    File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                                                    FileUtils.saveResponseBodyToDisk(responseBody, outputFile);


                                                    Toast.makeText(requireActivity(), "File Excel berhasil diunduh", Toast.LENGTH_SHORT).show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                    // Handle error saat menyimpan file Excel
                                                    Toast.makeText(requireActivity(), "Gagal Menyimpan File", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                // ResponseBody kosong, tangani error jika diperlukan
                                                Toast.makeText(requireActivity(), "Coba lagi!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            // Handle error respons HTTP jika diperlukan
                                            Toast.makeText(requireActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        }).show();
            }
        });


        loadData();

        return binding.getRoot();
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

    private void loadTransaksiFilter() {

        String tglAwal = formDialogBinding.tglAwal.getText().toString();
        String tglAkhir = formDialogBinding.tglAkhir.getText().toString();

        ApiClient.getFilterTransaksi(userId, tglAwal, tglAkhir,  new Callback<TransaksiResponse>() {
            @Override
            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                if (response.isSuccessful()) {
                    TransaksiResponse statisticResponse = response.body();
                    if (statisticResponse != null && statisticResponse.isSuccess()) {
                        List<Transaksi> transaksiList = statisticResponse.getData();
                        List<Transaksi> filterTransaksiPemasukan = new ArrayList<>();
                        List<Transaksi> filterTransaksiPengeluaran = new ArrayList<>();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Sembunyikan ProgressBar setelah penundaan
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
                        }, 1000);
                    }
                } else {
                    Toast.makeText(requireContext(), "Terjadi kesalahan saat mengirim permintaan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}