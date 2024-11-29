package model;

import java.time.LocalDate;
import java.util.UUID;

public class Contrat {
    private UUID id;
    private Property property;
    private Locataire locataire;
    private LocalDate startDate;
    private LocalDate endDate;
    private double monthlyRent;
    private boolean active;

    public Contrat(Property property, Locataire locataire, LocalDate startDate,
                   LocalDate endDate, double monthlyRent) {
        if (!property.getState().canBeRented()) {
            throw new IllegalStateException("Cette propriété ne peut pas être louée actuellement.");
        }

        this.id = UUID.randomUUID();
        this.property = property;
        this.locataire = locataire;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyRent = monthlyRent;
        this.active = true;
        property.changeState(property.getState().nextState());
    }

    public UUID getId() { return id; }
    public Property getProperty() { return property; }
    public Locataire getTenant() { return locataire; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getMonthlyRent() { return monthlyRent; }
    public boolean isActive() { return active; }

    public void terminate() {
        this.active = false;
        property.changeState(new DisponibleState());
    }

    public boolean isExpiringSoon() {
        return active && LocalDate.now().plusMonths(1).isAfter(endDate);
    }

    @Override
    public String toString() {
        return String.format("Contrat: %s - Locataire: %s - Du %s au %s - %,.2f€/mois - %s",
            property.getName(), locataire.getName(), startDate, endDate, monthlyRent,
            active ? "Actif" : "Terminé");
    }
}