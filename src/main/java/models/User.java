package models;

public class User {
    private int userId;
    private String email;
    private String password;
    private String phone;
    private String gender;
    private String fullName;
    private int roleId;
    private String googleId;
    private Address address;

    public User(int userId, String email, String password, String phone, String gender, String fullName, int roleId, String googleId) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.fullName = fullName;
        this.roleId = roleId;
        this.googleId = googleId;
    }
    public User(int userId, String email, String password, String phone, String gender, String fullName, int roleId, String googleId, Address address) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.fullName = fullName;
        this.roleId = roleId;
        this.googleId = googleId;
        this.address =address;
    }

    public User() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

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
}
