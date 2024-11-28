package com.github.jcapitanmoreno.test;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;

import java.sql.*;

public class TestSQLQuery {

    private final static String GET_DATA = "SELECT v.nombre AS titulo," +
            "g.nombre AS genero, " +
            "p.nombre AS plataforma, " +
            "d.fechaLanzamiento AS fecha, " +
            "u.usuario AS usuario " +
            "FROM videojuegos v " +
            "JOIN genero g ON v.idGenero = g.id " +
            "JOIN usuarios u ON v.idUsuarios = u.id " +
            "JOIN disponible d ON v.id = d.idVideojuego " +
            "JOIN plataformas p ON d.idPlataforma = p.id";

    public static void main(String[] args) {
        try {
            // Establecer la conexión a la base de datos
            Connection conn = ConnectionXamp.getConnection();

            // Ejecutar la consulta SQL
            PreparedStatement statement = conn.prepareStatement(GET_DATA);
            ResultSet resultSet = statement.executeQuery();

            // Imprimir los resultados
            System.out.println("Resultados de la consulta:");
            if (!resultSet.next()) {
                System.out.println("No se encontraron resultados.");
            } else {
                do {
                    // Obtener los valores de cada columna
                    String titulo = resultSet.getString("titulo");
                    String genero = resultSet.getString("genero");
                    String plataforma = resultSet.getString("plataforma");
                    String fecha = resultSet.getString("fecha");
                    String usuario = resultSet.getString("usuario");

                    // Imprimir los resultados
                    System.out.println("Titulo: " + titulo);
                    System.out.println("Genero: " + genero);
                    System.out.println("Plataforma: " + plataforma);
                    System.out.println("Fecha: " + fecha);
                    System.out.println("Usuario: " + usuario);
                    System.out.println("-----------");
                } while (resultSet.next());
            }

            // Cerrar la conexión
            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            // Manejo de errores
            e.printStackTrace();
        }
    }
}
