package com.github.jcapitanmoreno.model.dao;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;
import com.github.jcapitanmoreno.model.entity.Disponible;
import com.github.jcapitanmoreno.model.entity.Plataformas;
import com.github.jcapitanmoreno.model.entity.Videojuegos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisponibleDAO {
    private final static String INSERT = "INSERT INTO disponible (idVideojuego, idPlataforma, fechaLanzamiento) VALUES (?, ?, ?)";
    private final static String DELETE = "DELETE FROM disponible WHERE idVideojuego = ? AND idPlataforma = ?";
    private final static String SELECT_ALL = "SELECT d.fechaLanzamiento, " +
            "v.id AS videojuego_id, v.nombre AS videojuego_nombre, v.descripcion AS videojuego_descripcion, v.enlaceTrailer, " +
            "p.id AS plataforma_id, p.nombre AS plataforma_nombre " +
            "FROM disponible d " +
            "JOIN videojuegos v ON d.idVideojuego = v.id " +
            "JOIN plataformas p ON d.idPlataforma = p.id";
    private final static String SELECT_BY_VIDEOJUEGO = "SELECT d.fechaLanzamiento, " +
            "v.id AS videojuego_id, v.nombre AS videojuego_nombre, v.descripcion AS videojuego_descripcion, v.enlaceTrailer, " +
            "p.id AS plataforma_id, p.nombre AS plataforma_nombre " +
            "FROM disponible d " +
            "JOIN videojuegos v ON d.idVideojuego = v.id " +
            "JOIN plataformas p ON d.idPlataforma = p.id " +
            "WHERE d.idVideojuego = ?";

    public Disponible save(Disponible disponible) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(INSERT)) {
            statement.setInt(1, disponible.getVideojuego().getId());
            //statement.setInt(2, disponible.getPlataforma().getId());
            statement.setString(3, disponible.getFechaLanzamiento());
            statement.executeUpdate();
        }
        return disponible;
    }

    public Disponible delete(Disponible disponible) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(DELETE)) {
            statement.setInt(1, disponible.getVideojuego().getId());
            //statement.setInt(2, disponible.getPlataforma().getId());
            statement.executeUpdate();
        }
        return disponible;
    }

    public List<Disponible> findAll() throws SQLException {
        List<Disponible> disponibles = new ArrayList<>();
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                disponibles.add(mapResultSetToDisponible(resultSet));
            }
        }
        return disponibles;
    }

    public List<Disponible> findByVideojuego(int idVideojuego) throws SQLException {
        List<Disponible> disponibles = new ArrayList<>();
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_BY_VIDEOJUEGO)) {
            statement.setInt(1, idVideojuego);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    disponibles.add(mapResultSetToDisponible(resultSet));
                }
            }
        }
        return disponibles;
    }
    /**private Disponible mapResultSetToDisponible(ResultSet resultSet) throws SQLException {
        Videojuegos videojuego = new Videojuegos(
                resultSet.getInt("videojuego_id"),
                resultSet.getString("videojuego_nombre"),
                resultSet.getString("videojuego_descripcion"),
                resultSet.getString("enlaceTrailer"),
                null, // Genero no se recupera aquí, pero podrías añadirlo si es necesario
                null  // Usuario no se recupera aquí, pero podrías añadirlo si es necesario
        );

        Plataformas plataforma = new Plataformas(
                resultSet.getInt("plataforma_id"),
                resultSet.getString("plataforma_nombre")
        );

        return new Disponible(
                videojuego,
                plataforma,
                resultSet.getString("fechaLanzamiento")
        );
    }*/
    private Disponible mapResultSetToDisponible(ResultSet resultSet) throws SQLException {

        Videojuegos videojuego = new Videojuegos();
        videojuego.setId(resultSet.getInt("videojuego_id"));
        videojuego.setNombre(resultSet.getString("videojuego_nombre"));
        videojuego.setDescripcion(resultSet.getString("videojuego_descripcion"));
        videojuego.setEnlaceTrailer(resultSet.getString("enlaceTrailer"));



        ArrayList<Plataformas> plataformas = new ArrayList<>();
        Plataformas plataforma = new Plataformas();
        plataforma.setId(resultSet.getInt("plataforma_id"));
        plataforma.setNombre(resultSet.getString("plataforma_nombre"));
        plataformas.add(plataforma);

        return new Disponible(
                videojuego,
                plataformas,
                resultSet.getString("fechaLanzamiento")
        );
    }

}
