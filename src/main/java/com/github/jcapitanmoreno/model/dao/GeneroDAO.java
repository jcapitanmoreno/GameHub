package com.github.jcapitanmoreno.model.dao;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;
import com.github.jcapitanmoreno.model.entity.Genero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Data Access Object (DAO) for managing the "genero" table.
 * Provides CRUD operations for the "genero" entity (genre).
 */
public class GeneroDAO {
    private final static String INSERT = "INSERT INTO genero (nombre) VALUES (?)";
    private final static String UPDATE = "UPDATE genero SET nombre = ? WHERE id = ?";
    private final static String DELETE = "DELETE FROM genero WHERE id = ?";
    private final static String SELECT_ALL = "SELECT * FROM genero";
    private final static String SELECT_BY_ID = "SELECT * FROM genero WHERE id = ?";
    private final static String SELECT_BY_NOMBRE = "SELECT * FROM genero WHERE nombre = ?";


    /**
     * Saves a new genre or updates an existing one in the database.
     *
     * @param genero The {@code Genero} entity to save or update.
     * @return The saved or updated {@code Genero} entity.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Genero save(Genero genero) throws SQLException {
        if (genero.getId() == 0) {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, genero.getNombre());
                statement.executeUpdate();


                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        genero.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(UPDATE)) {
                statement.setString(1, genero.getNombre());
                statement.setInt(2, genero.getId());
                statement.executeUpdate();
            }
        }
        return genero;
    }

    /**
     * Deletes a genre record from the database.
     *
     * @param genero The {@code Genero} entity to delete.
     * @return The deleted {@code Genero} entity, or {@code null} if the entity was invalid.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Genero delete(Genero genero) throws SQLException {
        if (genero == null || genero.getId() == 0) {
            genero = null;
        } else {
            try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(DELETE)) {
                statement.setInt(1, genero.getId());
                statement.executeUpdate();
            }
        }
        return genero;
    }

    /**
     * Retrieves a genre by its ID from the database.
     *
     * @param id The ID of the genre to retrieve.
     * @return The {@code Genero} entity with the specified ID, or {@code null} if not found.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Genero findById(int id) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToGenero(resultSet);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all genres from the database.
     *
     * @return A list of all {@code Genero} entities.
     * @throws SQLException If an error occurs during the database operation.
     */
    public List<Genero> findAll() throws SQLException {
        List<Genero> generos = new ArrayList<>();
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                generos.add(mapResultSetToGenero(resultSet));
            }
        }
        return generos;
    }

    /**
     * Retrieves a genre by its name from the database.
     *
     * @param id The name of the genre to retrieve.
     * @return The {@code Genero} entity with the specified name, or {@code null} if not found.
     * @throws SQLException If an error occurs during the database operation.
     */
    public Genero findByName(String id) throws SQLException {
        try (PreparedStatement statement = ConnectionXamp.getConnection().prepareStatement(SELECT_BY_NOMBRE)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToGenero(resultSet);
                }
            }
        }
        return null;
    }

    /**
     * Maps a {@code ResultSet} row to a {@code Genero} entity.
     *
     * @param resultSet The {@code ResultSet} containing the database row data.
     * @return A {@code Genero} entity mapped from the row.
     * @throws SQLException If an error occurs while accessing the {@code ResultSet}.
     */
    private Genero mapResultSetToGenero(ResultSet resultSet) throws SQLException {
        return new Genero(
                resultSet.getInt("id"),
                resultSet.getString("nombre")
        );
    }
}
