package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.VideojuegosDAO;
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
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PerfilUsuarioController extends Controller implements Initializable {
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
    private Button btnVolver;

    @FXML
    private Label lblUsuario;

    @FXML
    private Label lblCorreo;

    @FXML
    private ImageView imgDeleteVideojuego;

    /**
     * Initialization of the view. Loads the user data and the associated games.
     * @param url URL of the FXML view
     * @param resourceBundle Resources for internationalization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Usuarios usuarioLogueado = UsuarioSingleton.get_Instance().getPlayerLoged();

        if (usuarioLogueado != null) {

            lblUsuario.setText(UsuarioSingleton.get_Instance().getPlayerLoged().getUsuario());
            lblCorreo.setText(UsuarioSingleton.get_Instance().getPlayerLoged().getCorreo());
        }


        VideojuegosDAO videojuegosDAO = new VideojuegosDAO();
        List<Videojuegos> inicioVideojuegos;
        try {
            inicioVideojuegos = videojuegosDAO.getDataByUser();
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
     * Method to navigate to the login view.
     */
    @FXML
    private void navigateToLogIn(){
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
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }
    /**
     * Displays an alert with a message.
     * @param alertType Type of alert (information, error, etc.)
     * @param title Title of the alert
     * @param content Content of the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Opens a modal with the details of the selected game.
     * @param videojuego The game whose details will be shown
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

    /**
     * Deletes the selected game from the table and the database.
     */
    @FXML
    private void deleteSelectedVideojuego() {

        Videojuegos selectedVideojuego = videojuegosTable.getSelectionModel().getSelectedItem();
        if (selectedVideojuego != null) {
            try {

                VideojuegosDAO videojuegosDAO = new VideojuegosDAO();
                videojuegosDAO.delete(selectedVideojuego);

                videojuegosTable.getItems().remove(selectedVideojuego);
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "El videojuego se eliminó correctamente.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el videojuego.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Seleccione un videojuego para eliminar.");
        }
    }




    @Override
    public void onOpen(Object input) throws IOException {

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
