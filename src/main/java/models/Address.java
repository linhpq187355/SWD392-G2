package models;

import java.security.Timestamp;

public class Address {
    private int addressId;
    private int userId;
    private String addressDetail;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Address() {
    }

    public Address(int addressId, int userId, String addressDetail, Timestamp createdAt, Timestamp updatedAt) {
        this.addressId = addressId;
        this.userId = userId;
        this.addressDetail = addressDetail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
