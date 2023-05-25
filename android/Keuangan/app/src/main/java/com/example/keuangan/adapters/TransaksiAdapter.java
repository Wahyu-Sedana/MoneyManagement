package com.example.keuangan.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keuangan.HomeActivity;
import com.example.keuangan.R;
import com.example.keuangan.UpdateTransaksiActivity;
import com.example.keuangan.databinding.ItemPemasukanBinding;
import com.example.keuangan.models.KategoriResponse;
import com.example.keuangan.models.Transaksi;
import com.example.keuangan.models.TransaksiResponse;
import com.example.keuangan.services.ApiClient;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder> {

    private List<Transaksi> transaksiList;


    public TransaksiAdapter(List<Transaksi> transaksiList) {
        this.transaksiList = transaksiList;
    }

    public void setTransaksiList(List<Transaksi> transaksiList) {
        this.transaksiList = transaksiList;
    }

    @NonNull
    @Override
    public TransaksiAdapter.TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPemasukanBinding itemPemasukanBinding = ItemPemasukanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransaksiViewHolder(itemPemasukanBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiAdapter.TransaksiViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Transaksi transaksi = transaksiList.get(position);

        holder.binding.kategori.setText(transaksi.getKategori());
        holder.binding.total.setText(formatIDR(Double.parseDouble(transaksi.getJumlah())));
        holder.binding.catatan.setText(transaksi.getCatatan());
        holder.binding.tglHistori.setText(transaksi.getTanggal());


        if(transaksi.getId_jenis() == 1){
            holder.binding.iconHistory.setImageResource(R.drawable.pemasukan);
            holder.binding.total.setTextColor(holder.itemView.getResources().getColor(R.color.green));
        }else if(transaksi.getId_jenis() == 2){
            holder.binding.iconHistory.setImageResource(R.drawable.pengeluaran);
            holder.binding.total.setTextColor(holder.itemView.getResources().getColor(R.color.red));
        }

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                // Tambahkan item menu Edit
                MenuItem editItem = menu.add(Menu.NONE, 1, 1, "Edit");
                editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Aksi ketika item Edit dipilih
                        int total = Integer.parseInt(transaksiList.get(position).getJumlah());
                        String catatan = transaksiList.get(position).getCatatan();
                        String tgl = transaksiList.get(position).getTanggal();
                        String kategori = transaksiList.get(position).getKategori();
                        String idTransaksiString = transaksiList.get(position).getId_transaksi();

                        Intent intent = new Intent(holder.itemView.getContext(), UpdateTransaksiActivity.class);
                        intent.putExtra("total", total);
                        intent.putExtra("catatan", catatan);
                        intent.putExtra("tgl", tgl);
                        intent.putExtra("kategori", kategori);
                        intent.putExtra("id_transaksi", idTransaksiString);
                        holder.itemView.getContext().startActivity(intent);

                        return true;
                    }
                });

                // Tambahkan item menu Delete
                MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");
                deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ApiClient.deleteTransaksi(transaksi.getId_transaksi(), new Callback<TransaksiResponse>() {
                            @Override
                            public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                                new AlertDialog.Builder(holder.itemView.getContext())
                                        .setTitle("Success")
                                        .setMessage("Apakah anda ingin menghapus data ini?")
                                        .setCancelable(true)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    TransaksiResponse transaksiResponse = response.body();
                                                    if (transaksiResponse.isSuccess()) {
                                                        // Hapus item kategori dari list jika penghapusan berhasil
                                                        transaksiList.remove(holder.getAdapterPosition());
                                                        notifyDataSetChanged();

                                                    } else {
                                                        // Tampilkan pesan error jika penghapusan gagal
                                                        Toast.makeText(holder.itemView.getContext(), transaksiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(holder.itemView.getContext(), HomeActivity.class);
                                                        holder.itemView.getContext().startActivity(intent);
                                                    }
                                                } else {
                                                    // Tampilkan pesan error jika response tidak sukses
                                                    Toast.makeText(holder.itemView.getContext(), response.message(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).show();
                            }

                            @Override
                            public void onFailure(Call<TransaksiResponse> call, Throwable t) {

                            }
                        });
                        return true;
                    }
                });
            }
        });
    }

    private String formatIDR(double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        return numberFormat.format(number);
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
