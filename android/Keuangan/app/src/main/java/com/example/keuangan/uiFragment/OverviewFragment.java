package com.example.keuangan.uiFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keuangan.ProfileActivity;
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
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}