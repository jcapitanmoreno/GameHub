package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.DisponibleDAO;
import com.github.jcapitanmoreno.model.dao.PlataformaDAO;
import com.github.jcapitanmoreno.model.dao.VideojuegosDAO;
import com.github.jcapitanmoreno.model.entity.Disponible;
import com.github.jcapitanmoreno.model.entity.Plataformas;
import com.github.jcapitanmoreno.model.entity.Videojuegos;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddPlataformToGameController extends Controller implements Initializable{

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
    private Button btnAgregar;

    @FXML
    private ComboBox cbJuego;

    @FXML
    private ComboBox cbPlataforma;

    @FXML
    private TextField txtfecha;

    List<Videojuegos> videojuegosList;
    List<Plataformas> plataformasList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        try {

            videojuegosDAO = new VideojuegosDAO();
            videojuegosList = videojuegosDAO.getAllData();
            List<String> videonames = new ArrayList<>();
            for (Videojuegos v : videojuegosList){
                videonames.add(v.getNombre());
            }
            cbJuego.setItems(FXCollections.observableList(videonames));

            PlataformaDAO plataformasDAO = new PlataformaDAO();
            plataformasList = plataformasDAO.findAll();
            List<String> plataformasname = new ArrayList<>();
            for (Plataformas p : plataformasList){
                plataformasname.add(p.getNombre());
            }
            cbPlataforma.setItems(FXCollections.observableArrayList(plataformasname));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los datos.");
        }
    }

    /**
     * Handles the addition of a new platform to a game.
     * Validates inputs, creates a new Disponible entity, and saves it to the database.
     */
    @FXML
    private void handleAddPlatform() {
        try {
            String videoname = cbJuego.getSelectionModel().getSelectedItem().toString();
            Videojuegos selectedGame = new Videojuegos();
            for (Videojuegos v : videojuegosList){
                if (v.getNombre().equals(videoname)){
                    selectedGame = v;
                }
            }

            String plataformname = cbPlataforma.getSelectionModel().getSelectedItem().toString();
            Plataformas selectedPlatform = new Plataformas();
            for (Plataformas p : plataformasList){
                if (p.getNombre().equals(plataformname)){
                    selectedPlatform = p;
                }
            }

            String releaseDate = txtfecha.getText();


            if (selectedGame == null || selectedPlatform == null || releaseDate.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor, completa todos los campos.");
                return;
            }


            Disponible disponible = new Disponible(selectedGame, List.of(selectedPlatform), releaseDate);


            DisponibleDAO disponibleDAO = new DisponibleDAO();
            disponibleDAO.saveNew(disponible);


            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Plataforma añadida exitosamente.");

            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Hubo un problema al añadir la plataforma.");
        }
    }

    /**
     * Refreshes the video games table by reloading data from the database.
     */
    private void refreshTable() {
        try {
            VideojuegosDAO videojuegosDAO = new VideojuegosDAO();
            List<Videojuegos> updatedVideojuegos = videojuegosDAO.getAllData();
            ObservableList<Videojuegos> observableVideojuegos = FXCollections.observableArrayList(updatedVideojuegos);
            videojuegosTable.setItems(observableVideojuegos);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la tabla.");
        }
    }

    /**
     * Opens a modal to display details of the selected video game.
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
     * Displays an alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Navigates back to the admin's home screen.
     */
    @FXML
    private void navigateToInicioADM(){
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
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
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
