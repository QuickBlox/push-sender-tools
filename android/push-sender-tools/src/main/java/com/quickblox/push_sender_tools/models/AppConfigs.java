package com.quickblox.push_sender_tools.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppConfigs implements Serializable {

    public AppConfigs() {
    }

    @SerializedName("app_id")
    private String appId;

    @SerializedName("auth_key")
    private String authKey;

    @SerializedName("auth_secret")
    private String authSecret;

    @SerializedName("account_key")
    private String accountKey;

    @SerializedName("api_domain")
    private String apiDomain;

    @SerializedName("chat_domain")
    private String chatDomain;

    @SerializedName("user_login")
    private String userLogin;

    @SerializedName("user_password")
    private String userPassword;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(String authSecret) {
        this.authSecret = authSecret;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getApiDomain() {
        return apiDomain;
    }

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public String getChatDomain() {
        return chatDomain;
    }

    public void setChatDomain(String chatDomain) {
        this.chatDomain = chatDomain;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
