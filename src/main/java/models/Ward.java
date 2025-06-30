package models;

public class Ward {
    private int wardId;
    private String name;
    private int districtId;

    // Constructors
    public Ward() {}

    public Ward(int wardId, String name, int districtId) {
        this.wardId = wardId;
        this.name = name;
        this.districtId = districtId;
    }

    // Getters and Setters
    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }
}