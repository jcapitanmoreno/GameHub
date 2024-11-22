package com.github.jcapitanmoreno.test;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión...");

        // Obtener la conexión
        try (Connection connection = ConnectionXamp.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Conexión establecida con éxito.");
            } else {
                System.out.println("No se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            System.err.println("Error durante la prueba de conexión:");
            e.printStackTrace();
        } finally {
            // Cerrar la conexión
            ConnectionXamp.closeConnection();
            System.out.println("Conexión cerrada.");
        }
    }
}
