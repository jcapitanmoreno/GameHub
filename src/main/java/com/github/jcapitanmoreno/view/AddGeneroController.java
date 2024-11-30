package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.GeneroDAO;
import com.github.jcapitanmoreno.model.entity.Genero;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddGeneroController extends Controller implements Initializable {
    @FXML
    private TextField txtGenero;

    @FXML
    private TableView<Genero> tblGenero;

    @FXML
    private TableColumn<Genero, String> colGenero;

    @FXML
    private TableColumn<Genero, Integer> colGeneroId;

    @FXML
    private Button btnAdd;


    @FXML
    private Button btnVolver;

    @FXML
    private ImageView imgDeleteGenero;

    private ObservableList<Genero> generoList;

    GeneroDAO generoDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generoDAO = new GeneroDAO();
        generoList = FXCollections.observableArrayList();
        tblGenero.setEditable(true);


        colGeneroId.setCellValueFactory(cellData -> {
            Genero genero = cellData.getValue();
            int id = genero.getId();
            return new SimpleObjectProperty<>(id);
        });

        colGenero.setCellValueFactory(cellData -> {
            Genero genero = cellData.getValue();
            String name = genero.getNombre();
            return new SimpleObjectProperty<>(name);
        });


        tblGenero.setItems(generoList);


        try {
            loadGeneros();
        } catch (SQLException e) {
            errorAlert("Error", "No se pudieron cargar los géneros", e.getMessage());
        }
    }

    private void loadGeneros() throws SQLException {
        List<Genero> generos = generoDAO.findAll();
        generoList.setAll(generos);
    }

    @FXML
    private void addGenero() throws SQLException {
        String nombreGenero = txtGenero.getText();

        if (nombreGenero == null || nombreGenero.trim().isEmpty()) {
            warningAlert("Advertencia", "Campo vacío", "Por favor, introduce un nombre para el género.");
            return;
        }
        Genero nuevoGenero = new Genero(0, nombreGenero);
        generoDAO.save(nuevoGenero);
        loadGeneros();
        txtGenero.clear();
    }

    @FXML
    private void deleteGenero() throws SQLException {
        Genero selectedGenero = tblGenero.getSelectionModel().getSelectedItem();

        if (selectedGenero == null) {
            warningAlert("Advertencia", "No seleccionado", "Por favor, selecciona un género para eliminar.");
            return;
        }
        generoDAO.delete(selectedGenero);
        loadGeneros();
    }

    @FXML
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
