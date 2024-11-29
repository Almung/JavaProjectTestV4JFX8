package view.dialogs;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Property;

public class TransactionDialog extends Dialog<Double> {
    private ComboBox<Property> propertyComboBox;
    private TextField amountField;
    private TextArea descriptionArea;

    public TransactionDialog(List<Property> properties, String title) {
        setTitle(title);

        // Création des champs
        propertyComboBox = new ComboBox<>();
        propertyComboBox.getItems().addAll(properties);

        amountField = new TextField();
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Propriété:"), 0, 0);
        grid.add(propertyComboBox, 1, 0);
        grid.add(new Label("Montant (€):"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descriptionArea, 1, 2);

        getDialogPane().setContent(grid);

        // Boutons
        ButtonType addButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    return Double.parseDouble(amountField.getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
    }

    public Property getSelectedProperty() {
        return propertyComboBox.getValue();
    }

    public String getDescription() {
        return descriptionArea.getText();
    }
}