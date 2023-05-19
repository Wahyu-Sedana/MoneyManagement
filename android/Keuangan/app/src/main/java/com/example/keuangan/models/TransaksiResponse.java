package com.example.keuangan.models;

import java.util.List;

public class TransaksiResponse {
    private boolean success;
    private boolean error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Transaksi> getData() {
        return data;
    }

    public void setData(List<Transaksi> data) {
        this.data = data;
    }

    public int getTotal_pemasukan() {
        return total_pemasukan;
    }

    public void setTotal_pemasukan(int total_pemasukan) {
        this.total_pemasukan = total_pemasukan;
    }

    public int getTotal_pengeluaran() {
        return total_pengeluaran;
    }

    public void setTotal_pengeluaran(int total_pengeluaran) {
        this.total_pengeluaran = total_pengeluaran;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private List<Transaksi> data;
    private int total_pemasukan;
    private int total_pengeluaran;
    private int saldo;
    private String message;
}
