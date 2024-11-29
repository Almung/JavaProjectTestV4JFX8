package view.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Property;
import model.PropertyFactory;
import model.PropertyType;

public class PropertyDialog extends Dialog<Property> {
    private ComboBox<PropertyType> typeComboBox;
    private TextField nameField;
    private TextField areaField;
    private TextField valueField;

    public PropertyDialog() {
        setTitle("Nouvelle Propriété");

        // Création des champs
        nameField = new TextField();
        areaField = new TextField();
        valueField = new TextField();
        typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(PropertyType.values());

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Surface (m²):"), 0, 1);
        grid.add(areaField, 1, 1);
        grid.add(new Label("Valeur (€):"), 0, 2);
        grid.add(valueField, 1, 2);
        grid.add(new Label("Type:"), 0, 3);
        grid.add(typeComboBox, 1, 3);

        getDialogPane().setContent(grid);

        // Boutons
        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText();
                    double area = Double.parseDouble(areaField.getText());
                    double value = Double.parseDouble(valueField.getText());
                    PropertyType type = typeComboBox.getValue();

                    return PropertyFactory.createProperty(type, name, area, value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
    }
}