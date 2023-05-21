package com.example.keuangan.models;

public class LoginResponse {
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
    private DataUser data;

    public DataUser getData() {
        return data;
    }

    public void setData(DataUser data) {
        this.data = data;
    }

    public class DataUser {
        private int id_user;
        private String email;
        private String nama_usaha;

        public int getId_user() {
            return id_user;
        }

        public void setId_user(int id_user) {
            this.id_user = id_user;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNama_usaha() {
            return nama_usaha;
        }

        public void setNama_usaha(String nama_usaha) {
            this.nama_usaha = nama_usaha;
        }
    }
}
