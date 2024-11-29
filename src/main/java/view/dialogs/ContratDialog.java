package view.dialogs;

import java.time.LocalDate;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Contrat;
import model.Locataire;
import model.Property;

public class ContratDialog extends Dialog<Contrat> {
    private ComboBox<Property> propertyComboBox;
    private ComboBox<Locataire> tenantComboBox;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TextField rentField;

    public ContratDialog(List<Property> properties, List<Locataire> tenants) {
        setTitle("Nouveau Contrat");

        // Création des champs
        propertyComboBox = new ComboBox<>();
        propertyComboBox.getItems().addAll(properties);

        tenantComboBox = new ComboBox<>();
        tenantComboBox.getItems().addAll(tenants);

        startDatePicker = new DatePicker(LocalDate.now());
        endDatePicker = new DatePicker(LocalDate.now().plusYears(1));
        rentField = new TextField();

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Propriété:"), 0, 0);
        grid.add(propertyComboBox, 1, 0);
        grid.add(new Label("Locataire:"), 0, 1);
        grid.add(tenantComboBox, 1, 1);
        grid.add(new Label("Date début:"), 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(new Label("Date fin:"), 0, 3);
        grid.add(endDatePicker, 1, 3);
        grid.add(new Label("Loyer mensuel (€):"), 0, 4);
        grid.add(rentField, 1, 4);

        getDialogPane().setContent(grid);

        // Boutons
        ButtonType addButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    Property property = propertyComboBox.getValue();
                    Locataire tenant = tenantComboBox.getValue();
                    LocalDate startDate = startDatePicker.getValue();
                    LocalDate endDate = endDatePicker.getValue();
                    double rent = Double.parseDouble(rentField.getText());

                    if (property != null && tenant != null && startDate != null && endDate != null) {
                        return new Contrat(property, tenant, startDate, endDate, rent);
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
    }
}