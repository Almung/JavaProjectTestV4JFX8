package view.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Locataire;

public class LocataireDialog extends Dialog<Locataire> {
    private TextField nameField;
    private TextField emailField;
    private TextField phoneField;

    public LocataireDialog() {
        setTitle("Nouveau Locataire");

        // Création des champs
        nameField = new TextField();
        emailField = new TextField();
        phoneField = new TextField();

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Téléphone:"), 0, 2);
        grid.add(phoneField, 1, 2);

        getDialogPane().setContent(grid);

        // Boutons
        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();

                if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
                    return new Locataire(name, email, phone);
                }
            }
            return null;
        });
    }
}