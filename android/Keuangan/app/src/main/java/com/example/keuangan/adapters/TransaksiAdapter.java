package com.example.keuangan.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keuangan.R;
import com.example.keuangan.databinding.ItemPemasukanBinding;
import com.example.keuangan.models.Transaksi;

import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder> {

    private List<Transaksi> transaksiList;

    public TransaksiAdapter(List<Transaksi> transaksiList) {
        this.transaksiList = transaksiList;
    }

    @NonNull
    @Override
    public TransaksiAdapter.TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPemasukanBinding itemPemasukanBinding = ItemPemasukanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransaksiViewHolder(itemPemasukanBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiAdapter.TransaksiViewHolder holder, int position) {
        Transaksi transaksi = transaksiList.get(position);

        holder.binding.kategori.setText(transaksi.getKategori());
        holder.binding.total.setText(transaksi.getJumlah());
        holder.binding.catatan.setText(transaksi.getCatatan());
        holder.binding.tglHistori.setText(transaksi.getTanggal());

        if(transaksi.getJenis() == "Pemasukan"){
            holder.binding.iconHistory.setImageResource(R.drawable.pemasukan);
        }else if(transaksi.getJenis() == "Pengeluaran"){
            holder.binding.iconHistory.setImageResource(R.drawable.pengeluaran);
        }
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public class TransaksiViewHolder extends RecyclerView.ViewHolder {

        private ItemPemasukanBinding binding;
        public TransaksiViewHolder(@NonNull ItemPemasukanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
