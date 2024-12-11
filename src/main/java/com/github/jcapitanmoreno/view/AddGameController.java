package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.GeneroDAO;
import com.github.jcapitanmoreno.model.dao.PlataformaDAO;
import com.github.jcapitanmoreno.model.dao.VideojuegosDAO;
import com.github.jcapitanmoreno.model.entity.Genero;
import com.github.jcapitanmoreno.model.entity.Plataformas;
import com.github.jcapitanmoreno.model.entity.Usuarios;
import com.github.jcapitanmoreno.model.singleton.UsuarioSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class responsible for managing the "Add Game" UI and handling user interactions.
 * It includes functionality to load available game genres, platforms, and to save a new game to the database.
 */
public class AddGameController {

    @FXML
    private TextField nombreJuegoField;

    @FXML
    private TextArea descripcionArea;

    @FXML
    private ComboBox<String> generoComboBox;

    @FXML
    private ComboBox<String> plataformaComboBox;

    @FXML
    private Button btnAnadir;
    @FXML
    private TextField fecha;
    @FXML
    private TextField trailer;
    @FXML
    private Button btnAtras;
    GeneroDAO generoDAO = new GeneroDAO();
    PlataformaDAO plataformaDAO = new PlataformaDAO();

    /**
     * Initializes the controller by loading the available genres and platforms.
     */
    @FXML
    public void initialize() {
        cargarGeneros();
        cargarPlataformas();
    }

    /**
     * Loads all available genres from the database and populates the genre combo box.
     */
    private void cargarGeneros() {
        try {
            List<Genero> generos = generoDAO.findAll();
            ObservableList<String> generosNombres = FXCollections.observableArrayList();
            for (Genero genero : generos) {
                generosNombres.add(genero.getNombre());
            }
            generoComboBox.setItems(generosNombres);
        } catch (SQLException e) {
            System.err.println("Error al cargar géneros: " + e.getMessage());
        }
    }

    /**
     * Loads all available platforms from the database and populates the platform combo box.
     */
    private void cargarPlataformas() {
        try {
            List<Plataformas> plataformas = plataformaDAO.findAll();
            ObservableList<String> plataformasNombres = FXCollections.observableArrayList();
            for (Plataformas plataforma : plataformas) {
                plataformasNombres.add(plataforma.getNombre());
            }
            plataformaComboBox.setItems(plataformasNombres);
        } catch (SQLException e) {
            System.err.println("Error al cargar plataformas: " + e.getMessage());
        }
    }

    /**
     * Handles the event when the "Add Game" button is clicked. Validates the form fields and
     * saves the game to the database if the data is valid.
     */
   @FXML
    private void handleAnadirJuego() {
        String nombre = nombreJuegoField.getText();
        String descripcion = descripcionArea.getText();
        String generoNombre = generoComboBox.getValue();
        String plataformaNombre = plataformaComboBox.getValue();
        String fechaLanzamiento = fecha.getText();
        String enlaceTrailer = trailer.getText();

        if (nombre.isEmpty() || descripcion.isEmpty() || generoNombre == null || plataformaNombre == null || fechaLanzamiento.isEmpty()) {
            System.out.println("Por favor, completa todos los campos.");
            return;
        }

        Usuarios usuarioLogueado = UsuarioSingleton.get_Instance().getPlayerLoged();
        if (usuarioLogueado == null) {
            System.out.println("Error: No hay un usuario logueado.");
            return;
        }
        int idUsuario = usuarioLogueado.getId();

        try {
            if (!fechaLanzamiento.matches("\\d{2}-\\d{2}-\\d{4}")) {
                System.out.println("Por favor, ingresa una fecha válida en el formato DD-MM-AAAA.");
                return;
            }

            int idGenero = generoDAO.findByName(generoNombre).getId();
            int idPlataforma = plataformaDAO.findByName(plataformaNombre).getId();

            VideojuegosDAO videojuegosDAO = new VideojuegosDAO();
            videojuegosDAO.saveFull(nombre, descripcion, enlaceTrailer, idGenero, idUsuario, idPlataforma, fechaLanzamiento);
            showAlert(Alert.AlertType.INFORMATION, "Completado", "juego añadido exitosamente");

            limpiarCampos();

        } catch (SQLException e) {
            System.err.println("Error al añadir el videojuego: " + e.getMessage());
        }
    }

    /**
     * Clears all the fields in the form after successfully adding the game.
     */
    private void limpiarCampos() {
        nombreJuegoField.clear();
        descripcionArea.clear();
        generoComboBox.getSelectionModel().clearSelection();
        plataformaComboBox.getSelectionModel().clearSelection();
        fecha.clear();
        trailer.clear();
    }

    /**
     * Handles conditional navigation based on the logged-in user's role (admin or regular user).
     */
    @FXML
    private void navegacionCondicional(){
        UsuarioSingleton usuarioSingleton = new UsuarioSingleton();
        if (usuarioSingleton.getPlayerLoged().isAdmin()){
            navigateToInicioAdm();
        }else {
            navigateToInicio();
        }
    }

    /**
     * Navigates to the main page for regular users.
     */
    private void navigateToInicio(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inicioV.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("LogIn");
            stage.setScene(new Scene(root));
            stage.show();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista");
        }
    }
    /**
     * Navigates to the admin's main page.
     */
    private void navigateToInicioAdm(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inicioADMV.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("LogIn");
            stage.setScene(new Scene(root));
            stage.show();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista");
        }
    }
    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) btnAnadir.getScene().getWindow();
        stage.close();
    }

    /**
     * Shows an alert with the specified type, title, and content.
     *
     * @param alertType The type of the alert (e.g., ERROR, INFORMATION).
     * @param title The title of the alert.
     * @param content The content message of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}