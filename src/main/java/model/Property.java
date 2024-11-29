package model;

import java.util.UUID;

public class Property {
    private UUID id;
    private String name;
    private double area;
    private double value;
    private PropertyType type;
    private PropertyState state;

    public Property(String name, double area, double value, PropertyType type) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.area = area;
        this.value = value;
        this.type = type;
        this.state = new DisponibleState();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public double getArea() { return area; }
    public double getValue() { return value; }
    public PropertyType getType() { return type; }
    public PropertyState getState() { return state; }

    public void changeState(PropertyState newState) {
        this.state = newState;
    }

    public void startMaintenance() {
        if (state.canBeMaintenanced()) {
            this.state = new MaintenanceState();
        } else {
            throw new IllegalStateException("La propriété ne peut pas être mise en maintenance dans son état actuel.");
        }
    }

    public void endMaintenance() {
        if (state instanceof MaintenanceState) {
            this.state = state.nextState();
        } else {
            throw new IllegalStateException("La propriété n'est pas en maintenance.");
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %.2fm² - %.2f€ - %s",
            name, type, area, value, state.getStateName());
    }
}