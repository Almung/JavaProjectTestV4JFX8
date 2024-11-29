package model;

class DisponibleState implements PropertyState {
    @Override
    public String getStateName() {
        return "Disponible";
    }

    @Override
    public boolean canBeRented() {
        return true;
    }

    @Override
    public boolean canBeMaintenanced() {
        return true;
    }

    @Override
    public PropertyState nextState() {
        return new LouerState();
    }
}