package com.example.g6podbookingsystem.dto;

public class CreateAccountRequest {
    private String email;
    private String name;
    private String phone;

    public CreateAccountRequest(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}
