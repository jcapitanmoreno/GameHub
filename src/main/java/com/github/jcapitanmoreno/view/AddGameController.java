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
    @FXML
    private TextField fecha;
    @FXML
    private TextField trailer;

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
        // Recuperar los datos ingresados en los campos
        String nombre = nombreJuegoField.getText();
        String descripcion = descripcionArea.getText();
        String generoNombre = generoComboBox.getValue();
        String plataformaNombre = plataformaComboBox.getValue();
        String fechaLanzamiento = fecha.getText();
        String enlaceTrailer = trailer.getText();

        // Validar los datos ingresados
        if (nombre.isEmpty() || descripcion.isEmpty() || generoNombre == null || plataformaNombre == null || fechaLanzamiento.isEmpty()) {
            System.out.println("Por favor, completa todos los campos.");
            return;
        }

        // Obtener el usuario logueado desde el Singleton
        Usuarios usuarioLogueado = UsuarioSingleton.get_Instance().getPlayerLoged();
        if (usuarioLogueado == null) {
            System.out.println("Error: No hay un usuario logueado.");
            return;
        }
        int idUsuario = usuarioLogueado.getId(); // Obtener el ID del usuario logueado

        try {
            // Validar el formato de la fecha (opcional, si se espera un formato específico como "YYYY-MM-DD")
            if (!fechaLanzamiento.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Por favor, ingresa una fecha válida en el formato YYYY-MM-DD.");
                return;
            }

            // Buscar el ID del género y la plataforma seleccionados
            int idGenero = generoDAO.findByName(generoNombre).getId();
            int idPlataforma = plataformaDAO.findByName(plataformaNombre).getId();

            // Llamar al método del DAO para guardar el videojuego con sus detalles
            VideojuegosDAO videojuegosDAO = new VideojuegosDAO();
            videojuegosDAO.saveFull(nombre, descripcion, enlaceTrailer, idGenero, idUsuario, idPlataforma, fechaLanzamiento);

            System.out.println("Videojuego añadido exitosamente.");

            // Limpiar los campos después de guardar el videojuego
            limpiarCampos();

        } catch (SQLException e) {
            System.err.println("Error al añadir el videojuego: " + e.getMessage());
        }
    }


    private void limpiarCampos() {
        nombreJuegoField.clear();
        descripcionArea.clear();
        generoComboBox.getSelectionModel().clearSelection();
        plataformaComboBox.getSelectionModel().clearSelection();
        fecha.clear();
        trailer.clear();
    }
}
