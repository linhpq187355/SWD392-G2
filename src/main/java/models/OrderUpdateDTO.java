package models;

import java.util.Map;

public class OrderUpdateDTO {
    private String orderId;
    private String receiverName;
    private String receiverPhone;
    private String receiverMail;
    private int provinceId;
    private int districtId;
    private int wardId;
    private String street;
    private String orderNotes;
    private int statusId;
    private Map<Integer, Integer> quantities;

    public OrderUpdateDTO() {
    }

    public OrderUpdateDTO(String orderId, String receiverName, String receiverPhone, String receiverMail, int provinceId, int wardId, int districtId, String street, String orderNotes, int statusId, Map<Integer, Integer> quantities) {
        this.orderId = orderId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverMail = receiverMail;
        this.provinceId = provinceId;
        this.wardId = wardId;
        this.districtId = districtId;
        this.street = street;
        this.orderNotes = orderNotes;
        this.statusId = statusId;
        this.quantities = quantities;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getReceiverMail() {
        return receiverMail;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Map<Integer, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<Integer, Integer> quantities) {
        this.quantities = quantities;
    }
}