package model;

import java.util.ArrayList;
import java.util.List;

import model.command.TransactionInvoker;
import model.transaction.Transaction;

public class PropertyManager {
    private List<Property> properties;
    private List<Locataire> locataires;
    private List<Contrat> contracts;
    private List<Transaction> transactions;
    private List<PropertyObserver> observers;
    private TransactionInvoker transactionInvoker;

    public PropertyManager() {
        this.properties = new ArrayList<>();
        this.locataires = new ArrayList<>();
        this.contracts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.transactionInvoker = new TransactionInvoker();
    }

    public void addProperty(Property property) {
        properties.add(property);
        notifyObservers("Nouvelle propriété ajoutée: " + property.getName());
    }

    public void addLocataire(Locataire tenant) {
        locataires.add(tenant);
        notifyObservers("Nouveau locataire ajouté: " + tenant.getName());
    }

    public void addContrat(Contrat contract) {
        contracts.add(contract);
        notifyObservers("Nouveau contrat créé pour: " + contract.getProperty().getName());
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        notifyObservers("Nouvelle transaction enregistrée: " + transaction.getDescription());
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        notifyObservers("Transaction annulée: " + transaction.getDescription());
    }

    public TransactionInvoker getTransactionInvoker() {
        return transactionInvoker;
    }

    public void checkExpiringContracts() {
        contracts.stream()
            .filter(Contrat::isExpiringSoon)
            .forEach(contract -> notifyObservers(
                "ATTENTION: Le contrat pour " + contract.getProperty().getName() +
                " avec " + contract.getTenant().getName() +
                " expire le " + contract.getEndDate()));
    }

    public void addObserver(PropertyObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {
        for (PropertyObserver observer : observers) {
            observer.update(message);
        }
    }

    public List<Property> getProperties() {
        return new ArrayList<>(properties);
    }

    public List<Locataire> getLocataires() {
        return new ArrayList<>(locataires);
    }

    public List<Contrat> getContrats() {
        return new ArrayList<>(contracts);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public double getTotalValue() {
        return properties.stream()
            .mapToDouble(Property::getValue)
            .sum();
    }

    public double getMonthlyRentalIncome() {
        return contracts.stream()
            .filter(Contrat::isActive)
            .mapToDouble(Contrat::getMonthlyRent)
            .sum();
    }

    public double getTransactionTotal() {
        return transactions.stream()
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
}