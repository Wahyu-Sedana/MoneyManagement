package com.example.keuangan.uiFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keuangan.R;
import com.example.keuangan.databinding.FragmentOverviewBinding;


public class OverviewFragment extends Fragment {

    private FragmentOverviewBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        // Lakukan manipulasi view di sini menggunakan binding
        return binding.getRoot();
    }
}