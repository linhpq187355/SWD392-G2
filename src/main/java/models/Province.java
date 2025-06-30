package models;

public class Province {
    private int provinceId;
    private String name;

    // Constructors
    public Province() {}

    public Province(int provinceId, String name) {
        this.provinceId = provinceId;
        this.name = name;
    }

    // Getters and Setters
    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}