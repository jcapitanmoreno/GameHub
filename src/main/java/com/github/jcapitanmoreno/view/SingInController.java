package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.UsuariosDAO;
import com.github.jcapitanmoreno.model.entity.Usuarios;
import com.github.jcapitanmoreno.utils.PasswordUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SingInController {
    @FXML
    private TextField usuarioField;

    @FXML
    private TextField correoField;

    @FXML
    private PasswordField contrasenaField;

    @FXML
    private CheckBox isAdminCheckBox;

    @FXML
    private PasswordField claveAdminField;

    @FXML
    private Button accederButton;

    @FXML
    private Button volverButton;

    private static final String CLAVE_ADMIN_VALIDA = "1234"; // Clave fija para administrador

    @FXML
    public void initialize() {
        claveAdminField.setDisable(true);


        isAdminCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            claveAdminField.setDisable(!newValue);
        });
    }

    @FXML
    private void handleAccederButton() {
        String usuario = usuarioField.getText();
        String correo = correoField.getText();
        String contrasena = contrasenaField.getText();
        boolean isAdmin = isAdminCheckBox.isSelected();
        String claveAdmin = claveAdminField.getText();

        if (usuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || (isAdmin && claveAdmin.isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor, completa todos los campos requeridos.");
            return;
        }

        if (isAdmin && !claveAdmin.equals(CLAVE_ADMIN_VALIDA)) {
            showAlert(Alert.AlertType.ERROR, "Clave Incorrecta", "La clave de administrador es incorrecta.");
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(contrasena);

        Usuarios usuarioNuevo = new Usuarios(0, usuario, hashedPassword, correo, isAdmin);
        UsuariosDAO usuariosDAO = new UsuariosDAO();

        try {
            usuariosDAO.save(usuarioNuevo);
            showAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", "El usuario se registró correctamente.");
            navigateToLogIn();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error de Registro", "No se pudo registrar el usuario. Inténtalo nuevamente.");
        }
    }

    @FXML
    private void handleVolverButton() {
        navigateToLogIn();
    }

    private void navigateToLogIn() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("logInV.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inicio de Sesión");
            stage.setScene(new Scene(root));
            stage.show();
            closeWindow();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista de inicio de sesión.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) volverButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
