package view;

import java.util.Optional;

import controller.PropertyController;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Contrat;
import model.Locataire;
import model.Property;
import model.PropertyManager;
import model.PropertyObserver;
import model.transaction.Transaction;
import view.dialogs.ContratDialog;
import view.dialogs.LocataireDialog;
import view.dialogs.PropertyDialog;
import view.dialogs.TransactionDialog;

public class MainView extends Application implements PropertyObserver {
    private PropertyManager model;
    private PropertyController controller;

    // Composants UI
    private TabPane tabPane;
    private TableView<Property> propertyTable;
    private TableView<Locataire> locataireTable;
    private TableView<Contrat> contratTable;
    private TableView<Transaction> transactionTable;
    private TextArea notificationArea;

    @Override
    public void start(Stage primaryStage) {
        initializeModel();
        createUI(primaryStage);
    }

    private void initializeModel() {
        model = new PropertyManager();
        controller = new PropertyController(model, this);
        model.addObserver(this);
    }

    private void createUI(Stage primaryStage) {
        tabPane = new TabPane();

        // Création des onglets
        Tab propertiesTab = createPropertiesTab();
        Tab tenantsTab = createTenantsTab();
        Tab contractsTab = createContractsTab();
        Tab transactionsTab = createTransactionsTab();
        Tab dashboardTab = createDashboardTab();

        tabPane.getTabs().addAll(
            propertiesTab,
            tenantsTab,
            contractsTab,
            transactionsTab,
            dashboardTab
        );

        // Zone de notifications
        notificationArea = new TextArea();
        notificationArea.setEditable(false);
        notificationArea.setPrefRowCount(3);
        notificationArea.setPromptText("Notifications...");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(tabPane, notificationArea);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle("Gestion Immobilière");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createPropertiesTab() {
        Tab tab = new Tab("Propriétés");
        tab.setClosable(false);

        propertyTable = new TableView<>();
        setupPropertyTable();

        Button addButton = new Button("Ajouter une propriété");
        addButton.setOnAction(event -> showAddPropertyDialog());

        Button maintenanceButton = new Button("Gérer la maintenance");
        maintenanceButton.setOnAction(event -> handleMaintenanceAction());

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(addButton, maintenanceButton);

        VBox content = new VBox(10);
        content.getChildren().addAll(buttonBox, propertyTable);
        tab.setContent(content);

        return tab;
    }

    private Tab createTenantsTab() {
        Tab tab = new Tab("Locataires");
        tab.setClosable(false);

        locataireTable = new TableView<>();
        setupLocataireTable();

        Button addButton = new Button("Ajouter un locataire");
        addButton.setOnAction(event -> showAddTenantDialog());

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(addButton, locataireTable);
        tab.setContent(content);

        return tab;
    }

    private Tab createContractsTab() {
        Tab tab = new Tab("Contrats");
        tab.setClosable(false);

        contratTable = new TableView<>();
        setupContratTable();

        Button addButton = new Button("Nouveau contrat");
        addButton.setOnAction(event -> showAddContractDialog());

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(addButton, contratTable);
        tab.setContent(content);

        return tab;
    }

    private Tab createTransactionsTab() {
        Tab tab = new Tab("Transactions");
        tab.setClosable(false);

        transactionTable = new TableView<>();
        setupTransactionTable();

        Button rentButton = new Button("Enregistrer un loyer");
        Button maintenanceButton = new Button("Frais de maintenance");
        Button undoButton = new Button("Annuler");
        Button redoButton = new Button("Rétablir");

        rentButton.setOnAction(event -> showRentPaymentDialog());
        maintenanceButton.setOnAction(event -> showMaintenanceCostDialog());
        undoButton.setOnAction(event -> controller.handleUndo());
        redoButton.setOnAction(event -> controller.handleRedo());

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(rentButton, maintenanceButton, undoButton, redoButton);

        VBox content = new VBox(10);
        content.getChildren().addAll(buttonBox, transactionTable);
        tab.setContent(content);

        return tab;
    }

    private Tab createDashboardTab() {
        Tab tab = new Tab("Tableau de bord");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label totalValueLabel = new Label("Valeur totale du patrimoine:");
        Label monthlyIncomeLabel = new Label("Revenus mensuels:");
        Label occupancyRateLabel = new Label("Taux d'occupation:");

        TextField totalValueField = new TextField();
        TextField monthlyIncomeField = new TextField();
        TextField occupancyRateField = new TextField();

        totalValueField.setEditable(false);
        monthlyIncomeField.setEditable(false);
        occupancyRateField.setEditable(false);

        grid.add(totalValueLabel, 0, 0);
        grid.add(totalValueField, 1, 0);
        grid.add(monthlyIncomeLabel, 0, 1);
        grid.add(monthlyIncomeField, 1, 1);
        grid.add(occupancyRateLabel, 0, 2);
        grid.add(occupancyRateField, 1, 2);

        Button refreshButton = new Button("Actualiser");
        refreshButton.setOnAction(event -> updateDashboard());

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(grid, refreshButton);
        tab.setContent(content);

        return tab;
    }

    private void setupPropertyTable() {
        TableColumn<Property, String> nameCol = new TableColumn<>("Nom");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Property, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getLabel()));

