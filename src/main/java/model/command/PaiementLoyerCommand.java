package model.command;

import model.Property;
import model.PropertyManager;
import model.transaction.Transaction;
import model.transaction.TransactionType;

public class PaiementLoyerCommand implements TransactionCommand {
    private final PropertyManager propertyManager;
    private final Property property;
    private final double amount;
    private Transaction transaction;

    public PaiementLoyerCommand(PropertyManager propertyManager, Property property, double amount) {
        this.propertyManager = propertyManager;
        this.property = property;
        this.amount = amount;
    }

    @Override
    public void execute() {
        transaction = new Transaction(
            amount,
            "Paiement de loyer pour " + property.getName(),
            TransactionType.RENT_PAYMENT,
            property.getId()
        );
        propertyManager.addTransaction(transaction);
    }

    @Override
    public void undo() {
        propertyManager.removeTransaction(transaction);
        Transaction refund = new Transaction(
            -amount,
            "Annulation du paiement de loyer pour " + property.getName(),
            TransactionType.RENT_REFUND,
            property.getId()
        );
        propertyManager.addTransaction(refund);
    }

    @Override
    public void redo() {
        execute();
    }
}