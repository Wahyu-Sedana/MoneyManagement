package com.example.keuangan.uiFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keuangan.ProfileActivity;
import com.example.keuangan.R;
import com.example.keuangan.adapters.TransaksiAdapter;
import com.example.keuangan.databinding.FragmentOverviewBinding;
import com.example.keuangan.models.Transaksi;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;


public class OverviewFragment extends Fragment {

    private FragmentOverviewBinding binding;

    private TransaksiAdapter transaksiAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);

        binding.listItemHistory.setAdapter(transaksiAdapter);
        binding.listItemHistory.setLayoutManager(new LinearLayoutManager(requireContext()));

        Date currentDate = new Date();
//        performDateFilter(currentDate, currentDate);
        // Lakukan manipulasi view di sini menggunakan binding
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

//    private void performDateFilter(Date startDate, Date endDate) {
//        startDate = new Date();
//        endDate = new Date();
//
//        // Konversi tanggal menjadi format "dd-MM-yyyy" yang sesuai dengan respons JSON
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//        String startDateString = dateFormat.format(startDate);
//        String endDateString = dateFormat.format(endDate);
//
//        // Panggil API untuk mengambil data sesuai filter
//        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
//        Call<ApiResponse> call = apiService.getData(userId, startDateString, endDateString);
//        call.enqueue(new Callback<ApiResponse>() {
//            @Override
//            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                if (response.isSuccessful()) {
//                    ApiResponse apiResponse = response.body();
//                    if (apiResponse != null && apiResponse.isSuccess()) {
//                        List<Transaksi> transaksiList = apiResponse.getData();
//
//                        // Update daftar transaksi pada adapter
//                        transaksiAdapter.setTransaksiList(transaksiList);
//                        transaksiAdapter.notifyDataSetChanged();
//                    } else {
//                        // Tangani respons API tidak berhasil
//                        // ...
//                    }
//                } else {
//                    // Tangani kesalahan dalam respons API
//                    // ...
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TransaksiResponse> call, Throwable t) {
//                // Tangani kegagalan permintaan
//                // ...
//            }
//        });
}