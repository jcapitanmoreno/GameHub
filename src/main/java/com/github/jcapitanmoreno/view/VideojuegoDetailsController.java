package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.model.entity.Videojuegos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
