package models;

public class UserWithOrderCount {
    private int userId;
    private String fullName;
    private String email;
    private int totalOrders;

    public UserWithOrderCount(int userId, String fullName, String email, int totalOrders) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.totalOrders = totalOrders;
    }

    // Getter & Setter
    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public int getTotalOrders() {
        return totalOrders;
    }
}
