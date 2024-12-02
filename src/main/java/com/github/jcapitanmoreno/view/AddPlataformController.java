package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.GeneroDAO;
import com.github.jcapitanmoreno.model.dao.PlataformaDAO;
import com.github.jcapitanmoreno.model.entity.Genero;
import com.github.jcapitanmoreno.model.entity.Plataformas;
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
 * Controller class responsible for managing the "Add Platform" functionality.
 * This includes adding, editing, and deleting platforms, as well as navigating between views.
 */
public class AddPlataformController extends Controller implements Initializable {
    @FXML
    private TextField txtPlataforma;

    @FXML
    private TableView<Plataformas> tblPlataforma;

    @FXML
    private TableColumn<Plataformas, String> colPlataforma;

    @FXML
    private TableColumn<Plataformas, Integer> plataformaId;

    @FXML
    private Button btnAdd;


    @FXML
    private Button btnVolver;

    @FXML
    private ImageView imgDeleteGenero;

    private ObservableList<Plataformas> plataformaList;

    PlataformaDAO plataformaDAO = new PlataformaDAO();

    /**
     * Initializes the controller, setting up the platform list and table view.
     * This method is automatically called when the FXML is loaded.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblPlataforma.setEditable(true);

        plataformaList = FXCollections.observableArrayList();

        plataformaId.setCellValueFactory(cellData -> {
            Plataformas plataformas = cellData.getValue();
            int id = plataformas.getId();
            return new SimpleObjectProperty<>(id);
        });


        colPlataforma.setCellValueFactory(cellData -> {
            Plataformas plataformas = cellData.getValue();
            String name = plataformas.getNombre();
            return new SimpleObjectProperty<>(name);
        });


        colPlataforma.setCellFactory(TextFieldTableCell.forTableColumn());
        colPlataforma.setOnEditCommit(event -> {

            Plataformas plataformaEditada = event.getRowValue();
            plataformaEditada.setNombre(event.getNewValue());


            try {
                plataformaDAO.save(plataformaEditada);
            } catch (SQLException e) {
                e.printStackTrace();
                errorAlert("Error", "No se pudo actualizar la plataforma", e.getMessage());
            }
        });

        tblPlataforma.setItems(plataformaList);

        try {
            loadPlataformas();
        } catch (SQLException e) {
            errorAlert("Error", "No se pudieron cargar las plataformas", e.getMessage());
        }
    }

    /**
     * Loads all platforms from the database and populates the platform table.
     * @throws SQLException If there is an error querying the database.
     */
    private void loadPlataformas() throws SQLException {
        List<Plataformas> generos = plataformaDAO.findAll();
        plataformaList.setAll(generos);
    }

    /**
     * Handles the event when the "Add Platform" button is clicked.
     * It checks if the platform name is valid and adds it to the database.
     * @throws SQLException If there is an error saving the platform.
     */
    @FXML
    private void addPlataforma() throws SQLException {
        String nombrePlataforma = txtPlataforma.getText();

        if (nombrePlataforma == null || nombrePlataforma.trim().isEmpty()) {
            warningAlert("Advertencia", "Campo vacío", "Por favor, introduce un nombre para el género.");
            return;
        }
        Plataformas nuevaPlataforma = new Plataformas(0, nombrePlataforma);
        PlataformaDAO.save(nuevaPlataforma);
        loadPlataformas();
        txtPlataforma.clear();
    }

    /**
     * Handles the event when the "Delete Platform" button is clicked.
     * It deletes the selected platform from the database.
     * @throws SQLException If there is an error deleting the platform.
     */
    @FXML
    private void deletePlataformas() throws SQLException {
        Plataformas selectedPlataforma = tblPlataforma.getSelectionModel().getSelectedItem();

        if (selectedPlataforma == null) {
            warningAlert("Advertencia", "No seleccionado", "Por favor, selecciona un género para eliminar.");
            return;
        }

        PlataformaDAO.delete(selectedPlataforma);


        loadPlataformas();
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
