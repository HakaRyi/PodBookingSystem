package com.example.g6podbookingsystem.models;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("accId")
    public int accId;
    @SerializedName("name")
    public String name;
    @SerializedName("password")
    public String password;
    @SerializedName("phone")
    public String phone;
    @SerializedName("email")
    public String email;
    @SerializedName("roleId")
    public int roleId;
    @SerializedName("avatarUrl")
    public String avatarUrl;

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
