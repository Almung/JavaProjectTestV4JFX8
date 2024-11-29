package controller;

import model.Contrat;
import model.Locataire;
import model.Property;
import model.PropertyManager;
import model.command.FraisMaintenanceCommand;
import model.command.PaiementLoyerCommand;
import view.MainView;

public class PropertyController {
    private PropertyManager model;
    private MainView view;

    public PropertyController(PropertyManager model, MainView view) {
        this.model = model;
        this.view = view;
    }

    public void handleUndo() {
        if (model.getTransactionInvoker().canUndo()) {
            model.getTransactionInvoker().undo();
        }
    }

    public void handleRedo() {
        if (model.getTransactionInvoker().canRedo()) {
            model.getTransactionInvoker().redo();
        }
    }

    public void handleRentPayment(Property property, double amount) {
        PaiementLoyerCommand command = new PaiementLoyerCommand(model, property, amount);
        model.getTransactionInvoker().executeCommand(command);
    }

    public void handleMaintenanceCost(Property property, double amount, String description) {
        FraisMaintenanceCommand command = new FraisMaintenanceCommand(model, property, amount, description);
        model.getTransactionInvoker().executeCommand(command);
    }

    public void addProperty(Property property) {
        model.addProperty(property);
    }

    public void addLocataire(Locataire locataire) {
        model.addLocataire(locataire);
    }

    public void addContrat(Contrat contrat) {
        model.addContrat(contrat);
    }

    public void checkExpiringContracts() {
        model.checkExpiringContracts();
    }
}