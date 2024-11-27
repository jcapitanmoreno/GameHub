package com.github.jcapitanmoreno.model.view;


import com.github.jcapitanmoreno.model.connection.ConnectionXamp;
import com.github.jcapitanmoreno.model.dao.UsuariosDAO;
import com.github.jcapitanmoreno.model.entity.Usuarios;
import com.github.jcapitanmoreno.model.singleton.UsuarioSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnRegistrarse;

    private final UsuariosDAO usuariosDAO = new UsuariosDAO();

    @FXML
    public void initialize() {
        btnIniciarSesion.setOnAction(event -> handleLogin());
        btnRegistrarse.setOnAction(event -> navigateToSignUp());
    }

    private void handleLogin() {
        String username = txtUsuario.getText();
        String password = txtContrasena.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Por favor, complete todos los campos.");
            return;
        }

        try {
            Usuarios usuario = usuariosDAO.findAll().stream()
                    .filter(u -> u.getUsuario().equals(username) && u.getContrasena().equals(password))
                    .findFirst()
                    .orElse(null);

            if (usuario != null) {
                UsuarioSingleton.get_Instance().login(usuario);
                showAlert(Alert.AlertType.INFORMATION, "Inicio de Sesión", "Inicio de sesión exitoso.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Usuario o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Hubo un problema al procesar la solicitud.");
        }
    }

    private void navigateToSignUp() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/jcapitanmoreno/view/signInV.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Registro");
            stage.setScene(new Scene(root));
            stage.show();


            closeWindow();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista de registro.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
        stage.close();
    }





}
