package models;

import java.time.LocalDateTime;

public class Order {
    private String orderId;
    private Integer userId; // nullable cho guest
    private int statusId;
    private String receiveName;
    private String receivePhone;
    private String receiveAddress;
    private String receiveEmail;
    private String shippingMethod;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order() {
    }

    public Order(LocalDateTime updatedAt, LocalDateTime createdAt, String note, String shippingMethod, String receiveEmail, String receiveAddress, String receivePhone, String receiveName, int statusId, Integer userId, String orderId) {
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.note = note;
        this.shippingMethod = shippingMethod;
        this.receiveEmail = receiveEmail;
        this.receiveAddress = receiveAddress;
        this.receivePhone = receivePhone;
        this.receiveName = receiveName;
        this.statusId = statusId;
        this.userId = userId;
        this.orderId = orderId;
    }
// Getters and Setters


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(String receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
