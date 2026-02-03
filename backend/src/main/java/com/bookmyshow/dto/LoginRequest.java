package com.bookmyshow.dto;

public class LoginRequest {
    private String phoneNumber;
    private String password;
    private String captchaToken;

    public LoginRequest() {
    }

    public LoginRequest(String phoneNumber, String password, String captchaToken) {
        this.phoneNumber = phoneNumber;
        this.password = password;
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

    public String getCaptchaToken() {
        return captchaToken;
    }

    public void setCaptchaToken(String captchaToken) {
        this.captchaToken = captchaToken;
    }

    public static LoginRequestBuilder builder() {
        return new LoginRequestBuilder();
    }

    public static class LoginRequestBuilder {
        private String phoneNumber;
        private String password;
        private String captchaToken;

        public LoginRequestBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public LoginRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public LoginRequestBuilder captchaToken(String captchaToken) {
            this.captchaToken = captchaToken;
            return this;
        }

        public LoginRequest build() {
            return new LoginRequest(phoneNumber, password, captchaToken);
        }
    }
}
