package model.command;

import model.Property;
import model.PropertyManager;
import model.transaction.Transaction;
import model.transaction.TransactionType;

public class FraisMaintenanceCommand implements TransactionCommand {
    private final PropertyManager propertyManager;
    private final Property property;
    private final double amount;
    private final String description;
    private Transaction transaction;

    public FraisMaintenanceCommand(PropertyManager propertyManager, Property property,
                                double amount, String description) {
        this.propertyManager = propertyManager;
        this.property = property;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public void execute() {
        transaction = new Transaction(
            -amount,
            "Frais de maintenance: " + description + " - " + property.getName(),
            TransactionType.MAINTENANCE_COST,
            property.getId()
        );
        propertyManager.addTransaction(transaction);
    }

    @Override
    public void undo() {
        propertyManager.removeTransaction(transaction);
        Transaction refund = new Transaction(
            amount,
            "Annulation des frais de maintenance: " + description + " - " + property.getName(),
            TransactionType.MAINTENANCE_REFUND,
            property.getId()
        );
        propertyManager.addTransaction(refund);
    }

    @Override
    public void redo() {
        execute();
    }
}