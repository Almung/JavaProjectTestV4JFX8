package model.transaction;

public enum TransactionType {
    RENT_PAYMENT("Paiement de loyer"),
    RENT_REFUND("Remboursement de loyer"),
    MAINTENANCE_COST("Frais de maintenance"),
    MAINTENANCE_REFUND("Remboursement maintenance"),
    ADJUSTMENT("Ajustement");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}