package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.UsuariosDAO;
import com.github.jcapitanmoreno.model.entity.Usuarios;
import com.github.jcapitanmoreno.utils.PasswordUtil;
import com.github.jcapitanmoreno.utils.Validaciones;
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

    /**
     * Initializes the SignIn view.
     * Disables the admin password field unless the user selects the 'isAdmin' checkbox.
     */
    @FXML
    public void initialize() {
        claveAdminField.setDisable(true);


        isAdminCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            claveAdminField.setDisable(!newValue);
        });
    }

    /**
     * Handles the registration process when the user clicks the 'Acceder' button.
     * It validates the input fields, hashes the password, and saves the user to the database.
     */
    @FXML
    private void handleAccederButton() {
        String usuario = usuarioField.getText().trim();
        String correo = correoField.getText().trim();
        String contrasena = contrasenaField.getText();
        boolean isAdmin = isAdminCheckBox.isSelected();
        String claveAdmin = claveAdminField.getText();

        try {
            // Llamada a las validaciones desde la clase Validaciones
            Validaciones.validarCampos(usuario, correo, contrasena, isAdmin, claveAdmin);

            String hashedPassword = hashPassword(contrasena);
            guardarUsuario(usuario, correo, hashedPassword, isAdmin);
            navigateToLogIn();
        } catch (Validaciones.ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validación Fallida", e.getMessage());
        } catch (SQLException e) {
            manejarExcepcionSQL(e);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error Interno", "Ocurrió un error inesperado.");
            e.printStackTrace();
        }
    }

    /**
     * Hashes the user's password using a utility method.
     * @param contrasena The plain text password
     * @return The hashed password
     * @throws Exception If an error occurs during the password hashing
     */
    private String hashPassword(String contrasena) throws Exception {
        return PasswordUtil.hashPassword(contrasena);
    }

    /**
     * Saves the new user to the database.
     * @param usuario The username
     * @param correo The email
     * @param hashedPassword The hashed password
     * @param isAdmin Whether the user is an admin
     * @throws SQLException If there is a database error
     */
    private void guardarUsuario(String usuario, String correo, String hashedPassword, boolean isAdmin) throws SQLException {
        Usuarios usuarioNuevo = new Usuarios(0, usuario, hashedPassword, correo, isAdmin);
        UsuariosDAO usuariosDAO = new UsuariosDAO();
        usuariosDAO.save(usuarioNuevo);
        showAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", "El usuario se registró correctamente.");
    }

    /**
     * Handles SQL exceptions, especially duplicate entry errors.
     * @param e The SQL exception
     */
    private void manejarExcepcionSQL(SQLException e) {
        if (e.getMessage().contains("Duplicate entry")) {
            showAlert(Alert.AlertType.ERROR, "Error de Registro", "El correo o usuario ya está en uso.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error de Registro", "No se pudo registrar el usuario. Inténtalo nuevamente.");
        }
        e.printStackTrace();
    }

    /**
     * Custom exception class for validation errors.
     */
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    /**
     * Handles the 'Volver' button click, navigating to the login screen.
     */
    @FXML
    private void handleVolverButton() {
        navigateToLogIn();
    }

    /**
     * Navigates to the login screen.
     */
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

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) volverButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert with a specific message.
     * @param type The type of alert (e.g., warning, error)
     * @param title The title of the alert
     * @param message The content of the alert
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
