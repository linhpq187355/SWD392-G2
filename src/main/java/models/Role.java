package models;

public class Role {
    private int roleId;
    private String name;
    private String description;

    // Constructors
    public Role() {}

    public Role(int roleId, String name, String description) {
        this.roleId = roleId;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
