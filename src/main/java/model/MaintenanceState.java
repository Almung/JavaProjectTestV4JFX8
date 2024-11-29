package model;

class MaintenanceState implements PropertyState {
    @Override
    public String getStateName() {
        return "En maintenance";
    }

    @Override
    public boolean canBeRented() {
        return false;
    }

    @Override
    public boolean canBeMaintenanced() {
        return false;
    }

    @Override
    public PropertyState nextState() {
        return new DisponibleState();
    }
}
