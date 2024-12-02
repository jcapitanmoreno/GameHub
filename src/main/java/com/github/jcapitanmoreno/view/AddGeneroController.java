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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class responsible for managing the "Add Genre" functionality.
 * This includes adding, editing, and deleting genres, as well as navigating between views.
 */
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


    /**
     * Initializes the controller, setting up the genre list and table view.
     * This method is automatically called when the FXML is loaded.
     */
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


        colGenero.setCellFactory(TextFieldTableCell.forTableColumn());
        colGenero.setOnEditCommit(event -> {

            Genero generoEditado = event.getRowValue();
            generoEditado.setNombre(event.getNewValue());

            try {
                generoDAO.save(generoEditado);
            } catch (SQLException e) {
                e.printStackTrace();
                errorAlert("Error", "No se pudo actualizar el género", e.getMessage());
            }
        });

        tblGenero.setItems(generoList);

        try {
            loadGeneros();
        } catch (SQLException e) {
            errorAlert("Error", "No se pudieron cargar los géneros", e.getMessage());
        }
    }


    /**
     * Loads all genres from the database and populates the genre table.
     * @throws SQLException If there is an error querying the database.
     */
    private void loadGeneros() throws SQLException {
        List<Genero> generos = generoDAO.findAll();
        generoList.setAll(generos);
    }


    /**
     * Handles the event when the "Add Genre" button is clicked.
     * It checks if the genre name is valid and adds it to the database.
     * @throws SQLException If there is an error saving the genre.
     */
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

    /**
     * Handles the event when the "Delete Genre" button is clicked.
     * It deletes the selected genre from the database.
     * @throws SQLException If there is an error deleting the genre.
     */
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


    /**
     * Navigates to the admin homepage.
     * It loads the admin home page and closes the current window.
     */
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
    /**
     * Shows an alert with the specified type, title, and content.
     * @param alertType The type of the alert (e.g., ERROR, WARNING).
     * @param title The title of the alert.
     * @param content The content message of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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
