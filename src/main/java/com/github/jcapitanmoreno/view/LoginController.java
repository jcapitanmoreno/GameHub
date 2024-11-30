package com.github.jcapitanmoreno.view;


import com.github.jcapitanmoreno.model.dao.UsuariosDAO;
import com.github.jcapitanmoreno.model.entity.Usuarios;
import com.github.jcapitanmoreno.model.singleton.UsuarioSingleton;
import com.github.jcapitanmoreno.utils.PasswordUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnRegistrarse;

    UsuariosDAO usuariosDAO = new UsuariosDAO();
    UsuarioSingleton usuarioSingleton = new UsuarioSingleton();


    @FXML
    private void handleLogin() {
        String username = txtUsuario.getText();
        String password = txtContrasena.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Por favor, complete todos los campos.");
            return;
        }

        try {
            // Buscar usuario por nombre de usuario
            Usuarios usuario = usuariosDAO.findAll().stream()
                    .filter(u -> u.getUsuario().equals(username))
                    .findFirst()
                    .orElse(null);

            if (usuario != null) {

                String hashedPassword = PasswordUtil.hashPassword(password);


                if (usuario.getContrasena().equals(hashedPassword)) {
                    UsuarioSingleton.get_Instance().login(usuario);
                    showAlert(Alert.AlertType.INFORMATION, "Inicio de Sesión", "Inicio de sesión exitoso.");

                    if (usuarioSingleton.getPlayerLoged().isAdmin()) {
                        navigateToInicioAdm();
                    } else {
                        navigateToInicio();
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Usuario o contraseña incorrectos.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Usuario o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Hubo un problema al procesar la solicitud.");
        }
    }

    @FXML
    private void navigateToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("singInV.fxml"));
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
    private void navigateToInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioV.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inicio");
            stage.setScene(new Scene(root));
            stage.show();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista");
        }
    }

    private void navigateToInicioAdm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioADMV.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inicio Administrador");
            stage.setScene(new Scene(root));
            stage.show();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista");
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
