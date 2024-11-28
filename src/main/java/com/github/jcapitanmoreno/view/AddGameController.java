package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.dao.GeneroDAO;
import com.github.jcapitanmoreno.model.dao.PlataformaDAO;
import com.github.jcapitanmoreno.model.entity.Genero;
import com.github.jcapitanmoreno.model.entity.Plataformas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

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

    GeneroDAO generoDAO = new GeneroDAO();
    PlataformaDAO plataformaDAO = new PlataformaDAO();

    @FXML
    public void initialize() {
        cargarGeneros();
        cargarPlataformas();
    }

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

    @FXML
    private void handleAnadirJuego() {
        String nombre = nombreJuegoField.getText();
        String descripcion = descripcionArea.getText();
        String genero = generoComboBox.getValue();
        String plataforma = plataformaComboBox.getValue();

        if (nombre.isEmpty() || descripcion.isEmpty() || genero == null || plataforma == null) {
            System.out.println("Por favor, completa todos los campos.");
            return;
        }

        // aqui tendria que añadir la logica de guardar el videojuego completo (insert en videojuego que guarde esto)
    }
}
