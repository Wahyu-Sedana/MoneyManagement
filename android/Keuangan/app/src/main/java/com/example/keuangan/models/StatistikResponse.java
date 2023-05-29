package com.example.keuangan.models;

import java.util.List;

public class StatistikResponse {
    private boolean success;
    private boolean error;
    private List<DataPemasukan> dataPemasukan;

    public List<DataPengeluaran> getDataPengeluaran() {
        return dataPengeluaran;
    }

    public void setDataPengeluaran(List<DataPengeluaran> dataPengeluaran) {
        this.dataPengeluaran = dataPengeluaran;
    }

    private List<DataPengeluaran> dataPengeluaran;

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

    private int total_pemasukan;
    private int total_pengeluaran;
    private int saldo;
    private String message;

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

    public List<DataPemasukan> getDataPemasukan() {
        return dataPemasukan;
    }

    public void setDataPemasukan(List<DataPemasukan> dataPemasukan) {
        this.dataPemasukan = dataPemasukan;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
