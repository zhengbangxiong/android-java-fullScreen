package com.example.verificationcodejavademo.model;

/**
 *
 * @author:ZJL
 */
public class CaptchaCheckIt {
    private String captchaType;
    private String token;
    private boolean result;
    private boolean opAdmin;
    private String captchaVerification;

    public String getCaptchaVerification() {
        return captchaVerification;
    }

    public void setCaptchaVerification(String captchaVerification) {
        this.captchaVerification = captchaVerification;
    }

    public String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        this.captchaType = captchaType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isOpAdmin() {
        return opAdmin;
    }

    public void setOpAdmin(boolean opAdmin) {
        this.opAdmin = opAdmin;
    }
}
