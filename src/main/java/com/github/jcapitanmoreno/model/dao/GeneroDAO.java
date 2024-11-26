package com.github.jcapitanmoreno.model.dao;

import com.github.jcapitanmoreno.model.connection.ConnectionXamp;
import com.github.jcapitanmoreno.model.entity.Genero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GeneroDAO {
    private final static String INSERT = "INSERT INTO genero (nombre) VALUES (?)";
    private final static String UPDATE = "UPDATE genero SET nombre = ? WHERE id = ?";
    private final static String DELETE = "DELETE FROM genero WHERE id = ?";
    private final static String SELECT_ALL = "SELECT * FROM genero";
    private final static String SELECT_BY_ID = "SELECT * FROM genero WHERE id = ?";

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
    private Genero mapResultSetToGenero(ResultSet resultSet) throws SQLException {
        return new Genero(
                resultSet.getInt("id"),
                resultSet.getString("nombre")
        );
    }
}
