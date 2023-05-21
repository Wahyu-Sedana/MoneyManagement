package com.example.keuangan.models;

import java.util.List;

public class KategoriResponse {
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    private boolean success;
    private String message;

    public List<Kategori> getData() {
        return data;
    }

    public void setData(List<Kategori> data) {
        this.data = data;
    }

    private List<Kategori> data;
}
