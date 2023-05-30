package com.example.keuangan.models;

import java.util.Date;

public class Transaksi {
    public int getId_jenis() {
        return id_jenis;
    }

    public void setId_jenis(int id_jenis) {
        this.id_jenis = id_jenis;
    }

    private int id_jenis;

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu_input() {
        return waktu_input;
    }

    public void setWaktu_input(String waktu_input) {
        this.waktu_input = waktu_input;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    private String kategori;
    private String jumlah;
    private String catatan;
    private String tanggal;

    public Date getTanggal2() {
        return tanggal2;
    }

    public void setTanggal2(Date tanggal2) {
        this.tanggal2 = tanggal2;
    }

    private Date tanggal2;
    private String waktu_input;
    private String jenis;

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    private String id_transaksi;
}
