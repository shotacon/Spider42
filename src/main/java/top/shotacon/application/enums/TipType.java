package top.shotacon.application.enums;

public enum TipType {

    WARNING("Warning"), INFO("Info"), ERROR("Error");

    private TipType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