        TableColumn<Property, Number> areaCol = new TableColumn<>("Surface");
        areaCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getArea()));

        TableColumn<Property, Number> valueCol = new TableColumn<>("Valeur");
        valueCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValue()));

        TableColumn<Property, String> stateCol = new TableColumn<>("État");
        stateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().getStateName()));

        propertyTable.getColumns().addAll(nameCol, typeCol, areaCol, valueCol, stateCol);
        refreshPropertyTable();
    }

    private void setupLocataireTable() {
        TableColumn<Locataire, String> nameCol = new TableColumn<>("Nom");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Locataire, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<Locataire, String> phoneCol = new TableColumn<>("Téléphone");
        phoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));

        locataireTable.getColumns().addAll(nameCol, emailCol, phoneCol);
        refreshTenantTable();
    }

    private void setupContratTable() {
        TableColumn<Contrat, String> propertyCol = new TableColumn<>("Propriété");
        propertyCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProperty().getName()));

        TableColumn<Contrat, String> tenantCol = new TableColumn<>("Locataire");
        tenantCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTenant().getName()));

        TableColumn<Contrat, String> startDateCol = new TableColumn<>("Date début");
        startDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDate().toString()));

        TableColumn<Contrat, String> endDateCol = new TableColumn<>("Date fin");
        endDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDate().toString()));

        TableColumn<Contrat, Number> rentCol = new TableColumn<>("Loyer");
        rentCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMonthlyRent()));

        contratTable.getColumns().addAll(propertyCol, tenantCol, startDateCol, endDateCol, rentCol);
        refreshContractTable();
    }

    private void setupTransactionTable() {
        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getLabel()));

        TableColumn<Transaction, Number> amountCol = new TableColumn<>("Montant");
        amountCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()));

        TableColumn<Transaction, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        transactionTable.getColumns().addAll(dateCol, typeCol, amountCol, descriptionCol);
        refreshTransactionTable();
    }

    private void refreshPropertyTable() {
        propertyTable.getItems().setAll(model.getProperties());
    }

    private void refreshTenantTable() {
        locataireTable.getItems().setAll(model.getLocataires());
    }

    private void refreshContractTable() {
        contratTable.getItems().setAll(model.getContrats());
    }

    private void refreshTransactionTable() {
        transactionTable.getItems().setAll(model.getTransactions());
    }

    private void updateDashboard() {
        double totalValue = model.getTotalValue();
        double monthlyIncome = model.getMonthlyRentalIncome();

        // Mise à jour des champs du tableau de bord
        ((TextField) ((GridPane) ((VBox) tabPane.getTabs().get(4).getContent()).getChildren().get(0))
            .getChildren().get(1)).setText(String.format("%.2f €", totalValue));

        ((TextField) ((GridPane) ((VBox) tabPane.getTabs().get(4).getContent()).getChildren().get(0))
            .getChildren().get(3)).setText(String.format("%.2f €", monthlyIncome));

        // Calcul et affichage du taux d'occupation
        long totalProperties = model.getProperties().size();
        long rentedProperties = model.getContrats().stream().filter(Contrat::isActive).count();
        double occupancyRate = totalProperties > 0 ? (rentedProperties * 100.0) / totalProperties : 0;

        ((TextField) ((GridPane) ((VBox) tabPane.getTabs().get(4).getContent()).getChildren().get(0))
            .getChildren().get(5)).setText(String.format("%.1f%%", occupancyRate));
    }

    private void showAddPropertyDialog() {
        PropertyDialog dialog = new PropertyDialog();
        Optional<Property> result = dialog.showAndWait();
        result.ifPresent(property -> {
            controller.addProperty(property);
            refreshPropertyTable();
            updateDashboard();
        });
    }

    private void showAddTenantDialog() {
        LocataireDialog dialog = new LocataireDialog();
        Optional<Locataire> result = dialog.showAndWait();
        result.ifPresent(tenant -> {
            controller.addLocataire(tenant);
            refreshTenantTable();
        });
    }

    private void showAddContractDialog() {
        ContratDialog dialog = new ContratDialog(model.getProperties(), model.getLocataires());
        Optional<Contrat> result = dialog.showAndWait();
        result.ifPresent(contract -> {
            controller.addContrat(contract);
            refreshContractTable();
            updateDashboard();
        });
    }

    private void showRentPaymentDialog() {
        TransactionDialog dialog = new TransactionDialog(model.getProperties(), "Paiement de loyer");
        Optional<Double> result = dialog.showAndWait();
        result.ifPresent(amount -> {
            Property property = dialog.getSelectedProperty();
            if (property != null) {
                controller.handleRentPayment(property, amount);
                refreshTransactionTable();
                updateDashboard();
            }
        });
    }

    private void showMaintenanceCostDialog() {
        TransactionDialog dialog = new TransactionDialog(model.getProperties(), "Frais de maintenance");
        Optional<Double> result = dialog.showAndWait();
        result.ifPresent(amount -> {
            Property property = dialog.getSelectedProperty();
            String description = dialog.getDescription();
            if (property != null) {
                controller.handleMaintenanceCost(property, amount, description);
                refreshTransactionTable();
                updateDashboard();
            }
        });
    }

    private void handleMaintenanceAction() {
        Property selectedProperty = propertyTable.getSelectionModel().getSelectedItem();
        if (selectedProperty != null) {
            try {
                selectedProperty.startMaintenance();
                refreshPropertyTable();
            } catch (IllegalStateException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible de mettre en maintenance");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText("Aucune propriété sélectionnée");
            alert.setContentText("Veuillez sélectionner une propriété dans la liste.");
            alert.showAndWait();
        }
    }

    @Override
    public void update(String message) {
        notificationArea.appendText(message + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}