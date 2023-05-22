package com.example.keuangan.models;

public class Kategori {
    private int id_kategori;
    private String kategori;

    private int id_jenis;

    public Kategori(int id_kategori, int id_jenis, String kategori) {
        this.id_kategori = id_kategori;
        this.id_jenis = id_jenis;
        this.kategori = kategori;
    }

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public int getId_jenis() {
        return id_jenis;
    }

    public void setId_jenis(int id_jenis) {
        this.id_jenis = id_jenis;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }


    public String toString() {
        return kategori;
    }

}

