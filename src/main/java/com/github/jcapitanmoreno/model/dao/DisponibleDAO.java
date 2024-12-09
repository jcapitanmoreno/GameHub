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


/**
 * Data Access Object (DAO) class for managing the "disponible" table.
 * Provides methods for performing CRUD operations related to the
 * availability of video games on different platforms.
 */
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

    /**
     * Saves a new "disponible" record to the database.
     *
     * @param disponible The {@code Disponible} entity to save.
     * @return The saved {@code Disponible} entity.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Disponible save(Disponible disponible) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(INSERT)) {
            statement.setInt(1, disponible.getVideojuego().getId());
            statement.setString(3, disponible.getFechaLanzamiento());
            statement.executeUpdate();
        }
        return disponible;
    }

    /**
     * Deletes a "disponible" record from the database.
     *
     * @param disponible The {@code Disponible} entity to delete.
     * @return The deleted {@code Disponible} entity.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Disponible delete(Disponible disponible) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(DELETE)) {
            statement.setInt(1, disponible.getVideojuego().getId());
            statement.executeUpdate();
        }
        return disponible;
    }

    /**
     * Saves a new "disponible" record for a specific platform.
     *
     * @param disponible The {@code Disponible} entity to save.
     * @return The saved {@code Disponible} entity.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Disponible saveNew(Disponible disponible) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(INSERT)) {
            statement.setInt(1, disponible.getVideojuego().getId());
            statement.setInt(2, disponible.getPlataforma().get(0).getId());
            statement.setString(3, disponible.getFechaLanzamiento());
            statement.executeUpdate();
        }
        return disponible;
    }

    /**
     * Retrieves all "disponible" records from the database.
     *
     * @return A list of {@code Disponible} entities.
     * @throws SQLException If an error occurs during the database operation.
     */
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

    /**
     * Retrieves "disponible" records filtered by video game ID.
     *
     * @param idVideojuego The ID of the video game to filter by.
     * @return A list of {@code Disponible} entities matching the video game ID.
     * @throws SQLException If an error occurs during the database operation.
     */
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

    /**
     * Maps a {@code ResultSet} row to a {@code Disponible} entity.
     *
     * @param resultSet The {@code ResultSet} containing the database row data.
     * @return A {@code Disponible} entity mapped from the row.
     * @throws SQLException If an error occurs while accessing the {@code ResultSet}.
     */
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
