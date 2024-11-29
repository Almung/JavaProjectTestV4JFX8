package model;

class LouerState implements PropertyState {
    @Override
    public String getStateName() {
        return "Loué";
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
