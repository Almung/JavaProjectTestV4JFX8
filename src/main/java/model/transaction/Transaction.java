package model.transaction;

import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final double amount;
    private final String description;
    private final LocalDate date;
    private final TransactionType type;
    private final UUID propertyId;

    public Transaction(double amount, String description, TransactionType type, UUID propertyId) {
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.description = description;
        this.date = LocalDate.now();
        this.type = type;
        this.propertyId = propertyId;
    }

    public UUID getId() { return id; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public TransactionType getType() { return type; }
    public UUID getPropertyId() { return propertyId; }

    @Override
    public String toString() {
        return String.format("%s - %s: %.2f€ - %s", date, type, amount, description);
    }
}