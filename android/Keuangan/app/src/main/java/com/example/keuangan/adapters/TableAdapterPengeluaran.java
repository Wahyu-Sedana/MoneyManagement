package com.example.keuangan.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keuangan.databinding.ItemTableRowBinding;
import com.example.keuangan.models.Transaksi;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TableAdapterPengeluaran extends RecyclerView.Adapter<TableAdapterPengeluaran.ViewHolder> {

    private List<Transaksi> dataPemasukanList;

    public TableAdapterPengeluaran(List<Transaksi> dataPemasukanList) {
        this.dataPemasukanList = dataPemasukanList;
    }

    public void serDataPengeluaranList(List<Transaksi> dataPemasukanList) {
        this.dataPemasukanList = dataPemasukanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTableRowBinding binding = ItemTableRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaksi dataPemasukan = dataPemasukanList.get(position);
        holder.binding.tvJumlah.setText(formatIDR(Double.parseDouble(dataPemasukan.getJumlah())));
        holder.binding.tvCatatan.setText(dataPemasukan.getCatatan());
        holder.binding.tvTanggal.setText(dataPemasukan.getTanggal());
    }

    @Override
    public int getItemCount() {
        return dataPemasukanList.size();
    }

    private String formatIDR(double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        return numberFormat.format(number);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemTableRowBinding binding;

        public ViewHolder(ItemTableRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}