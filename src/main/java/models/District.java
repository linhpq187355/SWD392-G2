package models;

public class District {
    private int districtId;
    private int provinceId;
    private String name;

    // Constructors
    public District() {}

    public District(int districtId, int provinceId, String name) {
        this.districtId = districtId;
        this.provinceId = provinceId;
        this.name = name;
    }

    // Getters and Setters
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}