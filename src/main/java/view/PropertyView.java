package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import model.Contrat;
import model.Locataire;
import model.Property;
import model.PropertyFactory;
import model.PropertyObserver;
import model.PropertyType;
import model.transaction.Transaction;

public class PropertyView implements PropertyObserver {
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    public PropertyView() {
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public void displayMenu() {
        System.out.println("\n=== Gestion Immobilière ===");
        System.out.println("1. Ajouter une propriété");
        System.out.println("2. Ajouter un locataire");
        System.out.println("3. Créer un contrat");
        System.out.println("4. Afficher les propriétés");
        System.out.println("5. Afficher les locataires");
        System.out.println("6. Afficher les contrats");
        System.out.println("7. Afficher la valeur totale");
        System.out.println("8. Afficher les revenus mensuels");
        System.out.println("9. Vérifier les contrats expirants");
        System.out.println("10. Enregistrer un paiement de loyer");
        System.out.println("11. Enregistrer des frais de maintenance");
        System.out.println("12. Annuler la dernière transaction");
        System.out.println("13. Rétablir la dernière transaction annulée");
        System.out.println("14. Afficher l'historique des transactions");
        System.out.println("0. Quitter");
        System.out.print("Choix: ");
    }

    public int getChoice() {
        return scanner.nextInt();
    }

    public Property selectProperty(List<Property> properties) {
        if (properties.isEmpty()) {
            System.out.println("Aucune propriété disponible.");
            return null;
        }

        System.out.println("\nSélectionnez une propriété:");
        for (int i = 0; i < properties.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, properties.get(i));
        }
        System.out.print("Choix (0 pour annuler): ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= properties.size()) {
            return properties.get(choice - 1);
        }
        return null;
    }
    public Property getPropertyDetails() {
        scanner.nextLine();
        System.out.print("Nom: ");
        String name = scanner.nextLine();
        System.out.print("Surface (m²): ");
        double area = scanner.nextDouble();
        System.out.print("Valeur (€): ");
        double value = scanner.nextDouble();

        System.out.println("Type de propriété:");
        PropertyType[] types = PropertyType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("%d. %s%n", i + 1, types[i].getLabel());
        }
        System.out.print("Choix: ");
        int typeChoice = scanner.nextInt() - 1;

        return PropertyFactory.createProperty(types[typeChoice], name, area, value);
    }

    public Locataire getTenantDetails() {
        scanner.nextLine();
        System.out.print("Nom: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Téléphone: ");
        String phone = scanner.nextLine();

        return new Locataire(name, email, phone);
    }

    public Contrat getContractDetails(List<Property> properties, List<Locataire> tenants) {
        if (properties.isEmpty() || tenants.isEmpty()) {
            System.out.println("Impossible de créer un contrat sans propriété ou locataire");
            return null;
        }

        System.out.println("\nChoisir une propriété:");
        for (int i = 0; i < properties.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, properties.get(i));
        }
        System.out.print("Choix: ");
        Property property = properties.get(scanner.nextInt() - 1);

        System.out.println("\nChoisir un locataire:");
        for (int i = 0; i < tenants.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, tenants.get(i));
        }
        System.out.print("Choix: ");
        Locataire tenant = tenants.get(scanner.nextInt() - 1);

        scanner.nextLine();
        System.out.print("Date de début (dd/MM/yyyy): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

        System.out.print("Date de fin (dd/MM/yyyy): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

        System.out.print("Loyer mensuel (€): ");
        double monthlyRent = scanner.nextDouble();

        return new Contrat(property, tenant, startDate, endDate, monthlyRent);
    }

    public void displayProperties(List<Property> properties) {
        System.out.println("\nListe des propriétés:");
        properties.forEach(System.out::println);
    }

    public void displayTenants(List<Locataire> tenants) {
        System.out.println("\nListe des locataires:");
        tenants.forEach(System.out::println);
    }

    public void displayContracts(List<Contrat> contracts) {
        System.out.println("\nListe des contrats:");
        contracts.forEach(System.out::println);
    }

    public void displayTotalValue(double value) {
        System.out.printf("\nValeur totale du patrimoine: %.2f€%n", value);
    }

    public void displayMonthlyIncome(double income) {
        System.out.printf("\nRevenus locatifs mensuels: %.2f€%n", income);
    }


    public double getTransactionAmount(String prompt) {
        System.out.println(prompt);
        return scanner.nextDouble();
    }

    public String getTransactionDescription() {
        scanner.nextLine(); // Clear buffer
        System.out.println("Description de la transaction:");
        return scanner.nextLine();
    }

    public void displayTransactions(List<Transaction> transactions) {
        System.out.println("\nHistorique des transactions:");
        transactions.forEach(System.out::println);
    }

    // ... (rest of the existing methods remain unchanged)

    @Override
    public void update(String message) {
        System.out.println("\nNotification: " + message);
    }
}