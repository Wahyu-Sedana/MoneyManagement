package com.example.keuangan.models;

import java.util.List;

public class StatistikResponse {
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

    public List<DataItem> getDataPemasukan() {
        return dataPemasukan;
    }

    public void setDataPemasukan(List<DataItem> dataPemasukan) {
        this.dataPemasukan = dataPemasukan;
    }

    public List<DataItem> getDataPengeluaran() {
        return dataPengeluaran;
    }

    public void setDataPengeluaran(List<DataItem> dataPengeluaran) {
        this.dataPengeluaran = dataPengeluaran;
    }

    public int getTotalPemasukan() {
        return totalPemasukan;
    }

    public void setTotalPemasukan(int totalPemasukan) {
        this.totalPemasukan = totalPemasukan;
    }

    public int getTotalPengeluaran() {
        return totalPengeluaran;
    }

    public void setTotalPengeluaran(int totalPengeluaran) {
        this.totalPengeluaran = totalPengeluaran;
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

    private boolean success;
    private boolean error;
    private List<DataItem> dataPemasukan;
    private List<DataItem> dataPengeluaran;
    private int totalPemasukan;
    private int totalPengeluaran;
    private int saldo;
    private String message;
}
