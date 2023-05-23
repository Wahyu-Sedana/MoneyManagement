package com.example.keuangan.models;

public class ProfileResponse {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProfileUser getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(ProfileUser userProfile) {
        this.userProfile = userProfile;
    }

    private boolean success;
    private boolean error;
    private String message;

    private ProfileUser userProfile;
}
