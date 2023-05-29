package com.example.keuangan.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keuangan.databinding.ItemTableRowBinding;
import com.example.keuangan.models.DataPemasukan;
import com.example.keuangan.models.Transaksi;

import java.util.List;

public class TableAdapterPemasukan extends RecyclerView.Adapter<TableAdapterPemasukan.ViewHolder> {

    private List<DataPemasukan> dataPemasukanList;

    public TableAdapterPemasukan(List<DataPemasukan> dataPemasukanList) {
        this.dataPemasukanList = dataPemasukanList;
    }

    public void setDataPemasukanList(List<DataPemasukan> dataPemasukanList) {
        this.dataPemasukanList = dataPemasukanList;
    }

    @NonNull
    @Override
    public TableAdapterPemasukan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTableRowBinding itemTableRowBinding = ItemTableRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemTableRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapterPemasukan.ViewHolder holder, int position) {
        DataPemasukan dataPemasukan = dataPemasukanList.get(position);
        holder.binding.tvJumlah.setText(dataPemasukan.getJumlah());
        holder.binding.tvCatatan.setText(dataPemasukan.getCatatan());
        holder.binding.tvTanggal.setText(dataPemasukan.getTanggal());
    }

    @Override
    public int getItemCount() {
        if(dataPemasukanList == null) return  0;
        return dataPemasukanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemTableRowBinding binding;
        public ViewHolder(@NonNull ItemTableRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
