package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.entity.Videojuegos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for displaying the details of a selected video game.
 * This controller is responsible for populating the details of a video game
 * in the UI when a video game is selected.
 */
public class VideojuegoDetailsController extends Controller implements Initializable {

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblGenero;

    @FXML
    private Label lblPlataformas;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblUsuario;

    @FXML
    private Label lblDescripcion;

    @FXML
    private Hyperlink hlEnlace;


    /**
     * Sets the details of the selected video game on the UI elements.
     * It populates the labels with information about the video game.
     * @param videojuego The video game whose details are to be displayed
     */
    @FXML
    public void setVideojuego(Videojuegos videojuego) {
        if (videojuego == null) {
            System.err.println("El videojuego es nulo.");
            return;
        }

        lblTitulo.setText(videojuego.getNombre());
        lblGenero.setText(videojuego.getGenero().getNombre());
        lblPlataformas.setText(videojuego.getDisponible().getPlataforma().stream()
                .map(plataforma -> plataforma.getNombre())
                .reduce((p1, p2) -> p1 + ", " + p2)
                .orElse("No disponible"));
        lblFecha.setText(videojuego.getDisponible().getFechaLanzamiento());
        lblUsuario.setText(videojuego.getUsuario().getUsuario());
        lblDescripcion.setText(
                videojuego.getDescripcion() != null && !videojuego.getDescripcion().isEmpty()
                        ? videojuego.getDescripcion()
                        : "Sin descripción disponible."
        );

        String enlaceTrailer = videojuego.getEnlaceTrailer();
        hlEnlace.setText(enlaceTrailer != null && !enlaceTrailer.isEmpty()
                ? enlaceTrailer
                : "sin enlace disponible");
        hlEnlace.setOnAction(event -> openLink(enlaceTrailer));

    }

    /**
     * Abre el enlace en el navegador predeterminado.
     * @param url El enlace a abrir.
     */
    private void openLink(String url) {
        if (url != null && !url.isEmpty()) {
            try {
                URI uri = new URI(url);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
