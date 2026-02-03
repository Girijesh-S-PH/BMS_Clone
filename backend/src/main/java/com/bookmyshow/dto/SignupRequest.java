package com.bookmyshow.dto;

public class SignupRequest {
    private String phoneNumber;
    private String password;
    private String fullName;
    private String email;
    private String captchaToken;

    public SignupRequest() {
    }

    public SignupRequest(String phoneNumber, String password, String fullName, String email, String captchaToken) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.captchaToken = captchaToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCaptchaToken() {
        return captchaToken;
    }

    public void setCaptchaToken(String captchaToken) {
        this.captchaToken = captchaToken;
    }

    public static SignupRequestBuilder builder() {
        return new SignupRequestBuilder();
    }

    public static class SignupRequestBuilder {
        private String phoneNumber;
        private String password;
        private String fullName;
        private String email;
        private String captchaToken;

        public SignupRequestBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public SignupRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public SignupRequestBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public SignupRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public SignupRequestBuilder captchaToken(String captchaToken) {
            this.captchaToken = captchaToken;
            return this;
        }

        public SignupRequest build() {
            return new SignupRequest(phoneNumber, password, fullName, email, captchaToken);
        }
    }
}
