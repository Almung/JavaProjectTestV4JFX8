package model;

public interface PropertyState {
    String getStateName();
    boolean canBeRented();
    boolean canBeMaintenanced();
    PropertyState nextState();
}

