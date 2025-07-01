package models;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String email;
    private String password;
    private String phone;
    private String gender;
    private String fullName;
    private String wardId;
    private  String addressDetail;
    private int roleId;
    private String googleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int districtId;
    private int provinceId;
    // Constructors
    public User() {}

    public User(int userId, String email, String phone, String password, String gender, String fullName, String wardId, String addressDetail, int roleId, String googleId, LocalDateTime createdAt, LocalDateTime updatedAt,int districtId, int provinceId) {
        this.userId = userId;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.fullName = fullName;
        this.wardId = wardId;
        this.addressDetail = addressDetail;
        this.roleId = roleId;
        this.googleId = googleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.districtId = districtId;
        this.provinceId = provinceId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}