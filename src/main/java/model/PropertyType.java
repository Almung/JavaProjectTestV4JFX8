package model;

public enum PropertyType {
    APARTMENT("Appartement"),
    OFFICE("Bureau"),
    COMMERCIAL("Local Commercial"),
    LAND("Terrain");

    private final String label;

    PropertyType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}