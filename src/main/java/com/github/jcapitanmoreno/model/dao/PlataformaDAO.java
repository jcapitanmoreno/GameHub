package com.github.jcapitanmoreno.model.dao;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;
import com.github.jcapitanmoreno.model.entity.Plataformas;
import org.checkerframework.checker.units.qual.C;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing the "plataformas" table.
 * Provides CRUD operations for the "plataformas" entity (platform).
 */
public class PlataformaDAO {

    private final static String INSERT = "INSERT INTO plataformas (nombre) VALUES (?)";
    private final static String UPDATE = "UPDATE plataformas SET nombre = ? WHERE id = ?";
    private final static String DELETE = "DELETE FROM plataformas WHERE id = ?";
    private final static String SELECT_ALL = "SELECT * FROM plataformas";
    private final static String SELECT_BY_ID = "SELECT * FROM plataformas WHERE id = ?";
    private final static String SELECT_BY_NOMBRE = "SELECT * FROM plataformas WHERE nombre = ?";

    /**
     * Saves a new platform or updates an existing one in the database.
     *
     * @param plataforma The {@code Plataformas} entity to save or update.
     * @return The saved or updated {@code Plataformas} entity.
     * @throws SQLException If an error occurs during the database operation.
     */
    public static Plataformas save(Plataformas plataforma) throws SQLException {
        if (plataforma.getId() == 0) {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, plataforma.getNombre());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        plataforma.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(UPDATE)) {
                statement.setString(1, plataforma.getNombre());
                statement.setInt(2, plataforma.getId());
                statement.executeUpdate();
            }
        }
        return plataforma;
    }

    /**
     * Deletes a platform record from the database.
     *
     * @param plataforma The {@code Plataformas} entity to delete.
     * @return The deleted {@code Plataformas} entity, or {@code null} if the entity was invalid.
     * @throws SQLException If an error occurs during the database operation.
     */
    public static Plataformas delete(Plataformas plataforma) throws SQLException {
        if (plataforma == null || plataforma.getId() == 0) {
            plataforma = null;
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(DELETE)) {
                statement.setInt(1, plataforma.getId());
                statement.executeUpdate();
            }
        }
        return plataforma;
    }

    /**
     * Retrieves a platform by its ID from the database.
     *
     * @param id The ID of the platform to retrieve.
     * @return The {@code Plataformas} entity with the specified ID, or {@code null} if not found.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Plataformas findById(int id) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPlataformas(resultSet);
                }
            }
        }
        return null;
    }


    /**
     * Retrieves all platforms from the database.
     *
     * @return A list of all {@code Plataformas} entities.
     * @throws SQLException If an error occurs during the database operation.
     */
    public List<Plataformas> findAll() throws SQLException {
        List<Plataformas> plataformas = new ArrayList<>();
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                plataformas.add(mapResultSetToPlataformas(resultSet));
            }
        }
        return plataformas;
    }

    /**
     * Retrieves a platform by its name from the database.
     *
     * @param id The name of the platform to retrieve.
     * @return The {@code Plataformas} entity with the specified name, or {@code null} if not found.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Plataformas findByName(String id) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_BY_NOMBRE)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPlataformas(resultSet);
                }
            }
        }
        return null;
    }

    /**
     * Maps a {@code ResultSet} row to a {@code Plataformas} entity.
     *
     * @param resultSet The {@code ResultSet} containing the database row data.
     * @return A {@code Plataformas} entity mapped from the row.
     * @throws SQLException If an error occurs while accessing the {@code ResultSet}.
     */
    private Plataformas mapResultSetToPlataformas(ResultSet resultSet) throws SQLException {
        return new Plataformas(
                resultSet.getInt("id"),
                resultSet.getString("nombre")
        );
    }
}
