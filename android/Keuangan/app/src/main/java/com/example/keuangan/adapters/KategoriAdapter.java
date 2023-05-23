package com.example.keuangan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keuangan.TambahKategoriActivity;
import com.example.keuangan.databinding.ItemKategoriBinding;
import com.example.keuangan.models.Kategori;
import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.services.ApiClient;
import com.example.keuangan.session.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder> {

    private List<Kategori> kategoriList;
    private Context ctx;
    public KategoriAdapter(Context ctx, List<Kategori> kategoriList) {
        this.ctx = ctx;
        this.kategoriList = kategoriList;
    }

    public void setKategoriList(List<Kategori> kategoriList) {
        this.kategoriList = kategoriList;
    }

    @NonNull
    @Override
    public KategoriAdapter.KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKategoriBinding itemKategoriBinding = ItemKategoriBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new KategoriViewHolder(itemKategoriBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Kategori kategori = kategoriList.get(position);
        holder.binding.namaKategori.setText(kategori.getKategori());
        holder.binding.editKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kategoriId = kategoriList.get(position).getId_kategori();
                String namaKategori = kategoriList.get(position).getKategori();
                int jenisId = kategoriList.get(position).getId_jenis();

                Intent kirim = new Intent(ctx, TambahKategoriActivity.class);
                kirim.putExtra("kategoriId", kategoriId);
                kirim.putExtra("namaKategori", namaKategori);
                kirim.putExtra("jenisId", jenisId);

                ctx.startActivity(kirim);

            }
        });
        holder.binding.hapusKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kategori kategori = kategoriList.get(holder.getAdapterPosition());
                ApiClient.deleteItemKategori(kategori.getId_kategori(), new Callback<KategoriResponse>() {
                    @Override
                    public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            KategoriResponse kategoriResponse = response.body();
                            if (kategoriResponse.isSuccess()) {
                                // Hapus item kategori dari list jika penghapusan berhasil
                                kategoriList.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();

                            } else {
                                // Tampilkan pesan error jika penghapusan gagal
                                Toast.makeText(ctx, kategoriResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Tampilkan pesan error jika response tidak sukses
                            Toast.makeText(ctx, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<KategoriResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    public class KategoriViewHolder extends RecyclerView.ViewHolder {

        private ItemKategoriBinding binding;
        public KategoriViewHolder(@NonNull ItemKategoriBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
