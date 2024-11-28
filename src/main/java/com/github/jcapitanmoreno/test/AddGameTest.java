package com.github.jcapitanmoreno.test;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


public class AddGameTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addGame.fxml"));

        // Crear la escena a partir del archivo FXML
        Scene scene = new Scene(loader.load());

        // Configurar el escenario
        stage.setTitle("Añadir Videojuego");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
