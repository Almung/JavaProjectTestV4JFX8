package model;

public class PropertyFactory {
    public static Property createProperty(PropertyType type, String name, double area, double value) {
        return new Property(name, area, value, type);
    }
}