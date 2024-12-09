package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.VideojuegosDAO;
import com.github.jcapitanmoreno.model.entity.Disponible;
import com.github.jcapitanmoreno.model.entity.Plataformas;
import com.github.jcapitanmoreno.model.entity.Usuarios;
import com.github.jcapitanmoreno.model.entity.Videojuegos;
import com.github.jcapitanmoreno.model.singleton.UsuarioSingleton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for the main screen (InicioController), handling the interaction with video games list
 * and navigation to other views.
 */
public class InicioController extends Controller implements Initializable {

    @FXML
    private TableView<Videojuegos> videojuegosTable;

    @FXML
    private TableColumn<Videojuegos, String> tituloColumn;

    @FXML
    private TableColumn<Videojuegos, String> generoColumn;

    @FXML
    private TableColumn<Videojuegos, String> plataformaColumn;

    @FXML
    private TableColumn<Videojuegos, String> fechaColumn;

    @FXML
    private TableColumn<Videojuegos, String> usuarioColumn;

    @FXML
    private Button btnAnadir;

    @FXML
    private Button btnVolver;

    @FXML
    private ImageView userImageView;

    @FXML
    private ImageView helpImageView;

    /**
     * Initializes the controller by loading the video games list from the database,
     * binding the data to the table, and setting up the table columns.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VideojuegosDAO videojuegosDAO = new VideojuegosDAO();
        List<Videojuegos> inicioVideojuegos;
        try {
            inicioVideojuegos = videojuegosDAO.getAllData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Videojuegos> observableVideojuegos = FXCollections.observableArrayList(inicioVideojuegos);


        videojuegosTable.setItems(observableVideojuegos);

        tituloColumn.setCellValueFactory(cellData -> {
            Videojuegos videojuegos = cellData.getValue();
            String name = videojuegos.getNombre();
            return new SimpleStringProperty(name);


        });
        generoColumn.setCellValueFactory(cellData -> {
            Videojuegos videojuegos = cellData.getValue();
            String name = videojuegos.getGenero().getNombre();
            return new SimpleStringProperty(name);


        });
        plataformaColumn.setCellValueFactory(cellData -> {
            Videojuegos videojuegos = cellData.getValue();
            String name = videojuegos.getDisponible().getPlataforma()
                    .stream()
                    .map(Plataformas::getNombre)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(name);


        });

        fechaColumn.setCellValueFactory(cellData -> {
            Videojuegos videojuegos = cellData.getValue();
            String name = videojuegos.getDisponible().getFechaLanzamiento();
            return new SimpleStringProperty(name);


        });
        usuarioColumn.setCellValueFactory(cellData -> {
            Videojuegos videojuegos = cellData.getValue();
            String name = videojuegos.getUsuario().getUsuario();
            return new SimpleStringProperty(name);


        });

        videojuegosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Videojuegos selectedVideojuego = videojuegosTable.getSelectionModel().getSelectedItem();
                if (selectedVideojuego != null) {
                    openDetailsModal(selectedVideojuego);
                }
            }
        });
    }

    /**
     * Navigates to the "Add Game" screen where a new video game can be added.
     */
    @FXML
    private void navigateToAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addGame.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("añadir videojuego");
            stage.setScene(new Scene(root));
            stage.show();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista de añadir videojuego.");
        }
    }

    /**
     * Navigates to the login screen.
     */
    @FXML
    private void navigateToLogIn(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logInV.fxml"));
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
     * Navigates to the user profile screen.
     */
    @FXML
    private void navigateToUser(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("perfilUsuario.fxml"));
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
     * Navigates to the user profile screen.
     */
    @FXML
    private void navigateToHelp(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpV.fxml"));
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
     * Closes the current window (stage).
     */
    private void closeWindow() {
        Stage stage = (Stage) btnAnadir.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert with the given parameters (type, title, and content).
     *
     * @param alertType the type of the alert (ERROR, INFORMATION, etc.)
     * @param title the title of the alert
     * @param content the content message of the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Opens the modal window to display the details of a selected video game.
     *
     * @param videojuego the video game object whose details will be displayed
     */
    private void openDetailsModal(Videojuegos videojuego) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("videojuegoDetails.fxml"));
            Parent root = loader.load();


            VideojuegoDetailsController controller = loader.getController();
            controller.setVideojuego(videojuego);


            Stage stage = new Stage();
            stage.setTitle("Detalles del Videojuego");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista de detalles.");
        }
    }



    @Override
    public void onOpen(Object input) {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void informationAlert(String text1, String text2, String text3) {

    }

    @Override
    public void errorAlert(String text1, String text2, String text3) {

    }

    @Override
    public void warningAlert(String text1, String text2, String text3) {

    }

}
