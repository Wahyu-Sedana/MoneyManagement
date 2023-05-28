package com.example.keuangan.models;

import java.util.List;

public class ProfileResponse {
    private boolean success;
    private boolean error;
    private List<User> data;
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

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class User {
        private String id_user;
        private String nama_usaha;
        private String alamat;
        private String email;
        private String tgl_register;

        public String getId_user() {
            return id_user;
        }

        public void setId_user(String id_user) {
            this.id_user = id_user;
        }

        public String getNama_usaha() {
            return nama_usaha;
        }

        public void setNama_usaha(String nama_usaha) {
            this.nama_usaha = nama_usaha;
        }

        public String getAlamat() {
            return alamat;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTgl_register() {
            return tgl_register;
        }

        public void setTgl_register(String tgl_register) {
            this.tgl_register = tgl_register;
        }
    }
}
