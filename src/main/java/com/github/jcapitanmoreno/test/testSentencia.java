package com.github.jcapitanmoreno.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class testSentencia {

    private static final String URL = "jdbc:mariadb://localhost:3306/gamehub";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        //esta sentencia me servira para la pantalla principal con algunos retoques (¡¡¡¡¡¡¡¡¡IMPORTANTE!!!!!!!!!)
        String query =  "SELECT " +
                "    videojuegos.nombre AS nombre_videojuego, " +
                "    genero.nombre AS nombre_genero, " +
                "    plataformas.nombre AS nombre_plataforma, " +
                "    disponible.fechaLanzamiento AS fecha_lanzamiento, " +
                "    usuarios.usuario AS nombre_usuario " +
                "FROM " +
                "    videojuegos " +
                "INNER JOIN genero ON videojuegos.idGenero = genero.id " +
                "INNER JOIN disponible ON videojuegos.id = disponible.idVideojuego " +
                "INNER JOIN plataformas ON disponible.idPlataforma = plataformas.id " +
                "INNER JOIN usuarios ON videojuegos.idUsuarios = usuarios.id;";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Nombre Videojuego | Género | Plataforma | Fecha Lanzamiento | Usuario");
            System.out.println("-----------------------------------------------------------------------");

            while (resultSet.next()) {
                String nombreVideojuego = resultSet.getString("nombre_videojuego");
                String nombreGenero = resultSet.getString("nombre_genero");
                String nombrePlataforma = resultSet.getString("nombre_plataforma");
                String fechaLanzamiento = resultSet.getString("fecha_lanzamiento");
                String nombreUsuario = resultSet.getString("nombre_usuario");

                System.out.printf("%-20s | %-10s | %-15s | %-18s | %-10s%n",
                        nombreVideojuego, nombreGenero, nombrePlataforma, fechaLanzamiento, nombreUsuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
