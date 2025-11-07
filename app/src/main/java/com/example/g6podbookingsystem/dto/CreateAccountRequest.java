package com.example.g6podbookingsystem.dto;

public class CreateAccountRequest {
    public String email;
    public String name;
    public String phone;
    public String password;
    public int roleId;

    // ✅ Constructor rỗng để Retrofit/Gson có thể parse
    public CreateAccountRequest() {}

    // ✅ Constructor đầy đủ nếu bạn muốn tạo bằng tham số
    public CreateAccountRequest(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.roleId = roleId;
    }
}
