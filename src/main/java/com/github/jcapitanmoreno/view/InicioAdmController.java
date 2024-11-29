package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.VideojuegosDAO;
import com.github.jcapitanmoreno.model.entity.Videojuegos;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class InicioAdmController extends Controller implements Initializable {

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
    private Button btnAddGenero;

    @FXML
    private Button btnAddPlataforma;

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
            String name = videojuegos.getDisponible().getPlataforma().getNombre();
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

    }

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
    @FXML
    private void navigateToAddGenero(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addGenero.fxml"));
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

    @FXML
    private void navigateToAddPlataforma(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addPlataforma.fxml"));
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

    private void closeWindow() {
        Stage stage = (Stage) btnAnadir.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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
